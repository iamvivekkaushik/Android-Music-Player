package com.vivek.musicplayer.activities;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vivek.musicplayer.R;
import com.vivek.musicplayer.adapter.AlbumDetailsAdapter;
import com.vivek.musicplayer.helper.Song;

import java.io.File;
import java.util.ArrayList;

public class AlbumDetailActivity extends AppCompatActivity {
    TextView albumNameTextView;
    TextView numberOfSongs;
    TextView artistTextView;
    ImageView albumCoverArt;

    ArrayList<Song> songsList;
    String album;
    String artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        album = getIntent().getStringExtra("Album");
        artist = getIntent().getStringExtra("Artist");
        songsList = new ArrayList<>();

        albumNameTextView = findViewById(R.id.albumNameTextView);
        artistTextView = findViewById(R.id.artistAlbumDetail);
        numberOfSongs = findViewById(R.id.numberOfSongs);
        albumCoverArt = findViewById(R.id.albumCoverArt);

        albumNameTextView.setText(album);
        artistTextView.setText(artist);

        createList();

        numberOfSongs.setText("" + songsList.size() + " Songs");

        RecyclerView recyclerView = findViewById(R.id.albumListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new AlbumDetailsAdapter(getApplicationContext(), songsList));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Playing " + album, Snackbar.LENGTH_LONG).show();
            }
        });


        //get Album Art
        String[] mProjection = {MediaStore.Audio.Albums.ALBUM_ART};
        String selection = MediaStore.Audio.Albums.ALBUM + "=?";
        String[] selectionArgs = new String[]{album};
        Cursor cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                mProjection,
                selection,
                selectionArgs,
                null);

        cursor.moveToFirst();
        String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        cursor.close();

        setSongArt(albumArt, albumCoverArt);
    }

    private void setSongArt(String albumArt, ImageView imageView) {
        if(albumArt == null) {
            // Do something
            imageView.setImageResource(R.drawable.reputation_art);
        } else {
            Glide.with(getApplicationContext())
                    .load(Uri.fromFile(new File(albumArt)))
                    .apply(new RequestOptions()
                            .override(1200, 1200)
                            .centerCrop())
                    .into(imageView);
        }
    }

    public void createList(){
        String selection = MediaStore.Audio.Media.ALBUM + "=?";
        String[] selectionArgs = new String[]{album};
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
