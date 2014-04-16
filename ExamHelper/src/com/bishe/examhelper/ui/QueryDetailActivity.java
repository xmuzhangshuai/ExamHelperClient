package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.utils.DateTimeTools;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.ImageTools;
import com.jsonobjects.JAnswerQuery;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class QueryDetailActivity extends BaseActivity {
	/***********VIEW************/
	private View queryContentView;
	private View footerView;
	private ImageView headImageView;
	private ImageView contentImageView;
	private TextView userNameTextView;
	private TextView locationTextView;
	private TextView timeTextView;
	private TextView letterNum;
	private TextView queryTextView;
	private EditText myAnswerEditText;
	private ImageView sendMyAnswer;
	private ListView mListView;
	private ImageView noAnswer;

	/**************用户变量**************/
	public static final int NUM = 250;
	List<Map<String, Object>> queryAnswerList;
	QueryAnswerListAdapter mAdapter;
	private int queryID;
	private int userID;
	private String userImage;
	private String userName;
	private String userLocation;
	private String queryContent;
	private String queryTime;
	private String queryImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_detail);

		queryAnswerList = new ArrayList<Map<String, Object>>();

		//从Intent获取数据
		getIntentData();

		findViewById();
		initView();

		mListView.addHeaderView(queryContentView);
		mListView.addFooterView(footerView);
		mAdapter = new QueryAnswerListAdapter();

		new GetDadaTask().execute();
	}

	private void getIntentData() {
		queryID = Integer.parseInt(getIntent().getStringExtra("queryID"));
		userImage = getIntent().getStringExtra("userImage");
		userName = getIntent().getStringExtra("username");
		userLocation = getIntent().getStringExtra("userLocation");
		queryContent = getIntent().getStringExtra("queryContent");
		queryTime = getIntent().getStringExtra("queryTime");
		queryImage = getIntent().getStringExtra("queryImage");
		userID = Integer.parseInt(getIntent().getStringExtra("userID"));
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		queryContentView = getLayoutInflater().inflate(R.layout.query_content_view, null);
		headImageView = (ImageView) queryContentView.findViewById(R.id.headiamge);
		contentImageView = (ImageView) queryContentView.findViewById(R.id.contentImage);
		userNameTextView = (TextView) queryContentView.findViewById(R.id.user_nickname);
		timeTextView = (TextView) queryContentView.findViewById(R.id.time);
		locationTextView = (TextView) queryContentView.findViewById(R.id.location);
		queryTextView = (TextView) queryContentView.findViewById(R.id.content);

		footerView = getLayoutInflater().inflate(R.layout.answer_query_footerview, null);
		noAnswer = (ImageView) footerView.findViewById(R.id.no_answer);

		letterNum = (TextView) findViewById(R.id.letterNum);
		myAnswerEditText = (EditText) findViewById(R.id.myAnswer);
		sendMyAnswer = (ImageView) findViewById(R.id.sendAnswer);
		mListView = (ListView) findViewById(R.id.answerList);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("疑问详情");
		getActionBar().setIcon(R.drawable.ic_title_view_tweet);

		letterNum.setText("" + NUM);
		sendMyAnswer.setEnabled(false);

		//设置编辑框事件
		myAnswerEditText.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
				if (count > 0) {
					sendMyAnswer.setEnabled(true);
				} else {
					sendMyAnswer.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int number = NUM - s.length();
				letterNum.setText("" + number);
				selectionStart = myAnswerEditText.getSelectionStart();
				selectionEnd = myAnswerEditText.getSelectionEnd();
				if (temp.length() > NUM) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					myAnswerEditText.setText(s);
					myAnswerEditText.setSelection(tempSelection);//设置光标在最后
				}
			}
		});

		//设置用户头像
		imageLoader.displayImage(HttpUtil.BASE_URL + userImage, headImageView, ImageTools.getHeadImageOptions(20),
				new AnimateFirstDisplayListener());

		if (queryImage != null) {
			contentImageView.setVisibility(View.VISIBLE);
			imageLoader.displayImage(HttpUtil.BASE_URL + queryImage, contentImageView, ImageTools.getImageOptions(),
					new AnimateFirstDisplayListener());
		} else {
			contentImageView.setVisibility(View.GONE);
		}
		userNameTextView.setText(userName);
		locationTextView.setText(userLocation);
		timeTextView.setText(queryTime);
		queryTextView.setText(queryContent);

		//设置评论事件
		sendMyAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AnswerToNet().execute();
			}
		});
	}

	/**
	 * 
	 * 类名称：GetDadaTask
	 * 类描述：从网络获取数据
	 * 创建人： 张帅
	 * 创建时间：2014-4-14 下午11:18:10
	 *
	 */
	class GetDadaTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String url = "QueryServlet";
			Map<String, String> map = new HashMap<String, String>();
			map.put("queryID", String.valueOf(queryID));
			map.put("type", "getAnswerList");
			try {
				String jsonString = HttpUtil.postRequest(url, map);
				queryAnswerList = FastJsonTool.getObjectMap(jsonString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			mListView.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();

			if (queryAnswerList.size() > 0) {
				noAnswer.setVisibility(View.GONE);
			} else {
				noAnswer.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 
	 * 类名称：AnswerToNet
	 * 类描述：评论异步任务
	 * 创建人： 张帅
	 * 创建时间：2014-4-16 下午11:16:40
	 *
	 */
	class AnswerToNet extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String url = "QueryServlet";
			Map<String, String> map = new HashMap<String, String>();
			JAnswerQuery jAnswerQuery = new JAnswerQuery(null, myAnswerEditText.getText().toString(),
					DateTimeTools.getCurrentDate(), userID, queryID);
			map.put("type", "addAnswer");
			map.put("answerQuery", FastJsonTool.createJsonString(jAnswerQuery));
			try {
				return HttpUtil.postRequest(url, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result.equals("ok")) {
				Toast.makeText(QueryDetailActivity.this, "回答成功", 1).show();
				new GetDadaTask().execute();
			} else {
				Toast.makeText(QueryDetailActivity.this, "网络出现异常", 1).show();
			}

		}
	}

	/**
	 * 
	 * 类名称：QueryAnswerListAdapter
	 * 类描述：回答列表适配器
	 * 创建人： 张帅
	 * 创建时间：2014-4-14 下午11:14:59
	 *
	 */
	class QueryAnswerListAdapter extends BaseAdapter {
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView answerTextView;
			public ImageView headImageView;
			public TextView userNameTextView;
			public TextView timeTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return queryAnswerList.size();
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
				view = LayoutInflater.from(QueryDetailActivity.this).inflate(R.layout.query_answer_list_item, null);
				holder = new ViewHolder();
				holder.answerTextView = (TextView) view.findViewById(R.id.answer);
				holder.headImageView = (ImageView) view.findViewById(R.id.headImage);
				holder.userNameTextView = (TextView) view.findViewById(R.id.userNickname);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			holder.answerTextView.setText((CharSequence) queryAnswerList.get(position).get("answerContent"));
			holder.userNameTextView.setText((CharSequence) queryAnswerList.get(position).get("userName"));
			holder.timeTextView.setText(DateTimeTools.getInterval(new Date((Long) queryAnswerList.get(position).get(
					"answerTime"))));
			imageLoader.displayImage(HttpUtil.BASE_URL + queryAnswerList.get(position).get("headImage"),
					holder.headImageView, ImageTools.getHeadImageOptions(10), animateFirstListener);

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
