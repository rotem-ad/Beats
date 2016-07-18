package com.example.rotem.beats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;

import java.util.List;

public class MyBeatsFragmentTab extends Fragment {

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
                startActivityForResult(intent,1); // TODO: change 1 to constant
            }
        });

        return rootView;
    }


    void loadPlaylistsData(){
        progressBar.setVisibility(View.VISIBLE);
        String userId = Model.getInstance().getUserId();
        Model.getInstance().getPlaylistsByUser(userId, new Model.GetPlaylistsListener() {
            @Override
            public void onResult(List<Playlist> playlists) {
                data = playlists;
                list.setAdapter(adapter); // data must not be null at this point!
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

            }
        });
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
