package com.example.rotem.beats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistDetailsFragment extends Fragment {

    Model model = Model.getInstance();
    ListView songsList;
    SongListAdapter adapter;
    ImageView image;
    RatingBar ratingBar;
    TextView title;
    TextView tags;
    TextView author;
    TextView rating;
    TextView createDate;
    ProgressBar progressBar;
    String playlistId;
    String currentUser = MyApplication.getCurrentUser();

    
    boolean modified;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_playlist_details, container, false);
        init(rootView);
        modified = false;
        return rootView;
    }

    private void init(final View view) {
        image = (ImageView) view.findViewById(R.id.playlist_details_image);
        title = (TextView) view.findViewById(R.id.playlist_details_title);
        ratingBar = (RatingBar) view.findViewById(R.id.playlist_details_rating_bar);
        tags = (TextView) view.findViewById(R.id.playlist_details_tags);
        author = (TextView) view.findViewById(R.id.playlist_details_author);
        rating = (TextView) view.findViewById(R.id.playlist_details_rating);
        createDate = (TextView) view.findViewById(R.id.playlist_details_cdate);
        progressBar = (ProgressBar) view.findViewById(R.id.playlist_details_progressbar);
        songsList = (ListView) view.findViewById(R.id.playlist_details_songs_listview);

        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().GetPlaylistById(this.playlistId, new Model.GetPlaylist() {
            @Override
            public void onResult(final Playlist playlist) {
                if (playlist != null) {
                    // Display options menu only if current user is owner of this playlist
                    if (currentUser.equals(playlist.getAuthor())) {
                        setHasOptionsMenu(true);
                    }

                    if (playlist.getPhoto() != null) {
                        Bitmap original = BitmapFactory.decodeFile(playlist.getPhoto());
                        Bitmap rotated;

                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        rotated = Bitmap.createBitmap(original, 0, 0,
                                original.getWidth(), original.getHeight(),
                                matrix, true);

                        image.setImageBitmap(rotated);
                    }

                    // set stars according to playlist rating
                    float plRating = playlist.getRatingSum()/playlist.getRatersCount();
                    if (plRating > 0) {
                        displayRating(plRating);
                    }

                    title.setText(playlist.getTitle());
                    author.setText(playlist.getAuthor());
                    createDate.setText(playlist.getCreationDate());

                    if (playlist.getSongList() != null) { // there is at least 1 song
                        adapter = new SongListAdapter(getActivity(), playlist.getSongList()); // populate songs list
                        songsList.setAdapter(adapter);
                    }

                    String tagList = "";
                    if (playlist.getTags() != null) {
                        for (String tag: playlist.getTags() ) {
                            tagList = tagList + tag + " ";
                        }
                        tags.setText(tagList); // populate tags text view
                    }


                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

            }
        });

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    openRatingDialog();
                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }
        });

        /*

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(MyApplication.getAppContext(), "RatingBar clicked" ,
                        Toast.LENGTH_SHORT).show();
            }
        });

        */



    }

    public boolean isModified() {
        return modified;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_playlist_details, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Handle item selection
        switch (id) {
            case R.id.action_del_playlist:
                this.removePlaylist();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removePlaylist() {
        model.removePlaylistById(playlistId, new Model.DelPlaylist() {
            @Override
            public void onComplete(String result) {
                if (result.equals("SUCCESS")) {
                    Toast.makeText(MyApplication.getAppContext(), "Playlist deleted" ,
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish(); // close current activity
                }
            }

            @Override
            public void onCancel() {}
        });


    }

    private void openRatingDialog() {
        // get dialog_rate_playlist.xml view
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.dialog_rate_playlist, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView); // set dialog_add_song.xml to alertdialog builder

        final TextView ratingUserInput = (TextView) promptsView.findViewById(R.id.dialog_rate_rating);
        final RatingBar userRatingBar = (RatingBar) promptsView.findViewById(R.id.dialog_rate_rating_bar);

        userRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingUserInput.setText(Float.toString(userRatingBar.getRating()));
            }
        });

        // set dialog message
        alertDialogBuilder
                .setTitle(R.string.dialog_rate_playlist_title)
                .setCancelable(true)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input
                                updatePlaylistRating(userRatingBar.getRating());
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

    private void updatePlaylistRating(float userRating) {
        Toast.makeText(MyApplication.getAppContext(), "Updating rating with new value: " + userRating ,
                Toast.LENGTH_SHORT).show();
        model.updatePlaylistRating(playlistId, userRating, new Model.GetPlaylistRating() {
            @Override
            public void onResult(float newRating) {
                displayRating(newRating);
            }
        });
    }

    private void displayRating(float playlistRating) {
        ratingBar.setRating(playlistRating);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2); // limit rating precision to 2 digits
        String ratingStr = Float.toString(playlistRating);
        rating.setText(df.format(playlistRating));
    }

    /*
    public void editPlaylist() {
        Intent intent = new Intent(getActivity(), StudentEditActivity.class);
        intent.putExtra("STUDENT_ID", st.getId());
        startActivityForResult(intent, 2);
    }
    */

}
