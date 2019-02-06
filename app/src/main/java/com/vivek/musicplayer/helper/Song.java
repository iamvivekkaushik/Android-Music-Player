package com.vivek.musicplayer.helper;

public class Song {
    private String songArt;
    private String songTitle;
    private String songArtist;
    private int songDuration;
    private String songId;
    private String songAlbum;
    private String songPath;

    public Song(String songArt, String songTitle, String songArtist, int songDuration, String songId, String songAlbum, String songPath) {
        this.songArt = songArt;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songDuration = songDuration;
        this.songId = songId;
        this.songAlbum = songAlbum;
        this.songPath = songPath;
    }

    public String getSongArt() {
        return songArt;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public int getSongDuration() {
        return songDuration;
    }

    public String getSongId() {
        return songId;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public String getSongPath() {
        return songPath;
    }
}
