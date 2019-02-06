package com.vivek.musicplayer.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vivek.musicplayer.activities.AlbumDetailActivity;
import com.vivek.musicplayer.helper.Album;
import com.vivek.musicplayer.R;

import java.io.File;
import java.util.ArrayList;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>{
    private ArrayList<Album> albumList;
    private Context context;

    public AlbumListAdapter(Context context, ArrayList<Album> list) {
        this.context = context;
        this.albumList = list;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View albumListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_list_item, parent, false);
        return new AlbumListAdapter.AlbumViewHolder(albumListView);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        final Album album = albumList.get(position);

        holder.albumName.setText(album.getAlbumName());
        holder.albumArtist.setText(album.getArtistName());
        setSongArt(album.getAlbumArt(), holder.albumCoverArt);

        holder.albumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlbumDetailActivity.class);
                intent.putExtra("Album", album.getAlbumName());
                intent.putExtra("Artist", album.getArtistName());
                context.startActivity(intent);
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
        return albumList.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder{
        private ImageView albumCoverArt;
        private TextView albumName;
        private TextView albumArtist;
        CardView albumView;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            albumCoverArt = itemView.findViewById(R.id.albumCoverArt);
            albumName = itemView.findViewById(R.id.albumName);
            albumArtist = itemView.findViewById(R.id.albumArtist);
            albumView = itemView.findViewById(R.id.albumView);
        }
    }
}
