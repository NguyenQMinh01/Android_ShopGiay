package com.example.doan_n14;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.doan_n14.adapter.PhotoAdapter;
import com.example.doan_n14.adapter.SanPhamAdapter;
import com.example.doan_n14.model.Photo;
import com.example.doan_n14.model.SanPham;
import com.example.doan_n14.service.Retrofitclient;
import com.example.doan_n14.service.UserService;
import com.example.doan_n14.ultils.Capture;
import com.example.doan_n14.ultils.Ultils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Retrofit;

public class TrangChinhActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    TextView txtEmailUser;
    ArrayList<SanPham> sanphamArrayList;
    SanPhamAdapter myAdapter;
    FirebaseFirestore db;
    public NavigationView navigationView;
    Button searchView, btnQRCode;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<Photo> photoList;
    private Timer timer;
    UserService service;
    SanPham sanPham;

    DangNhapActivity dangNhapActivity;



    public TrangChinhActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chinh);
        anhXa();
        autoSlideImage();
        setNavigationViewListener();

        Retrofitclient.getClient();
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setIcon(R.drawable.ic_baseline_search_24);


        Retrofit retrofit = Retrofitclient.retrofit;
        service = retrofit.create(UserService.class);

        db = FirebaseFirestore.getInstance();
        sanphamArrayList = new ArrayList<>();
        myAdapter = new SanPhamAdapter(sanphamArrayList,getApplicationContext());


        //Ultils.current_user = String.valueOf(findViewById(R.id.textViewEmaiuser));
        //Ultils.email_user = String.valueOf(findViewById(R.id.textViewEmaiuser));
        //final TextView txtname = (TextView) findViewById(R.id.txtView)

        //dangNhapActivity.edtEmail =  Ultils.current_user;



        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();


        myAdapter.setOnItemClickListenner(new SanPhamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SanPham sanPham;
                Intent intent = new Intent(TrangChinhActivity.this,DetailActivity.class);
                sanPham = sanphamArrayList.get(position);
                intent.putExtra("SanPham",sanPham);
                startActivity(intent);
            }
        });


        EvenChangeListener();
        CheckAmount();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(TrangChinhActivity.this,TimkiemActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnQRCode.setOnClickListener(v->
        {
            scanCode();
        });



    }

    private void anhXa()
    {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);

        photoList = getPhotoList();
        photoAdapter = new PhotoAdapter(this,photoList);
        viewPager.setAdapter(photoAdapter);
        btnQRCode = (Button) findViewById(R.id.btnQRCode);
        searchView = (Button) findViewById(R.id.searchView);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        recyclerView = findViewById(R.id.recyclerView_main);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager =new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);


        drawerLayout = findViewById(R.id.drawerLayout_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Ultils.lstCart == null)
        {
            Ultils.lstCart = new ArrayList<>();
        }else {
            int totalItem = 0;
            for (int i = 0; i < Ultils.lstCart.size(); i++) {
                totalItem += Integer.parseInt(Ultils.lstCart.get(i).getAmount());

            }
           //    badge.setText(String.valueOf(totalItem));
        }

    }

    private void CheckAmount()
    {
        for (int i =0; i < sanphamArrayList.size();i++)
        {
            if(Integer.parseInt( sanphamArrayList.get(i).getAmount()) == 0)
            {
                sanphamArrayList.remove(i);
            }
        }
    }



    private void EvenChangeListener() {

        String amuont;

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
                            CheckAmount();

                           myAdapter.notifyDataSetChanged();
                        }


                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.nav_trangchu:
                 intent = new Intent(TrangChinhActivity.this,TrangChinhActivity.class);
                 startActivity(intent);
                 break;
            case R.id.nav_timkiem:
                intent = new Intent(TrangChinhActivity.this,TimkiemActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_sanpham:
                intent = new Intent(TrangChinhActivity.this, SanphamActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_taikhoan:
                intent = new Intent(TrangChinhActivity.this, TaikhoanActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_giohang:
                intent = new Intent(TrangChinhActivity.this, GioHangActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_thongke:
                intent = new Intent(TrangChinhActivity.this,Thongke.class);
                startActivity(intent);
                break;
            case R.id.nav_ggmaps:
                intent = new Intent(TrangChinhActivity.this,GoogleMapActivity.class);
                startActivity(intent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setNavigationViewListener() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private List<Photo> getPhotoList()
    {
        List<Photo> photoList = new ArrayList<>();
        photoList.add(new Photo(R.mipmap.quangcao1));
        photoList.add(new Photo(R.mipmap.quangcao2));
        photoList.add(new Photo(R.mipmap.quangcao3));
        return photoList;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    private void autoSlideImage()
    {
        if(photoList == null || photoList.isEmpty() || viewPager == null)
        {
            return;
        }

        if(timer == null)
        {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = photoList.size()-1;
                        if (currentItem<totalItem)
                        {
                            currentItem ++;
                            viewPager.setCurrentItem(currentItem);
                        }
                        else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        },500,3000);
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(Capture.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher  = registerForActivityResult(new ScanContract(), result ->
    {
        if(result.getContents()!=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(TrangChinhActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }else{
            Toast.makeText(getApplicationContext(),"You didn't scan anything",Toast.LENGTH_SHORT).show();
        }
    });

}