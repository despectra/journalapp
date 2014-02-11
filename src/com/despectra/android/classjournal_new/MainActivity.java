package com.despectra.android.classjournal_new;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String[] USER_DATA_PREFS_KEYS = new String[]{"token", "uid", "name", "surname", "middlename", "level", "avatar"};
	
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_main);
		
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawer = (ListView)findViewById(R.id.nav_drawer);
		mDrawer.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				0,
				0);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		readNewPreferences();
		try {
			loadAvatar();
		} catch (FileNotFoundException e) {
			Toast.makeText(this, "Avatar not found", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_logout:
			deleteFile(BackgroundService.AVATAR_FILENAME);
			PreferencesManager.clearPreferences(this, USER_DATA_PREFS_KEYS);
			Intent intent = new Intent(this, AuthActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
		return true;
	}
	
	private void readNewPreferences() {
		SharedPreferences prefs = getSharedPreferences(PreferencesManager.USER_DATA_PREFS, MODE_PRIVATE);
		mToken = prefs.getString("token", "");
		mUserId = Integer.valueOf(prefs.getString("uid", ""));
		mName = prefs.getString("name", "");
		mSurname = prefs.getString("surname", "");
		mMiddlename = prefs.getString("middlename", "");
		mLevel = prefs.getString("level", "");
		mAvatar = prefs.getString("avatar", "");
		
		getActionBar().setTitle(mSurname);
		getActionBar().setSubtitle(String.format("%s %s", mName, mMiddlename));		
	}
	
	private void loadAvatar() throws FileNotFoundException {
		Bitmap ava = BitmapFactory.decodeStream(openFileInput(BackgroundService.AVATAR_FILENAME));
		getActionBar().setLogo(new BitmapDrawable(ava));
		
	}
}
