package com.sociality;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.sociality.Fragments.DonationsFragment;
import com.sociality.Fragments.JoinedFragment;
import com.sociality.Fragments.VolunteerFragment;
import com.sociality.Settings.MyEventsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MyWorksActivity extends AppCompatActivity {

    private TabLayout My_Works_Tab_Layout;
    private ViewPager My_Works_View_Pager;
    private ImageView My_Works_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_works);

        My_Works_Back = findViewById(R.id.my_works_back);

        My_Works_View_Pager = findViewById(R.id.my_works_view_pager);
        setupViewPager(My_Works_View_Pager);

        My_Works_Tab_Layout = findViewById(R.id.my_works_tab_layout);
        My_Works_Tab_Layout.setupWithViewPager(My_Works_View_Pager);

        setupTabIcons();

        My_Works_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupTabIcons() {

        ImageView tabOne = (ImageView) LayoutInflater.from(getApplication()).inflate(R.layout.custom_tab, null);
        tabOne.setImageResource(R.drawable.donation_empty);
        My_Works_Tab_Layout.getTabAt(0).setIcon(R.drawable.donation_empty);

        ImageView tabTwo = (ImageView) LayoutInflater.from(getApplication()).inflate(R.layout.custom_tab, null);
        tabOne.setImageResource(R.drawable.join_hands_empty);
        My_Works_Tab_Layout.getTabAt(1).setIcon(R.drawable.join_hands_empty);

        ImageView tabThree = (ImageView) LayoutInflater.from(getApplication()).inflate(R.layout.custom_tab, null);
        tabOne.setImageResource(R.drawable.volunteer_empty);
        My_Works_Tab_Layout.getTabAt(2).setIcon(R.drawable.volunteer_empty);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DonationsFragment(), "Donation");
        adapter.addFragment(new JoinedFragment(), "Join Hands");
        adapter.addFragment(new VolunteerFragment(), "Volunteer");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
