package com.bishe.examhelper.ui;

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
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.service.UserService;
import com.bishe.examhelper.utils.DateTimeTools;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.ImageTools;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 类名称：FragmentMyQuery
 * 类描述：我的疑问
 * 创建人： 张帅
 * 创建时间：2014-4-17 上午9:39:16
 *
 */
public class FragmentMyQuery extends BaseV4Fragment {
	private PullToRefreshListView queryListView;// queryListview
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	//疑问列表Adapter
	private MyQueryListAdapter mQueryListAdapter;
	//网络传回数据，包含Query和User列表
	private LinkedList<Map<String, Object>> netData;
	private int pageNow = 0;//控制页数
	private View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		netData = new LinkedList<Map<String, Object>>();
		mQueryListAdapter = new MyQueryListAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity_query_square, container, false);

		findViewById();
		//设置上拉下拉
		queryListView.setMode(Mode.BOTH);
		initView();

		queryListView.setAdapter(mQueryListAdapter);

		new GetDataTask().execute(0);

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		queryListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, false, true));
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		queryListView = (PullToRefreshListView) rootView.findViewById(R.id.queryList);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		//设置上拉下拉刷新事件
		queryListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				pageNow = 0;
				new GetDataTask().execute(pageNow);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				if (pageNow >= 0)
					++pageNow;
				new GetDataTask().execute(pageNow);
			}
		});
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
			page = params[0];
			String url = "QueryServlet";
			String type = "myQueryList";
			Map<String, String> map = new HashMap<String, String>();

			map.put("pageNow", String.valueOf(page));
			map.put("type", type);
			map.put("userID", String.valueOf(UserService.getInstance(getActivity()).getCurrentUserID()));
			String data;
			try {
				data = HttpUtil.postRequest(url, map);

				List<Map<String, Object>> temp = FastJsonTool.getObjectMap(data);
				return temp;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			// TODO Auto-generated method stub
			if (result != null) {
				//如果是首次获取数据
				if (page == 0) {
					if (result.size() < 10) {
						pageNow = -1;
					}
					netData = new LinkedList<Map<String, Object>>();
					netData.addAll(result);
				}
				//如果是获取更多
				else if (page > 0) {
					if (result.size() < 10) {
						pageNow = -1;
					}
					netData.addAll(result);
				}

				mQueryListAdapter.notifyDataSetChanged();
			}

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
			public TextView answerNum;
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.query_square_list_item, null);
				holder = new ViewHolder();
				holder.queryContent = (TextView) view.findViewById(R.id.content);
				holder.headImageView = (ImageView) view.findViewById(R.id.headiamge);
				holder.userNameTextView = (TextView) view.findViewById(R.id.user_nickname);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				holder.locationTextView = (TextView) view.findViewById(R.id.location);
				holder.answerNum = (TextView) view.findViewById(R.id.answer_num);
				holder.contentImage = (ImageView) view.findViewById(R.id.content_image);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), QueryDetailActivity.class);
					intent.putExtra("queryID", String.valueOf(netData.get(position).get("queryId")));
					intent.putExtra("userImage", String.valueOf(netData.get(position).get("userImage")));
					intent.putExtra("username", String.valueOf(netData.get(position).get("username")));
					intent.putExtra("userLocation", String.valueOf(netData.get(position).get("userLocation")));
					intent.putExtra("queryContent", String.valueOf(netData.get(position).get("queryContent")));
					intent.putExtra("userID", String.valueOf(netData.get(position).get("userID")));
					intent.putExtra("queryTime",
							DateTimeTools.getInterval(new Date((Long) netData.get(position).get("queryTime"))));
					intent.putExtra("queryImage", (String) netData.get(position).get("queryImage"));
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			//设置用户头像
			imageLoader.displayImage(HttpUtil.BASE_URL + netData.get(position).get("userImage"), holder.headImageView,
					ImageTools.getHeadImageOptions(10), animateFirstListener);
			//设置用户名
			holder.userNameTextView.setText((CharSequence) netData.get(position).get("username"));
			//设置时间
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
						holder.contentImage, ImageTools.getImageOptions(), animateFirstListener);
			} else {
				holder.contentImage.setVisibility(View.GONE);
			}

			holder.contentImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bitmap bitmap = ImageTools.drawableToBitmap(holder.contentImage.getDrawable());
					Intent intent = new Intent(getActivity(), ImageShower.class);
					intent.putExtra("imageToLoad", ImageTools.bitmapToBytes(bitmap));
					startActivity(intent);
					bitmap.recycle();
				}
			});

			holder.answerNum.setText(String.valueOf(netData.get(position).get("queryAnswerNum")) + "条回答");

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
}
