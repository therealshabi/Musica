package com.finiteloop.musica.Models;

import android.graphics.drawable.Drawable;

/**
 * Created by shahbaz on 29/1/17.
 */

public class ProfileAlbums {
    String albumName;
    int numOfSongs;
    Drawable drawable;

    public ProfileAlbums(String albumName, int numOfSongs, Drawable drawable) {
        this.albumName = albumName;
        this.numOfSongs = numOfSongs;
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public String getAlbumName() {
        return albumName;
    }
}
