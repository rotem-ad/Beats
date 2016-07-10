package com.example.rotem.beats.Model;

import android.content.Context;

import com.example.rotem.beats.MyApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rotem on 25/06/2016.
 */
public class Model {
    private final static Model instance = new Model();
    Context context;
    ModelFirebase modelFirebase;
    List<Playlist> data = new LinkedList<Playlist>(); // TODO: replace with firebase
    //ModelCoulinary modelCloudinary;
    //ModelSql modelSql;

    private Model(){
        context = MyApplication.getAppContext();
        modelFirebase = new ModelFirebase();
        //modelCloudinary = new ModelCoulinary();
        //modelSql = new ModelSql(MyApplication.getAppContext());
        seed();
    }

    public static Model getInstance(){
        return instance;
    }

    public interface AuthListener{
        void onDone(String userId, Exception e);
    }
    public void login(String email, String pwd, AuthListener listener){
        modelFirebase.login( email,  pwd,  listener);
    }
    public void signup(String email, String pwd, AuthListener listener){
        modelFirebase.signup( email,  pwd,  listener);
    }

    public String getUserId(){
        return modelFirebase.getUserId();
    }

    public String getUserEmail(){
        return modelFirebase.getUserEmail();
    }

    public void signout(){
        modelFirebase.signout();
    }

    public void add(Playlist pl){
        data.add(pl);
    }

    public void remove(Playlist pl){
        data.remove(pl);
    }

    public Playlist getPlaylist(String id){
        for (Playlist pl:data) {
            if(pl.getId().equals(id)){
                return pl;
            }
        }
        return null;
    }

    public List<Playlist> getAllPlaylists(){
        return data;
    }

    private void seed() {
        Playlist pl1 = new Playlist("123456","Rotem's Playlist","Rotem");
        Playlist pl2 = new Playlist("123123","John's Playlist","John");
        this.add(pl1);
        this.add(pl2);
    }
}
