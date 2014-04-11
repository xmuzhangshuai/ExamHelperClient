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
* 项目名称：ExamHelper   
* 类名称：AdCommentActivity   
* 类描述：  利用友盟应用联盟， 推荐其他软件，下载可获利。
* 创建人：张帅  
* 创建时间：2014-3-31 下午9:13:22   
* 修改人：张帅   
* 修改时间：2014-3-31 下午9:13:22   
* 修改备注：   
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
		getActionBar().setTitle("精彩推荐");

		ExchangeViewManager exchangeViewManager = new ExchangeViewManager(this, new ExchangeDataService());
		exchangeViewManager.addView(fatherLayout, listView);
	}

}
