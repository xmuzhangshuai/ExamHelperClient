package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.newxp.controller.ExchangeDataService;
import com.umeng.newxp.view.ExchangeViewManager;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�AdCommentActivity   
* ��������  ��������Ӧ�����ˣ� �Ƽ�������������ؿɻ�����
* �����ˣ���˧  
* ����ʱ�䣺2014-3-31 ����9:13:22   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-3-31 ����9:13:22   
* �޸ı�ע��   
* @version    
*    
*/
public class AdCommentActivity extends BaseActivity {
	private ViewGroup fatherLayout;
	private ListView listView;
	private TextView newTextView;

	// public static ExchangeDataService preloadDataService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_comment);
		findViewById();
		initView();

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		fatherLayout = (ViewGroup) this.findViewById(R.id.ad);
		listView = (ListView) this.findViewById(R.id.list);
		// newTextView = (TextView) this.findViewById(R.id.new_tips);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("�����Ƽ�");

		// ��ֵpreloadDataService,���newTips �ص�
		// preloadDataService = new ExchangeDataService();
		// preloadDataService.preloadData(this, new NTipsChangedListener() {
		// @Override
		// public void onChanged(int flag) {
		// if (flag == -1) {
		// // û��new���
		//
		// newTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		// } else if (flag > 1) {
		// // ��һҳnew�������
		// newTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
		// R.drawable.btn_my_comment_new2, 0);
		// } else if (flag == 0) {
		// // ��һҳȫ��Ϊnew ���
		// newTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
		// R.drawable.btn_my_comment_new2, 0);
		// }
		// };
		// }, ExchangeConstants.type_container);
		//
		// ExchangeDataService exchangeDataService = preloadDataService != null
		// ? preloadDataService
		// : new ExchangeDataService("");
		// ExchangeViewManager exchangeViewManager = new
		// ExchangeViewManager(this, exchangeDataService);
		ExchangeViewManager exchangeViewManager = new ExchangeViewManager(this, new ExchangeDataService());
		exchangeViewManager.addView(fatherLayout, listView);
	}

}
