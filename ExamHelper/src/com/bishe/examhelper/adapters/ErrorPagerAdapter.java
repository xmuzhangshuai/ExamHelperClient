package com.bishe.examhelper.adapters;

import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.ui.ByFrequencyFragment;
import com.bishe.examhelper.ui.BySectionFragment;
import com.bishe.examhelper.ui.ByTimeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�ErrorPagerAdapter   
* ��������   �����ط�Activity��ViewPager��Adapter����������Fragment
* �����ˣ���˧  
* ����ʱ�䣺2014-1-5 ����10:09:26   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-5 ����10:09:26   
* �޸ı�ע��   
* @version    
*    
*/
public class ErrorPagerAdapter extends FragmentPagerAdapter {

	public ErrorPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		Bundle bundle;
		switch (position) {
		case 0:// ���½���ʾ
			fragment = new BySectionFragment();
			bundle = new Bundle();
			bundle.putString(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, DefaultValues.BY_ERROR);
			fragment.setArguments(bundle);
			break;
		case 1:// ��ʱ����ʾ
			fragment = new ByTimeFragment();
			bundle = new Bundle();
			bundle.putString(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, DefaultValues.BY_ERROR);
			fragment.setArguments(bundle);
			break;
		case 2:// ������Ƶ����ʾ
			fragment = new ByFrequencyFragment();
			break;
		default:
			break;
		}

		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return "������ʾ";
		case 1:
			return "ʱ����ʾ";
		case 2:
			return "����Ƶ��";
		}
		return null;
	}
}
