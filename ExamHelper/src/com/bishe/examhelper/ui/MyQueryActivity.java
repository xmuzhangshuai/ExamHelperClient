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
 * 类名称：MyQueryActivity
 * 类描述：我的答疑Activity
 * 创建人： 张帅
 * 创建时间：2014-4-17 上午9:00:07
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

	// 提醒用户网络状况有异常
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

		// 设置Adapter
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
		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		MyQueryActivity.this.registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 卸载广播
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
		// 设置actionBar模式
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setIcon(R.drawable.icon_my_query);
		actionBar.setTitle("我的答疑");

		// viewPager绑定事件
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// 依次添加Tab
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// 当当前Tab选中时，设置ViewPager中相应的Page
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
