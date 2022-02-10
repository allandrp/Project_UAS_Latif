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

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.project_uas.adapter.AdapterTokoMebel;
import com.example.project_uas.model.Barang;
import com.example.project_uas.model.Cart;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Toko_MebelActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,  AdapterTokoMebel.ClickListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageView imageEmail;
    TextView textEmail;
    FirebaseUser user;
    FirebaseAuth fbAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ListView listViewTokomebel;
    AdapterTokoMebel adapterTokomebel;
    ArrayList<Barang> listBarangTokoMebel;
    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko_mebel);

        listBarangTokoMebel = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_toko_mebel);
        NavigationView navigationView = (NavigationView) findViewById(R.id.Nav_view_toko_mebel);
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
        DatabaseReference dbReference = fbDatabase.getReference("toko").child("Toko Mebel").child("barang");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Barang barangTemp = dataSnapshot.getValue(Barang.class);
                        listBarangTokoMebel.add(barangTemp);
                    }
                    listViewTokomebel = findViewById(R.id.lv_toko_mebel_list);
                    adapterTokomebel = new AdapterTokoMebel(listBarangTokoMebel, Toko_MebelActivity.this, Toko_MebelActivity.this);
                    listViewTokomebel.setAdapter(adapterTokomebel);
                    Log.d("TES_DATA_TOKO_DAPUR", listBarangTokoMebel.toString());
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

            case R.id.Menu_toko_mebel:
                break;

            case R.id.Menu_toko_elektronik:
                Intent intentTokoMebel = new Intent(this, Toko_ElektronikActivity.class);
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
        Log.d("TES_CLICK_DAPUR_ADD", "onClickAddDapur: "+listBarangTokoMebel.get(position).getNama());
        Query queryCart = dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(listBarangTokoMebel.get(position).getNama());
        queryCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(Toko_MebelActivity.this, "Barang sudah ada di cart", Toast.LENGTH_SHORT).show();
                }else{
                    Cart cartTemp = new Cart(1, listBarangTokoMebel.get(position).getHarga(), listBarangTokoMebel.get(position).getNama());
                    dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(listBarangTokoMebel.get(position).getNama()).setValue(cartTemp);
                    Toast.makeText(Toko_MebelActivity.this, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}