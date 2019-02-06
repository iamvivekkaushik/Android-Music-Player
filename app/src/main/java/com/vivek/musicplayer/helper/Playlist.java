package com.vivek.musicplayer.helper;

public class Playlist {
    private int playlistCover;
    private String playlistTitle;
    private int totalSongs;

    public Playlist(int playlistCover, String playlistTitle, int totalSongs) {
        this.totalSongs = totalSongs;
        this.playlistTitle = playlistTitle;
        this.playlistCover = playlistCover;
    }

    public int getPlaylistCover() {
        return playlistCover;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public int getTotalSongs() {
        return totalSongs;
    }
}
