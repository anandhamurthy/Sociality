package com.sociality.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sociality.Adapter.EventsAdapter;
import com.sociality.Models.Events;
import com.sociality.R;

import java.util.ArrayList;
import java.util.List;

public class MyEventsActivity extends AppCompatActivity {

    private RecyclerView My_Events_List;
    private EventsAdapter eventsAdapter;
    private List<Events> eventsList;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private DatabaseReference mEventsDatabase;
    private ImageView My_Event_Back;
    private RelativeLayout mNoMyEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        My_Event_Back = findViewById(R.id.my_event_back);

        My_Events_List = findViewById(R.id.my_events_list);
        My_Events_List.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MyEventsActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        My_Events_List.setLayoutManager(mLayoutManager);
        eventsList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(MyEventsActivity.this, eventsList, true);
        My_Events_List.setAdapter(eventsAdapter);

        mNoMyEvents = findViewById(R.id.no_events);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mEventsDatabase.keepSynced(true);

        My_Event_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        readEvents();
    }

    private void readEvents() {

        mEventsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Events events = snapshot.getValue(Events.class);
                    if (events.getUser_id().equals(mCurrentUserId)) {
                        My_Events_List.setVisibility(View.VISIBLE);
                        mNoMyEvents.setVisibility(View.GONE);
                        eventsList.add(events);
                    }else{
                        mNoMyEvents.setVisibility(View.VISIBLE);
                        My_Events_List.setVisibility(View.GONE);
                    }

                }
                eventsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
