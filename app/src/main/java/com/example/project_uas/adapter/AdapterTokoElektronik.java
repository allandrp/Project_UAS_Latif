package com.example.project_uas.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.project_uas.MapsActivity;
import com.example.project_uas.R;
import com.example.project_uas.Toko_DapurActivity;
import com.example.project_uas.Toko_ElektronikActivity;
import com.example.project_uas.Toko_MebelActivity;
import com.example.project_uas.model.Barang;
import com.example.project_uas.model.Cart;
import com.example.project_uas.model.Toko;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterTokoElektronik extends ArrayAdapter<Barang> implements View.OnClickListener {

    private List<Barang> listBarang;
    private Context context;
    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    FirebaseAuth fbAuth;
    private ClickListenerElektronik clickListener;

    public AdapterTokoElektronik(List<Barang>listBarang, Context context, ClickListenerElektronik listener) {
        super(context, R.layout.item_card_toko_elektronik_list, listBarang);
        this.listBarang = listBarang;
        this.context = context;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listViewItem = inflater.inflate(R.layout.item_card_toko_elektronik_list, null, true);

        //connect vew with variable
        TextView namaBarang = listViewItem.findViewById(R.id.tv_item_elektronik_name);
        TextView hargaBarang = listViewItem.findViewById(R.id.tv_item_elektronik_price);

        ImageView gambarBarang = listViewItem.findViewById(R.id.img_item_elektronik);
        Button buttonAddToCart = listViewItem.findViewById(R.id.add_barang_elektronik);

        Barang barang = listBarang.get(position);
        namaBarang.setText(barang.getNama());
        hargaBarang.setText(String.valueOf(barang.getHarga()));

        gambarBarang.setImageResource(barang.getImageResource());

        fbAuth = FirebaseAuth.getInstance();
        fbDatabase = FirebaseDatabase.getInstance(context.getString(R.string.INSTANCE_FIREBASE));
        FirebaseUser user = fbAuth.getCurrentUser();
        dbReference = fbDatabase.getReference("cart").child(user.getUid());
        Query queryCart = dbReference.child(barang.getNama());

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClickAddCart(position);
            }
        });


        return  listViewItem;
    }

    @Override
    public void onClick(View view) {

    }
    public interface ClickListenerElektronik {
        void onClickAddCart(int position);
    }
}
