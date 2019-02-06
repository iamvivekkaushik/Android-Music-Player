package com.vivek.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vivek.musicplayer.helper.Song;
import com.vivek.musicplayer.R;
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;

public class ArtistDetailsAdapter extends SectionedRecyclerViewAdapter<ArtistDetailsAdapter.SubheaderHolder, ArtistDetailsAdapter.ArtistDetailViewHolder> {
   ArrayList<Song> songsList;

    public ArtistDetailsAdapter(ArrayList<Song> songsList) {
        this.songsList = songsList;
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final Song song = songsList.get(position);
        final Song nextSong = songsList.get(position + 1);

        return !song.getSongAlbum().equals(nextSong.getSongAlbum());
    }

    @Override
    public ArtistDetailViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ArtistDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.artist_details_list_item, parent, false));
    }

    @Override
    public SubheaderHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.artist_details_list_item_header, parent, false));
    }

    @Override
    public void onBindItemViewHolder(ArtistDetailViewHolder holder, int itemPosition) {
        Song song = songsList.get(itemPosition);

        holder.songTitle.setText(song.getSongTitle());
        holder.songDuration.setText("" + song.getSongDuration());
        holder.songArtist.setText(song.getSongArtist());
        holder.songCover.setImageResource(R.drawable.reputation_art);
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        Song song = songsList.get(nextItemPosition);
        subheaderHolder.header.setText(song.getSongAlbum());
    }

    @Override
    public int getItemSize() {
        return songsList.size();
    }

    class ArtistDetailViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView songDuration;
        TextView songArtist;
        ImageView songCover;

        ArtistDetailViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
            songDuration = itemView.findViewById(R.id.songDuration);
            songArtist = itemView.findViewById(R.id.artist);
            songCover = itemView.findViewById(R.id.songArt);
        }
    }

    class SubheaderHolder extends RecyclerView.ViewHolder {
        TextView header;

        SubheaderHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.headerText);
        }
    }
}
