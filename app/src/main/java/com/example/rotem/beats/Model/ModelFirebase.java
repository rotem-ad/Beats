package com.example.rotem.beats.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rotem.beats.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Rotem on 25/06/2016.
 */
public class ModelFirebase {

    FirebaseDatabase database;
    DatabaseReference dbRef;

    List<Playlist> playlistData; // TODO: replace with Firebase

    public ModelFirebase() {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        //seed();
    }

    public void signup(String email, String password, final Model.AuthListener listener){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            listener.onDone(FirebaseAuth.getInstance().getCurrentUser().getUid(),null);
                        }else{
                            listener.onDone(null,task.getException());
                        }
                    }
                });
    }

    public void login(String email, String password, final Model.AuthListener listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            listener.onDone(FirebaseAuth.getInstance().getCurrentUser().getUid(),null);
                        }else{
                            listener.onDone(null,task.getException());
                        }
                    }
                });
    }


    public String getUserId() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }else{
            return null;
        }
    }

    public String getUserEmail() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }else{
            return null;
        }
    }

    public void signout() {
        FirebaseAuth.getInstance().signOut();
    }


    public void getAllPlaylistsAsynch(final Model.GetPlaylistsListener listener) {
        DatabaseReference playlists = dbRef.child(Constants.PLAYLISTS_COLLECTION); // ref to playlists collection
        Query queryRef = playlists.orderByChild("author");

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final List<Playlist> plList = new LinkedList<Playlist>();
                Log.d("getAllPlaylistsAsynch", "read " + snapshot.getChildrenCount() + " new playlists");
                for (DataSnapshot plSnapshot : snapshot.getChildren()) {
                    Playlist playlist = plSnapshot.getValue(Playlist.class);
                    Log.d("getAllPlaylistsAsynch", playlist.getAuthor() + " - " + playlist.getTitle());
                    plList.add(playlist);
                }
                listener.onResult(plList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getAllPlaylistsAsynch", "The read failed: " + databaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void getPlaylistById(String id, final Model.GetPlaylist listener) {
        DatabaseReference playlistRef = dbRef.child(Constants.PLAYLISTS_COLLECTION).child(id);
        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Playlist playlist = snapshot.getValue(Playlist.class);
                Log.d("getPlaylistById", playlist.getAuthor() + " - " + playlist.getTitle());
                listener.onResult(playlist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getPlaylistById", "The read failed: " + databaseError.getMessage());
                listener.onCancel();
            }
        });
    }


    private void seed() {
        generateData();
        DatabaseReference playlists = dbRef.child("playlists");
        Map<String, Object> childUpdates = new HashMap<>();

        for (Playlist playlist : playlistData) {
            String playlistId = playlists.push().getKey(); // generate key in DB
            playlist.setId(playlistId); // set id to current playlist object based on its DB key
            Map<String, Object> playlistValues = playlist.toMap();
            childUpdates.put("/playlists/" + playlistId, playlistValues);
        }

        dbRef.updateChildren(childUpdates);
    }

    private void generateData() {
        playlistData = new LinkedList<Playlist>();
        playlistData.add(new Playlist("Rotem's Playlist","Rotem"));
        playlistData.add(new Playlist("John's Playlist","John"));
        playlistData.add(new Playlist("Stas Playlist","Stas"));
    }

}
