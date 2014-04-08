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

	/********变量*******/
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
		// 给记录绑定事件
		myNoteListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity()
						.getSupportFragmentManager().beginTransaction();
				QuestionTypeService questionTypeService = QuestionTypeService.getInstance(getActivity());

				/**********如果是单选题**********/
				if (noteList.get(position).getQuestionType_id() == questionTypeService
						.getIdByName(DefaultValues.SINGLE_CHOICE)) {
					SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(getActivity());

					SingleChoice singleChoice = singleChoiceService.loadSingleChoice(noteList.get(position)
							.getQuestion_id());

					// 新建单选题页面
					SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
					Bundle localBundle = new Bundle();

					// 传入一个singleChoice对象
					localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE, singleChoice);
					localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
					mySingleChoiceFragment.setArguments(localBundle);

					// 替换当前页面
					fragmentTransaction.replace(R.id.fragment_container, mySingleChoiceFragment);
				}

				/**********如果是多选题**********/
				if (noteList.get(position).getQuestionType_id() == questionTypeService
						.getIdByName(DefaultValues.MULTI_CHOICE)) {
					MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(getActivity());
					MultiChoice multiChoice = multiChoiceService.loadMultiChoice(noteList.get(position)
							.getQuestion_id());

					// 新建多选题页面
					MultiChoiceFragment multiChoiceFragment = new MultiChoiceFragment();
					Bundle localBundle = new Bundle();

					// 传入一个multiChoice对象
					localBundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE, multiChoice);
					localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
					multiChoiceFragment.setArguments(localBundle);

					// 替换当前页面
					fragmentTransaction.replace(R.id.fragment_container, multiChoiceFragment);
				}

				/**********如果是材料题**********/
				if (noteList.get(position).getQuestionType_id() == questionTypeService
						.getIdByName(DefaultValues.MATERIAL_ANALYSIS)) {
					MaterialAnalysisService materialAnalysisService = MaterialAnalysisService
							.getInstance(getActivity());
					MaterialAnalysis materialAnalysis = materialAnalysisService.loadMaterialAnalysis(noteList.get(
							position).getQuestion_id());

					// 新建材料题页面
					MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
					Bundle localBundle = new Bundle();

					// 传入一个materialAnalysis对象
					localBundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS, materialAnalysis);
					localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
					materialAnalysisFragment.setArguments(localBundle);

					// 替换当前页面
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
	* 项目名称：ExamHelper   
	* 类名称：InitNoteListContent   
	* 类描述：   从数据库取数据，填充ListView
	* 创建人：张帅  
	* 创建时间：2014-1-12 下午7:46:36   
	* 修改人：张帅   
	* 修改时间：2014-1-12 下午7:46:36   
	* 修改备注：   
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

			// 按时间先后返回List
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
	* 项目名称：ExamHelper   
	* 类名称：NoteListAdapter   
	* 类描述：   ListView 的Adapter
	* 创建人：张帅  
	* 创建时间：2014-1-12 下午7:48:22   
	* 修改人：张帅   
	* 修改时间：2014-1-12 下午7:48:22   
	* 修改备注：   
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

			// 获取时间差
			Map<String, Integer> interval = DateTimeTools.compareTo(DateTimeTools.getCurrentDate(),
					noteList.get(position).getNote_time());

			// 根据时间先后设置人性化时间提醒
			if (interval.get("year") >= 1) {
				noteTime.setText(interval.get("year") + "年前");
			} else if (interval.get("month") >= 1) {
				noteTime.setText(interval.get("month") + "月前");
			} else if (interval.get("day") >= 1) {
				noteTime.setText(interval.get("day") + "天前");
			} else if (interval.get("hour") >= 1) {
				noteTime.setText(interval.get("hour") + "小时前");
			} else if (interval.get("minute") >= 3) {
				noteTime.setText(interval.get("minute") + "分钟前");
			} else {
				noteTime.setText("刚刚");
			}

			return convertView;
		}

	}

}
