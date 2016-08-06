package com.example.rotem.beats.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.rotem.beats.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public interface GetPlaylistRating{
        void onResult(float newRating);
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

    public void findPlaylists(String tag, String author, final GetPlaylistsListener listener) {
        modelFirebase.findPlaylists(tag, author, listener);
    }

    public void GetPlaylistById(String id, GetPlaylist listener){
        modelFirebase.getPlaylistById(id, listener);
    }

    public void updatePlaylistRating(final String id, final float userRating, final Model.GetPlaylistRating listener) {
        modelFirebase.updatePlaylistRating(id, userRating, listener);
    }

    public void removePlaylistById(String id, DelPlaylist listener){
        modelFirebase.removePlaylistById(id, listener);
    }

    public void addPlaylist(Playlist playlist, final Model.AddPlaylistListener listener) {
        modelFirebase.addPlaylist(playlist, listener);
    }

    public void updatePlaylist (final String id, Playlist playlist, final Model.AddPlaylistListener listener) {
        modelFirebase.updatePlaylist(id,playlist,listener);
    }

    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        saveImageToFile(imageBitmap,imageName); // synchronously save image locally
        Thread d = new Thread(new Runnable() {  // asynchronously save image to Cloudinary
            @Override
            public void run() {
                modelCloudinary.saveImage(imageBitmap,imageName);
            }
        });
        d.start();
    }

    public interface LoadImageListener{
        public void onResult(Bitmap imageBmp);
    }

    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bmp = loadImageFromFile(imageName);              //first try to find the image on the device
                if (bmp == null) {                                      //if image not found - try downloading it from parse
                    bmp = modelCloudinary.loadImage(imageName);
                    if (bmp != null) saveImageToFile(bmp,imageName);    //save the image locally for next time
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                listener.onResult(result);
            }
        };
        task.execute();
    }

    public void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        OutputStream out = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //add the picture to the gallery so we don't need to manage the cache size
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
            Log.d("saveImageToFile","add image to cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);

            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("loadImageFromFile","got image from cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }











}
