package com.oman.allinone.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.oman.allinone.R;
import com.oman.allinone.database.entity.Favourite;

import java.util.List;

/**
 * Created by khiemvx on 15/05/2015.
 */
public class FavouriteAdapter extends BaseAdapter
{
    private final Activity context;
    private List<Favourite> listContents;
    private LayoutInflater inflater;

    public FavouriteAdapter(Activity context, List<Favourite> listContents)
    {
        this.context = context;
        this.listContents = listContents;
    }

    @Override
    public int getCount()
    {
        return listContents.size();
    }

    @Override
    public Object getItem(int position)
    {
        return listContents.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = convertView;
        if (rowView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.favourite_item_view, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvCategory = (TextView) rowView.findViewById(R.id.tvCategoryName);
            viewHolder.tvSubCategory = (TextView) rowView.findViewById(R.id.tvSubCategoryName);
            viewHolder.tvFile = (TextView) rowView.findViewById(R.id.tvFileName);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.tvCategory.setText(listContents.get(position).getCategoryName());
        holder.tvSubCategory.setText(listContents.get(position).getSubCategoryName());
        holder.tvFile.setText(listContents.get(position).getFileName());

        return rowView;
    }

    public List<Favourite> getListContents()
    {
        return listContents;
    }

    public void setListContents(List<Favourite> listContents)
    {
        this.listContents = listContents;
    }

    static class ViewHolder
    {
        public TextView tvCategory;
        public TextView tvSubCategory;
        public TextView tvFile;
    }
}
