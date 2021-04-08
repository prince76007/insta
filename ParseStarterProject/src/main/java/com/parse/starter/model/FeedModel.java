package com.parse.starter.model;

import android.graphics.Bitmap;

public class FeedModel {
    private Bitmap pic;
    private String username;

    public FeedModel(){

    }

    public FeedModel(Bitmap pic, String username) {
        this.pic = pic;
        this.username = username;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
