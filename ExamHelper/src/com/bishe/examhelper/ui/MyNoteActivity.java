package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：MyNoteActivity   
* 类描述：   我的笔记页面，用Fragment承载笔记列表，点击一列之后进入题目详情页面
* 创建人：张帅  
* 创建时间：2014-1-12 下午7:36:38   
* 修改人：张帅   
* 修改时间：2014-1-12 下午7:36:38   
* 修改备注：   
* @version    
*    
*/
public class MyNoteActivity extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_note);

		findViewById();
		initView();

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		Fragment fragment = new MyNoteListFragment();
		fragmentTransaction.add(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
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
		getActionBar().setTitle("我的笔记");
		getActionBar().setIcon(R.drawable.function_mynote);
	}

}
