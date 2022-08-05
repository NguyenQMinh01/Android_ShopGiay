package com.example.doan_n14;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_n14.adapter.SPGioHanggAdapter;
import com.example.doan_n14.event.EventSumTotal;
import com.example.doan_n14.model.Cart;
import com.example.doan_n14.model.GioHang;
import com.example.doan_n14.service.Retrofitclient;
import com.example.doan_n14.service.UserService;
import com.example.doan_n14.ultils.Ultils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GioHangActivity extends AppCompatActivity  {

    RecyclerView recyclerViewGioHang;
    Button buttonThanhToan;
    TextView txtTongTien, txtTrangThai;
    ArrayList<Cart> gioHangArrayList;
    ImageView imgXoaGioHang;
    SPGioHanggAdapter spGioHanggAdapter;
    FirebaseFirestore db;
    TextView txtTenSP;
    UserService service;
    int tong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        AnhXa();
        Retrofit retrofit = Retrofitclient.retrofit;  // Gọi class Retrofitclient (bên trang chính đã gọi getClient)
        service = retrofit.create(UserService.class);
        db = FirebaseFirestore.getInstance();
        gioHangArrayList = new ArrayList<>();
        spGioHanggAdapter = new SPGioHanggAdapter(Ultils.lstCart,getApplicationContext());


        XuLy();
        TongTien();

        //EvenChangeListener();

    }

    private void AnhXa()
    {
        buttonThanhToan = (Button) findViewById(R.id.buttonThanhToanGioHang);
        recyclerViewGioHang = (RecyclerView) findViewById(R.id.recyclerView_giohang);
        recyclerViewGioHang.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(this);
        recyclerViewGioHang.setLayoutManager(layoutManager);
        txtTrangThai = (TextView) findViewById(R.id.txtTrangThai);
        txtTongTien = (TextView) findViewById(R.id.txtTongTien);

        getSupportActionBar().setTitle("Giỏ hàng");

    }

    private void EvenChangeListener() {

        db.collection("giohang").orderBy("email", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Firestore error", error.getMessage());

                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() ==  DocumentChange.Type.ADDED)
                            {
                                gioHangArrayList.add(dc.getDocument().toObject(Cart.class));
                                //Utils.manggiohang.add(dc.getDocument().toObject(SanPham.class));
                            }

                            spGioHanggAdapter.notifyDataSetChanged();
                        }


                    }
                });
    }

    private void XuLy()
    {
        if(Ultils.lstCart.size() == 0){
            txtTrangThai.setVisibility(View.VISIBLE);
        }
        else{
            txtTrangThai.setVisibility(View.GONE);
            spGioHanggAdapter = new SPGioHanggAdapter(Ultils.lstCart,getApplicationContext());
            recyclerViewGioHang.setAdapter(spGioHanggAdapter);
        }
        buttonThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(),ThanhToanActivity.class);
                intent.putExtra("tongtien",tong);
                startActivity(intent);
            }
        });
    }

    private void TongTien()
    {
        tong = 0;
        for(int i =0; i < Ultils.lstCart.size(); i++)
        {
            tong += Integer.parseInt(Ultils.lstCart.get(i).getPrice()) * Integer.parseInt(Ultils.lstCart.get(i).getAmount());
        }
        txtTongTien.setText(String.valueOf(tong) +" VNĐ");
    }

    private void layGioHang(String email)
    {
        Call<List<GioHang>> giohang = service.getGioHang("");
        giohang.enqueue(new Callback<List<GioHang>>() {
            @Override
            public void onResponse(Call<List<GioHang>> call, Response<List<GioHang>> response) {
                gioHangArrayList.clear();
                for(GioHang G: response.body())
                {
                    if(G.getEmail().toLowerCase().contains(email.toLowerCase()))
                    {
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GioHang>> call, Throwable t) {

            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventSumTotal(EventSumTotal event)
    {
        if (event != null){
            TongTien();
            XuLy();
        }
    }


}