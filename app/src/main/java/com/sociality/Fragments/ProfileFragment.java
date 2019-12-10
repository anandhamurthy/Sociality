package com.sociality.Fragments;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sociality.EditProfileActivity;
import com.sociality.Models.Donated;
import com.sociality.Models.Events;
import com.sociality.Models.Users;
import com.sociality.Models.Volunteer;
import com.sociality.MyWorksActivity;
import com.sociality.R;
import com.sociality.Settings.MyEventsActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }


    private ImageView Profile_Image_Edit;
    private TextView Profile_Sex, Profile_Website, Profile_Phone_Number, Profile_Founder,
            Profile_My_Donations_Count, Profile_My_Tie_Ups_Count, Profile_Voluntary_Work_Count, Profile_Description,
            Profile_User_Name, Profile_Place, Profile_My_Works;
    private CircleImageView Profile_Image;


    private FirebaseUser mFirebaseUser;
    private String profileid;
    private String mCurrentUserId;
    private DatabaseReference mUsersDatabase, mMyEventsDatabase, mVolunteerDatabase, mDonationDatabase, mJoinHandsDatabase;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mMyEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mMyEventsDatabase.keepSynced(true);

        Profile_Image_Edit = view.findViewById(R.id.profile_image_edit);
        Profile_My_Donations_Count = view.findViewById(R.id.profile_my_donations_count);
        Profile_My_Tie_Ups_Count = view.findViewById(R.id.profile_my_tie_ups_count);
        Profile_Image = view.findViewById(R.id.profile_image);
        Profile_Voluntary_Work_Count = view.findViewById(R.id.profile_my_volentary_count);
        Profile_User_Name = view.findViewById(R.id.profile_name);
        Profile_Description = view.findViewById(R.id.profile_description);
        Profile_Sex = view.findViewById(R.id.profile_sex);
        Profile_Founder = view.findViewById(R.id.profile_founder_name);
        Profile_Website = view.findViewById(R.id.profile_website);
        Profile_Phone_Number = view.findViewById(R.id.profile_phone_number);
        Profile_Place = view.findViewById(R.id.profile_place);
        Profile_My_Works = view.findViewById(R.id.profile_my_works);

        mVolunteerDatabase = FirebaseDatabase.getInstance().getReference().child("Volunteer");
        mVolunteerDatabase.keepSynced(true);

        mDonationDatabase = FirebaseDatabase.getInstance().getReference().child("Donation");
        mDonationDatabase.keepSynced(true);

        mJoinHandsDatabase = FirebaseDatabase.getInstance().getReference().child("Join Hands");
        mJoinHandsDatabase.keepSynced(true);

        Profile_Image_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        UserInformation(mCurrentUserId);
        joinCount();
        volCount();
        donationCount();

        Profile_My_Works.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyWorksActivity.class));
            }
        });
        return view;

    }

    private void UserInformation(final String profileid) {
        mUsersDatabase.child(profileid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }

                Users user = dataSnapshot.getValue(Users.class);
                if(user.getUser_key().equals("Organization")){
                    Profile_Founder.setVisibility(View.VISIBLE);
                    Profile_Founder.setText(user.getFounder());
                }else{
                    Profile_Founder.setVisibility(View.GONE);
                }
                Glide.with(getContext()).load(user.getLogo()).into(Profile_Image);
                Profile_User_Name.setText(user.getName());
                Profile_Website.setText("WEBSITE : "+user.getWebsite());
                Profile_Phone_Number.setText("Phone Number : "+user.getPhone_number());
                Profile_Place.setText("Place : "+user.getPlace());
                if (user.getDescription().isEmpty()) {
                    Profile_Description.setVisibility(View.GONE);
                } else {
                    Profile_Description.setVisibility(View.VISIBLE);
                    Profile_Description.setText(user.getDescription());
                }
                if (user.getGender().isEmpty()) {
                    Profile_Sex.setVisibility(View.GONE);
                } else {
                    Profile_Sex.setVisibility(View.VISIBLE);
                    Profile_Sex.setText(user.getGender());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean donationCount() {
        mDonationDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile_My_Donations_Count.setText("My Donations : "+dataSnapshot.getChildrenCount()+"");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }
    private boolean volCount() {
        mVolunteerDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile_Voluntary_Work_Count.setText("My Volentary Works : "+dataSnapshot.getChildrenCount()+"");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }

    private boolean joinCount() {
        mJoinHandsDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile_My_Tie_Ups_Count.setText("My Tie ups : "+dataSnapshot.getChildrenCount()+"");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }


}
