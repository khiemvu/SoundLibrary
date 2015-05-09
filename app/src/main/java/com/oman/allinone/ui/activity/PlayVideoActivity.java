package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;
import com.oman.allinone.R;

public class PlayVideoActivity extends Activity
{

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        videoView = (VideoView)findViewById(R.id.videoView);
        String path = getIntent().getExtras().getString("url");
        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);
        videoView.setZOrderOnTop(true);
        Uri uri=Uri.parse("http://www.w3schools.com/html/mov_bbb.mp4");
        videoView.setVideoURI(uri);
        videoView.start();
    }


}
