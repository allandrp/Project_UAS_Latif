package com.example.project_uas.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project_uas.MainActivity;
import com.example.project_uas.MapsActivity;
import com.example.project_uas.R;
import com.example.project_uas.Toko_DapurActivity;
import com.example.project_uas.Toko_ElektronikActivity;
import com.example.project_uas.Toko_MebelActivity;
import com.example.project_uas.model.Toko;

import java.util.List;

public class AdapterToko extends ArrayAdapter<Toko> implements View.OnClickListener {

    private List<Toko> listToko;
    private Context context;

    public AdapterToko(List<Toko>listToko, Context context) {
        super(context, R.layout.item_card_toko_list, listToko);
        this.listToko = listToko;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listViewItem = inflater.inflate(R.layout.item_card_toko_list, null, true);

        //connect vew with variable
        TextView namaToko = listViewItem.findViewById(R.id.tv_toko_name);
        ImageButton tombolMap = listViewItem.findViewById(R.id.button_maps);
        RelativeLayout relativeLayoutDetail = listViewItem.findViewById(R.id.relative_layout_check_for_details);

        Toko toko = listToko.get(position);
        namaToko.setText(toko.getNama());

        tombolMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMap = new Intent(context, MapsActivity.class);
                intentMap.putExtra("namaToko", toko.getNama());
                context.startActivity(intentMap);
            }
        });

        namaToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (toko.getJenisToko()){
                    case "elektronik":
                        Intent intentElektronik = new Intent(context, Toko_ElektronikActivity.class);
                        intentElektronik.putExtra("namaToko", toko.getNama());
                        context.startActivity(intentElektronik);
                        break;

                    case "mebel":
                        Intent intentMebel = new Intent(context, Toko_MebelActivity.class);
                        intentMebel.putExtra("namaToko", toko.getNama());
                        context.startActivity(intentMebel);
                        break;

                    case "dapur":
                        Intent intentDapur = new Intent(context, Toko_DapurActivity.class);
                        intentDapur.putExtra("namaToko", toko.getNama());
                        context.startActivity(intentDapur);
                        break;

                }
            }
        });

        relativeLayoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (toko.getJenisToko()){
                    case "elektronik":
                        Intent intentElektronik = new Intent(context, Toko_ElektronikActivity.class);
                        intentElektronik.putExtra("namaToko", toko.getNama());
                        context.startActivity(intentElektronik);
                        break;

                    case "mebel":
                        Intent intentMebel = new Intent(context, Toko_MebelActivity.class);
                        intentMebel.putExtra("namaToko", toko.getNama());
                        context.startActivity(intentMebel);
                        break;

                    case "dapur":
                        Intent intentDapur = new Intent(context, Toko_DapurActivity.class);
                        intentDapur.putExtra("namaToko", toko.getNama());
                        context.startActivity(intentDapur);
                        break;

                }
            }
        });



        return  listViewItem;
    }

    @Override
    public void onClick(View view) {

    }
}
