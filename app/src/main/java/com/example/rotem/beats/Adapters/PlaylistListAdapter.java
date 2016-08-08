package com.example.rotem.beats.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.Model.Playlist;
import com.example.rotem.beats.R;

import java.util.List;

/**
 * Created by Rotem on 25/07/2016.
 */

public class PlaylistListAdapter extends BaseAdapter {

    private Model model = Model.getInstance();
    private List<Playlist> data;
    private Context context;


    public PlaylistListAdapter(Context context, List list) {
        this.context = context;
        this.data = list;
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
            convertView = inflater.inflate(R.layout.fragment_home_playlists_list_row, null);
            Log.d("TAG", "create view:" + position);
        }else{
            Log.d("TAG", "use convert view:" + position);
        }

        TextView title = (TextView) convertView.findViewById(R.id.playlist_list_row_title);
        TextView author = (TextView) convertView.findViewById(R.id.playlist_list_row_author);
        final ImageView image = (ImageView) convertView.findViewById(R.id.playlist_list_row_image);

        Playlist playlist = data.get(position);

        // set photo
        if (playlist.getPhoto() != null) {
            final ProgressBar imageProgress = (ProgressBar) convertView.findViewById(R.id.playlist_list_image_progress);
            imageProgress.setVisibility(View.VISIBLE);
            model.loadImage(playlist.getPhoto(), new Model.LoadImageListener() {
                @Override
                public void onResult(Bitmap imageBmp) {
                    imageProgress.setVisibility(View.GONE);
                    image.setImageBitmap(imageBmp);
                }
            });
        }
        else // playlist photo is null
        {
            image.setImageResource(R.drawable.beats2);
        }

        title.setText(playlist.getTitle());
        author.setText("by " + playlist.getAuthor() + " at " + playlist.getCreationDate());

        return convertView;
    }
}
