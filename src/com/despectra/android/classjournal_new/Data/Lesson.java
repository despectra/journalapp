package com.despectra.android.classjournal_new.Data;

/**
 * Created by Dmitry on 14.03.14.
 */
public class Lesson extends ScheduleItem {
    private DataItem mSubject;
    private DataItem mGroup;
    private String mLessonType;

    public Lesson(String subject, String group, String type, int color) {
        mSubject = new DataItem(subject, 0);
        mGroup = new DataItem(group, 0);
        mLessonType = type;
        mColor = color;
    }

    public DataItem getSubject() {
        return mSubject;
    }

    public DataItem getGroup() {
        return mGroup;
    }
}
