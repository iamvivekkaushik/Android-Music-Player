package com.vivek.musicplayer.activities;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vivek.musicplayer.helper.Song;
import com.vivek.musicplayer.R;
import com.vivek.musicplayer.adapter.ArtistDetailsAdapter;

import java.util.ArrayList;

public class ArtistDetailActivity extends AppCompatActivity {
    String artist;
    ArrayList<Song> songsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        // Get Artist from the Intent
        artist = getIntent().getStringExtra("Artist");
        songsList = new ArrayList<>();

        createList();

        RecyclerView recyclerView = findViewById(R.id.artistDetailListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerView.setAdapter(new ArtistDetailsAdapter(songsList));
    }

    public void createList(){
        String selection = MediaStore.Audio.Media.ARTIST + "=?";
        String[] selectionArgs = new String[]{artist};
        String[] mProjection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                mProjection,
                selection,
                selectionArgs,
                null);

        while (cursor.moveToNext()) {
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

            songsList.add(new Song(null, title, artist, duration, id, album, data));
        }

        cursor.close();
    }
}
