package com.bishe.examhelper.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.TextView;

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
import com.bishe.examhelper.dbService.MaterialAnalysisService;
import com.bishe.examhelper.dbService.MultiChoiceService;
import com.bishe.examhelper.dbService.SingleChoiceService;
import com.bishe.examhelper.entities.ExamQuestion;
import com.bishe.examhelper.entities.ExamSection;
import com.bishe.examhelper.entities.Examination;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.interfaces.OnAnswerChangedListener;
import com.bishe.examhelper.utils.DialogTools;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：MockExamActivity   
* 类描述：   模拟考试Activity，以ViewPager呈现，传给各种题型 DefaultValues.MODEL_MOCK_EXAM参数
*          以展示为模拟考试模式，不能显示答案。实现OnAnswerChangedListener自定义接口，用来接收从题目页面传来的用户答案。
* 创建人：张帅  
* 创建时间：2014-1-10 下午10:54:06   
* 修改人：张帅   
* 修改时间：2014-1-10 下午10:54:06   
* 修改备注：   
* @version    
*    
*/
public class MockExamActivity extends BaseFragmentActivity implements OnClickListener, OnAnswerChangedListener {

	/************Views*************/
	private ViewPager mViewPager;
	private ExamAdapter myExamAdapter;// 定义一个pagerAdapter
	private TextView lastQuestion;// 上一题
	private TextView nextQuestion;// 下一题
	private TextView handIn;// 上交试卷
	private TextView preview;// 预览
	private Chronometer chronometer;// 计时器

	/***********试卷内容**************/
	private Examination mExamination;// 试卷的实体
	private List<SingleChoice> singleChoiceList = new ArrayList<SingleChoice>();// 单选题List
	private List<MultiChoice> multiChoiceList = new ArrayList<MultiChoice>();// 多选题List
	private List<MaterialAnalysis> materialAnalysiList = new ArrayList<MaterialAnalysis>();// 材料分析题List
	private String sinRequest;// 单选题题目要求
	private String mulRequest;// 多选题题目要求
	private String materRequest;// 材料题题目要求
	private int singleNum = 6;// 随机生成试卷单选题数量,默认16题
	private int multiNum = 7;// 随机生成试卷单选题数量，默认17题，
	private int materialNum = 0;// 随机生成试卷单选题数量，默认0题

	/**********用户内容***********/
	private boolean includeMaterial = false;// 是否包含材料题，true为包含,默认不包含
	private Long examId;// 试卷的ID,-1表示随机生成试卷，大于-1表示整体模拟试卷的ID
	private String[] userAnswers;// 用户做题答案
	private String examTitle;// 试卷题目

	// private String model = DefaultValues.MODEL_MOCK_EXAM;// 题目页面模式，默认为模拟考试模式

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mock_exam);

		/*********接收试卷类型，默认为随机生成************/
		examId = getIntent().getLongExtra(DefaultKeys.KEY_MOCK_EXAM_ID, -1);
		/*********接收是否包含材料题，默认不包含************/
		includeMaterial = getIntent().getBooleanExtra(DefaultKeys.KEY_MOCK_EXAM_INCLUDE, false);
		/*******如果包含材料题，则默认随机生成两个*******/
		if (includeMaterial) {
			materialNum = 2;
		}

		findViewById();
		initView();

		if (examId == -1) {// 如果是随机生成试题
			setQuestionResquet();
			examTitle = "随机生成";
			new InitRandomExam().execute();
		} else {// 如果是真题
			new InitRealExamContent().execute(examId);
		}

	}

	// @Override
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		/*********从MockExamPreviewActivity点击传回的参数，直接定位到这个题目*********/
		if (requestCode == 1 && data != null) {
			int current = data.getIntExtra(DefaultKeys.KEY_MOCK_EXAM_PREVIEW_BACK, 0);
			mViewPager.setCurrentItem(current, true);
		}
		// if (requestCode == 2 && resultCode == 2 && data != null) {
		// model = data.getStringExtra(DefaultKeys.KEY_QUESTION_MODEL);
		// }
	}

	/*********监听返回键，提示用户正在答题*********/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.icon_warning).setTitle("温馨提示").setMessage("您正在模拟考试过程中，如果此时退出，考试信息将不能保存。是否继续退出？")
					.setPositiveButton("是", backListener).setNegativeButton("否", backListener).show();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**监听对话框里面的button点击事件*/
	DialogInterface.OnClickListener backListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mViewPager = ((ViewPager) findViewById(R.id.pager));
		lastQuestion = (TextView) findViewById(R.id.last_question);
		nextQuestion = (TextView) findViewById(R.id.next_question);
		handIn = (TextView) findViewById(R.id.hand_in);
		preview = (TextView) findViewById(R.id.preview);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("模拟考试");
		lastQuestion.setOnClickListener(this);
		nextQuestion.setOnClickListener(this);
		handIn.setOnClickListener(this);
		preview.setOnClickListener(this);

		// 新建时开始计时
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.start();

		// 给计时器设置事件
		chronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				// TODO Auto-generated method stub
				if (includeMaterial) {// 如果包含材料题，则题目为180分钟
					if (SystemClock.elapsedRealtime() - chronometer.getBase() > 180 * 60 * 1000) {
						chronometer.stop();
						handIn();
					}
				} else {// 如果不包含材料题，则为60分钟
					if (SystemClock.elapsedRealtime() - chronometer.getBase() > 60 * 60 * 1000) {
						chronometer.stop();
						handIn();
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.last_question:// 上一题
			if (mViewPager.getCurrentItem() > 0) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
			}
			break;
		case R.id.next_question:// 下一题
			if (mViewPager.getCurrentItem() < userAnswers.length) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
			}
			break;
		case R.id.hand_in:// 交卷
			handIn();
			// this.model = DefaultValues.MODEL_EXERCISE;
			break;
		case R.id.preview:// 预览
			Intent intent = new Intent(MockExamActivity.this, MockExamPreviewActivity.class);
			intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_PREVIEW_LIST, userAnswers);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		default:
			break;
		}
	}

	/**
	 * 提交试卷
	 */
	private void handIn() {
		String message = "";// 展示消息
		int unFinishedCount = 0;// 未做题目数量

		/****取得未做题目数量*****/
		for (int i = 0; i < userAnswers.length; i++) {
			if (userAnswers[i] == null) {
				unFinishedCount++;
			}
		}

		if (unFinishedCount > 0) {
			message = "您还有" + unFinishedCount + "道题目没有做，是否提交试卷？";
		} else {
			message = "是否提交试卷？";
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon_warning).setTitle("温馨提示").setMessage(message)
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// 停止计时
						chronometer.stop();

						Intent intent = new Intent(MockExamActivity.this, MockExamResultActivity.class);
						intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_SINGLE_RESULT, getSingleResult());
						intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_MULTI_RESULT, getMultiResult());
						intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_TITLE, examTitle);
						startActivityForResult(intent, 2);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
				}).setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();

	}

	/**
	 * 返回单选题用户答案的正误
	 * @return
	 */
	private boolean[] getSingleResult() {
		boolean[] result = new boolean[singleChoiceList.size()];
		for (int i = 0; i < singleChoiceList.size(); i++) {
			if (singleChoiceList.get(i).getAnswer().equals(userAnswers[i])) {
				result[i] = true;
			} else {
				result[i] = false;
			}
		}
		return result;
	}

	/**
	 * 返回多选题用户答案的正误
	 * @return
	 */
	private boolean[] getMultiResult() {
		boolean[] result = new boolean[multiChoiceList.size()];
		for (int i = 0; i < multiChoiceList.size(); i++) {
			if (multiChoiceList.get(i).getRightAnswer().equals(userAnswers[i + singleChoiceList.size()])) {
				result[i] = true;
			} else {
				result[i] = false;
			}
		}
		return result;
	}

	/**
	 * 随机生成试卷时的默认题目要求
	 */
	public void setQuestionResquet() {
		sinRequest = "单项选择题:1~" + singleNum + "小题，每小题1分，共" + singleNum + "分。下列每题给出的四个选项中，只有一个选项是符合题目要求的。";
		mulRequest = "多项选择题：" + (singleNum + 1) + "~" + (singleNum + multiNum) + "小题，每小题2分，共" + (multiNum * 2)
				+ "分。下列每题给出的四个选项中，至少有两个选项是符合题目要求的。";
		materRequest = "分析题：" + (singleNum + multiNum + 1) + "~" + (singleNum + multiNum + materialNum) + "小题，每小题10分，共"
				+ (materialNum * 10) + "分。要求结合所学知识分析材料回答问题";
	}

	/**
	 * 
	*    
	* 项目名称：ExamHelper   
	* 类名称：InitExamContent   
	* 类描述：   模拟真题的异步任务内部类，通过试卷的ID查找数据库中数据
	*          并且在完成之后在onPostExecute方法中设置ViewPager的adapter
	* 创建人：张帅  
	* 创建时间：2014-1-10 上午10:11:12   
	* 修改人：张帅   
	* 修改时间：2014-1-10 上午10:11:12   
	* 修改备注：   
	* @version    
	*
	 */
	public class InitRealExamContent extends AsyncTask<Long, Void, Void> {
		DaoSession daoSession = BaseApplication.getDaoSession(MockExamActivity.this);
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
			examTitle = mExamination.getExam_name();

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
						// 如果用户选择包含材料题，从数据库中取出材料题并添加到materialAnalysiList
						if (includeMaterial) {
							materialAnalysiList.add(materialAnalysisDao.load(examQuestion.getQuestion_id()));
						}
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

			// 根据题目数量新建数组
			userAnswers = new String[singleChoiceList.size() + multiChoiceList.size() + materialAnalysiList.size()];
		}
	}

	/**
	 * 
	*    
	* 项目名称：ExamHelper   
	* 类名称：InitRandomExam   
	* 类描述：   随机生成试卷异步任务类，从题库中随机抽取试题
	* 创建人：张帅  
	* 创建时间：2014-1-11 上午9:39:26   
	* 修改人：张帅   
	* 修改时间：2014-1-11 上午9:39:26   
	* 修改备注：   
	* @version    
	*
	 */
	public class InitRandomExam extends AsyncTask<Void, Void, Void> {
		Dialog progressDialog;
		SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(MockExamActivity.this);
		MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(MockExamActivity.this);
		MaterialAnalysisService materialAnalysisService = MaterialAnalysisService.getInstance(MockExamActivity.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = MockExamActivity.this.showProgressDialog();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			/*******随机产生单选题********/
			while (singleChoiceList.size() < singleNum) {
				SingleChoice single = singleChoiceService.getRandomSingleChoice();
				if (!singleChoiceList.contains(single)) {// 如果没有生成过
					singleChoiceList.add(single);
				}
			}

			/*******随机产生多选题********/
			while (multiChoiceList.size() < multiNum) {
				MultiChoice multi = multiChoiceService.getRandomMultiChoice();
				if (!multiChoiceList.contains(multi)) {// 如果没有生成过
					multiChoiceList.add(multi);
				}
			}

			/*******随机产生材料题********/
			while (materialAnalysiList.size() < materialNum) {
				MaterialAnalysis materialAnalysis = materialAnalysisService.getRandomMaterialAnalysis();
				if (!materialAnalysiList.contains(materialAnalysis)) {// 如果没有生成过
					materialAnalysiList.add(materialAnalysis);
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

			progressDialog.dismiss();

			// 根据题目数量新建数组
			userAnswers = new String[singleChoiceList.size() + multiChoiceList.size() + materialAnalysiList.size()];
		}

	}

	/**
	 * 
	*    
	* 项目名称：ExamHelper   
	* 类名称：ExamAdapter   
	* 类描述：   试卷的Adapter，根据不同的题型新建不同的Fragment，以显示在ViewPager中
	* 创建人：张帅  
	* 创建时间：2014-1-10 上午10:14:48   
	* 修改人：张帅   
	* 修改时间：2014-1-10 上午10:14:48   
	* 修改备注：   
	* @version    
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
					DialogTools.showPositiveDialog(MockExamActivity.this, "题目要求", sinRequest);

				// 传入一个singleChoice对象
				localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE,
						(Serializable) singleChoiceList.get(position));
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
				localBundle.putString(DefaultKeys.KEY_QUESTION_MODEL, DefaultValues.MODEL_MOCK_EXAM);
				mySingleChoiceFragment.setArguments(localBundle);
				return mySingleChoiceFragment;

			} else if (position >= sinCount && position < sinCount + mulCount) {// 如果是多选题
				MultiChoiceFragment myMultiChoiceFragment = new MultiChoiceFragment();
				Bundle bundle = new Bundle();

				// 如果是第一个多选题，则显示题目要求
				if (position == sinCount + 1)
					DialogTools.showPositiveDialog(MockExamActivity.this, "题目要求", mulRequest);

				// 传入一个MultiChoice对象
				bundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE,
						(Serializable) multiChoiceList.get(position - sinCount));
				bundle.putInt(DefaultKeys.BUNDLE_MULTI_CHOICE_POSITION, position + 1);
				bundle.putString(DefaultKeys.KEY_QUESTION_MODEL, DefaultValues.MODEL_MOCK_EXAM);
				myMultiChoiceFragment.setArguments(bundle);
				return myMultiChoiceFragment;

			} else if (position >= sinCount + mulCount && position < count) {// 如果是材料分析题
				MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
				Bundle bundle = new Bundle();

				// 如果是第一个材料题，则显示题目要求
				if (position == sinCount + mulCount + 1)
					DialogTools.showPositiveDialog(MockExamActivity.this, "题目要求", materRequest);

				// 传入一个MaterialAnalysis对象
				bundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS,
						(Serializable) materialAnalysiList.get(position - sinCount - mulCount));
				bundle.putInt(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS_POSITION, position + 1);
				bundle.putString(DefaultKeys.KEY_QUESTION_MODEL, DefaultValues.MODEL_MOCK_EXAM);
				materialAnalysisFragment.setArguments(bundle);
				return materialAnalysisFragment;

			} else {
				return null;
			}

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (includeMaterial) {
				return count;
			} else {
				return sinCount + mulCount;
			}

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

	@Override
	public void onAnswerChanged(int questionNumber, String answer) {
		// TODO Auto-generated method stub
		userAnswers[questionNumber] = answer;
	}

}
