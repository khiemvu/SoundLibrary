package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oman.allinone.R;
import com.oman.allinone.database.entity.Favourite;
import com.oman.allinone.database.ormlite.DatabaseHelper;
import com.oman.allinone.dto.SoundFileDTO;

import java.sql.SQLException;
import java.util.List;

public class PlaySoundActivity extends Activity implements
        View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    private final Handler handler = new Handler();
    List<SoundFileDTO> listFie;
    private ImageView buttonPlayPause;
    private SeekBar seekBarProgress;
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private ImageView btNext;
    private ImageView btPrevious;
    private TextView tvName;
    private int position;
    private String url, category_name, subCategory_name, file_name;
    private Button btHome, btSearch, btShare, btFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sound);
        buttonPlayPause = (ImageView) findViewById(R.id.btPlay);
        btNext = (ImageView) findViewById(R.id.btNext);
        btPrevious = (ImageView) findViewById(R.id.btBack);
        seekBarProgress = (SeekBar) findViewById(R.id.seekBar);
        tvName = (TextView) findViewById(R.id.tvNameOfSound);
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);
        buttonPlayPause.setOnClickListener(this);
        btFavourite.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btPrevious.setOnClickListener(this);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);
        listFie = getIntent().getExtras().getParcelableArrayList("data");
        position = getIntent().getExtras().getInt("position");
        category_name = getIntent().getExtras().getString("category_name");
        subCategory_name = getIntent().getExtras().getString("subCategory_name");
        file_name = getIntent().getExtras().getString("file_name");
        url = listFie.get(position).getExtern_file();
        tvName.setText(listFie.get(position).getFile_title());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBarProgress.setSecondaryProgress(percent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btPlay:
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    buttonPlayPause.setImageResource(R.drawable.pause);
                } else {
                    mediaPlayer.pause();
                    buttonPlayPause.setImageResource(R.drawable.play);
                }

                primarySeekBarProgressUpdater();
                break;
            case R.id.btBack:
                if (position > 0) {
                    mediaPlayer.stop();
                    buttonPlayPause.setImageResource(R.drawable.play);
                    seekBarProgress.setSecondaryProgress(0);
                    position = position - 1;
                    url = listFie.get(position).getExtern_file();
                    tvName.setText(listFie.get(position).getFile_title());
                }
                break;
            case R.id.btNext:
                if (position < (listFie.size() - 1)) {
                    mediaPlayer.stop();
                    buttonPlayPause.setImageResource(R.drawable.play);
                    seekBarProgress.setSecondaryProgress(0);
                    position = position + 1;
                    url = listFie.get(position).getExtern_file();
                    tvName.setText(listFie.get(position).getFile_title());
                }
                break;
            case R.id.btFavourite:
                Favourite favourite = new Favourite();
                favourite.setCategoryName(category_name);
                favourite.setSubCategoryName(subCategory_name);
                favourite.setFileName(file_name);
                favourite.setPosition(position);
                favourite.setUrl(url);
                try {
                    DatabaseHelper.getInstance(this).getFavouriteDAO().createOrUpdate(favourite);
                } catch (SQLException e) {
                    Log.e(this.getClass().getName(), "Can't not create or update db");
                }
                break;

        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        buttonPlayPause.setImageResource(R.drawable.play);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.seekBar) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }
}
