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
import com.sociality.Adapter.DonatedAdapter;
import com.sociality.Models.Donated;
import com.sociality.Models.Joined;
import com.sociality.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DonationsFragment extends Fragment {

    private RecyclerView donated_List;
    private DonatedAdapter donatedAdapter;
    private List<Donated> donatedList;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private DatabaseReference mDonatedDatabase;
    private RelativeLayout mNoDonated;

    public DonationsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donations, container, false);

        donated_List = view.findViewById(R.id.donations_list);
        donated_List.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        donated_List.setLayoutManager(mLayoutManager);
        donatedList = new ArrayList<>();
        donatedAdapter = new DonatedAdapter(getContext(), donatedList);
        donated_List.setAdapter(donatedAdapter);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        mNoDonated = view.findViewById(R.id.no_donations);

        mDonatedDatabase = FirebaseDatabase.getInstance().getReference().child("Donation").child(mCurrentUserId);
        mDonatedDatabase.keepSynced(true);

        readDonated();

        return view;
    }

    private void readDonated() {

        mDonatedDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    donated_List.setVisibility(View.VISIBLE);
                    mNoDonated.setVisibility(View.GONE);
                    donatedList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Donated donated = snapshot.getValue(Donated.class);
                        donatedList.add(donated);
                    }

                    donatedAdapter.notifyDataSetChanged();
                    Collections.reverse(donatedList);
                } else {
                    mNoDonated.setVisibility(View.VISIBLE);
                    donated_List.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
