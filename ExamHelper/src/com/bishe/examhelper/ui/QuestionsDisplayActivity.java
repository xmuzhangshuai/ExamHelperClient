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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�QuestionSDisplayActivity   
* ��������   ����չʾ��Ŀ�ĸ�Ҫ��ͼ�����ҵ��ղء������طŵȹ��ܻ����Ŀ�б���ʾ��Ŀ��ɼ��𰸵ĸ�Ҫ��ͼ��
* �����ˣ���˧  
* ����ʱ�䣺2014-1-6 ����9:47:58   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-6 ����9:47:58   
* �޸ı�ע��   
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
		/*********������ղ��б�************/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals(DefaultValues.BY_COLLECTION)) {
			CollectionDisplayListFragment mFragment = new CollectionDisplayListFragment();
			fragmentTransaction.replace(R.id.display_fragment_container, mFragment);
		}

		/*********����Ǵ����б�************/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals(DefaultValues.BY_ERROR)) {
			ErrorDisplayListFragment mFragment = new ErrorDisplayListFragment();
			fragmentTransaction.replace(R.id.display_fragment_container, mFragment);
		}

		fragmentTransaction.commit();

	}

}
