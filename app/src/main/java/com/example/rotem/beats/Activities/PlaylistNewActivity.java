package com.example.rotem.beats.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.rotem.beats.Fragments.PlaylistNewFragment;
import com.example.rotem.beats.R;

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
