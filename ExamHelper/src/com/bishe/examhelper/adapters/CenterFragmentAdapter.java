package com.bishe.examhelper.adapters;

import com.bishe.examhelper.R;
import com.bishe.examhelper.config.Constants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 项目名称：ExamHelper 
 * 类名称：CenterFragmentAdapter 
 * 类描述：主页面CenterFragment的gridView的Adapter，用户返回主页面每个功能块的view 
 * 创建人：张帅
 * 创建时间：2013-12-5 下午23:46:11 
 * 修改人：张帅 
 * 修改时间：2013-12-15 下午3:59:23 
 * 修改备注：
 * 
 * @version
 * 
 */
public class CenterFragmentAdapter extends BaseAdapter {

	Context context;
	public String[] blocksText;// 主页面功能名称
	private LayoutInflater mInflater;
	private int mScreenWidth;// 屏幕宽度

	/******** 主页面功能块背景颜色***********/
	private int[] blocksColor = new int[] { R.color.lightview1, R.color.lightview2, R.color.lightview3,
			R.color.lightview4, R.color.lightview5, R.color.lightview6, R.color.lightview7, R.color.lightview8,
			R.color.lightview9, R.color.lightview10, R.color.lightview11, R.color.lightview12, R.color.lightview13,
			R.color.lightview14 };
	/******** 主页面功能块图片*********/
	private int[] blocksImage = new int[] { R.drawable.function_continue, R.drawable.function_per_practice,
			R.drawable.function_random_practice, R.drawable.function_simulate_examroom,
			R.drawable.function_error_again, R.drawable.function_mycollect, R.drawable.function_mynote,
			R.drawable.function_study_record, R.drawable.function_hot_exams, R.drawable.function_exam_guide,
			R.drawable.function_test_lib_search, R.drawable.function_statistic_analysis,
			R.drawable.function_myquestion, R.drawable.function_question_square, };

	public CenterFragmentAdapter(Context context) {
		this.context = context;
		blocksText = context.getResources().getStringArray(R.array.function_names);// 获取名称数组资源
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		/********* 获取屏幕分辨率宽度 **************/
		mScreenWidth = Constants.SCREEN_WIDTH;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return blocksText.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return blocksText[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		if (convertView == null) {
			view = (FrameLayout) mInflater.inflate(R.layout.block, null, false);
		} else {
			view = (FrameLayout) convertView;
		}
		ImageView imageView = (ImageView) view.findViewById(R.id.block_imageView);
		TextView textView = (TextView) view.findViewById(R.id.block_textView);
		textView.setText(blocksText[position]);// 设置名称
		view.setBackgroundResource(blocksColor[position]);// 设置背景颜色
		imageView.setImageResource(blocksImage[position]);// 设置背景图片
		view.setMinimumWidth((mScreenWidth - 100) / 2);// 设置功能块最小宽度
		view.setMinimumHeight((mScreenWidth - 100) / 2);// 设置功能块最小高度
		return view;
	}

}
