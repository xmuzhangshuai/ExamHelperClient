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
* 项目名称：ExamHelper   
* 类名称：MaterialAnalysisFragment   
* 类描述： 材料题Fragment，用于呈现在ExerciseActivity的viewPager中
* 创建人：张帅  
* 创建时间：2014-1-2 下午4:45:03   
* 修改人：张帅   
* 修改时间：2014-1-2 下午4:45:03   
* 修改备注：   
* @version    
*    
*/
public class MaterialAnalysisFragment extends BaseQuestionFragment implements android.view.View.OnClickListener {
	// 友盟社会化分享组件
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

	/************题目内容*************/
	private MaterialAnalysis materialAnalysis;// 材料分析题
	private List<QuestionsOfMaterial> questionsOfMaterialList;// 材料题问题
	private String rightAnswer = "";// 正确答案
	private int questionNumber;// 题号
	private boolean isAnswered = false;// 回答标志，true表示已回答，false表示没回答

	/*************Views************/
	private View rootView;// 根View
	private TextView materialTextView;// 材料题干
	private ImageView materialImage;// 材料图片
	private TableLayout questionsLayout;// 问题
	private EditText myAnswerEditText;// 作答区
	private Button submitButton;// 提交答案按钮
	private TextView analysisTextView;// 答案解析区
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
		materialAnalysis = (MaterialAnalysis) getArguments().getSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS);
		questionNumber = (int) getArguments().getInt(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS_POSITION, 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.material_analysis, null, false);
		findViewById();// 初始化views

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
		materialTextView.setText("    " + questionNumber + ".  " + materialAnalysis.getMaterial());// 设置题干
		if (materialAnalysis.getMaterial_image() != null) {// 如果有图片则设置图片
			byte[] imageByte = materialAnalysis.getMaterial_image();// 取出图片字节数组
			Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);// 将字节数组转化成Bitmap
			materialImage.setImageBitmap(imageBitmap);// 设置材料图片
			materialImage.setVisibility(View.VISIBLE);// 设置可见
		}

		questionsOfMaterialList = new ArrayList<QuestionsOfMaterial>();
		questionsOfMaterialList = materialAnalysis.getQuestionsOfMaterialList();// 获得小题列表

		// 循环显示小题题目
		for (QuestionsOfMaterial question : questionsOfMaterialList) {
			rightAnswer = rightAnswer + '\n' + "    " + question.getQusetion_number() + ".  " + question.getAnswer();// 取出答案
			TextView textView = new TextView(getActivity());
			textView.setText("    " + question.getQusetion_number() + ".  " + question.getQuestion_stem());
			textView.setTextSize((float) 15);
			textView.setLineSpacing(3.4f, 1f);
			questionsLayout.addView(textView);
		}

		if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {
		}

		/********如果编辑过，则提交按钮可用************/
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
	 * 初始化 按钮区域
	 */
	@Override
	protected void initButtonView() {
		// 设置按钮区可见
		buttonArea.setVisibility(View.VISIBLE);

		// 取得“收藏实体”数据库工具实例
		final CollectionService collectionService = CollectionService.getInstance(getActivity());

		// 如果已被收藏，则收藏按钮显示已被收藏状态
		if (collectionService.isCollected(materialAnalysis)) {
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
					collectionService.insertCollection(materialAnalysis);
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
	 * 判断答案正误
	 */
	@Override
	protected void judgeAnswer() {

	}

	/**
	 * 记录
	 */
	protected void record() {
		Question question = materialAnalysis;
		question.setQuestion_type(DefaultValues.MATERIAL_ANALYSIS);
		// 插入学习记录
		StudyRecordService studyRecordService = StudyRecordService.getInstance(getActivity());
		studyRecordService.insertStudyRecord(question, myAnswerEditText.getText().toString(), true);

		// 标志已做
		materialAnalysis.setFlag(true);
		MaterialAnalysisService materialAnalysisService = MaterialAnalysisService.getInstance(getActivity());
		materialAnalysisService.materialAnalysisDao.update(materialAnalysis);
	}

	/**
	 * 显示答案，包括选项正误提示和解析区
	 */
	@Override
	protected void displayAnswer() {
		analysisTextView.setText(rightAnswer);// 设置答案
		analysisTextView.setVisibility(View.VISIBLE);// 设置答案可见
	}

	/**
	 * 查找view
	 */
	protected void findViewById() {
		materialTextView = (TextView) rootView.findViewById(R.id.materials);// 材料题干
		materialImage = (ImageView) rootView.findViewById(R.id.material_image);// 材料图片
		questionsLayout = (TableLayout) rootView.findViewById(R.id.material_questions_table);// 问题
		myAnswerEditText = (EditText) rootView.findViewById(R.id.my_answer);// 作答区
		submitButton = (Button) rootView.findViewById(R.id.submit_btn);// 提交答案按钮
		analysisTextView = (TextView) rootView.findViewById(R.id.answer);// 答案解析区
		buttonArea = (LinearLayout) this.rootView.findViewById(R.id.button_area);// 按钮区
		collect_btn = (CheckBox) rootView.findViewById(R.id.collect_button);// 收藏按钮
		addnote_btn = (TextView) rootView.findViewById(R.id.addnote_button);// 添加笔记按钮
		share_btn = (TextView) rootView.findViewById(R.id.share_button);// 分享按钮
	}

	/**
	 * 给按钮绑定事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:// 如果点击了提交答案按钮
			if (model.equals(DefaultValues.MODEL_EXERCISE)) {// 如果是练习模式
				isAnswered = true;
				displayAnswer();
				submitButton.setVisibility(View.GONE);// 隐藏提交答案按钮
				record();
			} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// 如果是考试模式
				submitButton.setEnabled(false);
				record();
				/********将答案传回给MockExamActivity***********/
				mAnswerChangedListener.onAnswerChanged(questionNumber - 1, myAnswerEditText.getText().toString());
			}

			break;
		case R.id.addnote_button:// 如果点击了添加笔记按钮
			Intent intent = new Intent(getActivity(), AddNoteActivity.class);
			Question question = materialAnalysis;
			question.setQuestion_type(DefaultValues.MATERIAL_ANALYSIS);
			intent.putExtra(DefaultKeys.BUNDLE_QUESTION, question);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.share_button:// 如果点击了分享按钮
			// 设置分享内容
			mController.setShareContent(materialAnalysis.getMaterial() + "\n"
					+ materialAnalysis.getQuestionsOfMaterialList());
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

}
