package com.example.rotem.beats.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.rotem.beats.Utils;

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
    ImageButton changePhotoBtn;

    Bitmap photoBitmap;
    String imageName;

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
        changePhotoBtn = (ImageButton) view.findViewById(R.id.playlist_button_change_photo);
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

        changePhotoBtn.setOnClickListener(new View.OnClickListener() {
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

                if (photoBitmap != null) {
                    model.saveImage(photoBitmap, imageName);
                }

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
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            Uri imageCaptureUri;
            String path;
            Bitmap tempBitmap;
            if (requestCode == Constants.PICK_FROM_FILE) { // from gallery
                imageCaptureUri = data.getData();
                path = Utils.getRealPathFromUri(getActivity(),imageCaptureUri);
                if (path == null)
                    path = imageCaptureUri.getPath();
                photoBitmap = BitmapFactory.decodeFile(path);
                imageName = String.valueOf(System.currentTimeMillis()) + "_" + path.substring(path.lastIndexOf("/") + 1);
            } else {  // from camera
                imageName = "tmp_cam_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                tempBitmap = data.getExtras().getParcelable("data");
                tempBitmap = Utils.codec(tempBitmap,Bitmap.CompressFormat.JPEG,100);
                photoBitmap = Utils.rotateImage(tempBitmap,90);
                //File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName);
                //bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            }

            image.setImageBitmap(photoBitmap);
            playlist.setPhoto(imageName);

            /*

            // galaxy s4

            image.setVisibility(View.VISIBLE);
            image.post(new Runnable() {
                @Override
                public void run() {
                    image.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                    image.invalidate();
                    image.requestLayout();
                }
            });
            */
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

}
