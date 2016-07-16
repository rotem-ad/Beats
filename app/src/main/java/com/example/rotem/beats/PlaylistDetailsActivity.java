package com.example.rotem.beats;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlaylistDetailsActivity extends AppCompatActivity {

    private PlaylistDetailsFragment playlistDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_details);

        String playlistId = (String) getIntent().getExtras().get("PLAYLIST_ID"); // get playlist ID from main activity

        // create instance of PlaylistDetailsFragment
        playlistDetailsFragment = new PlaylistDetailsFragment();
        playlistDetailsFragment.setPlaylistId(playlistId);

        // attach PlaylistDetailsFragment fragment to activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.playlist_details_frag_container, playlistDetailsFragment, "attach PlaylistDetailsFragment");
        transaction.commit();
    }
}
