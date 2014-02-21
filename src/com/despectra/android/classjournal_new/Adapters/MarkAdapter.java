package com.despectra.android.classjournal_new.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.despectra.android.classjournal_new.R;

/**
 * Created by Dmirty on 18.02.14.
 */
public class MarkAdapter extends BaseAdapter {

    private Context mContext;
    private int[] mMarks;

    public MarkAdapter(Context context, int[] marks) {
        super();
        mContext = context;
        mMarks = marks;
    }

    @Override
    public int getCount() {
        return mMarks.length;
    }

    @Override
    public Object getItem(int pos) {
        return mMarks[pos];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.journal_mark_item, parent, false);
        }
        return v;
    }
}
