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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.CommonListAdapter;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dbService.ErrorQuestionsService;
import com.bishe.examhelper.entities.ErrorQuestions;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�ByFrequencyFragment   
* ��������   ������Ƶ����ʾ�����б���Ϊ�����λ����ϡ������Ρ���һ�ε����飬ÿ�鰴�ճ�����������ݿ�ȡ���ݣ�
*          ÿ����Ϊһ��ErrorQuestions��List�����ĳ�������ĿԤ��ҳ�棬����һ��List<ErrorQuestions> 
* �����ˣ���˧  
* ����ʱ�䣺2014-1-8 ����4:53:10   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-8 ����4:53:10   
* �޸ı�ע��   
* @version    
*    
*/
public class ByFrequencyFragment extends BaseV4Fragment {
	View rootView;
	private ListView listView;
	private CommonListAdapter mAdapter;
	private List<List<ErrorQuestions>> errorQuestionList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.common_list, container, false);
		findViewById();
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new InitData().execute();
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

				/*********����Ǵ����б�************/
				if (errorQuestionList.get(position).size() > 0) {
					Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
					intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, DefaultValues.BY_ERROR);// ��������
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
			titleList.add("�����λ�����");
			titleList.add("������");
			titleList.add("��һ��");

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// ��ʼ��errorQuestionList
			errorQuestionList = new ArrayList<List<ErrorQuestions>>();
			for (int i = 0; i < 3; i++) {
				List<ErrorQuestions> list = new ArrayList<ErrorQuestions>();
				errorQuestionList.add(list);
			}

			// �洢���еĴ���
			List<ErrorQuestions> allErrorList = new ArrayList<ErrorQuestions>();

			ErrorQuestionsService errorQuestionsService = ErrorQuestionsService.getInstance(getActivity());
			allErrorList = errorQuestionsService.loadAllErrorQuestions();

			/************ѭ���жϳ���ʱ��������ڵ�ʱ����,�����з���*****************/
			for (ErrorQuestions errorQuestions : allErrorList) {
				int times = errorQuestions.getError_num();

				if (times >= 3) {// ���������������
					errorQuestionList.get(0).add(errorQuestions);
				} else if (times == 2) {// �����������
					errorQuestionList.get(1).add(errorQuestions);
				} else if (times == 1) {// �������һ��
					errorQuestionList.get(2).add(errorQuestions);
				}
			}

			/****��ʼ��subtitleList,�����Ŀ����********/
			subtitleList = new ArrayList<String>();
			for (List<ErrorQuestions> list : errorQuestionList) {
				subtitleList.add("��" + list.size() + "��");
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
