package com.sociality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sociality.Adapter.TabsAdapter;
import com.sociality.Notification.Token;
import com.sociality.Settings.MyEventsActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private TextView mEventsTab,mProfileTab,mSettingsTab;
    private ViewPager mViewPager;
    private FloatingActionButton Add, NotificationButton;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUserId);
        mUsersDatabase.keepSynced(true);

        getViews();

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void updateToken(String refreshToken) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Token token = new Token(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mNewEvent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(mNewEvent);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

        @SuppressLint("RestrictedApi")
        private void getViews() {

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mEventsTab = (TextView) findViewById(R.id.EventsTab);
            mProfileTab = (TextView) findViewById(R.id.ProfileTab);
            mSettingsTab = (TextView) findViewById(R.id.SettingsTab);

            mViewPager =(ViewPager)findViewById(R.id.view_pager);
            Add = findViewById(R.id.add);
            NotificationButton = findViewById(R.id.notifications);
            Add.setVisibility(View.INVISIBLE);

            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.

                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            });

            mUsersDatabase.addValueEventListener(new ValueEventListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("user_key").getValue().equals("Individual")){
                        Add.setVisibility(View.INVISIBLE);

                    }else{
                        Add.setVisibility(VISIBLE);

                        Add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent mNewEvent = new Intent(MainActivity.this,NewEventActivity.class);
                                startActivity(mNewEvent);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            NotificationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mNewEvent = new Intent(MainActivity.this,NotificationsActivity.class);
                    startActivity(mNewEvent);
                }
            });

            mViewPager.setOffscreenPageLimit(3);

            TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(tabsAdapter);



            mEventsTab.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View view) {

                    mViewPager.setCurrentItem(0);
                    NotificationButton.setVisibility(VISIBLE);



                }
            });

            mProfileTab.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View view) {

                    mViewPager.setCurrentItem(1);
                    NotificationButton.setVisibility(GONE);


                }
            });

            mSettingsTab.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(2);
                    NotificationButton.setVisibility(GONE);


                }
            });

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    changeTabs(position);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    @SuppressLint("RestrictedApi")
        private void changeTabs(int position) {

            if(position == 0){
                NotificationButton.setVisibility(VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mEventsTab.setTextColor(getColor(R.color.colorWhite));
                }
                mEventsTab.setTextSize(20);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mProfileTab.setTextColor(getColor(R.color.colorWhite));
                }
                mProfileTab.setTextSize(15);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mSettingsTab.setTextColor(getColor(R.color.colorWhite));
                }
                mSettingsTab.setTextSize(15);

            }

            if(position == 1){
                NotificationButton.setVisibility(GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mProfileTab.setTextColor(getColor(R.color.colorWhite));
                }
                mProfileTab.setTextSize(20);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mSettingsTab.setTextColor(getColor(R.color.colorWhite));
                }
                mSettingsTab.setTextSize(15);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mEventsTab.setTextColor(getColor(R.color.colorWhite));
                }
                mEventsTab.setTextSize(15);

            }

            if(position == 2){
                NotificationButton.setVisibility(GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mSettingsTab.setTextColor(getColor(R.color.colorWhite));
                }
                mSettingsTab.setTextSize(20);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mProfileTab.setTextColor(getColor(R.color.colorWhite));
                }
                mProfileTab.setTextSize(15);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mEventsTab.setTextColor(getColor(R.color.colorWhite));
                }
                mEventsTab.setTextSize(15);

            }



    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
