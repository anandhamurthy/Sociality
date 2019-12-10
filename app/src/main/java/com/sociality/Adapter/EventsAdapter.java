package com.sociality.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sociality.Notification.APIService;
import com.sociality.DetailedActivity;
import com.sociality.Models.Events;
import com.sociality.Notification.Client;
import com.sociality.Notification.Data;
import com.sociality.Notification.MyResponse;
import com.sociality.Notification.Sender;
import com.sociality.Notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.sociality.R;
import com.sociality.Models.Users;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ImageViewHolder> {

    APIService apiService;
    private Context mContext;
    private List<Events> mEvents;
    private FirebaseAuth mAuth;
    private DatabaseReference mEventsDatabase, mUsersDatabase, mLikesDatabase, mNotificationDatabase, mDonationDatabase,
            mVolunteerDatabase, mJoinHandsDatabase, mVolunteerCheckDatabase, mDonationCheckDatabase, mJoinHandsCheckDatabase;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private FirebaseStorage mStorage;

    public EventsAdapter(Context context, List<Events> events, boolean b) {
        mContext = context;
        mEvents = events;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_layout_event, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();
        mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mEventsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
        mLikesDatabase = FirebaseDatabase.getInstance().getReference().child("Likes");
        mLikesDatabase.keepSynced(true);
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mNotificationDatabase.keepSynced(true);
        mVolunteerDatabase = FirebaseDatabase.getInstance().getReference().child("Volunteer");
        mVolunteerDatabase.keepSynced(true);
        mJoinHandsDatabase = FirebaseDatabase.getInstance().getReference().child("Join Hands");
        mJoinHandsDatabase.keepSynced(true);
        mDonationDatabase = FirebaseDatabase.getInstance().getReference().child("Donation");
        mDonationDatabase.keepSynced(true);
        mVolunteerCheckDatabase = FirebaseDatabase.getInstance().getReference().child("Volunteer Check");
        mVolunteerCheckDatabase.keepSynced(true);

        mDonationCheckDatabase = FirebaseDatabase.getInstance().getReference().child("Donation Check");
        mDonationCheckDatabase.keepSynced(true);

        mJoinHandsCheckDatabase = FirebaseDatabase.getInstance().getReference().child("Join Hands Check");
        mJoinHandsCheckDatabase.keepSynced(true);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        mStorage = FirebaseStorage.getInstance();

        final Events events = mEvents.get(position);
        Glide.with(mContext).load(events.getImage_one()).into(holder.Events_Image);

        UserInformation(holder.Events_Profile_Image, events.getUser_id());
        isLiked(events.getEvent_key(), holder.Events_Likes);
        nrLikes(holder.Events_Likes_Count, events.getEvent_key());

        isJoinedHands(events.getEvent_key(), holder.Events_Join);
        isDonated(events.getEvent_key(), holder.Events_Donate);
        isVolunteered(events.getEvent_key(), holder.Events_Volunteer);

//        nrJoinedHands(holder.Events_Joined_Count, events.getEvent_key());
//        nrDonated(holder.Events_Donated_Count, events.getEvent_key());
//        nrVolunteered(holder.Events_Volunteer_Count, events.getEvent_key());

        holder.Events_Name.setText(events.getEvent_name());
        if(events.getVolunteer_members().isEmpty()){
            holder.Events_Volunteer_Count.setVisibility(View.GONE);
            holder.Events_Volunteer.setVisibility(View.GONE);
        }else{
            holder.Events_Volunteer_Count.setVisibility(View.VISIBLE);
            holder.Events_Volunteer.setVisibility(View.VISIBLE);
        }
        if(events.getJoin_hands_members().isEmpty()){
            holder.Events_Joined_Count.setVisibility(View.GONE);
            holder.Events_Join.setVisibility(View.GONE);
        }else {
            holder.Events_Joined_Count.setVisibility(View.VISIBLE);
            holder.Events_Join.setVisibility(View.VISIBLE);
        }
        if(events.getDonation_web_page().isEmpty()){
            holder.Events_Donated_Count.setVisibility(View.GONE);
            holder.Events_Donate.setVisibility(View.GONE);
        }else{
            holder.Events_Donated_Count.setVisibility(View.VISIBLE);
            holder.Events_Donate.setVisibility(View.VISIBLE);
        }

        holder.Events_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailedIntent = new Intent(mContext, DetailedActivity.class);
                detailedIntent.putExtra("user_id",events.getUser_id());
                detailedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detailedIntent.putExtra("event_id",events.getEvent_key());
                mContext.startActivity(detailedIntent);
            }
        });

        holder.Events_Profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailedIntent = new Intent(mContext, DetailedActivity.class);
                detailedIntent.putExtra("user_id",events.getUser_id());
                detailedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detailedIntent.putExtra("event_id",events.getEvent_key());
                mContext.startActivity(detailedIntent);
            }
        });

        holder.Events_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailedIntent = new Intent(mContext, DetailedActivity.class);
                detailedIntent.putExtra("user_id",events.getUser_id());
                detailedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detailedIntent.putExtra("event_id",events.getEvent_key());
                mContext.startActivity(detailedIntent);
            }
        });



        holder.Events_Likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.Events_Likes.getTag().equals("like")) {
                    mLikesDatabase.child(events.getEvent_key()).child(mCurrentUserId).setValue(true);

                    if (!events.getUser_id().equals(mCurrentUserId)) {
                        mUsersDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Users users = dataSnapshot.getValue(Users.class);
                                sendNotification(events.getUser_id(), "Liked your Event", users.getName(), mCurrentUserId);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    addNotification(events.getUser_id(), events.getEvent_key(), "Liked your Event", true,false, false);
                } else {
                    mLikesDatabase.child(events.getEvent_key()).child(mCurrentUserId).removeValue();
                }
            }
        });

        holder.Events_Volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailedIntent = new Intent(mContext, DetailedActivity.class);
                detailedIntent.putExtra("user_id",events.getUser_id());
                detailedIntent.putExtra("event_id",events.getEvent_key());
                mContext.startActivity(detailedIntent);
            }
        });

//        holder.Events_More.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(mContext, view);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//                            case R.id.delete:
//                                final String id = events.getEvent_key();
//
//                                StorageReference photoRef = mStorage.getReferenceFromUrl(events.getImage_one());
//                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                        mEventsDatabase.child(events.getEvent_key()).removeValue()
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
//                                                            deleteNotifications(id, mCurrentUserId);
//                                                        }
//                                                    }
//                                                });
//                                    }
//                                });
//
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
//                popupMenu.inflate(R.menu.event_menu);
//                popupMenu.show();
//            }
//        });
    }

    private void UserInformation(final ImageView events_Profile_Image, String user_id) {

        mUsersDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Glide.with(mContext).load(user.getLogo()).into(events_Profile_Image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView Events_Profile_Image, Events_Image, Events_Likes, Events_Donate, Events_Join, Events_Volunteer, Events_More, Events_Save;
        public TextView Events_Name, Events_Likes_Count,Events_Donated_Count, Events_Joined_Count, Events_Volunteer_Count;

        public ImageViewHolder(View itemView) {
            super(itemView);

            Events_Profile_Image = itemView.findViewById(R.id.events_profile_image);
            Events_Image = itemView.findViewById(R.id.events_image);
            Events_Likes = itemView.findViewById(R.id.events_likes);
            Events_Join = itemView.findViewById(R.id.events_join);
            Events_Name = itemView.findViewById(R.id.events_name);
            Events_Donate = itemView.findViewById(R.id.events_donate);
            Events_Volunteer = itemView.findViewById(R.id.events_volunteer);
            Events_Likes_Count = itemView.findViewById(R.id.events_likes_count);
            Events_Donated_Count = itemView.findViewById(R.id.events_donated_count);
            Events_Joined_Count = itemView.findViewById(R.id.events_joined_count);
            Events_Volunteer_Count= itemView.findViewById(R.id.events_volunteer_count);
        }
    }

    private void nrLikes(final TextView likes, String postId) {
        mLikesDatabase.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 1 || dataSnapshot.getChildrenCount() == 0) {
                    likes.setText(dataSnapshot.getChildrenCount() + "");
                } else {
                    likes.setText(dataSnapshot.getChildrenCount() + "");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//    private void nrVolunteered(final TextView likes, String postId) {
//        mVolunteerDatabase.child(postId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getChildrenCount() == 1 || dataSnapshot.getChildrenCount() == 0) {
//                    likes.setText(dataSnapshot.getChildrenCount() + "");
//                } else {
//                    likes.setText(dataSnapshot.getChildrenCount() + "");
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//    private void nrJoinedHands(final TextView likes, String postId) {
//        mJoinHandsDatabase.child(postId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getChildrenCount() == 1 || dataSnapshot.getChildrenCount() == 0) {
//                    likes.setText(dataSnapshot.getChildrenCount() + "");
//                } else {
//                    likes.setText(dataSnapshot.getChildrenCount() + "");
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//    private void nrDonated(final TextView likes, String postId) {
//        mDonationDatabase.child(postId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getChildrenCount() == 1 || dataSnapshot.getChildrenCount() == 0) {
//                    likes.setText(dataSnapshot.getChildrenCount() + "");
//                } else {
//                    likes.setText(dataSnapshot.getChildrenCount() + "");
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private void sendNotification(final String Reciever, final String User_Name, final String Message, final String user_id) {

        final DatabaseReference token = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Query query = token.orderByKey().equalTo(Reciever);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Token token1 = snapshot.getValue(Token.class);
                    Data data = new Data(user_id, R.drawable.icon, User_Name, Message, Reciever);

                    Sender sender = new Sender(data, token1.getToken());
                    apiService.sendNotification(sender).enqueue(
                            new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    if (response.code() == 200) {
                                        if (response.body().sucess == 1) {
                                            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            }
                    );
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void isLiked(final String postid, final ImageView imageView) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mLikesDatabase.child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.like_fill);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.like_empty);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isVolunteered(final String postid, final ImageView imageView) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mVolunteerDatabase.child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    if(dataSnapshot.child(firebaseUser.getUid()).getValue().equals(true))
                        imageView.setImageResource(R.drawable.volunteer_fill);
                    imageView.setTag("volunteered");
                } else {
                    imageView.setImageResource(R.drawable.volunteer_empty);
                    imageView.setTag("volunteer");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isJoinedHands(final String postid, final ImageView imageView) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mJoinHandsDatabase.child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    if(dataSnapshot.child(firebaseUser.getUid()).getValue().equals(true))
                        imageView.setImageResource(R.drawable.join_hands_fill);
                    imageView.setTag("joined");
                } else {
                    imageView.setImageResource(R.drawable.join_hands_empty);
                    imageView.setTag("join");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isDonated(final String postid, final ImageView imageView) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mDonationDatabase.child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    if(dataSnapshot.child(firebaseUser.getUid()).getValue().equals(true))
                        imageView.setImageResource(R.drawable.donation_fill);
                    imageView.setTag("donated");
                } else {
                    imageView.setImageResource(R.drawable.donation_empty);
                    imageView.setTag("donate");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addNotification(String userid, String postid, String message, boolean is_event, boolean tie_up_request, boolean vol_request) {

        String key = mNotificationDatabase.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", mCurrentUserId);
        hashMap.put("message", message);
        hashMap.put("event_id", postid);
        hashMap.put("noti_key",key);
        hashMap.put("event_user_id", userid);
        hashMap.put("accept", false);
        hashMap.put("is_event", is_event);
        hashMap.put("is_tie_up", tie_up_request);
        hashMap.put("is_volunteer", vol_request);
        hashMap.put("is_read", false);

        mNotificationDatabase.child(userid).child(key).setValue(hashMap);
    }

    private void deleteNotifications(final String postid, String userid) {
        mNotificationDatabase.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("event_id").getValue().equals(postid)) {
                        snapshot.getRef().removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}