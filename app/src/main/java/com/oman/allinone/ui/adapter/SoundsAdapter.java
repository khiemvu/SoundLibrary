package com.oman.allinone.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.oman.allinone.R;
import com.oman.allinone.dto.SoundCategoryDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class SoundsAdapter extends BaseAdapter {
    private final Activity context;
    private List<SoundCategoryDTO> listContents;
    private LayoutInflater inflater;

    public SoundsAdapter(Activity context, List<SoundCategoryDTO> listContents) {
        this.context = context;
        this.listContents = listContents;
    }

    @Override
    public int getCount() {
        return listContents.size();
    }

    @Override
    public Object getItem(int position) {
        return listContents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_list_view, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.tvContentItem);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.text.setText(listContents.get(position).getTitle());

        return rowView;
    }

    public List<SoundCategoryDTO> getListContents()
    {
        return listContents;
    }

    public void setListContents(List<SoundCategoryDTO> listContents)
    {
        this.listContents = listContents;
    }

    static class ViewHolder {
        public TextView text;
    }
}
