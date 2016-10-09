package com.example.ziyuhuang.picshot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * class to display the images url that stored in the database
 */
public class DisplaySavePhotos extends AppCompatActivity {

    TextView textView;
    String photosRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_save_photos);
        textView = (TextView) findViewById(R.id.result);
        Intent intent = getIntent();
        photosRecord = intent.getStringExtra("photos");
        showData();
    }

    public void showData(){
        textView.setText(photosRecord);
    }
}
