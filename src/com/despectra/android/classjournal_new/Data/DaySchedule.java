package com.despectra.android.classjournal_new.Data;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by Dmitry on 14.03.14.
 */
public class DaySchedule {
    private int mDay;
    private Map<Integer, ScheduleItem> mSchedItems;

    public DaySchedule(int day) {
        mDay = day;
        mSchedItems = new IdentityHashMap<Integer, ScheduleItem>(10);
    }

    public DaySchedule(int day, Map<Integer, ScheduleItem> data) {
        mDay = day;
        mSchedItems = data;
    }

    public void setScheduleItem(int position, ScheduleItem item) {
        mSchedItems.put(position, item);
    }

    public ScheduleItem getScheduleItem(int position) {
        return mSchedItems.get(position);
    }

    public int getLastActiveItemPosition() {
        if(mSchedItems.isEmpty()) {
            return 0;
        }
        return Collections.max(mSchedItems.keySet());
    }
}
