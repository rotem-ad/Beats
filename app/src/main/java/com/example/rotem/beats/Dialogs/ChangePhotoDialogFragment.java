package com.example.rotem.beats.Dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.rotem.beats.Constants;
import com.example.rotem.beats.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by Rotem on 31/07/2016.
 */
public class ChangePhotoDialogFragment extends DialogFragment {

    private Uri imageCaptureUri;

    public ChangePhotoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String [] items = new String[]{"From Camera", "From Gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) { // from camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //File file = new File(Environment.getExternalStorageDirectory(), "tmp_avater" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                    //String imageFileName = "tmp_cam_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    //File imageFile = new File(dir, imageFileName);
                    //imageCaptureUri = Uri.fromFile(imageFile);
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                    //intent.putExtra("return data", true);
                    getTargetFragment().startActivityForResult(intent, Constants.PICK_FROM_CAMERA);
                    dialog.cancel();
                } else { // from gallery
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    getTargetFragment().startActivityForResult(Intent.createChooser(intent, "Complete action using"), Constants.PICK_FROM_FILE);
                }
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
