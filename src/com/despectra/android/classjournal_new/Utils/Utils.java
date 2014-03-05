package com.despectra.android.classjournal_new.Utils;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by Dmirty on 05.03.14.
 */
public class Utils {
    public static int getAbsListViewOffset(AbsListView listView) {
        int offset = 0;
        final View first = listView.getChildAt(0);
        if (first != null) {
            offset += first.getTop();
        }
        return offset;
    }
}
