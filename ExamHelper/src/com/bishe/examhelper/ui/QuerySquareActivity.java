package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.ImageTools;
import com.bishe.examhelper.utils.OutlineContainer;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * �����ƣ�QuerySquareActivity
 * �����������ɹ㳡
 * �����ˣ� ��˧
 * ����ʱ�䣺2014-4-14 ����4:37:21
 *
 */
public class QuerySquareActivity extends AbsListViewBaseActivity {
	private DisplayImageOptions options; // DisplayImageOptions����������ͼƬ��ʾ����
	private PullToRefreshListView queryListView;// queryListview

	// ============== ����л� ===================
	private JazzyViewPager mViewPager = null;

	//װָ����ImageView����
	private ImageView[] mIndicators;

	// װViewPager��ImageView������
	private ImageView[] mImageViews;
	private List<String> mImageUrls = new ArrayList<String>();
	private LinearLayout mIndicator = null;
	private String mImageUrl = null;
	private static final int MSG_CHANGE_PHOTO = 1;
	//ͼƬ�Զ��л�ʱ�� 
	private static final int PHOTO_CHANGE_TIME = 3000;

	// ============== ����л� ===================

	//�����б�
	private LinkedList<String> mQureyListItems;
	//�����б�Adapter
	private MyQueryListAdapter mQueryListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_square);

		//����ImageLoader����
		setImageLoaderOption();

		/**
		 * ���ƹ���ֲ�
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

		//��ʼ�����ͼƬ·��
		initAdImageURL();

		findViewById();
		initView();
		new GetDataTask().execute();

		mQureyListItems = new LinkedList<String>();
		mQueryListAdapter = new MyQueryListAdapter();
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
			startActivity(new Intent(QuerySquareActivity.this, PublishQueryActivity.class));
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
		getActionBar().setTitle("���ɹ㳡");
		getActionBar().setIcon(R.drawable.ic_title_home);

		// ======= ��ʼ��ViewPager ========
		initAdImageViewPager();

		//��������ˢ���¼�
		queryListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});

		//����б���Ŀ�¼�
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
	 * ��ʼ��ͼƬ·��
	 */
	private void initAdImageURL() {
		for (int i = 1; i < 9; i++) {
			mImageUrl = HttpUtil.BASE_URL + "AdImages/image0" + i + ".png";
			mImageUrls.add(mImageUrl);
		}
	}

	/**
	 *  ======= ��ʼ��ViewPager ========
	 */
	private void initAdImageViewPager() {
		// ======= ��ʼ��ViewPager ========
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
	 * 	 ʹ��DisplayImageOptions.Builder()����DisplayImageOptions
	 */
	public void setImageLoaderOption() {
		// ʹ��DisplayImageOptions.Builder()����DisplayImageOptions
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_ad)// ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.default_ad) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.default_ad) // ����ͼƬ���ػ��������з���������ʾ��ͼƬ
				.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc(true) // �������ص�ͼƬ�Ƿ񻺴���SD����
				.build(); // �������ù���DisplayImageOption����
	}

	/**
	 * ��ViewPager��ҳ���״̬�����ı�ʱ����
	 * @author ��˧
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
	 * ����ѡ�е�tip�ı���
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
	 * �����ƣ�GetDataTask
	 * ��������������������������
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014-4-13 ����8:25:59
	 *
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {

				for (int i = 0; i < 10; i++) {
					mQureyListItems.add("" + i);
				}
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mQureyListItems.addFirst("����");

			mQueryListAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			queryListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * 
	 * �����ƣ�MyQueryListAdapter
	 * �������������б�Adapter
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014-4-13 ����8:32:35
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
			return mQureyListItems.size();
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
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			holder.queryContent.setText("1927������ʧ�ܺ󣬵��Ĺ������Ŀ�ʼת��ũ�壬��ũ�彨���������ݵء�ũ��������ݵ��ܹ����й����ڴ��ںͷ�չ�ĸ���ԭ����ʲô��"
					+ mQureyListItems.get(position));
			//			holder.userNameTextView.setText("");

			holder.contentImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(QuerySquareActivity.this, "�����ͼƬ��", 1).show();
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
					Toast.makeText(QuerySquareActivity.this, "�������Ҫ�ش�", 1).show();
				}
			});

			/** 
			* ��ʾͼƬ 
			* ����1��ͼƬURL 
			* ����2����ʾͼƬ�Ŀؼ� 
			* ����3����ʾͼƬ������ 
			* ����4�������� 
			*/
			//						imageLoader.displayImage(HttpUtil.BASE_URL + jUserList.get(position).getAvatar(), holder.headImageView,
			//								options, animateFirstListener);

			return view;
		}
	}

	/** 
	 * ͼƬ���ص�һ����ʾ������ 
	 * @author Administrator 
	 * 
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// �Ƿ��һ����ʾ  
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// ͼƬ����Ч��  
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/**
	 * 
	 * �����ƣ�AdImageAdapter
	 * ������������ֲ�Adapter
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014-4-13 ����10:40:29
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
			imageLoader.displayImage(mImageUrls.get(position), mImageViews[position], options);
			((ViewPager) container).addView(mImageViews[position], 0);
			mViewPager.setObjectForPosition(mImageViews[position], position);
			return mImageViews[position];
		}

	}
}
