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
import com.bishe.examhelper.dao.ExaminationDao;
import com.bishe.examhelper.dao.MaterialAnalysisDao;
import com.bishe.examhelper.dao.MultiChoiceDao;
import com.bishe.examhelper.dao.SingleChoiceDao;
import com.bishe.examhelper.entities.ExamQuestion;
import com.bishe.examhelper.entities.ExamSection;
import com.bishe.examhelper.entities.Examination;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.utils.DialogTools;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：ExamActivity   
* 类描述：   试卷的Activity，各种题目用ViewPager来承载，根据传进来的参数不同，通过FragmentPagerAdapter，
 *          定义不同的Fragment返回到本ViewPager显示。
* 创建人：张帅  
* 创建时间：2014-1-2 下午6:44:36   
* 修改人：张帅   
* 修改时间：2014-1-2 下午6:44:36   
* 修改备注：   
* @version    
*    
*/
public class ExamActivity extends BaseFragmentActivity {
	List<SingleChoice> singleChoiceList = new ArrayList<SingleChoice>();// 单选题List
	List<MultiChoice> multiChoiceList = new ArrayList<MultiChoice>();// 多选题List
	List<MaterialAnalysis> materialAnalysiList = new ArrayList<MaterialAnalysis>();// 材料分析题List

	// 定义一个pagerAdapter
	ExamAdapter myExamAdapter;
	ViewPager mViewPager;

	// 试卷的实体
	Examination mExamination;
	String sinRequest;// 单选题题目要求
	String mulRequest;// 多选题题目要求
	String materRequest;// 材料题题目要求

	// String examRequest;// 试卷要求

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_activity);

		// 从Intent中接收Examination对象的ID
		Long examID = getIntent().getLongExtra(DefaultKeys.BUNDLE_EXAMACTIVITY_EXAMID, 1);
		new InitExamContent().execute(examID);
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
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("历年真题");
	}

	/**
	*    
	* 类名称：getQuestions   
	* 类描述：   异步任务内部类，通过试卷的ID查找数据库中数据
	*          并且在完成之后在onPostExecute方法中设置ViewPager的adapter
	* 创建人：张帅  
	* 创建时间：2013-12-27 下午8:09:48   
	 */
	public class InitExamContent extends AsyncTask<Long, Void, Void> {
		DaoSession daoSession = BaseApplication.getDaoSession(ExamActivity.this);
		ExaminationDao examinationDao = daoSession.getExaminationDao();
		SingleChoiceDao singleChoiceDao = daoSession.getSingleChoiceDao();
		MultiChoiceDao multiChoiceDao = daoSession.getMultiChoiceDao();
		MaterialAnalysisDao materialAnalysisDao = daoSession.getMaterialAnalysisDao();
		List<ExamSection> examSections;// 试卷大题列表

		@Override
		protected Void doInBackground(Long... ID) {
			// TODO Auto-generated method stub
			Long examID = ID[0];
			mExamination = examinationDao.load(examID);// 查找试卷

			examSections = mExamination.getExamSectionList();// 获取试卷大题列表
			for (ExamSection examSection : examSections) {
				List<ExamQuestion> examQuestionList;// 章节题目
				examQuestionList = examSection.getExamQuestionList();
				if (examSection.getQuestionType().getType_name().equals(DefaultValues.SINGLE_CHOICE)) {// 如果是单项选择题
					sinRequest = examSection.getRequest();// 获取题目要求
					for (ExamQuestion examQuestion : examQuestionList) {
						// 从数据库中取出单选题并添加到singleChoiceList
						singleChoiceList.add(singleChoiceDao.load(examQuestion.getQuestion_id()));
					}
				} else if (examSection.getQuestionType().getType_name().equals(DefaultValues.MULTI_CHOICE)) {// 如果是多项选择题
					mulRequest = examSection.getRequest();// 获取题目要求
					for (ExamQuestion examQuestion : examQuestionList) {
						// 从数据库中取出多选题并添加到multiChoiceList
						multiChoiceList.add(multiChoiceDao.load(examQuestion.getQuestion_id()));
					}
				} else if (examSection.getQuestionType().getType_name().equals(DefaultValues.MATERIAL_ANALYSIS)) {// 如果是材料题
					materRequest = examSection.getRequest();// 获取题目要求
					for (ExamQuestion examQuestion : examQuestionList) {
						// 从数据库中取出材料题并添加到materialAnalysiList
						materialAnalysiList.add(materialAnalysisDao.load(examQuestion.getQuestion_id()));
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// 取完数据之后设置ViewPager的adapter
			myExamAdapter = new ExamAdapter(getSupportFragmentManager());
			mViewPager = ((ViewPager) findViewById(R.id.pager));
			mViewPager.setAdapter(myExamAdapter);
			for (MultiChoice multiChoice : multiChoiceList) {
				System.out.println(multiChoice.getRightAnswer());
			}
			DialogTools.showPositiveDialog(ExamActivity.this, "试卷要求", mExamination.getExam_request());// 显示试卷要求对话框
		}
	}

	/**
	 * 
	*    
	* 项目名称：ExamHelper   
	* 类名称：ExamAdapter   
	* 类描述：    试卷的Adapter，根据不同的题型新建不同的Fragment，以显示在ViewPager中
	* 创建人：张帅  
	* 创建时间：2014-1-2 下午7:05:11   
	*
	 */
	public class ExamAdapter extends FragmentPagerAdapter {
		int sinCount = singleChoiceList.size();// 单选题数量
		int mulCount = multiChoiceList.size();// 多选题数量
		int materCount = materialAnalysiList.size();// 材料题数量
		int count = sinCount + mulCount + materCount;// 总题目数

		public ExamAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			if (position < sinCount) {// 如果是单选题
				SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
				Bundle localBundle = new Bundle();

				// 如果是第一个单选题，则显示题目要求
				if (position == 0)
					DialogTools.showPositiveDialog(ExamActivity.this, "题目要求", sinRequest);

				// 传入一个singleChoice对象
				localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE,
						(Serializable) singleChoiceList.get(position));
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
				mySingleChoiceFragment.setArguments(localBundle);
				return mySingleChoiceFragment;

			} else if (position >= sinCount && position < sinCount + mulCount) {// 如果是多选题
				MultiChoiceFragment myMultiChoiceFragment = new MultiChoiceFragment();
				Bundle bundle = new Bundle();

				// 如果是第一个多选题，则显示题目要求
				if (position == sinCount + 1)
					DialogTools.showPositiveDialog(ExamActivity.this, "题目要求", mulRequest);

				// 传入一个MultiChoice对象
				bundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE,
						(Serializable) multiChoiceList.get(position - sinCount));
				bundle.putInt(DefaultKeys.BUNDLE_MULTI_CHOICE_POSITION, position + 1);
				myMultiChoiceFragment.setArguments(bundle);
				return myMultiChoiceFragment;

			} else if (position >= sinCount + mulCount && position < count) {// 如果是材料分析题
				MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
				Bundle bundle = new Bundle();

				// 如果是第一个材料题，则显示题目要求
				if (position == sinCount + mulCount + 1)
					DialogTools.showPositiveDialog(ExamActivity.this, "题目要求", materRequest);

				// 传入一个MaterialAnalysis对象
				bundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS,
						(Serializable) materialAnalysiList.get(position - sinCount - mulCount));
				bundle.putInt(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS_POSITION, position + 1);
				materialAnalysisFragment.setArguments(bundle);
				return materialAnalysisFragment;

			} else {
				return null;
			}

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return singleChoiceList.size() + multiChoiceList.size() + materialAnalysiList.size();
		}

		// 返回标题
		public CharSequence getPageTitle(int position) {
			if (position < sinCount) {// 如果是单选题
				return DefaultValues.SINGLE_CHOICE + (position + 1);

			} else if (position >= sinCount && position < sinCount + mulCount) {// 如果是多选题
				return DefaultValues.MULTI_CHOICE + (position + 1);

			} else if (position >= sinCount + mulCount && position < count) {// 如果是材料分析题

				return DefaultValues.MATERIAL_ANALYSIS + (position + 1);
			} else {
				return null;
			}
		}

	}
}
