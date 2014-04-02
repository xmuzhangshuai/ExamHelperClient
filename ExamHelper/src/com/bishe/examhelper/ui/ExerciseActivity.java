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
 * 项目名称：ExamHelper   
 * 类名称：ExerciseActivity   
 * 类描述：   做题页面的activity，题目用ViewPager来承载，根据传进来的参数不同，通过FragmentPagerAdapter，
 *          定义不同的Fragment返回到本ViewPager显示。
 * 创建人：张帅   
 * 创建时间：2013-12-14 下午1:23:07   
 * 修改人：张帅   
 * 修改时间：2013-12-15 下午3:30:09   
 * 修改备注：   
 * @version    
 *    
 */
public class ExerciseActivity extends BaseFragmentActivity {

	List<SingleChoice> singleChoiceList = new ArrayList<SingleChoice>();// 单选题List
	List<MultiChoice> multiChoiceList = new ArrayList<MultiChoice>();// 多选题List
	List<MaterialAnalysis> materialAnalysiList = new ArrayList<MaterialAnalysis>();// 材料分析题List

	// 定义一个pagerAdapter
	QuestionsAdapter myQuestionsAdapter;
	ViewPager mViewPager;

	QuestionType myQuestionType;// 从ExpandableListActivity点击后传过来的题型
	String questionTypeString;// 题型名称
	List<GroupQuestion> groupQuestionList = new ArrayList<GroupQuestion>();// 一组内题目的List

	DaoSession daoSession = BaseApplication.getDaoSession(ExerciseActivity.this);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_activity);

		// 从intent中接收groups对象
		Long groupID = this.getIntent().getLongExtra(DefaultKeys.INTENT_GROUP_ID, 1);
		Groups myGroups = daoSession.getGroupsDao().load(groupID);
		myQuestionType = myGroups.getQuestionType();
		questionTypeString = myQuestionType.getType_name();
		groupQuestionList = myGroups.getQuestionList();

		// 执行异步任务，从数据库中取出题目，并且给mViewPager设置adapter
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
	 * 查找View
	 */
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
	}

	/**
	 * 初始化View
	 */
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("题目练习");
	}

	/**
	* 类名称：QuestionsAdapter   
	* 类描述：   题目的Adapter，根据不同的题型新建不同的Fragment，以显示在ViewPager中
	* 创建人：张帅  
	* 创建时间：2013-12-27 下午4:20:10   
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

		// 返回一个pager的fragment
		public Fragment getItem(int position) {

			// 如果题型是单项选择题
			if (questionTypeString.equals(DefaultValues.SINGLE_CHOICE)) {
				SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
				Bundle localBundle = new Bundle();

				// 如果是第一题则显示题目要求
				if (position == 0) {
					DialogTools.showPositiveDialog(ExerciseActivity.this, "题目要求", DefaultValues.REQUEST_SINGLE_CHOICE);
				}
				// 传入一个singleChoice对象
				localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE,
						(Serializable) ExerciseActivity.this.singleChoiceList.get(position));
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
				mySingleChoiceFragment.setArguments(localBundle);
				return mySingleChoiceFragment;
			}

			// 如果是多项选择题
			else if (questionTypeString.equals(DefaultValues.MULTI_CHOICE)) {
				MultiChoiceFragment myMultiChoiceFragment = new MultiChoiceFragment();
				Bundle bundle = new Bundle();

				// 如果是第一题则显示题目要求
				if (position == 0) {
					DialogTools.showPositiveDialog(ExerciseActivity.this, "题目要求", DefaultValues.REQUEST_MULTI_CHOICE);
				}

				// 传入一个MultiChoice对象
				bundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE,
						(Serializable) ExerciseActivity.this.multiChoiceList.get(position));
				bundle.putInt(DefaultKeys.BUNDLE_MULTI_CHOICE_POSITION, position + 1);
				myMultiChoiceFragment.setArguments(bundle);
				return myMultiChoiceFragment;
			}

			// 如果是材料分析题
			else if (questionTypeString.equals(DefaultValues.MATERIAL_ANALYSIS)) {
				MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
				Bundle bundle = new Bundle();

				// 如果是第一题则显示题目要求
				if (position == 0) {
					DialogTools.showPositiveDialog(ExerciseActivity.this, "题目要求",
							DefaultValues.REQUEST_MATERIAL_ANALYSIS);
				}

				// 传入一个MaterialAnalysis对象
				bundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS,
						(Serializable) ExerciseActivity.this.materialAnalysiList.get(position));
				materialAnalysisFragment.setArguments(bundle);
				return materialAnalysisFragment;
			} else {
				return null;
			}
		}

		// 返回标题
		public CharSequence getPageTitle(int position) {
			return myQuestionType.getType_name() + (position + 1);
		}
	}

	/**
	*    
	* 类名称：getQuestions   
	* 类描述：   异步任务，根据不同的题型，从不同的题目表中查找出题目，
	*          并且在完成之后在onPostExecute方法中设置ViewPager的adapter
	* 创建人：张帅  
	* 创建时间：2013-12-27 下午8:09:48   
	 */
	public class getQuestions extends AsyncTask<Void, Void, Void> {

		SingleChoiceDao singleChoiceDao = daoSession.getSingleChoiceDao();
		MultiChoiceDao multiChoiceDao = daoSession.getMultiChoiceDao();
		MaterialAnalysisDao materialAnalysisDao = daoSession.getMaterialAnalysisDao();

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			// 如果题型是单项选择题
			if (questionTypeString.equals(DefaultValues.SINGLE_CHOICE)) {
				// 根据groupQuestionList中的题目ID取出SingleChoice对象的列表
				for (GroupQuestion groupQuestion : groupQuestionList) {
					SingleChoice singleChoice = singleChoiceDao.load(groupQuestion.getQuestion_id());
					singleChoiceList.add(singleChoice);
				}
			}

			// 如果是多项选择题
			else if (questionTypeString.equals(DefaultValues.MULTI_CHOICE)) {
				// 根据groupQuestionList中的题目ID取出MultiChoice对象的列表
				for (GroupQuestion groupQuestion : groupQuestionList) {
					MultiChoice multiChoice = multiChoiceDao.load(groupQuestion.getQuestion_id());
					multiChoiceList.add(multiChoice);
				}
			}

			// 如果是材料分析题
			else if (questionTypeString.equals(DefaultValues.MATERIAL_ANALYSIS)) {
				// 根据groupQuestionList中的题目ID取出MaterialAnalysis对象的列表
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
			// 取完数据之后设置ViewPager的adapter
			ExerciseActivity.this.myQuestionsAdapter = new QuestionsAdapter(getSupportFragmentManager());
			ExerciseActivity.this.mViewPager = ((ViewPager) findViewById(R.id.pager));
			ExerciseActivity.this.mViewPager.setAdapter(myQuestionsAdapter);
		}

	}

}
