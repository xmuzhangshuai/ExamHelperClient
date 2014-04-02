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
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�SortPracticeActivity   
 * ��������   ������ϰ��activity����ʾһ��ListView�����������ݿ���ȡ�����б�
 * �����ˣ���˧     
 * ����ʱ�䣺2013-12-13 ����10:02:35   
 * �޸��ˣ���˧     
 * �޸�ʱ�䣺2013-12-15 ����3:49:55   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class SortPracticeActivity extends BaseActivity {

	ListView mylistView;
	ImageButton gobackButton;// ���������ؼ�
	TextView titleNameTextView;// ����������
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
		// ���б�ÿһ������¼�
		mylistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent;
				switch (position) {
				case 0:// ��������ϰ�⼯
					intent = new Intent(SortPracticeActivity.this, ExpandableListActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);// �����л�Ч��
					break;
				case 1:// ����������������
					intent = new Intent(SortPracticeActivity.this, ListActivity.class);
					intent.putExtra(DefaultKeys.BUNDLE_LIST_TYPE, "��������");
					intent.putExtra(DefaultKeys.BUNDLE_TITLE_NAME, "��������");
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);// �����л�Ч��
					break;
				case 2:// �������˿�ǰ���

					break;
				default:
					break;
				}
			}
		});

		// ���ذ�ť�¼�����
		gobackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
