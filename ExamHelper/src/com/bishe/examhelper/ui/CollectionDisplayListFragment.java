package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.List;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.QuestionDisplayAdapter;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.config.DefaultKeys;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�QuestionDisplayListFragment   
* ��������   ����չʾ��Ŀ�ĸ�Ҫ��ͼ�����ҵ��ղء������طŵȹ��ܻ����Ŀ�б���ʾ��Ŀ��ɼ��𰸵ĸ�Ҫ��ͼ��
* �����ˣ���˧  
* ����ʱ�䣺2014-1-7 ����1:10:35   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-7 ����1:10:35   
* �޸ı�ע��   
* @version    
*    
*/
public class CollectionDisplayListFragment extends BaseV4Fragment {
	ListView mListView;
	List<Object> questionList;// ��Ŀ�б�
	List<Collection> collectionList;// �ղ���Ŀ�б�
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

		/************��Intent��ȡ�ղ��б�***************/
		collectionList = (List<Collection>) getActivity().getIntent().getSerializableExtra(DefaultKeys.COLLECTION_LIST);
		if (collectionList != null) {
			/*********��ʼ������***********/
			new InitData().execute();
		}
		return mListView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

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
		// ��������ɾ��ȫ����ť
		if (item.getItemId() == R.id.delete_all) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("ɾ���ղؼ�¼").setMessage("�Ƿ�Ҫɾ�����½���ȫ���ղأ�").setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					// ����ListView
					questionDisplayAdapter.deleteAllItem();
					// ��DBɾ��
					deleteAllItemFromDB();
				}
			}).setNegativeButton("ȡ��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			}).show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActivity().getActionBar().setTitle("�ղ��б�");
		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		/*********��ListView��ÿһ����¼���¼�����������뵯��ѡ��˵�************/
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

		/*********��ListView��ÿһ����¼���¼�������������Ŀ����ҳ��************/
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				displayDetail(position);
			}
		});
	}

	/**
	 * ��ʾ�����˵�
	 */
	public void showOptionDialog(final int position, final CheckBox checkBox) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("�˵�").setItems(R.array.menu_options_array, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					// ����������ʾ����
					displayDetail(position);
					checkBox.setEnabled(false);
					checkBox.setChecked(false);
					break;
				case 1:
					// ���������Ƴ��ղ�
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
	 * �����ݿ�����ɾ��ĳ���ղؼ�¼
	 */
	public void deleteItemFromDB(final int id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				CollectionService collectionService = CollectionService.getInstance(getActivity());
				collectionService.deleteCollection(collectionList.get(id));
			}
		}).start();
	}

	/**
	 * �����ݿ�����ɾ���б������м�¼
	 */
	public void deleteAllItemFromDB() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final CollectionService collectionService = CollectionService.getInstance(getActivity());
				new Thread() {
					public void run() {
						collectionService.delCollectionFromNet(collectionList);
					};
				}.start();

				for (Collection collection : collectionList) {
					collectionService.deleteCollection(collection);
				}
			}
		}).start();
	}

	/**
	 * ��ʾĳ����Ŀ����ҳ��
	 * @param position
	 */
	public void displayDetail(int position) {
		Object object = questionList.get(position);

		/**********����ǵ�ѡ��**********/
		if (object.getClass().getSimpleName().equals(SingleChoice.class.getSimpleName())) {
			SingleChoice singleChoice = (SingleChoice) object;
			// �½���ѡ��ҳ��
			SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
			Bundle localBundle = new Bundle();

			// ����һ��singleChoice����
			localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE, singleChoice);
			localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
			mySingleChoiceFragment.setArguments(localBundle);

			// �滻��ǰҳ��
			fragmentTransaction.replace(R.id.display_fragment_container, mySingleChoiceFragment);
		}

		/**********����Ƕ�ѡ��**********/
		if (object.getClass().getSimpleName().equals(MultiChoice.class.getSimpleName())) {
			MultiChoice multiChoice = (MultiChoice) object;

			// �½���ѡ��ҳ��
			MultiChoiceFragment multiChoiceFragment = new MultiChoiceFragment();
			Bundle localBundle = new Bundle();

			// ����һ��multiChoice����
			localBundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE, multiChoice);
			localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
			multiChoiceFragment.setArguments(localBundle);

			// �滻��ǰҳ��
			fragmentTransaction.replace(R.id.display_fragment_container, multiChoiceFragment);
		}

		/**********����ǲ�����**********/
		if (object.getClass().getSimpleName().equals(MaterialAnalysis.class.getSimpleName())) {
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) object;

			// �½�������ҳ��
			MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
			Bundle localBundle = new Bundle();

			// ����һ��materialAnalysis����
			localBundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS, materialAnalysis);
			localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
			materialAnalysisFragment.setArguments(localBundle);

			// �滻��ǰҳ��
			fragmentTransaction.replace(R.id.display_fragment_container, materialAnalysisFragment);
		}

		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�InitData   
	* ��������  �첽�����ʼ������ 
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-7 ����10:11:45   
	*
	 */
	private class InitData extends AsyncTask<Void, Void, Void> {
		SingleChoiceService mSingleChoiceService;
		MultiChoiceService multiChoiceService;
		MaterialAnalysisService materialAnalysisService;
		QuestionTypeService mQuestionTypeService;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mSingleChoiceService = SingleChoiceService.getInstance(getActivity());
			multiChoiceService = MultiChoiceService.getInstance(getActivity());
			materialAnalysisService = MaterialAnalysisService.getInstance(getActivity());
			mQuestionTypeService = QuestionTypeService.getInstance(getActivity());
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			for (Collection collection : collectionList) {
				QuestionType questionType = mQuestionTypeService.loadQuestionType(collection.getQuestionType_id());
				/*******����ǵ�ѡ��********/
				if (questionType.getType_name().equals(DefaultValues.SINGLE_CHOICE)) {
					SingleChoice mSingleChoice;
					mSingleChoice = mSingleChoiceService.loadSingleChoice(collection.getQuestion_id());
					if (mSingleChoice != null) {
						questionList.add(mSingleChoice);
					}
				}

				/*******����Ƕ�ѡ��********/
				if (questionType.getType_name().equals(DefaultValues.MULTI_CHOICE)) {
					MultiChoice multiChoice;
					multiChoice = multiChoiceService.loadMultiChoice(collection.getQuestion_id());
					if (multiChoice != null) {
						questionList.add(multiChoice);
					}
				}

				/*******����ǲ�����********/
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
			questionDisplayAdapter = new QuestionDisplayAdapter(getActivity(), questionList);
			mListView.setAdapter(questionDisplayAdapter);
		}

	}

}
