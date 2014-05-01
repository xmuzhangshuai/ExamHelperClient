package com.bishe.examhelper.ui;

import java.util.ArrayList;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.GuidePagerAdapter;
import com.bishe.examhelper.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class GuidePagerActivity extends BaseActivity {

	// 翻页控件
	private ViewPager mViewPager;

	// 这5个是底部显示当前状态点imageView
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView mPage3;
	private Button startButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.guide_view);
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
		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);

		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);
		mPage3 = (ImageView) findViewById(R.id.page3);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		/*
		 * 这里是每一页要显示的布局，根据应用需要和特点自由设计显示的内容 以及需要显示多少页等
		 */
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.guide_paper_one, null);
		View view2 = mLi.inflate(R.layout.guide_paper_two, null);
		View view3 = mLi.inflate(R.layout.guide_paper_three, null);
		View view4 = mLi.inflate(R.layout.guide_paper_four, null);
		/*
		 * 这里将每一页显示的view存放到ArrayList集合中 可以在ViewPager适配器中顺序调用展示
		 */
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		/*
		 * 每个页面的Title数据存放到ArrayList集合中 可以在ViewPager适配器中调用展示
		 */
		final ArrayList<String> titles = new ArrayList<String>();
		titles.add("tab1");
		titles.add("tab2");
		titles.add("tab3");
		titles.add("tab4");

		// 填充ViewPager的数据适配器
		GuidePagerAdapter mPagerAdapter = new GuidePagerAdapter(views, titles);
		mViewPager.setAdapter(mPagerAdapter);
		startButton = (Button) view4.findViewById(R.id.guide_start_btn);
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GuidePagerActivity.this, ChangeSubjectActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
				finish();
			}
		});
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int page) {

			// 翻页时当前page,改变当前状态园点图片
			switch (page) {
			case 0:
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 1:
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 2:
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 3:
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

}
