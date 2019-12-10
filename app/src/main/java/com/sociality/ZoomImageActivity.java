package com.sociality;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ZoomImageActivity extends AppCompatActivity {

    private ImageView Zoom_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        String image_url = getIntent().getStringExtra("image_url");

        Zoom_Image = findViewById(R.id.zoom_image);
        Glide.with(ZoomImageActivity.this).load(image_url).into(Zoom_Image);

    }
}
