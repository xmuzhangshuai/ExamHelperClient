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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�ExamActivity   
* ��������   �Ծ��Activity��������Ŀ��ViewPager�����أ����ݴ������Ĳ�����ͬ��ͨ��FragmentPagerAdapter��
 *          ���岻ͬ��Fragment���ص���ViewPager��ʾ��
* �����ˣ���˧  
* ����ʱ�䣺2014-1-2 ����6:44:36   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-2 ����6:44:36   
* �޸ı�ע��   
* @version    
*    
*/
public class ExamActivity extends BaseFragmentActivity {
	List<SingleChoice> singleChoiceList = new ArrayList<SingleChoice>();// ��ѡ��List
	List<MultiChoice> multiChoiceList = new ArrayList<MultiChoice>();// ��ѡ��List
	List<MaterialAnalysis> materialAnalysiList = new ArrayList<MaterialAnalysis>();// ���Ϸ�����List

	// ����һ��pagerAdapter
	ExamAdapter myExamAdapter;
	ViewPager mViewPager;

	// �Ծ��ʵ��
	Examination mExamination;
	String sinRequest;// ��ѡ����ĿҪ��
	String mulRequest;// ��ѡ����ĿҪ��
	String materRequest;// ��������ĿҪ��

	// String examRequest;// �Ծ�Ҫ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_activity);

		// ��Intent�н���Examination�����ID
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
		getActionBar().setTitle("��������");
	}

	/**
	*    
	* �����ƣ�getQuestions   
	* ��������   �첽�����ڲ��࣬ͨ���Ծ��ID�������ݿ�������
	*          ���������֮����onPostExecute����������ViewPager��adapter
	* �����ˣ���˧  
	* ����ʱ�䣺2013-12-27 ����8:09:48   
	 */
	public class InitExamContent extends AsyncTask<Long, Void, Void> {
		DaoSession daoSession = BaseApplication.getDaoSession(ExamActivity.this);
		ExaminationDao examinationDao = daoSession.getExaminationDao();
		SingleChoiceDao singleChoiceDao = daoSession.getSingleChoiceDao();
		MultiChoiceDao multiChoiceDao = daoSession.getMultiChoiceDao();
		MaterialAnalysisDao materialAnalysisDao = daoSession.getMaterialAnalysisDao();
		List<ExamSection> examSections;// �Ծ�����б�

		@Override
		protected Void doInBackground(Long... ID) {
			// TODO Auto-generated method stub
			Long examID = ID[0];
			mExamination = examinationDao.load(examID);// �����Ծ�

			examSections = mExamination.getExamSectionList();// ��ȡ�Ծ�����б�
			for (ExamSection examSection : examSections) {
				List<ExamQuestion> examQuestionList;// �½���Ŀ
				examQuestionList = examSection.getExamQuestionList();
				if (examSection.getQuestionType().getType_name().equals(DefaultValues.SINGLE_CHOICE)) {// ����ǵ���ѡ����
					sinRequest = examSection.getRequest();// ��ȡ��ĿҪ��
					for (ExamQuestion examQuestion : examQuestionList) {
						// �����ݿ���ȡ����ѡ�Ⲣ��ӵ�singleChoiceList
						singleChoiceList.add(singleChoiceDao.load(examQuestion.getQuestion_id()));
					}
				} else if (examSection.getQuestionType().getType_name().equals(DefaultValues.MULTI_CHOICE)) {// ����Ƕ���ѡ����
					mulRequest = examSection.getRequest();// ��ȡ��ĿҪ��
					for (ExamQuestion examQuestion : examQuestionList) {
						// �����ݿ���ȡ����ѡ�Ⲣ��ӵ�multiChoiceList
						multiChoiceList.add(multiChoiceDao.load(examQuestion.getQuestion_id()));
					}
				} else if (examSection.getQuestionType().getType_name().equals(DefaultValues.MATERIAL_ANALYSIS)) {// ����ǲ�����
					materRequest = examSection.getRequest();// ��ȡ��ĿҪ��
					for (ExamQuestion examQuestion : examQuestionList) {
						// �����ݿ���ȡ�������Ⲣ��ӵ�materialAnalysiList
						materialAnalysiList.add(materialAnalysisDao.load(examQuestion.getQuestion_id()));
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// ȡ������֮������ViewPager��adapter
			myExamAdapter = new ExamAdapter(getSupportFragmentManager());
			mViewPager = ((ViewPager) findViewById(R.id.pager));
			mViewPager.setAdapter(myExamAdapter);
			for (MultiChoice multiChoice : multiChoiceList) {
				System.out.println(multiChoice.getRightAnswer());
			}
			DialogTools.showPositiveDialog(ExamActivity.this, "�Ծ�Ҫ��", mExamination.getExam_request());// ��ʾ�Ծ�Ҫ��Ի���
		}
	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�ExamAdapter   
	* ��������    �Ծ��Adapter�����ݲ�ͬ�������½���ͬ��Fragment������ʾ��ViewPager��
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-2 ����7:05:11   
	*
	 */
	public class ExamAdapter extends FragmentPagerAdapter {
		int sinCount = singleChoiceList.size();// ��ѡ������
		int mulCount = multiChoiceList.size();// ��ѡ������
		int materCount = materialAnalysiList.size();// ����������
		int count = sinCount + mulCount + materCount;// ����Ŀ��

		public ExamAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			if (position < sinCount) {// ����ǵ�ѡ��
				SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
				Bundle localBundle = new Bundle();

				// ����ǵ�һ����ѡ�⣬����ʾ��ĿҪ��
				if (position == 0)
					DialogTools.showPositiveDialog(ExamActivity.this, "��ĿҪ��", sinRequest);

				// ����һ��singleChoice����
				localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE,
						(Serializable) singleChoiceList.get(position));
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
				mySingleChoiceFragment.setArguments(localBundle);
				return mySingleChoiceFragment;

			} else if (position >= sinCount && position < sinCount + mulCount) {// ����Ƕ�ѡ��
				MultiChoiceFragment myMultiChoiceFragment = new MultiChoiceFragment();
				Bundle bundle = new Bundle();

				// ����ǵ�һ����ѡ�⣬����ʾ��ĿҪ��
				if (position == sinCount + 1)
					DialogTools.showPositiveDialog(ExamActivity.this, "��ĿҪ��", mulRequest);

				// ����һ��MultiChoice����
				bundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE,
						(Serializable) multiChoiceList.get(position - sinCount));
				bundle.putInt(DefaultKeys.BUNDLE_MULTI_CHOICE_POSITION, position + 1);
				myMultiChoiceFragment.setArguments(bundle);
				return myMultiChoiceFragment;

			} else if (position >= sinCount + mulCount && position < count) {// ����ǲ��Ϸ�����
				MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
				Bundle bundle = new Bundle();

				// ����ǵ�һ�������⣬����ʾ��ĿҪ��
				if (position == sinCount + mulCount + 1)
					DialogTools.showPositiveDialog(ExamActivity.this, "��ĿҪ��", materRequest);

				// ����һ��MaterialAnalysis����
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

		// ���ر���
		public CharSequence getPageTitle(int position) {
			if (position < sinCount) {// ����ǵ�ѡ��
				return DefaultValues.SINGLE_CHOICE + (position + 1);

			} else if (position >= sinCount && position < sinCount + mulCount) {// ����Ƕ�ѡ��
				return DefaultValues.MULTI_CHOICE + (position + 1);

			} else if (position >= sinCount + mulCount && position < count) {// ����ǲ��Ϸ�����

				return DefaultValues.MATERIAL_ANALYSIS + (position + 1);
			} else {
				return null;
			}
		}

	}
}
