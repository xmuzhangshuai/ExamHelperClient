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
* 项目名称：ExamHelper   
* 类名称：MockExamResultFragment   
* 类描述：   展示答题情况的Activity
* 创建人：张帅  
* 创建时间：2014-1-11 下午5:27:21   
* 修改人：张帅   
* 修改时间：2014-1-11 下午5:27:21   
* 修改备注：   
* @version    
*    
*/
public class MockExamResultActivity extends BaseActivity {
	/*******Views**********/
	TextView exam_titleTextView;// 试卷题目
	TextView total_scoreTextView;// 总分数
	TextView my_scoreTextView;// 我的得分
	TextView single_accuracyTextView;// 单选题正确率
	TextView multi_accuracyTextView;// 多选题正确率
	ImageView pass_img;// 考试通过图片
	Button check_detail_btn;// 查看详情按钮

	/********题目答案信息***********/
	boolean[] singleAnswers;// 用户单选题答案正误
	boolean[] multiAnswers;// 用户多选题答案正误
	String examTitleString;// 试卷题目
	int totalScore = 0;// 总分数
	int mySingleScore = 0;// 我的单选题得分
	int myMultiScore = 0;// 我的多选题得分
	float singleChoiceAccuracy;// 单选题正确率
	float multiChoiceAccuracy;// 多选题正确率

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
		// 初始化总分数
		totalScore = singleAnswers.length + multiAnswers.length * 2;

		// 初始化我的分数
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

		// 初始化单选题正确率
		singleChoiceAccuracy = (float) mySingleScore / (float) singleAnswers.length;

		// 初始化多选题正确率
		multiChoiceAccuracy = (float) myMultiScore / (float) (multiAnswers.length * 2);

		exam_titleTextView.setText("试卷题目：" + examTitleString);// 设置试卷题目
		total_scoreTextView.setText("总分数：" + totalScore);// 设置总分数
		my_scoreTextView.setText("您的得分：" + (mySingleScore + myMultiScore));// 设置我的得分
		single_accuracyTextView.setText("单选题正确率： " + (int) (singleChoiceAccuracy * 100) + "%");// 设置单选题正确率
		multi_accuracyTextView.setText("多选题正确率： " + (int) (multiChoiceAccuracy * 100) + "%");// 设置多选题正确率

		// 如果考试不通过，设置图片
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
