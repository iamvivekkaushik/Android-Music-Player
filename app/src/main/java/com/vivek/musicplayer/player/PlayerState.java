package com.vivek.musicplayer.player;

import com.vivek.musicplayer.helper.Song;

import java.util.ArrayList;

public class PlayerState {
    public static ArrayList<Song> nowPlaying;
    private static boolean musicPlaying;
    public static int songCurrentTime;
    public static String currentSongId;
    public static int songNumberInPlaylist;

    public static boolean isMusicPlaying() {
        return musicPlaying;
    }

    public static void setMusicPlaying(boolean musicPlaying) {
        PlayerState.musicPlaying = musicPlaying;
    }
}
