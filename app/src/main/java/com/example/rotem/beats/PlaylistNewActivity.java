package com.example.rotem.beats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.io.File;

public class PlaylistNewActivity extends AppCompatActivity {

    private PlaylistNewFragment playlistNewFragment;
//    private Uri imageCaptureUri;
//    private static final int PICK_FROM_CAMERA = 1;
//    private static final int PICK_FROM_FILE = 2;
//    ImageButton btn_choose_image;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_new);



//        final String [] items = new String[]{"From Camera", "From Gallery"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select Image");
//        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(which == 0) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File file = new File(Environment.getExternalStorageDirectory(), "tmp_avater" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    imageCaptureUri = Uri.fromFile(file);
//                    try {
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
//                        intent.putExtra("return data", true);
//
//                        startActivityForResult(intent, PICK_FROM_CAMERA);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                    dialog.cancel();
//                }else{
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
//
//
//                }
//            }
//        });
//        final AlertDialog dialog = builder.create();
//        btn_choose_image = (ImageButton) findViewById(R.id.playlist_button_change_photo);
//        btn_choose_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.show();
//            }
//        });






        // create instance of PlaylistNewFragment
        playlistNewFragment = new PlaylistNewFragment();

        // attach PlaylistNewFragment fragment to activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.playlist_new_frag_container, playlistNewFragment, "attach PlaylistNewFragment");
        transaction.commit();






    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode != RESULT_OK)
//            return;
//        Bitmap bitmap = null;
//        String path = "";
//        if(requestCode == PICK_FROM_FILE){
//            imageCaptureUri = data.getData();
//            path = getRealPathFromUri(imageCaptureUri);
//            if(path == null)
//                path = imageCaptureUri.getPath();
//            if(path != null)
//                bitmap = BitmapFactory.decodeFile(path);
//        }else{
//            path =  imageCaptureUri.getPath();
//            bitmap = BitmapFactory.decodeFile(path);
//        }
//
//
//    }
//
//    public String getRealPathFromUri(Uri contentURI){
//        String [] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(contentURI, proj, null, null, null);
//        if(cursor == null)
//            return null;
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//
//
//    }








}
