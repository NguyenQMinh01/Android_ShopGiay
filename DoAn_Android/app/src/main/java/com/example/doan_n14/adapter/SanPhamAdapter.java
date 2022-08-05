package com.example.doan_n14.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_n14.R;
import com.example.doan_n14.model.SanPham;

import java.util.ArrayList;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.MyViewHodel>  {
    Context context;
    private OnItemClickListener mListener;

    public SanPhamAdapter(ArrayList<SanPham> sanPhamArrayList, Context applicationContext) {
        this.context = applicationContext;
        this.sanPhamArrayList =sanPhamArrayList;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public  void setOnItemClickListenner(OnItemClickListener listenner){
        mListener = listenner;
    }


    ArrayList<SanPham> sanPhamArrayList;

    @NonNull
    @Override
    public MyViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_recyclerview,parent,false);

        return new MyViewHodel(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamAdapter.MyViewHodel hodel, int position)
    {
        SanPham sanPham = sanPhamArrayList.get(position);
        hodel.namesp.setText(sanPham.getNamesp());
        hodel.description.setText(sanPham.getPrice() + " VNĐ");
        hodel.amount.setText(sanPham.getAmount());
        Glide.with(context).load(sanPhamArrayList.get(position).getImages()).into(hodel.images);
    }

    @Override
    public int getItemCount() {
        if(sanPhamArrayList != null) {
            return sanPhamArrayList.size();
        }
        return  0;
    }


    public  static  class  MyViewHodel extends  RecyclerView.ViewHolder{

        TextView namesp,description, amount;
        ImageView images;

        public MyViewHodel(@NonNull View itemView,final OnItemClickListener listenner) {
            super(itemView);

            namesp = itemView.findViewById(R.id.textView);
            description = itemView.findViewById(R.id.textView2);
            images = itemView.findViewById(R.id.imageView);
            amount = itemView.findViewById(R.id.textViewSoLuongSanPham);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenner != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listenner.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}

