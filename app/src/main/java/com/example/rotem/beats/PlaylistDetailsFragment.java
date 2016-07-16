package com.example.rotem.beats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistDetailsFragment extends Fragment {

    Playlist playlist;
    TextView title;
    TextView author;
    TextView rating;
    TextView createDate;
    ProgressBar progressBar;
    String playlistId;
    
    boolean modified;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_playlist_details, container, false);

        init(rootView);
        modified = false;

      return rootView;
    }

    private void init(View view) {
        title = (TextView) view.findViewById(R.id.playlist_details_title);
        author = (TextView) view.findViewById(R.id.playlist_details_author);
        rating = (TextView) view.findViewById(R.id.playlist_details_rating);
        createDate = (TextView) view.findViewById(R.id.playlist_details_cdate);
        progressBar = (ProgressBar) view.findViewById(R.id.playlist_details_progressbar);

        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().GetPlaylistById(this.playlistId, new Model.GetPlaylist() {
            @Override
            public void onResult(Playlist playlist) {
                title.setText(playlist.getTitle());
                author.setText(playlist.getAuthor());
                rating.setText(Integer.toString( playlist.getRating() ));
                createDate.setText(playlist.getCreationDate());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public boolean isModified() {
        return modified;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    /*
    public void editPlaylist() {
        Intent intent = new Intent(getActivity(), StudentEditActivity.class);
        intent.putExtra("STUDENT_ID", st.getId());
        startActivityForResult(intent, 2);
    }
    */

}
