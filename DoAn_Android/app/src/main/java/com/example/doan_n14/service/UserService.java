package com.example.doan_n14.service;

import com.example.doan_n14.model.GioHang;
import com.example.doan_n14.model.SanPham;
import com.example.doan_n14.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @GET("api/products")
    Call<List<User>> hello();

    @GET("api/user/{email}")
    Call<User> get (@Path("email")String email);

    @POST("api/user")
    Call<User> create(@Body User user);

    @GET("api/sanpham")
    Call<List<SanPham>> searchSanPham();

    @POST("api/giohang")
    Call<GioHang> createGioHang(@Body GioHang giohang);

    @GET("api/giohang/{email}")
    Call<List<GioHang>> getGioHang(@Path("email") String email);

    @GET("api/sanpham")
    Call<List<SanPham>> phanLoaiSP();

    @GET("api/sanpham")
    Call<List<SanPham>> getAmount();

    @GET("api/giohang")
    Call<List<GioHang>> all();




}
