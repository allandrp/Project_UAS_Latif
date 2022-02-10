package com.example.project_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_uas.adapter.AdapterToko;
import com.example.project_uas.adapter.AdapterTokoDapur;
import com.example.project_uas.adapter.AdapterTokoElektronik;
import com.example.project_uas.model.Barang;
import com.example.project_uas.model.Cart;
import com.example.project_uas.model.Toko;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Toko_ElektronikActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,  AdapterTokoElektronik.ClickListenerElektronik{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageView imageEmail;
    TextView textEmail;
    FirebaseUser user;
    FirebaseAuth fbAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ListView listViewTokoElektronik;
    AdapterTokoElektronik adapterTokoElektronik;
    ArrayList<Barang> listBarangTokoElektronik;
    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko_elektronik);

        listBarangTokoElektronik = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_toko_elektronik);
        NavigationView navigationView = (NavigationView) findViewById(R.id.Nav_view_toko_elektronik);
        toolbar = findViewById(R.id.Toolbar_menu);
        fbAuth = FirebaseAuth.getInstance();
        user = fbAuth.getCurrentUser();

        fbDatabase = FirebaseDatabase.getInstance(getString(R.string.INSTANCE_FIREBASE));
        dbReference = fbDatabase.getReference();

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Navigation_Drawer_Open, R.string.Navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        imageEmail = headerView.findViewById(R.id.image_email);
        textEmail = headerView.findViewById(R.id.text_email);

        Glide.with(this).load(getUserPhotoUrl()).into(imageEmail);
        textEmail.setText(user.getEmail());
        setupListView();
    }

    private void setupListView() {
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance(getString(R.string.INSTANCE_FIREBASE));
        DatabaseReference dbReference = fbDatabase.getReference("toko").child("Toko Elektronik").child("barang");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Barang barangTemp = dataSnapshot.getValue(Barang.class);
                        listBarangTokoElektronik.add(barangTemp);
                    }
                    listViewTokoElektronik = findViewById(R.id.lv_toko_elektronik_list);
                    adapterTokoElektronik = new AdapterTokoElektronik(listBarangTokoElektronik, Toko_ElektronikActivity.this, Toko_ElektronikActivity.this);
                    listViewTokoElektronik.setAdapter(adapterTokoElektronik);
                    Log.d("TES_DATA_TOKO_DAPUR", listBarangTokoElektronik.toString());
                }else{
                    Log.d("TES_DATA_TOKO_DAPUR", "onDataChange: data tidak ada");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getUserPhotoUrl() {
        if (user != null && user.getPhotoUrl() != null) {
            return user.getPhotoUrl().toString();
        }
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Menu_home:
                Intent intentHome = new Intent(this, MainActivity.class);
                startActivity(intentHome);
                break;

            case R.id.Menu_toko_dapur:
                Intent intentTokoDapur = new Intent(this, Toko_DapurActivity.class);
                startActivity(intentTokoDapur);
                break;

            case R.id.Menu_toko_elektronik:
                break;

            case R.id.Menu_toko_mebel:
                Intent intentTokoMebel = new Intent(this, Toko_MebelActivity.class);
                startActivity(intentTokoMebel);
                break;

            case R.id.Menu_logout:
                fbAuth.signOut();
                mGoogleSignInClient.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        return true;
    }


    @Override
    public void onClickAddCart(int position) {
        Log.d("TES_CLICK_DAPUR_ADD", "onClickAddDapur: "+listBarangTokoElektronik.get(position).getNama());
        Query queryCart = dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(listBarangTokoElektronik.get(position).getNama());
        queryCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(Toko_ElektronikActivity.this, "Barang sudah ada di cart", Toast.LENGTH_SHORT).show();
                }else{
                    Cart cartTemp = new Cart(1, listBarangTokoElektronik.get(position).getHarga(), listBarangTokoElektronik.get(position).getNama());
                    dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(listBarangTokoElektronik.get(position).getNama()).setValue(cartTemp);
                    Toast.makeText(Toko_ElektronikActivity.this, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}