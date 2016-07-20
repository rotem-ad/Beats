package com.example.rotem.beats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;

import java.util.List;

public class MyBeatsFragmentTab extends Fragment {

    Model model;
    ListView list;
    List<Playlist> data;
    MyAdapter adapter;
    ProgressBar progressBar;

    public MyBeatsFragmentTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_beats_tab, container, false);

        model = Model.getInstance();
        progressBar = (ProgressBar) rootView.findViewById(R.id.my_beats_progressbar);
        list = (ListView) rootView.findViewById(R.id.my_beats_playlist_listview);
        adapter = new MyAdapter();
        loadPlaylistsData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String playlistId = data.get(position).getId();
                Intent intent = new Intent(getActivity(), PlaylistDetailsActivity.class);
                intent.putExtra("PLAYLIST_ID", playlistId);
                startActivityForResult(intent,Constants.PLAYLIST_DETAILS);
            }
        });

        setHasOptionsMenu(true);

        return rootView;
    }


    void loadPlaylistsData(){
        progressBar.setVisibility(View.VISIBLE);
        String userId = model.getUserId();
        model.getPlaylistsByUser(userId, new Model.GetPlaylistsListener() {
            @Override
            public void onResult(List<Playlist> playlists) {
                data = playlists;
                list.setAdapter(adapter); // data must not be null at this point!
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                // in case user doesn't have playlists of his own
                if (playlists.size() == 0) {
                    Toast.makeText(MyApplication.getAppContext(), "You don't have any playlists" ,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void addNewPlaylist() {
        Intent intent = new Intent(getActivity(), PlaylistNewActivity.class);
        startActivityForResult(intent,Constants.PLAYLIST_NEW);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_beats, menu);
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
            case R.id.action_new_playlist:
                this.addNewPlaylist();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLAYLIST_DETAILS) { //TODO: maybe should be removed
            if(resultCode == Activity.RESULT_OK){
                loadPlaylistsData();
            }
        }

        if (requestCode == Constants.PLAYLIST_NEW) {
            if(resultCode == Activity.RESULT_OK){
               loadPlaylistsData();
            }
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getActivity());;
                convertView = inflater.inflate(R.layout.fragment_home_playlists_list_row, null);
                Log.d("TAG", "create view:" + position);
            }else{
                Log.d("TAG", "use convert view:" + position);
            }

            TextView title = (TextView) convertView.findViewById(R.id.playlist_list_row_title);
            TextView author = (TextView) convertView.findViewById(R.id.playlist_list_row_author);
            ImageView image = (ImageView) convertView.findViewById(R.id.playlist_list_row_image);

            Playlist playlist = data.get(position);

            title.setText(playlist.getTitle());
            author.setText("by " + playlist.getAuthor() + " at " + playlist.getCreationDate());

            return convertView;
        }
    }

}
