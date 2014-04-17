package com.bishe.examhelper.adapters;

import com.bishe.examhelper.ui.FragmentMyAnswer;
import com.bishe.examhelper.ui.FragmentMyQuery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class QueryPagerAdapter extends FragmentPagerAdapter {

	public QueryPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = null;

		switch (position) {
		case 0:
			fragment = new FragmentMyQuery();
			break;
		case 1:
			fragment = new FragmentMyAnswer();
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
			return "我的疑问";
		case 1:
			return "我的回答";
		}
		return null;
	}
}
