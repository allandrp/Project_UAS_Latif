package com.example.project_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_uas.adapter.AdapterToko;
import com.example.project_uas.model.Barang;
import com.example.project_uas.model.Event;
import com.example.project_uas.model.Toko;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

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
    Button buttonCheckEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listToko = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_main);
        navigationView = findViewById(R.id.nav_view_main);
        toolbar = findViewById(R.id.Toolbar_menu);
        buttonCheckEvent = findViewById(R.id.button_check_for_event);
        buttonCart = findViewById(R.id.button_cart);
        buttonCheckEvent.setOnClickListener(this);
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

    private void listViewSetup() {
        listViewToko = findViewById(R.id.lv_toko_list);
        adapterToko = new AdapterToko(listToko, this);
        listViewToko.setAdapter(adapterToko);
    }

    private void getDataToko() {
        //buat ambil data
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance(getString(R.string.INSTANCE_FIREBASE));
        DatabaseReference fbReference = fbDatabase.getReference("toko");



        fbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Toko tokoTemp = dataSnapshot.getValue(Toko.class);
                        listToko.add(tokoTemp);
                        Log.d("TES_DATA_FIREBASE", "onDataChange: " + tokoTemp.getNama());
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
        if (view.getId() == buttonCart.getId()) {
            Intent intentCart = new Intent(this, CartActivity.class);
            startActivity(intentCart);
        }

        if (view.getId() == buttonCheckEvent.getId()) {
            Intent intentEvent = new Intent(this, EventActivity.class);
            startActivity(intentEvent);
        }

    }

}