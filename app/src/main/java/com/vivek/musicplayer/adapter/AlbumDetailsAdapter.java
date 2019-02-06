package com.vivek.musicplayer.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vivek.musicplayer.R;
import com.vivek.musicplayer.helper.Song;

import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.AlbumDetailViewHolder>{
    Context context;
    ArrayList<Song> songsList;

    public AlbumDetailsAdapter(Context context, ArrayList<Song> songsList) {
        this.context = context;
        this.songsList = songsList;
    }

    @Override
    public AlbumDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_detail_list, parent, false);
        return new AlbumDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumDetailViewHolder holder, int position) {
        final Song song = songsList.get(position);

        holder.songNumber.setText("" + (position+1));
        holder.songName.setText(song.getSongTitle());
        holder.songDuration.setText("" + song.getSongDuration());

        holder.albumSongItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Playing " + song.getSongTitle(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    class AlbumDetailViewHolder extends RecyclerView.ViewHolder {
        View albumSongItem;
        TextView songNumber;
        TextView songName;
        TextView songDuration;

        AlbumDetailViewHolder(View itemView) {
            super(itemView);
            albumSongItem = itemView.findViewById(R.id.albumSongItem);
            songNumber = itemView.findViewById(R.id.songNumberAlbumDetail);
            songName = itemView.findViewById(R.id.songNameAlbumDetail);
            songDuration = itemView.findViewById(R.id.songDurationAlbumDetail);
        }
    }
}
