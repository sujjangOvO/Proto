package com.example.proto;

public class HpostingItem {
    private String posting_Title;
    private String posting_Location;
    private String posting_Contents;
    private int photo;

    public HpostingItem(String posting_Title, String posting_Location, String posting_Contents, int photo) {
        this.posting_Title = posting_Title;
        this.posting_Location = posting_Location;
        this.posting_Contents = posting_Contents;
        this.photo = photo;
    }

    public String getPosting_Title() { return posting_Title; }

    public void setPosting_Title(String posting_Title) { this.posting_Title = posting_Title; }

    public String getPosting_Location() { return posting_Location; }

    public void setPosting_Location(String posting_Location) { this.posting_Location = posting_Location; }

    public String getPosting_Contents() { return posting_Contents; }

    public void setPosting_Contents(String posting_Contents) { this.posting_Contents = posting_Contents; }

    public int getPhoto() { return photo; }

    public void setPhoto(int photo) { this.photo = photo; }
}
