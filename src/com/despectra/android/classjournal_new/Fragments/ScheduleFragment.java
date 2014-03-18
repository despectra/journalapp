package com.despectra.android.classjournal_new.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.despectra.android.classjournal_new.Data.WeekSchedule;
import com.despectra.android.classjournal_new.R;
import com.despectra.android.classjournal_new.Views.WeekScheduleView;

/**
 * Created by Dmitry on 13.03.14.
 */
public class ScheduleFragment extends Fragment {

    WeekScheduleView mScheduleView;


    public ScheduleFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        mScheduleView = (WeekScheduleView) v.findViewById(R.id.schedule_view);

        fillSchedule();

        return v;
    }

    private void fillSchedule() {
        WeekSchedule schedule = new WeekSchedule();
        schedule.setLesson(WeekSchedule.MON, 0, "ИКТ", "8 А", "", Color.parseColor("#bbafef05"));
        schedule.setLesson(WeekSchedule.MON, 1, "Информатика и ИКТ", "9 А", "", Color.parseColor("#bbffef05"));
        schedule.setLesson(WeekSchedule.MON, 2, "Алгебра", "10 А", "", Color.parseColor("#bb4f5f35"));
        schedule.setLesson(WeekSchedule.MON, 6, "Математика", "11 Б", "", Color.parseColor("#bb324f0f"));
        schedule.setLesson(WeekSchedule.TUE, 0, "История", "8 А", "", Color.parseColor("#bbafefff"));
        schedule.setLesson(WeekSchedule.TUE, 1, "Культурология", "8 Б", "", Color.parseColor("#44afef00"));
        schedule.setLesson(WeekSchedule.TUE, 3, "МХК", "7 А", "", Color.parseColor("#b6afe305"));
        mScheduleView.setSchedule(schedule);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
