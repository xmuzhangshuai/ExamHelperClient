package com.bishe.examhelper.ui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.CollectionPagerAdapter;
import com.bishe.examhelper.base.BaseFragmentActivity;

public class MyCollectionActivity extends BaseFragmentActivity implements ActionBar.TabListener {
	CollectionPagerAdapter mCollectionPagerAdapter;
	ViewPager mViewPager;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		actionBar = getActionBar();

		mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
		findViewById();

		// 设置Adapter
		mViewPager.setAdapter(mCollectionPagerAdapter);

		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.collection_pager);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// 设置actionBar模式
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle("我的收藏");

		// viewPager绑定事件
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// 依次添加Tab
		for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mCollectionPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

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

}
