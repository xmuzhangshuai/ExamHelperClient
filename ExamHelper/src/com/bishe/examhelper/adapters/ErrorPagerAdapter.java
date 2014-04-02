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
* 项目名称：ExamHelper   
* 类名称：ErrorPagerAdapter   
* 类描述：   错题重放Activity中ViewPager的Adapter，返回三个Fragment
* 创建人：张帅  
* 创建时间：2014-1-5 上午10:09:26   
* 修改人：张帅   
* 修改时间：2014-1-5 上午10:09:26   
* 修改备注：   
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
		case 0:// 按章节显示
			fragment = new BySectionFragment();
			bundle = new Bundle();
			bundle.putString(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, DefaultValues.BY_ERROR);
			fragment.setArguments(bundle);
			break;
		case 1:// 按时间显示
			fragment = new ByTimeFragment();
			bundle = new Bundle();
			bundle.putString(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, DefaultValues.BY_ERROR);
			fragment.setArguments(bundle);
			break;
		case 2:// 按出错频率显示
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
			return "分类显示";
		case 1:
			return "时间显示";
		case 2:
			return "出错频率";
		}
		return null;
	}
}
