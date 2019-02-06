package com.vivek.musicplayer.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivek.musicplayer.PlayerActivity;
import com.vivek.musicplayer.R;
import com.vivek.musicplayer.adapter.SongsListAdapter;
import com.vivek.musicplayer.helper.Song;

import java.util.ArrayList;

public class SongsFragment extends Fragment{
    ArrayList<Song> songsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song, container, false);

        //Get Data from media store
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.TITLE + " ASC");

        songsList = new ArrayList<>();

        while(cursor.moveToNext()){
            //String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

            songsList.add(new Song(getAlbumArt(album, getContext()), title, artist, duration, id, album, data));
        }

        cursor.close();

        RecyclerView recyclerView = rootView.findViewById(R.id.musicListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        recyclerView.setAdapter(new SongsListAdapter(songsList));
        return rootView;
    }

    public String getAlbumArt(String album, Context context) {
        String[] mProjection = {MediaStore.Audio.Albums.ALBUM_ART};
        String selection = MediaStore.Audio.Media.ALBUM + "=?";
        String[] selectionArgs = new String[]{album};

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                mProjection,
                selection,
                selectionArgs,
                null);

        String albumArt = null;
        if( cursor != null && cursor.moveToFirst() ){
            albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            cursor.close();
        }
        if(albumArt == null) {
            // Do something
            return null;
        } else {
            return albumArt;
        }
    }
}
