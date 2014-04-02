package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：StudyRecordActivity   
* 类描述：   学习记录Activity,展示学习记录列表，点击后显示详情
* 创建人：张帅  
* 创建时间：2014-1-27 下午5:34:58   
* 修改人：张帅   
* 修改时间：2014-1-27 下午5:34:58   
* 修改备注：   
* @version    
*    
*/
public class StudyRecordActivity extends BaseFragmentActivity {
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
		StudyRecordDisplayFragment studyRecordDisplayFragment = new StudyRecordDisplayFragment();
		fragmentTransaction.replace(R.id.display_fragment_container, studyRecordDisplayFragment);
		fragmentTransaction.commit();
	}

}
