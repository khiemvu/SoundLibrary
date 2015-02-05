package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.oman.allinone.R;

public class MainActivity extends Activity
{
    private Button btSound;
    private Button btAbout;
    private Button btVideo;
    private Button btFavourite;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btSound = (Button) findViewById(R.id.btSound);
        btVideo = (Button) findViewById(R.id.btVideo);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btAbout = (Button) findViewById(R.id.btAbout);


        btAbout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}