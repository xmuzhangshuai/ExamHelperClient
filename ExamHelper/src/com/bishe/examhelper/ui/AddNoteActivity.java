package com.bishe.examhelper.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.dbService.NoteService;
import com.bishe.examhelper.entities.Note;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.utils.DateTimeTools;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：AddNoteActivity   
* 类描述：   在答题页面点击添加笔记按钮，显示此页面
* 创建人：张帅  
* 创建时间：2014-1-8 下午7:09:48   
* 修改人：张帅   
* 修改时间：2014-1-8 下午7:09:48   
* 修改备注：   
* @version    
*    
*/
public class AddNoteActivity extends BaseActivity implements OnClickListener {
	/***********Views************/
	private TextView textView_title;// 文本编辑区题目
	private EditText editText_mynote;// 文本编辑区
	private Button add_btn;// 添加按钮
	private TextView edit_btn;// 编辑按钮
	private TextView delete_btn;// 删除按钮
	private ListView everyone_note_listView;// 大家的笔记
	private LinearLayout btn_area;// 按钮区域，包含编辑按钮和删除按钮
	private Button submit_edit;// 确定修改按钮

	/***********变量************/
	private Question myQuestion;// 题目
	private Note myNote;// 笔记
	private String myNoteString;// 笔记内容

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnote);

		// 接收题目数据
		myQuestion = (Question) getIntent().getSerializableExtra(DefaultKeys.BUNDLE_QUESTION);

		findViewById();
		initView();

		// 如果此题目添加过笔记，则展示笔记
		if (isHaveNote()) {
			myNoteString = myNote.getNote_content();
			displayNote();
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		textView_title = (TextView) findViewById(R.id.textView_title);
		editText_mynote = (EditText) findViewById(R.id.editText_mynote);
		add_btn = (Button) findViewById(R.id.add_btn);
		everyone_note_listView = (ListView) findViewById(R.id.everyone_note_listView);
		btn_area = (LinearLayout) findViewById(R.id.btn_area);
		edit_btn = (TextView) findViewById(R.id.edit_btn);
		delete_btn = (TextView) findViewById(R.id.delete_btn);
		submit_edit = (Button) findViewById(R.id.submit_edit);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("添加笔记");
		add_btn.setOnClickListener(this);
		edit_btn.setOnClickListener(this);
		delete_btn.setOnClickListener(this);
		submit_edit.setOnClickListener(this);
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
		NoteService mNoteService = NoteService.getInstance(this);
		if (myQuestion != null && editText_mynote.getText().toString().length() > 0) {
			mNoteService.insertNote(myQuestion, editText_mynote.getText().toString());
		}
		myNote = mNoteService.loadNote(myQuestion);
	}

	/**
	 * 更新笔记
	 */
	protected void updateNote() {
		NoteService mNoteService = NoteService.getInstance(this);
		submit_edit.setVisibility(View.GONE);
		myNoteString = editText_mynote.getText().toString();
		displayNote();

		if (myNote != null) {
			try {
				myNote.setNote_content(myNoteString);// 更新内容
				myNote.setNote_time(DateTimeTools.getCurrentDate());// 更新时间
				mNoteService.updateNote(myNote);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
