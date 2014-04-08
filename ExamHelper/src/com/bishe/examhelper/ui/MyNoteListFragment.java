package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.Note;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.service.MaterialAnalysisService;
import com.bishe.examhelper.service.MultiChoiceService;
import com.bishe.examhelper.service.NoteService;
import com.bishe.examhelper.service.QuestionTypeService;
import com.bishe.examhelper.service.SingleChoiceService;
import com.bishe.examhelper.utils.DateTimeTools;

public class MyNoteListFragment extends BaseV4Fragment {
	public static final String TAG = MyNoteListFragment.class.getSimpleName();

	/*********Views*******/
	private View rootView;
	private ListView myNoteListView;

	/********����*******/
	private List<Note> noteList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_my_note_list, null, false);
		findViewById();
		initView();

		new InitNoteListContent().execute();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		myNoteListView = (ListView) rootView.findViewById(R.id.note_listView);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// ����¼���¼�
		myNoteListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity()
						.getSupportFragmentManager().beginTransaction();
				QuestionTypeService questionTypeService = QuestionTypeService.getInstance(getActivity());

				/**********����ǵ�ѡ��**********/
				if (noteList.get(position).getQuestionType_id() == questionTypeService
						.getIdByName(DefaultValues.SINGLE_CHOICE)) {
					SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(getActivity());

					SingleChoice singleChoice = singleChoiceService.loadSingleChoice(noteList.get(position)
							.getQuestion_id());

					// �½���ѡ��ҳ��
					SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
					Bundle localBundle = new Bundle();

					// ����һ��singleChoice����
					localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE, singleChoice);
					localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
					mySingleChoiceFragment.setArguments(localBundle);

					// �滻��ǰҳ��
					fragmentTransaction.replace(R.id.fragment_container, mySingleChoiceFragment);
				}

				/**********����Ƕ�ѡ��**********/
				if (noteList.get(position).getQuestionType_id() == questionTypeService
						.getIdByName(DefaultValues.MULTI_CHOICE)) {
					MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(getActivity());
					MultiChoice multiChoice = multiChoiceService.loadMultiChoice(noteList.get(position)
							.getQuestion_id());

					// �½���ѡ��ҳ��
					MultiChoiceFragment multiChoiceFragment = new MultiChoiceFragment();
					Bundle localBundle = new Bundle();

					// ����һ��multiChoice����
					localBundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE, multiChoice);
					localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
					multiChoiceFragment.setArguments(localBundle);

					// �滻��ǰҳ��
					fragmentTransaction.replace(R.id.fragment_container, multiChoiceFragment);
				}

				/**********����ǲ�����**********/
				if (noteList.get(position).getQuestionType_id() == questionTypeService
						.getIdByName(DefaultValues.MATERIAL_ANALYSIS)) {
					MaterialAnalysisService materialAnalysisService = MaterialAnalysisService
							.getInstance(getActivity());
					MaterialAnalysis materialAnalysis = materialAnalysisService.loadMaterialAnalysis(noteList.get(
							position).getQuestion_id());

					// �½�������ҳ��
					MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
					Bundle localBundle = new Bundle();

					// ����һ��materialAnalysis����
					localBundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS, materialAnalysis);
					localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
					materialAnalysisFragment.setArguments(localBundle);

					// �滻��ǰҳ��
					fragmentTransaction.replace(R.id.fragment_container, materialAnalysisFragment);
				}

				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});
	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�InitNoteListContent   
	* ��������   �����ݿ�ȡ���ݣ����ListView
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-12 ����7:46:36   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-1-12 ����7:46:36   
	* �޸ı�ע��   
	* @version    
	*
	 */
	private class InitNoteListContent extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			NoteService noteService = NoteService.getInstance(getActivity());
			noteList = new ArrayList<Note>();

			// ��ʱ���Ⱥ󷵻�List
			noteList = noteService.loadAllNotesByTime();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			NoteListAdapter mAdapter = new NoteListAdapter();
			myNoteListView.setAdapter(mAdapter);
		}

	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�NoteListAdapter   
	* ��������   ListView ��Adapter
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-12 ����7:48:22   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-1-12 ����7:48:22   
	* �޸ı�ע��   
	* @version    
	*
	 */
	private class NoteListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return noteList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.note_list_item, null);
			}

			TextView noteTime = (TextView) convertView.findViewById(R.id.note_time);
			TextView noteContent = (TextView) convertView.findViewById(R.id.note_content);

			noteContent.setText(noteList.get(position).getNote_content());

			// ��ȡʱ���
			Map<String, Integer> interval = DateTimeTools.compareTo(DateTimeTools.getCurrentDate(),
					noteList.get(position).getNote_time());

			// ����ʱ���Ⱥ��������Ի�ʱ������
			if (interval.get("year") >= 1) {
				noteTime.setText(interval.get("year") + "��ǰ");
			} else if (interval.get("month") >= 1) {
				noteTime.setText(interval.get("month") + "��ǰ");
			} else if (interval.get("day") >= 1) {
				noteTime.setText(interval.get("day") + "��ǰ");
			} else if (interval.get("hour") >= 1) {
				noteTime.setText(interval.get("hour") + "Сʱǰ");
			} else if (interval.get("minute") >= 3) {
				noteTime.setText(interval.get("minute") + "����ǰ");
			} else {
				noteTime.setText("�ո�");
			}

			return convertView;
		}

	}

}
