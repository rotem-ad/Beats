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

import com.example.rotem.beats.Constants;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(promptsView); // set dialog_add_song.xml to alertdialog builder

        final EditText artistUserInput = (EditText) promptsView.findViewById(R.id.add_song_artist);
        final EditText titleUserInput = (EditText) promptsView.findViewById(R.id.add_song_title);

        // set dialog message
        builder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and pass it to invoking fragment
                                artistInput = artistUserInput.getText().toString();
                                titleInput = titleUserInput.getText().toString();
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