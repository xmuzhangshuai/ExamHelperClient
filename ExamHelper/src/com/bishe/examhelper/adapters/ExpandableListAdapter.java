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
 * 项目名称：ExamHelper   
 * 类名称：ExpandableListAdapter   
 * 类描述：   可扩展列表的Adapter，给ExpandableListView绑定数据
 * 创建人：张帅   
 * 创建时间：2013-12-14 上午1:03:36   
 * 修改人：张帅   
 * 修改时间：2013-12-15 下午4:01:06   
 * 修改备注：   
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
		// 取得layoutInflater
		inflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// 获取child的数据
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

	// 每个子选项的外观
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.expandable_list_item, null);
		}
		TextView textView1 = (TextView) convertView.findViewById(R.id.expandable_list_item_text1);
		TextView textView2 = (TextView) convertView.findViewById(R.id.expandable_list_item_text2);
		textView1.setText(groups[groupPosition][childPosition].getGroup_name());// 设置组名

		GroupsService groupsService = GroupsService.getInstance(context);
		groupsService.refresh(groups[groupPosition][childPosition]);
		int question_num = groups[groupPosition][childPosition].getQuestion_num();// 题目数量
		float finished_num = groups[groupPosition][childPosition].getFinished_num();// 已完成题目数量
		float error_num = groups[groupPosition][childPosition].getError_num();// 错误题目数量
		float right_rate = (finished_num - error_num) / finished_num;// 正确率

		textView2.setText("题目数量：" + question_num + "，已完成：" + (int) finished_num + "，正确率：" + (int) (right_rate * 100)
				+ "%");// 设置子标题
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return groups[groupPosition].length;
	}

	// 指定组位置的组数据
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

	// 决定每个组选项的外观
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
		// 章节下所有题目数量
		int count = sectionService.getQuestionNum(sections[groupPosition]);

		// 已完成题目数量
		int finishedCount = sectionService.getFinishedNum(sections[groupPosition]);
		float rate = ((float) finishedCount / (float) count) * 100;
		textView2.setText("完成度：" + (int) rate + "%");
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	// 实现ChildView点击事件，必须返回true
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
