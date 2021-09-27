package com.example.proto;

public class UserAccount {
//    private String idToken;
    private String id;
    private String pw;
    private String nickname;
    private String phone;
    private String style;

    public UserAccount(){}

//    public String getIdToken(){return idToken;}
//
//    public void setIdToken(String idToken){this.idToken=idToken;}

    public String getId(){
        return id;
    }

    public void setId(String id){this.id=id;}

    public String getPw(){return pw;}

    public void setPw(String pw){this.pw=pw;}

    public String getNickname(){return nickname;}

    public void setNickname(String nickname){this.nickname=nickname;}

    public String getPhone(){ return phone;}

    public void setPhone(String phone){ this.phone = phone; }

    public String getStyle(){ return style;}

    public void setStyle(String style){ this.style = style; }

    public UserAccount(String id, String pw, String nickname, String phone, String style){
        this.id=id;
        this.pw=pw;
        this.nickname=nickname;
        this.phone=phone;
        this.style=style;
    }
}
