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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�QuestionSDisplayActivity   
* ��������   ����չʾ��Ŀ�ĸ�Ҫ��ͼ�����ҵ��ղء������طŵȹ��ܻ����Ŀ�б���ʾ��Ŀ��ɼ��𰸵ĸ�Ҫ��ͼ��
* �����ˣ���˧  
* ����ʱ�䣺2014-1-6 ����9:47:58   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-6 ����9:47:58   
* �޸ı�ע��   
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
		getActionBar().setTitle("��Ŀչʾ");
		// �����л�Ч��
		fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in,
				R.anim.push_right_out);
		/*********������ղ��б�************/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals(DefaultValues.BY_COLLECTION)) {
			CollectionDisplayListFragment mFragment = new CollectionDisplayListFragment();
			fragmentTransaction.replace(R.id.display_fragment_container, mFragment);
		}

		/*********����Ǵ����б�************/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals(DefaultValues.BY_ERROR)) {
			ErrorDisplayListFragment mFragment = new ErrorDisplayListFragment();
			fragmentTransaction.replace(R.id.display_fragment_container, mFragment);
		}

		/***********�����չʾ��Ŀ**********/
		if (getIntent().getStringExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE).equals("displsyQuestion")) {
			int number = getIntent().getIntExtra("questionNumber", 1);
			int questionId = getIntent().getIntExtra("questionId", 1);
			int questionTypeId = getIntent().getIntExtra("questionTypeId", 1);
			Object object = QuestionService.getInstance(QuestionsDisplayActivity.this).getQuestionObject(
					new Long(questionId), new Long(questionTypeId));

			/**********����ǵ�ѡ��**********/
			if (object.getClass().getSimpleName().equals(SingleChoice.class.getSimpleName())) {
				SingleChoice singleChoice = (SingleChoice) object;
				// �½���ѡ��ҳ��
				SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
				Bundle localBundle = new Bundle();

				// ����һ��singleChoice����
				localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE, singleChoice);
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, number);
				mySingleChoiceFragment.setArguments(localBundle);

				// �滻��ǰҳ��
				fragmentTransaction.replace(R.id.display_fragment_container, mySingleChoiceFragment);
			}

			/**********����Ƕ�ѡ��**********/
			if (object.getClass().getSimpleName().equals(MultiChoice.class.getSimpleName())) {
				MultiChoice multiChoice = (MultiChoice) object;

				// �½���ѡ��ҳ��
				MultiChoiceFragment multiChoiceFragment = new MultiChoiceFragment();
				Bundle localBundle = new Bundle();

				// ����һ��multiChoice����
				localBundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE, multiChoice);
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, number);
				multiChoiceFragment.setArguments(localBundle);

				// �滻��ǰҳ��
				fragmentTransaction.replace(R.id.display_fragment_container, multiChoiceFragment);
			}

			/**********����ǲ�����**********/
			if (object.getClass().getSimpleName().equals(MaterialAnalysis.class.getSimpleName())) {
				MaterialAnalysis materialAnalysis = (MaterialAnalysis) object;

				// �½�������ҳ��
				MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
				Bundle localBundle = new Bundle();

				// ����һ��materialAnalysis����
				localBundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS, materialAnalysis);
				localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, number);
				materialAnalysisFragment.setArguments(localBundle);

				// �滻��ǰҳ��
				fragmentTransaction.replace(R.id.display_fragment_container, materialAnalysisFragment);
			}

		}

		fragmentTransaction.commit();

	}

}
