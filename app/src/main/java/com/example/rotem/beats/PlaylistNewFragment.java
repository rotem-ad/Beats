package com.example.rotem.beats;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistNewFragment extends Fragment {

    Model model;
    EditText title;
    EditText tags;
    String author;
    Button saveBtn;
    Button cancelBtn;


    public PlaylistNewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist_new, container, false);
        init(rootView);
        return rootView;
    }


    private void init(View view) {

        model = Model.getInstance();
        saveBtn = (Button) view.findViewById(R.id.playlist_new_save);
        cancelBtn = (Button) view.findViewById(R.id.playlist_new_cancel);
        title = (EditText) view.findViewById(R.id.playlist_new_title);
        tags = (EditText) view.findViewById(R.id.playlist_new_tags);
        model.getUserNameById(model.getUserId(), new Model.GetUserListener() {
            @Override
            public void onResult(String userName) {
                author = userName;
            }

            @Override
            public void onCancel() {

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
                String tagsUserInput = String.valueOf(tags.getText());
                Playlist playlist = new Playlist(titleUserInput,author); // create new playlist object
                //TODO: tags
                //TODO: songs
                model.addPlaylist(playlist, new Model.AddPlaylistListener() { // add new playlist to DB
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

}
