package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.oman.allinone.R;
import com.oman.allinone.dto.ListSoundFileDTO;

import java.util.List;

public class PlaySoundActivity extends Activity implements
        View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener
{
    private ImageView buttonPlayPause;
    private SeekBar seekBarProgress;
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    private ImageView btNext;
    private ImageView btPrevious;
    private TextView tvName;
    List<ListSoundFileDTO> listFie;
    private int position;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sound);
        buttonPlayPause = (ImageView) findViewById(R.id.btPlay);
        btNext = (ImageView) findViewById(R.id.btNext);
        btPrevious = (ImageView) findViewById(R.id.btBack);
        seekBarProgress = (SeekBar) findViewById(R.id.seekBar);
        tvName = (TextView) findViewById(R.id.tvNameOfSound);
        buttonPlayPause.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btPrevious.setOnClickListener(this);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);
        listFie = getIntent().getExtras().getParcelableArrayList("data");
        position = getIntent().getExtras().getInt("position");
        url = listFie.get(position).getExtern_file();
        tvName.setText(listFie.get(position).getFile_title());
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
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

            if (!mediaPlayer.isPlaying())
            {
                mediaPlayer.start();
                buttonPlayPause.setImageResource(R.drawable.pause);
            }
            else
            {
                mediaPlayer.pause();
                buttonPlayPause.setImageResource(R.drawable.play);
            }

            primarySeekBarProgressUpdater();
        }
        if(v.getId() == R.id.btNext)
        {
            if(position < (listFie.size() -1))
            {
                mediaPlayer.stop();
                buttonPlayPause.setImageResource(R.drawable.play);
                seekBarProgress.setSecondaryProgress(0);
                position = position + 1;
                url = listFie.get(position).getExtern_file();
                tvName.setText(listFie.get(position).getFile_title());
            }

        }
        if(v.getId() == R.id.btBack)
        {
            if(position > 0)
            {
                mediaPlayer.stop();
                buttonPlayPause.setImageResource(R.drawable.play);
                seekBarProgress.setSecondaryProgress(0);
                position = position -1;
                url = listFie.get(position).getExtern_file();
                tvName.setText(listFie.get(position).getFile_title());
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        buttonPlayPause.setImageResource(R.drawable.play);
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
