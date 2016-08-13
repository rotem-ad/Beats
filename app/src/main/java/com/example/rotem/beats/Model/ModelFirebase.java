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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Rotem on 25/06/2016.
 */
public class ModelFirebase {

    FirebaseDatabase database;
    DatabaseReference dbRef;

    public ModelFirebase() {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
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

    public void addUser(User user) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userValues = user.toMap();
        childUpdates.put("/users/" + user.getId(), userValues);
        dbRef.updateChildren(childUpdates);
    }

    public void getUserNameById(String userId, final Model.GetUserListener listener) {
        DatabaseReference userRef = dbRef.child(Constants.USERS_COLLECTION).child(userId); // ref to user
        Query queryRef = userRef.child("name");

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String userName = snapshot.getValue(String.class);
                listener.onResult(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getUserById", "The read failed: " + databaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void signout() {
        FirebaseAuth.getInstance().signOut();
    }


    public void getAllPlaylistsAsynch(final Model.GetPlaylistsListener listener) {
        DatabaseReference playlistRef = dbRef.child(Constants.USERS_COLLECTION); // ref to users collection
        final List<Playlist> plList = new LinkedList<Playlist>();
        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) { // for each user in collection
                    for (DataSnapshot plSnapshot : userSnapshot.child(Constants.PLAYLISTS_COLLECTION).getChildren()) { // for each user's playlist
                        Playlist playlist = plSnapshot.getValue(Playlist.class);
                        Log.d("getAllPlaylistsAsynch", playlist.getAuthor() + " - " + playlist.getTitle());
                        plList.add(playlist);
                    }
                }
                Collections.sort(plList, new PlayListByDateComparer()); // sort playlists by date
                listener.onResult(plList);
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getAllPlaylistsAsynch", "The read failed: " + databaseError.getMessage());
                listener.onCancel();
            }
        });
    }


    public void getPlaylistsByUser(String userId, final Model.GetPlaylistsListener listener) {
        DatabaseReference userRef = dbRef.child(Constants.USERS_COLLECTION).child(userId); // ref to user
        Query queryRef = userRef.child(Constants.PLAYLISTS_COLLECTION).orderByChild("title");

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final List<Playlist> plList = new LinkedList<Playlist>();
                Log.d("getPlaylistsByUser", "read " + snapshot.getChildrenCount() + " new playlists");
                for (DataSnapshot plSnapshot : snapshot.getChildren()) {
                    Playlist playlist = plSnapshot.getValue(Playlist.class);
                    Log.d("getPlaylistsByUser", playlist.getAuthor() + " - " + playlist.getTitle());
                    plList.add(playlist);
                }
                Collections.sort(plList, new PlayListByDateComparer()); // sort playlists by date
                listener.onResult(plList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getPlaylistsByUser", "The read failed: " + databaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void findPlaylists(final String byTag, final String byAuthor, final Model.GetPlaylistsListener listener) {
        DatabaseReference playlistRef = dbRef.child(Constants.USERS_COLLECTION); // ref to users collection
        final List<Playlist> plList = new LinkedList<Playlist>();
        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) { // for each user in collection
                    for (DataSnapshot plSnapshot : userSnapshot.child(Constants.PLAYLISTS_COLLECTION).getChildren()) { // for each user's playlist
                        Playlist playlist = plSnapshot.getValue(Playlist.class);
                        Log.d("findPlaylists", playlist.getAuthor() + " - " + playlist.getTitle());

                        // there is a value in author search field and not in tag field
                        if ( !(byAuthor.isEmpty()) && (byTag.isEmpty()) ) {
                            // do case insensitive search for author
                            boolean foundAuthor = Pattern.compile(Pattern.quote(byAuthor), Pattern.CASE_INSENSITIVE).matcher(playlist.getAuthor()).find();
                            if (foundAuthor) {
                                plList.add(playlist);
                            }
                        }

                        // there is a value in tag search field and not in author field
                        if ( (byAuthor.isEmpty()) && !(byTag.isEmpty()) ) {
                            if (playlist.getTags() != null) {
                                for (String tag : playlist.getTags()) { // check if tag is found in playlist
                                    if (byTag.equalsIgnoreCase(tag)) {
                                        plList.add(playlist);
                                    }
                                }
                            }
                        }

                        // there is a value in both search fields
                        if ( !(byAuthor.isEmpty()) && !(byTag.isEmpty()) ) {
                            // do case insensitive search for author
                            boolean foundAuthor = Pattern.compile(Pattern.quote(byAuthor), Pattern.CASE_INSENSITIVE).matcher(playlist.getAuthor()).find();
                            if (foundAuthor) { // author was found
                                if (playlist.getTags() != null) {
                                    for (String tag : playlist.getTags()) { // check if tag is found in playlist
                                        if (byTag.equalsIgnoreCase(tag)) {
                                            plList.add(playlist);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                listener.onResult(plList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getPlaylistsByTag", "The read failed: " + databaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void updatePlaylistRating(final String id, final float userRating, final Model.GetPlaylistRating listener) {

        DatabaseReference playlistRef = dbRef.child(Constants.USERS_COLLECTION); // ref to users collection
        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) { // for each user in collection
                    for (DataSnapshot plSnapshot : userSnapshot.child(Constants.PLAYLISTS_COLLECTION).getChildren()) { // for each user's playlist
                        Playlist playlist = plSnapshot.getValue(Playlist.class);
                        Log.d("updatePlaylistRating", playlist.getAuthor() + " - " + playlist.getTitle());
                        if (playlist.getId().equals(id)) { // check if playlist is found
                            int newRatersCount = playlist.getRatersCount()+1;
                            float newRatingSum = playlist.getRatingSum() + userRating;
                            float newRating = newRatingSum/newRatersCount;
                            plSnapshot.getRef().child("ratersCount").setValue(newRatersCount); // increment raters count by 1
                            plSnapshot.getRef().child("ratingSum").setValue(newRatingSum); // update ratingSum
                            listener.onResult(newRating);
                            return;
                        }
                    }
                }
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("updatePlaylistRating", "The read failed: " + databaseError.getMessage());
            }
        });

    }


    public void getPlaylistById(final String id, final Model.GetPlaylist listener) {
        DatabaseReference playlistRef = dbRef.child(Constants.USERS_COLLECTION); // ref to users collection
        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) { // for each user in collection
                    for (DataSnapshot plSnapshot : userSnapshot.child(Constants.PLAYLISTS_COLLECTION).getChildren()) { // for each user's playlist
                        Playlist playlist = plSnapshot.getValue(Playlist.class);
                        Log.d("getPlaylistById", playlist.getAuthor() + " - " + playlist.getTitle());
                        if (playlist.getId().equals(id)) { // check if playlist is found
                            listener.onResult(playlist);
                            return;
                        }
                    }
                }
                listener.onResult(null); // playlist was not found
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getPlaylistById", "The read failed: " + databaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void removePlaylistById(final String id, final Model.DelPlaylist listener) {
        final DatabaseReference playlistsRef = dbRef.child(Constants.USERS_COLLECTION).child(this.getUserId())
                .child(Constants.PLAYLISTS_COLLECTION); // ref to user's playlists
        playlistsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot plSnapshot : snapshot.getChildren()) { // for each playlist in collection
                        if (plSnapshot.getKey().equals(id)) { // check if playlist is found
                            Log.d("removePlaylistById", "removing playlist with id " + plSnapshot.getKey());
                            // remove the playlist from DB
                            plSnapshot.getRef().removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    listener.onComplete("SUCCESS");
                                }
                            });
                            return;
                        }
                }
                listener.onComplete(null); // playlist id was not found
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("removePlaylistById", "Deletion failed: " + databaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void addPlaylist (Playlist playlist, final Model.AddPlaylistListener listener) {
        DatabaseReference userPlaylists = dbRef.child(Constants.USERS_COLLECTION).child(this.getUserId()).child("playlists"); // ref to current user's playlists
        String playlistId = userPlaylists.push().getKey(); // generate key in DB
        playlist.setId(playlistId); // set id to current playlist object based on its DB key
        userPlaylists.child(playlistId).setValue(playlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete("SUCCESS");
            }
        });

        /*
        Map<String, Object> childUpdates = new HashMap<>();
        String playlistId = userPlaylists.push().getKey(); // generate key in DB
        playlist.setId(playlistId); // set id to current playlist object based on its DB key
        Map<String, Object> playlistValues = playlist.toMap();
        childUpdates.put(playlistId , playlistValues);
        userPlaylists.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete("SUCCESS");
            }
        });
        */
    }

    public void updatePlaylist (final String id, Playlist playlist, final Model.AddPlaylistListener listener) {
        DatabaseReference playlistRef = dbRef.child(Constants.USERS_COLLECTION).child(this.getUserId())
                .child(Constants.PLAYLISTS_COLLECTION).child(id); // ref to current playlist
        playlistRef.setValue(playlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete("SUCCESS");
            }
        });
    }
}
