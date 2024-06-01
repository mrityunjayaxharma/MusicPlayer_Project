package com.example.music_player;

public class Song_ID {
    private static int song_ID;

    public static void setSong_ID(int song_ID) {
        Song_ID.song_ID = song_ID;
    }

    public static int getSong_ID() {
        return song_ID;
    }
}

