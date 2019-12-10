package com.sociality.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sociality.NewEventActivity;
import com.sociality.Notification.APIService;
import com.sociality.DetailedActivity;
import com.sociality.Models.Events;
import com.sociality.Notification.Client;
import com.sociality.Notification.Data;
import com.sociality.Notification.MyResponse;
import com.sociality.Notification.Sender;
import com.sociality.Notification.Token;
import com.sociality.Models.Notifications;
import com.sociality.R;
import com.sociality.Models.Users;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ImageViewHolder> {

    APIService apiService;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId, User_Name;

    private DatabaseReference mEventsDatabase, mUsersDatabase, mVolunteerDatabase,
            mJoinHandsDatabase, mDonationDatabase, mNotificationDatabase, mVolunteerCheckDatabase, mJoinHandsCheckDatabase, mDonationCheckDatabase;

    private Context mContext;
    private List<Notifications> mNotification;

    public NotificationsAdapter(Context context, List<Notifications> notifications) {
        mContext = context;
        mNotification = notifications;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_layout_notifications, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mEventsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
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
        mJoinHandsCheckDatabase = FirebaseDatabase.getInstance().getReference().child("Join Hands Check");
        mJoinHandsCheckDatabase.keepSynced(true);
        mDonationCheckDatabase = FirebaseDatabase.getInstance().getReference().child("Donation Check");
        mDonationCheckDatabase.keepSynced(true);

        final Notifications notification = mNotification.get(position);

        holder.Notification_Comment.setText(notification.getMessage());

        UserInformation(holder.Notification_Profile_Image, holder.Notification_User_Name, notification.getUser_id());

        if (notification.isAccept()){
            holder.Notification_Events_Accept.setVisibility(View.GONE);
            holder.Notification_Events_Reject.setVisibility(View.GONE);
            holder.Notification_Accepted.setVisibility(View.VISIBLE);
        }else{
            holder.Notification_Events_Accept.setVisibility(View.VISIBLE);
            holder.Notification_Events_Reject.setVisibility(View.VISIBLE);
            holder.Notification_Accepted.setVisibility(View.GONE);
        }

        if(notification.isIs_tie_up() && !notification.isAccept()){
            holder.Notification_Events_Accept.setVisibility(View.VISIBLE);
            holder.Notification_Events_Reject.setVisibility(View.VISIBLE);
            holder.Notification_Events_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notification.isIs_tie_up()){
                        addJoin(notification.getEvent_id(),notification.getUser_id(), notification.getNoti_key(), notification.getEvent_user_id());
                    }

                }
            });
            holder.Notification_Events_Reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notification.isIs_tie_up()){
                        mJoinHandsDatabase.child(notification.getEvent_id()).child(notification.getUser_id()).removeValue();
                        deleteNotifications(notification.getEvent_id(),notification.getUser_id());
                    }
                }
            });
        }else if(notification.isIs_volunteer() && !notification.isAccept()){
            holder.Notification_Events_Accept.setVisibility(View.VISIBLE);
            holder.Notification_Events_Reject.setVisibility(View.VISIBLE);
            holder.Notification_Events_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notification.isIs_volunteer()){
                        addVol(notification.getEvent_id(),notification.getUser_id(), notification.getNoti_key(), notification.getEvent_user_id());
                    }

                }
            });
            holder.Notification_Events_Reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notification.isIs_volunteer()){
                        mVolunteerDatabase.child(notification.getEvent_id()).child(notification.getUser_id()).removeValue();
                        deleteNotifications(notification.getNoti_key(),notification.getEvent_user_id());
                    }
                }
            });
        }else if(notification.isIs_donation() && !notification.isAccept()){
            holder.Notification_Events_Accept.setVisibility(View.VISIBLE);
            holder.Notification_Events_Reject.setVisibility(View.VISIBLE);
            holder.Notification_Events_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notification.isIs_donation()){
                        addDonation(notification.getEvent_id(),notification.getUser_id(),notification.getNoti_key(), notification.getEvent_user_id());
                    }

                }
            });
            holder.Notification_Events_Reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notification.isIs_volunteer()){
                        mDonationDatabase.child(notification.getEvent_id()).child(notification.getUser_id()).removeValue();
                        deleteNotifications(notification.getEvent_id(),notification.getUser_id());
                    }
                }
            });
        }else{
            holder.Notification_Events_Accept.setVisibility(View.GONE);
            holder.Notification_Events_Reject.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", notification.getEvent_id());
                editor.apply();

                Intent detailedIntent = new Intent(mContext, DetailedActivity.class);
                detailedIntent.putExtra("user_id",mCurrentUserId);
                detailedIntent.putExtra("event_id",notification.getEvent_id());
                mContext.startActivity(detailedIntent);
            }
        });

    }

    private void addDonation(final String event_id, final String user_id, final String noti_key, final String event_user_id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Confirm");
        builder.setMessage("Have they donated to you ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDonationCheckDatabase.child(event_id).child(user_id).setValue(true);
                        String key = mDonationDatabase.child(user_id).push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("donation_key",key);
                        hashMap.put("user_id", user_id);
                        hashMap.put("event_user_id", mCurrentUserId);
                        hashMap.put("event_id", event_id);
                        hashMap.put("accept", true);

                        mDonationDatabase.child(user_id).child(key).setValue(hashMap);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("accept", true);
                        mNotificationDatabase.child(event_user_id).child(noti_key).updateChildren(map);
                        deleteNotifications(event_id,user_id);
                        sendNotification(user_id, "Donation Confirm", User_Name, mCurrentUserId);
                        addNotification(user_id,event_id,"Donation Confirm",false,false,false,false);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addVol(final String event_id, final String user_id, final String noti_key, final String event_user_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Confirm");
        builder.setMessage("Do you want to their Volunteer Action ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mVolunteerCheckDatabase.child(event_id).child(user_id).setValue(true);
                        String key = mVolunteerDatabase.child(user_id).push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("vol_key",key);
                        hashMap.put("user_id", user_id);
                        hashMap.put("event_user_id", mCurrentUserId);
                        hashMap.put("event_id", event_id);
                        hashMap.put("accept", true);

                        mVolunteerDatabase.child(user_id).child(key).setValue(hashMap);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("accept", true);
                        mNotificationDatabase.child(event_user_id).child(noti_key).updateChildren(map);
                        deleteNotifications(event_id,user_id);
                        sendNotification(user_id, "Volunteer Confirmed", User_Name, mCurrentUserId);
                        addNotification(user_id,event_id,"Volunteer Confirm",false,false,false,false);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void addJoin(final String event_id, final String user_id, final String noti_key, final String event_user_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Confirm");
        builder.setMessage("Do you want to tie up with them ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mJoinHandsCheckDatabase.child(event_id).child(user_id).setValue(true);
                        String key = mJoinHandsDatabase.child(user_id).push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("join_key",key);
                        hashMap.put("user_id", user_id);
                        hashMap.put("event_user_id", mCurrentUserId);
                        hashMap.put("event_id", event_id);
                        hashMap.put("accept", true);

                        mJoinHandsDatabase.child(user_id).child(key).setValue(hashMap);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("accept", true);

                        mNotificationDatabase.child(event_user_id).child(noti_key).updateChildren(map);
                        sendNotification(user_id, "Tie Up Accepted", User_Name, mCurrentUserId);
                        addNotification(user_id,event_id,"Tie Up Confirm",false,false,false,false);


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void deleteNotifications(final String postid, final String userid) {
        mNotificationDatabase.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("noti_key").getValue().equals(postid)) {
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

    private void UserInformation(final ImageView profile_image, final TextView user_name, String user_id) {

        mUsersDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Glide.with(mContext).load(user.getLogo()).into(profile_image);
                user_name.setText(user.getName());
                User_Name=user.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addNotification(String userid, String postid, String message, boolean is_event, boolean tie_up_request, boolean vol_request, boolean donation_check) {

        String key = mNotificationDatabase.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("noti_key",key);
        hashMap.put("user_id", mCurrentUserId);
        hashMap.put("message", message);
        hashMap.put("event_user_id", userid);
        hashMap.put("event_id", postid);
        hashMap.put("accept", false);
        hashMap.put("is_event", is_event);
        hashMap.put("is_tie_up", tie_up_request);
        hashMap.put("is_volunteer", vol_request);
        hashMap.put("is_donation", donation_check);
        hashMap.put("is_read", false);

        mNotificationDatabase.child(userid).child(key).setValue(hashMap);
    }


    @Override
    public int getItemCount() {
        return mNotification.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView Notification_Profile_Image, Notification_Events_Accept, Notification_Events_Reject ;
        public TextView Notification_User_Name, Notification_Comment, Notification_Accepted;

        public ImageViewHolder(View itemView) {
            super(itemView);
            Notification_Profile_Image = itemView.findViewById(R.id.single_notification_profile_image);
            Notification_Events_Accept = itemView.findViewById(R.id.single_notification_accept);
            Notification_Accepted = itemView.findViewById(R.id.single_notification_accepted);
            Notification_Events_Reject = itemView.findViewById(R.id.single_notification_reject);
            Notification_User_Name = itemView.findViewById(R.id.single_notification_user_name);
            Notification_Comment = itemView.findViewById(R.id.single_notification_comment);
        }
    }
}