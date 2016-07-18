package com.example.rotem.beats.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rotem on 09/07/2016.
 */
public class User {

    String id;
    String name;
    String email;
    List<Playlist> playlists;

    // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    public User() {
    }

    public  User (String id, String name, String email) {
        setId(id);
        setName(name);
        setEmail(email);
        setPlaylists(null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("email", email);
        result.put("playlists", playlists);
        return result;
    }
}
