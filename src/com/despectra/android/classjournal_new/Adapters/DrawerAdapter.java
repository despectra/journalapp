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

    public static final Integer HEADER = 0;
    public static final Integer ITEM = 1;

    private Context mContext;
    private  String[] mData;
    private int[] mTypes;
    private int mItemLayout;
    private int mHeaderLayout;

    public DrawerAdapter(Context context, String[] data, int[] types, int itemLayout, int headerLayout) {
        super();
        mContext = context;
        mData = data;
        mTypes = types;
        mItemLayout = itemLayout;
        mHeaderLayout = headerLayout;
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
        if (isHeader(pos)) {
            if(v == null || v.getTag() == ITEM) {
                v = LayoutInflater.from(mContext).inflate(mHeaderLayout, parent, false);
                v.setEnabled(false);
                v.setTag(HEADER);
            }
        } else if(v == null || v.getTag() == HEADER) {
            v = LayoutInflater.from(mContext).inflate(mItemLayout, parent, false);
            v.setTag(ITEM);
        }
        TextView itemTextView = (TextView)v.findViewById(android.R.id.text1);
        itemTextView.setText(mData[pos]);
        return v;
    }

    @Override
    public boolean isEnabled(int position) {
        return !isHeader(position);
    }

    private boolean isHeader(int pos) {
        return mTypes[pos] == HEADER;
    }
}
