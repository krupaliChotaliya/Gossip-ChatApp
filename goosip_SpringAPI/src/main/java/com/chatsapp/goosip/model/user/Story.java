package com.chatsapp.goosip.model.user;

public class Story {
    private String uid;
    private String story;
    private Long timestamp;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Story() {
    }

    public Story(String uid, String story, Long timestamp) {
        this.uid = uid;
        this.story = story;
        this.timestamp = timestamp;
    }
}
