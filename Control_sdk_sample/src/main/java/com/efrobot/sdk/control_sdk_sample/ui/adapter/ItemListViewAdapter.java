package com.efrobot.sdk.control_sdk_sample.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.efrobot.sdk.control_sdk_sample.R;

import java.util.List;

public class ItemListViewAdapter extends BaseAdapter {

    private  List<String> mEntities;

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemListViewAdapter(Context context, List<String> entities) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.mEntities=entities;
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @Override
    public String getItem(int position) {
        return mEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_listview, parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((String)getItem(position), (ViewHolder) convertView.getTag(),position);
        return convertView;
    }

    private void initializeViews(String entity, ViewHolder holder,int position) {
            holder.lvTv.setText(entity);
    }

    protected class ViewHolder {
        private TextView lvTv;

        public ViewHolder(View view) {
            lvTv = (TextView) view.findViewById(R.id.lv_tv);
        }
    }
}
