package com.example.rotem.beats.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;
import com.example.rotem.beats.Activities.PlaylistDetailsActivity;
import com.example.rotem.beats.R;

import java.util.List;

public class HomeFragmentTab extends Fragment {

    ListView list;
    List<Playlist> data;
    MyAdapter adapter;
    ProgressBar progressBar;
    String searchTag;
    String searchAuthor;

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
        adapter = new MyAdapter();
        loadPlaylistsData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String playlistId = data.get(position).getId();
                //Toast.makeText(MyApplication.getAppContext(),"Playlist ID: " + playlistId , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), PlaylistDetailsActivity.class);
                intent.putExtra("PLAYLIST_ID", playlistId);
                startActivityForResult(intent,1); // TODO: change 1 to constant
            }
        });

        setHasOptionsMenu(true);

        return rootView;
    }


    void loadPlaylistsData(){
        progressBar.setVisibility(View.VISIBLE);
        String userId = Model.getInstance().getUserId();
        Model.getInstance().getAllPlaylistsAsynch(new Model.GetPlaylistsListener() {
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
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
            case R.id.action_seach_playlist:
                this.searchPlaylists();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchPlaylists() {

        // get dialog_search_playlistss.xml view
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.dialog_search_playlists, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView); // set dialog_search_playlistss.xml to alertdialog builder

        final EditText tagUserInput = (EditText) promptsView.findViewById(R.id.search_by_tag);
        final EditText authorUserInput = (EditText) promptsView.findViewById(R.id.search_by_author);

        // set dialog message
        alertDialogBuilder
                .setTitle(R.string.action_search_playlist)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input
                                searchTag = tagUserInput.getText().toString();
                                searchAuthor = authorUserInput.getText().toString();

                                // in case both search fields are empty, do nothing
                                if (searchTag.isEmpty() && searchAuthor.isEmpty()) {
                                    return;
                                }

                                progressBar.setVisibility(View.VISIBLE);
                                Model.getInstance().findPlaylists(searchTag,searchAuthor,new Model.GetPlaylistsListener() {
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
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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
