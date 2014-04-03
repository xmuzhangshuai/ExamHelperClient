package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseFragmentActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.test.ExamGuide;
import com.bishe.examhelper.test.ExamGuideType;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.NetworkUtils;

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
		List<ExamGuideType> examGuideTypes;// ����ָ��Ŀ¼
		private List<String> dataList;// ����ָ��Ŀ¼��Ŀ
		NetData netData = new NetData();// �첽����

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			// ִ���첽���񣬴������ȡ����
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

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			ExamGuideListFragment examGuideListFragment = new ExamGuideListFragment();

			// ���������Ŀ����ExamGuideListFragment
			Bundle bundle = new Bundle();
			bundle.putLong(DefaultKeys.BUNDLE_EXAMGUIDE_TYPE, examGuideTypes.get(position).getId());
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
		private class NetData extends AsyncTask<Void, Void, List<ExamGuideType>> {
			Dialog dialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				dialog = showProgressDialog();
			}

			@Override
			protected List<ExamGuideType> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				examGuideTypes = new ArrayList<ExamGuideType>();
				// ����������
				if (NetworkUtils.isNetworkAvailable(getActivity())) {
					try {
						String jsonString = HttpUtil.getRequest("ExamGuideServlet");
						examGuideTypes = FastJsonTool.getObjectList(jsonString, ExamGuideType.class);
						return examGuideTypes;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<ExamGuideType> result) {
				// TODO Auto-generated method stub
				/**
				 * ������緵�ؽ����Ϊ�գ�����ʾ���б�
				 */
				if (result != null) {
					dataList = new ArrayList<String>();
					for (ExamGuideType examGuideType : result) {
						dataList.add(examGuideType.getType());
					}
					setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.exam_guide_list_item,
							R.id.list_item_title, dataList));

					dialog.cancel();
				} else {
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
		private Long examGuideTypeId;// ����ָ��Ŀ¼ID
		private View rootView;
		private List<ExamGuide> examGuideList;// ����ָ����Ŀ¼�б�
		private List<String> titleList;// ��Ŀ�б�
		NetData netData = new NetData();// �첽����

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			examGuideTypeId = getArguments().getLong(DefaultKeys.BUNDLE_EXAMGUIDE_TYPE, 1);
			new NetData().execute();
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
			Toast.makeText(getActivity(), examGuideList.get(position).getContent().substring(0, 20), 1).show();
		}

		/**
		 * 
		* ��Ŀ���ƣ�ExamHelper   
		* �����ƣ�NetData   
		* ��������   ������ȡ������ĳ������ָ�ϵľ����¼
		* �����ˣ���˧  
		*
		 */
		private class NetData extends AsyncTask<Void, Void, List<ExamGuide>> {

			@Override
			protected List<ExamGuide> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				examGuideList = new ArrayList<ExamGuide>();
				// ����������
				if (NetworkUtils.isNetworkAvailable(getActivity())) {
					try {
						Map<String, String> map = new HashMap<String, String>();
						map.put("ExamGuideTypeId", examGuideTypeId.toString());
						String jsonString = HttpUtil.postRequest("ExamGuideServlet", map);
						examGuideList = FastJsonTool.getObjectList(jsonString, ExamGuide.class);
						return examGuideList;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<ExamGuide> result) {
				// TODO Auto-generated method stub
				/**
				 * ������緵�ؽ����Ϊ�գ�����ʾ���б�
				 */
				if (result != null) {
					titleList = new ArrayList<String>();
					for (ExamGuide examGuide : result) {
						titleList.add(examGuide.getTitle());
					}
					setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.exam_guide_list_item,
							R.id.list_item_title, titleList));
				} else {
				}
			}
		}
	}
}
