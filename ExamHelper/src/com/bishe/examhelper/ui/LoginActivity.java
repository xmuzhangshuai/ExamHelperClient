package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：LoginActivity   
* 类描述：   登陆Activity
* 创建人：张帅  
* 创建时间：2014-3-23 下午2:24:28   
* 修改人：张帅   
* 修改时间：2014-3-23 下午2:24:28   
* 修改备注：   
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
		getActionBar().setTitle("登录");
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
