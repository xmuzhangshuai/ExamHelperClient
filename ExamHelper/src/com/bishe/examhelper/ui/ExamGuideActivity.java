package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.NetworkUtils;
import com.jsonobjects.JExamGuide;
import com.jsonobjects.JExamGuideType;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�ExamGuideActivity   
* ��������   ����ָ�Ϲ���ģ�飬
* �����ˣ���˧  
* ����ʱ�䣺2014-3-25 ����8:48:05   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-3-25 ����8:48:05   
* �޸ı�ע��   
* @version    
*    
*/
public class ExamGuideActivity extends BaseFragmentActivity {

	// �����û�����״�����쳣
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (!NetworkUtils.isNetworkAvailable(ExamGuideActivity.this)) {
				NetworkUtils.networkStateTips(ExamGuideActivity.this);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examguide);
		findViewById();
		initView();

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.exam_guide_container, new ExamGuideFragment());
		fragmentTransaction.commit();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ע��㲥
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// ж�ع㲥
		if (broadcastReceiver != null) {
			unregisterReceiver(broadcastReceiver);
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("����ָ��");
		getActionBar().setIcon(R.drawable.function_exam_guide);

	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�ExamGuideFragment   
	* ��������   չʾ����ָ���µ�Ŀ¼��ͨ���첽����������ȡ����
	*          ���ÿ����¼ʱ�������µ�ExamGuideListFragment����ʾ�����б�
	*          ͬʱ���ݲ���ID��
	* �����ˣ���˧  
	* ����ʱ�䣺2014-3-25 ����9:00:20   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-3-25 ����9:00:20   
	* �޸ı�ע��   
	* @version    
	*
	 */
	@SuppressLint("ValidFragment")
	public class ExamGuideFragment extends ListFragment {
		private View rootView;
		List<JExamGuideType> examGuideTypes;// ����ָ��Ŀ¼
		private List<String> dataList;// ����ָ��Ŀ¼��Ŀ

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			rootView = inflater.inflate(R.layout.fragment_mock_exam_guide, container, false);

			// ִ���첽���񣬴������ȡ����
			new NetData().execute();
			return rootView;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			ExamGuideListFragment examGuideListFragment = new ExamGuideListFragment();

			// ���������Ŀ����ExamGuideListFragment
			Bundle bundle = new Bundle();
			bundle.putInt(DefaultKeys.BUNDLE_EXAMGUIDE_TYPE, examGuideTypes.get(position).getId());
			examGuideListFragment.setArguments(bundle);

			// �����л�Ч��
			fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in,
					R.anim.push_right_out);
			fragmentTransaction.replace(R.id.exam_guide_container, examGuideListFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}

		/**
		 * 
		* ��Ŀ���ƣ�ExamHelper   
		* �����ƣ�NetData   
		* ��������   ������ȡ����
		* �����ˣ���˧  
		*
		 */
		private class NetData extends AsyncTask<Void, Void, List<JExamGuideType>> {

			@Override
			protected List<JExamGuideType> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				examGuideTypes = new ArrayList<JExamGuideType>();
				// ����������
				if (NetworkUtils.isNetworkAvailable(getActivity())) {
					try {
						String url = "ExamGuideServlet";
						Map<String, String> map = new HashMap<String, String>();
						map.put("type", "getExamGuideTypeList");
						map.put("subjectId", String.valueOf(DefaultValues.SUBJECT_ID));
						String jsonString = HttpUtil.postRequest(url, map);
						examGuideTypes = FastJsonTool.getObjectList(jsonString, JExamGuideType.class);
						return examGuideTypes;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					NetworkUtils.networkStateTips(ExamGuideActivity.this);
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<JExamGuideType> result) {
				// TODO Auto-generated method stub
				/**
				 * ������緵�ؽ����Ϊ�գ�����ʾ���б�
				 */
				if (result != null) {
					dataList = new ArrayList<String>();
					for (JExamGuideType examGuideType : result) {
						dataList.add(examGuideType.getTypeName());
					}
					setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.exam_guide_list_item,
							R.id.list_item_title, dataList));
				}
			}
		}
	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�ExamGuideDetailFragment   
	* ��������   
	* �����ˣ���˧  
	* ����ʱ�䣺2014-3-25 ����10:37:58   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-3-25 ����10:37:58   
	* �޸ı�ע��   
	* @version    
	*
	 */
	@SuppressLint("ValidFragment")
	public class ExamGuideListFragment extends ListFragment {
		private int examGuideTypeId;// ����ָ��Ŀ¼ID
		private View rootView;
		private List<JExamGuide> jExamGuideList;// ����ָ����Ŀ¼�б�
		private List<String> titleList;// ��Ŀ�б�
		NetData netData = new NetData();// �첽����

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			examGuideTypeId = getArguments().getInt(DefaultKeys.BUNDLE_EXAMGUIDE_TYPE, 1);
			netData.execute();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			rootView = inflater.inflate(R.layout.fragment_mock_exam_guide, container, false);

			return rootView;
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			if (!netData.isCancelled()) {
				netData.cancel(true);
			}

		}

		/**
		 * ����б�ʱ�¼���Ӧ
		 */
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(ExamGuideActivity.this, ExamGuideWebActivity.class);
			intent.putExtra("examGuideUrl", jExamGuideList.get(position).getUrl());
			intent.putExtra("title", jExamGuideList.get(position).getTitle());
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}

		/**
		 * 
		* ��Ŀ���ƣ�ExamHelper   
		* �����ƣ�NetData   
		* ��������   ������ȡ������ĳ������ָ�ϵľ����¼
		* �����ˣ���˧  
		*
		 */
		private class NetData extends AsyncTask<Void, Void, List<JExamGuide>> {

			@Override
			protected List<JExamGuide> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				jExamGuideList = new ArrayList<JExamGuide>();
				// ����������
				if (NetworkUtils.isNetworkAvailable(getActivity())) {
					try {
						Map<String, String> map = new HashMap<String, String>();
						map.put("type", "getExamGuideList");
						map.put("examTypeId", String.valueOf(examGuideTypeId));
						String jsonString = HttpUtil.postRequest("ExamGuideServlet", map);
						jExamGuideList = FastJsonTool.getObjectList(jsonString, JExamGuide.class);
						return jExamGuideList;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<JExamGuide> result) {
				// TODO Auto-generated method stub
				/**
				 * ������緵�ؽ����Ϊ�գ�����ʾ���б�
				 */
				if (result != null) {
					titleList = new ArrayList<String>();
					for (JExamGuide jExamGuide : result) {
						titleList.add(jExamGuide.getTitle());
					}
					setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.exam_guide_list_item,
							R.id.list_item_title, titleList));
				} else {
				}
			}
		}
	}
}
