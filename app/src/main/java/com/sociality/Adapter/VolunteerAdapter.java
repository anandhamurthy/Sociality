package com.sociality.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.sociality.Models.Joined;
import com.sociality.Models.Users;
import com.sociality.Models.Volunteer;
import com.sociality.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.ImageViewHolder> {


    private Context mContext;
    private List<Volunteer> mVolunteerList;
    private FirebaseAuth mAuth;
    private DatabaseReference mVolunteerDatabase,mUsersDatabase;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private FirebaseStorage mStorage;

    public VolunteerAdapter(Context context, List<Volunteer> volunteerList) {
        mContext = context;
        mVolunteerList = volunteerList;
    }

    @NonNull
    @Override
    public VolunteerAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_layout_volunteer, parent, false);
        return new VolunteerAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VolunteerAdapter.ImageViewHolder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();
        mVolunteerDatabase = FirebaseDatabase.getInstance().getReference().child("Volunteer");
        mVolunteerDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mStorage = FirebaseStorage.getInstance();

        final Volunteer volunteer = mVolunteerList.get(position);
        UserInformation(holder.Volunteer_Profile_Image,holder.Volunteer_User_Name,volunteer.getUser_id());
    }

    @Override
    public int getItemCount() {
        return mVolunteerList.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView Volunteer_Profile_Image, Volunteer_Image;
        public TextView Volunteer_User_Name;

        public ImageViewHolder(View itemView) {
            super(itemView);

            Volunteer_Profile_Image = itemView.findViewById(R.id.volunteer_profile_image);
            Volunteer_Image = itemView.findViewById(R.id.volunteer_status_image);
            Volunteer_User_Name = itemView.findViewById(R.id.volunteer_user_name);
        }
    }

    private void UserInformation(final ImageView events_Profile_Image, final TextView joined_User_Name, String user_id) {

        mUsersDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Glide.with(mContext).load(user.getLogo()).into(events_Profile_Image);
                joined_User_Name.setText(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
