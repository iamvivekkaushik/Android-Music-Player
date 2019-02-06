package com.vivek.musicplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivek.musicplayer.helper.Playlist;
import com.vivek.musicplayer.R;
import com.vivek.musicplayer.adapter.PlaylistAdapter;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment{
    ArrayList<Playlist> playlistArray;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);


        playlistArray = new ArrayList<>();
        createPlaylist();


        RecyclerView recyclerView = rootView.findViewById(R.id.playlistRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(new PlaylistAdapter(playlistArray));
        return rootView;
    }

    private void createPlaylist() {
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Now Playing", 39));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Favourite", 34));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Liked", 47));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Songs Taylor Love", 59));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Awesome Playlist", 55));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Radio Likes", 20));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Happy Playlist", 26));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Now Playing", 39));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Favourite", 34));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Liked", 47));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Songs Taylor Love", 59));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Awesome Playlist", 55));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Radio Likes", 20));
        playlistArray.add(new Playlist(R.drawable.reputation_art, "Happy Playlist", 26));
    }
}
