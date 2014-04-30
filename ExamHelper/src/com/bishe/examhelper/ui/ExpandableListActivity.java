package com.bishe.examhelper.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.ExpandableListAdapter;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultSetting;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.SectionDao;
import com.bishe.examhelper.dao.SubjectDao;
import com.bishe.examhelper.dao.SubjectDao.Properties;
import com.bishe.examhelper.entities.Groups;
import com.bishe.examhelper.entities.Section;
import com.bishe.examhelper.entities.Subject;

/**   
 *    
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�ExpandableListActivity   
 * ��������   ����չ�б��Activity����ʾһ��ExpandableListView��������ʾ��Ӧ�б�����ݡ�ͨ��ExpandableListAdapter
 *          �������ݣ����ݺ��ڴ����ݿ���ȡ��
 * �����ˣ���˧   
 * ����ʱ�䣺2013-12-13 ����9:10:11   
 * �޸��ˣ���˧   
 * �޸�ʱ�䣺2013-12-25 ����5:15:49   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class ExpandableListActivity extends BaseActivity {
	ImageButton goback;// ���������ؼ�
	TextView titleNameTextView;// ����������TextView
	ExpandableListView myeExpandableListView;

	// �½��б�
	private Section[] mySections;

	// �½��·����б�
	private Groups[][] myGroups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.expandable_list);
		findViewById();

		initListContent();// �����ݿ���ȡ�����ݣ���ʼ���б�����

		// ͨ��ExpandableListAdapterΪExpandableListView������
		if (mySections != null && myGroups != null) {
			ExpandableListAdapter myExpandableListAdapter = new ExpandableListAdapter(this, mySections, myGroups);
			myeExpandableListView.setAdapter(myExpandableListAdapter);
		}

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

	/**
	 * ��ʼ���б�����
	 */
	private void initListContent() {
		DaoSession daoSession = BaseApplication.getDaoSession(this);
		SubjectDao subjectDao = daoSession.getSubjectDao();
		SectionDao sectionDao = daoSession.getSectionDao();

		// ��ʼ���������ε��½��б�
		Subject subject = subjectDao.queryBuilder()
				.where(Properties.Subject_name.eq(DefaultValues.SUBJECT_POLITICAL_EXAM)).unique();
		if (subject != null) {
			List<Section> sectionList = subject.getSectionList();
			mySections = sectionList.toArray(new Section[sectionList.size()]);

			myGroups = new Groups[mySections.length][];

			// ��ʼ�������½��µ�����
			for (int i = DefaultSetting.START_SECTION_ID; i <= DefaultSetting.END_SECTION_ID; i++) {
				Section section = sectionDao.load((long) i);
				List<Groups> groupsList = section.getGroupList();
				myGroups[i - DefaultSetting.START_SECTION_ID] = new Groups[groupsList.size()];

				for (int j = 0; j < groupsList.size(); j++) {
					myGroups[i - DefaultSetting.START_SECTION_ID][j] = groupsList.get(j);
				}
			}
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		goback = (ImageButton) findViewById(R.id.titleBtnLeft);
		titleNameTextView = (TextView) findViewById(R.id.titleName);
		myeExpandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		titleNameTextView.setText("ϰ�⼯");

		// ΪExpandableListView���Ӽ�¼���¼�
		myeExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				// �򿪵�ѡ�����

				Groups groups = myGroups[groupPosition][childPosition];// ��õ��������
				Intent intent = new Intent(ExpandableListActivity.this, ExerciseActivity.class);
				intent.putExtra(DefaultKeys.INTENT_GROUP_ID, groups.getId());// �������groups��ID���ݸ������activity
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				return true;
			}
		});

		// �����¼�����
		goback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
}
