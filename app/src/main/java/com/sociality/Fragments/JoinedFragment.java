package com.sociality.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sociality.Adapter.JoinedAdapter;
import com.sociality.Models.Joined;
import com.sociality.Models.Notifications;
import com.sociality.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoinedFragment extends Fragment {

    private RecyclerView Joined_List;
    private JoinedAdapter joinedAdapter;
    private List<Joined> joinedList;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private DatabaseReference mJoinedDatabase;
    private RelativeLayout mNoJoined;

    public JoinedFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joined, container, false);

        Joined_List = view.findViewById(R.id.joined_list);
        Joined_List.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        Joined_List.setLayoutManager(mLayoutManager);
        joinedList = new ArrayList<>();
        joinedAdapter = new JoinedAdapter(getContext(), joinedList);
        Joined_List.setAdapter(joinedAdapter);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        mNoJoined = view.findViewById(R.id.no_joined);

        mJoinedDatabase = FirebaseDatabase.getInstance().getReference().child("Join Hands").child(mCurrentUserId);
        mJoinedDatabase.keepSynced(true);

        readJoined();
        return view;
    }

    private void readJoined() {

        mJoinedDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Joined_List.setVisibility(View.VISIBLE);
                    mNoJoined.setVisibility(View.GONE);
                    joinedList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Joined joined = snapshot.getValue(Joined.class);
                        joinedList.add(joined);
                    }

                    joinedAdapter.notifyDataSetChanged();
                    Collections.reverse(joinedList);
                } else {
                    mNoJoined.setVisibility(View.VISIBLE);
                    Joined_List.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
