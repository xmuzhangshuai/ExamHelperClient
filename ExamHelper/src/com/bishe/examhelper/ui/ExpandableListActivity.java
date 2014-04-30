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
 * 项目名称：ExamHelper   
 * 类名称：ExpandableListActivity   
 * 类描述：   可扩展列表的Activity，显示一个ExpandableListView，用来显示对应列表的内容。通过ExpandableListAdapter
 *          设置数据，数据后期从数据库中取出
 * 创建人：张帅   
 * 创建时间：2013-12-13 上午9:10:11   
 * 修改人：张帅   
 * 修改时间：2013-12-25 下午5:15:49   
 * 修改备注：   
 * @version    
 *    
 */
public class ExpandableListActivity extends BaseActivity {
	ImageButton goback;// 标题栏返回键
	TextView titleNameTextView;// 标题栏名字TextView
	ExpandableListView myeExpandableListView;

	// 章节列表
	private Section[] mySections;

	// 章节下分组列表
	private Groups[][] myGroups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.expandable_list);
		findViewById();

		initListContent();// 从数据库中取出数据，初始化列表内容

		// 通过ExpandableListAdapter为ExpandableListView绑定数据
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
	 * 初始化列表内容
	 */
	private void initListContent() {
		DaoSession daoSession = BaseApplication.getDaoSession(this);
		SubjectDao subjectDao = daoSession.getSubjectDao();
		SectionDao sectionDao = daoSession.getSectionDao();

		// 初始化考研政治的章节列表
		Subject subject = subjectDao.queryBuilder()
				.where(Properties.Subject_name.eq(DefaultValues.SUBJECT_POLITICAL_EXAM)).unique();
		if (subject != null) {
			List<Section> sectionList = subject.getSectionList();
			mySections = sectionList.toArray(new Section[sectionList.size()]);

			myGroups = new Groups[mySections.length][];

			// 初始化所有章节下的组名
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
		titleNameTextView.setText("习题集");

		// 为ExpandableListView的子记录绑定事件
		myeExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				// 打开单选题界面

				Groups groups = myGroups[groupPosition][childPosition];// 获得点击的题组
				Intent intent = new Intent(ExpandableListActivity.this, ExerciseActivity.class);
				intent.putExtra(DefaultKeys.INTENT_GROUP_ID, groups.getId());// 将点击的groups的ID传递给答题的activity
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				return true;
			}
		});

		// 返回事件监听
		goback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
}
