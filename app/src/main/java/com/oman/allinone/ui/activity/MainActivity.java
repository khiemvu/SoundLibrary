package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.oman.allinone.R;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btSound;
    private Button btAbout;
    private Button btVideo;
    private Button btFavourite;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btSound = (Button) findViewById(R.id.btSound);
        btVideo = (Button) findViewById(R.id.btVideo);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btAbout = (Button) findViewById(R.id.btAbout);


        btAbout.setOnClickListener(this);
        btSound.setOnClickListener(this);
        btVideo.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btAbout:
                intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.btSound:
                intent = new Intent(getApplicationContext(), ListSoundCategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.btVideo:
                intent = new Intent(getApplicationContext(), ListVideoCategoryActivity.class);
                startActivity(intent);
                break;
        }
    }
}