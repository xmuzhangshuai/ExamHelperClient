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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�MockExamActivity   
* ��������   ģ�⿼��Activity����ViewPager���֣������������� DefaultValues.MODEL_MOCK_EXAM����
*          ��չʾΪģ�⿼��ģʽ��������ʾ�𰸡�ʵ��OnAnswerChangedListener�Զ���ӿڣ��������մ���Ŀҳ�洫�����û��𰸡�
* �����ˣ���˧  
* ����ʱ�䣺2014-1-10 ����10:54:06   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-10 ����10:54:06   
* �޸ı�ע��   
* @version    
*    
*/
public class MockExamActivity extends BaseFragmentActivity implements OnClickListener, OnAnswerChangedListener {

	/************Views*************/
	private ViewPager mViewPager;
	private ExamAdapter myExamAdapter;// ����һ��pagerAdapter
	private TextView lastQuestion;// ��һ��
	private TextView nextQuestion;// ��һ��
	private TextView handIn;// �Ͻ��Ծ�
	private TextView preview;// Ԥ��
	private Chronometer chronometer;// ��ʱ��

	/***********�Ծ�����**************/
	private Examination mExamination;// �Ծ��ʵ��
	private List<SingleChoice> singleChoiceList = new ArrayList<SingleChoice>();// ��ѡ��List
	private List<MultiChoice> multiChoiceList = new ArrayList<MultiChoice>();// ��ѡ��List
	private List<MaterialAnalysis> materialAnalysiList = new ArrayList<MaterialAnalysis>();// ���Ϸ�����List
	private String sinRequest;// ��ѡ����ĿҪ��
	private String mulRequest;// ��ѡ����ĿҪ��
	private String materRequest;// ��������ĿҪ��
	private int singleNum = 6;// ��������Ծ�ѡ������,Ĭ��16��
	private int multiNum = 7;// ��������Ծ�ѡ��������Ĭ��17�⣬
	private int materialNum = 0;// ��������Ծ�ѡ��������Ĭ��0��

	/**********�û�����***********/
	private boolean includeMaterial = false;// �Ƿ���������⣬trueΪ����,Ĭ�ϲ�����
	private Long examId;// �Ծ��ID,-1��ʾ��������Ծ�����-1��ʾ����ģ���Ծ��ID
	private String[] userAnswers;// �û������
	private String examTitle;// �Ծ���Ŀ

	// private String model = DefaultValues.MODEL_MOCK_EXAM;// ��Ŀҳ��ģʽ��Ĭ��Ϊģ�⿼��ģʽ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mock_exam);

		/*********�����Ծ����ͣ�Ĭ��Ϊ�������************/
		examId = getIntent().getLongExtra(DefaultKeys.KEY_MOCK_EXAM_ID, -1);
		/*********�����Ƿ���������⣬Ĭ�ϲ�����************/
		includeMaterial = getIntent().getBooleanExtra(DefaultKeys.KEY_MOCK_EXAM_INCLUDE, false);
		/*******������������⣬��Ĭ�������������*******/
		if (includeMaterial) {
			materialNum = 2;
		}

		findViewById();
		initView();

		if (examId == -1) {// ����������������
			setQuestionResquet();
			examTitle = "�������";
			new InitRandomExam().execute();
		} else {// ���������
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

		/*********��MockExamPreviewActivity������صĲ�����ֱ�Ӷ�λ�������Ŀ*********/
		if (requestCode == 1 && data != null) {
			int current = data.getIntExtra(DefaultKeys.KEY_MOCK_EXAM_PREVIEW_BACK, 0);
			mViewPager.setCurrentItem(current, true);
		}
		// if (requestCode == 2 && resultCode == 2 && data != null) {
		// model = data.getStringExtra(DefaultKeys.KEY_QUESTION_MODEL);
		// }
	}

	/*********�������ؼ�����ʾ�û����ڴ���*********/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.icon_warning).setTitle("��ܰ��ʾ").setMessage("������ģ�⿼�Թ����У������ʱ�˳���������Ϣ�����ܱ��档�Ƿ�����˳���")
					.setPositiveButton("��", backListener).setNegativeButton("��", backListener).show();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**�����Ի��������button����¼�*/
	DialogInterface.OnClickListener backListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
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
		getActionBar().setTitle("ģ�⿼��");
		lastQuestion.setOnClickListener(this);
		nextQuestion.setOnClickListener(this);
		handIn.setOnClickListener(this);
		preview.setOnClickListener(this);

		// �½�ʱ��ʼ��ʱ
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.start();

		// ����ʱ�������¼�
		chronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				// TODO Auto-generated method stub
				if (includeMaterial) {// ������������⣬����ĿΪ180����
					if (SystemClock.elapsedRealtime() - chronometer.getBase() > 180 * 60 * 1000) {
						chronometer.stop();
						handIn();
					}
				} else {// ��������������⣬��Ϊ60����
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
		case R.id.last_question:// ��һ��
			if (mViewPager.getCurrentItem() > 0) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
			}
			break;
		case R.id.next_question:// ��һ��
			if (mViewPager.getCurrentItem() < userAnswers.length) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
			}
			break;
		case R.id.hand_in:// ����
			handIn();
			// this.model = DefaultValues.MODEL_EXERCISE;
			break;
		case R.id.preview:// Ԥ��
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
	 * �ύ�Ծ�
	 */
	private void handIn() {
		String message = "";// չʾ��Ϣ
		int unFinishedCount = 0;// δ����Ŀ����

		/****ȡ��δ����Ŀ����*****/
		for (int i = 0; i < userAnswers.length; i++) {
			if (userAnswers[i] == null) {
				unFinishedCount++;
			}
		}

		if (unFinishedCount > 0) {
			message = "������" + unFinishedCount + "����Ŀû�������Ƿ��ύ�Ծ�";
		} else {
			message = "�Ƿ��ύ�Ծ�";
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon_warning).setTitle("��ܰ��ʾ").setMessage(message)
				.setPositiveButton("��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// ֹͣ��ʱ
						chronometer.stop();

						Intent intent = new Intent(MockExamActivity.this, MockExamResultActivity.class);
						intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_SINGLE_RESULT, getSingleResult());
						intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_MULTI_RESULT, getMultiResult());
						intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_TITLE, examTitle);
						startActivityForResult(intent, 2);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
				}).setNegativeButton("��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();

	}

	/**
	 * ���ص�ѡ���û��𰸵�����
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
	 * ���ض�ѡ���û��𰸵�����
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
	 * ��������Ծ�ʱ��Ĭ����ĿҪ��
	 */
	public void setQuestionResquet() {
		sinRequest = "����ѡ����:1~" + singleNum + "С�⣬ÿС��1�֣���" + singleNum + "�֡�����ÿ��������ĸ�ѡ���У�ֻ��һ��ѡ���Ƿ�����ĿҪ��ġ�";
		mulRequest = "����ѡ���⣺" + (singleNum + 1) + "~" + (singleNum + multiNum) + "С�⣬ÿС��2�֣���" + (multiNum * 2)
				+ "�֡�����ÿ��������ĸ�ѡ���У�����������ѡ���Ƿ�����ĿҪ��ġ�";
		materRequest = "�����⣺" + (singleNum + multiNum + 1) + "~" + (singleNum + multiNum + materialNum) + "С�⣬ÿС��10�֣���"
				+ (materialNum * 10) + "�֡�Ҫ������ѧ֪ʶ�������ϻش�����";
	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�InitExamContent   
	* ��������   ģ��������첽�����ڲ��࣬ͨ���Ծ��ID�������ݿ�������
	*          ���������֮����onPostExecute����������ViewPager��adapter
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-10 ����10:11:12   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-1-10 ����10:11:12   
	* �޸ı�ע��   
	* @version    
	*
	 */
	public class InitRealExamContent extends AsyncTask<Long, Void, Void> {
		DaoSession daoSession = BaseApplication.getDaoSession(MockExamActivity.this);
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
			examTitle = mExamination.getExam_name();

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
						// ����û�ѡ����������⣬�����ݿ���ȡ�������Ⲣ��ӵ�materialAnalysiList
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
			// ȡ������֮������ViewPager��adapter
			myExamAdapter = new ExamAdapter(getSupportFragmentManager());
			mViewPager = ((ViewPager) findViewById(R.id.pager));
			mViewPager.setAdapter(myExamAdapter);

			// ������Ŀ�����½�����
			userAnswers = new String[singleChoiceList.size() + multiChoiceList.size() + materialAnalysiList.size()];
		}
	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�InitRandomExam   
	* ��������   ��������Ծ��첽�����࣬������������ȡ����
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-11 ����9:39:26   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-1-11 ����9:39:26   
	* �޸ı�ע��   
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
			/*******���������ѡ��********/
			while (singleChoiceList.size() < singleNum) {
				SingleChoice single = singleChoiceService.getRandomSingleChoice();
				if (!singleChoiceList.contains(single)) {// ���û�����ɹ�
					singleChoiceList.add(single);
				}
			}

			/*******���������ѡ��********/
			while (multiChoiceList.size() < multiNum) {
				MultiChoice multi = multiChoiceService.getRandomMultiChoice();
				if (!multiChoiceList.contains(multi)) {// ���û�����ɹ�
					multiChoiceList.add(multi);
				}
			}

			/*******�������������********/
			while (materialAnalysiList.size() < materialNum) {
				MaterialAnalysis materialAnalysis = materialAnalysisService.getRandomMaterialAnalysis();
				if (!materialAnalysiList.contains(materialAnalysis)) {// ���û�����ɹ�
					materialAnalysiList.add(materialAnalysis);
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

			progressDialog.dismiss();

			// ������Ŀ�����½�����
			userAnswers = new String[singleChoiceList.size() + multiChoiceList.size() + materialAnalysiList.size()];
		}

	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�ExamAdapter   
	* ��������   �Ծ��Adapter�����ݲ�ͬ�������½���ͬ��Fragment������ʾ��ViewPager��
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-10 ����10:14:48   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-1-10 ����10:14:48   
	* �޸ı�ע��   
	* @version    
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
					DialogTools.showPositiveDialog(MockExamActivity.this, "��ĿҪ��", sinRequest);

				// ����һ��singleChoice����
				localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE,
						(Serializable) singleChoiceList.get(position));
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
				localBundle.putString(DefaultKeys.KEY_QUESTION_MODEL, DefaultValues.MODEL_MOCK_EXAM);
				mySingleChoiceFragment.setArguments(localBundle);
				return mySingleChoiceFragment;

			} else if (position >= sinCount && position < sinCount + mulCount) {// ����Ƕ�ѡ��
				MultiChoiceFragment myMultiChoiceFragment = new MultiChoiceFragment();
				Bundle bundle = new Bundle();

				// ����ǵ�һ����ѡ�⣬����ʾ��ĿҪ��
				if (position == sinCount + 1)
					DialogTools.showPositiveDialog(MockExamActivity.this, "��ĿҪ��", mulRequest);

				// ����һ��MultiChoice����
				bundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE,
						(Serializable) multiChoiceList.get(position - sinCount));
				bundle.putInt(DefaultKeys.BUNDLE_MULTI_CHOICE_POSITION, position + 1);
				bundle.putString(DefaultKeys.KEY_QUESTION_MODEL, DefaultValues.MODEL_MOCK_EXAM);
				myMultiChoiceFragment.setArguments(bundle);
				return myMultiChoiceFragment;

			} else if (position >= sinCount + mulCount && position < count) {// ����ǲ��Ϸ�����
				MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
				Bundle bundle = new Bundle();

				// ����ǵ�һ�������⣬����ʾ��ĿҪ��
				if (position == sinCount + mulCount + 1)
					DialogTools.showPositiveDialog(MockExamActivity.this, "��ĿҪ��", materRequest);

				// ����һ��MaterialAnalysis����
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

	@Override
	public void onAnswerChanged(int questionNumber, String answer) {
		// TODO Auto-generated method stub
		userAnswers[questionNumber] = answer;
	}

}
