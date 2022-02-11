package com.example.project_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project_uas.adapter.AdapterEvent;
import com.example.project_uas.adapter.AdapterToko;
import com.example.project_uas.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class EventActivity extends AppCompatActivity implements AdapterEvent.ClickListener, View.OnClickListener {
    ListView listViewEvent;
    FirebaseDatabase fbDatabase;
    FirebaseAuth fbAuth;
    DatabaseReference dbReference;
    ArrayList<Event> listEvent;
    AdapterEvent adapterEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        listViewEvent = findViewById(R.id.lv_event_list);
        listEvent = new ArrayList<>();

        fbDatabase = FirebaseDatabase.getInstance(getString(R.string.INSTANCE_FIREBASE));
        dbReference = fbDatabase.getReference();

        getDataEvent();
    }

    private void getDataEvent(){

        Query queryEvent = dbReference.child("event");

        queryEvent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Event event = dataSnapshot.getValue(Event.class);
                        listEvent.add(event);
                    }
                    listViewSetup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listViewSetup() {
        adapterEvent = new AdapterEvent(listEvent, this, this);
        listViewEvent.setAdapter(adapterEvent);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClickAddEvent(int position) {
        addEventToCalendar(listEvent.get(position));
    }

    public void addEventToCalendar(Event event) {
        long calID = 1;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(event.getYearStart(), event.getMonthStart()-1, event.getDateStart(),6, 00);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(event.getYearEnd(), event.getMonthEnd()-1, event.getDateEnd(),6, 00);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, event.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, "EVENT TOKO ONLINE");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Indonesia/Jakarta");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Toast.makeText(this, "Event Telah Ditambahkan ke Calendar", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
        }
    }
}