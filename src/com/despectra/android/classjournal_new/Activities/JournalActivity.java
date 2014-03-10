package com.despectra.android.classjournal_new.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.despectra.android.classjournal_new.Adapters.JournalPagerAdapter;
import com.despectra.android.classjournal_new.Fragments.JournalMarksFragment;
import com.despectra.android.classjournal_new.R;
import com.despectra.android.classjournal_new.Utils.AbsListViewsDrawSynchronizer;
import com.despectra.android.classjournal_new.Utils.Utils;
import com.despectra.android.classjournal_new.Views.BottomTabWidget;
import com.despectra.android.classjournal_new.Views.PercentLinearLayout;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dmirty on 17.02.14.
 */
public class JournalActivity extends FragmentActivity implements
        JournalMarksFragment.JournalFragmentCallback, AbsListViewsDrawSynchronizer.Callback, BottomTabWidget.OnTabSelectedListener, AdapterView.OnItemClickListener{

    private static final String TAG = "JOURNAL_ACTIVITY";

    public static final int GROUP = 0;
    public static final int MARKS = 1;

    private PercentLinearLayout mContentLayout;
    private ListView mGroupsList;
    private ListView mStudentsList;
    private ViewPager mPager;
    private BottomTabWidget mTabWidget;
    private JournalPagerAdapter mPagerAdapter;
    private AbsListViewsDrawSynchronizer mListsSync;
    private JournalMarksFragment mCurrentGridFragment;
    private int mFragmentGridPosition;
    private int mFragmentGridOffset;
    private boolean mIsPagerDragging;
    private int mUiState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        mContentLayout = (PercentLinearLayout)findViewById(R.id.journal_content_layout);
        mGroupsList = (ListView)findViewById(R.id.groups_list_view);
        mGroupsList.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                new String[]{
                        "8",
                        "8 A",
                        "9 Б",
                        "10 В",
                        "8",
                        "8 A",
                        "9 Б",
                        "10 В",
                        "8",
                        "8 A",
                        "9 Б",
                        "10 В",
                        "8",
                        "8 A",
                        "9 Б",
                        "10 В",
                        "8",
                        "8 A",
                        "9 Б"
                }
        ));
        mGroupsList.setOnItemClickListener(this);

        mStudentsList = (ListView)findViewById(R.id.students_list_view);
        mStudentsList.setAdapter(new ArrayAdapter<String>(
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
        mTabWidget = (BottomTabWidget) findViewById(R.id.journal_tabs);
        mTabWidget.setTabsList(Arrays.asList(new String[]{"Класс", "Оценки"}));
        mTabWidget.setCurrentTab(1);
        mTabWidget.setOnTabSelectedListener(this);

        mUiState = MARKS;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mContentLayout.setTranslationXByPercent(-30);
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
        updateListsDrawSynchronizer();
    }

    private void updateListsDrawSynchronizer() {
        if (mListsSync != null) {
            mListsSync.setNewViews(mStudentsList, mCurrentGridFragment.getMarksGridView());
        } else {
            mListsSync = new AbsListViewsDrawSynchronizer(this, mStudentsList, mCurrentGridFragment.getMarksGridView());
            mListsSync.setCallback(this);
        }
    }

    @Override
    public void onScrollingStopped(AbsListView view) {
        if (!mIsPagerDragging) {
            mFragmentGridPosition = view.getFirstVisiblePosition();
            mFragmentGridOffset = Utils.getAbsListViewOffset(view);
        }
    }

    @Override
    public void onTabSelected(final int index) {
        if (index != mUiState) {
            mContentLayout.smoothScrollByPercent(
                    (index == GROUP) ? 30 : -30,
                    250,
                    null,
                    new Runnable() {
                        @Override
                        public void run() {
                            mUiState = index;
                        }
                    });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == mGroupsList) {
            mContentLayout.smoothScrollByPercent(
                    -30,
                    250,
                    null,
                    new Runnable() {
                        @Override
                        public void run() {
                            mUiState = MARKS;
                        }
                    });
            mTabWidget.setCurrentTab(1);
        }
    }
}
