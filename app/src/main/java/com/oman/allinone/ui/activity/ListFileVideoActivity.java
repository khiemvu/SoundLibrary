package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.*;
import com.oman.allinone.R;
import com.oman.allinone.adapter.FileSoundAdapter;
import com.oman.allinone.common.URLServices;
import com.oman.allinone.dto.ListSoundFileDTO;
import com.oman.allinone.event.GetFileSoundEvent;
import com.oman.allinone.event.GetFileSoundResponseEvent;
import com.oman.allinone.utils.NetworkUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListFileVideoActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    FileSoundAdapter fileSoundAdapter;
    int file_id;
    private TextView btBack, tvTitle;
    private Button btHome, btSearch, btShare, btFavourite;
    private ListView lvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sound_category);
        lvContent = (ListView) findViewById(R.id.lvContent);
        btBack = (TextView) findViewById(R.id.tvBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Title of the sounds");
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);
        btBack.setOnClickListener(this);
        lvContent.setOnItemClickListener(this);
        Bundle extras = getIntent().getExtras();
        file_id = extras.getInt("file_id");
        EventBus.getDefault().register(this);
        getDataFromServer();
    }

    private void getDataFromServer()
    {
        EventBus.getDefault().post(new GetFileSoundEvent());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tvBack:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    public void onEventBackgroundThread(GetFileSoundEvent event) throws IOException
    {
        String url = URLServices.getInstance().getURLGetFileVideos(file_id);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = NetworkUtils.getHttpClientInstance().newCall(request).execute();
        String jsonResult = response.body().string();

        JsonParser jsonParser = new JsonParser();
        JsonElement rootResult = jsonParser.parse(jsonResult);
        JsonObject rootResultObject = rootResult.getAsJsonObject();
        Gson gson = new Gson();
        List<ListSoundFileDTO> results = new ArrayList<ListSoundFileDTO>();
        ListSoundFileDTO temp;
        if (!rootResultObject.get("data").equals(null))
        {
            JsonArray resultDTOJson = rootResultObject.get("data").getAsJsonArray();
            Iterator<JsonElement> iterator = resultDTOJson.iterator();

            while (iterator.hasNext())
            {
                temp = gson.fromJson(iterator.next(), ListSoundFileDTO.class);
                results.add(temp);
            }
        }
        EventBus.getDefault().post(new GetFileSoundResponseEvent(results));
    }

    public void onEventMainThread(GetFileSoundResponseEvent event)
    {
        fileSoundAdapter = new FileSoundAdapter(this, event.getListSoundFileDTO());
        lvContent.setAdapter(fileSoundAdapter);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(getApplicationContext(), PlayVideoActivity
                .class);
        intent.putExtra("url", ((ListSoundFileDTO) fileSoundAdapter.getItem(position)).getExtern_file());
        startActivity(intent);
    }
}