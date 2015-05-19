package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oman.allinone.R;
import com.oman.allinone.common.URLServices;
import com.oman.allinone.database.entity.Favourite;
import com.oman.allinone.database.ormlite.DatabaseHelper;
import com.oman.allinone.dto.SoundFileDTO;
import com.oman.allinone.ui.event.GetFileSoundEvent;
import com.oman.allinone.utils.NetworkUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

public class PlaySoundActivity extends Activity implements
        View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    private final Handler handler = new Handler();
    List<SoundFileDTO> listFie;
    CheckBox btFavourite;
    private ImageView buttonPlayPause;
    private SeekBar seekBarProgress;
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private ImageView btNext;
    private ImageView btPrevious;
    private TextView tvName, tvBack;
    private int position, file_id;
    private String url, category_name, subCategory_name, file_name;
    private Button btHome, btSearch, btShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sound);
        EventBus.getDefault().register(this);
        buttonPlayPause = (ImageView) findViewById(R.id.btPlay);
        btNext = (ImageView) findViewById(R.id.btNext);
        btPrevious = (ImageView) findViewById(R.id.btBack);
        seekBarProgress = (SeekBar) findViewById(R.id.seekBar);
        tvName = (TextView) findViewById(R.id.tvTitle);
        tvBack = (TextView) findViewById(R.id.tvBack);
        tvBack.setOnClickListener(this);
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btSearch.setVisibility(View.GONE);
        btHome.setOnClickListener(this);
        btFavourite = (CheckBox) findViewById(R.id.btFavourite);
        btFavourite.setEnabled(true);
        btShare = (Button) findViewById(R.id.btShare);
        buttonPlayPause.setOnClickListener(this);
        btFavourite.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btPrevious.setOnClickListener(this);
        btShare.setOnClickListener(this);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);
        file_id = getIntent().getExtras().getInt("file_id");
        listFie = getIntent().getExtras().getParcelableArrayList("data");
        if (listFie == null) {
            getDataFromServer();
        }
        position = getIntent().getExtras().getInt("position");
        category_name = getIntent().getExtras().getString("category_name");
        subCategory_name = getIntent().getExtras().getString("subCategory_name");
        file_name = getIntent().getExtras().getString("file_name");
        url = getIntent().getExtras().getString("url");
        tvName.setText(file_name);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void getDataFromServer() {
        EventBus.getDefault().post(new GetFileSoundEvent());
    }

    public void onEventBackgroundThread(GetFileSoundEvent event) throws IOException {
        String url = URLServices.getInstance().getURLGetFileSounds(file_id);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = NetworkUtils.getHttpClientInstance().newCall(request).execute();
        String jsonResult = response.body().string();

        JsonParser jsonParser = new JsonParser();
        JsonElement rootResult = jsonParser.parse(jsonResult);
        JsonObject rootResultObject = rootResult.getAsJsonObject();
        Gson gson = new Gson();
        List<SoundFileDTO> results = new ArrayList<SoundFileDTO>();
        SoundFileDTO temp;
        if (!rootResultObject.get("data").equals(null)) {
            JsonArray resultDTOJson = rootResultObject.get("data").getAsJsonArray();
            Iterator<JsonElement> iterator = resultDTOJson.iterator();

            while (iterator.hasNext()) {
                temp = gson.fromJson(iterator.next(), SoundFileDTO.class);
                results.add(temp);
            }
        }
        listFie = results;
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
            case R.id.tvBack:
                finish();
                break;
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
                favourite.setFileID(file_id);
                try {
                    List<Favourite> favouriteList = DatabaseHelper.getInstance(this).getFavouriteDAO().queryForAll();
                    int count = 0;
                    if (btFavourite.isChecked())
                        if (favouriteList != null && favouriteList.size() > 0) {
                            for (Favourite temp : favouriteList) {
                                if (!(temp.getFileName().equals(file_name)))
                                    count++;
                                else {
                                    favourite.setFavourite(!favourite.isFavourite());
                                    break;
                                }
                            }
                            if (count == favouriteList.size())
                                DatabaseHelper.getInstance(this).getFavouriteDAO().create(favourite);
                            else
                                DatabaseHelper.getInstance(this).getFavouriteDAO().update(favourite);
                        } else {
                            favourite.setFavourite(false);
                            DatabaseHelper.getInstance(this).getFavouriteDAO().create(favourite);
                        }
                    else {
                        List<Favourite> results = DatabaseHelper.getInstance(this).getFavouriteDAO().queryBuilder().where().eq("FILE_ID", favourite.getFileID()).query();
                        DatabaseHelper.getInstance(this).getFavouriteDAO().delete(results);
                    }
                } catch (SQLException e) {
                    Log.e(this.getClass().getName(), "Can't not create or update db");
                }
                break;
            case R.id.btShare:
                Intent dropbox = new Intent(Intent.ACTION_SEND);
                dropbox.setType("text/plain");
                dropbox.putExtra(Intent.EXTRA_TEXT, "اعمال الصلاة و الوضوء " +
                        "\n https://play.google.com/store/apps/developer?id=Omani%20Muslim&hl=en");
                startActivity(dropbox);
                break;
            case R.id.btHome:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
