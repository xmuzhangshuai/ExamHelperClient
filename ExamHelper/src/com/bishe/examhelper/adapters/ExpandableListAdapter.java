package com.bishe.examhelper.adapters;

import com.bishe.examhelper.R;
import com.bishe.examhelper.dbService.GroupsService;
import com.bishe.examhelper.dbService.SectionService;
import com.bishe.examhelper.entities.Groups;
import com.bishe.examhelper.entities.Section;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**   
 *    
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�ExpandableListAdapter   
 * ��������   ����չ�б��Adapter����ExpandableListView������
 * �����ˣ���˧   
 * ����ʱ�䣺2013-12-14 ����1:03:36   
 * �޸��ˣ���˧   
 * �޸�ʱ�䣺2013-12-15 ����4:01:06   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
	private Section[] sections;
	private Groups[][] groups;
	private Context context;
	private LayoutInflater inflater;

	public ExpandableListAdapter(Context contex, Section[] sections, Groups[][] groups) {
		this.context = contex;
		this.sections = sections;
		this.groups = groups;
		// ȡ��layoutInflater
		inflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// ��ȡchild������
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return groups[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	// ÿ����ѡ������
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.expandable_list_item, null);
		}
		TextView textView1 = (TextView) convertView.findViewById(R.id.expandable_list_item_text1);
		TextView textView2 = (TextView) convertView.findViewById(R.id.expandable_list_item_text2);
		textView1.setText(groups[groupPosition][childPosition].getGroup_name());// ��������

		GroupsService groupsService = GroupsService.getInstance(context);
		groupsService.refresh(groups[groupPosition][childPosition]);
		int question_num = groups[groupPosition][childPosition].getQuestion_num();// ��Ŀ����
		float finished_num = groups[groupPosition][childPosition].getFinished_num();// �������Ŀ����
		float error_num = groups[groupPosition][childPosition].getError_num();// ������Ŀ����
		float right_rate = (finished_num - error_num) / finished_num;// ��ȷ��

		textView2.setText("��Ŀ������" + question_num + "������ɣ�" + (int) finished_num + "����ȷ�ʣ�" + (int) (right_rate * 100)
				+ "%");// �����ӱ���
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return groups[groupPosition].length;
	}

	// ָ����λ�õ�������
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups[groupPosition];
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	// ����ÿ����ѡ������
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.expandable_list_group, null);
		}
		TextView textView1 = (TextView) convertView.findViewById(R.id.expandable_list_grouop_text1);
		TextView textView2 = (TextView) convertView.findViewById(R.id.expandable_list_grouop_text2);
		textView1.setText(sections[groupPosition].getSection_name());

		SectionService sectionService = SectionService.getInstance(context);
		// �½���������Ŀ����
		int count = sectionService.getQuestionNum(sections[groupPosition]);

		// �������Ŀ����
		int finishedCount = sectionService.getFinishedNum(sections[groupPosition]);
		float rate = ((float) finishedCount / (float) count) * 100;
		textView2.setText("��ɶȣ�" + (int) rate + "%");
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	// ʵ��ChildView����¼������뷵��true
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
