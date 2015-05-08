package com.oman.allinone.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oman.allinone.R;
import com.oman.allinone.dto.ListSoundDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class SoundsAdapter extends BaseAdapter {
    private final Activity context;
    private final List<ListSoundDTO> listContents;
    private LayoutInflater inflater;

    public SoundsAdapter(Activity context, List<ListSoundDTO> listContents) {
        this.context = context;
        this.listContents = listContents;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

    static class ViewHolder {
        public TextView text;
    }
}