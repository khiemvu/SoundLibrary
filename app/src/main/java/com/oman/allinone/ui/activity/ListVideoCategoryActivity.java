package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.*;
import com.oman.allinone.R;
import com.oman.allinone.adapter.SoundsAdapter;
import com.oman.allinone.common.URLServices;
import com.oman.allinone.dto.ListSoundCategoryDTO;
import com.oman.allinone.event.GetListSoundEvent;
import com.oman.allinone.event.GetListSoundResponseEvent;
import com.oman.allinone.utils.NetworkUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Trung on 5/8/2015.
 */
public class ListVideoCategoryActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    SoundsAdapter soundsAdapter;
    private TextView btBack;
    private Button btHome, btSearch, btShare, btFavourite;
    private ListView lvContent;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sound_category);
        lvContent = (ListView) findViewById(R.id.lvContent);
        btBack = (TextView) findViewById(R.id.tvBack);
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);
        btBack.setOnClickListener(this);
        lvContent.setOnItemClickListener(this);
        EventBus.getDefault().register(this);
        getDataFromServer();
    }

    private void getDataFromServer()
    {
        EventBus.getDefault().post(new GetListSoundEvent());
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }


    public void onEventBackgroundThread(GetListSoundEvent event) throws IOException
    {
        String url = URLServices.getInstance().getURLGetListVideos();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = NetworkUtils.getHttpClientInstance().newCall(request).execute();
        String jsonResult = response.body().string();

        JsonParser jsonParser = new JsonParser();
        JsonElement rootResult = jsonParser.parse(jsonResult);
        JsonObject rootResultObject = rootResult.getAsJsonObject();
        Gson gson = new Gson();
        List<ListSoundCategoryDTO> results = new ArrayList<ListSoundCategoryDTO>();
        ListSoundCategoryDTO temp;
        if (!rootResultObject.get("data").equals(null)) {
            JsonArray resultDTOJson = rootResultObject.get("data").getAsJsonArray();
            Iterator<JsonElement> iterator = resultDTOJson.iterator();

            while (iterator.hasNext()) {
                temp = gson.fromJson(iterator.next(), ListSoundCategoryDTO.class);
                results.add(temp);
            }
        }
        EventBus.getDefault().post(new GetListSoundResponseEvent(results));
    }

    public void onEventMainThread(GetListSoundResponseEvent event)
    {
        soundsAdapter = new SoundsAdapter(this, event.getListSoundDTOs());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(getApplicationContext(), ListSubVideoCategoriesActivity.class);
        intent.putExtra("parent_id", ((ListSoundCategoryDTO)soundsAdapter.getItem(position)).getId());
        startActivity(intent);
    }
}
