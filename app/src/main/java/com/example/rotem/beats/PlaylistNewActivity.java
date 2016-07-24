package com.example.rotem.beats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.io.File;

public class PlaylistNewActivity extends AppCompatActivity {

    private PlaylistNewFragment playlistNewFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_new);


        // create instance of PlaylistNewFragment
        playlistNewFragment = new PlaylistNewFragment();

        // attach PlaylistNewFragment fragment to activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.playlist_new_frag_container, playlistNewFragment, "attach PlaylistNewFragment");
        transaction.commit();






    }









}
