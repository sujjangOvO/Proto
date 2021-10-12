package com.example.proto;

import android.graphics.drawable.Drawable;
import android.widget.Button;

public class HlocationItem {
    private String store_name;
    private String store_location;
    private int photo;

    public HlocationItem(){}

    public HlocationItem(String store_name, String store_location,int photo) {
        this.store_name = store_name;
        this.store_location = store_location;
        this.photo=photo;
    }

    public String getStore_name() { return store_name; }

    public void setStore_name(String store_name) { this.store_name = store_name; }

    public String getStore_location() { return store_location; }

    public void setStore_location(String store_location) { this.store_location = store_location; }

    public int getPhoto() { return photo; }

    public void setPhoto(int photo) { this.photo = photo; }
}
