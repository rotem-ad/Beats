package com.example.rotem.beats;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rotem.beats.Model.Song;

import java.util.List;

/**
 * Created by Rotem on 25/07/2016.
 */
public class SongListAdapter extends BaseAdapter {

    private List<Song> data;
    private Context context;


    public SongListAdapter (Context context, List list) {
        this.context = context;
        this.data = list;
    }

    private void playOnYouTube(String searchText) {
        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo")));
        //Log.i("Video", "Video Playing....");
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage(Constants.LOCAL_YOUTUBE_APP);
        intent.putExtra("query", searchText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

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
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.songs_list_view_row, null);
            Log.d("songs_list_view", "create view:" + position);
        }else{
            Log.d("songs_list_view", "use convert view:" + position);
        }

        TextView title = (TextView) convertView.findViewById(R.id.song_list_row_title);
        TextView artist = (TextView) convertView.findViewById(R.id.song_list_row_artist);
        ImageButton playBtn = (ImageButton) convertView.findViewById(R.id.song_list_row_play_btn);

        final Song song = data.get(position);

        title.setText(song.getTitle());
        artist.setText("by " + song.getArtist());

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOnYouTube(song.getArtist() + " " + song.getTitle());
            }
        });

        return convertView;
    }
}
