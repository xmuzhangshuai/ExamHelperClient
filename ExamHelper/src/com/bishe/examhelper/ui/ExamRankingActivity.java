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
 * 类名称：ExamRankingActivity
 * 类描述：实体排行Activity
 * 创建人： 张帅
 * 创建时间：2014-4-27 下午8:41:47
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

		// 设置Adapter
		mViewPager.setAdapter(examRankingAdapter);

		initView();
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
		actionBar.setTitle("试题排行");
		actionBar.setIcon(R.drawable.function_hot_exams);

		// viewPager绑定事件
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// 依次添加Tab
		for (int i = 0; i < examRankingAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(examRankingAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	/**
	 * 
	 * 类名称：ExamRankingAdapter
	 * 类描述：试题排行Adapter
	 * 创建人： 张帅
	 * 创建时间：2014-4-27 下午8:46:56
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
				return "收藏排行榜";
			case 1:
				return "错题排行榜";
			}
			return null;
		}

	}

}
