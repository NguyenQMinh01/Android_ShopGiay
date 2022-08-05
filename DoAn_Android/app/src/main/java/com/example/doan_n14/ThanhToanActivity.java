package com.example.doan_n14;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_n14.model.GioHang;
import com.example.doan_n14.service.Retrofitclient;
import com.example.doan_n14.service.UserService;
import com.example.doan_n14.ultils.Ultils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ThanhToanActivity extends AppCompatActivity {

    EditText txtname, txtemail, txtnumberphone, txtaddress;
    Button btnDathang;
    ArrayList<GioHang> gioHangArrayList;
    TextView txtTongThanhToan;
    UserService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        AnhXa();
        Retrofit retrofit = Retrofitclient.retrofit;  // Gọi class Retrofitclient (bên trang chính đã gọi getClient)
        service = retrofit.create(UserService.class); //Gọi interface
        XuLy();
    }

    private void XuLy() {
        btnDathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtname.getText().toString().trim();
                String email = txtemail.getText().toString().trim();
                String phone = txtnumberphone.getText().toString().trim();
                String address = txtaddress.getText().toString().trim();
                String tongtien = txtTongThanhToan.getText().toString().trim();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||TextUtils.isEmpty(phone) ||TextUtils.isEmpty(address))
                {
                    Toast.makeText(ThanhToanActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Date date  = Calendar.getInstance().getTime();
                    GioHang gioHang = new GioHang(address,email,name,date.toString(),phone, Ultils.lstCart,tongtien,Ultils.current_user);
                    Call<GioHang> gioHangCall =service.createGioHang(gioHang);
                    gioHangCall.enqueue(new Callback<GioHang>() {
                        @Override
                        public void onResponse(Call<GioHang> call, Response<GioHang> response) {
                            Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),TrangChinhActivity.class);
                            Ultils.lstCart.clear();
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<GioHang> call, Throwable t) {
                            Toast.makeText(ThanhToanActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),TrangChinhActivity.class);
                            Ultils.lstCart.clear();
                            startActivity(intent);
                            finish();
                        }
                    });

                }

            }
        });

        int tong =getIntent().getIntExtra("tongtien",0);
        txtTongThanhToan.setText(String.valueOf(tong));
    }

    private void AnhXa()
    {
        txtaddress = (EditText) findViewById(R.id.txtAddressTT);
        txtname =(EditText) findViewById(R.id.txtNameTT);
        txtnumberphone = (EditText) findViewById(R.id.txtPhoneTT);
        txtemail = (EditText) findViewById(R.id.txtEmailTT);
        btnDathang = (Button) findViewById(R.id.btnDatHang);
        txtTongThanhToan = (TextView) findViewById(R.id.txtTienThanhToan);
        gioHangArrayList = new ArrayList<>();

        getSupportActionBar().setTitle("Thanh Toán");
    }
}