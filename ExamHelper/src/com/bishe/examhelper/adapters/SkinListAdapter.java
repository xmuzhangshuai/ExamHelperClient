package com.bishe.examhelper.adapters;

import java.util.List;
import com.bishe.examhelper.R;
import com.bishe.examhelper.ui.ThemeSitcherDialogFragment.SkinListItemData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**   
 *    
 * 项目名称：ExamHelper   
 * 类名称：SkinListAdapter   
 * 类描述：   皮肤列表的Adapter，返回每个皮肤缩略图的格式
 * 创建人：张帅   
 * 创建时间：2013-12-6 下午22:07:46   
 * 修改人：张帅  
 * 修改时间：2013-12-15 下午4:02:15   
 * 修改备注：   
 * @version    
 *    
 */
public class SkinListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<SkinListItemData> mItemDataList;
	private int mCurSelect = -1;

	public SkinListAdapter(Context context, List<SkinListItemData> itemDatalist) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		mItemDataList = itemDatalist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mItemDataList == null) {
			return 0;
		}
		return mItemDataList.size();
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

	public void setSelect(int pos) {
		if (pos >= 0 && pos < mItemDataList.size()) {
			mCurSelect = pos;
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.skin_listview_item, null);
		}

		ImageView imageViewLeft = (ImageView) convertView.findViewById(R.id.imageLeft);
		ImageView imageViewRight = (ImageView) convertView.findViewById(R.id.imageRight);
		TextView textView = (TextView) convertView.findViewById(R.id.skinname);

		if (mItemDataList != null) {
			imageViewLeft.setImageResource(mItemDataList.get(position).mImageViewLeftID);
			textView.setText(mItemDataList.get(position).mTextView);

			imageViewRight.setImageResource(mItemDataList.get(position).mImageViewRightID);

			if (position == mCurSelect) {
				imageViewRight.setImageResource(R.drawable.themeradio_check);
			}
		}
		return convertView;
	}

}
