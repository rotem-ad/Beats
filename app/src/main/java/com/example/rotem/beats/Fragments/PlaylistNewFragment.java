package com.example.rotem.beats.Fragments;


import android.app.Activity;
import android.content.ContentResolver;
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
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.rotem.beats.Dialogs.ChangePhotoDialogFragment;
import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;
import com.example.rotem.beats.Model.Song;
import com.example.rotem.beats.MyApplication;
import com.example.rotem.beats.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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
    ImageButton btn_choose_image;

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

        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Constants.CHANGE_PHOTO);
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
        if ((requestCode == Constants.PICK_FROM_FILE) || (requestCode == Constants.PICK_FROM_CAMERA)) {
            Uri imageCaptureUri;
            String imageName;
            String path;
            Bitmap tempBitmap, bitmap;
            if (requestCode == Constants.PICK_FROM_FILE) { // from gallery
                imageCaptureUri = data.getData();
                path = getRealPathFromUri(imageCaptureUri);
                if (path == null)
                    path = imageCaptureUri.getPath();
                bitmap = BitmapFactory.decodeFile(path);
                imageName = String.valueOf(System.currentTimeMillis()) + "_" + path.substring(path.lastIndexOf("/") + 1);
            } else {  // from camera
                imageName = "tmp_cam_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                tempBitmap = data.getExtras().getParcelable("data");
                bitmap = codec(tempBitmap,Bitmap.CompressFormat.JPEG,100);
                //File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName);
                //bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            }
            playlist.setPhoto(imageName);
            model.saveImage(bitmap, imageName);
            image.setImageBitmap(bitmap);
        }
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
            case Constants.CHANGE_PHOTO:
                fragment = new ChangePhotoDialogFragment();
                break;
            default:
                fragment = null;
        }
        fragment.setTargetFragment(this, dialogCode);
        fragment.show(fm, "Display dialog");
    }

    // compress bitmap to specified format and quality
    private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
                                int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    private String getRealPathFromUri(Uri contentURI){
        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentURI, proj, null, null, null);
        if(cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
