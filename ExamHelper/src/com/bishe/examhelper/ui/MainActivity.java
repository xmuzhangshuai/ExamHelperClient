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
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�MainActivity   
 * ��������   ��ҳ��Activity��������centerFragment��LeftFragment��RightFragment����Fragment���Ѿ���ʾ�ĶԻ����ڡ�
 *          �û�����ʹ����ʾ��ҳ�档
 * �����ˣ���˧   
 * ����ʱ�䣺2013-12-3 ����8:44:38   
 * �޸��ˣ���˧     
 * �޸�ʱ�䣺2013-12-15 ����3:38:41   
 * �޸ı�ע��   
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
	 * ��ʼ���һ�����Fragment
	 */
	private void initRightView() {
		// TODO Auto-generated method stub
		mSlidingMenu.setSecondaryMenu(R.layout.main_right_layout);// ��ӵڶ��˵���Ĭ��ֻ��һ��
		FragmentTransaction mFragementTransaction = getFragmentManager().beginTransaction();
		RightFragment mFrag = new RightFragment();// �½�����ҳ���fragment
		mFragementTransaction.replace(R.id.main_right_fragment, mFrag); // ����ұ�Fragment
		mFragementTransaction.commit();
	}

	/**
	 * ��ʼ����ҳ��VIEW������ҳ��VIEW
	 */
	private void initCenterView() {

		android.support.v4.app.FragmentTransaction myfFragmentTransaction2 = getSupportFragmentManager()
				.beginTransaction();

		CenterFragment myCenterFragment = new CenterFragment(); // �½���ҳ�湦�ܿ��fragment
		myfFragmentTransaction2.add(R.id.center_bolck_fragment, myCenterFragment);// �����ҳ��Fragment
		myfFragmentTransaction2.commit();

		// TODO Auto-generated method stub
		((ImageButton) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.ivTitleBtnRigh)).setOnClickListener(this);
		mTitleName = (TextView) findViewById(R.id.ivTitleName);
		mTitleName.setText(getResources().getString(R.string.app_name));

	}

	// ��ʼ���໬�˵�
	private void initSlidingMenu() {
		// TODO Auto-generated method stub
		setBehindContentView(R.layout.main_left_layout);// ������˵�
		android.support.v4.app.FragmentTransaction mFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment mFrag = new LeftFragment();
		mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
		mFragementTransaction.commit();
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// �������󻬻����һ����������Ҷ����Ի������������Ҷ����Ի�
		mSlidingMenu.setShadowWidth(Constants.SCREEN_WIDTH / 40);// ������Ӱ���
		mSlidingMenu.setBehindOffset(Constants.SCREEN_WIDTH / 8);// ���ò˵����
		mSlidingMenu.setFadeDegree(0.35f);// ���õ��뵭���ı���
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);// ������˵���ӰͼƬ
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.right_shadow);// �����Ҳ˵���ӰͼƬ
		mSlidingMenu.setFadeEnabled(true);// ���û���ʱ�˵����Ƿ��뵭��
		mSlidingMenu.setBehindScrollScale(0.333f);// ���û���ʱ��קЧ��
	}

	// �໬��ť
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

	// �������ĵ���Ϣ��
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
			Toast.makeText(source.getContext(), "����˻���", 1).show();
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
