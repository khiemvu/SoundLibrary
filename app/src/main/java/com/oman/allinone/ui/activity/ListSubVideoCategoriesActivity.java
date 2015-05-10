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
import com.oman.allinone.adapter.SoundsAdapter;
import com.oman.allinone.adapter.SubSoundsAdapter;
import com.oman.allinone.common.URLServices;
//import com.oman.allinone.dto.ListSoundDTO;
import com.oman.allinone.dto.ListSubSoundCategoryDTO;
import com.oman.allinone.event.*;
import com.oman.allinone.utils.NetworkUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListSubVideoCategoriesActivity extends Activity implements View.OnClickListener
{
    SubSoundsAdapter soundsAdapter;
    private TextView btBack;
    private Button btHome, btSearch, btShare, btFavourite;
    private ListView lvContent;
    private int parentId;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sound_category);
        parentId = getIntent().getExtras().getInt("parent_id");
        lvContent = (ListView) findViewById(R.id.lvContent);
        btBack = (TextView) findViewById(R.id.tvBack);
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);
        btBack.setOnClickListener(this);
        EventBus.getDefault().register(this);
        getDataFromServer();
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(),ListFileVideoActivity.class);
                intent.putExtra("file_id",((ListSubSoundCategoryDTO)soundsAdapter.getItem(position)).getId());
                startActivity(intent);
            }
        });
    }

    private void getDataFromServer()
    {
        EventBus.getDefault().post(new GetSubVideoEvent());
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    public void onEventBackgroundThread(GetSubVideoEvent event) throws IOException {
        String url = URLServices.getInstance().getURLGetListSubVideos(1);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = NetworkUtils.getHttpClientInstance().newCall(request).execute();
        String jsonResult = response.body().string();

        JsonParser jsonParser = new JsonParser();
        JsonElement rootResult = jsonParser.parse(jsonResult);
        JsonObject rootResultObject = rootResult.getAsJsonObject();
        Gson gson = new Gson();
        List<ListSubSoundCategoryDTO> results = new ArrayList<ListSubSoundCategoryDTO>();
        ListSubSoundCategoryDTO temp;
        if (!rootResultObject.get("data").equals(null)) {
            JsonArray resultDTOJson = rootResultObject.get("data").getAsJsonArray();
            Iterator<JsonElement> iterator = resultDTOJson.iterator();

            while (iterator.hasNext()) {
                temp = gson.fromJson(iterator.next(), ListSubSoundCategoryDTO.class);
                results.add(temp);
            }
        }
        EventBus.getDefault().post(new GetSubVideoResponseEvent(results));
    }


    public void onEventMainThread(GetSubVideoResponseEvent event)
    {
        soundsAdapter = new SubSoundsAdapter(this, event.getListSoundDTOs());
        lvContent.setAdapter(soundsAdapter);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v)
    {

    }
}