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
 * 项目名称：ExamHelper   
 * 类名称：GuideActivity   
 * 类描述：   首次安装运行的activity，欢迎页面。并且在这其间将数据库拷贝到手机存储中。
 * 创建人：张帅   
 * 创建时间：2013-12-21 上午9:58:08   
 * 修改人：张帅   
 * 修改时间：2013-12-26 上午10:00:04   
 * 修改备注：   
 * @version    
 *    
 */
public class GuideActivity extends BaseActivity {

	public static SharedPreferences countPreferences;// 记录软件使用次数
	private ImageView loadingImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);

		// 设置全屏显示
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 获取启动的次数
		countPreferences = getSharedPreferences("start_count", 0);
		int count = countPreferences.getInt("count", 0);
		SharedPreferences.Editor localEditor = countPreferences.edit();

		if (count == 0) {// 如果是第一次登陆，则启动向导页面
			startActivity(new Intent(GuideActivity.this, GuidePagerActivity.class));

			// 第一次运行拷贝数据库文件并对数据进行分组
			new initDataBase().execute();
			initDeviceInfo();// 初始化设备信息

			localEditor.putInt("count", ++count);// 次数加1
			localEditor.commit();
			finish();
		} else {// 如果不是第一次使用,则不启动向导页面，显示欢迎页面。
			setContentView(R.layout.welcome);
			initDeviceInfo();// 初始化设备信息
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
	* 项目名称：ExamHelper   
	* 类名称：initDataBase   
	* 类描述：   异步任务，拷贝完成数据库后，对数据进行分组
	* 创建人：张帅  
	* 创建时间：2014-1-2 下午10:27:32   
	*
	 */
	public class initDataBase extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			new CopyDataBase(GuideActivity.this).copyDataBase();// 拷贝数据库
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			new DivideIntoGroup(GuideActivity.this).divideIntoGroup();// 对数据分组
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
				// 启动MainActivity，相当于Intent
				openActivity(MainActivity.class);
				overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
				GuideActivity.this.finish();
			}
		});
		loadingImage.setAnimation(translate);
	}

	/**
	 * 初始化设备信息
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
