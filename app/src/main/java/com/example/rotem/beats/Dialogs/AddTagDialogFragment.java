package com.example.rotem.beats.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rotem.beats.Model.Playlist;
import com.example.rotem.beats.R;

/**
 * Created by Rotem on 31/07/2016.
 */
public class AddTagDialogFragment extends DialogFragment {
    private Playlist playlist;
    private TextView tags;



    public AddTagDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get dialog_add_tag.xml view
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.dialog_add_tag, null);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(promptsView); // set dialog_add_tag.xml to alertdialog builder
        final EditText tagUserInput = (EditText) promptsView.findViewById(R.id.add_tag_input);

        // set dialog message
        builder
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

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
