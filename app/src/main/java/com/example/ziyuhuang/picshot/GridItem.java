package com.example.ziyuhuang.picshot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * create GridItem object class
 */
public class GridItem {
    private String image;
    private String title;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}