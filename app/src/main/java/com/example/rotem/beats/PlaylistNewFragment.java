package com.example.rotem.beats;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistNewFragment extends Fragment {

    Model model = Model.getInstance();
    EditText title;
    TextView tags;
    String author;
    Button addTagBtn;
    Button saveBtn;
    Button cancelBtn;
    Playlist playlist;

    public PlaylistNewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist_new, container, false);

        init(rootView);

//        addButton = (ImageButton) rootView.findViewById(R.id.playlist_button_add);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.image02);
//                model.saveImage(image,"aman_patuy.jpg");
//
//            }
//        });

        return rootView;
    }


    private void init(View view) {
        addTagBtn = (Button) view.findViewById(R.id.playlist_new_add_tag);
        saveBtn = (Button) view.findViewById(R.id.playlist_new_save);
        cancelBtn = (Button) view.findViewById(R.id.playlist_new_cancel);
        title = (EditText) view.findViewById(R.id.playlist_new_title);
        tags = (TextView) view.findViewById(R.id.playlist_new_tags);
        author = MyApplication.getCurrentUser();

        playlist = new Playlist(null,null);  // create new playlist object

        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get dialog_add_tag.xml view
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.dialog_add_tag, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setView(promptsView); // set dialog_add_tag.xml to alertdialog builder

                final EditText tagUserInput = (EditText) promptsView.findViewById(R.id.add_tag_input);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to Tags text view
                                        String tagList = "";
                                        playlist.getTags().add(tagUserInput.getText().toString());
                                        for (String tag : playlist.getTags()) {
                                            tagList = tagList + tag + " ";
                                        }
                                        tags.setText(tagList);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

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
                playlist.setTitle(titleUserInput);
                playlist.setAuthor(author);
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
