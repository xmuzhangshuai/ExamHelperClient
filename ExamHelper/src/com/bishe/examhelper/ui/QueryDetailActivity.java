package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;

public class QueryDetailActivity extends BaseActivity {
	private ImageView headImageView;
	private ImageView contentImageView;
	private TextView userNameTextView;
	private TextView timeTextView;
	private TextView locationTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_detail);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		headImageView = (ImageView) findViewById(R.id.headiamge);
		contentImageView = (ImageView) findViewById(R.id.contentImage);
		userNameTextView = (TextView) findViewById(R.id.user_nickname);
		timeTextView = (TextView) findViewById(R.id.time);
		locationTextView = (TextView) findViewById(R.id.location);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("“…Œ œÍ«È");
		getActionBar().setIcon(R.drawable.ic_title_view_tweet);
	}
}
