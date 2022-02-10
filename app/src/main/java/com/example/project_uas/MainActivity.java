package com.example.project_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_uas.adapter.AdapterToko;
import com.example.project_uas.model.Barang;
import com.example.project_uas.model.Toko;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView imageEmail;
    TextView textEmail;
    FirebaseUser user;
    FirebaseAuth fbAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ListView listViewToko;
    AdapterToko adapterToko;
    ArrayList<Toko> listToko;
    ImageButton buttonCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listToko = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_main);
        navigationView = findViewById(R.id.nav_view_main);
        toolbar = findViewById(R.id.Toolbar_menu);
        buttonCart = findViewById(R.id.button_cart);
        buttonCart.setOnClickListener(this);

        fbAuth = FirebaseAuth.getInstance();
        user = fbAuth.getCurrentUser();

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Navigation_Drawer_Open, R.string.Navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        View headerView = navigationView.getHeaderView(0);

        imageEmail = headerView.findViewById(R.id.image_email);
        textEmail = headerView.findViewById(R.id.text_email);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("827736177418-g772mdo3g0lh4v026vbtne81i6toroa3.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Glide.with(this).load(getUserPhotoUrl()).into(imageEmail);
        textEmail.setText(user.getEmail());

        getDataToko();
    }

    private void listViewSetup(){
        listViewToko = findViewById(R.id.lv_toko_list);
        adapterToko = new AdapterToko(listToko, this);
        listViewToko.setAdapter(adapterToko);
    }

    private void getDataToko() {
        //buat data
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance("https://project-uas-b188b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference fbReference = fbDatabase.getReference("toko");

//        Toko toko1 = new Toko("Toko Elektronik", "elektronik", 47.5622, 13.6493);
//        Toko toko2 = new Toko("Toko Dapur", "dapur", 21.4082, -78.5684);
//        Toko toko3 = new Toko("Toko Mebel", "mebel", -13.1631, -72.545);
//        fbReference.child(toko1.getNama()).setValue(toko1);
//        fbReference.child(toko2.getNama()).setValue(toko2);
//        fbReference.child(toko3.getNama()).setValue(toko3);
//
//        Barang barang1 = new Barang("lemari", 500000.0, R.drawable.img_lemari);
//        Barang barang2 = new Barang("meja", 200000.0, R.drawable.img_meja);
//        Barang barang3 = new Barang("tempat tidur", 700000.0, R.drawable.img_tempat_tidur);
//        fbReference.child("Toko Mebel").child("barang").child(barang1.getNama()).setValue(barang1);
//        fbReference.child("Toko Mebel").child("barang").child(barang2.getNama()).setValue(barang2);
//        fbReference.child("Toko Mebel").child("barang").child(barang3.getNama()).setValue(barang3);
//
//        Barang barang4 = new Barang("blender", 150000.0, R.drawable.img_blender);
//        Barang barang5 = new Barang("mesin cuci", 250000.0, R.drawable.img_mesin_cuci);
//        Barang barang6 = new Barang("tv", 1000000.0, R.drawable.img_tv);
//        fbReference.child("Toko Elektronik").child("barang").child(barang4.getNama()).setValue(barang4);
//        fbReference.child("Toko Elektronik").child("barang").child(barang5.getNama()).setValue(barang5);
//        fbReference.child("Toko Elektronik").child("barang").child(barang6.getNama()).setValue(barang6);
//
//        Barang barang7 = new Barang("set makan", 1500000.0, R.drawable.img_set_makan);
//        Barang barang8 = new Barang("spatula", 40000.0, R.drawable.img_spatula);
//        Barang barang9 = new Barang("wajan", 20000.0, R.drawable.img_wajan);
//        fbReference.child("Toko Dapur").child("barang").child(barang7.getNama()).setValue(barang7);
//        fbReference.child("Toko Dapur").child("barang").child(barang8.getNama()).setValue(barang8);
//        fbReference.child("Toko Dapur").child("barang").child(barang9.getNama()).setValue(barang9);

        fbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Toko tokoTemp = dataSnapshot.getValue(Toko.class);
                        listToko.add(tokoTemp);
                        Log.d("TES_DATA_FIREBASE", "onDataChange: "+ tokoTemp.getNama());
                    }
                    listViewSetup();
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Menu_home:
                break;

            case R.id.Menu_toko_dapur:
                Intent intentTokoDapur = new Intent(this, Toko_DapurActivity.class);
                startActivity(intentTokoDapur);
                break;

            case R.id.Menu_toko_elektronik:
                Intent intentTokoElektronik = new Intent(this, Toko_ElektronikActivity.class);
                startActivity(intentTokoElektronik);
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
    protected void onResume() {
        super.onResume();
        drawerLayout.closeDrawers();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == buttonCart.getId()){
            Intent intentCart = new Intent(this, CartActivity.class);
            startActivity(intentCart);
        }
    }
}