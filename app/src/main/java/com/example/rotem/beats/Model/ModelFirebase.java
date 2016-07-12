package com.example.rotem.beats.Model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Rotem on 25/06/2016.
 */
public class ModelFirebase {

    FirebaseDatabase database;
    DatabaseReference dbRef;

    public ModelFirebase() {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        // TODO: need to test
        Playlist pl1 = new Playlist("123456","Rotem's Playlist","Rotem");
        Playlist pl2 = new Playlist("123123","John's Playlist","John");
        dbRef.child("playlists").child("id1").setValue(pl1);
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
}
