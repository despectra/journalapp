package com.despectra.android.classjournal_new.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import com.despectra.android.classjournal_new.R;

import java.util.Random;

/**
 * Created by Dmirty on 17.02.14.
 */
public class JournalMarksFragment extends Fragment {
    public static final String ARG_POSITION = "pos";
    public static final String ARG_OFFSET = "offset";
    private static final String TAG = "JOURNAL_MARKS_FRAG";

    private OnMarksGridScrolledListener mGridScrollListener;
    private GridView mHeaderGrid;
    private GridView mMarksGrid;

    public JournalMarksFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGridScrollListener = (OnMarksGridScrolledListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGridScrollListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_journal, container, false);
        mHeaderGrid = (GridView)v.findViewById(R.id.journal_header_view);
        mMarksGrid = (GridView)v.findViewById(R.id.journal_marks_view);

        String[] headerData = new String[]{"1 сен. 2014", " 4 сен. 2014", " 8 сен. 2014", " 10 сен. 2014", " 18 сен. 2014", " 21 сен 2014"};
        String[] marksData = new String[120];
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < marksData.length; i++) {
            marksData[i] = String.valueOf(rand.nextInt(5));
        }
        mHeaderGrid.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.journal_mark_item, headerData));
        mMarksGrid.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.journal_mark_item, marksData));

        mMarksGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mGridScrollListener != null) {
                    int offset = (int)getActivity().getResources().getDisplayMetrics().density;
                    int index = mMarksGrid.getFirstVisiblePosition();
                    final View first = mMarksGrid.getChildAt(0);
                    if (first != null) {
                        offset -= first.getTop() + 1;
                    }
                    mGridScrollListener.onMarksGridScrolled(motionEvent, index, offset);
                }
                return false;
            }
        });
        return v;
    }

    public void setMarksGridScrolling(int position, int offset) {
        mMarksGrid.smoothScrollToPositionFromTop(position, -offset, 1);
    }

    public interface OnMarksGridScrolledListener {
        public void onMarksGridScrolled(MotionEvent motionEvent, int scrolledPosition, int offset);
    }
}
