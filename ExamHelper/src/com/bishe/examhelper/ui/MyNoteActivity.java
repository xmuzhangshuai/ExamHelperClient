package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�MyNoteActivity   
* ��������   �ҵıʼ�ҳ�棬��Fragment���رʼ��б����һ��֮�������Ŀ����ҳ��
* �����ˣ���˧  
* ����ʱ�䣺2014-1-12 ����7:36:38   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-12 ����7:36:38   
* �޸ı�ע��   
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
		getActionBar().setTitle("�ҵıʼ�");
		getActionBar().setIcon(R.drawable.function_mynote);
	}

}
