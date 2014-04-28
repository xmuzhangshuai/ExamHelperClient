package com.bishe.examhelper.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseQuestionFragment;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.interfaces.OnAnswerChangedListener;
import com.bishe.examhelper.service.CollectionService;
import com.bishe.examhelper.service.ErrorQuestionsService;
import com.bishe.examhelper.service.SingleChoiceService;
import com.bishe.examhelper.service.StudyRecordService;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.umeng.socialize.controller.UMWXHandler;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;

/**   
 *    
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�SingleChoiceFragment   
 * ��������   ��ѡ��Fragment������һ����ѡ���ҳ���ExerciseActivity�е�ViewPager����ʾ
 * �����ˣ���˧     
 * ����ʱ�䣺2013-12-14 ����10:37:20   
 * �޸��ˣ���˧     
 * �޸�ʱ�䣺2013-12-15 ����3:47:16   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class SingleChoiceFragment extends BaseQuestionFragment implements View.OnClickListener {
	// ������ữ�������
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

	/************��Ŀ����*************/
	private SingleChoice mysingleChoice;// ��ѡ��
	private int questionNumber;// ���
	private String myAnswer;// �ҵĴ�
	private int myCheckedId;// ѡ���RadioButtonID
	private boolean isAnswered = false;// �ش��־��true��ʾ�ѻش�false��ʾû�ش�

	/*************Views************/
	private View rootView;// ��View
	private TextView single_choice_stem;// ���
	private RadioGroup mRadioGroup;// RadioGroup
	private RadioButton optionA;// ѡ��A
	private RadioButton optionB;// ѡ��B
	private RadioButton optionC;// ѡ��C
	private RadioButton optionD;// ѡ��D
	private ViewStub viewStub;// ������Stub
	private LinearLayout buttonArea;// ��ť����������ӱʼǰ�ť���ղذ�ť������ť������ť
	private TextView addnoteButton;// ��ӱʼǰ�ť
	private CheckBox collectButton;// �ղذ�ť
	private TextView shareButton;// ����ť
	private Button submitButton;// ��ʾ�𰸰�ť

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

		// �Ӱ󶨲�����ȡ��SingleChoice����
		mysingleChoice = (SingleChoice) getArguments().getSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE);
		questionNumber = (int) getArguments().getInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = getActivity().getLayoutInflater().inflate(R.layout.single_choice, null, false);
		findViewById();

		// �����չʾģʽ�������û���
		if (model.equals(DefaultValues.MODEL_DISPLAY)) {
			myAnswer = (String) getArguments().getString(DefaultKeys.KEY_USER_ANSWER, "A");
			myCheckedId = getIdByString(myAnswer);
		}

		// ��ʼ����Ŀ����
		initQuestionView();

		// �������ϰģʽ��������չʾģʽ������ʾ��ť��
		if (model.equals(DefaultValues.MODEL_EXERCISE) || model.equals(DefaultValues.MODEL_DISPLAY)) {
			initButtonView();
		}

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// ����û����ò�������ʾ��,������ʾ�𰸰�ť
		if (!isCheckNow && model.equals(DefaultValues.MODEL_EXERCISE)) {
			submitButton.setVisibility(View.VISIBLE);
			submitButton.setOnClickListener(this);
			submitButton.setEnabled(false);
		} else {
			submitButton.setVisibility(View.GONE);
		}

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

	/**
	 * ��ʼ����Ŀ����
	 */
	@Override
	protected void initQuestionView() {
		/************������Ŀ����**************/
		this.single_choice_stem.setText("    " + questionNumber + ".  " + mysingleChoice.getQuestion_stem());// �������
		this.optionA.setText(mysingleChoice.getOptionA());// ����ѡ��A
		this.optionB.setText(mysingleChoice.getOptionB());// ����ѡ��B
		this.optionC.setText(mysingleChoice.getOptionC());// ����ѡ��C
		this.optionD.setText(mysingleChoice.getOptionD());// ����ѡ��D

		// ��RadiobuttonGroup�����¼�
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				myCheckedId = checkedId;
				// ���Ϊ��ϰģʽ���û�����Ϊ������ʾ��
				if (isCheckNow && model.equals(DefaultValues.MODEL_EXERCISE)) {
					// ���û�лش�������ж�����
					if (!isAnswered) {
						judgeAnswer();
						record();
					}
					isAnswered = true;// ��־�ѻش�
					displayAnswer();

					optionA.setEnabled(false);// ����ѡ�ť������
					optionB.setEnabled(false);// ����ѡ�ť������
					optionC.setEnabled(false);// ����ѡ�ť������
					optionD.setEnabled(false);// ����ѡ�ť������
				} else if (!isCheckNow && model.equals(DefaultValues.MODEL_EXERCISE)) {// ���Ϊ��ϰģʽ���û�����Ϊ��������ʾ��
					// ������ʾ�𰸰�ť����
					submitButton.setEnabled(true);
				} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// ���Ϊģ�⿼��ģʽ
					/********���𰸴��ظ�MockExamActivity***********/
					mAnswerChangedListener.onAnswerChanged(questionNumber - 1, getStringByCheckedId(checkedId));
				}
			}
		});

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
		if (collectionService.isCollected(mysingleChoice)) {
			collectButton.setText("ȡ���ղ�");
			this.collectButton.setChecked(true);
		}

		// ���ղذ�ť���¼�
		this.collectButton.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					collectButton.setText("ȡ���ղ�");
					collectionService.insertCollection(mysingleChoice);
					new Thread() {
						public void run() {
							collectionService.addCollectionToNet(mysingleChoice);
						};
					}.start();

					Toast.makeText(getActivity(), "�ղسɹ���", 1).show();
				} else {
					collectionService.deleteCollection();
					new Thread() {
						public void run() {
							collectionService.delCollectionFromNet(mysingleChoice);
						};
					}.start();
					collectButton.setText("�ղ�");
				}
			}
		});

		// ������ť���¼�
		this.shareButton.setOnClickListener(this);

		// ����ӱʼǰ�ť���¼�
		this.addnoteButton.setOnClickListener(this);
	}

	/**
	 * �жϴ�����
	 */
	@Override
	protected void judgeAnswer() {
		// ����𰸲���ȷ�������һ�������¼
		if (!getStringByCheckedId(myCheckedId).equals(mysingleChoice.getAnswer().trim())) {
			// �������ϰģʽ�����������ѡ���Ƿ���
			if (model.equals(DefaultValues.MODEL_EXERCISE)) {
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
				boolean vibrator = sharedPreferences.getBoolean(DefaultKeys.KEY_PREF_VIBRATE_AFTER, false);
				if (vibrator) {
					Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(100);
				}
			}
			/************��������¼*************/
			final ErrorQuestionsService errorQuestionsService = ErrorQuestionsService.getInstance(getActivity());
			errorQuestionsService.addErrorQuestions(mysingleChoice);
			new Thread() {
				public void run() {
					errorQuestionsService.addErrorQuestionsToNet(mysingleChoice);
				};
			}.start();

		}
	}

	/**
	 * ��¼
	 */
	protected void record() {
		boolean isRight = false;
		if (getStringByCheckedId(myCheckedId).equals(mysingleChoice.getAnswer().trim())) {
			isRight = true;
		}
		Question question = mysingleChoice;
		question.setQuestion_type(DefaultValues.SINGLE_CHOICE);
		// ����ѧϰ��¼
		StudyRecordService studyRecordService = StudyRecordService.getInstance(getActivity());
		studyRecordService.insertStudyRecord(question, getStringByCheckedId(myCheckedId), isRight);

		// ��־����
		mysingleChoice.setFlag(true);
		SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(getActivity());
		singleChoiceService.singleChoiceDao.update(mysingleChoice);
	}

	/**
	 * ��ʾ�𰸣�����ѡ��������ʾ�ͽ�����
	 */
	@Override
	protected void displayAnswer() {

		/**************���ô𰸽�����**************/
		viewStub.setVisibility(View.VISIBLE);// ���ý������ɼ�

		View view = rootView.findViewById(R.id.single_choice_inflaterId);// ��ý�����view
		TextView myAnswerTextView = (TextView) view.findViewById(R.id.my_answer);// �ҵĴ�
		TextView rightAnswerTextView = (TextView) view.findViewById(R.id.right_answer);// ��ȷ��
		ImageView rightorwrongImage = (ImageView) view.findViewById(R.id.image_wrong_or_right);// �ԺŴ��ͼ��
		TextView analysisTextView = (TextView) view.findViewById(R.id.analysis);// �𰸽���

		// �������ȷ��
		if (getStringByCheckedId(myCheckedId).equals(mysingleChoice.getAnswer().trim())) {
			rightorwrongImage.setBackgroundResource(R.drawable.image_right);// ��ʾ�Ժ�
			myAnswerTextView.setTextColor(getResources().getColor(R.color.rightcolor));// �ҵĴ���ʾ��ɫ
			myAnswerTextView.setText("�ҵĴ𰸣�  " + getStringByCheckedId(myCheckedId) + "������ˣ���");// ��ʾ�ҵĴ�
		} else {
			rightorwrongImage.setBackgroundResource(R.drawable.image_wrong);// ��ʾ���
			myAnswerTextView.setTextColor(getResources().getColor(R.color.wrongcolor));// �ҵĴ���ʾ��ɫ
			myAnswerTextView.setText("�ҵĴ𰸣�  " + getStringByCheckedId(myCheckedId) + "������ˣ���");// ��ʾ�ҵĴ�
		}

		rightAnswerTextView.setText("��ȷ���ǣ�" + mysingleChoice.getAnswer());// ��ʾ��ȷ��
		analysisTextView.setText(mysingleChoice.getAnalysis());// ��ʾ�𰸽���
		/**************���ô𰸽���������**************/

		/**************����ѡ����ʽ**************/
		RadioButton myOptionButton = (RadioButton) rootView.findViewById(myCheckedId);// �ҵ�ѡ��
		// ���ѡ�������ȷ��
		if (getStringByCheckedId(myCheckedId).equals(mysingleChoice.getAnswer().trim())) {
			myOptionButton.setTextColor(getResources().getColor(R.color.rightcolor));// ѡ������Ϊ��ɫ
			myOptionButton.setButtonDrawable(R.drawable.selector_radio_true);// ����RadioButtonͼ��
		} else {
			myOptionButton.setTextColor(getResources().getColor(R.color.wrongcolor));// ѡ������Ϊ��ɫ
			myOptionButton.setButtonDrawable(R.drawable.selector_radio_false);// ����RadioButtonͼ��

			// ȡ����ȷѡ��
			RadioButton rightOptionButton = (RadioButton) rootView.findViewById(getIdByString(mysingleChoice
					.getAnswer().trim()));
			rightOptionButton.setTextColor(getResources().getColor(R.color.rightcolor));// ѡ������Ϊ��ɫ
			rightOptionButton.setButtonDrawable(R.drawable.selector_radio_true);// ����RadioButtonͼ��
		}
		/**************����ѡ�����**************/

	}

	// ����view
	@Override
	protected void findViewById() {
		this.single_choice_stem = ((TextView) this.rootView.findViewById(R.id.single_choice_stem));// ��ѡ���
		this.mRadioGroup = ((RadioGroup) this.rootView.findViewById(R.id.singal_choice_optionGroup));// RadioGroup
		this.optionA = ((RadioButton) this.rootView.findViewById(R.id.optionA));// ѡ��A
		this.optionB = ((RadioButton) this.rootView.findViewById(R.id.optionB));// ѡ��B
		this.optionC = ((RadioButton) this.rootView.findViewById(R.id.optionC));// ѡ��C
		this.optionD = ((RadioButton) this.rootView.findViewById(R.id.optionD));// ѡ��D
		this.viewStub = ((ViewStub) this.rootView.findViewById(R.id.single_choice_answer_stub));// ������Stub
		this.buttonArea = (LinearLayout) this.rootView.findViewById(R.id.button_area);// ��ť��
		this.shareButton = ((TextView) this.rootView.findViewById(R.id.share_button));// ����ť
		this.collectButton = ((CheckBox) this.rootView.findViewById(R.id.collect_button));// �ղذ�ť
		this.addnoteButton = ((TextView) this.rootView.findViewById(R.id.addnote_button));// ��ӱʼǰ�ť
		this.submitButton = (Button) this.rootView.findViewById(R.id.submit_btn);// ��ʾ�𰸰�ť
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:// �ύ�𰸰�ť
			if (!isAnswered) {// ���û�лش�������ж�����
				judgeAnswer();
				record();
			}
			isAnswered = true;
			displayAnswer();
			submitButton.setVisibility(View.GONE);
			optionA.setEnabled(false);// ����ѡ�ť������
			optionB.setEnabled(false);// ����ѡ�ť������
			optionC.setEnabled(false);// ����ѡ�ť������
			optionD.setEnabled(false);// ����ѡ�ť������
			break;
		case R.id.share_button:// ����ť
			// ���÷�������
			mController.setShareContent(mysingleChoice.getQuestion_stem() + "\n" + "A " + mysingleChoice.getOptionA()
					+ "\n" + "B " + mysingleChoice.getOptionB() + "\n" + "C " + mysingleChoice.getOptionC() + "\n"
					+ "D " + mysingleChoice.getOptionD());
			// ���÷���ͼƬ������2Ϊ����ͼƬ����Դ����
			mController.setShareMedia(new UMImage(getActivity(), R.drawable.logo));

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
		case R.id.addnote_button:// ��ӱʼǰ�ť
			Intent intent = new Intent(getActivity(), AddNoteActivity.class);
			Question question = mysingleChoice;
			question.setQuestion_type(DefaultValues.SINGLE_CHOICE);
			intent.putExtra(DefaultKeys.BUNDLE_QUESTION, question);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;

		default:
			break;
		}

	}

	/**
	 * ����ѡ��ID���ض�Ӧ�Ĵ�
	 * @return
	 */
	public String getStringByCheckedId(int checkedId) {
		String checkedString = "";
		switch (checkedId) {
		case R.id.submit_btn:// �ύ�𰸰�ť
			break;
		case R.id.optionA:
			checkedString = "A";
			break;
		case R.id.optionB:
			checkedString = "B";
			break;
		case R.id.optionC:
			checkedString = "C";
			break;
		case R.id.optionD:
			checkedString = "D";
			break;
		default:
			break;
		}
		return checkedString;
	}

	/**
	 *  ����ѡ���ѡ��RadioButton��ID
	 * @param myanswer
	 * @return
	 */
	public int getIdByString(String myanswer) {
		switch (myanswer.charAt(0)) {
		default:
			return -1;
		case 'A':
			return this.optionA.getId();
		case 'B':
			return this.optionB.getId();
		case 'C':
			return this.optionC.getId();
		case 'D':
			return this.optionD.getId();
		}
	}

}
