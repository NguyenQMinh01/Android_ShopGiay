package com.example.doan_n14.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GioHang implements Serializable {

    @SerializedName("email")
    public String email;
    @SerializedName("address")
    public String address;
    @SerializedName("namekh")
    public String namekh;
    @SerializedName("phoneNumber")
    public String phoneNumber;
    @SerializedName("productList")
    public List<Cart> productList;
    @SerializedName("tongTien")
    public String tongTien;
    @SerializedName("ngaydat")
    public String ngaydat;
    @SerializedName("username")
    public String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GioHang(String address, String email, String namekh, String ngaydat, String phoneNumber, List<Cart> productList, String tongTien, String username)
    {
        this.address = address;
        this.email = email;
        this.namekh = namekh;
        this.phoneNumber = phoneNumber;
        this.productList = productList;
        this.tongTien = tongTien;
        this.ngaydat = ngaydat;
        this.username = username;
    }

    public String getNgaydat() {
        return ngaydat;
    }

    public void setNgaydat(String ngaydat) {
        this.ngaydat = ngaydat;
    }

    public String getTongTien() {
        return tongTien;
    }

    public void setTongTien(String tongTien) {
        this.tongTien = tongTien;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Cart> getProductList() {
        return productList;
    }

    public void setProductList(List<Cart> productList) {
        this.productList = productList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNamekh() {
        return namekh;
    }

    public void setNamekh(String namekh) {
        this.namekh = namekh;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public GioHang()
    {
    }

}
