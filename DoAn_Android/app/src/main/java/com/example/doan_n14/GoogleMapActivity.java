package com.example.doan_n14;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapAPI = googleMap;
        //PolygonOptions polygonOptions = new PolygonOptions();
        //polygonOptions.add(new LatLng(10.806482655181629, 106.62803205213861))

        LatLng snearkerShop = new LatLng(10.806535348032709, 106.62871869763529);
        mapAPI.addMarker(new MarkerOptions().position(snearkerShop).title("SnearkerShop"));
        mapAPI.moveCamera(CameraUpdateFactory.newLatLng(snearkerShop));


        LatLng footShop = new LatLng(10.800846649425338, 106.6385391074237);
        mapAPI.addMarker(new MarkerOptions().position(footShop).title("FootShop"));
    }
}