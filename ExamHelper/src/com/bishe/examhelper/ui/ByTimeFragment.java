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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�ByTimeFragment   
* ��������   ��ʱ����ʾ�����б���Ϊ�����ڡ������ڡ�һ���ڡ�һ���ڡ����õ����飬ÿ�鰴��ʱ������ݿ�ȡ���ݣ�
*          ÿ����Ϊһ��ErrorQuestions��List�����ĳ�������ĿԤ��ҳ�棬����һ��List<ErrorQuestions> 
* �����ˣ���˧  
* ����ʱ�䣺2014-1-8 ����3:49:58   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-8 ����3:49:58   
* �޸ı�ע��   
* @version    
*    
*/
public class ByTimeFragment extends BaseV4Fragment {
	View rootView;
	private ListView listView;
	private CommonListAdapter mAdapter;
	private List<List<ErrorQuestions>> errorQuestionList;
	private List<List<Collection>> collectionList;
	private String type;// Ҫչʾ�����ͣ�����⡢�ղ���Ŀ��

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.common_list, container, false);
		findViewById();
		// ��ȡ����
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
		/***********��ListView��Item���¼�����������QuestionDisplayActivityҳ��*******/
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				/*********������ղ��б�************/
				if (type.equals(DefaultValues.BY_COLLECTION) && collectionList.get(position).size() > 0) {
					Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
					intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, type);// ��������
					intent.putExtra(DefaultKeys.COLLECTION_LIST, (Serializable) collectionList.get(position));
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}

				/*********����Ǵ����б�************/
				if (type.equals(DefaultValues.BY_ERROR) && errorQuestionList.get(position).size() > 0) {
					Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
					intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, type);// ��������
					// �������б����
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
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�InitData   
	* ��������   �����ݿ���ȡ����
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-6 ����4:39:27   
	*
	 */
	private class InitData extends AsyncTask<Void, Void, Void> {
		// private ProgressDialog progressDialog;
		private List<String> titleList;// ʱ�䷶Χ����
		private List<String> subtitleList;// �������

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*******���ؽ���********/
			// progressDialog = new ProgressDialog(getActivity());
			// progressDialog.setMessage("���Ժ�����Ŭ������...");
			// progressDialog.show();

			titleList = new ArrayList<String>();
			titleList.add("������");
			titleList.add("������");
			titleList.add("һ����");
			titleList.add("һ����");
			titleList.add("���õ�");

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			/*********������ղ��б�************/
			if (type.equals(DefaultValues.BY_COLLECTION)) {
				// ��ʼ��collectionList
				collectionList = new ArrayList<List<Collection>>();
				for (int i = 0; i < 5; i++) {
					List<Collection> list = new ArrayList<Collection>();
					collectionList.add(list);
				}

				// �洢���еĴ���
				List<Collection> allCollectionList = new ArrayList<Collection>();
				CollectionService collectionService = CollectionService.getInstance(getActivity());
				allCollectionList = collectionService.loadAllCollections();

				/************ѭ���жϳ���ʱ��������ڵ�ʱ����,�����з���*****************/
				for (Collection collection : allCollectionList) {
					Map<String, Integer> interval = DateTimeTools.compareTo(DateTimeTools.getCurrentDate(),
							collection.getCollect_time());
					Integer dayInterval = interval.get("month");
					if (dayInterval < 1) {// �������С��1
						collectionList.get(0).add(collection);
					} else if (dayInterval < 3) {// ���������
						collectionList.get(1).add(collection);
					} else if (dayInterval < 7) {// ���һ����
						collectionList.get(2).add(collection);
					} else if (dayInterval < 30) {// ���һ����
						collectionList.get(3).add(collection);
					} else {// ���õ�
						collectionList.get(4).add(collection);
					}
				}

				/****��ʼ��subtitleList,�����Ŀ����********/
				subtitleList = new ArrayList<String>();
				for (List<Collection> list : collectionList) {
					subtitleList.add("��" + list.size() + "��");
				}

			}

			/*********����Ǵ����б�************/
			if (type.equals(DefaultValues.BY_ERROR)) {
				// ��ʼ��errorQuestionList
				errorQuestionList = new ArrayList<List<ErrorQuestions>>();
				for (int i = 0; i < 5; i++) {
					List<ErrorQuestions> list = new ArrayList<ErrorQuestions>();
					errorQuestionList.add(list);
				}

				// �洢���еĴ���
				List<ErrorQuestions> allErrorList = new ArrayList<ErrorQuestions>();

				ErrorQuestionsService errorQuestionsService = ErrorQuestionsService.getInstance(getActivity());
				allErrorList = errorQuestionsService.loadAllErrorQuestions();

				/************ѭ���жϳ���ʱ��������ڵ�ʱ����,�����з���*****************/
				for (ErrorQuestions errorQuestions : allErrorList) {
					Map<String, Integer> interval = DateTimeTools.compareTo(DateTimeTools.getCurrentDate(),
							errorQuestions.getError_time());
					Integer dayInterval = interval.get("month");
					if (dayInterval < 1) {// �������С��1
						errorQuestionList.get(0).add(errorQuestions);
					} else if (dayInterval < 3) {// ���������
						errorQuestionList.get(1).add(errorQuestions);
					} else if (dayInterval < 7) {// ���һ����
						errorQuestionList.get(2).add(errorQuestions);
					} else if (dayInterval < 30) {// ���һ����
						errorQuestionList.get(3).add(errorQuestions);
					} else {// ���õ�
						errorQuestionList.get(4).add(errorQuestions);
					}
				}

				/****��ʼ��subtitleList,�����Ŀ����********/
				subtitleList = new ArrayList<String>();
				for (List<ErrorQuestions> list : errorQuestionList) {
					subtitleList.add("��" + list.size() + "��");
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			/************�½�Adapter***************/
			mAdapter = new CommonListAdapter(getActivity(), titleList, subtitleList);
			listView.setAdapter(mAdapter);
			// progressDialog.dismiss();
		}

	}

}
