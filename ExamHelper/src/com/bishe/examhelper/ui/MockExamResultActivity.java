package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.config.DefaultKeys;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�MockExamResultFragment   
* ��������   չʾ���������Activity
* �����ˣ���˧  
* ����ʱ�䣺2014-1-11 ����5:27:21   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-11 ����5:27:21   
* �޸ı�ע��   
* @version    
*    
*/
public class MockExamResultActivity extends BaseActivity {
	/*******Views**********/
	TextView exam_titleTextView;// �Ծ���Ŀ
	TextView total_scoreTextView;// �ܷ���
	TextView my_scoreTextView;// �ҵĵ÷�
	TextView single_accuracyTextView;// ��ѡ����ȷ��
	TextView multi_accuracyTextView;// ��ѡ����ȷ��
	ImageView pass_img;// ����ͨ��ͼƬ
	Button check_detail_btn;// �鿴���鰴ť

	/********��Ŀ����Ϣ***********/
	boolean[] singleAnswers;// �û���ѡ�������
	boolean[] multiAnswers;// �û���ѡ�������
	String examTitleString;// �Ծ���Ŀ
	int totalScore = 0;// �ܷ���
	int mySingleScore = 0;// �ҵĵ�ѡ��÷�
	int myMultiScore = 0;// �ҵĶ�ѡ��÷�
	float singleChoiceAccuracy;// ��ѡ����ȷ��
	float multiChoiceAccuracy;// ��ѡ����ȷ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mock_exam_result);

		singleAnswers = getIntent().getBooleanArrayExtra(DefaultKeys.KEY_MOCK_EXAM_SINGLE_RESULT);
		multiAnswers = getIntent().getBooleanArrayExtra(DefaultKeys.KEY_MOCK_EXAM_MULTI_RESULT);
		examTitleString = getIntent().getStringExtra(DefaultKeys.KEY_MOCK_EXAM_TITLE);

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
		exam_titleTextView = (TextView) findViewById(R.id.exam_title);
		total_scoreTextView = (TextView) findViewById(R.id.total_score);
		my_scoreTextView = (TextView) findViewById(R.id.my_score);
		single_accuracyTextView = (TextView) findViewById(R.id.single_accuracy);
		multi_accuracyTextView = (TextView) findViewById(R.id.multi_accuracy);
		pass_img = (ImageView) findViewById(R.id.pass_img);
		check_detail_btn = (Button) findViewById(R.id.check_detail_btn);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// ��ʼ���ܷ���
		totalScore = singleAnswers.length + multiAnswers.length * 2;

		// ��ʼ���ҵķ���
		for (int i = 0; i < singleAnswers.length; i++) {
			if (singleAnswers[i]) {
				mySingleScore++;
			}
		}
		for (int i = 0; i < multiAnswers.length; i++) {
			if (multiAnswers[i]) {
				myMultiScore = myMultiScore + 2;
			}
		}

		// ��ʼ����ѡ����ȷ��
		singleChoiceAccuracy = (float) mySingleScore / (float) singleAnswers.length;

		// ��ʼ����ѡ����ȷ��
		multiChoiceAccuracy = (float) myMultiScore / (float) (multiAnswers.length * 2);

		exam_titleTextView.setText("�Ծ���Ŀ��" + examTitleString);// �����Ծ���Ŀ
		total_scoreTextView.setText("�ܷ�����" + totalScore);// �����ܷ���
		my_scoreTextView.setText("���ĵ÷֣�" + (mySingleScore + myMultiScore));// �����ҵĵ÷�
		single_accuracyTextView.setText("��ѡ����ȷ�ʣ� " + (int) (singleChoiceAccuracy * 100) + "%");// ���õ�ѡ����ȷ��
		multi_accuracyTextView.setText("��ѡ����ȷ�ʣ� " + (int) (multiChoiceAccuracy * 100) + "%");// ���ö�ѡ����ȷ��

		// ������Բ�ͨ��������ͼƬ
		if ((float) (mySingleScore + myMultiScore) / totalScore < 0.6) {
			pass_img.setImageResource(R.drawable.test_fail);
		}

		check_detail_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent();
				// intent.putExtra(DefaultKeys.KEY_QUESTION_MODEL,
				// DefaultValues.MODEL_EXERCISE);
				// setResult(2, intent);
				// finish();
			}
		});

	}
}
