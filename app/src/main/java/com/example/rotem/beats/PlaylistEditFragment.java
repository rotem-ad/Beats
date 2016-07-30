package com.example.rotem.beats;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.rotem.beats.Dialogs.AddTagDialogFragment;
import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistEditFragment extends Fragment {

    Model model = Model.getInstance();
    ListView songsList;
    SongListAdapter adapter;
    ImageView image;
    TextView title;
    TextView tags;
    ProgressBar progressBar;
    String playlistId;
    Button addTagBtn;

    boolean modified;


    public PlaylistEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_playlist_edit, container, false);

        init(rootView);
        modified = false;
        return rootView;

    }

    private void init(final View view) {
        image = (ImageView) view.findViewById(R.id.playlist_edit_image);
        title = (EditText) view.findViewById(R.id.playlist_edit_title);
        tags = (TextView) view.findViewById(R.id.playlist_edit_tags);
        //addSongBtn = (Button) view.findViewById(R.id.playlist_edit_add_song);
        addTagBtn = (Button) view.findViewById(R.id.playlist_edit_add_tag);
        progressBar = (ProgressBar) view.findViewById(R.id.playlist_edit_progressbar);
        songsList = (ListView) view.findViewById(R.id.playlist_edit_songs_listview);

        progressBar.setVisibility(View.VISIBLE);
        model.GetPlaylistById(this.playlistId, new Model.GetPlaylist() {
            @Override
            public void onResult(final Playlist playlist) {
                if (playlist != null) {
                    if (playlist.getPhoto() != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(playlist.getPhoto());
                        image.setImageBitmap(bitmap);
                    }

                    title.setText(playlist.getTitle());

                    String tagList = "";
                    if (playlist.getTags() != null) {
                        for (String tag : playlist.getTags()) {
                            tagList = tagList + tag + " ";
                        }
                        tags.setText(tagList); // populate tags text view
                    }

                    if (playlist.getSongList() != null) { // there is at least 1 song
                        adapter = new SongListAdapter(getActivity(), playlist.getSongList()); // populate songs list
                        songsList.setAdapter(adapter);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

            }
        });

        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addTagDialog = new AddTagDialogFragment();
                addTagDialog.show(getFragmentManager(), "Display add tag dialog");
            }
        });
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

}
