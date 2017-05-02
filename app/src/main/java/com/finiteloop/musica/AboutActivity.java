package com.finiteloop.musica;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");

        String description = "MUSICA is a social networking App for Music Lovers. The basic notion of this app is to provide a platform for Music Enthusiasts to share their own creations and to like and follow their idol or friends and their Music on this Social Network.\n" +
                "Not only that, we can also have a Private Playlist which will only be visible to us, also, there will be our Friend Circles or Rooms in which we can collaborate and find what our fellow friends are listening to and know their taste of music, also you can know what is trending around the world and in your circles. \n" +
                "Icons used in this app are icons made by Freepik from www.flaticon.com";
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.musica)
                .setDescription(description)
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("shahbaz.h96@gmail.com")
                .addFacebook("shahbazhussain123")
                .addTwitter("therealshabi")
                .addGitHub("therealshabi")
                .addInstagram("therealshabi")
                .create();

        setContentView(aboutPage);

    }
}
