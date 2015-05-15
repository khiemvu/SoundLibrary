package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.oman.allinone.R;
import com.oman.allinone.database.entity.Favourite;
import com.oman.allinone.database.ormlite.DatabaseHelper;
import com.oman.allinone.ui.adapter.FavouriteAdapter;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Khiemvx on 5/15/2015.
 */
public class FavouriteActivity extends Activity implements View.OnClickListener
{
    FavouriteAdapter favouriteAdapter;
    private List<Favourite> favouriteList;
    private TextView btBack, tvTitle;
    private Button btHome, btSearch, btShare, btFavourite;
    private ListView lvContent;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sound_category);
        lvContent = (ListView) findViewById(R.id.lvContent);
        btBack = (TextView) findViewById(R.id.tvBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Categories");
        btHome = (Button) findViewById(R.id.btHome);
        btSearch = (Button) findViewById(R.id.btSearch);
        btFavourite = (Button) findViewById(R.id.btFavourite);
        btShare = (Button) findViewById(R.id.btShare);
        btBack.setOnClickListener(this);
        try
        {
            favouriteList = DatabaseHelper.getInstance(this).getFavouriteDAO().queryForAll();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        favouriteAdapter = new FavouriteAdapter(this, favouriteList);
        lvContent.setAdapter(favouriteAdapter);
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Favourite favourite = ((Favourite) favouriteAdapter.getItem(position));
                Intent intent = new Intent(getApplicationContext(), PlaySoundActivity.class);
                intent.putExtra("url", favourite.getUrl());
                intent.putExtra("position", favourite.getPosition());
                intent.putExtra("category_name", favourite.getCategoryName());
                intent.putExtra("subCategory_name", favourite.getSubCategoryName());
                intent.putExtra("file_name", favourite.getFileName());
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v)
    {

    }
}
