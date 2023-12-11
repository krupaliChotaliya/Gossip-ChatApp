package com.chatsapp.goosip.model.user;

public class User {

    private String uid;
    private String name;
    private String about;
    private String profileImg;
    private String active="1";
    private String status;

    public User(String uid, String name, String about, String profileImg, String active, String status) {
        this.uid = uid;
        this.name = name;
        this.about = about;
        this.profileImg = profileImg;
        this.active = active;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User() {
    }
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
