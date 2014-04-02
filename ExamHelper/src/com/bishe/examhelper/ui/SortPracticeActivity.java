package com.bishe.examhelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;

/**   
 *    
 * 项目名称：ExamHelper   
 * 类名称：SortPracticeActivity   
 * 类描述：   分类练习的activity，显示一个ListView，包含从数据库中取出的列表
 * 创建人：张帅     
 * 创建时间：2013-12-13 上午10:02:35   
 * 修改人：张帅     
 * 修改时间：2013-12-15 下午3:49:55   
 * 修改备注：   
 * @version    
 *    
 */
public class SortPracticeActivity extends BaseActivity {

	ListView mylistView;
	ImageButton gobackButton;// 标题栏返回键
	TextView titleNameTextView;// 标题栏名字
	String data[] = DefaultValues.SORTS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_list);
		findViewById();

		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_item_title, data);
		mylistView.setAdapter(mAdapter);

		initView();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mylistView = (ListView) findViewById(R.id.sort_practice_list);
		gobackButton = (ImageButton) findViewById(R.id.titleBtnLeft);
		titleNameTextView = (TextView) findViewById(R.id.titleName);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		titleNameTextView.setText(DefaultValues.SORT_TITILE_NAME);
		// 给列表每一项监听事件
		mylistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent;
				switch (position) {
				case 0:// 如果点击了习题集
					intent = new Intent(SortPracticeActivity.this, ExpandableListActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);// 设置切换效果
					break;
				case 1:// 如果点击了历年真题
					intent = new Intent(SortPracticeActivity.this, ListActivity.class);
					intent.putExtra(DefaultKeys.BUNDLE_LIST_TYPE, "历年真题");
					intent.putExtra(DefaultKeys.BUNDLE_TITLE_NAME, "历年真题");
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);// 设置切换效果
					break;
				case 2:// 如果点击了考前冲刺

					break;
				default:
					break;
				}
			}
		});

		// 返回按钮事件监听
		gobackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
