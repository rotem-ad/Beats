package com.example.rotem.beats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.rotem.beats.Model.Model;

public class PlaylistNewActivity extends AppCompatActivity {

    private PlaylistNewFragment playlistNewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_new);

        // create instance of PlaylistNewFragment
        PlaylistNewFragment playlistNewFragment = new PlaylistNewFragment();



        // attach PlaylistNewFragment fragment to activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.playlist_new_frag_container, playlistNewFragment, "attach PlaylistNewFragment");
        transaction.commit();

    }












}
