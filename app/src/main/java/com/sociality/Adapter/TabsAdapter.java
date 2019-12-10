package com.sociality.Adapter;

import com.sociality.Fragments.EventsFragment;
import com.sociality.Fragments.JoinedFragment;
import com.sociality.Fragments.ProfileFragment;
import com.sociality.Fragments.SettingsFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter {

    public TabsAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                EventsFragment events = new EventsFragment();
                return events;
            case 1:
                ProfileFragment profile = new ProfileFragment();
                return profile;
            case 2:
                SettingsFragment settings = new SettingsFragment();
                return settings;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }

}