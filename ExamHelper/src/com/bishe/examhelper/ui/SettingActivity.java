package com.bishe.examhelper.ui;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;

public class SettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		RightFragment fragment = new RightFragment();
		fragmentTransaction.add(R.id.setting_container, fragment);
		fragmentTransaction.commit();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("…Ë÷√");
	}

}
