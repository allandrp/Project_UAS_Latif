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
import com.example.project_uas.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class AdapterEvent extends ArrayAdapter<Event> implements View.OnClickListener {

    private List<Event> listEvent;
    private Context context;
    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    FirebaseAuth fbAuth;
    private ClickListener clickListener;

    public AdapterEvent(List<Event>listEvent, Context context, ClickListener listener) {
        super(context, R.layout.item_card_event_list, listEvent);
        this.listEvent = listEvent;
        this.context = context;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listViewItem = inflater.inflate(R.layout.item_card_event_list, null, true);

        //connect vew with variable
        TextView namaEvent = listViewItem.findViewById(R.id.tv_event_name);
        TextView waktuEvent = listViewItem.findViewById(R.id.tv_event_date);
        Button buttonAddEvent = listViewItem.findViewById(R.id.add_calender);

        Event event = listEvent.get(position);
        namaEvent.setText(event.getTitle());

        String mon[]={"January", "February", "March","April", "May", "June", "July", "August", "September", "October", "November", "December"};

        waktuEvent.setText(event.getDateStart() +" "+ mon[event.getMonthStart()-1] +" "+ event.getYearStart());

        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClickAddEvent(position);
            }
        });


        return  listViewItem;
    }

    @Override
    public void onClick(View view) {

    }
    public interface ClickListener{
        void onClickAddEvent(int position);
    }
}
