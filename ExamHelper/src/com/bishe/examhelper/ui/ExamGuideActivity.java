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
* 项目名称：ExamHelper   
* 类名称：ExamGuideActivity   
* 类描述：   考试指南功能模块，
* 创建人：张帅  
* 创建时间：2014-3-25 下午8:48:05   
* 修改人：张帅   
* 修改时间：2014-3-25 下午8:48:05   
* 修改备注：   
* @version    
*    
*/
public class ExamGuideActivity extends BaseFragmentActivity {

	// 提醒用户网络状况有异常
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
		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 卸载广播
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
		getActionBar().setTitle("考试指南");
		getActionBar().setIcon(R.drawable.function_exam_guide);

	}

	/**
	 * 
	*    
	* 项目名称：ExamHelper   
	* 类名称：ExamGuideFragment   
	* 类描述：   展示考试指南下的目录，通过异步任务从网络获取数据
	*          点击每条记录时，启动新的ExamGuideListFragment，显示文章列表。
	*          同时传递参数ID。
	* 创建人：张帅  
	* 创建时间：2014-3-25 下午9:00:20   
	* 修改人：张帅   
	* 修改时间：2014-3-25 下午9:00:20   
	* 修改备注：   
	* @version    
	*
	 */
	@SuppressLint("ValidFragment")
	public class ExamGuideFragment extends ListFragment {
		private View rootView;
		List<JExamGuideType> examGuideTypes;// 考试指南目录
		private List<String> dataList;// 考试指南目录题目

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			// 执行异步任务，从网络获取数据
			new NetData().execute();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			rootView = inflater.inflate(R.layout.fragment_mock_exam_guide, container, false);

			return rootView;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			ExamGuideListFragment examGuideListFragment = new ExamGuideListFragment();

			// 将点击的条目传给ExamGuideListFragment
			Bundle bundle = new Bundle();
			bundle.putInt(DefaultKeys.BUNDLE_EXAMGUIDE_TYPE, examGuideTypes.get(position).getId());
			examGuideListFragment.setArguments(bundle);

			// 设置切换效果
			fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in,
					R.anim.push_right_out);
			fragmentTransaction.replace(R.id.exam_guide_container, examGuideListFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}

		/**
		 * 
		* 项目名称：ExamHelper   
		* 类名称：NetData   
		* 类描述：   从网络取数据
		* 创建人：张帅  
		*
		 */
		private class NetData extends AsyncTask<Void, Void, List<JExamGuideType>> {
			Dialog dialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				dialog = showProgressDialog();
			}

			@Override
			protected List<JExamGuideType> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				examGuideTypes = new ArrayList<JExamGuideType>();
				// 如果网络可用
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
				 * 如果网络返回结果不为空，则显示成列表
				 */
				if (result != null) {
					dataList = new ArrayList<String>();
					for (JExamGuideType examGuideType : result) {
						dataList.add(examGuideType.getTypeName());
					}
					setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.exam_guide_list_item,
							R.id.list_item_title, dataList));
				}
				dialog.cancel();
			}
		}
	}

	/**
	 * 
	*    
	* 项目名称：ExamHelper   
	* 类名称：ExamGuideDetailFragment   
	* 类描述：   
	* 创建人：张帅  
	* 创建时间：2014-3-25 下午10:37:58   
	* 修改人：张帅   
	* 修改时间：2014-3-25 下午10:37:58   
	* 修改备注：   
	* @version    
	*
	 */
	@SuppressLint("ValidFragment")
	public class ExamGuideListFragment extends ListFragment {
		private int examGuideTypeId;// 考试指南目录ID
		private View rootView;
		private List<JExamGuide> jExamGuideList;// 考试指南子目录列表
		private List<String> titleList;// 题目列表
		NetData netData = new NetData();// 异步任务

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
		 * 点击列表时事件响应
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
		* 项目名称：ExamHelper   
		* 类名称：NetData   
		* 类描述：   从网络取单击的某条考试指南的具体记录
		* 创建人：张帅  
		*
		 */
		private class NetData extends AsyncTask<Void, Void, List<JExamGuide>> {

			@Override
			protected List<JExamGuide> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				jExamGuideList = new ArrayList<JExamGuide>();
				// 如果网络可用
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
				 * 如果网络返回结果不为空，则显示成列表
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
