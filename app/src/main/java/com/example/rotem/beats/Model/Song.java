package com.example.rotem.beats.Model;

/**
 * Created by Rotem on 09/07/2016.
 */
public class Song {
    String title;
    String artist;
    String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
