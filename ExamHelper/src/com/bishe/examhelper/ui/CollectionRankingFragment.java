package com.bishe.examhelper.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
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
import com.bishe.examhelper.entities.Section;
import com.bishe.examhelper.service.QuestionService;
import com.bishe.examhelper.service.SectionService;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jsonobjects.JScollection;

public class CollectionRankingFragment extends BaseV4Fragment {
	private PullToRefreshListView queryListView;// queryListview
	private LinkedList<JScollection> netData;//�ղ��б�
	private int pageNow = 0;//����ҳ��
	private View rootView;
	private CollectionAdapter collectionAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		netData = new LinkedList<JScollection>();
		collectionAdapter = new CollectionAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity_query_square, container, false);

		findViewById();
		//������������
		queryListView.setMode(Mode.BOTH);
		initView();

		queryListView.setAdapter(collectionAdapter);

		new GetNetDataTask().execute(0);

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		queryListView = (PullToRefreshListView) rootView.findViewById(R.id.queryList);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		//������������ˢ���¼�
		queryListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				pageNow = 0;
				new GetNetDataTask().execute(pageNow);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				if (pageNow >= 0)
					++pageNow;
				new GetNetDataTask().execute(pageNow);
			}
		});

		queryListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), QuestionsDisplayActivity.class);
				intent.putExtra(DefaultKeys.BY_SECTION_FRAGMENT_TYPE, "displsyQuestion");// ��������
				intent.putExtra("questionId", netData.get(position - 1).getQuestionId());
				intent.putExtra("questionTypeId", netData.get(position - 1).getQuestiontypeId());
				intent.putExtra("questionNumber", position);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}

	/**
	 * 
	 * �����ƣ�GetNetDataTask
	 * ���������������ȡ�ղ�����
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014-4-27 ����9:49:19
	 *
	 */
	class GetNetDataTask extends AsyncTask<Integer, Void, List<JScollection>> {
		int page = 0;

		@Override
		protected List<JScollection> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			page = params[0];
			String url = "ExamRankingServlet";
			String type = "getScollection";
			Map<String, String> map = new HashMap<String, String>();
			map.put("pageNow", String.valueOf(page));
			map.put("type", type);
			String data;
			try {
				data = HttpUtil.postRequest(url, map);
				List<JScollection> temp = FastJsonTool.getObjectList(data, JScollection.class);
				return temp;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<JScollection> result) {
			// TODO Auto-generated method stub
			if (result != null) {
				//������״λ�ȡ����
				if (page == 0) {
					if (result.size() < 10) {
						pageNow = -1;
					}
					netData = new LinkedList<JScollection>();
					netData.addAll(result);
				}
				//����ǻ�ȡ����
				else if (page > 0) {
					if (result.size() < 10) {
						pageNow = -1;
					}
					netData.addAll(result);
				}
				collectionAdapter.notifyDataSetChanged();
			}

			queryListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * 
	 * �����ƣ�CollectionAdapter
	 * ���������ղ�����Adapter
	 * �����ˣ� ��˧
	 * ����ʱ�䣺2014-4-27 ����9:46:37
	 *
	 */
	class CollectionAdapter extends BaseAdapter {
		private class ViewHolder {
			public TextView questionStemTextView;
			public TextView collectNumTextView;
			public TextView numberTextView;
			public TextView rightAnswerTextView;
			public TextView sectionTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return netData.size();
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
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(getActivity()).inflate(R.layout.collection_rank_list_item, null);
				holder = new ViewHolder();
				holder.questionStemTextView = (TextView) view.findViewById(R.id.list_item_title);
				holder.collectNumTextView = (TextView) view.findViewById(R.id.num);
				holder.numberTextView = (TextView) view.findViewById(R.id.list_item_leftimg);
				holder.rightAnswerTextView = (TextView) view.findViewById(R.id.rightAnswer);
				holder.sectionTextView = (TextView) view.findViewById(R.id.section);
				view.setTag(holder); // ��View���һ����������� 
			} else {
				holder = (ViewHolder) view.getTag(); // ������ȡ����  
			}

			holder.numberTextView.setText((position + 1) + ".  ");
			holder.collectNumTextView.setText("�ղ��ȶ�Ϊ�� " + netData.get(position).getCollectionNum() + "  ��");
			QuestionService questionService = QuestionService.getInstance(getActivity());
			holder.questionStemTextView.setText(questionService.getQuestionStem(netData.get(position).getQuestionId()
					.longValue(), netData.get(position).getQuestiontypeId().longValue()));
			holder.rightAnswerTextView.setText("��ȷ�𰸣�  "
					+ questionService.getRightAnswer(netData.get(position).getQuestionId().longValue(),
							netData.get(position).getQuestiontypeId().longValue()));
			SectionService sectionService = SectionService.getInstance(getActivity());
			Section section = sectionService.loadCollection(netData.get(position).getSectionId().longValue());
			if (section != null) {
				holder.sectionTextView.setText("�����½ڣ�  " + section.getSection_name());
			}

			return view;
		}
	}

}
