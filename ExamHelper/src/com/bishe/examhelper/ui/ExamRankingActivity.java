package com.bishe.examhelper.ui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;

/**
 * �����ƣ�ExamRankingActivity
 * ��������ʵ������Activity
 * �����ˣ� ��˧
 * ����ʱ�䣺2014-4-27 ����8:41:47
 *
 */
public class ExamRankingActivity extends BaseFragmentActivity implements TabListener {
	ExamRankingAdapter examRankingAdapter;
	ViewPager mViewPager;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		actionBar = getActionBar();

		examRankingAdapter = new ExamRankingAdapter(getSupportFragmentManager());
		findViewById();

		// ����Adapter
		mViewPager.setAdapter(examRankingAdapter);

		initView();
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
		actionBar.setTitle("��������");
		actionBar.setIcon(R.drawable.function_hot_exams);

		// viewPager���¼�
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// �������Tab
		for (int i = 0; i < examRankingAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(examRankingAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	/**
	 * 
	 * �����ƣ�ExamRankingAdapter
	 * ����������������Adapter
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014-4-27 ����8:46:56
	 *
	 */
	class ExamRankingAdapter extends FragmentPagerAdapter {

		public ExamRankingAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new CollectionRankingFragment();
				break;
			case 1:
				fragment = new ErrorRankingFragment();
				break;
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "�ղ����а�";
			case 1:
				return "�������а�";
			}
			return null;
		}

	}

}
