package com.example.proto;

public class FriendsFoodItem {
    private int item_id;
    private String item_title;
    private String item_addr;

    public FriendsFoodItem(int item_id,String item_title, String item_addr) {
        this.item_id=item_id;
        this.item_title = item_title;
        this.item_addr = item_addr;
    }

    public int getItem_id() { return item_id; }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_addr() {
        return item_addr;
    }

    public void setItem_addr(String item_addr) {
        this.item_addr = item_addr;
    }
}
