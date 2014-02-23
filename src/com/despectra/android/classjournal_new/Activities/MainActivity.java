package com.despectra.android.classjournal_new.Activities;

/**
 * Created by Dmirty on 17.02.14.
 */

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.despectra.android.classjournal_new.Adapters.DrawerAdapter;
import com.despectra.android.classjournal_new.Background.BackgroundService;
import com.despectra.android.classjournal_new.Fragments.MainPageFragment;
import com.despectra.android.classjournal_new.R;
import com.despectra.android.classjournal_new.Utils.PrefsManager;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final String[] USER_DATA_PREFS_KEYS = new String[]{"token", "uid", "name", "surname", "middlename", "level", "avatar"};

    public static final int ACTION_JOURNAL = 3;
    public static final int ACTION_SCHEDULE = 4;
    public static final int ACTION_GROUPS = 5;

    private String mToken;
    private int mUserId;
    private String mName;
    private String mSurname;
    private String mMiddlename;
    private String mLevel;
    private String mAvatar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawer;
    private MainPageFragment mContentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer = (ListView)findViewById(R.id.nav_drawer);
        mDrawer.setAdapter(new DrawerAdapter(
                this,
                new String[]{"Главное", "Домашняя страница", "Категории", "Журнал", "Расписание", "Классы", "Другое", "Настройки", "О программе"},
                new int[]{0, 1, 0, 1, 1, 1, 0, 1, 1},
                android.R.layout.simple_list_item_1,
                R.layout.simple_list_header_1
        ));
        mDrawer.setOnItemClickListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                0,
                0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        readNewPreferences();
        loadAvatar();

        mContentFragment = new MainPageFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout, mContentFragment);
        ft.commit();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_logout:
                PrefsManager.clearPreferences(this, new String[]{"token"});

                Intent serviceIntent = new Intent(this, BackgroundService.class);
                serviceIntent.setAction(BackgroundService.ACTION_LOGOUT);
                serviceIntent.putExtra("token", mToken);
                startService(serviceIntent);

                Intent activityIntent = new Intent(this, AuthActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(activityIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readNewPreferences() {
        mToken = "OMG";
        mUserId = 1;
        mName = "Dmitry";
        mSurname = "Shvedchikov";
        mMiddlename = "Igorevich";
        mLevel = "4096";
        mAvatar = "404";

        getActionBar().setTitle(mSurname);
        getActionBar().setSubtitle(String.format("%s %s", mName, mMiddlename));
    }

    private void loadAvatar() {
        getActionBar().setLogo(R.drawable.test_ava);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch(position){
            case ACTION_JOURNAL:
                Intent intent = new Intent(this, JournalActivity.class);
                startActivity(intent);
                break;
        }
    }
}

