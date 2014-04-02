package com.bishe.examhelper.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.base.BaseFragmentActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.MaterialAnalysisDao;
import com.bishe.examhelper.dao.MultiChoiceDao;
import com.bishe.examhelper.dao.SingleChoiceDao;
import com.bishe.examhelper.entities.GroupQuestion;
import com.bishe.examhelper.entities.Groups;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.QuestionType;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.utils.DialogTools;

/**   
 *    
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�ExerciseActivity   
 * ��������   ����ҳ���activity����Ŀ��ViewPager�����أ����ݴ������Ĳ�����ͬ��ͨ��FragmentPagerAdapter��
 *          ���岻ͬ��Fragment���ص���ViewPager��ʾ��
 * �����ˣ���˧   
 * ����ʱ�䣺2013-12-14 ����1:23:07   
 * �޸��ˣ���˧   
 * �޸�ʱ�䣺2013-12-15 ����3:30:09   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class ExerciseActivity extends BaseFragmentActivity {

	List<SingleChoice> singleChoiceList = new ArrayList<SingleChoice>();// ��ѡ��List
	List<MultiChoice> multiChoiceList = new ArrayList<MultiChoice>();// ��ѡ��List
	List<MaterialAnalysis> materialAnalysiList = new ArrayList<MaterialAnalysis>();// ���Ϸ�����List

	// ����һ��pagerAdapter
	QuestionsAdapter myQuestionsAdapter;
	ViewPager mViewPager;

	QuestionType myQuestionType;// ��ExpandableListActivity����󴫹���������
	String questionTypeString;// ��������
	List<GroupQuestion> groupQuestionList = new ArrayList<GroupQuestion>();// һ������Ŀ��List

	DaoSession daoSession = BaseApplication.getDaoSession(ExerciseActivity.this);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_activity);

		// ��intent�н���groups����
		Long groupID = this.getIntent().getLongExtra(DefaultKeys.INTENT_GROUP_ID, 1);
		Groups myGroups = daoSession.getGroupsDao().load(groupID);
		myQuestionType = myGroups.getQuestionType();
		questionTypeString = myQuestionType.getType_name();
		groupQuestionList = myGroups.getQuestionList();

		// ִ���첽���񣬴����ݿ���ȡ����Ŀ�����Ҹ�mViewPager����adapter
		new getQuestions().execute();

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
	 * ����View
	 */
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
	}

	/**
	 * ��ʼ��View
	 */
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("��Ŀ��ϰ");
	}

	/**
	* �����ƣ�QuestionsAdapter   
	* ��������   ��Ŀ��Adapter�����ݲ�ͬ�������½���ͬ��Fragment������ʾ��ViewPager��
	* �����ˣ���˧  
	* ����ʱ�䣺2013-12-27 ����4:20:10   
	* @version    
	 */
	public class QuestionsAdapter extends FragmentPagerAdapter {

		public QuestionsAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		public int getCount() {
			return ExerciseActivity.this.groupQuestionList.size();
		}

		// ����һ��pager��fragment
		public Fragment getItem(int position) {

			// ��������ǵ���ѡ����
			if (questionTypeString.equals(DefaultValues.SINGLE_CHOICE)) {
				SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
				Bundle localBundle = new Bundle();

				// ����ǵ�һ������ʾ��ĿҪ��
				if (position == 0) {
					DialogTools.showPositiveDialog(ExerciseActivity.this, "��ĿҪ��", DefaultValues.REQUEST_SINGLE_CHOICE);
				}
				// ����һ��singleChoice����
				localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE,
						(Serializable) ExerciseActivity.this.singleChoiceList.get(position));
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
				mySingleChoiceFragment.setArguments(localBundle);
				return mySingleChoiceFragment;
			}

			// ����Ƕ���ѡ����
			else if (questionTypeString.equals(DefaultValues.MULTI_CHOICE)) {
				MultiChoiceFragment myMultiChoiceFragment = new MultiChoiceFragment();
				Bundle bundle = new Bundle();

				// ����ǵ�һ������ʾ��ĿҪ��
				if (position == 0) {
					DialogTools.showPositiveDialog(ExerciseActivity.this, "��ĿҪ��", DefaultValues.REQUEST_MULTI_CHOICE);
				}

				// ����һ��MultiChoice����
				bundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE,
						(Serializable) ExerciseActivity.this.multiChoiceList.get(position));
				bundle.putInt(DefaultKeys.BUNDLE_MULTI_CHOICE_POSITION, position + 1);
				myMultiChoiceFragment.setArguments(bundle);
				return myMultiChoiceFragment;
			}

			// ����ǲ��Ϸ�����
			else if (questionTypeString.equals(DefaultValues.MATERIAL_ANALYSIS)) {
				MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
				Bundle bundle = new Bundle();

				// ����ǵ�һ������ʾ��ĿҪ��
				if (position == 0) {
					DialogTools.showPositiveDialog(ExerciseActivity.this, "��ĿҪ��",
							DefaultValues.REQUEST_MATERIAL_ANALYSIS);
				}

				// ����һ��MaterialAnalysis����
				bundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS,
						(Serializable) ExerciseActivity.this.materialAnalysiList.get(position));
				materialAnalysisFragment.setArguments(bundle);
				return materialAnalysisFragment;
			} else {
				return null;
			}
		}

		// ���ر���
		public CharSequence getPageTitle(int position) {
			return myQuestionType.getType_name() + (position + 1);
		}
	}

	/**
	*    
	* �����ƣ�getQuestions   
	* ��������   �첽���񣬸��ݲ�ͬ�����ͣ��Ӳ�ͬ����Ŀ���в��ҳ���Ŀ��
	*          ���������֮����onPostExecute����������ViewPager��adapter
	* �����ˣ���˧  
	* ����ʱ�䣺2013-12-27 ����8:09:48   
	 */
	public class getQuestions extends AsyncTask<Void, Void, Void> {

		SingleChoiceDao singleChoiceDao = daoSession.getSingleChoiceDao();
		MultiChoiceDao multiChoiceDao = daoSession.getMultiChoiceDao();
		MaterialAnalysisDao materialAnalysisDao = daoSession.getMaterialAnalysisDao();

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			// ��������ǵ���ѡ����
			if (questionTypeString.equals(DefaultValues.SINGLE_CHOICE)) {
				// ����groupQuestionList�е���ĿIDȡ��SingleChoice������б�
				for (GroupQuestion groupQuestion : groupQuestionList) {
					SingleChoice singleChoice = singleChoiceDao.load(groupQuestion.getQuestion_id());
					singleChoiceList.add(singleChoice);
				}
			}

			// ����Ƕ���ѡ����
			else if (questionTypeString.equals(DefaultValues.MULTI_CHOICE)) {
				// ����groupQuestionList�е���ĿIDȡ��MultiChoice������б�
				for (GroupQuestion groupQuestion : groupQuestionList) {
					MultiChoice multiChoice = multiChoiceDao.load(groupQuestion.getQuestion_id());
					multiChoiceList.add(multiChoice);
				}
			}

			// ����ǲ��Ϸ�����
			else if (questionTypeString.equals(DefaultValues.MATERIAL_ANALYSIS)) {
				// ����groupQuestionList�е���ĿIDȡ��MaterialAnalysis������б�
				for (GroupQuestion groupQuestion : groupQuestionList) {
					MaterialAnalysis materialAnalysis = materialAnalysisDao.load(groupQuestion.getQuestion_id());
					materialAnalysiList.add(materialAnalysis);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// ȡ������֮������ViewPager��adapter
			ExerciseActivity.this.myQuestionsAdapter = new QuestionsAdapter(getSupportFragmentManager());
			ExerciseActivity.this.mViewPager = ((ViewPager) findViewById(R.id.pager));
			ExerciseActivity.this.mViewPager.setAdapter(myQuestionsAdapter);
		}

	}

}
