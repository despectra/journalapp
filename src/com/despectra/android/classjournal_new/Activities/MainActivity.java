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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.despectra.android.classjournal_new.Adapters.DrawerAdapter;
import com.despectra.android.classjournal_new.Background.BackgroundService;
import com.despectra.android.classjournal_new.Fragments.MainPageFragment;
import com.despectra.android.classjournal_new.R;
import com.despectra.android.classjournal_new.Utils.PrefsManager;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final String[] USER_DATA_PREFS_KEYS = new String[]{"token", "uid", "name", "surname", "middlename", "level", "avatar"};

    public static final int ACTION_EVENTS = 1;
    public static final int ACTION_JOURNAL = 2;
    public static final int ACTION_SCHEDULE = 3;
    public static final int ACTION_GROUPS = 4;
    public static final int ACTION_SETTINGS = 5;
    public static final int ACTION_ABOUT = 6;

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
    private RelativeLayout mDrawerUserItemLayout;
    private ImageView mUserAvatarView;
    private TextView mUserSurnameView;
    private TextView mUserNameView;

    private MainPageFragment mContentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readNewPreferences();
        initDrawer();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

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

        getActionBar().setTitle("События");
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer = (ListView)findViewById(R.id.nav_drawer);
        mDrawerUserItemLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.user_drawer_item, null);
        mDrawer.addHeaderView(mDrawerUserItemLayout);
        mUserAvatarView = (ImageView) mDrawerUserItemLayout.findViewById(R.id.user_avatar);
        mUserSurnameView = (TextView) mDrawerUserItemLayout.findViewById(R.id.user_surname);
        mUserNameView = (TextView) mDrawerUserItemLayout.findViewById(R.id.user_name);
        mDrawer.setAdapter(new DrawerAdapter(
                this,
                new String[]{"События", "Журнал", "Расписание", "Классы", "Настройки", "О программе"},
                R.layout.drawer_item
        ));
        mDrawer.setOnItemClickListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                0,
                0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        loadUserData();
    }

    private void loadUserData() {
        mUserAvatarView.setImageDrawable(getResources().getDrawable(R.drawable.test_ava));
        mUserSurnameView.setText(mSurname);
        mUserNameView.setText(mName);
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

