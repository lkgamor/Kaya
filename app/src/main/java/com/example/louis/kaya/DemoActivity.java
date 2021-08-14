package com.example.louis.kaya;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DemoActivity extends AppCompatActivity {

    private VideoView videoView;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.demo_custom_title, null),
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        videoView = (VideoView)findViewById(R.id.demo);
        mediaController = new MediaController(this);

        String videoPath = "android.resource://com.example.louis.kaya/" + R.raw.demo;

        Uri videoURI = Uri.parse(videoPath);
        videoView.setVideoURI(videoURI);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        videoView.stopPlayback();
        this.finish();
        startActivity(new Intent(this, MainActivity.class));
        super.onBackPressed();
    }

    public void CLOSE_DEMO(View view) {
        videoView.stopPlayback();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
