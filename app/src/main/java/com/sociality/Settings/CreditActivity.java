package com.sociality.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.android.material.tabs.TabLayout;
import com.sociality.InterstitialAdActivity;

public class CreditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(CreditActivity.this, InterstitialAdActivity.class));
        WebView view = new WebView(this);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl("file:///android_asset/credit.html");
        setContentView(view);


    }
}
