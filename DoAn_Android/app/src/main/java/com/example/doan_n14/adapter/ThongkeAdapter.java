package com.example.doan_n14.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_n14.R;
import com.example.doan_n14.model.GioHang;

import java.util.List;

public class ThongkeAdapter extends RecyclerView.Adapter<ThongkeAdapter.MyViewHolder1> {

    private Context context;
    private List<GioHang> orderList;



    public ThongkeAdapter(Context context, List<GioHang> orderList)
    {
        this.context = context;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public ThongkeAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_thongke,parent, false);

        return new ThongkeAdapter.MyViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongkeAdapter.MyViewHolder1 holder, int position) {

        GioHang order = orderList.get(position);
        holder.namekh.setText(order.getNamekh());
        holder.ngaydat.setText(order.getNgaydat());
        holder.tongtien.setText(order.getTongTien());
    }



    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public static class MyViewHolder1 extends RecyclerView.ViewHolder {

        TextView namekh, ngaydat, tongtien;
        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            namekh = itemView.findViewById(R.id.txt_id);
            ngaydat = itemView.findViewById(R.id.txt_date);
            tongtien = itemView.findViewById(R.id.txt_price);

        }
    }
}
