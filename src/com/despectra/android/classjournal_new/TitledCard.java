package com.despectra.android.classjournal_new;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitledCard extends LinearLayout {

	private TextView mTitleTextView;
	private LinearLayout mSelfLayout;
	
	public TitledCard(Context context) {
		super(context);
	}

	public TitledCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.TitledCard);
		String title = arr.getString(R.styleable.TitledCard_title);
		arr.recycle();

		LayoutInflater.from(context).inflate(R.layout.titled_card, this, true);
		mSelfLayout = (LinearLayout) findViewById(R.id.card_layout);
		mTitleTextView = (TextView) findViewById(R.id.card_title);
		setTitle(title);
	}
	
	public String getTitle() {
		return mTitleTextView.getText().toString();
	}
	
	public void setTitle(String title) {
		mTitleTextView.setText(title);
	}
	
	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if(mSelfLayout != null) {
			mSelfLayout.addView(child, index, params);
		} else {
			super.addView(child, index, params);
		}
	}
	
}
