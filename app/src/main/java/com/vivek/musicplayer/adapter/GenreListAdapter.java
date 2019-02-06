package com.vivek.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vivek.musicplayer.helper.Genre;
import com.vivek.musicplayer.R;

import java.util.ArrayList;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.GenreViewHolder>{
    ArrayList<Genre> genreList;

    public GenreListAdapter(ArrayList<Genre> genreList) {
        this.genreList = genreList;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View genreListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_list_item, parent, false);
        return new GenreViewHolder(genreListView);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        Genre genre = genreList.get(position);

        holder.genreType.setText(genre.getGenreType());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView genreType;

        GenreViewHolder(View itemView) {
            super(itemView);
            genreType = itemView.findViewById(R.id.genreType);
        }
    }
}
