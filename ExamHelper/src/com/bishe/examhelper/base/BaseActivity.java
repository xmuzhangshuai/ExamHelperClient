package com.bishe.examhelper.base;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：BaseActivity   
* 类描述：   自定义的继承了Activity的抽象基类，create时添加到了栈内。
*          定义了项目ActionBar的基本样式
*          自定义了findViewById()、initView()两个抽象方法，子类中必须覆盖实现，确保代码规范
*          自定义了几个快捷打开Activity的方法
*          自定义了ShowDialog方法，在Activity加载完成之前可显示
* 创建人：张帅  
* 创建时间：2014-1-5 上午10:17:18   
* 修改人：张帅   
* 修改时间：2014-1-5 上午10:17:18   
* 修改备注：   
* @version    
*    
*/
public abstract class BaseActivity extends Activity {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public static final String TAG = BaseActivity.class.getSimpleName();

	protected Handler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/**
	 * 设置ActionBar样式
	 */
	@Override
	public ActionBar getActionBar() {
		// TODO Auto-generated method stub
		ActionBar actionBar = super.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar));
		actionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_stacked_bg));
		actionBar.setSplitBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar));
		return actionBar;
	}

	/**
	 * 绑定控件id
	 */
	protected abstract void findViewById();

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 通过类名启动Activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 通过Action启动Activity
	 * 
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * 通过Action启动Activity，并且含有Bundle数据
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	protected void DisPlay(String content) {
		Toast.makeText(this, content, 1).show();
	}

	/**加载进度条**/
	@SuppressLint("ShowToast")
	public Dialog showProgressDialog() {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("请稍候，正在努力加载...");
		dialog.show();
		return dialog;
	}

}
