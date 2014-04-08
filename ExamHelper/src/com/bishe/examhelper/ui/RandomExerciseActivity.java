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
* 项目名称：ExamHelper   
* 类名称：RandomExerciseActivity   
* 类描述：   随机练习Activity，
* 创建人：张帅  
* 创建时间：2014-1-7 下午9:03:36   
* 修改人：张帅   
* 修改时间：2014-1-7 下午9:03:36   
* 修改备注：   
* @version    
*    
*/
public class RandomExerciseActivity extends BaseFragmentActivity {

	ViewPager mViewPager;
	RandomExerciseAdapter randomExerciseAdapter;
	List<Question> questionList;// 题目列表

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
		getActionBar().setTitle("随机练习");

		// 初始随机显示两个题目
		questionList.add(getRandomQuestion());
		questionList.add(getRandomQuestion());

		// 新建Adapter
		final RandomExerciseAdapter randomExerciseAdapter = new RandomExerciseAdapter(getSupportFragmentManager(),
				questionList);
		mViewPager.setAdapter(randomExerciseAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int state) {
				// TODO Auto-generated method stub
				/*****当滑动到当前题目时，重新从数据库取题放在下一题*****/
				questionList.add(getRandomQuestion());
				/******更新Adapter*********/
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
	 * 随机返回一个题目
	 * @return
	 */
	public Question getRandomQuestion() {
		Question question = null;
		SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(this);
		MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(this);
		MaterialAnalysisService materialAnalysisService = MaterialAnalysisService.getInstance(this);

		int type;// 题目类型
		Random random = new Random();
		type = random.nextInt(3);

		switch (type) {
		case 0:// 单选题
			question = singleChoiceService.getRandomSingleChoice();
			question.setQuestion_type(DefaultValues.SINGLE_CHOICE);
			break;
		case 1:// 多选题
			question = multiChoiceService.getRandomMultiChoice();
			question.setQuestion_type(DefaultValues.MULTI_CHOICE);
			break;
		case 2:// 材料题
			question = materialAnalysisService.getRandomMaterialAnalysis();
			question.setQuestion_type(DefaultValues.MATERIAL_ANALYSIS);
			break;
		default:
			break;
		}

		return question;
	}

}
