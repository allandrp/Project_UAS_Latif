package com.example.project_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.project_uas.adapter.AdapterCart;
import com.example.project_uas.model.Barang;
import com.example.project_uas.model.Cart;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterCart.ClickListener, View.OnClickListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageView imageEmail;
    TextView textEmail, textViewTotalHarga;
    FirebaseUser user;
    FirebaseAuth fbAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ListView listViewTokoCart;
    AdapterCart AdapterCart;
    ArrayList<Cart> listBarangCart;
    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    int totalHarga = 0;
    Button buttonBayar;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builderNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        createNotificationChannel();

        listBarangCart = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_cart);
        NavigationView navigationView = (NavigationView) findViewById(R.id.Nav_view_cart);
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
        textViewTotalHarga = findViewById(R.id.tv_total);
        buttonBayar = findViewById(R.id.button_bayar);
        buttonBayar.setOnClickListener(this);

        Glide.with(this).load(getUserPhotoUrl()).into(imageEmail);
        textEmail.setText(user.getEmail());
        getData();
    }

    private void getData() {
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance(getString(R.string.INSTANCE_FIREBASE));
        DatabaseReference dbReference = fbDatabase.getReference("cart").child(fbAuth.getCurrentUser().getUid());
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listBarangCart.clear();
                    totalHarga = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Cart barangTemp = dataSnapshot.getValue(Cart.class);
                        listBarangCart.add(barangTemp);
                        totalHarga = totalHarga + barangTemp.getHarga() * barangTemp.getJumlahPesan();
                    }

                } else {
                    finish();
                    Toast.makeText(CartActivity.this, "CART KOSONG", Toast.LENGTH_SHORT).show();
                }
                textViewTotalHarga.setText(String.valueOf(totalHarga));
                setupListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupListView() {
        listViewTokoCart = findViewById(R.id.lv_cart_list);
        AdapterCart = new AdapterCart(listBarangCart, CartActivity.this, CartActivity.this);
        listViewTokoCart.setAdapter(AdapterCart);
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
                Intent intentTokoCart = new Intent(this, Toko_ElektronikActivity.class);
                startActivity(intentTokoCart);
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
        Cart barang = listBarangCart.get(position);
        listBarangCart.get(position).setJumlahPesan(barang.getJumlahPesan() + 1);
        barang = listBarangCart.get(position);

        dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(barang.getNama()).setValue(barang).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ((BaseAdapter) listViewTokoCart.getAdapter()).notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onClickRemoveCart(int position) {
        Cart barang = listBarangCart.get(position);
        listBarangCart.get(position).setJumlahPesan(barang.getJumlahPesan() - 1);

        barang = listBarangCart.get(position);

        if (barang.getJumlahPesan() > 0) {
            dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(barang.getNama()).setValue(barang).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ((BaseAdapter) listViewTokoCart.getAdapter()).notifyDataSetChanged();
                }
            });

        } else {
            dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).child(barang.getNama()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    }
                }
            });
            listBarangCart.remove(position);
            ((BaseAdapter) listViewTokoCart.getAdapter()).notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == buttonBayar.getId()) {
            dbReference.child("cart").child(fbAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //membuat notifikasi
                    builderNotification = new NotificationCompat.Builder(CartActivity.this, "TokoOnline")
                            .setSmallIcon(R.drawable.ic_menu)
                            .setContentTitle("Toko Online")
                            .setContentText("segera lakukan pembayaran sejumlah "+ totalHarga)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    notificationManager = NotificationManagerCompat.from(CartActivity.this);
                    notificationManager.notify(1, builderNotification.build());
                }
            });
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Pembayaran";
            String description = "Channel untuk melakukan pembayaran";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("TokoOnline", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}