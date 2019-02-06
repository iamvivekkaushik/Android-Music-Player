package com.vivek.musicplayer.fragments;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivek.musicplayer.helper.Album;
import com.vivek.musicplayer.R;
import com.vivek.musicplayer.adapter.AlbumListAdapter;

import java.util.ArrayList;

public class AlbumFragment extends Fragment{
    ArrayList<Album> albumList;
    ContentResolver mContentResolver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.albumListRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));

        albumList = new ArrayList<>();
        mContentResolver = rootView.getContext().getContentResolver();
        createAlbum();

        recyclerView.setAdapter(new AlbumListAdapter(rootView.getContext(), albumList));
        return rootView;
    }

    public void createAlbum(){
        String[] mProjection =
                {
                        MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ALBUM,
                        MediaStore.Audio.Albums.ARTIST,
                        MediaStore.Audio.Albums.ALBUM_ART
                };

        Cursor albumCursor = mContentResolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                mProjection,
                null,
                null,
                MediaStore.Audio.Albums.ALBUM + " ASC");

        while(albumCursor.moveToNext()){
            int albumId = albumCursor.getInt(albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
            String albumName = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            String albumArtist = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
            String coverArt = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

            albumList.add(new Album(coverArt, albumId, albumName,albumArtist));
        }
    }
}
