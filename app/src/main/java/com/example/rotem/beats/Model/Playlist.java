package com.example.rotem.beats.Model;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Rotem on 09/07/2016.
 */
public class Playlist {
    String id;
    String title;
    String author;
    String creationDate;
    String photo;
    List<String> tags;
    List<Song> songList;
    int rating;

    public Playlist(String id, String title, String author) {
        setId(id);
        setTitle(title);
        setAuthor(author);
        // set current date as creation date
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        String formattedDate = df.format(Calendar.getInstance().getTime());
        setCreationDate(formattedDate);
        setPhoto(null);
        setTags(null);
        setSongList(null);
        setRating(0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
