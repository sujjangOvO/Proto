package com.example.proto;

// 맛집 리스트의 각 게시물 DATA

import java.io.Serializable;

public class Item implements Serializable {
    private String title;
    private String body;
    private String name;
    private String address;
    private String user;
    private String type;
    private String score;
    private String waiting;
    private String open;
    private String imageUrl;
//    private String open; //0은 친구공개 1은 전체공개임


    public Item(){}

    public Item(String user, String name, String title,String body, String address, String type,
                String score,String waiting, String imageUrl) {
        this.user=user;
        this.name=name;
        this.title=title;
        this.body=body;
        this.address=address;
        this.type=type;
        this.score=score;
        this.waiting=waiting;
        this.open=null;
        this.imageUrl = imageUrl;

    }

    public Item(String user, String name, String title,String body, String address, String type, String score,String waiting) {
        this.user=user;
        this.name=name;
        this.title=title;
        this.body=body;
        this.address=address;
        this.type=type;
        this.score=score;
        this.waiting=waiting;
        this.open=null;
        this.imageUrl=null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getWaiting() {
        return waiting;
    }

    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }


}
