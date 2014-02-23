package com.despectra.android.classjournal_new.Utils;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import com.despectra.android.classjournal_new.Adapters.ToggleableArrayAdapter;

/**
 * Created by Dmirty on 20.02.14.
 */
public class AbsListViewsTouchSynchronizer {
    private AbsListView mFirstView;
    private AbsListView mSecondView;
    private ToggleableArrayAdapter<String> mFirstAdapter;
    private ToggleableArrayAdapter<String> mSecondAdapter;
    private boolean mTouchedFirst;
    private boolean mTouchedSecond;

    private View.OnTouchListener mFirstListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!mTouchedSecond) {
                if (!mTouchedFirst) {
                    mTouchedFirst = true;
                    if (mSecondAdapter != null) {
                        mSecondAdapter.disableAllItems();
                    }
                }
                mSecondView.dispatchTouchEvent(motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    mTouchedFirst = false;
                    if (mSecondAdapter != null) {
                        mSecondAdapter.enableAllItems();
                    }
                }
            }
            return false;
        }
    };
    private View.OnTouchListener mSecondListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!mTouchedFirst) {
                if (!mTouchedSecond) {
                    mTouchedSecond = true;
                    if (mFirstAdapter != null) {
                        mFirstAdapter.disableAllItems();
                    }
                }
                mFirstView.dispatchTouchEvent(motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    mTouchedSecond = false;
                    if (mFirstAdapter != null) {
                        mFirstAdapter.enableAllItems();
                    }
                }
            }
            return false;
        }
    };

    public AbsListViewsTouchSynchronizer(Context c, AbsListView v1, AbsListView v2) {
        mFirstView = v1;
        mSecondView = v2;
        mTouchedFirst = false;
        mTouchedSecond = false;
        updateAdapters();
        attachTouchListeners();
    }

    public void setNewViews(AbsListView v1, AbsListView v2) {
        mFirstView.setOnTouchListener(null);
        mSecondView.setOnTouchListener(null);
        mFirstView = v1;
        mSecondView = v2;
        updateAdapters();
        attachTouchListeners();
    }

    private void attachTouchListeners() {
        mFirstView.setOnTouchListener(mFirstListener);
        mSecondView.setOnTouchListener(mSecondListener);
    }

    private void updateAdapters() {
        try {
            mFirstAdapter = ((ToggleableArrayAdapter<String>) mFirstView.getAdapter());
            mSecondAdapter = ((ToggleableArrayAdapter<String>) mSecondView.getAdapter());
        } catch (Exception ex) {
            Log.e("ERR", "Adapter isnt toggleable");
        }
    }
}
