package com.sociality.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sociality.Adapter.JoinedAdapter;
import com.sociality.Adapter.VolunteerAdapter;
import com.sociality.Models.Joined;
import com.sociality.Models.Volunteer;
import com.sociality.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolunteerFragment extends Fragment {

    private RecyclerView Volunteer_List;
    private VolunteerAdapter volunteerAdapter;
    private List<Volunteer> volunteerList;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private DatabaseReference mVolunteerDatabase;
    private RelativeLayout mNoVolunteer;

    public VolunteerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volunteer, container, false);

        Volunteer_List = view.findViewById(R.id.volunteer_list);
        Volunteer_List.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        Volunteer_List.setLayoutManager(mLayoutManager);
        volunteerList = new ArrayList<>();
        volunteerAdapter = new VolunteerAdapter(getContext(), volunteerList);
        Volunteer_List.setAdapter(volunteerAdapter);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        mNoVolunteer = view.findViewById(R.id.no_volunteer);

        mVolunteerDatabase = FirebaseDatabase.getInstance().getReference().child("Volunteer").child(mCurrentUserId);
        mVolunteerDatabase.keepSynced(true);

        readVolunteer();
        return view;
    }

    private void readVolunteer() {

        mVolunteerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Volunteer_List.setVisibility(View.VISIBLE);
                    mNoVolunteer.setVisibility(View.GONE);
                    volunteerList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Volunteer volunteer = snapshot.getValue(Volunteer.class);
                        volunteerList.add(volunteer);
                    }
                    Collections.reverse(volunteerList);
                    volunteerAdapter.notifyDataSetChanged();

                } else {
                    mNoVolunteer.setVisibility(View.VISIBLE);
                    Volunteer_List.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
