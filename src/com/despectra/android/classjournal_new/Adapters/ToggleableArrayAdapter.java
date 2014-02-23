package com.despectra.android.classjournal_new.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

/**
 * Created by Андрей on 22.02.14.
 */
public class ToggleableArrayAdapter<T> extends ArrayAdapter<T> {
    private boolean mAreAllEnabled = true;

    public ToggleableArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ToggleableArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ToggleableArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ToggleableArrayAdapter(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, objects);
    }

    public ToggleableArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ToggleableArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAreAllEnabled;
    }

    public void enableAllItems() {
        mAreAllEnabled = true;
        notifyDataSetChanged();
    }

    public void disableAllItems() {
        mAreAllEnabled = false;
        notifyDataSetChanged();
    }
}
