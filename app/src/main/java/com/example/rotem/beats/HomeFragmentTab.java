package com.example.rotem.beats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;

import java.util.List;

public class HomeFragmentTab extends Fragment {

    ListView list;
    List<Playlist> data;
    MyAdapter adapter;
    ProgressBar progressBar;

    public HomeFragmentTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_home_tab, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.home_progressbar);
        list = (ListView) rootView.findViewById(R.id.playlist_listview);
        loadPlaylistsData();
        //data = Model.getInstance().getAllPlaylists()
        adapter = new MyAdapter();
        list.setAdapter(adapter);

        /*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), StudentDetailsActivity.class);
                intent.putExtra("STUDENT_ID", data.get(position).getId());
                startActivityForResult(intent,1);
            }
        });
        */

        return rootView;
    }


    void loadPlaylistsData(){
        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getAllPlaylistsAsynch(new Model.GetPlaylistsListener() {
            @Override
            public void onResult(List<Playlist> playlists) {
                progressBar.setVisibility(View.GONE);
                data = playlists; // data is null
                adapter.notifyDataSetChanged(); // adapter is null
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

            Playlist st = data.get(position);

            title.setText(st.getTitle());
            author.setText("by " + st.getAuthor() + " at " + st.getCreationDate());

            return convertView;
        }
    }

}
