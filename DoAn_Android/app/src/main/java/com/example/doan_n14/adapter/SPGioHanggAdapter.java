package com.example.doan_n14.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_n14.R;
import com.example.doan_n14.event.EventSumTotal;
import com.example.doan_n14.model.Cart;
import com.example.doan_n14.service.Retrofitclient;
import com.example.doan_n14.service.UserService;
import com.example.doan_n14.ultils.IImageClickListener;
import com.example.doan_n14.ultils.Ultils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class SPGioHanggAdapter extends RecyclerView.Adapter<SPGioHanggAdapter.MyViewHolder1>{
    Context context;
    ArrayList<Cart> arrayList;
    UserService service;

    public SPGioHanggAdapter(ArrayList<Cart> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context  =context;
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sanpham_giohang,parent,false);
        return new MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position) {

        Cart gioHang = arrayList.get(position);
        holder.namesp.setText(gioHang.getNamesp());
        holder.price.setText(gioHang.getPrice()+" VNĐ");
        holder.amount.setText(gioHang.getAmount()+ "");
        Glide.with(context).load(gioHang.getImages()).into(holder.images);

        holder.setiImageClickListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int value) {
                if(value == 1)
                {
                    if(Integer.parseInt( arrayList.get(pos).getAmount()) > 1)
                    {
                        int slnew= Integer.parseInt(arrayList.get(pos).getAmount()) -1;
                        arrayList.get(pos).setAmount(String.valueOf(slnew));

                        holder.amount.setText(arrayList.get(pos).getAmount()+"");
                    }
                    else if(Integer.parseInt( arrayList.get(pos).getAmount()) == 1)
                    {
                        AlertDialog.Builder builder =new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Ultils.lstCart.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new EventSumTotal());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                }

                else if (value ==2){
                    if (Integer.parseInt(arrayList.get(pos).getAmount()) < 10){
                        int slnew = Integer.parseInt(arrayList.get(pos).getAmount())+1;
                        arrayList.get(pos).setAmount(String.valueOf(slnew));
                    }
                    holder.amount.setText(arrayList.get(pos).getAmount()+ "");
                    EventBus.getDefault().postSticky(new EventSumTotal());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class MyViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namesp, price, amount;
        ImageView images,ibtnSub,ibtnAdd;
        IImageClickListener iImageClickListener;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            namesp= itemView.findViewById(R.id.textViewTenSPGioHang);
            price =itemView.findViewById(R.id.textViewGiaGioHang);
            images = itemView.findViewById(R.id.imageView_giohang);
            amount = itemView.findViewById(R.id.txtSoLuongGioHang);

            ibtnAdd = itemView.findViewById(R.id.ibtnAdd);
            ibtnSub = itemView.findViewById(R.id.ibtnSub);

            Retrofit retrofit = Retrofitclient.retrofit;  // Gọi class Retrofitclient (bên trang chính đã gọi getClient)
            service = retrofit.create(UserService.class);

            ibtnAdd.setOnClickListener(this);
            ibtnSub.setOnClickListener(this);

        }

        public void setiImageClickListener(IImageClickListener iImageClickListener) {
            this.iImageClickListener = iImageClickListener;
        }

        @Override
        public void onClick(View view) {
            if (view  == ibtnSub){
                iImageClickListener.onImageClick(view,getAdapterPosition(),1);
            }else if(view == ibtnAdd){
                iImageClickListener.onImageClick(view,getAdapterPosition(),2);
            }

        }
    }

}
