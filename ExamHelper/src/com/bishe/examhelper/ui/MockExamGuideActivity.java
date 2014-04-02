package com.bishe.examhelper.ui;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.dbService.ExaminationService;
import com.bishe.examhelper.entities.Examination;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：MockExamGuideActivity   
* 类描述：   模拟考试开始页面，可选择是否包含材料题，随机生成试卷还是模拟真题，以及选择真题。
* 创建人：张帅  
* 创建时间：2014-1-10 下午10:56:38   
* 修改人：张帅   
* 修改时间：2014-1-10 下午10:56:38   
* 修改备注：   
* @version    
*    
*/
public class MockExamGuideActivity extends BaseActivity {

	private CheckBox includeBox;// 是否包含材料分析题选项
	private boolean includeMaterial;// 是否包含材料题
	private RadioButton radioRandom;// 随机生成试卷RadioButton
	private RadioButton radioReal;// 真题模拟试卷RadioButton
	private Button startButton;// 开始按钮
	private Button switchButton;// 选择试题按钮
	private TextView examRequest;// 试卷要求
	private List<Examination> examinationList;// 模拟试题列表
	public Long examId = (long) -1;// 选中的模拟试卷试卷ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mock_exam_guide);

		findViewById();

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
		includeBox = (CheckBox) findViewById(R.id.include_material_checkbox);
		radioRandom = (RadioButton) findViewById(R.id.random_create_radio);
		radioReal = (RadioButton) findViewById(R.id.real_exam_radio);
		startButton = (Button) findViewById(R.id.start_mock_exam_btn);
		switchButton = (Button) findViewById(R.id.switch_mock_exam_btn);
		examRequest = (TextView) findViewById(R.id.exam_request);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("模拟考试");

		includeMaterial = includeBox.isChecked();
		includeBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				includeMaterial = isChecked;
			}
		});

		radioRandom.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					startButton.setEnabled(true);
					switchButton.setVisibility(View.GONE);
					examId = (long) -1;
				} else {
					startButton.setEnabled(false);
				}
			}
		});

		radioReal.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					switchButton.setVisibility(View.VISIBLE);
				} else {
				}
			}
		});

		switchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChooseExamDialog();
			}
		});

		// 没有选择试卷类型前按钮不可用
		startButton.setEnabled(false);

		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MockExamGuideActivity.this);
				builder.setIcon(R.drawable.dialog_blue_icon).setTitle("温馨提示").setMessage(getMessage())
						.setPositiveButton("开始答题", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

								/*******开始MockExam答题页面***********/
								Intent intent = new Intent(MockExamGuideActivity.this, MockExamActivity.class);
								intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_INCLUDE, includeMaterial);
								intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_ID, examId);
								startActivity(intent);
								overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
								finish();
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						}).show();
			}
		});

	}

	/*****返回用户设置信息*******/
	private String getMessage() {
		String message = "";
		// 如果选中了包含材料分析题
		if (includeBox.isChecked()) {
			message = "  您选择了包含材料分析题，系统不能给出材料题评分，需要您根据答案自评。";
		} else {
			message = "  您选择了不包含材料分析题，答题时间将变为60分钟。";
		}
		return message;
	}

	/**
	 * 显示试卷列表Dialog
	 */
	private void showChooseExamDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(MockExamGuideActivity.this);
		builder.setIcon(R.drawable.switch_exam_icon).setTitle("请选择要模拟考试试题：");
		builder.setSingleChoiceItems(getExamTitleList(), -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				examId = examinationList.get(which).getId();
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (examId != -1) {
					startButton.setEnabled(true);
					ExaminationService examinationService = ExaminationService.getInstance(MockExamGuideActivity.this);
					examRequest.setText("考试类型:  " + examinationService.loadExamination(examId).getExam_name());
				} else {
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				examId = (long) -1;
			}
		});
		builder.setCancelable(false);
		builder.show();
	}

	/****返回试卷题目数组*****/
	private String[] getExamTitleList() {
		ExaminationService examinationService = ExaminationService.getInstance(MockExamGuideActivity.this);
		examinationList = examinationService.loadAllExaminations();
		String[] titles = new String[examinationList.size()];

		for (int i = 0; i < titles.length; i++) {
			titles[i] = examinationList.get(i).getExam_name();
		}
		return titles;
	}

}
