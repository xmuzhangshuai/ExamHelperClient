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
* 项目名称：ExamHelper   
* 类名称：MockExamPreviewActivity   
* 类描述：   模拟考试预览界面，提供一个GridView，展示题目的答题情况，点击快速定位到这个题目
* 创建人：张帅  
* 创建时间：2014-1-11 下午5:17:46   
* 修改人：张帅   
* 修改时间：2014-1-11 下午5:17:46   
* 修改备注：   
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
		getActionBar().setTitle("答题预览");
		getActionBar().setIcon(R.drawable.function_simulate_examroom);

		MyGridViewAdapter mAdapter = new MyGridViewAdapter();
		gridView.setColumnWidth((DensityUtil.getScreenWidthforPX(MockExamPreviewActivity.this) - 100) / 5);
		gridView.setAdapter(mAdapter);

		// 绑定事件
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				/********点击某个题目后传回给MockExamActivity，定位到这个题目**********/
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
	* 项目名称：ExamHelper   
	* 类名称：MyGridViewAdapter   
	* 类描述：   每个Item为一个显示了题号和已做、未做状态的TextView
	* 创建人：张帅  
	* 创建时间：2014-1-11 上午11:20:58   
	* 修改人：张帅   
	* 修改时间：2014-1-11 上午11:20:58   
	* 修改备注：   
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
			// 如果已做,更换背景
			if (previewArray[position] != null) {
				convertView.setBackgroundResource(R.drawable.selector_preview_gridview_item_finished);
				answer.setVisibility(View.VISIBLE);
				if (previewArray[position].length() < 5) {
					answer.setText(previewArray[position]);
				} else {
					answer.setText("材料题");
				}

			}

			convertView.setMinimumWidth((DensityUtil.getScreenWidthforPX(MockExamPreviewActivity.this) - 100) / 5);// 设置功能块最小宽度
			convertView.setMinimumHeight((DensityUtil.getScreenWidthforPX(MockExamPreviewActivity.this) - 100) / 5);// 设置功能块最小高度
			number.setText("" + (position + 1));
			return convertView;
		}

	}

}
