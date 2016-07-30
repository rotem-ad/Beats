package com.example.rotem.beats.Model;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    float ratingSum;
    int ratersCount;

    public Playlist() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Playlist(String title, String author) {
        tags = new LinkedList<>();
        songList = new LinkedList<Song>();
        setTitle(title);
        setAuthor(author);
        // set current date as creation date
        setCreationDate(this.getCurrentDate());
        setPhoto(null);
        setRatingSum(0);
        setRatersCount(0);
        setId(null);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("author", author);
        result.put("title", title);
        result.put("creationDate", creationDate);
        result.put("photo", photo);
        result.put("tags", tags);
        result.put("songList", songList);
        result.put("ratingSum", ratingSum);
        result.put("ratersCount", ratersCount);

        return result;
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

    public float getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(float ratingSum) {
        this.ratingSum = ratingSum;
    }

    public int getRatersCount() {
        return ratersCount;
    }

    public void setRatersCount(int ratersCount) {
        this.ratersCount = ratersCount;
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

    private String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(Calendar.getInstance().getTime());
        return formattedDate;
    }
}
