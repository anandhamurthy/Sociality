package com.sociality.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class EventsFragment extends Fragment {

    public EventsFragment() {
    }

    private RecyclerView Events_List;
    private EventsAdapter eventsAdapter;
    private List<Events> eventsList;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private DatabaseReference mEventsDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        Events_List = view.findViewById(R.id.events_list);
        Events_List.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        Events_List.setLayoutManager(mLayoutManager);
        eventsList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(getContext(), eventsList, true);
        Events_List.setAdapter(eventsAdapter);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mEventsDatabase.keepSynced(true);

        readEvents();

        return view;
    }

    private void readEvents() {

        mEventsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Events events = snapshot.getValue(Events.class);
                    eventsList.add(events);
                }
                eventsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}