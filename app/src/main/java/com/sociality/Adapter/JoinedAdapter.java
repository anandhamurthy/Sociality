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
import com.sociality.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JoinedAdapter extends RecyclerView.Adapter<JoinedAdapter.ImageViewHolder> {


    private Context mContext;
    private List<Joined> mJoined;
    private FirebaseAuth mAuth;
    private DatabaseReference mJoinedDatabase,mUsersDatabase;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private FirebaseStorage mStorage;

    public JoinedAdapter(Context context, List<Joined> joineds) {
        mContext = context;
        mJoined = joineds;
    }

    @NonNull
    @Override
    public JoinedAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_layout_joined, parent, false);
        return new JoinedAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final JoinedAdapter.ImageViewHolder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();
        mJoinedDatabase = FirebaseDatabase.getInstance().getReference().child("Join Hands").child(mCurrentUserId);
        mJoinedDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mStorage = FirebaseStorage.getInstance();

        final Joined joined = mJoined.get(position);
        UserInformation(holder.Joined_Profile_Image,holder.Joined_User_Name,joined.getUser_id());
    }

    @Override
    public int getItemCount() {
        return mJoined.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView Joined_Profile_Image, Joined_Image;
        public TextView Joined_User_Name;

        public ImageViewHolder(View itemView) {
            super(itemView);

            Joined_Profile_Image = itemView.findViewById(R.id.joined_profile_image);
            Joined_Image = itemView.findViewById(R.id.joined_status_image);
            Joined_User_Name = itemView.findViewById(R.id.joined_user_name);
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
