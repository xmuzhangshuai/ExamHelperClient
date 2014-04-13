package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.AbsListViewBaseActivity;
import com.bishe.examhelper.customerwidget.JazzyViewPager;
import com.bishe.examhelper.customerwidget.JazzyViewPager.TransitionEffect;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.OutlineContainer;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

public class QuerySquareActivity extends AbsListViewBaseActivity {
	private DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private PullToRefreshListView queryListView;// queryListview

	// ============== 广告切换 ===================
	private JazzyViewPager mViewPager = null;
	/**
	 * 装指引的ImageView数组
	 */
	private ImageView[] mIndicators;

	/**
	 * 装ViewPager中ImageView的数组
	 */
	private ImageView[] mImageViews;
	private List<String> mImageUrls = new ArrayList<String>();
	private LinearLayout mIndicator = null;
	private String mImageUrl = null;
	private static final int MSG_CHANGE_PHOTO = 1;
	/** 图片自动切换时间 */
	private static final int PHOTO_CHANGE_TIME = 3000;

	// ============== 广告切换 ===================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_square);

		setImageLoaderOption();

		mHandler = new Handler(getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case MSG_CHANGE_PHOTO:
					int index = mViewPager.getCurrentItem();
					if (index == mImageUrls.size() - 1) {
						index = -1;
					}
					mViewPager.setCurrentItem(index + 1);
					mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
				}
			}

		};

		initData();

		findViewById();
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		applyScrollListener();
	}

	private void applyScrollListener() {
		queryListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mIndicator = (LinearLayout) findViewById(R.id.index_product_images_indicator);
		mViewPager = (JazzyViewPager) findViewById(R.id.index_ad_images_container);
		queryListView = (PullToRefreshListView) findViewById(R.id.queryList);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("答疑广场");

		// ======= 初始化ViewPager ========
		mIndicators = new ImageView[mImageUrls.size()];
		if (mImageUrls.size() <= 1) {
			mIndicator.setVisibility(View.GONE);
		}

		for (int i = 0; i < mIndicators.length; i++) {
			ImageView imageView = new ImageView(this);
			LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
			if (i != 0) {
				params.leftMargin = 5;
			}
			imageView.setLayoutParams(params);
			mIndicators[i] = imageView;
			if (i == 0) {
				mIndicators[i].setBackgroundResource(R.drawable.android_activities_cur);
			} else {
				mIndicators[i].setBackgroundResource(R.drawable.android_activities_bg);
			}

			mIndicator.addView(imageView);
		}

		mImageViews = new ImageView[mImageUrls.size()];

		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImageViews[i] = imageView;
		}
		mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
		mViewPager.setCurrentItem(0);
		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);

		mViewPager.setAdapter(new MyAdapter());
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (mImageUrls.size() == 0 || mImageUrls.size() == 1)
					return true;
				else
					return false;
			}
		});
	}

	/**
	 * 初始化图片路径
	 */
	private void initData() {
		for (int i = 1; i < 9; i++) {
			mImageUrl = HttpUtil.BASE_URL + "AdImages/image0" + i + ".png";
			mImageUrls.add(mImageUrl);
		}
	}

	/**
	 * 	 使用DisplayImageOptions.Builder()创建DisplayImageOptions
	 */
	public void setImageLoaderOption() {
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_ad)// 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_ad) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_ad) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build(); // 创建配置过得DisplayImageOption对象
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			setImageBackground(position);
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
		for (int i = 0; i < mIndicators.length; i++) {
			if (i == selectItemsIndex) {
				mIndicators[i].setBackgroundResource(R.drawable.android_activities_cur);
			} else {
				mIndicators[i].setBackgroundResource(R.drawable.android_activities_bg);
			}
		}
	}

	/**
	 * 
	 * 类名称：MyAdapter
	 * 类描述：
	 * 创建人： 张帅
	 * 创建时间：2014-4-13 上午10:40:29
	 *
	 */
	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mViewPager.findViewFromObject(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			imageLoader.displayImage(mImageUrls.get(position), mImageViews[position], options);
			((ViewPager) container).addView(mImageViews[position], 0);
			mViewPager.setObjectForPosition(mImageViews[position], position);
			return mImageViews[position];
		}

	}
}
