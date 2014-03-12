package com.despectra.android.classjournal_new.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.despectra.android.classjournal_new.R;

/**
 * Created by Dmirty on 17.02.14.
 */
public class DrawerAdapter extends BaseAdapter {

    private Context mContext;
    private  String[] mData;
    private int mItemLayout;

    public DrawerAdapter(Context context, String[] data, int itemLayout) {
        super();
        mContext = context;
        mData = data;
        mItemLayout = itemLayout;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object getItem(int pos) {
        return mData[pos];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            v = LayoutInflater.from(mContext).inflate(mItemLayout, parent, false);
        }
        TextView itemTextView = (TextView)v;
        itemTextView.setText(mData[pos]);
        return v;
    }
}
