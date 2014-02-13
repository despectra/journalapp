package com.despectra.android.classjournal_new;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CurrentScheduleAdapter extends BaseAdapter {

	public static final int STATUS_PAST = 0;
	public static final int STATUS_CURRENT = 1;
	public static final int STATUS_FUTURE = 0;
	
	private Model[] mData;
	
	public CurrentScheduleAdapter(Context context, Model[] data) {
		mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.length;
	}

	@Override
	public Object getItem(int position) {
		return mData[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//TODO complete this adapter after compositing well looking layout
		if(convertView == null) {
			
		}
		
		
		return null;
	}
	
	private static class ViewHolder {
		public TextView mStatusTextView;
		public TextView mGroupTextView;
		public TextView mSubjectTextView;
		public View mGroupColorIcon;
		public View mSubjectColorIcon;
	}
	
	public static class Model {
		public int status;
		public String group;
		public String subject;
		public String timeStart;
		public String timeEnd;
		public int groupColor;
		public int subjectColor;
	}

}
