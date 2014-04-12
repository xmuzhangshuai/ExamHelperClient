package com.bishe.examhelper.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
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

		ExchangeViewManager exchangeViewManager = new ExchangeViewManager(this, new ExchangeDataService());
		exchangeViewManager.addView(fatherLayout, listView);
	}

}
