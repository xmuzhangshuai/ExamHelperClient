package com.bishe.examhelper.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.QuestionDisplayAdapter;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.entities.Collection;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.QuestionType;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.service.CollectionService;
import com.bishe.examhelper.service.MaterialAnalysisService;
import com.bishe.examhelper.service.MultiChoiceService;
import com.bishe.examhelper.service.QuestionTypeService;
import com.bishe.examhelper.service.SingleChoiceService;

/**
 * 类名称：CollectionListDisplayActivity
 * 类描述：用于发布疑问时选择收藏题目
 * 创建人： 张帅
 * 创建时间：2014-4-14 下午5:33:05
 *
 */
public class CollectionListDisplayActivity extends BaseActivity {
	ListView mListView;
	List<Object> questionList;// 题目列表
	List<Collection> collectionList;// 收藏题目列表
	QuestionDisplayAdapter questionDisplayAdapter;
	ImageView noCollection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collectionlist_display);
		findViewById();

		questionList = new ArrayList<Object>();
		new InitData().execute();

		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mListView = (ListView) findViewById(R.id.collection_list);
		noCollection = (ImageView) findViewById(R.id.no_collection);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("收藏列表");

		/*********给ListView的每一条记录绑定事件，点击后进入题目答题页面************/
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent();
				mIntent.putExtra("question", (Serializable) questionList.get(position));
				// 设置结果，并进行传送  
				CollectionListDisplayActivity.this.setResult(2, mIntent);
				CollectionListDisplayActivity.this.finish();

			}
		});
	}

	/**
	 * 
	*    
	* 项目名称：ExamHelper   
	* 类名称：InitData   
	* 类描述：  异步任务初始化数据 
	* 创建人：张帅  
	* 创建时间：2014-1-7 上午10:11:45   
	*
	 */
	private class InitData extends AsyncTask<Void, Void, Void> {
		SingleChoiceService mSingleChoiceService;
		MultiChoiceService multiChoiceService;
		MaterialAnalysisService materialAnalysisService;
		QuestionTypeService mQuestionTypeService;
		CollectionService mCollectionService;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mSingleChoiceService = SingleChoiceService.getInstance(CollectionListDisplayActivity.this);
			multiChoiceService = MultiChoiceService.getInstance(CollectionListDisplayActivity.this);
			materialAnalysisService = MaterialAnalysisService.getInstance(CollectionListDisplayActivity.this);
			mQuestionTypeService = QuestionTypeService.getInstance(CollectionListDisplayActivity.this);
			mCollectionService = CollectionService.getInstance(CollectionListDisplayActivity.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			collectionList = mCollectionService.loadCurrentCollections();

			for (Collection collection : collectionList) {
				QuestionType questionType = mQuestionTypeService.loadQuestionType(collection.getQuestionType_id());
				/*******如果是单选题********/
				if (questionType.getType_name().equals(DefaultValues.SINGLE_CHOICE)) {
					SingleChoice mSingleChoice;
					mSingleChoice = mSingleChoiceService.loadSingleChoice(collection.getQuestion_id());
					if (mSingleChoice != null) {
						questionList.add(mSingleChoice);
					}
				}

				/*******如果是多选题********/
				if (questionType.getType_name().equals(DefaultValues.MULTI_CHOICE)) {
					MultiChoice multiChoice;
					multiChoice = multiChoiceService.loadMultiChoice(collection.getQuestion_id());
					if (multiChoice != null) {
						questionList.add(multiChoice);
					}
				}

				/*******如果是材料题********/
				if (questionType.getType_name().equals(DefaultValues.MATERIAL_ANALYSIS)) {
					MaterialAnalysis materialAnalysis;
					materialAnalysis = materialAnalysisService.loadMaterialAnalysis(collection.getQuestion_id());
					if (materialAnalysis != null) {
						questionList.add(materialAnalysis);
					}
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (questionList.size() > 0) {
				questionDisplayAdapter = new QuestionDisplayAdapter(CollectionListDisplayActivity.this, questionList);
				mListView.setAdapter(questionDisplayAdapter);
			} else {
				mListView.setVisibility(View.GONE);
				noCollection.setVisibility(View.VISIBLE);
			}

		}

	}

}
