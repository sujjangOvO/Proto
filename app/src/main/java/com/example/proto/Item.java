package com.example.proto;

// 맛집 리스트의 각 게시물 DATA

public class Item {
    private int item_id;
    private String item_title;
    private String item_addr;
    private String item_user;


    public Item(int item_id, String item_title, String item_addr, String item_user) {
        this.item_id = item_id;
        this.item_title = item_title;
        this.item_addr = item_addr;
        this.item_user = item_user;
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

    public String getItem_user() {
        return item_user;
    }

    public void setItem_user(String item_user) {
        this.item_user = item_user;
    }
}
