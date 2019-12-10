package com.sociality;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.sociality.Adapter.ImageSliderAdapter;
import com.sociality.Models.Donated;
import com.sociality.Models.Events;
import com.sociality.Models.Joined;
import com.sociality.Models.Users;
import com.sociality.Models.Volunteer;
import com.sociality.Notification.APIService;
import com.sociality.Notification.Client;
import com.sociality.Notification.Data;
import com.sociality.Notification.MyResponse;
import com.sociality.Notification.Sender;
import com.sociality.Notification.Token;

import java.util.Collections;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;

    private SliderView Detailed_Slider;
    private FloatingActionButton Detailed_Donation_Button,Detailed_Join_Hands_Button,Detailed_Volunteer_Button;
    private DatabaseReference mUsersDatabase, mEventsDatabase, mLikesDatabase, mJoinHandsCheckDatabase,mVolunteerCheckDatabase,
            mDonationCheckDatabase,mNotificationDatabase, mVolunteerDatabase, mDonationDatabase, mJoinHandsDatabase;
    public ImageView Detailed_Profile_Image,Detailed_Back, Detailed_Likes,Detailed_Donate,Detailed_Join,Detailed_Volunteer;
    public TextView Detailed_Name,Detailed_User_Name, Detailed_Description, Detailed_Likes_Count ,Detailed_Website,Detailed_Phone_Number,Detailed_Incharge_Person_Email_Id ,
            Detailed_Volunteer_Count,Detailed_Donated_Count,Detailed_Joined_Count,Detailed_Donation_Web_Page,Detailed_Incharge_Person,Detailed_Incharge_Person_Contact_Number;

    public TextView Detailed_Donation_Web_Page_Text,Detailed_Volunteer_Members_Text,Detailed_Join_Hands_Text;

    public TextView Detailed_Volunteer_Members,Detailed_Join_Hands;

    ImageSliderAdapter viewPagerAdapter;
    APIService apiService;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        final String user_id = getIntent().getStringExtra("user_id");
        final String event_id = getIntent().getStringExtra("event_id");

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        Detailed_Profile_Image = findViewById(R.id.detailed_profile_image);
        Detailed_Likes = findViewById(R.id.detailed_likes);
        Detailed_Back= findViewById(R.id.detailed_back);
        Detailed_Join = findViewById(R.id.detailed_join);
        Detailed_Name = findViewById(R.id.detailed_name);
        Detailed_User_Name = findViewById(R.id.detailed_user_name);
        Detailed_Donate = findViewById(R.id.detailed_donate);
        Detailed_Likes_Count = findViewById(R.id.detailed_likes_count);
        Detailed_Donated_Count = findViewById(R.id.detailed_donated_count);
        Detailed_Joined_Count = findViewById(R.id.detailed_joined_count);
        Detailed_Volunteer = findViewById(R.id.detailed_volunteer);
        Detailed_Volunteer_Count = findViewById(R.id.detailed_volunteer_count);
        Detailed_Description = findViewById(R.id.detailed_description);
        Detailed_Website = findViewById(R.id.detailed_website);
        Detailed_Phone_Number = findViewById(R.id.detailed_phone_number);
        Detailed_Incharge_Person = findViewById(R.id.detailed_incharge_person_name);
        Detailed_Incharge_Person_Contact_Number = findViewById(R.id.detailed_incharge_person_contact_number);
        Detailed_Incharge_Person_Email_Id = findViewById(R.id.detailed_incharge_person_email_id);
        Detailed_Donation_Web_Page = findViewById(R.id.detailed_donation_web_page);

        Detailed_Donation_Button = findViewById(R.id.detailed_donation_button);
        Detailed_Join_Hands_Button = findViewById(R.id.detailed_join_hands_button);
        Detailed_Volunteer_Button = findViewById(R.id.detailed_volunteer_button);

        Detailed_Donation_Web_Page_Text = findViewById(R.id.detailed_donation_web_page_text);
        Detailed_Join_Hands_Text = findViewById(R.id.detailed_join_hands_text);
        Detailed_Volunteer_Members_Text = findViewById(R.id.detailed_volunteer_members_text);

        Detailed_Join_Hands = findViewById(R.id.detailed_join_hands);
        Detailed_Volunteer_Members = findViewById(R.id.detailed_volunteer_members);

        Detailed_Slider = findViewById(R.id.detailed_image_slider);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mLikesDatabase = FirebaseDatabase.getInstance().getReference().child("Likes");
        mLikesDatabase.keepSynced(true);

        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mNotificationDatabase.keepSynced(true);

        mVolunteerDatabase = FirebaseDatabase.getInstance().getReference().child("Volunteer");
        mVolunteerDatabase.keepSynced(true);

        mDonationDatabase = FirebaseDatabase.getInstance().getReference().child("Donation");
        mDonationDatabase.keepSynced(true);

        mJoinHandsDatabase = FirebaseDatabase.getInstance().getReference().child("Join Hands");
        mJoinHandsDatabase.keepSynced(true);

        mVolunteerCheckDatabase = FirebaseDatabase.getInstance().getReference().child("Volunteer Check");
        mVolunteerCheckDatabase.keepSynced(true);

        mDonationCheckDatabase = FirebaseDatabase.getInstance().getReference().child("Donation Check");
        mDonationCheckDatabase.keepSynced(true);

        mJoinHandsCheckDatabase = FirebaseDatabase.getInstance().getReference().child("Join Hands Check");
        mJoinHandsCheckDatabase.keepSynced(true);

        UserInformation(user_id);
        mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id);
        mEventsDatabase.keepSynced(true);
        mEventsDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Events detailed = dataSnapshot.getValue(Events.class);
                count=detailed.getImage_count();
                
                Detailed_Name.setText(detailed.getEvent_name());
                
                Detailed_Description.setText(detailed.getDescription());
                
                Detailed_Website.setText(detailed.getWebsite());
                Detailed_Incharge_Person.setText(detailed.getIncharge_person());
                Detailed_Incharge_Person_Contact_Number.setText(detailed.getContact_number());
                Detailed_Incharge_Person_Email_Id.setText(detailed.getIncharge_email_id());
                Detailed_Donation_Web_Page.setText(detailed.getDonation_web_page());

                if(detailed.getVolunteer_members().isEmpty()){
                    Detailed_Volunteer_Count.setVisibility(View.INVISIBLE);
                    Detailed_Volunteer.setVisibility(View.INVISIBLE);
                    Detailed_Volunteer_Button.setVisibility(View.GONE);
                    Detailed_Volunteer_Button.setVisibility(View.GONE);
                    Detailed_Volunteer_Members_Text.setVisibility(View.GONE);
                    Detailed_Volunteer_Members.setVisibility(View.GONE);
                }else{
                    Detailed_Volunteer_Count.setVisibility(View.VISIBLE);
                    Detailed_Volunteer.setVisibility(View.VISIBLE);
                    Detailed_Volunteer_Button.setVisibility(View.VISIBLE);
                    Detailed_Volunteer_Members_Text.setVisibility(View.VISIBLE);
                    Detailed_Volunteer_Members.setVisibility(View.VISIBLE);
                    Detailed_Volunteer_Members.setText(detailed.getVolunteer_members());
                }
                if(detailed.getJoin_hands_members().isEmpty()){
                    Detailed_Joined_Count.setVisibility(View.INVISIBLE);
                    Detailed_Join.setVisibility(View.INVISIBLE);
                    Detailed_Join_Hands_Button.setVisibility(View.GONE);
                    Detailed_Join_Hands_Text.setVisibility(View.GONE);
                    Detailed_Join_Hands.setVisibility(View.GONE);
                }else {
                    Detailed_Joined_Count.setVisibility(View.VISIBLE);
                    Detailed_Join.setVisibility(View.VISIBLE);
                    Detailed_Join_Hands_Button.setVisibility(View.VISIBLE);
                    Detailed_Join_Hands_Text.setVisibility(View.VISIBLE);
                    Detailed_Join_Hands.setVisibility(View.VISIBLE);
                    Detailed_Join_Hands.setText(detailed.getJoin_hands_members());
                }
                if(detailed.getDonation_web_page().isEmpty()){
                    Detailed_Donated_Count.setVisibility(View.INVISIBLE);
                    Detailed_Donate.setVisibility(View.INVISIBLE);
                    Detailed_Donation_Web_Page.setVisibility(View.GONE);
                    Detailed_Donation_Button.setVisibility(View.GONE);
                    Detailed_Donation_Web_Page_Text.setVisibility(View.GONE);
                }else{
                    Detailed_Donated_Count.setVisibility(View.VISIBLE);
                    Detailed_Donate.setVisibility(View.VISIBLE);
                    Detailed_Donation_Web_Page.setVisibility(View.VISIBLE);
                    Detailed_Donation_Button.setVisibility(View.VISIBLE);
                    Detailed_Donation_Web_Page.setText(detailed.getDonation_web_page());
                    Detailed_Donation_Web_Page_Text.setVisibility(View.VISIBLE);

                }

                isLiked(event_id,Detailed_Likes);
                isJoinedHands(event_id,Detailed_Join);
                isDonated(event_id,Detailed_Donate);
                isVolunteered(event_id,Detailed_Volunteer);
                getLikes(Detailed_Likes_Count, event_id);
//                getJoinedHands(Detailed_Joined_Count, event_id);
//                getDonated(Detailed_Donated_Count, event_id);
//                getVolunteered(Detailed_Volunteer_Count, event_id);
                checkDonation();
                checkJoin();
                checkVol();

                Detailed_Likes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Detailed_Likes.getTag().equals("like")) {
                            mLikesDatabase.child(detailed.getEvent_key()).child(mCurrentUserId).setValue(false);

                            if (!detailed.getUser_id().equals(mCurrentUserId)) {
                                mUsersDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Users users = dataSnapshot.getValue(Users.class);
                                        sendNotification(detailed.getUser_id(), "Liked your Event", users.getName(), mCurrentUserId);
                                        addNotification(user_id, event_id,"Liked your Event", true, false, false,false);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            
                        } else {
                            mLikesDatabase.child(detailed.getEvent_key()).child(mCurrentUserId).removeValue();
                        }

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Detailed_Volunteer_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_id.equals(mCurrentUserId)) {
                    if (Detailed_Volunteer.getTag().equals("volunteer")) {

                        mVolunteerCheckDatabase.child(event_id).child(mCurrentUserId).setValue(false);

                        mUsersDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Users users = dataSnapshot.getValue(Users.class);
                                ViewDialog alert = new ViewDialog();
                                alert.showDialog(DetailedActivity.this, "V", "", user_id, event_id, users.getName());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(DetailedActivity.this, "You are already Volunteer!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DetailedActivity.this, "You cannot request for Volunteer!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Detailed_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Detailed_Join_Hands_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_id.equals(mCurrentUserId)) {
                    if (Detailed_Join.getTag().equals("join")) {

                        mJoinHandsCheckDatabase.child(event_id).child(mCurrentUserId).setValue(false);

                        mUsersDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Users users = dataSnapshot.getValue(Users.class);
                                ViewDialog alert = new ViewDialog();
                                alert.showDialog(DetailedActivity.this, "J", "", user_id, event_id, users.getName());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(DetailedActivity.this, "You already request for Tie Up!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DetailedActivity.this, "You cannot request for Tie Up!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Detailed_Donation_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_id.equals(mCurrentUserId)){
                    if(Detailed_Donate.getTag().equals("donate")) {

                        mDonationCheckDatabase.child(event_id).child(mCurrentUserId).setValue(false);
                        mEventsDatabase.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Events detailed = dataSnapshot.getValue(Events.class);
                                ViewDialog alert = new ViewDialog();
                                String msg;
                                if (detailed.getDonation_web_page().startsWith("http://")) {
                                    msg = detailed.getDonation_web_page();
                                } else {
                                    msg = "http://" + detailed.getDonation_web_page();
                                }
                                alert.showDialog(DetailedActivity.this, "D", msg, user_id, event_id, detailed.getEvent_name());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(DetailedActivity.this, "You already donated!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DetailedActivity.this, "You cannot Donate!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPagerAdapter = new ImageSliderAdapter(this,event_id, count);

        Detailed_Slider.setSliderAdapter(viewPagerAdapter);

        Detailed_Slider.setIndicatorAnimation(IndicatorAnimations.WORM);
        Detailed_Slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        Detailed_Slider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        Detailed_Slider.setIndicatorSelectedColor(Color.YELLOW);
        Detailed_Slider.setIndicatorUnselectedColor(Color.BLUE);
        Detailed_Slider.setScrollTimeInSec(5);
        Detailed_Slider.startAutoCycle();

    }

    private void checkDonation() {
        mDonationCheckDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mCurrentUserId).exists()) {
                    Detailed_Donation_Button.setTag("sent");
                    Detailed_Donation_Button.setEnabled(false);
                } else {
                    Detailed_Donation_Button.setTag("donate");
                    Detailed_Donation_Button.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void checkVol() {
        mVolunteerCheckDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mCurrentUserId).exists()) {
                    Detailed_Volunteer_Button.setTag("sent");
                    Detailed_Volunteer_Button.setEnabled(false);
                } else {
                    Detailed_Volunteer_Button.setTag("vol");
                    Detailed_Volunteer_Button.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkJoin() {
        mJoinHandsCheckDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mCurrentUserId).exists()) {
                    Detailed_Join_Hands_Button.setTag("sent");
                    Detailed_Join_Hands_Button.setEnabled(false);
                } else {
                    Detailed_Join_Hands_Button.setTag("join");
                    Detailed_Join_Hands_Button.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getLikes(final TextView likes, String postId) {
        mLikesDatabase.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount() + "");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//
//    private void getVolunteered(final TextView volunteers, String postId) {
//        mVolunteerCheckDatabase.child(postId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String s=dataSnapshot.getValue().toString().substring(dataSnapshot.getValue().toString().indexOf("=") + 1, dataSnapshot.getValue().toString().indexOf("}"));
//                if(s.equals(true))
//                    volunteers.setText(dataSnapshot.getChildrenCount() + "");
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
//    private void getJoinedHands(final TextView joined, String postId) {
//
//        mJoinHandsCheckDatabase.child(postId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                joined.setText(dataSnapshot.getChildrenCount()+"");
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
//    private void getDonated(final TextView donations, String postId) {
//        mDonationCheckDatabase.child(postId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                donations.setText(dataSnapshot.getChildrenCount() + "");
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

        mVolunteerCheckDatabase.child(postid).addValueEventListener(new ValueEventListener() {
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

        mJoinHandsCheckDatabase.child(postid).addValueEventListener(new ValueEventListener() {
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

        mDonationCheckDatabase.child(postid).addValueEventListener(new ValueEventListener() {
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

    public class ViewDialog {

        public void showDialog(Activity activity, final String d, final String msg, final String user_id, final String event_id, final String name){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.single_layout_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (d.equals("D")){
                        sendNotification(user_id, "Donation Request", name, mCurrentUserId);
                        addNotification(user_id, event_id,"Donation Confirmation", false, false, false, true);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(msg));
                        startActivity(intent);
                    }else if(d.equals("V")){
                        sendNotification(user_id, "Volunteer Request", name, mCurrentUserId);
                        addNotification(user_id, event_id,"Volunteer Confirmation", false, false, true, false);
                    }else if(d.equals("J")){
                        sendNotification(user_id, "Tie Up Request", name, mCurrentUserId);
                        addNotification(user_id, event_id,"Tie Up Confirmation", false,true, false, false);
                    }
                    dialog.dismiss();

                }
            });
            dialog.show();

        }
    }

    private void UserInformation(String user_id) {

        mUsersDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Glide.with(DetailedActivity.this).load(user.getLogo()).into(Detailed_Profile_Image);
                Detailed_User_Name.setText(user.getName());
                if (user.getPhone_number().isEmpty()){
                    Detailed_Phone_Number.setVisibility(View.INVISIBLE);
                }else{
                    Detailed_Phone_Number.setText(user.getPhone_number());
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
                                            Toast.makeText(DetailedActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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

}
