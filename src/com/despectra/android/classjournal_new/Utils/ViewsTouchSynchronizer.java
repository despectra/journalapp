package com.despectra.android.classjournal_new.Utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dmirty on 20.02.14.
 */
public class ViewsTouchSynchronizer {
    private View mFirstView;
    private View mSecondView;
    private GestureDetector mDetector;
    private boolean mTouchedFirst;
    private boolean mTouchedSecond;

    private View.OnTouchListener mFirstListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!mTouchedSecond) {
                if (!mTouchedFirst) {
                    mTouchedFirst = true;
                }
                mSecondView.dispatchTouchEvent(motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    mTouchedFirst = false;
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
                }
                mFirstView.dispatchTouchEvent(motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    mTouchedSecond = false;
                }
            }
            return false;
        }
    };

    public ViewsTouchSynchronizer(Context c, View v1, View v2) {
        mFirstView = v1;
        mSecondView = v2;
        mTouchedFirst = false;
        mTouchedSecond = false;
        attachTouchListeners();
    }

    public void setNewViews(View v1, View v2) {
        mFirstView.setOnTouchListener(null);
        mSecondView.setOnTouchListener(null);
        mFirstView = v1;
        mSecondView = v2;
        attachTouchListeners();
    }

    private void attachTouchListeners() {
        mFirstView.setOnTouchListener(mFirstListener);
        mSecondView.setOnTouchListener(mSecondListener);
    }
}
