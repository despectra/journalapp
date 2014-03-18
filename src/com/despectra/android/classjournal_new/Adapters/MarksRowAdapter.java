package com.despectra.android.classjournal_new.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.despectra.android.classjournal_new.R;

/**
 * Created by Dmitry on 18.03.14.
 */
public class MarksRowAdapter extends HorizontalViewsRowAdapter {

    public MarksRowAdapter(Context c, int columnsCount, String[] data) {
        super(c, R.layout.journal_mark_item, columnsCount, data);
    }

    @Override
    public void defineNewViewHolder(int row, int column, View view) {
    }

    @Override
    public void bindSingleViewToData(int row, int column, View view, Object dataObject) {
        TextView markView = (TextView) view;
        markView.setText((String) dataObject);
    }
}
