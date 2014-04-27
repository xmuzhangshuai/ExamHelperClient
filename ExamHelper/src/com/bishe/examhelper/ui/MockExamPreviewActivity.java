package com.bishe.examhelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.utils.DensityUtil;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�MockExamPreviewActivity   
* ��������   ģ�⿼��Ԥ�����棬�ṩһ��GridView��չʾ��Ŀ�Ĵ��������������ٶ�λ�������Ŀ
* �����ˣ���˧  
* ����ʱ�䣺2014-1-11 ����5:17:46   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-11 ����5:17:46   
* �޸ı�ע��   
* @version    
*    
*/
public class MockExamPreviewActivity extends BaseActivity {
	private GridView gridView;
	private String[] previewArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mock_exam_preview);

		previewArray = getIntent().getStringArrayExtra(DefaultKeys.KEY_MOCK_EXAM_PREVIEW_LIST);

		findViewById();
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
		gridView = (GridView) findViewById(R.id.preview_gridview);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("����Ԥ��");
		getActionBar().setIcon(R.drawable.function_simulate_examroom);

		MyGridViewAdapter mAdapter = new MyGridViewAdapter();
		gridView.setColumnWidth((DensityUtil.getScreenWidthforPX(MockExamPreviewActivity.this) - 100) / 5);
		gridView.setAdapter(mAdapter);

		// ���¼�
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				/********���ĳ����Ŀ�󴫻ظ�MockExamActivity����λ�������Ŀ**********/
				Intent intent = new Intent();
				intent.putExtra(DefaultKeys.KEY_MOCK_EXAM_PREVIEW_BACK, position);
				setResult(2, intent);
				finish();
			}
		});
	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�MyGridViewAdapter   
	* ��������   ÿ��ItemΪһ����ʾ����ź�������δ��״̬��TextView
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-11 ����11:20:58   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-1-11 ����11:20:58   
	* �޸ı�ע��   
	* @version    
	*
	 */
	public class MyGridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return previewArray.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.preview_gridview_item, null, false);
			}
			TextView number = (TextView) convertView.findViewById(R.id.question_number);
			TextView answer = (TextView) convertView.findViewById(R.id.question_answer);
			// �������,��������
			if (previewArray[position] != null) {
				convertView.setBackgroundResource(R.drawable.selector_preview_gridview_item_finished);
				answer.setVisibility(View.VISIBLE);
				if (previewArray[position].length() < 5) {
					answer.setText(previewArray[position]);
				} else {
					answer.setText("������");
				}

			}

			convertView.setMinimumWidth((DensityUtil.getScreenWidthforPX(MockExamPreviewActivity.this) - 100) / 5);// ���ù��ܿ���С���
			convertView.setMinimumHeight((DensityUtil.getScreenWidthforPX(MockExamPreviewActivity.this) - 100) / 5);// ���ù��ܿ���С�߶�
			number.setText("" + (position + 1));
			return convertView;
		}

	}

}
