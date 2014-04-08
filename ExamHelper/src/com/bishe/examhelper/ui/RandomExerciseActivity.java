package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.RandomExerciseAdapter;
import com.bishe.examhelper.base.BaseFragmentActivity;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.service.MaterialAnalysisService;
import com.bishe.examhelper.service.MultiChoiceService;
import com.bishe.examhelper.service.SingleChoiceService;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�RandomExerciseActivity   
* ��������   �����ϰActivity��
* �����ˣ���˧  
* ����ʱ�䣺2014-1-7 ����9:03:36   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-7 ����9:03:36   
* �޸ı�ע��   
* @version    
*    
*/
public class RandomExerciseActivity extends BaseFragmentActivity {

	ViewPager mViewPager;
	RandomExerciseAdapter randomExerciseAdapter;
	List<Question> questionList;// ��Ŀ�б�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_activity);
		questionList = new ArrayList<Question>();

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
		mViewPager = ((ViewPager) findViewById(R.id.pager));
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("�����ϰ");

		// ��ʼ�����ʾ������Ŀ
		questionList.add(getRandomQuestion());
		questionList.add(getRandomQuestion());

		// �½�Adapter
		final RandomExerciseAdapter randomExerciseAdapter = new RandomExerciseAdapter(getSupportFragmentManager(),
				questionList);
		mViewPager.setAdapter(randomExerciseAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int state) {
				// TODO Auto-generated method stub
				/*****����������ǰ��Ŀʱ�����´����ݿ�ȡ�������һ��*****/
				questionList.add(getRandomQuestion());
				/******����Adapter*********/
				randomExerciseAdapter.notifyDataSetChanged();
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int position) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * �������һ����Ŀ
	 * @return
	 */
	public Question getRandomQuestion() {
		Question question = null;
		SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(this);
		MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(this);
		MaterialAnalysisService materialAnalysisService = MaterialAnalysisService.getInstance(this);

		int type;// ��Ŀ����
		Random random = new Random();
		type = random.nextInt(3);

		switch (type) {
		case 0:// ��ѡ��
			question = singleChoiceService.getRandomSingleChoice();
			question.setQuestion_type(DefaultValues.SINGLE_CHOICE);
			break;
		case 1:// ��ѡ��
			question = multiChoiceService.getRandomMultiChoice();
			question.setQuestion_type(DefaultValues.MULTI_CHOICE);
			break;
		case 2:// ������
			question = materialAnalysisService.getRandomMaterialAnalysis();
			question.setQuestion_type(DefaultValues.MATERIAL_ANALYSIS);
			break;
		default:
			break;
		}

		return question;
	}

}
