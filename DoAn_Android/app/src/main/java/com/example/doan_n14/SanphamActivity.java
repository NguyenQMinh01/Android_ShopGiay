package com.example.doan_n14;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_n14.adapter.SanPhamAdapter;
import com.example.doan_n14.model.SanPham;
import com.example.doan_n14.service.Retrofitclient;
import com.example.doan_n14.service.UserService;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SanphamActivity extends AppCompatActivity {

    ArrayList<SanPham> sanphamArrayList;
    Button btnTrangChu, btnVans, btnNike, btnNew, btnBalenciaga;
    RecyclerView recyclerViewSanpham;
    FirebaseFirestore db;
    SanPhamAdapter myAdapter;
    UserService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanpham);
        AnhXa();


        Retrofit retrofit = Retrofitclient.retrofit;  // Gọi class Retrofitclient (bên trang chính đã gọi getClient)
        service = retrofit.create(UserService.class);


        db = FirebaseFirestore.getInstance();
        sanphamArrayList = new ArrayList<>();
        myAdapter = new SanPhamAdapter(sanphamArrayList,getApplicationContext());

        recyclerViewSanpham.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager =new GridLayoutManager(this,2);
        recyclerViewSanpham.setLayoutManager(gridLayoutManager);

        recyclerViewSanpham.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();




        XuLySuKien();
        XuLyDetail();
        EvenChangeListener();

    }

    private void AnhXa()
    {
        btnTrangChu = (Button) findViewById(R.id.btnTrangchu);
        recyclerViewSanpham = (RecyclerView) findViewById(R.id.recyclerView_sanpham);
        btnVans = (Button) findViewById(R.id.btnVans);
        btnNike = (Button) findViewById(R.id.btnNike);
        btnNew = (Button) findViewById(R.id.btnNew);
        btnBalenciaga = (Button) findViewById(R.id.btnBalenciaga);

        getSupportActionBar().setTitle("Phân loại sản phẩm");
    }

    private void XuLySuKien()
    {

        btnTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SanphamActivity.this, TrangChinhActivity.class);
                startActivity(intent);
            }
        });

        btnVans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                phanLoai("Vans");
                if(sanphamArrayList.isEmpty())
                {
                    Toast.makeText(SanphamActivity.this, "Phân loại không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phanLoai("Nike");
                if(sanphamArrayList.isEmpty())
                {
                    Toast.makeText(SanphamActivity.this, "Phân loại không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phanLoai("New");
                if(sanphamArrayList.isEmpty())
                {
                    Toast.makeText(SanphamActivity.this, "Phân loại không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBalenciaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phanLoai("Balenciaga");
                if(sanphamArrayList.isEmpty())
                {
                    Toast.makeText(SanphamActivity.this, "Phân loại không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void XuLyDetail()
    {
        recyclerViewSanpham.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        myAdapter.setOnItemClickListenner(new SanPhamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SanPham sanPham;
                Intent intent = new Intent(SanphamActivity.this,DetailActivity.class);
                sanPham = sanphamArrayList.get(position);
                intent.putExtra("SanPham",sanPham);
                startActivity(intent);
            }
        });
    }

    private void EvenChangeListener() {

        db.collection("sanpham").orderBy("namesp", Query.Direction.ASCENDING)
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
                                sanphamArrayList.add(dc.getDocument().toObject(SanPham.class));
                            }

                            myAdapter.notifyDataSetChanged();
                        }


                    }
                });
    }

    private void phanLoai(String text)
    {
        Call<List<SanPham>> sanpham = service.phanLoaiSP();
        sanpham.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanphamArrayList.clear();
                for(SanPham S: response.body())
                {
                    if(S.getType().toLowerCase().contains(text.toLowerCase()))
                        sanphamArrayList.add(new SanPham(S.getNamesp(), S.getImages(), S.getPrice(), S.getAmount()));
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {
                Toast.makeText(SanphamActivity.this, "Lỗi phân loại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}