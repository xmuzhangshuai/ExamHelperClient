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
* 项目名称：ExamHelper   
* 类名称：MultiChoiceFragment   
* 类描述：   多选题Fragment，用于呈现在ExerciseActivity的viewPager中
* 创建人：张帅  
* 创建时间：2014-1-2 下午4:43:16   
* 修改人：张帅   
* 修改时间：2014-1-2 下午4:43:16   
* 修改备注：   
* @version    
*    
*/
public class MultiChoiceFragment extends BaseQuestionFragment implements android.view.View.OnClickListener,
		OnCheckedChangeListener {
	// 友盟社会化分享组件
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

	/************题目内容*************/
	private MultiChoice myMultiChoice;
	private String myAnswer = "";// 我的答案
	private String rightAnswer = "";// 正确答案
	private int questionNumber;// 题号
	private boolean isAnswered = false;// 回答标志，true表示已回答，false表示没回答

	/*************Views************/
	private View rootView;// 根View
	private TextView multi_choice_stem;// 题干
	private ViewStub viewStub;
	private CheckBox optionA;// 选项A
	private CheckBox optionB;// 选项B
	private CheckBox optionC;// 选项C
	private CheckBox optionD;// 选项D
	private Button submitButton;// 提交答案按钮
	private LinearLayout buttonArea;// 按钮区，包括添加笔记按钮、收藏按钮、分享按钮三个按钮
	private CheckBox collect_btn;// 收藏按钮
	private TextView addnote_btn;// 添加笔记按钮
	private TextView share_btn;// 分享按钮

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

		// 从绑定参数中取得MultiChoice对象
		myMultiChoice = (MultiChoice) getArguments().getSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE);
		questionNumber = (int) getArguments().getInt(DefaultKeys.BUNDLE_MULTI_CHOICE_POSITION, 1);

		// 如果是展示模式，接收用户答案
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

		// 如果是练习模式,或者是展示模式，则显示按钮区
		if (model.equals(DefaultValues.MODEL_EXERCISE) || model.equals(DefaultValues.MODEL_DISPLAY)) {
			initButtonView();
		} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// 如果是考试模式
			submitButton.setText("确定");
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

	/**
	 * 初始化题目内容
	 */
	@Override
	protected void initQuestionView() {
		/************设置题目内容**************/
		this.multi_choice_stem.setText("    " + questionNumber + ".  " + myMultiChoice.getQuestion_stem());// 设置题干
		this.optionA.setText(myMultiChoice.getOptionA());// 设置选项A
		this.optionB.setText(myMultiChoice.getOptionB());// 设置选项B
		this.optionC.setText(myMultiChoice.getOptionC());// 设置选项C
		this.optionD.setText(myMultiChoice.getOptionD());// 设置选项D
		optionA.setOnCheckedChangeListener(this);
		optionB.setOnCheckedChangeListener(this);
		optionC.setOnCheckedChangeListener(this);
		optionD.setOnCheckedChangeListener(this);
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
		if (collectionService.isCollected(myMultiChoice)) {
			collect_btn.setText("取消收藏");
			this.collect_btn.setChecked(true);
		}

		// 给收藏按钮绑定事件
		collect_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					collect_btn.setText("取消收藏");
					collectionService.insertCollection(myMultiChoice);
					Toast.makeText(getActivity(), "收藏成功！", 1).show();
				} else {
					collectionService.deleteCollection();
					collect_btn.setText("收藏");
				}
			}
		});

		addnote_btn.setOnClickListener(this);// 添加笔记按钮设置事件
		share_btn.setOnClickListener(this);// 分享按钮设置事件

	}

	/**
	 * 显示答案，包括选项正误提示和解析区
	 */
	@Override
	protected void displayAnswer() {
		viewStub.setVisibility(View.VISIBLE);// 设置解析区可见
		View view = rootView.findViewById(R.id.multi_choice_inflaterId);// 获得解析区view
		TextView myAnswerTextView = (TextView) view.findViewById(R.id.my_answer);// 我的答案
		TextView rightAnswerTextView = (TextView) view.findViewById(R.id.right_answer);// 正确答案
		ImageView rightorwrongImage = (ImageView) view.findViewById(R.id.image_wrong_or_right);// 对号错号图标
		TextView analysisTextView = (TextView) view.findViewById(R.id.analysis);// 答案解析

		/***判断我选择的答案*****/
		if (!model.equals(DefaultValues.MODEL_DISPLAY)) {
			myAnswer = getMyAnswer();
		}

		/********判断每个选项的正误，并将正确答案保存在rightAnswer中********/
		rightAnswer = getRightAnswer();

		rightAnswerTextView.setText("正确答案：  " + rightAnswer);// 设置正确答案
		analysisTextView.setText(myMultiChoice.getAnalysis());// 设置答案解析
		// 设置解析区颜色、图片
		if (rightAnswer.trim().equals(myAnswer.trim())) {// 如果所选答案正确
			myAnswerTextView.setText("我的答案：  " + myAnswer + "（正确）");// 设置我的答案
			myAnswerTextView.setTextColor(getResources().getColor(R.color.rightcolor));// 绿色
			rightorwrongImage.setBackgroundResource(R.drawable.image_right);// 显示对号
		} else {// 如果所选答案错误
			myAnswerTextView.setText("我的答案：  " + myAnswer + "（错误）");// 设置我的答案
			myAnswerTextView.setTextColor(getResources().getColor(R.color.wrongcolor));// 红色
			rightorwrongImage.setBackgroundResource(R.drawable.image_wrong);// 显示错号
		}
	}

	/**
	 * 判断答案正误
	 */
	@Override
	protected void judgeAnswer() {
		// 如果回答不正确，则插入错误记录
		if (!rightAnswer.trim().equals(myAnswer.trim())) {
			/************插入错误记录*************/
			ErrorQuestionsService errorQuestionsService = ErrorQuestionsService.getInstance(getActivity());
			errorQuestionsService.addErrorQuestions(myMultiChoice);
		}

	}

	/**
	 * 记录
	 */
	protected void record() {
		boolean isRight = false;
		if (rightAnswer.trim().equals(myAnswer.trim())) {
			isRight = true;
		}
		Question question = myMultiChoice;
		question.setQuestion_type(DefaultValues.MULTI_CHOICE);
		// 插入学习记录
		StudyRecordService studyRecordService = StudyRecordService.getInstance(getActivity());
		studyRecordService.insertStudyRecord(question, getMyAnswer(), isRight);

		// 标志已做
		myMultiChoice.setFlag(true);
		MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(getActivity());
		multiChoiceService.multiChoiceDao.update(myMultiChoice);
	}

	/**
	 *  查找View
	 */
	protected void findViewById() {
		viewStub = (ViewStub) rootView.findViewById(R.id.multi_choice_answer_stub);// 解析区Stub
		multi_choice_stem = (TextView) rootView.findViewById(R.id.multi_choice_stem);
		optionA = (CheckBox) rootView.findViewById(R.id.optionA);// 选项A
		optionB = (CheckBox) rootView.findViewById(R.id.optionB);// 选项B
		optionC = (CheckBox) rootView.findViewById(R.id.optionC);// 选项C
		optionD = (CheckBox) rootView.findViewById(R.id.optionD);// 选项D
		submitButton = (Button) rootView.findViewById(R.id.submit_btn);// 提交答案按钮
		this.buttonArea = (LinearLayout) this.rootView.findViewById(R.id.button_area);// 按钮区
		collect_btn = (CheckBox) rootView.findViewById(R.id.collect_button);// 收藏按钮
		addnote_btn = (TextView) rootView.findViewById(R.id.addnote_button);// 添加笔记按钮
		share_btn = (TextView) rootView.findViewById(R.id.share_button);// 分享按钮
	};

	/**
	 * 定义事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:// 如果点击了提交答案按钮
			if (model.equals(DefaultValues.MODEL_EXERCISE)) {// 如果是练习模式
				displayAnswer();
				if (!isAnswered) {// 如果没有回答过，则判断正误
					judgeAnswer();
					record();
				}
				isAnswered = true;
				submitButton.setVisibility(View.GONE);
				optionA.setEnabled(false);// 设置选项按钮不可用
				optionB.setEnabled(false);// 设置选项按钮不可用
				optionC.setEnabled(false);// 设置选项按钮不可用
				optionD.setEnabled(false);// 设置选项按钮不可用
			} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// 如果是考试模式
				submitButton.setEnabled(false);
				record();
				/********将答案传回给MockExamActivity***********/
				mAnswerChangedListener.onAnswerChanged(questionNumber - 1, getMyAnswer());
			}
			break;
		case R.id.addnote_button:// 如果点击了添加笔记按钮
			Intent intent = new Intent(getActivity(), AddNoteActivity.class);
			Question question = myMultiChoice;
			question.setQuestion_type(DefaultValues.MULTI_CHOICE);
			intent.putExtra(DefaultKeys.BUNDLE_QUESTION, question);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.share_button:// 如果点击了分享按钮
			// 设置分享内容
			mController.setShareContent(myMultiChoice.getQuestion_stem() + "\n" + "A " + myMultiChoice.getOptionA()
					+ "\n" + "B " + myMultiChoice.getOptionB() + "\n" + "C " + myMultiChoice.getOptionC() + "\n" + "D "
					+ myMultiChoice.getOptionD());
			// 设置分享图片，参数2为本地图片的资源引用
			mController.setShareMedia(new UMImage(getActivity(), R.drawable.headimage));

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
	 * 根据选项返回我的答案
	 * @return
	 */
	public String getMyAnswer() {
		String myAnswer = "";
		// 如果选择了A选项
		if (optionA.isChecked() == true) {
			myAnswer = myAnswer + "A";// 我的答案添加A
		}
		// 如果选择了B选项
		if (optionB.isChecked() == true) {
			myAnswer = myAnswer + "B";
		}
		// 如果选择了C选项
		if (optionC.isChecked() == true) {
			myAnswer = myAnswer + "C";
		}
		// 如果选择了D选项
		if (optionD.isChecked() == true) {
			myAnswer = myAnswer + "D";
		}
		return myAnswer;
	}

	/**
	 * 根据数据库数据返回正确
	 * @return
	 */
	public String getRightAnswer() {
		String rightAnswer = "";
		if (myMultiChoice.getAnswerA() == true) {// 如果A正确
			rightAnswer = rightAnswer + "A";
			optionA.setTextColor(getResources().getColor(R.color.rightcolor));// 设置绿色
			optionA.setButtonDrawable(R.drawable.selector_multi_checkbox_true);// 设置选项图标对号
		} else {
			optionA.setTextColor(getResources().getColor(R.color.wrongcolor));// 设置红色
			optionA.setButtonDrawable(R.drawable.selector_multi_checkbox_false);// 设置选项图标错号
		}

		if (myMultiChoice.getAnswerB() == true) {// 如果B正确
			rightAnswer = rightAnswer + "B";
			optionB.setTextColor(getResources().getColor(R.color.rightcolor));// 设置绿色
			optionB.setButtonDrawable(R.drawable.selector_multi_checkbox_true);// 设置选项图标对号
		} else {
			optionB.setTextColor(getResources().getColor(R.color.wrongcolor));// 设置红色
			optionB.setButtonDrawable(R.drawable.selector_multi_checkbox_false);// 设置选项图标错号
		}

		if (myMultiChoice.getAnswerC() == true) {// 如果C正确
			rightAnswer = rightAnswer + "C";
			optionC.setTextColor(getResources().getColor(R.color.rightcolor));// 设置绿色
			optionC.setButtonDrawable(R.drawable.selector_multi_checkbox_true);// 设置选项图标对号
		} else {
			optionC.setTextColor(getResources().getColor(R.color.wrongcolor));// 设置红色
			optionC.setButtonDrawable(R.drawable.selector_multi_checkbox_false);// 设置选项图标错号
		}

		if (myMultiChoice.getAnswerD() == true) {// 如果D正确
			rightAnswer = rightAnswer + "D";
			optionD.setTextColor(getResources().getColor(R.color.rightcolor));// 设置绿色
			optionD.setButtonDrawable(R.drawable.selector_multi_checkbox_true);// 设置选项图标对号
		} else {
			optionD.setTextColor(getResources().getColor(R.color.wrongcolor));// 设置红色
			optionD.setButtonDrawable(R.drawable.selector_multi_checkbox_false);// 设置选项图标错号
		}

		return rightAnswer;
	}

}
