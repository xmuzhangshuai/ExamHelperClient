package com.bishe.examhelper.adapters;

import java.util.List;

import com.bishe.examhelper.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：CommonListAdapter   
* 类描述：   通用ListView的Adapter
* 创建人：张帅  
* 创建时间：2014-1-6 下午6:54:58   
* 修改人：张帅   
* 修改时间：2014-1-6 下午6:54:58   
* 修改备注：   
* @version    
*    
*/
public class CommonListAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<String> titleList;
	private List<String> subtitleList;

	public CommonListAdapter(Context context, List<String> titleList, List<String> subtitleList) {
		// TODO Auto-generated constructor stub
		this.mLayoutInflater = LayoutInflater.from(context);
		this.titleList = titleList;
		this.subtitleList = subtitleList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titleList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
			convertView = mLayoutInflater.inflate(R.layout.common_list_item, null);
		}

		TextView title = (TextView) convertView.findViewById(R.id.list_item_title);
		TextView subtitle = (TextView) convertView.findViewById(R.id.list_item_subtext);
		title.setText(titleList.get(position));
		subtitle.setText(subtitleList.get(position));
		return convertView;
	}

}
