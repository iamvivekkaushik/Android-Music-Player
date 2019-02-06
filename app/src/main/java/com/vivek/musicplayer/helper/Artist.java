package com.vivek.musicplayer.helper;

public class Artist {
    private int artistImage;
    private int artistId;
    private int noOfAlbums;
    private String artistName;

    public Artist(int artistImage, String artistName, int artistId, int noOfAlbums) {
        this.artistImage = artistImage;
        this.artistName = artistName;
        this.artistId = artistId;
        this.noOfAlbums = noOfAlbums;
    }

    public int getArtistImage() {
        return artistImage;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getArtistId() {
        return artistId;
    }

    public int getNoOfAlbums() {
        return noOfAlbums;
    }
}
