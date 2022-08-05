package com.example.doan_n14;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_n14.adapter.ThongkeAdapter;
import com.example.doan_n14.model.GioHang;
import com.example.doan_n14.service.Retrofitclient;
import com.example.doan_n14.service.UserService;
import com.example.doan_n14.ultils.Ultils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Thongke extends AppCompatActivity {

    TextView totalQuantityStatistical, totalPriceStatistical;
    List<GioHang> orderList;
    List<GioHang> orders;
    RecyclerView recOrder;
    ThongkeAdapter thongkeAdapter;
    UserService userService;
    int total_QuantityStatistical = 0;
    int total_PriceStatistical = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        Anhxa();
        getSupportActionBar().setTitle("Thống Kê Hóa Đơn Bán");

        getStatistical();
    }


        private void getStatistical () {
            userService.all().enqueue(new Callback<List<GioHang>>() {

                @Override
                public void onResponse(Call<List<GioHang>> call, Response<List<GioHang>> response) {
                    orders = response.body();
                    for (GioHang d: orders){
                        if (d.getUsername().equalsIgnoreCase(Ultils.current_user.toLowerCase())){
                            orderList.add(d);
                            for(int i = 0; i < orders.size(); i++)
                            {
                                total_QuantityStatistical = orders.size();
                                total_PriceStatistical = total_PriceStatistical + Integer.parseInt(orders.get(i).getTongTien());
                            }

                        }
                    }

                    totalQuantityStatistical.setText(String.valueOf(total_QuantityStatistical));
                    totalPriceStatistical.setText(String.valueOf(total_PriceStatistical));
                    thongkeAdapter = new ThongkeAdapter(getApplicationContext(),orderList);
                    recOrder.setAdapter(thongkeAdapter);


                }

                @Override
                public void onFailure(Call<List<GioHang>> call, Throwable t) {

                }
            });


        }


                private void Anhxa () {
                    totalQuantityStatistical = (TextView) findViewById(R.id.txt_quantity_statistical);
                    totalPriceStatistical = (TextView) findViewById(R.id.txt_price_statistical);
                    recOrder = (RecyclerView) findViewById(R.id.recyclerView_statistical);
                    recOrder.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                    recOrder.setLayoutManager(layoutManager);
                    userService = Retrofitclient.getClient().create(UserService.class);
                    orders = new ArrayList<>();
                    orderList = new ArrayList<>();
                }




}