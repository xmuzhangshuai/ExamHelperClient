package com.bishe.examhelper.adapters;

import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.ui.BySectionFragment;
import com.bishe.examhelper.ui.ByTimeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CollectionPagerAdapter extends FragmentPagerAdapter {

	public CollectionPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		Bundle bundle;
		switch (position) {
		case 0:
			fragment = new BySectionFragment();
			bundle = new Bundle();
			bundle.putString(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, DefaultValues.BY_COLLECTION);
			fragment.setArguments(bundle);
			break;
		case 1:
			fragment = new ByTimeFragment();
			bundle = new Bundle();
			bundle.putString(DefaultKeys.BY_SECTION_FRAGMENT_TYPE,DefaultValues.BY_COLLECTION);
			fragment.setArguments(bundle);
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
			return "按章节显示";
		case 1:
			return "按时间显示";
		}
		return null;
	}
}
