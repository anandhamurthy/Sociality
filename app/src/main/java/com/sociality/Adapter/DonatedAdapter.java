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
import com.sociality.Models.Donated;
import com.sociality.Models.Users;
import com.sociality.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DonatedAdapter extends RecyclerView.Adapter<DonatedAdapter.ImageViewHolder> {


    private Context mContext;
    private List<Donated> mDonated;
    private FirebaseAuth mAuth;
    private DatabaseReference mDonatedDatabase,mUsersDatabase;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private FirebaseStorage mStorage;

    public DonatedAdapter(Context context, List<Donated> donateds) {
        mContext = context;
        mDonated = donateds;
    }

    @NonNull
    @Override
    public DonatedAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_layout_donated, parent, false);
        return new DonatedAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DonatedAdapter.ImageViewHolder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();
        mDonatedDatabase = FirebaseDatabase.getInstance().getReference().child("Donation");
        mDonatedDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mStorage = FirebaseStorage.getInstance();

        final Donated donated = mDonated.get(position);
        UserInformation(holder.Donated_Profile_Image,holder.Donated_User_Name,donated.getUser_id());
    }

    @Override
    public int getItemCount() {
        return mDonated.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView Donated_Profile_Image, Donated_Image;
        public TextView Donated_User_Name;

        public ImageViewHolder(View itemView) {
            super(itemView);

            Donated_Profile_Image = itemView.findViewById(R.id.donated_profile_image);
            Donated_Image = itemView.findViewById(R.id.donated_status_image);
            Donated_User_Name = itemView.findViewById(R.id.donated_user_name);
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
