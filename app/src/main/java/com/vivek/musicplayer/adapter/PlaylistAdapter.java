package com.vivek.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vivek.musicplayer.helper.Playlist;
import com.vivek.musicplayer.R;


import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>{
    ArrayList<Playlist> playlists;

    public PlaylistAdapter(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View playlistView = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new PlaylistViewHolder(playlistView);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        holder.playlistCover.setImageResource(playlist.getPlaylistCover());
        holder.playlistName.setText(playlist.getPlaylistTitle());
        holder.noOfSongs.setText("" + playlist.getTotalSongs());
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder{
        ImageView playlistCover;
        TextView playlistName;
        TextView noOfSongs;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            playlistCover = itemView.findViewById(R.id.playlistArt);
            playlistName = itemView.findViewById(R.id.playlistTitle);
            noOfSongs = itemView.findViewById(R.id.numberOfSongs);
        }
    }
}
