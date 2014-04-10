package com.bishe.examhelper.ui;

import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.entities.Note;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.service.NoteService;
import com.bishe.examhelper.utils.DateTimeTools;
import com.umeng.analytics.MobclickAgent;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�AddNoteActivity   
* ��������   �ڴ���ҳ������ӱʼǰ�ť����ʾ��ҳ��
* �����ˣ���˧  
* ����ʱ�䣺2014-1-8 ����7:09:48   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-8 ����7:09:48   
* �޸ı�ע��   
* @version    
*    
*/
public class AddNoteActivity extends ListActivity implements OnClickListener {
	/***********Views************/
	private TextView textView_title;// �ı��༭����Ŀ
	private EditText editText_mynote;// �ı��༭��
	private Button add_btn;// ��Ӱ�ť
	private TextView edit_btn;// �༭��ť
	private TextView delete_btn;// ɾ����ť
//	private PullToRefreshListView everyone_note_listView;// ��ҵıʼ�
	private LinearLayout btn_area;// ��ť���򣬰����༭��ť��ɾ����ť
	private Button submit_edit;// ȷ���޸İ�ť

	/***********����************/
	private Question myQuestion;// ��Ŀ
	private Note myNote;// �ʼ�
	private String myNoteString;// �ʼ�����
	private LinkedList<String> mListItems;
	private ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnote);

		// ������Ŀ����
		myQuestion = (Question) getIntent().getSerializableExtra(DefaultKeys.BUNDLE_QUESTION);

		findViewById();
		initView();

		// �������Ŀ��ӹ��ʼǣ���չʾ�ʼ�
		if (isHaveNote()) {
			myNoteString = myNote.getNote_content();
			displayNote();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		textView_title = (TextView) findViewById(R.id.textView_title);
		editText_mynote = (EditText) findViewById(R.id.editText_mynote);
		add_btn = (Button) findViewById(R.id.add_btn);
//		everyone_note_listView = (PullToRefreshListView) findViewById(R.id.everyone_note_listView);
		btn_area = (LinearLayout) findViewById(R.id.btn_area);
		edit_btn = (TextView) findViewById(R.id.edit_btn);
		delete_btn = (TextView) findViewById(R.id.delete_btn);
		submit_edit = (Button) findViewById(R.id.submit_edit);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("��ӱʼ�");
		add_btn.setOnClickListener(this);
		edit_btn.setOnClickListener(this);
		delete_btn.setOnClickListener(this);
		submit_edit.setOnClickListener(this);

		// Set a listener to be invoked when the list should be refreshed.
//		everyone_note_listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
//
//			@Override
//			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				// TODO Auto-generated method stub
//				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
//						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//				// Update the LastUpdatedLabel
//				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//
//				// Do work to refresh the list here.
//				new GetDataTask().execute();
//			}
//		});
//
//		// Add an end-of-list listener
//		everyone_note_listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
//
//			@Override
//			public void onLastItemVisible() {
//				// TODO Auto-generated method stub
//				Toast.makeText(AddNoteActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
//			}
//		});
//
//		ListView actualListView = everyone_note_listView.getRefreshableView();
//
//		// Need to use the Actual ListView when registering for Context Menu
//		registerForContextMenu(actualListView);
//
//		mListItems = new LinkedList<String>();
//		mListItems.addAll(Arrays.asList(mStrings));
//
//		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
//
//		/**
//		 * Add Sound Event Listener
//		 */
//		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(this);
//		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
//		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
//		everyone_note_listView.setOnPullEventListener(soundListener);
//
//		// You can also just use setListAdapter(mAdapter) or
//		// mPullRefreshListView.setAdapter(mAdapter)
//		actualListView.setAdapter(mAdapter);
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
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�GetDataTask   
	* ��������   
	* �����ˣ���˧  
	* ����ʱ�䣺2014-4-10 ����11:53:23   
	* @version    
	*
	 */
//	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
//
//		@Override
//		protected String[] doInBackground(Void... params) {
//			// Simulates a background job.
//			try {
//				Thread.sleep(4000);
//			} catch (InterruptedException e) {
//			}
//			return mStrings;
//		}
//
//		@Override
//		protected void onPostExecute(String[] result) {
//			mListItems.addFirst("��������");
//			mAdapter.notifyDataSetChanged();
//
//			// Call onRefreshComplete when the list has been refreshed.
//			everyone_note_listView.onRefreshComplete();
//
//			super.onPostExecute(result);
//		}
//	}

	private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };
}
