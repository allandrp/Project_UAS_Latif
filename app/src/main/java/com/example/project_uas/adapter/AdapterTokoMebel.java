package com.example.project_uas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.project_uas.R;
import com.example.project_uas.model.Barang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class AdapterTokoMebel extends ArrayAdapter<Barang> implements View.OnClickListener {

    private List<Barang> listBarang;
    private Context context;
    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    FirebaseAuth fbAuth;
    private ClickListener clickListener;

    public AdapterTokoMebel(List<Barang>listBarang, Context context, ClickListener listener) {
        super(context, R.layout.item_card_toko_mebel_list, listBarang);
        this.listBarang = listBarang;
        this.context = context;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listViewItem = inflater.inflate(R.layout.item_card_toko_mebel_list, null, true);

        //connect vew with variable
        TextView namaBarang = listViewItem.findViewById(R.id.tv_item_mebel_name);
        TextView hargaBarang = listViewItem.findViewById(R.id.tv_item_mebel_price);

        ImageView gambarBarang = listViewItem.findViewById(R.id.img_item_mebel);
        Button buttonAddToCart = listViewItem.findViewById(R.id.add_barang_mebel);

        Barang barang = listBarang.get(position);
        namaBarang.setText(barang.getNama());
        hargaBarang.setText(String.valueOf(barang.getHarga()));

        gambarBarang.setImageResource(barang.getImageResource());

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
    public interface ClickListener{
        void onClickAddCart(int position);
    }
}
