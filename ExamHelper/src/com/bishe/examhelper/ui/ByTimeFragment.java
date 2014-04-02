package com.bishe.examhelper.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.CommonListAdapter;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dbService.CollectionService;
import com.bishe.examhelper.dbService.ErrorQuestionsService;
import com.bishe.examhelper.entities.Collection;
import com.bishe.examhelper.entities.ErrorQuestions;
import com.bishe.examhelper.utils.DateTimeTools;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：ByTimeFragment   
* 类描述：   按时间显示错题列表，分为当日内、三日内、一周内、一月内、更久的五组，每组按照时间从数据库取数据，
*          每组内为一个ErrorQuestions的List，点击某组进入题目预览页面，传递一个List<ErrorQuestions> 
* 创建人：张帅  
* 创建时间：2014-1-8 下午3:49:58   
* 修改人：张帅   
* 修改时间：2014-1-8 下午3:49:58   
* 修改备注：   
* @version    
*    
*/
public class ByTimeFragment extends BaseV4Fragment {
	View rootView;
	private ListView listView;
	private CommonListAdapter mAdapter;
	private List<List<ErrorQuestions>> errorQuestionList;
	private List<List<Collection>> collectionList;
	private String type;// 要展示的类型，如错题、收藏题目等

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.common_list, container, false);
		findViewById();
		// 获取类型
		type = getArguments().getString(DefaultKeys.BY_SECTION_FRAGMENT_TYPE);
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (type != null) {
			new InitData().execute();
		}
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		listView = (ListView) rootView.findViewById(R.id.common_ListView);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		/***********给ListView的Item绑定事件，点击后进入QuestionDisplayActivity页面*******/
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				/*********如果是收藏列表************/
				if (type.equals(DefaultValues.BY_COLLECTION) && collectionList.get(position).size() > 0) {
					Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
					intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, type);// 设置类型
					intent.putExtra(DefaultKeys.COLLECTION_LIST, (Serializable) collectionList.get(position));
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}

				/*********如果是错题列表************/
				if (type.equals(DefaultValues.BY_ERROR) && errorQuestionList.get(position).size() > 0) {
					Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
					intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, type);// 设置类型
					// 传错题列表参数
					intent.putExtra(DefaultKeys.ERROR_LIST, (Serializable) errorQuestionList.get(position));
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			}
		});
	}

	/**
	 * 
	*    
	* 项目名称：ExamHelper   
	* 类名称：InitData   
	* 类描述：   从数据库中取数据
	* 创建人：张帅  
	* 创建时间：2014-1-6 下午4:39:27   
	*
	 */
	private class InitData extends AsyncTask<Void, Void, Void> {
		// private ProgressDialog progressDialog;
		private List<String> titleList;// 时间范围名称
		private List<String> subtitleList;// 错题个数

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*******加载进度********/
			// progressDialog = new ProgressDialog(getActivity());
			// progressDialog.setMessage("请稍候，正在努力加载...");
			// progressDialog.show();

			titleList = new ArrayList<String>();
			titleList.add("当日内");
			titleList.add("三日内");
			titleList.add("一周内");
			titleList.add("一月内");
			titleList.add("更久的");

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			/*********如果是收藏列表************/
			if (type.equals(DefaultValues.BY_COLLECTION)) {
				// 初始化collectionList
				collectionList = new ArrayList<List<Collection>>();
				for (int i = 0; i < 5; i++) {
					List<Collection> list = new ArrayList<Collection>();
					collectionList.add(list);
				}

				// 存储所有的错题
				List<Collection> allCollectionList = new ArrayList<Collection>();
				CollectionService collectionService = CollectionService.getInstance(getActivity());
				allCollectionList = collectionService.loadAllCollections();

				/************循环判断出错时间距离现在的时间间隔,并进行分组*****************/
				for (Collection collection : allCollectionList) {
					Map<String, Integer> interval = DateTimeTools.compareTo(DateTimeTools.getCurrentDate(),
							collection.getCollect_time());
					Integer dayInterval = interval.get("month");
					if (dayInterval < 1) {// 如果天数小于1
						collectionList.get(0).add(collection);
					} else if (dayInterval < 3) {// 如果三日内
						collectionList.get(1).add(collection);
					} else if (dayInterval < 7) {// 如果一周内
						collectionList.get(2).add(collection);
					} else if (dayInterval < 30) {// 如果一月内
						collectionList.get(3).add(collection);
					} else {// 更久的
						collectionList.get(4).add(collection);
					}
				}

				/****初始化subtitleList,填充题目个数********/
				subtitleList = new ArrayList<String>();
				for (List<Collection> list : collectionList) {
					subtitleList.add("共" + list.size() + "题");
				}

			}

			/*********如果是错题列表************/
			if (type.equals(DefaultValues.BY_ERROR)) {
				// 初始化errorQuestionList
				errorQuestionList = new ArrayList<List<ErrorQuestions>>();
				for (int i = 0; i < 5; i++) {
					List<ErrorQuestions> list = new ArrayList<ErrorQuestions>();
					errorQuestionList.add(list);
				}

				// 存储所有的错题
				List<ErrorQuestions> allErrorList = new ArrayList<ErrorQuestions>();

				ErrorQuestionsService errorQuestionsService = ErrorQuestionsService.getInstance(getActivity());
				allErrorList = errorQuestionsService.loadAllErrorQuestions();

				/************循环判断出错时间距离现在的时间间隔,并进行分组*****************/
				for (ErrorQuestions errorQuestions : allErrorList) {
					Map<String, Integer> interval = DateTimeTools.compareTo(DateTimeTools.getCurrentDate(),
							errorQuestions.getError_time());
					Integer dayInterval = interval.get("month");
					if (dayInterval < 1) {// 如果天数小于1
						errorQuestionList.get(0).add(errorQuestions);
					} else if (dayInterval < 3) {// 如果三日内
						errorQuestionList.get(1).add(errorQuestions);
					} else if (dayInterval < 7) {// 如果一周内
						errorQuestionList.get(2).add(errorQuestions);
					} else if (dayInterval < 30) {// 如果一月内
						errorQuestionList.get(3).add(errorQuestions);
					} else {// 更久的
						errorQuestionList.get(4).add(errorQuestions);
					}
				}

				/****初始化subtitleList,填充题目个数********/
				subtitleList = new ArrayList<String>();
				for (List<ErrorQuestions> list : errorQuestionList) {
					subtitleList.add("共" + list.size() + "题");
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			/************新建Adapter***************/
			mAdapter = new CommonListAdapter(getActivity(), titleList, subtitleList);
			listView.setAdapter(mAdapter);
			// progressDialog.dismiss();
		}

	}

}
