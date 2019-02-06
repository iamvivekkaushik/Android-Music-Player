package com.vivek.musicplayer.helper;


public class Album {
    private String albumArt;
    private int albumId;
    private String albumName;
    private String artistName;

    public Album(String cover, int albumId, String albumName, String artistName) {
        this.albumArt = cover;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public int getAlbumId() {
        return albumId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumArt() {
        return albumArt;
    }
}
