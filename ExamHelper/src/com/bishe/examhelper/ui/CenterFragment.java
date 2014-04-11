package com.bishe.examhelper.ui;

import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.CenterFragmentAdapter;
import com.bishe.examhelper.config.Constants;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

/**   
 *    
 * 项目名称：ExamHelper   
 * 类名称：CenterFragment   
 * 类描述： 主页面中间的Fragment，为gridview中14个功能模块添加内容并且注册事件
 * 创建人：张帅   
 * 创建时间：2013-12-3 下午1:21:03   
 * 修改人：张帅   
 * 修改时间：2013-12-15 下午3:24:37   
 * 修改备注：   
 * @version    
 *    
 */
public class CenterFragment extends Fragment {
	private GridView gridView;// 主页面中间fragment的GridView
	CenterFragmentAdapter centerFragmentAdapter;// 创建一个适配器
	SharedPreferences sharedPreferences;// 用于获得用户设置到手机本地

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.main_center_layout, container, false);// 加载centerFragment的layout

		/********* 通过adapter给GridView填充内容 **************/
		gridView = (GridView) view.findViewById(R.id.center_gridviewID);
		gridView.setColumnWidth((Constants.SCREEN_WIDTH - 100) / 2);
		centerFragmentAdapter = new CenterFragmentAdapter(getActivity());
		gridView.setAdapter(centerFragmentAdapter);
		/********* 通过adapter给GridView填充内容 **************/

		// GridView设置单击相应事件
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = null;
				switch (position) {
				case 0:// 如果点击了分类练习
					intent = new Intent(getActivity(), SortPracticeActivity.class);
					break;
				case 1:// 如果点击了随机练习
					intent = new Intent(getActivity(), RandomExerciseActivity.class);
					break;
				case 2:// 如果点击了模拟考场
					intent = new Intent(getActivity(), MockExamGuideActivity.class);
					break;
				case 3:// 如果点击了错题重放
					intent = new Intent(getActivity(), MyErrorActivity.class);
					break;
				case 4:// 如果点击了我的收藏
					intent = new Intent(getActivity(), MyCollectionActivity.class);
					break;
				case 5:// 如果点击了我的笔记
					intent = new Intent(getActivity(), MyNoteActivity.class);
					break;
				case 6:// 如果点击了学习记录
					intent = new Intent(getActivity(), StudyRecordActivity.class);
					break;
				case 7:// 如果点击了试题排行
					intent = new Intent(getActivity(), DefaultActivity.class);
					break;
				case 8:// 如果点击了考试指南
					intent = new Intent(getActivity(), ExamGuideActivity.class);
					break;
				case 9:// 如果点击了题库搜索
					intent = new Intent(getActivity(), DefaultActivity.class);
					break;
				case 10:// 如果点击了统计分析
					intent = new Intent(getActivity(), StatisticActivity.class);
					break;
				case 11:// 如果点击了我的答疑
					intent = new Intent(getActivity(), DefaultActivity.class);
					break;
				case 12:// 如果点击了答疑广场
					intent = new Intent(getActivity(), DefaultActivity.class);
					break;
				default:
					break;
				}
				if (intent != null) {
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			}
		});

		return view;
	}

}
