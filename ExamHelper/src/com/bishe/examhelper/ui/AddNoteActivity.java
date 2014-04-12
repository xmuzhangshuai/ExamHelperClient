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
 * ��Ŀ���ƣ�ExamHelper 
 * �����ƣ�AddNoteActivity 
 * �������� �ڴ���ҳ������ӱʼǰ�ť����ʾ��ҳ�� 
 * �����ˣ���˧
 * ����ʱ�䣺2014-1-8 
 * ����7:09:48 
 * �޸��ˣ���˧ 
 * �޸�ʱ�䣺2014-1-8 ����7:09:48 ��
 * �ı�ע��
 * @version
 * 
 */
public class AddNoteActivity extends AbsListViewBaseActivity implements OnClickListener {
	/*********** Views ************/
	private TextView textView_title;// �ı��༭����Ŀ
	private EditText editText_mynote;// �ı��༭��
	private Button add_btn;// ��Ӱ�ť
	private TextView edit_btn;// �༭��ť
	private TextView delete_btn;// ɾ����ť
	private PullToRefreshListView everyone_note_listView;// ��ҵıʼ�
	private LinearLayout btn_area;// ��ť���򣬰����༭��ť��ɾ����ť
	private Button submit_edit;// ȷ���޸İ�ť

	/*********** ���� ************/
	private Question myQuestion;// ��Ŀ
	private Note myNote;// �ʼ�
	private String myNoteString;// �ʼ�����
	private LinkedList<JNote> noteList;//�ʼ��б�
	private NoteListAdapter noteListAdapter;
	private DisplayImageOptions options; // DisplayImageOptions����������ͼƬ��ʾ����
	private LinkedList<JUser> jUserList;//�û��б� 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnote);

		// ������Ŀ����
		myQuestion = (Question) getIntent().getSerializableExtra(DefaultKeys.BUNDLE_QUESTION);

		// ʹ��DisplayImageOptions.Builder()����DisplayImageOptions  
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_empty)// ����ͼƬ�����ڼ���ʾ��ͼƬ  
				.showImageForEmptyUri(R.drawable.photoconor) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
				.showImageOnFail(R.drawable.photoconor) // ����ͼƬ���ػ��������з���������ʾ��ͼƬ      
				.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���  
				.cacheOnDisc(true) // �������ص�ͼƬ�Ƿ񻺴���SD����  
				.displayer(new RoundedBitmapDisplayer(20)) // ���ó�Բ��ͼƬ  
				.build(); // �������ù���DisplayImageOption����  

		noteList = new LinkedList<JNote>();
		jUserList = new LinkedList<JUser>();

		findViewById();
		initView();

		// �������Ŀ��ӹ��ʼǣ���չʾ�ʼ�
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
		actionBar.setTitle("��ӱʼ�");
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
				Toast.makeText(AddNoteActivity.this, "û�и���ʼ���!", Toast.LENGTH_SHORT).show();
			}
		});

		new GetDataTask().execute();

		noteListAdapter = new NoteListAdapter();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_btn:// ��Ӱ�ť
			if (editText_mynote.getText().toString().length() > 0) {
				myNoteString = editText_mynote.getText().toString();
				addNote();// ��ӱʼ�
				displayNote();
			}
			break;
		case R.id.edit_btn:// �༭��ť
			changeEditView();
			break;
		case R.id.delete_btn:// ɾ����ť
			deleteNote();
			break;
		case R.id.submit_edit:// ȷ���޸İ�ť
			updateNote();
			break;

		default:
			break;
		}
	}

	/**
	 * ת�䵽�༭״̬View
	 */
	protected void changeEditView() {
		editText_mynote.setEnabled(true);
		submit_edit.setVisibility(View.VISIBLE);
		textView_title.setBackgroundResource(R.color.white);
		textView_title.setText("����ʼ����ݣ�");
		textView_title.setTextColor(Color.BLACK);
		textView_title.setTextSize(12);
		textView_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		btn_area.setVisibility(View.GONE);
	}

	/**
	 * չʾ�ʼ�
	 */
	protected void displayNote() {
		textView_title.setBackgroundResource(R.color.textview_bg);
		textView_title.setText("�ҵıʼ�:");
		textView_title.setTextColor(Color.WHITE);
		textView_title.setTextSize(16);
		textView_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_my_note, 0, 0, 0);
		editText_mynote.setText(myNoteString);
		editText_mynote.setEnabled(false);
		add_btn.setVisibility(View.GONE);
		btn_area.setVisibility(View.VISIBLE);
	}

	/**
	 * �ж���Ŀ�Ƿ��бʼ�
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
	 * ɾ���ʼ�
	 */
	protected void deleteNote() {
		final NoteService mNoteService = NoteService.getInstance(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon_warning).setTitle("��ܰ��ʾ").setMessage("�Ƿ�ɾ���ʼǣ�")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
				}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();
	}

	/**
	 * ���ʼ���ӵ����ݿ�
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
	 * ���±ʼ�
	 */
	protected void updateNote() {
		final NoteService mNoteService = NoteService.getInstance(this);
		submit_edit.setVisibility(View.GONE);
		myNoteString = editText_mynote.getText().toString();
		displayNote();

		if (myNote != null) {
			try {
				myNote.setNote_content(myNoteString);// ��������
				myNote.setNote_time(DateTimeTools.getCurrentDate());// ����ʱ��
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
	 * ��Ŀ���ƣ�ExamHelper
	 *  �����ƣ�GetDataTask 
	 *  ��������
	 *   �����ˣ���˧ 
	 *  ����ʱ�䣺2014-4-10 ����11:53:23
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
				 * �������������ȡ����Ŀ�ıʼ��б�
				 */
				NoteService noteService = NoteService.getInstance(AddNoteActivity.this);
				noteList = new LinkedList<JNote>();
				List<JNote> jNoteList = noteService.getNoteListFormNet(myQuestion);
				noteList.addAll(jNoteList);

				/**
				 * �ӱʼ��б��г�ȡ�û��б�
				 */
				jUserList = new LinkedList<JUser>();
				for (JNote jNote : jNoteList) {
					JUser jUser = new JUser(jNote.getUser_id());
					jUserList.add(jUser);
				}

				/**
				 * ������˻�ȡ�û������б�
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
			//			mListItems.addFirst("��������");
			everyone_note_listView.setAdapter(noteListAdapter);
			noteListAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			everyone_note_listView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * �����ƣ�NoteListAdapter
	 * ���������ʼ��б��Adapter
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014-4-10 ����4:14:07
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
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			holder.noteContent.setText(noteList.get(position).getNote_content());
			holder.userNameTextView.setText(jUserList.get(position).getNickname());

			/** 
			* ��ʾͼƬ 
			* ����1��ͼƬurl 
			* ����2����ʾͼƬ�Ŀؼ� 
			* ����3����ʾͼƬ������ 
			* ����4�������� 
			*/
			imageLoader.displayImage(HttpUtil.BASE_URL + jUserList.get(position).getAvatar(), holder.headImageView,
					options, animateFirstListener);

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
