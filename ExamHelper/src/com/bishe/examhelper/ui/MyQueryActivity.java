package com.bishe.examhelper.ui;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.QueryPagerAdapter;
import com.bishe.examhelper.base.BaseFragmentActivity;
import com.bishe.examhelper.utils.NetworkUtils;

/**
 * �����ƣ�MyQueryActivity
 * ���������ҵĴ���Activity
 * �����ˣ� ��˧
 * ����ʱ�䣺2014-4-17 ����9:00:07
 *
 */
public class MyQueryActivity extends BaseFragmentActivity implements TabListener {

	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

	// protected AbsListView listView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;

	ViewPager mViewPager;
	ActionBar actionBar;
	QueryPagerAdapter mPagerAdapter;

	// �����û�����״�����쳣
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (!NetworkUtils.isNetworkAvailable(MyQueryActivity.this)) {
				NetworkUtils.networkStateTips(MyQueryActivity.this);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myquery);
		actionBar = getActionBar();

		mPagerAdapter = new QueryPagerAdapter(getSupportFragmentManager());

		findViewById();
		initView();

		// ����Adapter
		mViewPager.setAdapter(mPagerAdapter);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
		pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ע��㲥
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		MyQueryActivity.this.registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// ж�ع㲥
		if (broadcastReceiver != null) {
			MyQueryActivity.this.unregisterReceiver(broadcastReceiver);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
		outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.myquery_pager);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// ����actionBarģʽ
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setIcon(R.drawable.icon_my_query);
		actionBar.setTitle("�ҵĴ���");

		// viewPager���¼�
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// �������Tab
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// ����ǰTabѡ��ʱ������ViewPager����Ӧ��Page
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
