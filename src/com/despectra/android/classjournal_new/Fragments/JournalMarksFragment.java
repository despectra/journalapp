package com.despectra.android.classjournal_new.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import com.despectra.android.classjournal_new.Activities.JournalActivity;
import com.despectra.android.classjournal_new.R;

import java.util.Random;

/**
 * Created by Dmirty on 17.02.14.
 */
public class JournalMarksFragment extends Fragment implements AbsListView.OnScrollListener {
    public static final String ARG_POSITION = "pos";
    public static final String ARG_OFFSET = "offset";
    public static final String ARG_INDEX = "index";
    private static final String TAG = "JOURNAL_MARKS_FRAG";

    private JournalFragmentCallback mFragmentCallback;
    private GridView mHeaderGrid;
    private GridView mMarksGrid;
    private ListView mLinkedGroupList;
    private int mIndex;

    public JournalMarksFragment() {
        super();
    }

    public int getIndex() {
        return mIndex;
    }

    public GridView getMarksGridView() {
        return  mMarksGrid;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (JournalFragmentCallback) activity;
        mLinkedGroupList = ((JournalActivity) activity).getGroupList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentCallback = null;
        mLinkedGroupList = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndex = getArguments().getInt(ARG_INDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_journal, container, false);
        mHeaderGrid = (GridView)v.findViewById(R.id.journal_header_view);
        mMarksGrid = (GridView)v.findViewById(R.id.journal_marks_view);

        String[] headerData = new String[]{"Section 1", "Section2", "Section 3", "Section 4", "Section 5", "Section 6"};
        String[] marksData = new String[120];
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < marksData.length; i++) {
            marksData[i] = String.valueOf(i);
        }
        mHeaderGrid.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.journal_mark_item, headerData));
        mMarksGrid.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.journal_mark_item, marksData));
        mMarksGrid.setOnScrollListener(this);
        if (mFragmentCallback != null) {
            mFragmentCallback.onFragmentCreated();
        }
        return v;
    }

    public void setMarksGridScrolling(final int position, final int offset) {
        mMarksGrid.setSelection(position);
        mMarksGrid.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMarksGrid.smoothScrollToPositionFromTop(position, offset, 1);
            }
        }, 1);
    }

    private int calculateGridOffset(AbsListView grid) {
        int offset = 0;
        final View first = grid.getChildAt(0);
        if (first != null) {
            offset += first.getTop();
        }
        return offset;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int state) {
        if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mFragmentCallback != null) {
            int position = mMarksGrid.getFirstVisiblePosition();
            int offset = calculateGridOffset(mMarksGrid);
            mFragmentCallback.onMarksGridScrolled(position, offset);
            mLinkedGroupList.setSelectionFromTop(position / 6, offset);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    public interface JournalFragmentCallback {
        public void onMarksGridScrolled(int scrolledPosition, int offset);
        public void onFragmentCreated();
    }
}
