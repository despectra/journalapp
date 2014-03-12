package com.despectra.android.classjournal_new.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import com.despectra.android.classjournal_new.Fragments.JournalMarksFragment;

/**
 * Created by Dmirty on 17.02.14.
 */
public class JournalPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "JOURNAL_PAGER_ADAPTER";

    public JournalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        JournalMarksFragment fragment = new JournalMarksFragment();
        Bundle args = new Bundle();
        args.putInt(JournalMarksFragment.ARG_INDEX, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 20;
    }
}
