package com.bishe.examhelper.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.CommonListAdapter;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dbService.SectionService;
import com.bishe.examhelper.entities.Section;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：CollectionBySectionFragment   
* 类描述：   按章节展开收藏列表的Fragment
* 创建人：张帅  
* 创建时间：2014-1-5 下午2:39:05   
* 修改人：张帅   
* 修改时间：2014-1-5 下午2:39:05   
* 修改备注：   
* @version    
*    
*/
public class BySectionFragment extends BaseV4Fragment {
	View rootView;
	private ListView listView;
	private List<Section> sectionList;// 章节列表
	private CommonListAdapter mAdapter;
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
			initView();
		}
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
				if (type.equals(DefaultValues.BY_COLLECTION)
						&& sectionList.get(position).getCollectionList().size() > 0) {
					Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
					intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, type);//设置类型
					intent.putExtra(DefaultKeys.COLLECTION_LIST, (Serializable) sectionList.get(position)
							.getCollectionList());
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}

				/*********如果是错题列表************/
				if (type.equals(DefaultValues.BY_ERROR)
						&& sectionList.get(position).getErrorQuestionsList().size() > 0) {
					Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
					intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, type);//设置类型
					//传错题列表参数
					intent.putExtra(DefaultKeys.ERROR_LIST, (Serializable) sectionList.get(position)
							.getErrorQuestionsList());
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
//		private ProgressDialog progressDialog;
		private List<String> titleList;// 章节名称
		private List<String> subtitleList;// 错题个数

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*******加载进度********/
//			progressDialog = new ProgressDialog(getActivity());
//			progressDialog.setMessage("请稍候，正在努力加载...");
//			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			/***********取出章节列表****************/
			SectionService mSectionService = SectionService.getInstance(getActivity());
			sectionList = mSectionService.getSectionsBySubjectName(DefaultValues.SUBJECT_POLITICAL_EXAM);

			titleList = new ArrayList<String>();
			subtitleList = new ArrayList<String>();

			/*********如果是收藏列表************/
			if (type.equals(DefaultValues.BY_COLLECTION) && sectionList != null) {
				/***********取出每个章节的题目和收藏题目的数量****************/
				for (Section section : sectionList) {
					section.resetCollectionList();
					titleList.add(section.getSection_name());
					subtitleList.add("共" + section.getCollectionList().size() + "题");
				}
			}

			/*********如果是错题列表************/
			if (type.equals(DefaultValues.BY_ERROR) && sectionList != null) {
				/***********取出每个章节的题目和错题的数量****************/
				for (Section section : sectionList) {
					section.resetErrorQuestionsList();
					titleList.add(section.getSection_name());
					subtitleList.add("共" + section.getErrorQuestionsList().size() + "题");
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
//			progressDialog.dismiss();
		}

	}

}
