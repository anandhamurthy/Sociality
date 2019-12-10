package com.sociality.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.sociality.InterstitialAdActivity;
import com.sociality.R;

public class OpenSourceLibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);
        startActivity(new Intent(OpenSourceLibraryActivity.this, InterstitialAdActivity.class));
        WebView view = new WebView(this);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl("file:///android_asset/open_source_library.html");
        setContentView(view);
    }
}
