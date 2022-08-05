package com.example.doan_n14;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_n14.adapter.SanPhamAdapter;
import com.example.doan_n14.model.SanPham;
import com.example.doan_n14.service.Retrofitclient;
import com.example.doan_n14.service.UserService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TimkiemActivity extends AppCompatActivity {
    com.airbnb.lottie.LottieAnimationView lottieAnimationView;
    SearchView searchView;
    RecyclerView recyclerViewSP;
    TagGroup tagGroup;
    TextView txtKhongTimThay;
    ImageButton imgQuayLai;
    SanPhamAdapter sanPhamAdapter;
    FirebaseFirestore db;
    ArrayList<SanPham> sanPhamArrayList;
    DatabaseReference databaseReference;
    ArrayList<String> tag;
    UserService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timkiem);
        AnhXa();
        tag=new ArrayList<>();

        getSupportActionBar().setTitle("Tìm kiếm sản phẩm");


        Retrofit retrofit = Retrofitclient.retrofit;  // Gọi class Retrofitclient (bên trang chính đã gọi getClient)
        service = retrofit.create(UserService.class); //Gọi interface



        searchView.clearFocus();
        recyclerViewSP.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerViewSP.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        sanPhamArrayList = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(sanPhamArrayList, getApplicationContext());


        recyclerViewSP.setAdapter(sanPhamAdapter);
        sanPhamAdapter.notifyDataSetChanged();

        chuyenItemDetail();
        EvenChangeListener();

        //Search();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                tag.add(s);
                tagGroup.setTags(tag);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                timKiemSP(newText);
                if(sanPhamArrayList.isEmpty())
                {
                }
                return false;
            }
        });

        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                sanPhamArrayList.clear();
                sanPhamAdapter.notifyDataSetChanged();
                searchView.setQuery(tag,false);
                hideSoftKeyboard(searchView);
            }
        });



        imgQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TimkiemActivity.this, TrangChinhActivity.class));
                finish();
            }
        });




    }


    private void AnhXa() {
        searchView = (SearchView) findViewById(R.id.search_view);
        recyclerViewSP = (RecyclerView) findViewById(R.id.recycleViewSP);
        tagGroup = (TagGroup) findViewById(R.id.tag_group);
        txtKhongTimThay = (TextView) findViewById(R.id.txtKhongTimThay);
        imgQuayLai = (ImageButton) findViewById(R.id.imageView_quaylai);
        lottieAnimationView = findViewById(R.id.lottieKhongTimThay);

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
                                sanPhamArrayList.add(dc.getDocument().toObject(SanPham.class));
                            }

                            sanPhamAdapter.notifyDataSetChanged();
                        }


                    }
                });
    }

    private void chuyenItemDetail (){
        sanPhamAdapter.setOnItemClickListenner(new SanPhamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SanPham sanPham;
                Intent intent = new Intent(TimkiemActivity.this,DetailActivity.class);
                sanPham = sanPhamArrayList.get(position);
                intent.putExtra("TENSP",sanPham.getNamesp());
                intent.putExtra("HINH",sanPham.getImages());
                intent.putExtra("GIA",sanPham.getPrice());
                startActivity(intent);
            }
        });
    }

    private void timKiemSP(String text)
    {
        Call<List<SanPham>> sanpham = service.searchSanPham();

        sanpham.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhamArrayList.clear();

                Log.e("TAG", "onResponse: "+ response.body().size());
                for(SanPham S: response.body())
                {
                    if(S.getNamesp().toLowerCase().contains(text.toLowerCase()))
                        sanPhamArrayList.add(new SanPham(S.getNamesp(), S.getImages(), S.getPrice(), S.getAmount()));
                }
                sanPhamAdapter.notifyDataSetChanged();

                sanPhamAdapter.setOnItemClickListenner(new SanPhamAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        SanPham sanPham;
                        Intent intent = new Intent(TimkiemActivity.this,DetailActivity.class);
                        sanPham = sanPhamArrayList.get(position);
                        intent.putExtra("TENSP",sanPham.getNamesp());
                        intent.putExtra("HINH",sanPham.getImages());
                        intent.putExtra("GIA",sanPham.getPrice());
                        startActivity(intent);
                        finish();
                    }
                });
            }
            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {
                Toast.makeText(TimkiemActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                Log.d("SSS",t.toString());
            }
        });
    }


    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
