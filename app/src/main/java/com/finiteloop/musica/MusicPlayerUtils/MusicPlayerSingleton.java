package com.finiteloop.musica.MusicPlayerUtils;

import android.media.MediaPlayer;

/**
 * Created by therealshabi on 21/04/17.
 */

public class MusicPlayerSingleton extends MediaPlayer {
    public static MediaPlayer musicPlayer;

    private MusicPlayerSingleton() {

    }

    public static MediaPlayer getInstance() {
        if (musicPlayer == null) {
            musicPlayer = new MediaPlayer();
        }
        return musicPlayer;
    }
}
