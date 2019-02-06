package com.vivek.musicplayer.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivek.musicplayer.helper.Genre;
import com.vivek.musicplayer.R;
import com.vivek.musicplayer.adapter.GenreListAdapter;

import java.util.ArrayList;

public class GenreFragment extends Fragment{
    ArrayList<Genre> genreList;
    ContentResolver mContentResolver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genre, container, false);

        genreList = new ArrayList<>();
        mContentResolver = rootView.getContext().getContentResolver();
        createList();

        RecyclerView recyclerView = rootView.findViewById(R.id.genreListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(new GenreListAdapter(genreList));
        return rootView;
    }

    private void createList() {
        String[] mProjection =
                {
                };


    }
}
