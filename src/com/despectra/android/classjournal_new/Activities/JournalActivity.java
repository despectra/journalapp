package com.despectra.android.classjournal_new.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.despectra.android.classjournal_new.Adapters.JournalPagerAdapter;
import com.despectra.android.classjournal_new.Fragments.JournalMarksFragment;
import com.despectra.android.classjournal_new.R;

import java.util.List;

/**
 * Created by Dmirty on 17.02.14.
 */
public class JournalActivity extends FragmentActivity implements JournalMarksFragment.OnMarksGridScrolledListener {

    private static final String TAG = "JOURNAL_ACTIVITY";
    private ListView mGroupList;
    private ViewPager mPager;
    private JournalPagerAdapter mPagerAdapter;
    private int mFragmentGridPosition;
    private int mFragmentGridOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        mGroupList = (ListView)findViewById(R.id.group_list_view);
        mGroupList.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
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
                    List<Fragment> frags = getSupportFragmentManager().getFragments();
                    for (Fragment frag : frags) {
                        if (frag instanceof JournalMarksFragment) {
                            ((JournalMarksFragment) frag).setMarksGridScrolling(mFragmentGridPosition, mFragmentGridOffset);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onMarksGridScrolled(MotionEvent motionEvent, int scrolledPosition, int offset) {
        mGroupList.dispatchTouchEvent(motionEvent);
        mFragmentGridPosition = scrolledPosition;
        mFragmentGridOffset = offset;
    }
}
