package com.example.project_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.project_uas.model.Toko;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{
    private MapView mapView;
    private TextView namaToko;
    private Toko toko;
    FirebaseAuth fbAuth;
    FirebaseDatabase fbDatabase;
    DatabaseReference fbReference;
    private double latitude = 20, longitude = 100;
    private String namaTokoStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.mapView);
        namaToko = findViewById(R.id.maps_name);
        fbAuth = FirebaseAuth.getInstance();

        Intent intentToko = getIntent();
        namaToko.setText(intentToko.getStringExtra("namaToko"));

        fbDatabase = FirebaseDatabase.getInstance("https://project-uas-b188b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        fbReference = fbDatabase.getReference("toko").child(intentToko.getStringExtra("namaToko"));

        getDataToko();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void getDataToko(){
        fbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    toko = snapshot.getValue(Toko.class);
                    latitude = toko.getLatitude();
                    longitude = toko.getLogitude();
                    namaTokoStr = toko.getNama();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(namaTokoStr));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(13)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.getMaxZoomLevel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}