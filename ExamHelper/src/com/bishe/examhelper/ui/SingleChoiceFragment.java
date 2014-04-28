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
 * 项目名称：ExamHelper   
 * 类名称：SingleChoiceFragment   
 * 类描述：   单选题Fragment，返回一个单选题的页面给ExerciseActivity中的ViewPager来显示
 * 创建人：张帅     
 * 创建时间：2013-12-14 下午10:37:20   
 * 修改人：张帅     
 * 修改时间：2013-12-15 下午3:47:16   
 * 修改备注：   
 * @version    
 *    
 */
public class SingleChoiceFragment extends BaseQuestionFragment implements View.OnClickListener {
	// 友盟社会化分享组件
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

	/************题目内容*************/
	private SingleChoice mysingleChoice;// 单选题
	private int questionNumber;// 题号
	private String myAnswer;// 我的答案
	private int myCheckedId;// 选择的RadioButtonID
	private boolean isAnswered = false;// 回答标志，true表示已回答，false表示没回答

	/*************Views************/
	private View rootView;// 根View
	private TextView single_choice_stem;// 题干
	private RadioGroup mRadioGroup;// RadioGroup
	private RadioButton optionA;// 选项A
	private RadioButton optionB;// 选项B
	private RadioButton optionC;// 选项C
	private RadioButton optionD;// 选项D
	private ViewStub viewStub;// 解析区Stub
	private LinearLayout buttonArea;// 按钮区，包括添加笔记按钮、收藏按钮、分享按钮三个按钮
	private TextView addnoteButton;// 添加笔记按钮
	private CheckBox collectButton;// 收藏按钮
	private TextView shareButton;// 分享按钮
	private Button submitButton;// 显示答案按钮

	/**********MockExamActivity回调接口**************/
	private OnAnswerChangedListener mAnswerChangedListener;

	/**************如果是模拟考试模式，新建OnAnswerChangedListener回调接口，
	 * 如果MockExamActivity没有实现OnAnswerChangedListener接口，则抛出异常**********/
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {
			try {
				mAnswerChangedListener = (OnAnswerChangedListener) activity;
			} catch (ClassCastException e) {
				throw new ClassCastException(activity.toString() + " 必须实现OnAnswerChangedListener接口");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 从绑定参数中取得SingleChoice对象
		mysingleChoice = (SingleChoice) getArguments().getSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE);
		questionNumber = (int) getArguments().getInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = getActivity().getLayoutInflater().inflate(R.layout.single_choice, null, false);
		findViewById();

		// 如果是展示模式，接收用户答案
		if (model.equals(DefaultValues.MODEL_DISPLAY)) {
			myAnswer = (String) getArguments().getString(DefaultKeys.KEY_USER_ANSWER, "A");
			myCheckedId = getIdByString(myAnswer);
		}

		// 初始化题目内容
		initQuestionView();

		// 如果是练习模式，或者是展示模式，则显示按钮区
		if (model.equals(DefaultValues.MODEL_EXERCISE) || model.equals(DefaultValues.MODEL_DISPLAY)) {
			initButtonView();
		}

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// 如果用户设置不立即显示答案,设置显示答案按钮
		if (!isCheckNow && model.equals(DefaultValues.MODEL_EXERCISE)) {
			submitButton.setVisibility(View.VISIBLE);
			submitButton.setOnClickListener(this);
			submitButton.setEnabled(false);
		} else {
			submitButton.setVisibility(View.GONE);
		}

		// 如果已经回答过,并且是练习模式，显示答案解析,隐藏显示答案按钮
		if (isAnswered && model.equals(DefaultValues.MODEL_EXERCISE)) {
			displayAnswer();
			submitButton.setVisibility(View.GONE);
		}

		// 如果是展示模式，则直接显示答案和解析
		if (model.equals(DefaultValues.MODEL_DISPLAY)) {
			submitButton.setVisibility(View.GONE);
			displayAnswer();
		}

	}

	/**
	 * 初始化题目内容
	 */
	@Override
	protected void initQuestionView() {
		/************设置题目内容**************/
		this.single_choice_stem.setText("    " + questionNumber + ".  " + mysingleChoice.getQuestion_stem());// 设置题干
		this.optionA.setText(mysingleChoice.getOptionA());// 设置选项A
		this.optionB.setText(mysingleChoice.getOptionB());// 设置选项B
		this.optionC.setText(mysingleChoice.getOptionC());// 设置选项C
		this.optionD.setText(mysingleChoice.getOptionD());// 设置选项D

		// 给RadiobuttonGroup监听事件
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				myCheckedId = checkedId;
				// 如果为练习模式，用户设置为立即显示答案
				if (isCheckNow && model.equals(DefaultValues.MODEL_EXERCISE)) {
					// 如果没有回答过，则判断正误
					if (!isAnswered) {
						judgeAnswer();
						record();
					}
					isAnswered = true;// 标志已回答
					displayAnswer();

					optionA.setEnabled(false);// 设置选项按钮不可用
					optionB.setEnabled(false);// 设置选项按钮不可用
					optionC.setEnabled(false);// 设置选项按钮不可用
					optionD.setEnabled(false);// 设置选项按钮不可用
				} else if (!isCheckNow && model.equals(DefaultValues.MODEL_EXERCISE)) {// 如果为练习模式，用户设置为不立即显示答案
					// 设置显示答案按钮可用
					submitButton.setEnabled(true);
				} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// 如果为模拟考试模式
					/********将答案传回给MockExamActivity***********/
					mAnswerChangedListener.onAnswerChanged(questionNumber - 1, getStringByCheckedId(checkedId));
				}
			}
		});

	}

	/**
	 * 初始化 按钮区域
	 */
	@Override
	protected void initButtonView() {
		// 设置按钮区可见
		buttonArea.setVisibility(View.VISIBLE);

		// 取得“收藏实体”数据库工具实例
		final CollectionService collectionService = CollectionService.getInstance(getActivity());
		// 如果已被收藏，则收藏按钮显示已被收藏状态
		if (collectionService.isCollected(mysingleChoice)) {
			collectButton.setText("取消收藏");
			this.collectButton.setChecked(true);
		}

		// 给收藏按钮绑定事件
		this.collectButton.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					collectButton.setText("取消收藏");
					collectionService.insertCollection(mysingleChoice);
					new Thread() {
						public void run() {
							collectionService.addCollectionToNet(mysingleChoice);
						};
					}.start();

					Toast.makeText(getActivity(), "收藏成功！", 1).show();
				} else {
					collectionService.deleteCollection();
					new Thread() {
						public void run() {
							collectionService.delCollectionFromNet(mysingleChoice);
						};
					}.start();
					collectButton.setText("收藏");
				}
			}
		});

		// 给分享按钮绑定事件
		this.shareButton.setOnClickListener(this);

		// 给添加笔记按钮绑定事件
		this.addnoteButton.setOnClickListener(this);
	}

	/**
	 * 判断答案正误
	 */
	@Override
	protected void judgeAnswer() {
		// 如果答案不正确，则插入一条错误记录
		if (!getStringByCheckedId(myCheckedId).equals(mysingleChoice.getAnswer().trim())) {
			// 如果是练习模式，则根据设置选择是否震动
			if (model.equals(DefaultValues.MODEL_EXERCISE)) {
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
				boolean vibrator = sharedPreferences.getBoolean(DefaultKeys.KEY_PREF_VIBRATE_AFTER, false);
				if (vibrator) {
					Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(100);
				}
			}
			/************插入错误记录*************/
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
	 * 记录
	 */
	protected void record() {
		boolean isRight = false;
		if (getStringByCheckedId(myCheckedId).equals(mysingleChoice.getAnswer().trim())) {
			isRight = true;
		}
		Question question = mysingleChoice;
		question.setQuestion_type(DefaultValues.SINGLE_CHOICE);
		// 插入学习记录
		StudyRecordService studyRecordService = StudyRecordService.getInstance(getActivity());
		studyRecordService.insertStudyRecord(question, getStringByCheckedId(myCheckedId), isRight);

		// 标志已做
		mysingleChoice.setFlag(true);
		SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(getActivity());
		singleChoiceService.singleChoiceDao.update(mysingleChoice);
	}

	/**
	 * 显示答案，包括选项正误提示和解析区
	 */
	@Override
	protected void displayAnswer() {

		/**************设置答案解析区**************/
		viewStub.setVisibility(View.VISIBLE);// 设置解析区可见

		View view = rootView.findViewById(R.id.single_choice_inflaterId);// 获得解析区view
		TextView myAnswerTextView = (TextView) view.findViewById(R.id.my_answer);// 我的答案
		TextView rightAnswerTextView = (TextView) view.findViewById(R.id.right_answer);// 正确答案
		ImageView rightorwrongImage = (ImageView) view.findViewById(R.id.image_wrong_or_right);// 对号错号图标
		TextView analysisTextView = (TextView) view.findViewById(R.id.analysis);// 答案解析

		// 如果是正确答案
		if (getStringByCheckedId(myCheckedId).equals(mysingleChoice.getAnswer().trim())) {
			rightorwrongImage.setBackgroundResource(R.drawable.image_right);// 显示对号
			myAnswerTextView.setTextColor(getResources().getColor(R.color.rightcolor));// 我的答案显示绿色
			myAnswerTextView.setText("我的答案：  " + getStringByCheckedId(myCheckedId) + "（答对了！）");// 显示我的答案
		} else {
			rightorwrongImage.setBackgroundResource(R.drawable.image_wrong);// 显示错号
			myAnswerTextView.setTextColor(getResources().getColor(R.color.wrongcolor));// 我的答案显示红色
			myAnswerTextView.setText("我的答案：  " + getStringByCheckedId(myCheckedId) + "（答错了！）");// 显示我的答案
		}

		rightAnswerTextView.setText("正确答案是：" + mysingleChoice.getAnswer());// 显示正确答案
		analysisTextView.setText(mysingleChoice.getAnalysis());// 显示答案解析
		/**************设置答案解析区结束**************/

		/**************设置选项样式**************/
		RadioButton myOptionButton = (RadioButton) rootView.findViewById(myCheckedId);// 我的选择
		// 如果选择的是正确答案
		if (getStringByCheckedId(myCheckedId).equals(mysingleChoice.getAnswer().trim())) {
			myOptionButton.setTextColor(getResources().getColor(R.color.rightcolor));// 选项设置为绿色
			myOptionButton.setButtonDrawable(R.drawable.selector_radio_true);// 设置RadioButton图标
		} else {
			myOptionButton.setTextColor(getResources().getColor(R.color.wrongcolor));// 选项设置为红色
			myOptionButton.setButtonDrawable(R.drawable.selector_radio_false);// 设置RadioButton图标

			// 取得正确选项
			RadioButton rightOptionButton = (RadioButton) rootView.findViewById(getIdByString(mysingleChoice
					.getAnswer().trim()));
			rightOptionButton.setTextColor(getResources().getColor(R.color.rightcolor));// 选项设置为绿色
			rightOptionButton.setButtonDrawable(R.drawable.selector_radio_true);// 设置RadioButton图标
		}
		/**************设置选项结束**************/

	}

	// 查找view
	@Override
	protected void findViewById() {
		this.single_choice_stem = ((TextView) this.rootView.findViewById(R.id.single_choice_stem));// 单选题干
		this.mRadioGroup = ((RadioGroup) this.rootView.findViewById(R.id.singal_choice_optionGroup));// RadioGroup
		this.optionA = ((RadioButton) this.rootView.findViewById(R.id.optionA));// 选项A
		this.optionB = ((RadioButton) this.rootView.findViewById(R.id.optionB));// 选项B
		this.optionC = ((RadioButton) this.rootView.findViewById(R.id.optionC));// 选项C
		this.optionD = ((RadioButton) this.rootView.findViewById(R.id.optionD));// 选项D
		this.viewStub = ((ViewStub) this.rootView.findViewById(R.id.single_choice_answer_stub));// 解析区Stub
		this.buttonArea = (LinearLayout) this.rootView.findViewById(R.id.button_area);// 按钮区
		this.shareButton = ((TextView) this.rootView.findViewById(R.id.share_button));// 分享按钮
		this.collectButton = ((CheckBox) this.rootView.findViewById(R.id.collect_button));// 收藏按钮
		this.addnoteButton = ((TextView) this.rootView.findViewById(R.id.addnote_button));// 添加笔记按钮
		this.submitButton = (Button) this.rootView.findViewById(R.id.submit_btn);// 显示答案按钮
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/**使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:// 提交答案按钮
			if (!isAnswered) {// 如果没有回答过，则判断正误
				judgeAnswer();
				record();
			}
			isAnswered = true;
			displayAnswer();
			submitButton.setVisibility(View.GONE);
			optionA.setEnabled(false);// 设置选项按钮不可用
			optionB.setEnabled(false);// 设置选项按钮不可用
			optionC.setEnabled(false);// 设置选项按钮不可用
			optionD.setEnabled(false);// 设置选项按钮不可用
			break;
		case R.id.share_button:// 分享按钮
			// 设置分享内容
			mController.setShareContent(mysingleChoice.getQuestion_stem() + "\n" + "A " + mysingleChoice.getOptionA()
					+ "\n" + "B " + mysingleChoice.getOptionB() + "\n" + "C " + mysingleChoice.getOptionC() + "\n"
					+ "D " + mysingleChoice.getOptionD());
			// 设置分享图片，参数2为本地图片的资源引用
			mController.setShareMedia(new UMImage(getActivity(), R.drawable.logo));

			// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
			UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(getActivity(),
					DefaultValues.WEIXIN_APP_ID, DefaultValues.WEIXIN_CONTENT_URL);
			wxHandler.setWXTitle("友盟社会化组件还不错...");

			// 支持微信朋友圈
			UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(getActivity(),
					DefaultValues.WEIXIN_APP_ID, DefaultValues.WEIXIN_CONTENT_URL);
			circleHandler.setCircleTitle("友盟社会化组件还不错...");

			// 参数1为当前Activity， 参数2为用户点击分享内容时跳转到的目标地址
			mController.getConfig().supportQQPlatform(getActivity(), DefaultValues.WEIXIN_CONTENT_URL);

			// 设置新浪SSO handler
			mController.getConfig().setSsoHandler(new SinaSsoHandler());

			// 设置腾讯微博SSO handler
			mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

			mController.openShare(getActivity(), false);
			break;
		case R.id.addnote_button:// 添加笔记按钮
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
	 * 根据选项ID返回对应的答案
	 * @return
	 */
	public String getStringByCheckedId(int checkedId) {
		String checkedString = "";
		switch (checkedId) {
		case R.id.submit_btn:// 提交答案按钮
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
	 *  根据选项返回选项RadioButton的ID
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
