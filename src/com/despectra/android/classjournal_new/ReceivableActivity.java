package com.despectra.android.classjournal_new;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public abstract class ReceivableActivity extends Activity {
	protected BroadcastReceiver mReceiver;
	
	public void replaceReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		removeReceiver();
		registerReceiver(receiver, filter);
		mReceiver = receiver;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeReceiver();
	}
	
	public void removeReceiver() {
		if(mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}
}
