package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.webkit.WebView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;

public class ExamGuideWebActivity extends BaseActivity {
	WebView webView;
	String url;
	String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		url = getIntent().getStringExtra("examGuideUrl");
		title = getIntent().getStringExtra("title");

		findViewById();
		initView();

		webView.loadUrl(url);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.web_container);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle(title);
		getActionBar().setIcon(R.drawable.ic_action_web_site);

	}

}
