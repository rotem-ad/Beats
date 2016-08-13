package com.example.rotem.beats.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rotem.beats.Constants;
import com.example.rotem.beats.MyApplication;
import com.example.rotem.beats.R;

/**
 * Created by Rotem on 31/07/2016.
 */
public class AddSongDialogFragment extends DialogFragment {

    private String artistInput;
    private String titleInput;

    public AddSongDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get dialog_add_song.xml view
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.dialog_add_song, null);

        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(promptsView); // set dialog_add_song.xml to alertdialog builder

        final EditText artistUserInput = (EditText) promptsView.findViewById(R.id.add_song_artist);
        final EditText titleUserInput = (EditText) promptsView.findViewById(R.id.add_song_title);

        if (getArguments() != null) {
            artistUserInput.setText(getArguments().getString("CURRENT_ARTIST"));
            titleUserInput.setText(getArguments().getString("CURRENT_TITLE"));
        }

        // set dialog message
        builder
                .setTitle(R.string.dialog_add_song_title)
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and pass it to invoking fragment
                                artistInput = artistUserInput.getText().toString();
                                titleInput = titleUserInput.getText().toString();

                                // validate non empty input
                                if  (artistInput.isEmpty() || titleInput.isEmpty()) {
                                    Toast.makeText(MyApplication.getAppContext(), "Artist/Title can't be empty" ,
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                sendResult(Constants.GET_SONG);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void sendResult(int requestCode) {
        Intent intent = new Intent();
        intent.putExtra("ARTIST",artistInput);
        intent.putExtra("TITLE",titleInput);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), requestCode, intent);
    }

}
