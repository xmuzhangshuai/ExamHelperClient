package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.service.QuestionService;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：QuestionSDisplayActivity   
* 类描述：   用于展示题目的概要视图，从我的收藏、错题重放等功能获得题目列表，显示题目题干及答案的概要视图。
* 创建人：张帅  
* 创建时间：2014-1-6 下午9:47:58   
* 修改人：张帅   
* 修改时间：2014-1-6 下午9:47:58   
* 修改备注：   
* @version    
*    
*/
public class QuestionsDisplayActivity extends BaseFragmentActivity {
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_display);
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

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
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("题目展示");
		// 设置切换效果
		fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in,
				R.anim.push_right_out);
		/*********如果是收藏列表************/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals(DefaultValues.BY_COLLECTION)) {
			CollectionDisplayListFragment mFragment = new CollectionDisplayListFragment();
			fragmentTransaction.replace(R.id.display_fragment_container, mFragment);
		}

		/*********如果是错题列表************/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals(DefaultValues.BY_ERROR)) {
			ErrorDisplayListFragment mFragment = new ErrorDisplayListFragment();
			fragmentTransaction.replace(R.id.display_fragment_container, mFragment);
		}

		/***********如果是展示题目**********/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals("displsyQuestion")) {
			int number = getIntent().getIntExtra("questionNumber", 1);
			int questionId = getIntent().getIntExtra("questionId", 1);
			int questionTypeId = getIntent().getIntExtra("questionTypeId", 1);
			Object object = QuestionService.getInstance(QuestionsDisplayActivity.this).getQuestionObject(
					new Long(questionId), new Long(questionTypeId));

			/**********如果是单选题**********/
			if (object.getClass().getSimpleName().equals(SingleChoice.class.getSimpleName())) {
				SingleChoice singleChoice = (SingleChoice) object;
				// 新建单选题页面
				SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
				Bundle localBundle = new Bundle();

				// 传入一个singleChoice对象
				localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE, singleChoice);
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, number);
				mySingleChoiceFragment.setArguments(localBundle);

				// 替换当前页面
				fragmentTransaction.replace(R.id.display_fragment_container, mySingleChoiceFragment);
			}

			/**********如果是多选题**********/
			if (object.getClass().getSimpleName().equals(MultiChoice.class.getSimpleName())) {
				MultiChoice multiChoice = (MultiChoice) object;

				// 新建多选题页面
				MultiChoiceFragment multiChoiceFragment = new MultiChoiceFragment();
				Bundle localBundle = new Bundle();

				// 传入一个multiChoice对象
				localBundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE, multiChoice);
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, number);
				multiChoiceFragment.setArguments(localBundle);

				// 替换当前页面
				fragmentTransaction.replace(R.id.display_fragment_container, multiChoiceFragment);
			}

			/**********如果是材料题**********/
			if (object.getClass().getSimpleName().equals(MaterialAnalysis.class.getSimpleName())) {
				MaterialAnalysis materialAnalysis = (MaterialAnalysis) object;

				// 新建材料题页面
				MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
				Bundle localBundle = new Bundle();

				// 传入一个materialAnalysis对象
				localBundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS, materialAnalysis);
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, number);
				materialAnalysisFragment.setArguments(localBundle);

				// 替换当前页面
				fragmentTransaction.replace(R.id.display_fragment_container, materialAnalysisFragment);
			}

		}

		fragmentTransaction.commit();

	}

}
