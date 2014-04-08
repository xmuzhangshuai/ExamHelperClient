package com.bishe.examhelper.ui;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseQuestionFragment;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.interfaces.OnAnswerChangedListener;
import com.bishe.examhelper.service.CollectionService;
import com.bishe.examhelper.service.ErrorQuestionsService;
import com.bishe.examhelper.service.MultiChoiceService;
import com.bishe.examhelper.service.StudyRecordService;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.umeng.socialize.controller.UMWXHandler;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�MultiChoiceFragment   
* ��������   ��ѡ��Fragment�����ڳ�����ExerciseActivity��viewPager��
* �����ˣ���˧  
* ����ʱ�䣺2014-1-2 ����4:43:16   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-2 ����4:43:16   
* �޸ı�ע��   
* @version    
*    
*/
public class MultiChoiceFragment extends BaseQuestionFragment implements android.view.View.OnClickListener,
		OnCheckedChangeListener {
	// ������ữ�������
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

	/************��Ŀ����*************/
	private MultiChoice myMultiChoice;
	private String myAnswer = "";// �ҵĴ�
	private String rightAnswer = "";// ��ȷ��
	private int questionNumber;// ���
	private boolean isAnswered = false;// �ش��־��true��ʾ�ѻش�false��ʾû�ش�

	/*************Views************/
	private View rootView;// ��View
	private TextView multi_choice_stem;// ���
	private ViewStub viewStub;
	private CheckBox optionA;// ѡ��A
	private CheckBox optionB;// ѡ��B
	private CheckBox optionC;// ѡ��C
	private CheckBox optionD;// ѡ��D
	private Button submitButton;// �ύ�𰸰�ť
	private LinearLayout buttonArea;// ��ť����������ӱʼǰ�ť���ղذ�ť������ť������ť
	private CheckBox collect_btn;// �ղذ�ť
	private TextView addnote_btn;// ��ӱʼǰ�ť
	private TextView share_btn;// ����ť

	/**********MockExamActivity�ص��ӿ�**************/
	private OnAnswerChangedListener mAnswerChangedListener;

	/**************�����ģ�⿼��ģʽ���½�OnAnswerChangedListener�ص��ӿڣ�
	 * ���MockExamActivityû��ʵ��OnAnswerChangedListener�ӿڣ����׳��쳣**********/
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {
			try {
				mAnswerChangedListener = (OnAnswerChangedListener) activity;
			} catch (ClassCastException e) {
				throw new ClassCastException(activity.toString() + " ����ʵ��OnAnswerChangedListener�ӿ�");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// �Ӱ󶨲�����ȡ��MultiChoice����
		myMultiChoice = (MultiChoice) getArguments().getSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE);
		questionNumber = (int) getArguments().getInt(DefaultKeys.BUNDLE_MULTI_CHOICE_POSITION, 1);

		// �����չʾģʽ�������û���
		if (model.equals(DefaultValues.MODEL_DISPLAY)) {
			myAnswer = (String) getArguments().getString(DefaultKeys.KEY_USER_ANSWER, "A");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = getActivity().getLayoutInflater().inflate(R.layout.multi_choice, null, false);
		findViewById();
		initQuestionView();

		// �������ϰģʽ,������չʾģʽ������ʾ��ť��
		if (model.equals(DefaultValues.MODEL_EXERCISE) || model.equals(DefaultValues.MODEL_DISPLAY)) {
			initButtonView();
		} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// ����ǿ���ģʽ
			submitButton.setText("ȷ��");
			submitButton.setEnabled(false);
		}

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		submitButton.setEnabled(false);
		submitButton.setOnClickListener(this);

		// ����Ѿ��ش��,��������ϰģʽ����ʾ�𰸽���,������ʾ�𰸰�ť
		if (isAnswered && model.equals(DefaultValues.MODEL_EXERCISE)) {
			displayAnswer();
			submitButton.setVisibility(View.GONE);
		}

		// �����չʾģʽ����ֱ����ʾ�𰸺ͽ���
		if (model.equals(DefaultValues.MODEL_DISPLAY)) {
			submitButton.setVisibility(View.GONE);
			displayAnswer();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/**ʹ��SSO��Ȩ����������´��� */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * ��ʼ����Ŀ����
	 */
	@Override
	protected void initQuestionView() {
		/************������Ŀ����**************/
		this.multi_choice_stem.setText("    " + questionNumber + ".  " + myMultiChoice.getQuestion_stem());// �������
		this.optionA.setText(myMultiChoice.getOptionA());// ����ѡ��A
		this.optionB.setText(myMultiChoice.getOptionB());// ����ѡ��B
		this.optionC.setText(myMultiChoice.getOptionC());// ����ѡ��C
		this.optionD.setText(myMultiChoice.getOptionD());// ����ѡ��D
		optionA.setOnCheckedChangeListener(this);
		optionB.setOnCheckedChangeListener(this);
		optionC.setOnCheckedChangeListener(this);
		optionD.setOnCheckedChangeListener(this);
	}

	/**
	 * ��ʼ�� ��ť����
	 */
	@Override
	protected void initButtonView() {
		// ���ð�ť���ɼ�
		buttonArea.setVisibility(View.VISIBLE);

		// ȡ�á��ղ�ʵ�塱���ݿ⹤��ʵ��
		final CollectionService collectionService = CollectionService.getInstance(getActivity());

		// ����ѱ��ղأ����ղذ�ť��ʾ�ѱ��ղ�״̬
		if (collectionService.isCollected(myMultiChoice)) {
			collect_btn.setText("ȡ���ղ�");
			this.collect_btn.setChecked(true);
		}

		// ���ղذ�ť���¼�
		collect_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					collect_btn.setText("ȡ���ղ�");
					collectionService.insertCollection(myMultiChoice);
					Toast.makeText(getActivity(), "�ղسɹ���", 1).show();
				} else {
					collectionService.deleteCollection();
					collect_btn.setText("�ղ�");
				}
			}
		});

		addnote_btn.setOnClickListener(this);// ��ӱʼǰ�ť�����¼�
		share_btn.setOnClickListener(this);// ����ť�����¼�

	}

	/**
	 * ��ʾ�𰸣�����ѡ��������ʾ�ͽ�����
	 */
	@Override
	protected void displayAnswer() {
		viewStub.setVisibility(View.VISIBLE);// ���ý������ɼ�
		View view = rootView.findViewById(R.id.multi_choice_inflaterId);// ��ý�����view
		TextView myAnswerTextView = (TextView) view.findViewById(R.id.my_answer);// �ҵĴ�
		TextView rightAnswerTextView = (TextView) view.findViewById(R.id.right_answer);// ��ȷ��
		ImageView rightorwrongImage = (ImageView) view.findViewById(R.id.image_wrong_or_right);// �ԺŴ��ͼ��
		TextView analysisTextView = (TextView) view.findViewById(R.id.analysis);// �𰸽���

		/***�ж���ѡ��Ĵ�*****/
		if (!model.equals(DefaultValues.MODEL_DISPLAY)) {
			myAnswer = getMyAnswer();
		}

		/********�ж�ÿ��ѡ������󣬲�����ȷ�𰸱�����rightAnswer��********/
		rightAnswer = getRightAnswer();

		rightAnswerTextView.setText("��ȷ�𰸣�  " + rightAnswer);// ������ȷ��
		analysisTextView.setText(myMultiChoice.getAnalysis());// ���ô𰸽���
		// ���ý�������ɫ��ͼƬ
		if (rightAnswer.trim().equals(myAnswer.trim())) {// �����ѡ����ȷ
			myAnswerTextView.setText("�ҵĴ𰸣�  " + myAnswer + "����ȷ��");// �����ҵĴ�
			myAnswerTextView.setTextColor(getResources().getColor(R.color.rightcolor));// ��ɫ
			rightorwrongImage.setBackgroundResource(R.drawable.image_right);// ��ʾ�Ժ�
		} else {// �����ѡ�𰸴���
			myAnswerTextView.setText("�ҵĴ𰸣�  " + myAnswer + "������");// �����ҵĴ�
			myAnswerTextView.setTextColor(getResources().getColor(R.color.wrongcolor));// ��ɫ
			rightorwrongImage.setBackgroundResource(R.drawable.image_wrong);// ��ʾ���
		}
	}

	/**
	 * �жϴ�����
	 */
	@Override
	protected void judgeAnswer() {
		// ����ش���ȷ�����������¼
		if (!rightAnswer.trim().equals(myAnswer.trim())) {
			/************��������¼*************/
			ErrorQuestionsService errorQuestionsService = ErrorQuestionsService.getInstance(getActivity());
			errorQuestionsService.addErrorQuestions(myMultiChoice);
		}

	}

	/**
	 * ��¼
	 */
	protected void record() {
		boolean isRight = false;
		if (rightAnswer.trim().equals(myAnswer.trim())) {
			isRight = true;
		}
		Question question = myMultiChoice;
		question.setQuestion_type(DefaultValues.MULTI_CHOICE);
		// ����ѧϰ��¼
		StudyRecordService studyRecordService = StudyRecordService.getInstance(getActivity());
		studyRecordService.insertStudyRecord(question, getMyAnswer(), isRight);

		// ��־����
		myMultiChoice.setFlag(true);
		MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(getActivity());
		multiChoiceService.multiChoiceDao.update(myMultiChoice);
	}

	/**
	 *  ����View
	 */
	protected void findViewById() {
		viewStub = (ViewStub) rootView.findViewById(R.id.multi_choice_answer_stub);// ������Stub
		multi_choice_stem = (TextView) rootView.findViewById(R.id.multi_choice_stem);
		optionA = (CheckBox) rootView.findViewById(R.id.optionA);// ѡ��A
		optionB = (CheckBox) rootView.findViewById(R.id.optionB);// ѡ��B
		optionC = (CheckBox) rootView.findViewById(R.id.optionC);// ѡ��C
		optionD = (CheckBox) rootView.findViewById(R.id.optionD);// ѡ��D
		submitButton = (Button) rootView.findViewById(R.id.submit_btn);// �ύ�𰸰�ť
		this.buttonArea = (LinearLayout) this.rootView.findViewById(R.id.button_area);// ��ť��
		collect_btn = (CheckBox) rootView.findViewById(R.id.collect_button);// �ղذ�ť
		addnote_btn = (TextView) rootView.findViewById(R.id.addnote_button);// ��ӱʼǰ�ť
		share_btn = (TextView) rootView.findViewById(R.id.share_button);// ����ť
	};

	/**
	 * �����¼�
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:// ���������ύ�𰸰�ť
			if (model.equals(DefaultValues.MODEL_EXERCISE)) {// �������ϰģʽ
				displayAnswer();
				if (!isAnswered) {// ���û�лش�������ж�����
					judgeAnswer();
					record();
				}
				isAnswered = true;
				submitButton.setVisibility(View.GONE);
				optionA.setEnabled(false);// ����ѡ�ť������
				optionB.setEnabled(false);// ����ѡ�ť������
				optionC.setEnabled(false);// ����ѡ�ť������
				optionD.setEnabled(false);// ����ѡ�ť������
			} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// ����ǿ���ģʽ
				submitButton.setEnabled(false);
				record();
				/********���𰸴��ظ�MockExamActivity***********/
				mAnswerChangedListener.onAnswerChanged(questionNumber - 1, getMyAnswer());
			}
			break;
		case R.id.addnote_button:// ����������ӱʼǰ�ť
			Intent intent = new Intent(getActivity(), AddNoteActivity.class);
			Question question = myMultiChoice;
			question.setQuestion_type(DefaultValues.MULTI_CHOICE);
			intent.putExtra(DefaultKeys.BUNDLE_QUESTION, question);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.share_button:// �������˷���ť
			// ���÷�������
			mController.setShareContent(myMultiChoice.getQuestion_stem() + "\n" + "A " + myMultiChoice.getOptionA()
					+ "\n" + "B " + myMultiChoice.getOptionB() + "\n" + "C " + myMultiChoice.getOptionC() + "\n" + "D "
					+ myMultiChoice.getOptionD());
			// ���÷���ͼƬ������2Ϊ����ͼƬ����Դ����
			mController.setShareMedia(new UMImage(getActivity(), R.drawable.headimage));

			// ���΢��ƽ̨������1Ϊ��ǰActivity, ����2Ϊ�û������AppID, ����3Ϊ�������������ת����Ŀ��url
			UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(getActivity(),
					DefaultValues.WEIXIN_APP_ID, DefaultValues.WEIXIN_CONTENT_URL);
			wxHandler.setWXTitle("������ữ���������...");

			// ֧��΢������Ȧ
			UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(getActivity(),
					DefaultValues.WEIXIN_APP_ID, DefaultValues.WEIXIN_CONTENT_URL);
			circleHandler.setCircleTitle("������ữ���������...");

			// ����1Ϊ��ǰActivity�� ����2Ϊ�û������������ʱ��ת����Ŀ���ַ
			mController.getConfig().supportQQPlatform(getActivity(), DefaultValues.WEIXIN_CONTENT_URL);

			// ��������SSO handler
			mController.getConfig().setSsoHandler(new SinaSsoHandler());

			// ������Ѷ΢��SSO handler
			mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
			mController.openShare(getActivity(), false);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		submitButton.setEnabled(true);
	}

	/**
	 * ����ѡ����ҵĴ�
	 * @return
	 */
	public String getMyAnswer() {
		String myAnswer = "";
		// ���ѡ����Aѡ��
		if (optionA.isChecked() == true) {
			myAnswer = myAnswer + "A";// �ҵĴ����A
		}
		// ���ѡ����Bѡ��
		if (optionB.isChecked() == true) {
			myAnswer = myAnswer + "B";
		}
		// ���ѡ����Cѡ��
		if (optionC.isChecked() == true) {
			myAnswer = myAnswer + "C";
		}
		// ���ѡ����Dѡ��
		if (optionD.isChecked() == true) {
			myAnswer = myAnswer + "D";
		}
		return myAnswer;
	}

	/**
	 * �������ݿ����ݷ�����ȷ
	 * @return
	 */
	public String getRightAnswer() {
		String rightAnswer = "";
		if (myMultiChoice.getAnswerA() == true) {// ���A��ȷ
			rightAnswer = rightAnswer + "A";
			optionA.setTextColor(getResources().getColor(R.color.rightcolor));// ������ɫ
			optionA.setButtonDrawable(R.drawable.selector_multi_checkbox_true);// ����ѡ��ͼ��Ժ�
		} else {
			optionA.setTextColor(getResources().getColor(R.color.wrongcolor));// ���ú�ɫ
			optionA.setButtonDrawable(R.drawable.selector_multi_checkbox_false);// ����ѡ��ͼ����
		}

		if (myMultiChoice.getAnswerB() == true) {// ���B��ȷ
			rightAnswer = rightAnswer + "B";
			optionB.setTextColor(getResources().getColor(R.color.rightcolor));// ������ɫ
			optionB.setButtonDrawable(R.drawable.selector_multi_checkbox_true);// ����ѡ��ͼ��Ժ�
		} else {
			optionB.setTextColor(getResources().getColor(R.color.wrongcolor));// ���ú�ɫ
			optionB.setButtonDrawable(R.drawable.selector_multi_checkbox_false);// ����ѡ��ͼ����
		}

		if (myMultiChoice.getAnswerC() == true) {// ���C��ȷ
			rightAnswer = rightAnswer + "C";
			optionC.setTextColor(getResources().getColor(R.color.rightcolor));// ������ɫ
			optionC.setButtonDrawable(R.drawable.selector_multi_checkbox_true);// ����ѡ��ͼ��Ժ�
		} else {
			optionC.setTextColor(getResources().getColor(R.color.wrongcolor));// ���ú�ɫ
			optionC.setButtonDrawable(R.drawable.selector_multi_checkbox_false);// ����ѡ��ͼ����
		}

		if (myMultiChoice.getAnswerD() == true) {// ���D��ȷ
			rightAnswer = rightAnswer + "D";
			optionD.setTextColor(getResources().getColor(R.color.rightcolor));// ������ɫ
			optionD.setButtonDrawable(R.drawable.selector_multi_checkbox_true);// ����ѡ��ͼ��Ժ�
		} else {
			optionD.setTextColor(getResources().getColor(R.color.wrongcolor));// ���ú�ɫ
			optionD.setButtonDrawable(R.drawable.selector_multi_checkbox_false);// ����ѡ��ͼ����
		}

		return rightAnswer;
	}

}
