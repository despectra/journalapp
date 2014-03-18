package com.despectra.android.classjournal_new.Adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.despectra.android.classjournal_new.Data.Lesson;
import com.despectra.android.classjournal_new.Data.WeekSchedule;
import com.despectra.android.classjournal_new.Data.ScheduleItem;
import com.despectra.android.classjournal_new.R;

/**
 * Created by Dmitry on 14.03.14.
 */
public class ScheduleRowAdapter extends HorizontalViewsRowAdapter {
    private static final String TAG = "ScheduleRowAdapter";
    private static final int COLS_COUNT = 8;
    public static final int TIME_INTERVAL_LAYOUT_RES = R.layout.week_schedule_time_item;
    private static final int ITEM_LAYOUT_RES = R.layout.week_schedule_item;
    public static final int ITEM_BACKGROUND_IDLE_BORDERS = R.drawable.border_left_bottom;
    public static final int ITEM_BACKGROUND_IDLE_CLICKABLE = R.drawable.schedule_item_background_selector;

    private ScheduleItemClickListener mClickListener;
    private int mLeftHeaderWidth;
    private int mItemWidth;

    public ScheduleRowAdapter(Context context, WeekSchedule schedule) {
        super(context, null, COLS_COUNT, null, null);
        setCellsLayouts();
        changeSchedule(schedule, false);
    }

    public ScheduleRowAdapter(Context context, WeekSchedule schedule, int leftHeaderWidth, int itemWidth) {
        super(context, null, COLS_COUNT, null, null);
        setCellsLayouts();
        changeSchedule(schedule, false);

        int[] columnsWidths = new int[COLS_COUNT];
        columnsWidths[0] = leftHeaderWidth;
        for (int i = 1; i < COLS_COUNT; i++) {
            columnsWidths[i] = itemWidth;
        }
        setColumnsWidths(columnsWidths, false);
    }

    public void setItemClickListener(ScheduleItemClickListener listener) {
        mClickListener = listener;
    }

    public void setSchedule(WeekSchedule schedule) {
        changeSchedule(schedule, true);
    }

    public void setLeftHeaderWidth(int width) {
        mLeftHeaderWidth = width;
        setColumnWidthAtPos(0, mLeftHeaderWidth);
    }

    public void setItemWidth(int width) {
        mItemWidth = width;
        for (int i = 1; i < COLS_COUNT; i++) {
            setColumnWidthAtPos(i, mItemWidth);
        }
    }

    private void setCellsLayouts() {
        setCellLayoutAtPos(0, TIME_INTERVAL_LAYOUT_RES, false);
        for (int i = 1; i < COLS_COUNT; i++) {
            setCellLayoutAtPos(i, ITEM_LAYOUT_RES, false);
        }
    }

    private void changeSchedule(WeekSchedule schedule, boolean notifyChanged) {
        int rowsCount = schedule.getLastItemPositionInLongestDay() + 1;
        ScheduleItem[] data = new ScheduleItem[COLS_COUNT * rowsCount];
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < COLS_COUNT; j++) {
                data[i * COLS_COUNT + j] = (j > 0) ? schedule.getScheduleItem(j - 1, i) : null;
            }
        }
        setData(data, notifyChanged);
    }

    @Override
    public void defineNewViewHolder(int row, int column, View view) {
        if (column % COLS_COUNT == 0) {
            return;
        }
        ViewHolder holder = new ViewHolder();
        holder.groupView = (TextView) view.findViewById(R.id.schedule_item_group);
        holder.subjectView = (TextView) view.findViewById(R.id.schedule_item_subject);
        view.setTag(holder);
    }

    @Override
    public void bindSingleViewToData(final int row, final int column, View view, Object dataObject) {
        if (column % COLS_COUNT == 0) {
            clearSingleView(view);
            return;
        }
        Lesson lesson = (Lesson) dataObject;
        if (lesson != null) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.groupView.setText(lesson.getGroup().label);
            holder.subjectView.setText(lesson.getSubject().label);

            final ColorDrawable color = new ColorDrawable(lesson.getColor());
            LayerDrawable background = new LayerDrawable(
                new Drawable[]{
                    color,
                    getContext().getResources().getDrawable(ITEM_BACKGROUND_IDLE_BORDERS)
                }
            );
            view.setBackground(background);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if (action == MotionEvent.ACTION_DOWN) {
                        LayerDrawable background = new LayerDrawable(
                                new Drawable[]{
                                        color,
                                        new ColorDrawable(getContext().getResources().getColor(R.color.lighten)),
                                        getContext().getResources().getDrawable(ITEM_BACKGROUND_IDLE_BORDERS)
                                }
                        );
                        view.setBackground(background);
                    } else if (action == MotionEvent.ACTION_UP
                            || action == MotionEvent.ACTION_POINTER_UP
                            || action == MotionEvent.ACTION_CANCEL) {
                        LayerDrawable background = new LayerDrawable(
                                new Drawable[]{
                                        color,
                                        getContext().getResources().getDrawable(ITEM_BACKGROUND_IDLE_BORDERS)
                                }
                        );
                        view.setBackground(background);
                    }
                    return false;
                }
            });
        } else {
            clearSingleView(view);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onClick(column, row);
                }
            }
        });
    }

    private void clearSingleView(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder != null) {
            holder.groupView.setText("");
            holder.subjectView.setText("");
            view.setBackgroundResource(ITEM_BACKGROUND_IDLE_CLICKABLE);
            view.setOnTouchListener(null);
        }
    }

    private static class ViewHolder {
        TextView groupView;
        TextView subjectView;
    }

    public interface ScheduleItemClickListener {
        public void onClick(int day, int position);
    }
}
