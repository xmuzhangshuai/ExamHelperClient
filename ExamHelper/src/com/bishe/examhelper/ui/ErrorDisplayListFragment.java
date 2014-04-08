package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.QuestionDisplayAdapter;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.entities.ErrorQuestions;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.QuestionType;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.service.ErrorQuestionsService;
import com.bishe.examhelper.service.MaterialAnalysisService;
import com.bishe.examhelper.service.MultiChoiceService;
import com.bishe.examhelper.service.QuestionTypeService;
import com.bishe.examhelper.service.SingleChoiceService;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：ErrorDisplayListFragment   
* 类描述：   按章节展示出错题目的预览列表，点击每个列表可显示题目详情，长按记录可选择删除、显示详情等操作。ActionBar添加全部删除按钮
*          可删除全部记录
* 创建人：张帅  
* 创建时间：2014-1-8 下午3:21:33   
* 修改人：张帅   
* 修改时间：2014-1-8 下午3:21:33   
* 修改备注：   
* @version    
*    
*/
public class ErrorDisplayListFragment extends BaseV4Fragment {
	ListView mListView;
	List<Object> questionList;// 题目列表
	List<ErrorQuestions> errorQuestionList;// 出错题目列表
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	QuestionDisplayAdapter questionDisplayAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mListView = (ListView) inflater.inflate(R.layout.fragment_display_list, container, false);
		questionList = new ArrayList<Object>();
		initView();

		/************从Intent获取错题列表***************/
		errorQuestionList = (List<ErrorQuestions>) getActivity().getIntent().getSerializableExtra(
				DefaultKeys.ERROR_LIST);
		if (errorQuestionList != null) {
			/*********初始化数据***********/
			new InitData().execute();
		}
		return mListView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.display_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// 如果点击了删除全部按钮
		if (item.getItemId() == R.id.delete_all) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("删除错题记录").setMessage("是否要删除此章节下全部错题记录？").setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					// 更新ListView
					questionDisplayAdapter.deleteAllItem();
					// 从DB删除
					deleteAllItemFromDB();
				}
			}).setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			}).show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActivity().getActionBar().setTitle("错题列表");
		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		/*********给ListView的每一条记录绑定事件，长按后进入弹出选择菜单************/
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checked_item);
				checkBox.setEnabled(true);
				checkBox.setChecked(true);
				showOptionDialog(position, checkBox);
				return true;
			}
		});

		/*********给ListView的每一条记录绑定事件，点击后进入题目答题页面************/
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				displayDetail(position);
			}
		});
	}

	/**
	 * 显示操作菜单
	 */
	public void showOptionDialog(final int position, final CheckBox checkBox) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("菜单").setItems(R.array.menu_options_array, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					// 如果点击了显示详情
					displayDetail(position);
					checkBox.setEnabled(false);
					checkBox.setChecked(false);
					break;
				case 1:
					// 如果点击了移除错题
					questionDisplayAdapter.deleteItem(questionList.get(position));
					deleteItemFromDB(position);
					checkBox.setEnabled(false);
					checkBox.setChecked(false);
					break;
				case 2:
					checkBox.setEnabled(false);
					checkBox.setChecked(false);
					break;
				default:
					break;
				}
			}
		}).setCancelable(false).show();
	}

	/**
	 * 从数据库永久删除某条错题记录
	 */
	public void deleteItemFromDB(final int id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ErrorQuestionsService errorQuestionsService = ErrorQuestionsService.getInstance(getActivity());
				errorQuestionsService.deleteErrorQuestions(errorQuestionList.get(id));
			}
		}).start();
	}

	/**
	 * 从数据库永久删除列表下所有记录
	 */
	public void deleteAllItemFromDB() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ErrorQuestionsService errorQuestionsService = ErrorQuestionsService.getInstance(getActivity());
				for (ErrorQuestions errorQuestions : errorQuestionList) {
					errorQuestionsService.deleteErrorQuestions(errorQuestions);
				}
			}
		}).start();
	}

	/**
	 * 显示某个题目详情页面
	 * @param position
	 */
	public void displayDetail(int position) {
		Object object = questionList.get(position);

		/**********如果是单选题**********/
		if (object.getClass().getSimpleName().equals(SingleChoice.class.getSimpleName())) {
			SingleChoice singleChoice = (SingleChoice) object;
			// 新建单选题页面
			SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
			Bundle localBundle = new Bundle();

			// 传入一个singleChoice对象
			localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE, singleChoice);
			localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
			mySingleChoiceFragment.setArguments(localBundle);

			// 替换当前页面
			fragmentTransaction.replace(R.id.display_fragment_container, mySingleChoiceFragment);
		}

		/**********如果是多选题**********/
		if (object.getClass().getSimpleName().equals(MultiChoice.class.getSimpleName())) {
			MultiChoice multiChoice = (MultiChoice) object;

			// 新建多选题页面
			MultiChoiceFragment multiChoiceFragment = new MultiChoiceFragment();
			Bundle localBundle = new Bundle();

			// 传入一个multiChoice对象
			localBundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE, multiChoice);
			localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
			multiChoiceFragment.setArguments(localBundle);

			// 替换当前页面
			fragmentTransaction.replace(R.id.display_fragment_container, multiChoiceFragment);
		}

		/**********如果是材料题**********/
		if (object.getClass().getSimpleName().equals(MaterialAnalysis.class.getSimpleName())) {
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) object;

			// 新建材料题页面
			MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
			Bundle localBundle = new Bundle();

			// 传入一个materialAnalysis对象
			localBundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS, materialAnalysis);
			localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
			materialAnalysisFragment.setArguments(localBundle);

			// 替换当前页面
			fragmentTransaction.replace(R.id.display_fragment_container, materialAnalysisFragment);
		}

		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
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
		List<String> errorTimes;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mSingleChoiceService = SingleChoiceService.getInstance(getActivity());
			multiChoiceService = MultiChoiceService.getInstance(getActivity());
			materialAnalysisService = MaterialAnalysisService.getInstance(getActivity());
			mQuestionTypeService = QuestionTypeService.getInstance(getActivity());
			errorTimes = new ArrayList<String>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			for (ErrorQuestions errorQuestions : errorQuestionList) {
				// 取出出错次数0
				errorTimes.add("出错次数： " + errorQuestions.getError_num());

				QuestionType questionType = mQuestionTypeService.loadQuestionType(errorQuestions.getQuestionType_id());
				/*******如果是单选题********/
				if (questionType.getType_name().equals(DefaultValues.SINGLE_CHOICE)) {
					SingleChoice mSingleChoice;
					mSingleChoice = mSingleChoiceService.loadSingleChoice(errorQuestions.getQuestion_id());
					if (mSingleChoice != null) {
						questionList.add(mSingleChoice);
					}
				}

				/*******如果是多选题********/
				if (questionType.getType_name().equals(DefaultValues.MULTI_CHOICE)) {
					MultiChoice multiChoice;
					multiChoice = multiChoiceService.loadMultiChoice(errorQuestions.getQuestion_id());
					if (multiChoice != null) {
						questionList.add(multiChoice);
					}
				}

				/*******如果是材料题********/
				if (questionType.getType_name().equals(DefaultValues.MATERIAL_ANALYSIS)) {
					MaterialAnalysis materialAnalysis;
					materialAnalysis = materialAnalysisService.loadMaterialAnalysis(errorQuestions.getQuestion_id());
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
			questionDisplayAdapter = new QuestionDisplayAdapter(getActivity(), questionList, errorTimes, true);
			mListView.setAdapter(questionDisplayAdapter);
		}

	}

}
