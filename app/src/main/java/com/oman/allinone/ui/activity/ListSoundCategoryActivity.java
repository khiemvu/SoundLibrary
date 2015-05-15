package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.google.gson.*;
import com.oman.allinone.R;
import com.oman.allinone.common.URLServices;
import com.oman.allinone.dto.SoundCategoryDTO;
import com.oman.allinone.ui.adapter.SoundsAdapter;
import com.oman.allinone.ui.event.GetListSoundEvent;
import com.oman.allinone.ui.event.GetListSoundResponseEvent;
import com.oman.allinone.utils.CommonUtils;
import com.oman.allinone.utils.NetworkUtils;
import com.oman.allinone.utils.StringUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;

import java.io.IOException;
import java.util.*;


/**
 * Created by Khiemvx on 5/7/2015.
 */
public class ListSoundCategoryActivity extends Activity implements View.OnClickListener {
    SoundsAdapter soundsAdapter;
    EditText etSearch;
    TextView tvCancelSearch;
    ImageView ivClearText;
    LinearLayout ll_search, ll_menu;
    private TextView btBack, tvTitle;
    private Button btHome, btSearch, btShare, btFavourite;
    private ListView lvContent;
    private List<SoundCategoryDTO> soundCategoryDTOs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sound_category);
        lvContent = (ListView) findViewById(R.id.lvContent);
        btBack = (TextView) findViewById(R.id.tvBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        //block search
        etSearch = (EditText) findViewById(R.id.etSearch);
        tvCancelSearch = (TextView) findViewById(R.id.tvCancel);
        ivClearText = (ImageView) findViewById(R.id.ivCancelSearch);
        ll_search = (LinearLayout) findViewById(R.id.rlSearch);
        ll_menu = (LinearLayout) findViewById(R.id.ll_menu_common);
        ll_search.setVisibility(View.GONE);
        etSearch.addTextChangedListener(textWatcher);
        ivClearText.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);

        tvTitle.setText("Categories");
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);
        btBack.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        EventBus.getDefault().register(this);
        getDataFromServer();
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ListSubSoundCategoriesActivity.class);
                SoundCategoryDTO listSoundDTO = (SoundCategoryDTO) lvContent.getItemAtPosition(position);
                intent.putExtra("category_id", listSoundDTO.getId());
                intent.putExtra("category_name", listSoundDTO.getTitle());
                startActivity(intent);
            }
        });
    }

    public List<SoundCategoryDTO> getStaffMatchingWithText(List<SoundCategoryDTO> soundCategoryDTOs, String input)
    {
        List<SoundCategoryDTO> resultSearch = new ArrayList<SoundCategoryDTO>();
        for (SoundCategoryDTO soundCategoryDTO : soundCategoryDTOs)
        {
            String category_name = soundCategoryDTO.getTitle();

            if (category_name.toLowerCase().contains(input))
            {
                resultSearch.add(soundCategoryDTO);
            }
        }
        return resultSearch;
    }

    TextWatcher textWatcher = new TextWatcher()
    {
        private final long SEARCH_TYPING_DELAY = 400; // in ms
        private Timer timer = new Timer();

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }

        @Override
        public void afterTextChanged(final Editable s)
        {
            tvCancelSearch.setVisibility(View.VISIBLE);
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    final List<SoundCategoryDTO> soundCategoryDTOList;
                    soundCategoryDTOList = soundsAdapter.getListContents();
                    if (StringUtils.isNotBlank(s.toString()))
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ivClearText.setVisibility(View.VISIBLE);
                                List<SoundCategoryDTO> staffMatchingWithText = getStaffMatchingWithText(soundCategoryDTOList, s.toString().toLowerCase().trim());
                                soundsAdapter.setListContents(staffMatchingWithText);
                                soundsAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ivClearText.setVisibility(View.INVISIBLE);
                                soundsAdapter.setListContents(soundCategoryDTOs);
                                soundsAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }, SEARCH_TYPING_DELAY);
        }
    };

    private void getDataFromServer() {
        EventBus.getDefault().post(new GetListSoundEvent());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btSearch:
                ll_search.setVisibility(View.VISIBLE);
                ll_menu.setVisibility(View.GONE);
                break;
            case R.id.ivCancelSearch:
                etSearch.setText("");
                break;
            case R.id.tvCancel:
                etSearch.setText("");
                tvCancelSearch.setVisibility(View.GONE);
                ll_search.setVisibility(View.GONE);
                ll_menu.setVisibility(View.VISIBLE);
                CommonUtils.hideKeyboard(this);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    public void onEventBackgroundThread(GetListSoundEvent event) throws IOException
    {
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
        List<SoundCategoryDTO> results = new ArrayList<SoundCategoryDTO>();
        SoundCategoryDTO temp;
        if (!rootResultObject.get("data").equals(null))
        {
            JsonArray resultDTOJson = rootResultObject.get("data").getAsJsonArray();
            Iterator<JsonElement> iterator = resultDTOJson.iterator();

            while (iterator.hasNext())
            {
                temp = gson.fromJson(iterator.next(), SoundCategoryDTO.class);
                results.add(temp);
            }
        }
        EventBus.getDefault().post(new GetListSoundResponseEvent(results));
    }

    public void onEventMainThread(GetListSoundResponseEvent event)
    {
        soundCategoryDTOs = event.getListSoundDTOs();
        soundsAdapter = new SoundsAdapter(this, soundCategoryDTOs);
        lvContent.setAdapter(soundsAdapter);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
