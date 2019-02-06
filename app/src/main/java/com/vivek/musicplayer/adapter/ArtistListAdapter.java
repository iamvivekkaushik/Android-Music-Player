package com.vivek.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vivek.musicplayer.activities.ArtistDetailActivity;
import com.vivek.musicplayer.helper.Artist;
import com.vivek.musicplayer.R;
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;

public class ArtistListAdapter extends SectionedRecyclerViewAdapter<ArtistListAdapter.SubheaderHolder, ArtistListAdapter.ArtistViewHolder> {
    ArrayList<Artist> artistsList;
    Context context;

    public ArtistListAdapter(ArrayList<Artist> itemList, Context context) {
        super();
        this.artistsList = itemList;
        this.context = context;
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final Artist artist = artistsList.get(position);
        final Artist nextArtist = artistsList.get(position + 1);

        return !artist.getArtistName().substring(0, 1).equals(nextArtist.getArtistName().substring(0, 1));
    }

    @Override
    public ArtistViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ArtistViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_list_item, parent, false));
    }

    @Override
    public SubheaderHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_list_item_header, parent, false));
    }

    @Override
    public void onBindItemViewHolder(final ArtistViewHolder holder, int itemPosition) {
        final Artist artist = artistsList.get(itemPosition);

        holder.artistImage.setImageResource(R.drawable.reputation_art);
        holder.artistName.setText(artist.getArtistName());

        holder.artistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArtistDetailActivity.class);
                intent.putExtra("Artist", artist.getArtistName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        final Artist nextMovie = artistsList.get(nextItemPosition);
        subheaderHolder.alphabetView.setText(nextMovie.getArtistName().substring(0, 1));
    }

    @Override
    public int getItemSize() {
        return artistsList.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        ImageView artistImage;
        TextView artistName;
        View artistView;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artistImage);
            artistName = itemView.findViewById(R.id.artistName);
            artistView = itemView.findViewById(R.id.artistView);
        }
    }

    public class SubheaderHolder extends RecyclerView.ViewHolder {
        TextView alphabetView;
        public SubheaderHolder(View itemView) {
            super(itemView);
            alphabetView = itemView.findViewById(R.id.alphabetTextView);
        }
    }
}
