package com.bishe.examhelper.ui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.ErrorPagerAdapter;
import com.bishe.examhelper.base.BaseFragmentActivity;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：MyErrorActivity   
* 类描述：   错题重放功能主Activity，有三个Tab，分别是按章节显示、按时间显示和按出错了显示错题列表的Fragment
*          以ViewPager为载体呈现。
* 创建人：张帅  
* 创建时间：2014-1-5 上午10:15:56   
* 修改人：张帅   
* 修改时间：2014-1-5 上午10:15:56   
* 修改备注：   
* @version    
*    
*/
public class MyErrorActivity extends BaseFragmentActivity implements ActionBar.TabListener {
	ErrorPagerAdapter mErrorPagerAdapter;
	ViewPager mViewPager;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_error);

		actionBar = getActionBar();

		mErrorPagerAdapter = new ErrorPagerAdapter(getSupportFragmentManager());
		findViewById();

		// 设置Adapter
		mViewPager.setAdapter(mErrorPagerAdapter);
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

	/**
	 * 查找View
	 */
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.error_pager);
	}

	/**
	 * 初始化View
	 */
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// 设置actionBar模式
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle("错题重放");

		// viewPager绑定事件
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// 依次添加Tab
		for (int i = 0; i < mErrorPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mErrorPagerAdapter.getPageTitle(i)).setTabListener(this));
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
