package com.despectra.android.classjournal_new.Data;

/**
 * Created by Dmitry on 14.03.14.
 */
public abstract class ScheduleItem {
    protected String mDescription;
    protected int mColor;

    @Override
    public String toString() {
        return mDescription;
    }

    public int getColor() {
        return  mColor;
    }
}
