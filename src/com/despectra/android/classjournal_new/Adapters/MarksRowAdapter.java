package com.despectra.android.classjournal_new.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Dmirty on 04.03.14.
 */
public class MarksRowAdapter extends BaseAdapter {

    private int mCellLayout;
    private int mColumnsCount;
    private String[] mData;
    private Context mContext;

    public MarksRowAdapter(Context c, int cellLayout, int columnsCount, String[] data) {
        if (data.length % columnsCount != 0) {
            throw new IllegalArgumentException("Data items count must be KRATNYM to columnsCount");
        }
        mContext = c;
        mCellLayout = cellLayout;
        mColumnsCount = columnsCount;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.length / mColumnsCount;
    }

    @Override
    public Object getItem(int i) {
        return mData[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = convertView;
        LinearLayout row;
        if(v == null) {
            row = new LinearLayout(mContext) {
                @Override
                public boolean onInterceptTouchEvent(MotionEvent ev) {
                    return false;
                }
            };
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

            int cellWidth = parent.getWidth() / mColumnsCount;
            for (int column = 0; column < mColumnsCount; column++) {
                View cellView = LayoutInflater.from(mContext).inflate(mCellLayout, null, false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        cellWidth,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                row.addView(cellView, params);
            }
            v = row;
        }
        row = (LinearLayout)v;
        for (int column = 0; column < mColumnsCount; column++) {
            TextView cell = (TextView) row.getChildAt(column);
            cell.setText(mData[i * mColumnsCount + column]);
        }
        return v;
    }
}
