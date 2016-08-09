package com.example.rotem.beats.Fragments;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rotem.beats.Activities.PlaylistDetailsActivity;
import com.example.rotem.beats.Activities.PlaylistNewActivity;
import com.example.rotem.beats.Adapters.PlaylistListAdapter;
import com.example.rotem.beats.Constants;
import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;
import com.example.rotem.beats.MyApplication;
import com.example.rotem.beats.R;

import java.util.LinkedList;
import java.util.List;

public class MyBeatsFragmentTab extends Fragment {

    Model model = Model.getInstance();
    ListView list;
    List<Playlist> data;
    PlaylistListAdapter adapter;
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
        data = new LinkedList<>();
        adapter = new PlaylistListAdapter(getActivity(),data);
        loadPlaylistsData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String playlistId = data.get(position).getId();
                Intent intent = new Intent(getActivity(), PlaylistDetailsActivity.class);
                intent.putExtra("PLAYLIST_ID", playlistId);
                startActivityForResult(intent, Constants.PLAYLIST_DETAILS);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Playlist")
                        .setMessage("Are you sure you want to delete this playlist?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String playlistId = data.get(pos).getId();
                                model.removePlaylistById(playlistId, new Model.DelPlaylist() {
                                    @Override
                                    public void onComplete(String result) {
                                        loadPlaylistsData();
                                        Log.d("MyBeatsFragTab ","LongClick");
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                Log.v("long clicked","pos: " + pos);

                return true;
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
                adapter.setData(data);
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
            case R.id.action_new_playlist2:
                this.addNewPlaylist();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLAYLIST_DETAILS) {
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
}
