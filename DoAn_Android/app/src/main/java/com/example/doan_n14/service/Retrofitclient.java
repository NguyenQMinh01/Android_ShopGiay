package com.example.doan_n14.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofitclient {
    public static Retrofit retrofit= null;
    public  static Retrofit getClient()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://chettrongvovong.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
