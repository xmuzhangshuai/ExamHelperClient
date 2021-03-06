package com.bishe.examhelper.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.AbsListViewBaseActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.entities.Note;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.service.NoteService;
import com.bishe.examhelper.service.UserService;
import com.bishe.examhelper.utils.DateTimeTools;
import com.bishe.examhelper.utils.HttpUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jsonobjects.JNote;
import com.jsonobjects.JUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 
 * 项目名称：ExamHelper 
 * 类名称：AddNoteActivity 
 * 类描述： 在答题页面点击添加笔记按钮，显示此页面 
 * 创建人：张帅
 * 创建时间：2014-1-8 
 * 下午7:09:48 
 * 修改人：张帅 
 * 修改时间：2014-1-8 下午7:09:48 修
 * 改备注：
 * @version
 * 
 */
public class AddNoteActivity extends AbsListViewBaseActivity implements OnClickListener {
	/*********** Views ************/
	private TextView textView_title;// 文本编辑区题目
	private EditText editText_mynote;// 文本编辑区
	private Button add_btn;// 添加按钮
	private TextView edit_btn;// 编辑按钮
	private TextView delete_btn;// 删除按钮
	private PullToRefreshListView everyone_note_listView;// 大家的笔记
	private LinearLayout btn_area;// 按钮区域，包含编辑按钮和删除按钮
	private Button submit_edit;// 确定修改按钮

	/*********** 变量 ************/
	private Question myQuestion;// 题目
	private Note myNote;// 笔记
	private String myNoteString;// 笔记内容
	private LinkedList<JNote> noteList;//笔记列表
	private NoteListAdapter noteListAdapter;
	private DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private LinkedList<JUser> jUserList;//用户列表 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnote);

		// 接收题目数据
		myQuestion = (Question) getIntent().getSerializableExtra(DefaultKeys.BUNDLE_QUESTION);

		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions  
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_empty)// 设置图片下载期间显示的图片  
				.showImageForEmptyUri(R.drawable.photoconor) // 设置图片Uri为空或是错误的时候显示的图片  
				.showImageOnFail(R.drawable.photoconor) // 设置图片加载或解码过程中发生错误显示的图片      
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中  
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中  
				.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片  
				.build(); // 创建配置过得DisplayImageOption对象  

		noteList = new LinkedList<JNote>();
		jUserList = new LinkedList<JUser>();

		findViewById();
		initView();

		// 如果此题目添加过笔记，则展示笔记
		if (isHaveNote()) {
			myNoteString = myNote.getNote_content();
			displayNote();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		applyScrollListener();
	}

	private void applyScrollListener() {
		everyone_note_listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		textView_title = (TextView) findViewById(R.id.textView_title);
		editText_mynote = (EditText) findViewById(R.id.editText_mynote);
		add_btn = (Button) findViewById(R.id.add_btn);
		everyone_note_listView = (PullToRefreshListView) findViewById(R.id.everyone_note_listView);
		btn_area = (LinearLayout) findViewById(R.id.btn_area);
		edit_btn = (TextView) findViewById(R.id.edit_btn);
		delete_btn = (TextView) findViewById(R.id.delete_btn);
		submit_edit = (Button) findViewById(R.id.submit_edit);
	}

	protected void initView() {
		// TODO Auto-generated method stub
		ActionBar actionBar = super.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar));
		actionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_stacked_bg));
		actionBar.setSplitBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar));
		actionBar.setTitle("添加笔记");
		add_btn.setOnClickListener(this);
		edit_btn.setOnClickListener(this);
		delete_btn.setOnClickListener(this);
		submit_edit.setOnClickListener(this);

		// Set a listener to be invoked when the list should be refreshed.
		everyone_note_listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

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

		// Add an end-of-list listener
		everyone_note_listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				Toast.makeText(AddNoteActivity.this, "没有更多笔记了!", Toast.LENGTH_SHORT).show();
			}
		});

		new GetDataTask().execute();

		noteListAdapter = new NoteListAdapter();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_btn:// 添加按钮
			if (editText_mynote.getText().toString().length() > 0) {
				myNoteString = editText_mynote.getText().toString();
				addNote();// 添加笔记
				displayNote();
			}
			break;
		case R.id.edit_btn:// 编辑按钮
			changeEditView();
			break;
		case R.id.delete_btn:// 删除按钮
			deleteNote();
			break;
		case R.id.submit_edit:// 确定修改按钮
			updateNote();
			break;

		default:
			break;
		}
	}

	/**
	 * 转变到编辑状态View
	 */
	protected void changeEditView() {
		editText_mynote.setEnabled(true);
		submit_edit.setVisibility(View.VISIBLE);
		textView_title.setBackgroundResource(R.color.white);
		textView_title.setText("输入笔记内容：");
		textView_title.setTextColor(Color.BLACK);
		textView_title.setTextSize(12);
		textView_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		btn_area.setVisibility(View.GONE);
	}

	/**
	 * 展示笔记
	 */
	protected void displayNote() {
		textView_title.setBackgroundResource(R.color.textview_bg);
		textView_title.setText("我的笔记:");
		textView_title.setTextColor(Color.WHITE);
		textView_title.setTextSize(16);
		textView_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_my_note, 0, 0, 0);
		editText_mynote.setText(myNoteString);
		editText_mynote.setEnabled(false);
		add_btn.setVisibility(View.GONE);
		btn_area.setVisibility(View.VISIBLE);
	}

	/**
	 * 判断题目是否含有笔记
	 * 
	 * @return
	 */
	protected boolean isHaveNote() {
		boolean flag = false;
		NoteService mNoteService = NoteService.getInstance(this);
		myNote = mNoteService.loadNote(myQuestion);
		if (myNote != null) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除笔记
	 */
	protected void deleteNote() {
		final NoteService mNoteService = NoteService.getInstance(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon_warning).setTitle("温馨提示").setMessage("是否删除笔记？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						add_btn.setVisibility(View.VISIBLE);
						myNoteString = null;
						changeEditView();
						submit_edit.setVisibility(View.GONE);
						editText_mynote.setText("");
						if (myNote != null) {
							mNoteService.deleteNote(myNote);

							new Thread() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									mNoteService.delNoteFromNet(myNote);
								}
							}.start();
						}
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();
	}

	/**
	 * 将笔记添加到数据库
	 */
	protected void addNote() {
		final NoteService mNoteService = NoteService.getInstance(this);
		if (myQuestion != null && editText_mynote.getText().toString().length() > 0) {
			mNoteService.insertNote(myQuestion, editText_mynote.getText().toString());
			new Thread() {
				public void run() {
					mNoteService.addNoteToNet(myQuestion, editText_mynote.getText().toString());
				};
			}.start();
		}
		myNote = mNoteService.loadNote(myQuestion);
	}

	/**
	 * 更新笔记
	 */
	protected void updateNote() {
		final NoteService mNoteService = NoteService.getInstance(this);
		submit_edit.setVisibility(View.GONE);
		myNoteString = editText_mynote.getText().toString();
		displayNote();

		if (myNote != null) {
			try {
				myNote.setNote_content(myNoteString);// 更新内容
				myNote.setNote_time(DateTimeTools.getCurrentDate());// 更新时间
				mNoteService.updateNote(myNote);

				new Thread() {
					public void run() {
						mNoteService.updateNoteToNet(myNote);
					};
				}.start();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 项目名称：ExamHelper
	 *  类名称：GetDataTask 
	 *  类描述：
	 *   创建人：张帅 
	 *  创建时间：2014-4-10 上午11:53:23
	 * 
	 * @version
	 * 
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				/**
				 * 从网络服务器获取该题目的笔记列表
				 */
				NoteService noteService = NoteService.getInstance(AddNoteActivity.this);
				noteList = new LinkedList<JNote>();
				List<JNote> jNoteList = noteService.getNoteListFormNet(myQuestion);
				noteList.addAll(jNoteList);

				/**
				 * 从笔记列表中抽取用户列表
				 */
				jUserList = new LinkedList<JUser>();
				for (JNote jNote : jNoteList) {
					JUser jUser = new JUser(jNote.getUser_id());
					jUserList.add(jUser);
				}

				/**
				 * 从网络端获取用户详情列表
				 */
				UserService userService = UserService.getInstance(AddNoteActivity.this);
				List<JUser> temp = userService.getUserList(jUserList);
				jUserList = new LinkedList<JUser>();
				jUserList.addAll(temp);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			//			mListItems.addFirst("新增。。");
			everyone_note_listView.setAdapter(noteListAdapter);
			noteListAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			everyone_note_listView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * 类名称：NoteListAdapter
	 * 类描述：笔记列表的Adapter
	 * 创建人： 张帅
	 * 创建时间：2014-4-10 下午4:14:07
	 *
	 */
	private class NoteListAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView noteContent;
			public ImageView headImageView;
			public TextView userNameTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return noteList.size();
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
				view = LayoutInflater.from(AddNoteActivity.this).inflate(R.layout.everyone_note_list_item, null);
				holder = new ViewHolder();
				holder.noteContent = (TextView) view.findViewById(R.id.note_content);
				holder.headImageView = (ImageView) view.findViewById(R.id.headImage);
				holder.userNameTextView = (TextView) view.findViewById(R.id.user_nickname);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			holder.noteContent.setText(noteList.get(position).getNote_content());
			holder.userNameTextView.setText(jUserList.get(position).getNickname());

			/** 
			* 显示图片 
			* 参数1：图片url 
			* 参数2：显示图片的控件 
			* 参数3：显示图片的设置 
			* 参数4：监听器 
			*/
			imageLoader.displayImage(HttpUtil.BASE_URL + jUserList.get(position).getAvatar(), holder.headImageView,
					options, animateFirstListener);

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
