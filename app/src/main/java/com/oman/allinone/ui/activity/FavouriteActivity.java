package com.oman.allinone.ui.activity;

import android.app.Activity;
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

import com.oman.allinone.R;
import com.oman.allinone.database.entity.Favourite;
import com.oman.allinone.database.ormlite.DatabaseHelper;
import com.oman.allinone.ui.adapter.FavouriteAdapter;
import com.oman.allinone.utils.CommonUtils;
import com.oman.allinone.utils.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Khiemvx on 5/15/2015.
 */
public class FavouriteActivity extends Activity implements View.OnClickListener {
    FavouriteAdapter favouriteAdapter;
    CheckBox btFavourite;
    EditText etSearch;
    TextView tvCancelSearch;
    ImageView ivClearText;
    LinearLayout ll_search, ll_menu;
    private List<Favourite> favouriteList;
    private TextView btBack, tvTitle;
    private Button btHome, btSearch, btShare;
    private ListView lvContent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sound_category);
        lvContent = (ListView) findViewById(R.id.lvContent);
        btBack = (TextView) findViewById(R.id.tvBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Favourites");
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btFavourite = (CheckBox) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);
        btShare.setOnClickListener(this);
        //block search
        etSearch = (EditText) findViewById(R.id.etSearch);
        tvCancelSearch = (TextView) findViewById(R.id.tvCancel);
        ivClearText = (ImageView) findViewById(R.id.ivCancelSearch);
        ll_search = (LinearLayout) findViewById(R.id.rlSearch);
        ll_menu = (LinearLayout) findViewById(R.id.ll_menu_common);
        btSearch = (Button) findViewById(R.id.btSearch);
        ll_search.setVisibility(View.GONE);
        etSearch.addTextChangedListener(textWatcher);
        ivClearText.setOnClickListener(this);
        tvCancelSearch.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btBack.setOnClickListener(this);
        btHome.setOnClickListener(this);
        try {
            favouriteList = DatabaseHelper.getInstance(this).getFavouriteDAO().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        favouriteAdapter = new FavouriteAdapter(this, favouriteList);
        lvContent.setAdapter(favouriteAdapter);
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Favourite favourite = ((Favourite) favouriteAdapter.getItem(position));
                Intent intent = new Intent(getApplicationContext(), PlaySoundActivity.class);
                intent.putExtra("url", favourite.getUrl());
                intent.putExtra("position", favourite.getPosition());
                intent.putExtra("category_name", favourite.getCategoryName());
                intent.putExtra("subCategory_name", favourite.getSubCategoryName());
                intent.putExtra("file_name", favourite.getFileName());
                intent.putExtra("file_id", favourite.getFileID());
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public List<Favourite> getSoundFavouriteMatchingWithText(List<Favourite> favourites, String input) {
        List<Favourite> resultSearch = new ArrayList<Favourite>();
        for (Favourite favourite : favourites) {
            String categoryName = favourite.getFileName();
            String subCategoryName = favourite.getSubCategoryName();
            String fileName = favourite.getFileName();

            if (categoryName.toLowerCase().contains(input) || subCategoryName.toLowerCase().contains(input)
                    || fileName.toLowerCase().contains(input)) {
                resultSearch.add(favourite);
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
                    final List<Favourite> favourites;
                    favourites = favouriteAdapter.getListContents();
                    if (StringUtils.isNotBlank(s.toString())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivClearText.setVisibility(View.VISIBLE);
                                List<Favourite> favouriteResults = getSoundFavouriteMatchingWithText(favourites, s.toString().toLowerCase().trim());
                                favouriteAdapter.setListContents(favouriteResults);
                                favouriteAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivClearText.setVisibility(View.INVISIBLE);
                                favouriteAdapter.setListContents(favouriteList);
                                favouriteAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }, SEARCH_TYPING_DELAY);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btSearch:
                ll_search.setVisibility(View.VISIBLE);
                ll_menu.setVisibility(View.GONE);
                break;
            case R.id.btShare:
                Intent dropbox = new Intent(Intent.ACTION_SEND);
                dropbox.setType("text/plain");
                dropbox.putExtra(Intent.EXTRA_TEXT, "اعمال الصلاة و الوضوء " +
                        "\n https://play.google.com/store/apps/developer?id=Omani%20Muslim&hl=en");
                startActivity(dropbox);
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


}
