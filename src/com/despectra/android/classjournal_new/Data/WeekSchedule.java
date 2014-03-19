package com.despectra.android.classjournal_new.Data;

import org.json.JSONObject;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by Dmitry on 13.03.14.
 *
 *
 * JSON Format of Schedule
 *
 *      "Days" : [
 *          {
 *              "DayOfWeek" : "MON",
 *              "ScheduleItemsCount" : 5,
 *              "ScheduleItems" : [
 *                  {
 *                      "ItemType" : "Lesson",
 *                      "ItemNumber" : 1,
 *                      "Subject" : {
 *                          "label" : "ICT",
 *                          "value" : 1
 *                      },
 *                      "Group" : {
 *                          "label" : "8A",
 *                          "value" : 10
 *                      },
 *                      "LessonType" : {
 *                          "label" : "Самостоятельная",
 *                          "value" : 3
 *                      }
 *                  },
 *                  ...
 *              ]
 *          },
 *          ...
 *      ]
 *
 *
 */
public class WeekSchedule {

    public static final int MON = 0;
    public static final int TUE = 1;
    public static final int WED = 2;
    public static final int THU = 3;
    public static final int FRI = 4;
    public static final int SAT = 5;
    public static final int SUN = 6;

    private static final int DAYS_IN_WEEK = 7;

    private DaySchedule[] mDaySchedules;
    private String[] mTimeIntervals;

    public WeekSchedule() {
        mDaySchedules = new DaySchedule[DAYS_IN_WEEK];
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            mDaySchedules[i] = new DaySchedule(i);
        }
    }

    public WeekSchedule(JSONObject jsonScheduleData) {
        //TODO implement extracting data from JSON
    }

    public void setScheduleItem(int day, int position, ScheduleItem item) {
        mDaySchedules[day].setScheduleItem(position, item);
    }

    public void setLesson(int day, int position, String subject, String group, String lessonType, int color) {
        mDaySchedules[day].setScheduleItem(position, new Lesson(subject, group, lessonType, color));
    }

    public int getLastItemPositionInLongestDay() {
        int lastPosition = mDaySchedules[MON].getLastActiveItemPosition();
        for (int i = 1; i < DAYS_IN_WEEK; i++) {
            int curLastPosition = mDaySchedules[i].getLastActiveItemPosition();
            if(lastPosition < curLastPosition) {
                lastPosition = curLastPosition;
            }
        }
        return lastPosition;
    }

    public ScheduleItem getScheduleItem(int day, int position) {
        return mDaySchedules[day].getScheduleItem(position);
    }
}
