package com.example.doan_n14.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SanPham implements Serializable {

    @SerializedName("namesp")
    String namesp;

    @SerializedName("images")
    String images;

    @SerializedName("description")
    String price;

    @SerializedName("amount")
    String amount;

    @SerializedName("type")
    String type;



    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public SanPham(String namesp , String images, String price, String amount, String type)
    {
        this.namesp = namesp;
        this.price = price;
        this.images = images;
        this.amount = amount;
        this.type = type;
    }

    public SanPham(String namesp , String images, String price, String amount)
    {
        this.namesp = namesp;
        this.price = price;
        this.images = images;
        this.amount = amount;
    }

    public String getNamesp() {
        return namesp;
    }

    public SanPham()
    {
    }

    public void setNamesp(String namesp) {
        this.namesp = namesp;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
