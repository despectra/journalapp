package com.despectra.android.classjournal_new.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import com.despectra.android.classjournal_new.R;

/**
 * Created by Dmirty on 18.02.14.
 */
public class MarkAdapter extends ToggleableArrayAdapter<String> {

    public MarkAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
    }
}
