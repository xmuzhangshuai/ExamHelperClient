package com.bishe.examhelper.ui;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bishe.examhelper.R;
import com.bishe.examhelper.config.Constants;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.service.UserService;
import com.bishe.examhelper.slidingmenu.BaseSlidingFragmentActivity;
import com.bishe.examhelper.slidingmenu.SlidingMenu;
import com.bishe.examhelper.ui.PersonalModifyDialogFragment.OnUserInfoChangedListener;
import com.bishe.examhelper.ui.RightFragment.OnSignOutPressedListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

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
public class MainActivity extends BaseSlidingFragmentActivity implements OnClickListener, OnUserInfoChangedListener,
		OnSignOutPressedListener {

	protected SlidingMenu mSlidingMenu;
	private TextView mTitleName;
	private String loc = null; // 保存定位信息
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public static SharedPreferences locationPreferences;// 记录用户位置
	SharedPreferences.Editor locationEditor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		UmengUpdateAgent.update(this);
		PushAgent.getInstance(this).onAppStart();

		initSlidingMenu();
		setContentView(R.layout.activity_main);
		initCenterView();
		initRightView();

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开GPS
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5 * 60 * 60 * 1000);// 设置发起定位请求的间隔时间为3000ms
		option.disableCache(false);// 禁止启用缓存定位
		option.setPriority(LocationClientOption.NetWorkFirst);// 网络定位优先
		mLocationClient.setLocOption(option);// 使用设置
		mLocationClient.start();// 开启定位SDK
		mLocationClient.requestLocation();// 开始请求位置

		locationPreferences = getSharedPreferences("location", Context.MODE_PRIVATE);
		locationEditor = locationPreferences.edit();
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

	@Override
	public void onDestroy() {
		stopListener();// 停止监听
		super.onDestroy();
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location != null) {
				String detailLoc = null;// 精确位置
				StringBuffer sb = new StringBuffer(128);// 接受服务返回的缓冲区
				sb.append(location.getProvince());
				sb.append(location.getCity());// 获得城市
				loc = sb.toString().trim();
				detailLoc = location.getAddrStr();
				final UserService userService = UserService.getInstance(MainActivity.this);
				User user = userService.getCurrentUser();
				locationEditor.putString(DefaultKeys.PREF_LOCATION, loc);
				locationEditor.putString(DefaultKeys.PREF_DETAIL_LOCATION, detailLoc);
				locationEditor.commit();
				if (user != null) {
					user.setArea(detailLoc);
					userService.updateUser(user);
					new Thread() {
						public void run() {
							userService.updateUserToNet();
						};
					}.start();
				}

			} else {
				return;
			}
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 停止，减少资源消耗
	 */
	public void stopListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();// 关闭定位SDK
			mLocationClient = null;
		}
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
		mTitleName.setText("考研政治");

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

	/**
	 * 当按了注销按钮之后
	 */
	@Override
	public void onSignOutPressed() {
		// TODO Auto-generated method stub
		getSupportFragmentManager().findFragmentById(R.id.main_left_fragment).onResume();
	}

}
