package com.despectra.android.classjournal_new.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.despectra.android.classjournal_new.Fragments.JournalMarksFragment;

/**
 * Created by Dmirty on 17.02.14.
 */
public class JournalPagerAdapter extends FragmentStatePagerAdapter {

    public JournalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        JournalMarksFragment fragment = new JournalMarksFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return 20;
    }
}
