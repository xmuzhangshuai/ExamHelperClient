package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：QuestionSDisplayActivity   
* 类描述：   用于展示题目的概要视图，从我的收藏、错题重放等功能获得题目列表，显示题目题干及答案的概要视图。
* 创建人：张帅  
* 创建时间：2014-1-6 下午9:47:58   
* 修改人：张帅   
* 修改时间：2014-1-6 下午9:47:58   
* 修改备注：   
* @version    
*    
*/
public class QuestionsDisplayActivity extends BaseFragmentActivity {
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_display);
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		findViewById();
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

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		/*********如果是收藏列表************/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals(DefaultValues.BY_COLLECTION)) {
			CollectionDisplayListFragment mFragment = new CollectionDisplayListFragment();
			fragmentTransaction.replace(R.id.display_fragment_container, mFragment);
		}

		/*********如果是错题列表************/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals(DefaultValues.BY_ERROR)) {
			ErrorDisplayListFragment mFragment = new ErrorDisplayListFragment();
			fragmentTransaction.replace(R.id.display_fragment_container, mFragment);
		}

		fragmentTransaction.commit();

	}

}
