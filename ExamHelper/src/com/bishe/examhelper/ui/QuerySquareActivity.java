package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.AbsListViewBaseActivity;
import com.bishe.examhelper.customerwidget.JazzyViewPager;
import com.bishe.examhelper.customerwidget.JazzyViewPager.TransitionEffect;
import com.bishe.examhelper.utils.DateTimeTools;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.ImageTools;
import com.bishe.examhelper.utils.OutlineContainer;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 类名称：QuerySquareActivity
 * 类描述：答疑广场
 * 创建人： 张帅
 * 创建时间：2014-4-14 下午4:37:21
 *
 */
public class QuerySquareActivity extends AbsListViewBaseActivity {
	private DisplayImageOptions headImageOptions; // DisplayImageOptions是用于设置图片显示的类
	private DisplayImageOptions queryImageOptions; // DisplayImageOptions是用于设置图片显示的类
	private PullToRefreshListView queryListView;// queryListview

	// ============== 广告切换 ===================
	private JazzyViewPager mViewPager = null;

	//装指引的ImageView数组
	private ImageView[] mIndicators;

	// 装ViewPager中ImageView的数组
	private ImageView[] mImageViews;
	private List<String> mImageUrls = new ArrayList<String>();
	private LinearLayout mIndicator = null;
	private String mImageUrl = null;
	private static final int MSG_CHANGE_PHOTO = 1;
	//图片自动切换时间 
	private static final int PHOTO_CHANGE_TIME = 3000;

	// ============== 广告切换 ===================

	//疑问列表Adapter
	private MyQueryListAdapter mQueryListAdapter;
	//网络传回数据，包含Query和User列表
	private LinkedList<Map<String, Object>> netData;
	private int pageNow = 0;//控制页数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_square);

		//设置ImageLoader下载
		setImageLoaderOption();

		/**
		 * 控制广告轮播
		 */
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

		//初始化广告图片路径
		initAdImageURL();

		findViewById();
		initView();

		netData = new LinkedList<Map<String, Object>>();

		new GetDataTask().execute(0);

		mQueryListAdapter = new MyQueryListAdapter();
		queryListView.setMode(Mode.BOTH);
		queryListView.setAdapter(mQueryListAdapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		queryListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.query_square_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_query:
			startActivity(new Intent(QuerySquareActivity.this, QueryPublishActivity.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

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
		getActionBar().setIcon(R.drawable.ic_title_home);

		// ======= 初始化ViewPager ========
		initAdImageViewPager();

		//设置上拉下拉刷新事件
		queryListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				pageNow = 0;
				new GetDataTask().execute(pageNow);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				pageNow++;
				new GetDataTask().execute(pageNow);
			}

		});

		//点击列表项目事件
		queryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(QuerySquareActivity.this, QueryDetailActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

	}

	/**
	 * 初始化图片路径
	 */
	private void initAdImageURL() {
		for (int i = 1; i < 9; i++) {
			mImageUrl = HttpUtil.BASE_URL + "AdImages/image0" + i + ".png";
			mImageUrls.add(mImageUrl);
		}
	}

	/**
	 *  ======= 初始化ViewPager ========
	 */
	private void initAdImageViewPager() {
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

		mViewPager.setAdapter(new AdImageAdapter());
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
	 * 	 使用DisplayImageOptions.Builder()创建DisplayImageOptions
	 */
	public void setImageLoaderOption() {
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		headImageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_hedad_iamge)// 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.photoconor) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.photoconor) // 设置图片加载或解码过程中发生错误显示的图片
				.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片  
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build(); // 创建配置过得DisplayImageOption对象

		queryImageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading)// 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.image_error) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.image_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build(); // 创建配置过得DisplayImageOption对象
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * @author 张帅
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
	 * 类名称：GetDataTask
	 * 类描述：从网络下载疑问数据
	 * 创建人： 张帅
	 * 创建时间：2014-4-13 下午8:25:59
	 *
	 */
	private class GetDataTask extends AsyncTask<Integer, Void, List<Map<String, Object>>> {
		int page = 0;

		@Override
		protected List<Map<String, Object>> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int page = params[0];
			String url = "QueryServlet";
			String type = "getQueryByPage";
			Map<String, String> map = new HashMap<String, String>();

			map.put("pageNow", String.valueOf(page));
			map.put("type", type);
			String data;
			try {
				data = HttpUtil.postRequest(url, map);

				List<Map<String, Object>> temp = FastJsonTool.getObjectMap(data);
				return temp;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("答疑广场", "获取数据出错！");
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			// TODO Auto-generated method stub
			if (result != null) {
				//如果是首次获取数据
				if (page == 0) {
					netData = new LinkedList<Map<String, Object>>();
					netData.addAll(result);
				}
				//如果是获取更多
				else if (page > 0) {
					netData.addAll(result);
				}

				mQueryListAdapter.notifyDataSetChanged();
			}

			// Call onRefreshComplete when the list has been refreshed.
			queryListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * 
	 * 类名称：MyQueryListAdapter
	 * 类描述：疑问列表Adapter
	 * 创建人： 张帅
	 * 创建时间：2014-4-13 下午8:32:35
	 *
	 */
	private class MyQueryListAdapter extends BaseAdapter {
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView queryContent;
			public ImageView headImageView;
			public ImageView contentImage;
			public TextView userNameTextView;
			public TextView timeTextView;
			public TextView locationTextView;
			public Button answerNum;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return netData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(QuerySquareActivity.this).inflate(R.layout.query_square_list_item, null);
				holder = new ViewHolder();
				holder.queryContent = (TextView) view.findViewById(R.id.content);
				holder.headImageView = (ImageView) view.findViewById(R.id.headiamge);
				holder.userNameTextView = (TextView) view.findViewById(R.id.user_nickname);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				holder.locationTextView = (TextView) view.findViewById(R.id.location);
				holder.answerNum = (Button) view.findViewById(R.id.answer_num);
				holder.contentImage = (ImageView) view.findViewById(R.id.content_image);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			//设置用户头像
			imageLoader.displayImage(HttpUtil.BASE_URL + netData.get(position).get("userImage"), holder.headImageView,
					headImageOptions, animateFirstListener);
			//设置用户名
			holder.userNameTextView.setText((CharSequence) netData.get(position).get("username"));
			//设置时间

			//			Timestamp.valueOf((String) netData.get(position).get("queryTime")).getTime();
			holder.timeTextView.setText(DateTimeTools.getInterval(new Date((Long) netData.get(position)
					.get("queryTime"))));
			//设置地
			holder.locationTextView.setText((CharSequence) netData.get(position).get("userLocation"));
			//设置提问内容
			holder.queryContent.setText((CharSequence) netData.get(position).get("queryContent"));

			//设置疑问图片
			if (netData.get(position).get("queryImage") != null) {
				holder.contentImage.setVisibility(View.VISIBLE);
				imageLoader.displayImage(HttpUtil.BASE_URL + netData.get(position).get("queryImage"),
						holder.contentImage, queryImageOptions, animateFirstListener);

			}

			holder.contentImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(QuerySquareActivity.this, "点击了图片！", 1).show();
					Bitmap bitmap = ImageTools.drawableToBitmap(QuerySquareActivity.this.getResources().getDrawable(
							R.drawable.mainbackground2));
					Intent intent = new Intent(QuerySquareActivity.this, ImageShower.class);
					intent.putExtra("imageToLoad", ImageTools.bitmapToBytes(bitmap));
					startActivity(intent);
				}
			});

			holder.answerNum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(QuerySquareActivity.this, QueryDetailActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			return view;
		}
	}

	/** 
	 * 图片加载第一次显示监听器 
	 * @author Administrator 
	 * 
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示  
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果  
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/**
	 * 
	 * 类名称：AdImageAdapter
	 * 类描述：广告轮播Adapter
	 * 创建人： 张帅
	 * 创建时间：2014-4-13 上午10:40:29
	 *
	 */
	public class AdImageAdapter extends PagerAdapter {

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
			imageLoader.displayImage(mImageUrls.get(position), mImageViews[position], queryImageOptions);
			((ViewPager) container).addView(mImageViews[position], 0);
			mViewPager.setObjectForPosition(mImageViews[position], position);
			return mImageViews[position];
		}

	}

}
