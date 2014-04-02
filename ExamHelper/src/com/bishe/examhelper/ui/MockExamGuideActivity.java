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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�MockExamGuideActivity   
* ��������   ģ�⿼�Կ�ʼҳ�棬��ѡ���Ƿ���������⣬��������Ծ���ģ�����⣬�Լ�ѡ�����⡣
* �����ˣ���˧  
* ����ʱ�䣺2014-1-10 ����10:56:38   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-10 ����10:56:38   
* �޸ı�ע��   
* @version    
*    
*/
public class MockExamGuideActivity extends BaseActivity {

	private CheckBox includeBox;// �Ƿ�������Ϸ�����ѡ��
	private boolean includeMaterial;// �Ƿ����������
	private RadioButton radioRandom;// ��������Ծ�RadioButton
	private RadioButton radioReal;// ����ģ���Ծ�RadioButton
	private Button startButton;// ��ʼ��ť
	private Button switchButton;// ѡ�����ⰴť
	private TextView examRequest;// �Ծ�Ҫ��
	private List<Examination> examinationList;// ģ�������б�
	public Long examId = (long) -1;// ѡ�е�ģ���Ծ��Ծ�ID

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
		getActionBar().setTitle("ģ�⿼��");

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

		// û��ѡ���Ծ�����ǰ��ť������
		startButton.setEnabled(false);

		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MockExamGuideActivity.this);
				builder.setIcon(R.drawable.dialog_blue_icon).setTitle("��ܰ��ʾ").setMessage(getMessage())
						.setPositiveButton("��ʼ����", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

								/*******��ʼMockExam����ҳ��***********/
								Intent intent = new Intent(MockExamGuideActivity.this, MockExamActivity.class);
								intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_INCLUDE, includeMaterial);
								intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_ID, examId);
								startActivity(intent);
								overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
								finish();
							}
						}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						}).show();
			}
		});

	}

	/*****�����û�������Ϣ*******/
	private String getMessage() {
		String message = "";
		// ���ѡ���˰������Ϸ�����
		if (includeBox.isChecked()) {
			message = "  ��ѡ���˰������Ϸ����⣬ϵͳ���ܸ������������֣���Ҫ�����ݴ�������";
		} else {
			message = "  ��ѡ���˲��������Ϸ����⣬����ʱ�佫��Ϊ60���ӡ�";
		}
		return message;
	}

	/**
	 * ��ʾ�Ծ��б�Dialog
	 */
	private void showChooseExamDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(MockExamGuideActivity.this);
		builder.setIcon(R.drawable.switch_exam_icon).setTitle("��ѡ��Ҫģ�⿼�����⣺");
		builder.setSingleChoiceItems(getExamTitleList(), -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				examId = examinationList.get(which).getId();
			}
		});
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (examId != -1) {
					startButton.setEnabled(true);
					ExaminationService examinationService = ExaminationService.getInstance(MockExamGuideActivity.this);
					examRequest.setText("��������:  " + examinationService.loadExamination(examId).getExam_name());
				} else {
				}
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				examId = (long) -1;
			}
		});
		builder.setCancelable(false);
		builder.show();
	}

	/****�����Ծ���Ŀ����*****/
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
