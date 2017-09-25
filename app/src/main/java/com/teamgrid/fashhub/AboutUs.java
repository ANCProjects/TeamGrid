package com.teamgrid.fashhub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {

    private String aboutUs = getString(R.string.about_us);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");

        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .setDescription("I love me")
                .isRTL(false)
                .setImage(R.drawable.fashhub_home)
                .addItem(versionElement)
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("elmehdi.sakout@gmai.com")
                .addFacebook("the.medy")
                .addTwitter("androidngr")
              //.addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                // .addPlayStore("com.ideashower.readitlater.pro")
                //.addInstagram("medyo80")
                .addGitHub("ANCprojects")
                .create();

        setContentView(aboutPage);

    }
}
