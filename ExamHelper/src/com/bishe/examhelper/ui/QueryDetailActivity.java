package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class QueryDetailActivity extends BaseActivity {
	/***********VIEW************/
	private ImageView headImageView;
	private ImageView contentImageView;
	private TextView userNameTextView;
	private TextView locationTextView;
	private TextView timeTextView;
	private TextView letterNum;
	private EditText myAnswerEditText;
	private ImageView sendMyAnswer;
	private ListView mListView;

	/**************�û�����**************/
	public static final int NUM = 250;
	List<String> queryAnswerList;
	QueryAnswerListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_detail);

		queryAnswerList = new ArrayList<String>();

		findViewById();
		initView();

		new GetDadaTask().execute();

		mListView.addHeaderView(getLayoutInflater().inflate(R.layout.query_content_view, null));
		mAdapter = new QueryAnswerListAdapter();
		mListView.setAdapter(mAdapter);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		headImageView = (ImageView) findViewById(R.id.headiamge);
		contentImageView = (ImageView) findViewById(R.id.contentImage);
		userNameTextView = (TextView) findViewById(R.id.user_nickname);
		timeTextView = (TextView) findViewById(R.id.time);
		locationTextView = (TextView) findViewById(R.id.location);
		letterNum = (TextView) findViewById(R.id.letterNum);
		myAnswerEditText = (EditText) findViewById(R.id.myAnswer);
		sendMyAnswer = (ImageView) findViewById(R.id.sendAnswer);
		mListView = (ListView) findViewById(R.id.answerList);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("��������");
		getActionBar().setIcon(R.drawable.ic_title_view_tweet);

		letterNum.setText("" + NUM);
		sendMyAnswer.setEnabled(false);

		//���ñ༭���¼�
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
					myAnswerEditText.setSelection(tempSelection);//���ù�������
				}
			}
		});
	}

	/**
	 * 
	 * �����ƣ�GetDadaTask
	 * ���������������ȡ����
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014-4-14 ����11:18:10
	 *
	 */
	class GetDadaTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			for (int i = 0; i < 20; i++) {
				queryAnswerList.add("��ô�����ܲغ�һ���ˣ���������ʱ��ʱ���ܳ��������ﵷ�ң���ô�����ܲغ�һ���ˣ���������ʱ��ʱ���ܳ��������ﵷ�ң�" + i);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			mAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

	/**
	 * 
	 * �����ƣ�QueryAnswerListAdapter
	 * ���������ش��б�������
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014-4-14 ����11:14:59
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
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			holder.answerTextView.setText(queryAnswerList.get(position));
			//			holder.userNameTextView.setText(jUserList.get(position).getNickname());
			/** 
			* ��ʾͼƬ 
			* ����1��ͼƬurl 
			* ����2����ʾͼƬ�Ŀؼ� 
			* ����3����ʾͼƬ������ 
			* ����4�������� 
			*/
			//			imageLoader.displayImage(HttpUtil.BASE_URL + jUserList.get(position).getAvatar(), holder.headImageView,
			//					options, animateFirstListener);

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
}
