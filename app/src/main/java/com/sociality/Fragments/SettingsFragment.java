package com.sociality.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.sociality.Settings.MyEventsActivity;
import com.sociality.R;
import com.sociality.Settings.CreditActivity;
import com.sociality.Settings.OpenSourceLibraryActivity;
import com.sociality.Settings.PrivacyPolicyActivity;
import com.sociality.StartActivity;
import com.sociality.Settings.TermsAndConditionsActivity;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }


    private Button Settings_Logout, Settings_Private_Policy, Settings_Open_Sources_Library, Setting_Terms_And_Conditions, Settings_Credits, Settings_My_Events;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        Setting_Terms_And_Conditions = view.findViewById(R.id.settings_terms_and_condition);
        Settings_Logout = view.findViewById(R.id.settings_logout);
        Settings_Private_Policy = view.findViewById(R.id.settings_private_policy);
        Settings_Open_Sources_Library = view.findViewById(R.id.settings_open_sources_library);
        Settings_Credits = view.findViewById(R.id.settings_credits);
        Settings_My_Events = view.findViewById(R.id.settings_my_events);

        Setting_Terms_And_Conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TermsAndConditionsActivity.class));
            }
        });

        Settings_My_Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyEventsActivity.class));
            }
        });

        Settings_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), StartActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                getActivity().finish();
            }
        });

        Settings_Private_Policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
            }
        });

        Settings_Credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreditActivity.class));
            }
        });

        Settings_Open_Sources_Library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OpenSourceLibraryActivity.class));
            }
        });

        return view;

    }
}