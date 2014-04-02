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
 * ��Ŀ���ƣ�ExamHelper 
 * �����ƣ�CenterFragmentAdapter 
 * ����������ҳ��CenterFragment��gridView��Adapter���û�������ҳ��ÿ�����ܿ��view 
 * �����ˣ���˧
 * ����ʱ�䣺2013-12-5 ����23:46:11 
 * �޸��ˣ���˧ 
 * �޸�ʱ�䣺2013-12-15 ����3:59:23 
 * �޸ı�ע��
 * 
 * @version
 * 
 */
public class CenterFragmentAdapter extends BaseAdapter {

	Context context;
	public String[] blocksText;// ��ҳ�湦������
	private LayoutInflater mInflater;
	private int mScreenWidth;// ��Ļ���

	/******** ��ҳ�湦�ܿ鱳����ɫ***********/
	private int[] blocksColor = new int[] { R.color.lightview1, R.color.lightview2, R.color.lightview3,
			R.color.lightview4, R.color.lightview5, R.color.lightview6, R.color.lightview7, R.color.lightview8,
			R.color.lightview9, R.color.lightview10, R.color.lightview11, R.color.lightview12, R.color.lightview13,
			R.color.lightview14 };
	/******** ��ҳ�湦�ܿ�ͼƬ*********/
	private int[] blocksImage = new int[] { R.drawable.function_continue, R.drawable.function_per_practice,
			R.drawable.function_random_practice, R.drawable.function_simulate_examroom,
			R.drawable.function_error_again, R.drawable.function_mycollect, R.drawable.function_mynote,
			R.drawable.function_study_record, R.drawable.function_hot_exams, R.drawable.function_exam_guide,
			R.drawable.function_test_lib_search, R.drawable.function_statistic_analysis,
			R.drawable.function_myquestion, R.drawable.function_question_square, };

	public CenterFragmentAdapter(Context context) {
		this.context = context;
		blocksText = context.getResources().getStringArray(R.array.function_names);// ��ȡ����������Դ
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		/********* ��ȡ��Ļ�ֱ��ʿ�� **************/
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
		textView.setText(blocksText[position]);// ��������
		view.setBackgroundResource(blocksColor[position]);// ���ñ�����ɫ
		imageView.setImageResource(blocksImage[position]);// ���ñ���ͼƬ
		view.setMinimumWidth((mScreenWidth - 100) / 2);// ���ù��ܿ���С���
		view.setMinimumHeight((mScreenWidth - 100) / 2);// ���ù��ܿ���С�߶�
		return view;
	}

}
