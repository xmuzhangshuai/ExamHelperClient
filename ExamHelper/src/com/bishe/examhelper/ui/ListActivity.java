package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.ExaminationDao;
import com.bishe.examhelper.entities.Examination;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：ListActivity   
* 类描述：  通用列表Activity，通过ListView展示，内容通过Intent接受
* 创建人：张帅  
* 创建时间：2014-1-2 下午4:45:49   
* 修改人：张帅   
* 修改时间：2014-1-2 下午4:45:49   
* 修改备注：   
* @version    
*    
*/
public class ListActivity extends BaseActivity {
	ListView mylistView;
	ImageButton gobackButton;// 标题栏返回键
	TextView titleNameTextView;// 标题栏名字TextView
	String titleName;// 标题栏名字
	List<String> dataList;// 要展示的数据
	List<Examination> examinations;// 试卷列表
	String datatype;// 参数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_list);

		findViewById();// 初始化View

		titleName = getIntent().getStringExtra(DefaultKeys.BUNDLE_TITLE_NAME);
		datatype = getIntent().getStringExtra(DefaultKeys.BUNDLE_LIST_TYPE);

		initView();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mylistView = (ListView) findViewById(R.id.sort_practice_list);
		gobackButton = (ImageButton) findViewById(R.id.titleBtnLeft);
		titleNameTextView = (TextView) findViewById(R.id.titleName);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		initData(datatype);// 从数据库中初始化数据

		// 如果数据不为空，则设置Adapter
		if (dataList != null) {
			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_item_title,
					dataList);
			mylistView.setAdapter(mAdapter);
		}

		setListener();// 设置ListView Item点击事件

		titleNameTextView.setText(titleName);// 设置标题
		// 返回事件监听
		gobackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * 初始化数据，从数据库中取数据
	 */
	private void initData(String datatype) {
		DaoSession daoSession = BaseApplication.getDaoSession(ListActivity.this);
		ExaminationDao examinationDao = daoSession.getExaminationDao();
		// TODO Auto-generated method stub
		if (datatype.equals("历年真题")) {
			dataList = new ArrayList<String>();
			// 从数据库中查找历年真题的列表，并将名字添加到dataList
			examinations = examinationDao.queryBuilder()
					.where(com.bishe.examhelper.dao.ExaminationDao.Properties.Exam_type.eq(datatype)).list();
			for (Examination examination : examinations) {
				dataList.add(examination.getExam_name());
			}
		}
	}

	/**
	 * 设置ListView Item点击事件
	 */
	private void setListener() {
		// TODO Auto-generated method stub
		if (datatype.equals("历年真题")) {
			mylistView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ListActivity.this, ExamActivity.class);
					intent.putExtra(DefaultKeys.BUNDLE_EXAMACTIVITY_EXAMID, examinations.get(position).getId());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);// 设置切换效果
				}
			});
		}
	}

}
