package com.example.doan_n14;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_n14.model.Cart;
import com.example.doan_n14.model.SanPham;
import com.example.doan_n14.service.Retrofitclient;
import com.example.doan_n14.service.UserService;
import com.example.doan_n14.ultils.Ultils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {

    ImageView imgHinhDetail, imgBack;
    TextView txtGia, txtTenSP, txtMoTa, txtSoLuong, txtSoLuongSP;
    ImageButton  ibtnCong, ibtnTru, ibtnCart;
    Button btnThemGioHang;
    NavigationView navigationView;
    SanPham product;
    FirebaseAuth mAuth;
    UserService service;
    private FirebaseFirestore db;
    private FirebaseUser user;
    String userID;
    NotificationBadge badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        AnhXa();
        Retrofit retrofit = Retrofitclient.retrofit;  // Gọi class Retrofitclient (bên trang chính đã gọi getClient)
        service = retrofit.create(UserService.class); //Gọi interface
        loadDataIntent();
        loadSoLuong();
        XuLyButton();
        mAuth = FirebaseAuth.getInstance();

    }

    private void XuLyButton()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),TrangChinhActivity.class));


            }
        });
        btnThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(txtSoLuong.getText().toString()) == 0)
                {
                    Toast.makeText(DetailActivity.this, "Bạn chưa nhập số lượng", Toast.LENGTH_SHORT).show();
                }
                addtoCart();

            }
        });

        ibtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, GioHangActivity.class);
                startActivity(intent);
            }
        });
    }


    private void addtoCart() {
        if (Ultils.lstCart.size() > 0) {
            boolean flag = false;
            int sl = Integer.parseInt(txtSoLuong.getText().toString());
            for (int i = 0; i < Ultils.lstCart.size(); i++) {
                if (Ultils.lstCart.get(i).getNamesp() == product.getNamesp()) {
                    int slsp = sl + Integer.parseInt(Ultils.lstCart.get(i).getAmount());
                    Ultils.lstCart.get(i).setAmount(String.valueOf(slsp));
                    int price = Integer.parseInt(product.getPrice()) * Integer.parseInt(txtSoLuong.getText().toString());
                    Ultils.lstCart.get(i).setPrice(String.valueOf(price));
                    flag = true;
                }
            }
            if (flag==false){
                int price =Integer.parseInt(product.getPrice()) * sl;
                Cart cart = new Cart();
                cart.setNamesp(product.getNamesp());
                cart.setPrice(String.valueOf(price));
                cart.setImages(product.getImages());
                cart.setAmount(String.valueOf(sl));
                Ultils.lstCart.add(cart);
            }
        }
        else {
            int sl = Integer.parseInt(txtSoLuong.getText().toString());
            int price =Integer.parseInt(product.getPrice()) * sl;
            Cart cart = new Cart();
            cart.setNamesp(product.getNamesp());
            cart.setPrice(String.valueOf(price));
            cart.setImages(product.getImages());
            cart.setAmount(String.valueOf(sl));
            Ultils.lstCart.add(cart);
        }
       int totalItem = 0;
        if(Integer.parseInt(badge.getTextView().getText().toString()) == 0)
        {
            for (int i = 0; i < Ultils.lstCart.size(); i++) {
                totalItem += Integer.parseInt(Ultils.lstCart.get(i).getAmount());
            }
        }
        else {
            totalItem = Integer.parseInt(badge.getTextView().getText().toString()) + Integer.parseInt(txtSoLuong.getText().toString());
        }
        badge.setText(String.valueOf(totalItem));

    }

    private void AnhXa()
    {
        imgHinhDetail = (ImageView) findViewById(R.id.imageView_detail);
        imgBack = (ImageView) findViewById(R.id.imageView_back);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGia = (TextView) findViewById(R.id.textViewDetail_giasanpham);
        txtTenSP = (TextView) findViewById(R.id.textViewDetail_tensanpham);
        txtMoTa = (TextView) findViewById(R.id.textViewDetail_motasanpham);
        btnThemGioHang = (Button) findViewById(R.id.btnThemvaogiohang);
        ibtnCong = (ImageButton) findViewById(R.id.btnCong);
        ibtnTru = (ImageButton) findViewById(R.id.btnTru);
        txtSoLuong = (TextView) findViewById(R.id.textView_Soluong);
        badge = (NotificationBadge) findViewById(R.id.badge_soluong);
        ibtnCart = (ImageButton) findViewById(R.id.ibtn_Cart);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        getSupportActionBar().setTitle("Chi tiết sản phẩm");

    }

    private void loadSoLuong()
    {
        ibtnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(txtSoLuong.getText().toString()) == 10) {
                    Toast.makeText(DetailActivity.this, "Lưu ý: Không thể mua quá 10 sản phẩm trong một lần", Toast.LENGTH_SHORT).show();
                    return;}
                else if(Integer.parseInt(txtSoLuong.getText().toString()) >= Integer.parseInt(txtSoLuongSP.getText().toString()))
                {
                    Toast.makeText(DetailActivity.this, "Số lượng bị giới hạn", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    int soluong = Integer.parseInt(txtSoLuong.getText().toString());
                    soluong = soluong + 1;
                    txtSoLuong.setText(soluong + "");
                }

            }
        });

        ibtnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(txtSoLuong.getText().toString()) == 0) {
                    return;}
                else {
                    int soluong = Integer.parseInt(txtSoLuong.getText().toString());
                    soluong = soluong - 1;
                    txtSoLuong.setText(soluong + "");
                }
            }
        });

        if(Ultils.lstCart != null)
        {
            int totalItem = 0;
            for(int i = 0; i < Ultils.lstCart.size(); i++ )
            {
                totalItem += Integer.parseInt(Ultils.lstCart.get(i).getAmount());
            }
            badge.setText(String.valueOf(totalItem));
        }


    }

    private void loadDataIntent()
    {
        product = (SanPham)getIntent().getSerializableExtra("SanPham");
        txtGia.setText(product.getPrice() +" VNĐ");
        txtTenSP.setText(product.getNamesp());
        txtSoLuongSP.setText(product.getAmount());
        Picasso.with(this)
                .load(product.getImages())
                .into(imgHinhDetail);
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (Ultils.lstCart!=null){
            int totalItem = 0;
            for (int i = 0; i < Ultils.lstCart.size(); i++) {
                totalItem += Integer.parseInt(Ultils.lstCart.get(i).getAmount());
            }
            badge.setText(String.valueOf(totalItem));
        }
    }

}
