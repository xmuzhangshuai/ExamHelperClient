package com.bishe.examhelper.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.config.Constants;
import com.bishe.examhelper.dbService.CopyDataBase;
import com.bishe.examhelper.dbService.DivideIntoGroup;
import com.umeng.analytics.MobclickAgent;

/**   
 *    
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�GuideActivity   
 * ��������   �״ΰ�װ���е�activity����ӭҳ�档����������佫���ݿ⿽�����ֻ��洢�С�
 * �����ˣ���˧   
 * ����ʱ�䣺2013-12-21 ����9:58:08   
 * �޸��ˣ���˧   
 * �޸�ʱ�䣺2013-12-26 ����10:00:04   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class GuideActivity extends BaseActivity {

	public static SharedPreferences countPreferences;// ��¼���ʹ�ô���
	private ImageView loadingImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// ���Ͳ��Զ������û���ͳ�Ʒ���SDK���������ݷ��ͻ����˷�������Ƶ�ʡ�
		MobclickAgent.updateOnlineConfig(this);

		// ����ȫ����ʾ
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// ��ȡ�����Ĵ���
		countPreferences = getSharedPreferences("start_count", 0);
		int count = countPreferences.getInt("count", 0);
		SharedPreferences.Editor localEditor = countPreferences.edit();

		if (count == 0) {// ����ǵ�һ�ε�½����������ҳ��
			startActivity(new Intent(GuideActivity.this, GuidePagerActivity.class));

			// ��һ�����п������ݿ��ļ��������ݽ��з���
			new initDataBase().execute();
			initDeviceInfo();// ��ʼ���豸��Ϣ

			localEditor.putInt("count", ++count);// ������1
			localEditor.commit();
			finish();
		} else {// ������ǵ�һ��ʹ��,��������ҳ�棬��ʾ��ӭҳ�档
			setContentView(R.layout.welcome);
			initDeviceInfo();// ��ʼ���豸��Ϣ
			mHandler = new Handler(getMainLooper());
			findViewById();
			initView();
			localEditor.putInt("count", ++count);
			localEditor.commit();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�initDataBase   
	* ��������   �첽���񣬿���������ݿ�󣬶����ݽ��з���
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-2 ����10:27:32   
	*
	 */
	public class initDataBase extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			new CopyDataBase(GuideActivity.this).copyDataBase();// �������ݿ�
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			new DivideIntoGroup(GuideActivity.this).divideIntoGroup();// �����ݷ���
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		loadingImage = (ImageView) findViewById(R.id.loading_item);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		Animation translate = AnimationUtils.loadAnimation(this, R.anim.splash_loading);
		translate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// ����MainActivity���൱��Intent
				openActivity(MainActivity.class);
				overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
				GuideActivity.this.finish();
			}
		});
		loadingImage.setAnimation(translate);
	}

	/**
	 * ��ʼ���豸��Ϣ
	 */
	private void initDeviceInfo() {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constants.SCREEN_DENSITY = dm.density;
		Constants.SCREEN_HEIGHT = dm.heightPixels;
		Constants.SCREEN_WIDTH = dm.widthPixels;
	}

}
