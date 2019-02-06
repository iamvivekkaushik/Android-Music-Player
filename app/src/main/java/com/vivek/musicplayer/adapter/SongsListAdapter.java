package com.vivek.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vivek.musicplayer.R;
import com.vivek.musicplayer.helper.Song;
import com.vivek.musicplayer.player.PlayerState;
import com.vivek.musicplayer.services.MusicPlayerService;

import java.io.File;
import java.util.ArrayList;


public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.ViewHolder> {
    private ArrayList<Song> songsList;
    private Context context;

    public SongsListAdapter(ArrayList<Song> list) {
        this.songsList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View songListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list, parent, false);
        return new ViewHolder(songListView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Song song = songsList.get(position);

        holder.songTitle.setText(song.getSongTitle());
        holder.songArtist.setText(song.getSongArtist());
        holder.songDuration.setText("" + song.getSongDuration());
        //holder.songArt.setImageResource(R.drawable.reputation_art);
        setSongArt(song.getSongArt(), holder.songArt);

        holder.songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Playing " + song.getSongTitle(), Toast.LENGTH_SHORT).show();
                PlayerState.nowPlaying = songsList;
                PlayerState.currentSongId = song.getSongId();
                PlayerState.songCurrentTime = 0;

                Intent intent = new Intent( context, MusicPlayerService.class );
                intent.setAction(MusicPlayerService.ACTION_PLAY);
                context.startService(intent);
            }
        });
    }

    private void setSongArt(String albumArt, ImageView imageView) {
        if(albumArt == null) {
            // Do something
            imageView.setImageResource(R.drawable.reputation_art);
        } else {
            Glide.with(context)
                    .load(Uri.fromFile(new File(albumArt)))
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView songArt;
        TextView songTitle, songArtist, songDuration;
        View songItem;

        ViewHolder(View itemView) {
            super(itemView);
            songArt = itemView.findViewById(R.id.songArt);
            songTitle = itemView.findViewById(R.id.songTitle);
            songArtist = itemView.findViewById(R.id.artist);
            songDuration = itemView.findViewById(R.id.songDuration);
            songItem = itemView.findViewById(R.id.songItem);
        }
    }
}
