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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�ListActivity   
* ��������  ͨ���б�Activity��ͨ��ListViewչʾ������ͨ��Intent����
* �����ˣ���˧  
* ����ʱ�䣺2014-1-2 ����4:45:49   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-2 ����4:45:49   
* �޸ı�ע��   
* @version    
*    
*/
public class ListActivity extends BaseActivity {
	ListView mylistView;
	ImageButton gobackButton;// ���������ؼ�
	TextView titleNameTextView;// ����������TextView
	String titleName;// ����������
	List<String> dataList;// Ҫչʾ������
	List<Examination> examinations;// �Ծ��б�
	String datatype;// ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_list);

		findViewById();// ��ʼ��View

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
		initData(datatype);// �����ݿ��г�ʼ������

		// ������ݲ�Ϊ�գ�������Adapter
		if (dataList != null) {
			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_item_title,
					dataList);
			mylistView.setAdapter(mAdapter);
		}

		setListener();// ����ListView Item����¼�

		titleNameTextView.setText(titleName);// ���ñ���
		// �����¼�����
		gobackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * ��ʼ�����ݣ������ݿ���ȡ����
	 */
	private void initData(String datatype) {
		DaoSession daoSession = BaseApplication.getDaoSession(ListActivity.this);
		ExaminationDao examinationDao = daoSession.getExaminationDao();
		// TODO Auto-generated method stub
		if (datatype.equals("��������")) {
			dataList = new ArrayList<String>();
			// �����ݿ��в�������������б�����������ӵ�dataList
			examinations = examinationDao.queryBuilder()
					.where(com.bishe.examhelper.dao.ExaminationDao.Properties.Exam_type.eq(datatype)).list();
			for (Examination examination : examinations) {
				dataList.add(examination.getExam_name());
			}
		}
	}

	/**
	 * ����ListView Item����¼�
	 */
	private void setListener() {
		// TODO Auto-generated method stub
		if (datatype.equals("��������")) {
			mylistView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ListActivity.this, ExamActivity.class);
					intent.putExtra(DefaultKeys.BUNDLE_EXAMACTIVITY_EXAMID, examinations.get(position).getId());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);// �����л�Ч��
				}
			});
		}
	}

}
