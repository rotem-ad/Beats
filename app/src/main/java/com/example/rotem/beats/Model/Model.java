package com.example.rotem.beats.Model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.rotem.beats.MyApplication;

import java.util.List;

/**
 * Created by Rotem on 25/06/2016.
 */
public class Model {
    private final static Model instance = new Model();
    Context context;
    ModelFirebase modelFirebase;
    ModelCloudinary modelCloudinary;

    //ModelSql modelSql;

    public interface GetPlaylistsListener{
        void onResult(List<Playlist> playlists);
        void onCancel();
    }

    public interface GetPlaylist{
        void onResult(Playlist playlist);
        void onCancel();
    }

    public interface DelPlaylist{
        void onComplete(String result);
        void onCancel();
    }

    public interface GetUserListener{
        void onResult(String userName);
        void onCancel();
    }

    public interface AddPlaylistListener{
        void onComplete(String result);
    }

    public interface GetImageListener{
        public void onDone(Bitmap image, String imageName);
    }


    private Model(){
        context = MyApplication.getAppContext();
        modelFirebase = new ModelFirebase();
        modelCloudinary = new ModelCloudinary();
        //modelSql = new ModelSql(MyApplication.getAppContext());
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

    public void addUser(User user){
        modelFirebase.addUser(user);
    }

    public String getUserId(){
        return modelFirebase.getUserId();
    }

    public String getUserEmail(){
        return modelFirebase.getUserEmail();
    }

    public void getUserNameById(String userId, final GetUserListener listener) {
        modelFirebase.getUserNameById(userId, listener);
    }

    public void signout(){
        modelFirebase.signout();
    }

    public void getAllPlaylistsAsynch(final GetPlaylistsListener listener) {
        modelFirebase.getAllPlaylistsAsynch(listener);
    }

    public void getPlaylistsByUser(String userId, final GetPlaylistsListener listener) {
        modelFirebase.getPlaylistsByUser(userId, listener);
    }

    public void GetPlaylistById(String id, GetPlaylist listener){
        modelFirebase.getPlaylistById(id, listener);
    }

    public void removePlaylistById(String id, DelPlaylist listener){
        modelFirebase.removePlaylistById(id, listener);
    }

    public void addPlaylist(Playlist playlist, final Model.AddPlaylistListener listener) {
        modelFirebase.addPlaylist(playlist, listener);
    }


    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        // synchronously save image locally
        //saveImageToFile(imageBitmap,imageName);
        // asynchronously save image to cloudinary
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                modelCloudinary.saveImage(imageBitmap,imageName);
            }
        });
        thread.start();
    }

    public void getImage(String imageName, GetImageListener listener){
        modelCloudinary.getImage(imageName, listener);
    }











}
