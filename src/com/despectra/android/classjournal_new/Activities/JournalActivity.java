package com.despectra.android.classjournal_new.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.despectra.android.classjournal_new.Adapters.JournalPagerAdapter;
import com.despectra.android.classjournal_new.Fragments.JournalMarksFragment;
import com.despectra.android.classjournal_new.R;
import com.despectra.android.classjournal_new.Utils.ViewsTouchSynchronizer;

import java.util.List;

/**
 * Created by Dmirty on 17.02.14.
 */
public class JournalActivity extends FragmentActivity implements JournalMarksFragment.JournalFragmentCallback {

    private static final String TAG = "JOURNAL_ACTIVITY";

    private ListView mGroupList;

    private ViewPager mPager;
    private JournalPagerAdapter mPagerAdapter;
    private ViewsTouchSynchronizer mListAndGridSync;
    private JournalMarksFragment mCurrentGridFragment;
    private int mFragmentGridPosition;
    private int mFragmentGridOffset;
    private boolean mIsPagerDragging;

    public ListView getGroupList() {
        return mGroupList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        mGroupList = (ListView)findViewById(R.id.group_list_view);
        mGroupList.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.journal_student_item,
                new String[]{"Дубинкинa Анастасия Кузьмевна",
                        "Жиренков Александр Филиппович",
                        "Кожедубa Кристина Данииловна",
                        "Чуркинa Мария Якововна",
                        "Лапуновa Альбина Брониславовна",
                        "Умберг Харитон Елисеевич",
                        "Наумовa Агафья Романовна",
                        "Толбановa Марианна Афанасиевна",
                        "Лапунов Вячеслав Филимонович",
                        "Аниканов Артём Андриянович",
                        "Кваснин Аристарх Епифанович",
                        "Нагибин Самсон Владиславович",
                        "Салтанов Павел Прокофиевич",
                        "Молодцовa Марта Филипповна",
                        "Коллеровa Изольда Святославовна",
                        "Абабков Тихон Феликсович",
                        "Вагинa Инга Никитевна",
                        "Клецка Наталья Семеновна",
                        "Телицын Никон Всеволодович",
                        "Ягутян Вадим Тимурович"}));

        mPager = (ViewPager)findViewById(R.id.journal_pager);
        mPagerAdapter = new JournalPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if(state == ViewPager.SCROLL_STATE_DRAGGING) {
                    mIsPagerDragging = true;
                    List<Fragment> frags = getSupportFragmentManager().getFragments();
                    for (Fragment frag : frags) {
                        if (frag instanceof JournalMarksFragment) {
                            JournalMarksFragment fragment = (JournalMarksFragment) frag;
                            if(fragment.getIndex() != mPager.getCurrentItem()) {
                                fragment.setMarksGridScrolling(mFragmentGridPosition, mFragmentGridOffset);
                            }
                        }
                    }
                } else if(state == ViewPager.SCROLL_STATE_IDLE) {
                    mIsPagerDragging = false;
                    updateCurrentFragment();
                }
            }
        });
        mIsPagerDragging = false;
    }

    @Override
    public void onMarksGridScrolled(int scrolledPosition, int offset) {
        if (!mIsPagerDragging) {
            mFragmentGridPosition = scrolledPosition;
            mFragmentGridOffset = offset;
        }
    }

    @Override
    public void onFragmentCreated() {
        updateCurrentFragment();
    }

    private void updateCurrentFragment() {
        for (Fragment frag : getSupportFragmentManager().getFragments()) {
            if (frag instanceof JournalMarksFragment
                    && ((JournalMarksFragment) frag).getIndex() == mPager.getCurrentItem()) {
                mCurrentGridFragment = ((JournalMarksFragment) frag);
                break;
            }
        }
        updateListsTouchSynchronizer();
    }

    private void updateListsTouchSynchronizer() {
        if (mListAndGridSync != null) {
            mListAndGridSync.setNewViews(mGroupList, mCurrentGridFragment.getMarksGridView());
        } else {
            mListAndGridSync = new ViewsTouchSynchronizer(this, mGroupList, mCurrentGridFragment.getMarksGridView());
        }
    }
}
