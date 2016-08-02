package com.example.rotem.beats.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotem.beats.Adapters.SongListAdapter;
import com.example.rotem.beats.Constants;
import com.example.rotem.beats.Dialogs.AddSongDialogFragment;
import com.example.rotem.beats.Dialogs.AddTagDialogFragment;
import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;
import com.example.rotem.beats.Model.Song;
import com.example.rotem.beats.MyApplication;
import com.example.rotem.beats.R;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistNewFragment extends Fragment {

    Model model = Model.getInstance();
    List<Song> data;
    ListView songsList;
    SongListAdapter adapter;
    ImageView image;
    EditText title;
    TextView tags;
    String author;
    Button addTagBtn;
    Button addSongBtn;
    Button saveBtn;
    Button cancelBtn;
    Playlist playlist;

    /************ Change Photo Variables ************/
    private Uri imageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    ImageButton btn_choose_image;
    /***********************************************/

    public PlaylistNewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_playlist_new, container, false);

        songsList = (ListView) rootView.findViewById(R.id.playlist_new_songs_listview);
        data = new LinkedList<>();
        adapter = new SongListAdapter(getActivity(),data);

        init(rootView);


        /****************************** Change Photo Process - Start *******************************/
        final String [] items = new String[]{"From Camera", "From Gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(), "tmp_avater" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    imageCaptureUri = Uri.fromFile(file);
                    try {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                        intent.putExtra("return data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    dialog.cancel();
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        });
        final AlertDialog dialog = builder.create();

        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        /******************************* Change Photo Process - End *****************************************/

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
        btn_choose_image = (ImageButton) view.findViewById(R.id.playlist_button_change_photo);
        addSongBtn = (Button) view.findViewById(R.id.playlist_new_add_song);
        addTagBtn = (Button) view.findViewById(R.id.playlist_new_add_tag);
        saveBtn = (Button) view.findViewById(R.id.playlist_new_save);
        cancelBtn = (Button) view.findViewById(R.id.playlist_new_cancel);
        image = (ImageView) view.findViewById(R.id.playlist_new_image);
        title = (EditText) view.findViewById(R.id.playlist_new_title);
        tags = (TextView) view.findViewById(R.id.playlist_new_tags);
        author = MyApplication.getCurrentUser();

        playlist = new Playlist(null,null);  // create new playlist object

        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Constants.GET_TAG);
            }
        });

        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Constants.GET_SONG);
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
                playlist.setSongList(data); // attach all songs to playlist
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // add new tag
        if (requestCode == Constants.GET_TAG) {
            String tagInput = data.getStringExtra("TAG");
            String tagList = "";
            playlist.getTags().add(tagInput);
            for (String tag : playlist.getTags()) {
                tagList = tagList + tag + " ";
            }
            tags.setText(tagList);
        }

        // add new song
        if (requestCode == Constants.GET_SONG) {
            String artistInput = data.getStringExtra("ARTIST");
            String titleInput = data.getStringExtra("TITLE");
            addSong(artistInput,titleInput);
            loadSongsData();
        }

        // change photo
        if(resultCode != Activity.RESULT_OK)
            return;
        Bitmap bitmap = null;
        String path;
        if(requestCode == PICK_FROM_FILE){
            imageCaptureUri = data.getData();
            path = getRealPathFromUri(imageCaptureUri);
            if(path == null)
                path = imageCaptureUri.getPath();
            if(path != null)
                bitmap = BitmapFactory.decodeFile(path);
        }else{
            path =  imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }

        /*
        *   Now the bitmap variable holds the photo we've been selected
        *   either by choosing it from the gallery or photo which has been taken by camera.
        *   Now we need to start manipulate it.
         */

        Bitmap rotated;

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        rotated = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);


        playlist.setPhoto(imageCaptureUri.getPath());
        image.setImageBitmap(rotated);

    }

    public String getRealPathFromUri(Uri contentURI){
        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentURI, proj, null, null, null);
        if(cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void loadSongsData(){
        songsList.setAdapter(adapter); // data must not be null at this point!
        adapter.notifyDataSetChanged();
    }

    private void addSong(String artist, String title) {
        Song song = new Song();
        song.setArtist(artist);
        song.setTitle(title);
        String songId = String.valueOf(data.size());
        song.setId(songId);
        data.add(song); // add song to list
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