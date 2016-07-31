package com.example.rotem.beats;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotem.beats.Dialogs.AddSongDialogFragment;
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
    Button addSongBtn;
    Button saveBtn;
    Button cancelBtn;
    Playlist mPlaylist;

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

        saveBtn = (Button) view.findViewById(R.id.playlist_edit_save);
        cancelBtn = (Button) view.findViewById(R.id.playlist_edit_cancel);
        //addSongBtn = (Button) view.findViewById(R.id.playlist_edit_add_song);
        addTagBtn = (Button) view.findViewById(R.id.playlist_edit_add_tag);
        progressBar = (ProgressBar) view.findViewById(R.id.playlist_edit_progressbar);
        songsList = (ListView) view.findViewById(R.id.playlist_edit_songs_listview);

        progressBar.setVisibility(View.VISIBLE);
        model.GetPlaylistById(this.playlistId, new Model.GetPlaylist() {
            @Override
            public void onResult(final Playlist playlist) {
                mPlaylist = playlist;
                if (mPlaylist != null) {
                    if (mPlaylist.getPhoto() != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(mPlaylist.getPhoto());
                        image.setImageBitmap(bitmap);
                    }

                    title.setText(mPlaylist.getTitle());

                    String tagList = "";
                    if (mPlaylist.getTags() != null) {
                        for (String tag : mPlaylist.getTags()) {
                            tagList = tagList + tag + " ";
                        }
                        tags.setText(tagList); // populate tags text view
                    }

                    if (mPlaylist.getSongList() != null) { // there is at least 1 song
                        adapter = new SongListAdapter(getActivity(), mPlaylist.getSongList()); // populate songs list
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
                openDialog(Constants.GET_TAG);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                getActivity().setResult(Activity.RESULT_CANCELED, intent);
                getActivity().finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleUserInput = String.valueOf(title.getText());
                mPlaylist.setTitle(titleUserInput);
                model.updatePlaylist(playlistId,mPlaylist, new Model.AddPlaylistListener() { // update playlist to DB
                    @Override
                    public void onComplete(String result) {
                        if (result.equals("SUCCESS")) {
                            Toast.makeText(MyApplication.getAppContext(), "Playlist saved successfully" ,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent intent = new Intent();
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure fragment codes match up
        if (requestCode == Constants.GET_TAG) {
            String tagInput = data.getStringExtra("TAG");
            String tagList = "";
            mPlaylist.getTags().add(tagInput);
            for (String tag : mPlaylist.getTags()) {
                tagList = tagList + tag + " ";
            }
            tags.setText(tagList);
        }
    }

    private void openDialog (int dialogCode) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment fragment;
        switch (dialogCode) {
            case Constants.GET_TAG:
                fragment = new AddTagDialogFragment();
                break;
            case Constants.GET_SONG:
                fragment = new AddSongDialogFragment();
                break;
            default:
                fragment = null;
        }
        fragment.setTargetFragment(this, dialogCode);
        fragment.show(fm, "Display dialog");
    }

}
