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
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�CenterFragment   
 * �������� ��ҳ���м��Fragment��Ϊgridview��14������ģ��������ݲ���ע���¼�
 * �����ˣ���˧   
 * ����ʱ�䣺2013-12-3 ����1:21:03   
 * �޸��ˣ���˧   
 * �޸�ʱ�䣺2013-12-15 ����3:24:37   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class CenterFragment extends Fragment {
	private GridView gridView;// ��ҳ���м�fragment��GridView
	CenterFragmentAdapter centerFragmentAdapter;// ����һ��������
	SharedPreferences sharedPreferences;// ���ڻ���û����õ��ֻ�����

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.main_center_layout, container, false);// ����centerFragment��layout

		/********* ͨ��adapter��GridView������� **************/
		gridView = (GridView) view.findViewById(R.id.center_gridviewID);
		gridView.setColumnWidth((Constants.SCREEN_WIDTH - 100) / 2);
		centerFragmentAdapter = new CenterFragmentAdapter(getActivity());
		gridView.setAdapter(centerFragmentAdapter);
		/********* ͨ��adapter��GridView������� **************/

		// GridView���õ�����Ӧ�¼�
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = null;
				switch (position) {
				case 0:// �������˷�����ϰ
					intent = new Intent(getActivity(), SortPracticeActivity.class);
					break;
				case 1:// �������������ϰ
					intent = new Intent(getActivity(), RandomExerciseActivity.class);
					break;
				case 2:// ��������ģ�⿼��
					intent = new Intent(getActivity(), MockExamGuideActivity.class);
					break;
				case 3:// �������˴����ط�
					intent = new Intent(getActivity(), MyErrorActivity.class);
					break;
				case 4:// ���������ҵ��ղ�
					intent = new Intent(getActivity(), MyCollectionActivity.class);
					break;
				case 5:// ���������ҵıʼ�
					intent = new Intent(getActivity(), MyNoteActivity.class);
					break;
				case 6:// ��������ѧϰ��¼
					intent = new Intent(getActivity(), StudyRecordActivity.class);
					break;
				case 7:// ����������������
					intent = new Intent(getActivity(), DefaultActivity.class);
					break;
				case 8:// �������˿���ָ��
					intent = new Intent(getActivity(), ExamGuideActivity.class);
					break;
				case 9:// ���������������
					intent = new Intent(getActivity(), DefaultActivity.class);
					break;
				case 10:// ��������ͳ�Ʒ���
					intent = new Intent(getActivity(), StatisticActivity.class);
					break;
				case 11:// ���������ҵĴ���
					intent = new Intent(getActivity(), DefaultActivity.class);
					break;
				case 12:// �������˴��ɹ㳡
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
