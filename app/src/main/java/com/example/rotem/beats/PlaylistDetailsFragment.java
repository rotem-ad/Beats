package com.example.rotem.beats;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistDetailsFragment extends Fragment {

    Model model = Model.getInstance();
    ImageView image;
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

    private void init(View view) {
        image = (ImageView) view.findViewById(R.id.playlist_details_image);
        title = (TextView) view.findViewById(R.id.playlist_details_title);
        tags = (TextView) view.findViewById(R.id.playlist_details_tags);
        author = (TextView) view.findViewById(R.id.playlist_details_author);
        rating = (TextView) view.findViewById(R.id.playlist_details_rating);
        createDate = (TextView) view.findViewById(R.id.playlist_details_cdate);
        progressBar = (ProgressBar) view.findViewById(R.id.playlist_details_progressbar);

        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().GetPlaylistById(this.playlistId, new Model.GetPlaylist() {
            @Override
            public void onResult(final Playlist playlist) {
                if (playlist != null) {
                    if (playlist.getPhoto() != null) {
                        image.setImageBitmap( BitmapFactory.decodeFile(playlist.getPhoto()) );
                    }
                    title.setText(playlist.getTitle());
                    author.setText(playlist.getAuthor());
                    rating.setText(Integer.toString( playlist.getRating() ));
                    createDate.setText(playlist.getCreationDate());

                    String tagList = "";
                    if (playlist.getTags() != null) {
                        for (String tag: playlist.getTags() ) {
                            tagList = tagList + tag + " ";
                        }
                        tags.setText(tagList); // populate tags text view
                    }

                    // Display options menu only if current user is owner of this playlist
                    if (currentUser.equals(playlist.getAuthor())) {
                        setHasOptionsMenu(true);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

            }
        });


        Button playBtn = (Button) view.findViewById(R.id.playlist_details_play);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOnYouTube("Linkin Park in the end");
            }
        });

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

    private void playOnYouTube(String searchText) {
        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo")));
        //Log.i("Video", "Video Playing....");
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage(Constants.LOCAL_YOUTUBE_APP);
        intent.putExtra("query", searchText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*
    public void editPlaylist() {
        Intent intent = new Intent(getActivity(), StudentEditActivity.class);
        intent.putExtra("STUDENT_ID", st.getId());
        startActivityForResult(intent, 2);
    }
    */

}
