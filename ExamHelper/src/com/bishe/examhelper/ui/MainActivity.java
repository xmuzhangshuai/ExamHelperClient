package com.bishe.examhelper.ui;

import com.bishe.examhelper.R;
import com.bishe.examhelper.config.Constants;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.slidingmenu.BaseSlidingFragmentActivity;
import com.bishe.examhelper.slidingmenu.SlidingMenu;
import com.bishe.examhelper.ui.PersonalModifyDialogFragment.OnUserInfoChangedListener;
import com.bishe.examhelper.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**   
 *    
 * 项目名称：ExamHelper   
 * 类名称：MainActivity   
 * 类描述：   主页面Activity，控制着centerFragment、LeftFragment、RightFragment三个Fragment，已经显示的对话窗口。
 *          用户初次使用显示的页面。
 * 创建人：张帅   
 * 创建时间：2013-12-3 上午8:44:38   
 * 修改人：张帅     
 * 修改时间：2013-12-15 下午3:38:41   
 * 修改备注：   
 * @version    
 *    
 */
public class MainActivity extends BaseSlidingFragmentActivity implements OnClickListener, OnUserInfoChangedListener {

	protected SlidingMenu mSlidingMenu;
	private TextView mTitleName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		initSlidingMenu();
		setContentView(R.layout.activity_main);
		initCenterView();
		initRightView();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	/**
	 * 初始化右滑设置Fragment
	 */
	private void initRightView() {
		// TODO Auto-generated method stub
		mSlidingMenu.setSecondaryMenu(R.layout.main_right_layout);// 添加第二菜单，默认只有一个
		FragmentTransaction mFragementTransaction = getFragmentManager().beginTransaction();
		RightFragment mFrag = new RightFragment();// 新建设置页面的fragment
		mFragementTransaction.replace(R.id.main_right_fragment, mFrag); // 添加右边Fragment
		mFragementTransaction.commit();
	}

	/**
	 * 初始化主页面VIEW和设置页面VIEW
	 */
	private void initCenterView() {

		android.support.v4.app.FragmentTransaction myfFragmentTransaction2 = getSupportFragmentManager()
				.beginTransaction();

		CenterFragment myCenterFragment = new CenterFragment(); // 新建主页面功能块的fragment
		myfFragmentTransaction2.add(R.id.center_bolck_fragment, myCenterFragment);// 添加主页面Fragment
		myfFragmentTransaction2.commit();

		// TODO Auto-generated method stub
		((ImageButton) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ivTitleBtnRigh)).setOnClickListener(this);
		mTitleName = (TextView) findViewById(R.id.ivTitleName);
		mTitleName.setText(getResources().getString(R.string.app_name));

	}

	// 初始化侧滑菜单
	private void initSlidingMenu() {
		// TODO Auto-generated method stub
		setBehindContentView(R.layout.main_left_layout);// 设置左菜单
		android.support.v4.app.FragmentTransaction mFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment mFrag = new LeftFragment();
		mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
		mFragementTransaction.commit();
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 设置是左滑还是右滑，还是左右都可以滑，我这里左右都可以滑
		mSlidingMenu.setShadowWidth(Constants.SCREEN_WIDTH / 40);// 设置阴影宽度
		mSlidingMenu.setBehindOffset(Constants.SCREEN_WIDTH / 8);// 设置菜单宽度
		mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);// 设置左菜单阴影图片
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.right_shadow);// 设置右菜单阴影图片
		mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		mSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
	}

	// 侧滑按钮
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			mSlidingMenu.showMenu(true);
			break;
		case R.id.ivTitleBtnRigh:
			mSlidingMenu.showSecondaryMenu(true);
			break;
		default:
			break;
		}
	}

	// 个人中心的信息区
	@SuppressLint("ShowToast")
	public void clickUser(View source) {
		switch (source.getId()) {
		case R.id.personal:
			PersonalModifyDialogFragment personalModifyDialogFragment = new PersonalModifyDialogFragment();
			FragmentTransaction myFragmentTransaction = getFragmentManager().beginTransaction();
			myFragmentTransaction
					.add(personalModifyDialogFragment, "com.bishe.examhelper.personalModifyDialogFragment");
			myFragmentTransaction.commit();
			break;

		case R.id.security:
			SecurityModifyDialogFragment securityModifyDialogFragment = new SecurityModifyDialogFragment();
			FragmentTransaction myFragmentTransaction2 = getFragmentManager().beginTransaction();
			myFragmentTransaction2.add(securityModifyDialogFragment,
					"com.bishe.examhelper.securityModifyDialogFragment");
			myFragmentTransaction2.commit();
			break;
		case R.id.integral:
			Toast.makeText(source.getContext(), "点击了积分", 1).show();
			break;
		default:
			break;
		}

	}

	@Override
	public void onUserInfoChanged(User user) {
		// TODO Auto-generated method stub
		LeftFragment leftFragment = (LeftFragment) getSupportFragmentManager()
				.findFragmentById(R.id.main_left_fragment);
		leftFragment.onResume();
	}

}
