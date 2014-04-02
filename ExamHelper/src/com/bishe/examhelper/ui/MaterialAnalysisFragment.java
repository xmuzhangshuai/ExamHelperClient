package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.List;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseQuestionFragment;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dbService.CollectionService;
import com.bishe.examhelper.dbService.MaterialAnalysisService;
import com.bishe.examhelper.dbService.StudyRecordService;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.entities.QuestionsOfMaterial;
import com.bishe.examhelper.interfaces.OnAnswerChangedListener;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�MaterialAnalysisFragment   
* �������� ������Fragment�����ڳ�����ExerciseActivity��viewPager��
* �����ˣ���˧  
* ����ʱ�䣺2014-1-2 ����4:45:03   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-2 ����4:45:03   
* �޸ı�ע��   
* @version    
*    
*/
public class MaterialAnalysisFragment extends BaseQuestionFragment implements android.view.View.OnClickListener {
	// ������ữ�������
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

	/************��Ŀ����*************/
	private MaterialAnalysis materialAnalysis;// ���Ϸ�����
	private List<QuestionsOfMaterial> questionsOfMaterialList;// ����������
	private String rightAnswer = "";// ��ȷ��
	private int questionNumber;// ���
	private boolean isAnswered = false;// �ش��־��true��ʾ�ѻش�false��ʾû�ش�

	/*************Views************/
	private View rootView;// ��View
	private TextView materialTextView;// �������
	private ImageView materialImage;// ����ͼƬ
	private TableLayout questionsLayout;// ����
	private EditText myAnswerEditText;// ������
	private Button submitButton;// �ύ�𰸰�ť
	private TextView analysisTextView;// �𰸽�����
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
		materialAnalysis = (MaterialAnalysis) getArguments().getSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS);
		questionNumber = (int) getArguments().getInt(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS_POSITION, 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.material_analysis, null, false);
		findViewById();// ��ʼ��views

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
		materialTextView.setText("    " + questionNumber + ".  " + materialAnalysis.getMaterial());// �������
		if (materialAnalysis.getMaterial_image() != null) {// �����ͼƬ������ͼƬ
			byte[] imageByte = materialAnalysis.getMaterial_image();// ȡ��ͼƬ�ֽ�����
			Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);// ���ֽ�����ת����Bitmap
			materialImage.setImageBitmap(imageBitmap);// ���ò���ͼƬ
			materialImage.setVisibility(View.VISIBLE);// ���ÿɼ�
		}

		questionsOfMaterialList = new ArrayList<QuestionsOfMaterial>();
		questionsOfMaterialList = materialAnalysis.getQuestionsOfMaterialList();// ���С���б�

		// ѭ����ʾС����Ŀ
		for (QuestionsOfMaterial question : questionsOfMaterialList) {
			rightAnswer = rightAnswer + '\n' + "    " + question.getQusetion_number() + ".  " + question.getAnswer();// ȡ����
			TextView textView = new TextView(getActivity());
			textView.setText("    " + question.getQusetion_number() + ".  " + question.getQuestion_stem());
			textView.setTextSize((float) 15);
			textView.setLineSpacing(3.4f, 1f);
			questionsLayout.addView(textView);
		}

		if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {
		}

		/********����༭�������ύ��ť����************/
		myAnswerEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				submitButton.setEnabled(true);
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
		if (collectionService.isCollected(materialAnalysis)) {
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
					collectionService.insertCollection(materialAnalysis);
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
	 * �жϴ�����
	 */
	@Override
	protected void judgeAnswer() {

	}

	/**
	 * ��¼
	 */
	protected void record() {
		Question question = materialAnalysis;
		question.setQuestion_type(DefaultValues.MATERIAL_ANALYSIS);
		// ����ѧϰ��¼
		StudyRecordService studyRecordService = StudyRecordService.getInstance(getActivity());
		studyRecordService.insertStudyRecord(question, myAnswerEditText.getText().toString(), true);

		// ��־����
		materialAnalysis.setFlag(true);
		MaterialAnalysisService materialAnalysisService = MaterialAnalysisService.getInstance(getActivity());
		materialAnalysisService.materialAnalysisDao.update(materialAnalysis);
	}

	/**
	 * ��ʾ�𰸣�����ѡ��������ʾ�ͽ�����
	 */
	@Override
	protected void displayAnswer() {
		analysisTextView.setText(rightAnswer);// ���ô�
		analysisTextView.setVisibility(View.VISIBLE);// ���ô𰸿ɼ�
	}

	/**
	 * ����view
	 */
	protected void findViewById() {
		materialTextView = (TextView) rootView.findViewById(R.id.materials);// �������
		materialImage = (ImageView) rootView.findViewById(R.id.material_image);// ����ͼƬ
		questionsLayout = (TableLayout) rootView.findViewById(R.id.material_questions_table);// ����
		myAnswerEditText = (EditText) rootView.findViewById(R.id.my_answer);// ������
		submitButton = (Button) rootView.findViewById(R.id.submit_btn);// �ύ�𰸰�ť
		analysisTextView = (TextView) rootView.findViewById(R.id.answer);// �𰸽�����
		buttonArea = (LinearLayout) this.rootView.findViewById(R.id.button_area);// ��ť��
		collect_btn = (CheckBox) rootView.findViewById(R.id.collect_button);// �ղذ�ť
		addnote_btn = (TextView) rootView.findViewById(R.id.addnote_button);// ��ӱʼǰ�ť
		share_btn = (TextView) rootView.findViewById(R.id.share_button);// ����ť
	}

	/**
	 * ����ť���¼�
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:// ���������ύ�𰸰�ť
			if (model.equals(DefaultValues.MODEL_EXERCISE)) {// �������ϰģʽ
				isAnswered = true;
				displayAnswer();
				submitButton.setVisibility(View.GONE);// �����ύ�𰸰�ť
				record();
			} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// ����ǿ���ģʽ
				submitButton.setEnabled(false);
				record();
				/********���𰸴��ظ�MockExamActivity***********/
				mAnswerChangedListener.onAnswerChanged(questionNumber - 1, myAnswerEditText.getText().toString());
			}

			break;
		case R.id.addnote_button:// ����������ӱʼǰ�ť
			Intent intent = new Intent(getActivity(), AddNoteActivity.class);
			Question question = materialAnalysis;
			question.setQuestion_type(DefaultValues.MATERIAL_ANALYSIS);
			intent.putExtra(DefaultKeys.BUNDLE_QUESTION, question);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.share_button:// �������˷���ť
			// ���÷�������
			mController.setShareContent(materialAnalysis.getMaterial() + "\n"
					+ materialAnalysis.getQuestionsOfMaterialList());
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

}
