package com.finiteloop.musica.MusicPlayerUtils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by therealshabi on 21/04/17.
 */

public class MusicPlayerSingleton extends MediaPlayer {
    public static MediaPlayer musicPlayer;

    private MusicPlayerSingleton() {

    }

    public static MediaPlayer getInstance(Context context, int resId) {
        if (musicPlayer == null) {
            musicPlayer = MediaPlayer.create(context, resId);
            return musicPlayer;
        }
        return musicPlayer;
    }
}
