package com.udacity.getfit.dao;

public class FavoriteData {

    public String videoName;
    public String videoId;

    public FavoriteData() {
    }

    public FavoriteData(String videoName, String videoId) {
        this.videoName = videoName;
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
