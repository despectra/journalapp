package com.despectra.android.classjournal_new.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import com.despectra.android.classjournal_new.Activities.MainActivity;
import com.despectra.android.classjournal_new.Adapters.HorizontalViewsRowAdapter;
import com.despectra.android.classjournal_new.Adapters.MarksRowAdapter;
import com.despectra.android.classjournal_new.R;

import java.util.Random;

/**
 * Created by Dmirty on 17.02.14.
 */
public class JournalMarksFragment extends Fragment {
    public static final String ARG_POSITION = "pos";
    public static final String ARG_OFFSET = "offset";
    public static final String ARG_INDEX = "index";
    private static final String TAG = "JOURNAL_MARKS_FRAG";

    private JournalFragmentCallback mFragmentCallback;
    private GridView mHeaderGrid;
    private ListView mMarksGrid;
    private int mIndex;

    public JournalMarksFragment() {
        super();
    }

    public int getIndex() {
        return mIndex;
    }

    public ListView getMarksGridView() {
        return  mMarksGrid;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = ((MainActivity) activity).getJournalCallback();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentCallback = null;
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
        mMarksGrid = (ListView)v.findViewById(R.id.journal_marks_view);

        String[] headerData = new String[]{"Section 1", "Section2", "Section 3", "Section 4", "Section 5", "Section 6"};
        String[] marksData = new String[120];
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < marksData.length; i++) {
            marksData[i] = String.valueOf(i);
        }
        mHeaderGrid.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.journal_mark_item, headerData));
        mMarksGrid.setAdapter(new MarksRowAdapter(getActivity(), 6, marksData));
        if (mFragmentCallback != null) {
            mFragmentCallback.onFragmentCreated(mIndex);
        }
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "JOURNAL MARKS FRAGMENT ONDESTROY");
    }

    public void setMarksGridScrolling(final int position, final int offset) {
        mMarksGrid.setSelectionFromTop(position, offset);
    }

    public interface JournalFragmentCallback {
        public void onFragmentCreated(int index);
    }
}
