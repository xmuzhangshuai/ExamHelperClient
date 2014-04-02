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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�CollectionBySectionFragment   
* ��������   ���½�չ���ղ��б��Fragment
* �����ˣ���˧  
* ����ʱ�䣺2014-1-5 ����2:39:05   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-5 ����2:39:05   
* �޸ı�ע��   
* @version    
*    
*/
public class BySectionFragment extends BaseV4Fragment {
	View rootView;
	private ListView listView;
	private List<Section> sectionList;// �½��б�
	private CommonListAdapter mAdapter;
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
		/***********��ListView��Item���¼�����������QuestionDisplayActivityҳ��*******/
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				/*********������ղ��б�************/
				if (type.equals(DefaultValues.BY_COLLECTION)
						&& sectionList.get(position).getCollectionList().size() > 0) {
					Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
					intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, type);//��������
					intent.putExtra(DefaultKeys.COLLECTION_LIST, (Serializable) sectionList.get(position)
							.getCollectionList());
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}

				/*********����Ǵ����б�************/
				if (type.equals(DefaultValues.BY_ERROR)
						&& sectionList.get(position).getErrorQuestionsList().size() > 0) {
					Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
					intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, type);//��������
					//�������б����
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
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�InitData   
	* ��������   �����ݿ���ȡ����
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-6 ����4:39:27   
	*
	 */
	private class InitData extends AsyncTask<Void, Void, Void> {
//		private ProgressDialog progressDialog;
		private List<String> titleList;// �½�����
		private List<String> subtitleList;// �������

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*******���ؽ���********/
//			progressDialog = new ProgressDialog(getActivity());
//			progressDialog.setMessage("���Ժ�����Ŭ������...");
//			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			/***********ȡ���½��б�****************/
			SectionService mSectionService = SectionService.getInstance(getActivity());
			sectionList = mSectionService.getSectionsBySubjectName(DefaultValues.SUBJECT_POLITICAL_EXAM);

			titleList = new ArrayList<String>();
			subtitleList = new ArrayList<String>();

			/*********������ղ��б�************/
			if (type.equals(DefaultValues.BY_COLLECTION) && sectionList != null) {
				/***********ȡ��ÿ���½ڵ���Ŀ���ղ���Ŀ������****************/
				for (Section section : sectionList) {
					section.resetCollectionList();
					titleList.add(section.getSection_name());
					subtitleList.add("��" + section.getCollectionList().size() + "��");
				}
			}

			/*********����Ǵ����б�************/
			if (type.equals(DefaultValues.BY_ERROR) && sectionList != null) {
				/***********ȡ��ÿ���½ڵ���Ŀ�ʹ��������****************/
				for (Section section : sectionList) {
					section.resetErrorQuestionsList();
					titleList.add(section.getSection_name());
					subtitleList.add("��" + section.getErrorQuestionsList().size() + "��");
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
//			progressDialog.dismiss();
		}

	}

}
