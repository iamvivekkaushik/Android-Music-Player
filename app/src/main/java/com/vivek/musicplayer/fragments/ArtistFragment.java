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

import com.vivek.musicplayer.helper.Artist;
import com.vivek.musicplayer.R;
import com.vivek.musicplayer.adapter.ArtistListAdapter;

import java.util.ArrayList;

public class ArtistFragment extends Fragment{
    ArrayList<Artist> artistList;
    ContentResolver mContentResolver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), 3);
        RecyclerView recyclerView = rootView.findViewById(R.id.artistListRecyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);

        artistList = new ArrayList<>();
        mContentResolver = rootView.getContext().getContentResolver();
        createAlbum();
        ArtistListAdapter adapter = new ArtistListAdapter(artistList, rootView.getContext());
        adapter.setGridLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void createAlbum() {
        String[] mProjection =
                {
                        MediaStore.Audio.Artists._ID,
                        MediaStore.Audio.Artists.ARTIST,
                        MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
                };

        Cursor artistCursor = mContentResolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                mProjection,
                null,
                null,
                MediaStore.Audio.Artists.ARTIST + " ASC");


        while(artistCursor.moveToNext()){
            int id = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
            String artist = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
            int noOfAlbums = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));

            artistList.add(new Artist(R.drawable.reputation_art, artist, id, noOfAlbums));
        }
    }
}
