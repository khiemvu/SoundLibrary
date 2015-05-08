package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oman.allinone.R;
import com.oman.allinone.adapter.SoundsAdapter;
import com.oman.allinone.common.URLServices;
import com.oman.allinone.dto.ListSoundDTO;
import com.oman.allinone.event.GetListSoundEvent;
import com.oman.allinone.event.GetListSoundResponseEvent;
import com.oman.allinone.utils.NetworkUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by Khiemvx on 5/7/2015.
 */
public class ListSoundCategoryActivity extends Activity implements View.OnClickListener {
    SoundsAdapter soundsAdapter;
    private TextView btBack;
    private Button btHome, btSearch, btShare, btFavourite;
    private ListView lvContent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sound_category);
        lvContent = (ListView) findViewById(R.id.lvContent);
        btBack = (TextView) findViewById(R.id.tvBack);
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);
        btBack.setOnClickListener(this);
        EventBus.getDefault().register(this);
        getDataFromServer();
    }

    private void getDataFromServer() {
        EventBus.getDefault().post(new GetListSoundEvent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onEventBackgroundThread(GetListSoundEvent event) throws IOException {
        String url = URLServices.getInstance().getURLGetListSounds();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = NetworkUtils.getHttpClientInstance().newCall(request).execute();
        String jsonResult = response.body().string();

        JsonParser jsonParser = new JsonParser();
        JsonElement rootResult = jsonParser.parse(jsonResult);
        JsonObject rootResultObject = rootResult.getAsJsonObject();
        Gson gson = new Gson();
        List<ListSoundDTO> results = new ArrayList<ListSoundDTO>();
        ListSoundDTO temp;
        if (!rootResultObject.get("data").equals(null)) {
            JsonArray resultDTOJson = rootResultObject.get("data").getAsJsonArray();
            Iterator<JsonElement> iterator = resultDTOJson.iterator();

            while (iterator.hasNext()) {
                temp = gson.fromJson(iterator.next(), ListSoundDTO.class);
                results.add(temp);
            }
        }
        EventBus.getDefault().post(new GetListSoundResponseEvent(results));
    }

    public void onEventMainThread(GetListSoundResponseEvent event) {
        soundsAdapter = new SoundsAdapter(this, event.getListSoundDTOs());
        lvContent.setAdapter(soundsAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
