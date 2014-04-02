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

		// ����Adapter
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
		// ����actionBarģʽ
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle("�ҵ��ղ�");

		// viewPager���¼�
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// �������Tab
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
		// ����ǰTabѡ��ʱ������ViewPager����Ӧ��Page
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
