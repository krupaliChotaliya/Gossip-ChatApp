package com.chatsapp.goosip.model.user;

import java.util.ArrayList;

public class UserStory {

    private String uid;
    private String name;
    private String ProfileImg;

    public UserStory(String uid, String name, String profileImg, ArrayList<Story> stories) {
        this.uid = uid;
        this.name = name;
        this.ProfileImg = profileImg;
        this.stories = stories;
    }

    private ArrayList<Story> stories;

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

    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }

    public UserStory() {
    }

    public String getProfileImg() {
        return ProfileImg;
    }

    public void setProfileImg(String profileImg) {
        this.ProfileImg = profileImg;
    }
}
