package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oman.allinone.R;
import com.oman.allinone.common.URLServices;
import com.oman.allinone.dto.SubSoundCategoryDTO;
import com.oman.allinone.ui.adapter.SubSoundsAdapter;
import com.oman.allinone.ui.event.GetSubSoundEvent;
import com.oman.allinone.ui.event.GetSubSoundResponseEvent;
import com.oman.allinone.utils.CommonUtils;
import com.oman.allinone.utils.NetworkUtils;
import com.oman.allinone.utils.StringUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * Created by Khiemvx on 5/8/2015.
 */
public class ListSubSoundCategoriesActivity extends Activity implements View.OnClickListener {
    SubSoundsAdapter soundsAdapter;
    int category_id;
    CheckBox btFavourite;
    EditText etSearch;
    TextView tvCancelSearch;
    ImageView ivClearText;
    LinearLayout ll_search, ll_menu;
    List<SubSoundCategoryDTO> subSoundCategoryDTOList;
    ProgressDialog dialog;
    private TextView btBack, tvTitle;
    private Button btHome, btSearch, btShare;
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
        btFavourite = (CheckBox) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);

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
        btSearch.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        category_id = extras.getInt("category_id");
        category_name = extras.getString("category_name");
        btBack.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btHome.setOnClickListener(this);
        EventBus.getDefault().register(this);
        if (NetworkUtils.isOnline(this)) {
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            getDataFromServer();
        } else {
            Toast.makeText(this, "Can't get data from server", Toast.LENGTH_LONG).show();
        }
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

    public List<SubSoundCategoryDTO> getSubSoundMatchingWithText(List<SubSoundCategoryDTO> subSoundCategoryDTOs, String input) {
        List<SubSoundCategoryDTO> resultSearch = new ArrayList<SubSoundCategoryDTO>();
        for (SubSoundCategoryDTO soundCategoryDTO : subSoundCategoryDTOs) {
            String category_name = soundCategoryDTO.getTitle();

            if (category_name.toLowerCase().contains(input)) {
                resultSearch.add(soundCategoryDTO);
            }
        }
        return resultSearch;
    }

    TextWatcher textWatcher = new TextWatcher() {
        private final long SEARCH_TYPING_DELAY = 400; // in ms
        private Timer timer = new Timer();

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(final Editable s) {
            tvCancelSearch.setVisibility(View.VISIBLE);
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    final List<SubSoundCategoryDTO> subSoundCategoryDTOs;
                    subSoundCategoryDTOs = soundsAdapter.getListContents();
                    if (StringUtils.isNotBlank(s.toString())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivClearText.setVisibility(View.VISIBLE);
                                List<SubSoundCategoryDTO> subSoundMatchingWithText = getSubSoundMatchingWithText(subSoundCategoryDTOs, s.toString().toLowerCase().trim());
                                soundsAdapter.setListContents(subSoundMatchingWithText);
                                soundsAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivClearText.setVisibility(View.INVISIBLE);
                                soundsAdapter.setListContents(subSoundCategoryDTOList);
                                soundsAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }, SEARCH_TYPING_DELAY);
        }
    };

    private void getDataFromServer() {
        EventBus.getDefault().post(new GetSubSoundEvent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btShare:
                Intent dropbox = new Intent(Intent.ACTION_SEND);
                dropbox.setType("text/plain");
                dropbox.putExtra(Intent.EXTRA_TEXT, "اعمال الصلاة و الوضوء " +
                        "\n https://play.google.com/store/apps/developer?id=Omani%20Muslim&hl=en");
                startActivity(dropbox);
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
            case R.id.btHome:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
        subSoundCategoryDTOList = event.getResults();
        soundsAdapter = new SubSoundsAdapter(this, subSoundCategoryDTOList);
        lvContent.setAdapter(soundsAdapter);
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
