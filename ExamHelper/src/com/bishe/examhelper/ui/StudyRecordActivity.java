package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�StudyRecordActivity   
* ��������   ѧϰ��¼Activity,չʾѧϰ��¼�б��������ʾ����
* �����ˣ���˧  
* ����ʱ�䣺2014-1-27 ����5:34:58   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-27 ����5:34:58   
* �޸ı�ע��   
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
