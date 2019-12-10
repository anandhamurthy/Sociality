package com.sociality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sociality.Adapter.NotificationsAdapter;
import com.sociality.Models.Notifications;
import com.sociality.Settings.MyEventsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView Notifications_List;
    private NotificationsAdapter notificationsAdapter;
    private List<Notifications> notificationsList;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private DatabaseReference mNotificationsDatabase;
    private RelativeLayout mNoNotifications;
    private ImageView Notification_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Notification_Back = findViewById(R.id.notification_back);

        Notifications_List =findViewById(R.id.notifications_list);
        mNoNotifications = findViewById(R.id.no_notification);
        Notifications_List.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(NotificationsActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        Notifications_List.setLayoutManager(mLayoutManager);
        notificationsList = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(NotificationsActivity.this, notificationsList);
        Notifications_List.setAdapter(notificationsAdapter);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        mNotificationsDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications").child(mCurrentUserId);
        mNotificationsDatabase.keepSynced(true);

        readNotifications();

        Notification_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void readNotifications() {

        mNotificationsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Notifications_List.setVisibility(View.VISIBLE);
                    mNoNotifications.setVisibility(View.GONE);
                    notificationsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notifications notifications = snapshot.getValue(Notifications.class);
                        notificationsList.add(notifications);
                    }

                    Collections.reverse(notificationsList);
                    notificationsAdapter.notifyDataSetChanged();
                } else {
                    mNoNotifications.setVisibility(View.VISIBLE);
                    Notifications_List.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
