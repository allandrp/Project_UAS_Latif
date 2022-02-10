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

public class AdapterCart extends ArrayAdapter<Cart> implements View.OnClickListener {

    private List<Cart> listBarang;
    private Context context;
    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    FirebaseAuth fbAuth;
    private ClickListener clickListener;

    public AdapterCart(List<Cart> listBarang, Context context, ClickListener listener) {
        super(context, R.layout.item_card_cart_list, listBarang);
        this.listBarang = listBarang;
        this.context = context;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listViewItem = inflater.inflate(R.layout.item_card_cart_list, null, true);

        //connect vew with variable
        TextView namaBarang = listViewItem.findViewById(R.id.tv_item_cart_name);
        TextView hargaBarang = listViewItem.findViewById(R.id.tv_item_cart_price);
        TextView itemCount = listViewItem.findViewById(R.id.tv_item_count);

        ImageView gambarBarang = listViewItem.findViewById(R.id.img_item_cart);
        ImageButton buttonAddCart = listViewItem.findViewById(R.id.button_add_cart);
        ImageButton buttonRemoveCart = listViewItem.findViewById(R.id.button_remove_cart);

        Cart cart = listBarang.get(position);

        namaBarang.setText(cart.getNama());
        hargaBarang.setText(String.valueOf(cart.getHarga()));
        itemCount.setText(String.valueOf(cart.getJumlahPesan()));

        gambarBarang.setImageResource(getImageResource(cart.getNama()));

        buttonAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClickAddCart(position);
            }
        });

        buttonRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClickRemoveCart(position);
            }
        });


        return listViewItem;
    }

    @Override
    public void onClick(View view) {

    }

    public interface ClickListener {
        void onClickAddCart(int position);

        void onClickRemoveCart(int position);
    }

    private int getImageResource(String nama) {
        switch (nama) {
            case "blender":
                return R.drawable.img_blender;

            case "lemari":
                return R.drawable.img_lemari;

            case "meja":
                return R.drawable.img_meja;

            case "mesin cuci":
                return R.drawable.img_mesin_cuci;

            case "set makan":
                return R.drawable.img_set_makan;

            case "spatula":
                return R.drawable.img_spatula;

            case "tempat tidur":
                return R.drawable.img_tempat_tidur;

            case "tv":
                return R.drawable.img_tv;

            case "wajan":
                return R.drawable.img_tv;

            default:
                return 0;
        }
    }
}
