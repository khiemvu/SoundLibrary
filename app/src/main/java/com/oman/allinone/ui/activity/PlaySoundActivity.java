package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import com.oman.allinone.R;

public class PlaySoundActivity extends Activity implements
        View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener
{
    private Button buttonPlayPause;
    private SeekBar seekBarProgress;
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sound);
        buttonPlayPause = (Button) findViewById(R.id.btPlay);
        seekBarProgress = (SeekBar) findViewById(R.id.seekBar);
        buttonPlayPause.setOnClickListener(this);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void primarySeekBarProgressUpdater()
    {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying())
        {
            Runnable notification = new Runnable()
            {
                public void run()
                {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent)
    {
        seekBarProgress.setSecondaryProgress(percent);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btPlay)
        {
            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            try
            {
                mediaPlayer.setDataSource(getIntent().getExtras().getString("url")); // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

            if (!mediaPlayer.isPlaying())
            {
                mediaPlayer.start();
                buttonPlayPause.setText("Pause");
            }
            else
            {
                mediaPlayer.pause();
                buttonPlayPause.setText("Play");
            }

            primarySeekBarProgressUpdater();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        buttonPlayPause.setText("Play");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (v.getId() == R.id.seekBar)
        {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (mediaPlayer.isPlaying())
            {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }
}
