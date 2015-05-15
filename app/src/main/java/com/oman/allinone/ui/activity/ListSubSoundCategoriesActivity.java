package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oman.allinone.R;
import com.oman.allinone.dto.SubSoundCategoryDTO;
import com.oman.allinone.ui.adapter.SubSoundsAdapter;
import com.oman.allinone.common.URLServices;
import com.oman.allinone.ui.event.GetSubSoundEvent;
import com.oman.allinone.ui.event.GetSubSoundResponseEvent;
import com.oman.allinone.utils.NetworkUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Khiemvx on 5/8/2015.
 */
public class ListSubSoundCategoriesActivity extends Activity implements View.OnClickListener {
    SubSoundsAdapter soundsAdapter;
    int category_id;
    private TextView btBack, tvTitle;
    private Button btHome, btSearch, btShare, btFavourite;
    private ListView lvContent;
    private String category_name;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sound_category);
        lvContent = (ListView) findViewById(R.id.lvContent);
        btBack = (TextView) findViewById(R.id.tvBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("SubCategories");
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);
        Bundle extras = getIntent().getExtras();
        category_id = extras.getInt("category_id");
        category_name = extras.getString("category_name");
        btBack.setOnClickListener(this);
        EventBus.getDefault().register(this);
        getDataFromServer();
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ListFileSoundActivity.class);
                SubSoundCategoryDTO listSoundDTO = (SubSoundCategoryDTO) lvContent.getItemAtPosition(position);
                intent.putExtra("file_id", listSoundDTO.getId());
                intent.putExtra("subCategory_name", listSoundDTO.getTitle());
                intent.putExtra("category_name", category_name);
                startActivity(intent);
            }
        });
    }

    private void getDataFromServer() {
        EventBus.getDefault().post(new GetSubSoundEvent());
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

    public void onEventBackgroundThread(GetSubSoundEvent event) throws IOException {
        String url = URLServices.getInstance().getURLGetSubSounds(category_id);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = NetworkUtils.getHttpClientInstance().newCall(request).execute();
        String jsonResult = response.body().string();

        JsonParser jsonParser = new JsonParser();
        JsonElement rootResult = jsonParser.parse(jsonResult);
        JsonObject rootResultObject = rootResult.getAsJsonObject();
        Gson gson = new Gson();
        List<SubSoundCategoryDTO> results = new ArrayList<SubSoundCategoryDTO>();
        SubSoundCategoryDTO temp;
        if (!rootResultObject.get("data").equals(null)) {
            JsonArray resultDTOJson = rootResultObject.get("data").getAsJsonArray();
            Iterator<JsonElement> iterator = resultDTOJson.iterator();

            while (iterator.hasNext()) {
                temp = gson.fromJson(iterator.next(), SubSoundCategoryDTO.class);
                results.add(temp);
            }
        }
        EventBus.getDefault().post(new GetSubSoundResponseEvent(results));
    }

    public void onEventMainThread(GetSubSoundResponseEvent event) {
        soundsAdapter = new SubSoundsAdapter(this, event.getResults());
        lvContent.setAdapter(soundsAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
