package com.example.rotem.beats;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlaylistEditActivity extends AppCompatActivity {

    private PlaylistEditFragment playlistEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_edit);

        String playlistId = (String) getIntent().getExtras().get("PLAYLIST_ID"); // get playlist ID from playlist details activity

        // create instance of PlaylistEditFragment
        playlistEditFragment = new PlaylistEditFragment();
        playlistEditFragment.setPlaylistId(playlistId);

        // attach PlaylistDetailsFragment fragment to activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.playlist_edit_frag_container, playlistEditFragment, "attach PlaylistEditFragment");
        transaction.commit();

    }
}
