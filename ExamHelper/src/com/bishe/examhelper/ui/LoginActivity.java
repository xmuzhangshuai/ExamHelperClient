package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�LoginActivity   
* ��������   ��½Activity
* �����ˣ���˧  
* ����ʱ�䣺2014-3-23 ����2:24:28   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-3-23 ����2:24:28   
* �޸ı�ע��   
* @version    
*    
*/
public class LoginActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewById();
		initView();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginFragment()).commit();
		}
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
		getActionBar().setTitle("��¼");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
