package com.example.ziyuhuang.picshot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imageView = (ImageView) findViewById(R.id.grid_item_page);
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("image");

        Picasso.with(this).load(imageUrl).into(imageView);
    }
}
