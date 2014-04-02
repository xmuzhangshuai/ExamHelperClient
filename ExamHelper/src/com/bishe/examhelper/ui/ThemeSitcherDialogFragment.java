package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.List;
import com.bishe.examhelper.R;
import com.bishe.examhelper.adapters.SkinListAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**   
 *    
 * 项目名称：ExamHelper   
 * 类名称：ThemeSitcherDialogFragment   
 * 类描述：   主题选择窗口，依附于MainActivity，主题变化表现为背景图片和功能颜色的变化。存储在sharedpreference中
 *          下次从sharedpreference取出。
 * 创建人：张帅     
 * 创建时间：2013-12-8 下午19:32:20   
 * 修改人：张帅     
 * 修改时间：2013-12-15 下午3:51:28   
 * 修改备注：   
 * @version    
 *    
 */
public class ThemeSitcherDialogFragment extends DialogFragment {
	// 主题缩略图
	private final int[] themeThumbImages = new int[] { R.drawable.person_center_backthumb01,
			R.drawable.person_center_backthumb02, R.drawable.person_center_backthumb03,
			R.drawable.person_center_backthumb04, R.drawable.person_center_backthumb05, };
	// 主题壁纸
	private final int[] themeImages = new int[] { R.drawable.person_center_background01,
			R.drawable.person_center_background02, R.drawable.person_center_background03,
			R.drawable.person_center_background04, R.drawable.person_center_background05, };
	// 主题名称
	private final String text[] = { "芦苇静溢", "秋意浓浓", "绿色花语", "银装素裹", "盛夏果实" };
	// 主题颜色
	private final int[] themeColors = new int[] { R.color.Ucenter_theme1, R.color.Ucenter_theme2,
			R.color.Ucenter_theme3, R.color.Ucenter_theme4, R.color.Ucenter_theme5 };
	private final int uncheckIcon = R.drawable.themeradio;
	private ListView mListView;
	private SkinListAdapter mSkinListAdapter;
	private TextView mTitleTextView;
	private View view;
	private int currentTheme = -1;

	@SuppressLint({ "CommitPrefEdits", "ShowToast" })
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dialog_choose_theme, null);

		/*** sharedPreference保存主题ID ***/
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_user_center),
				Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = sharedPref.edit();

		init();
		// 设置builder的view和按钮
		builder.setView(view).setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 如果单击确定，则将当前主题设置为选中的图片
				if (currentTheme != -1) {
					RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(
							R.id.person_cente_relativeLayout);
					TextView textView1 = (TextView) getActivity().findViewById(R.id.user_center_itemname1);// 设置个人背景颜色
					TextView textView2 = (TextView) getActivity().findViewById(R.id.user_center_itemname2);// 设置安全背景颜色
					TextView textView3 = (TextView) getActivity().findViewById(R.id.user_center_itemname3);// 设置积分背景颜色
					textView1.setBackgroundResource(themeColors[currentTheme]);
					textView2.setBackgroundResource(themeColors[currentTheme]);
					textView3.setBackgroundResource(themeColors[currentTheme]);

					relativeLayout.setBackgroundResource(themeImages[currentTheme]);
					editor.putInt(getString(R.string.user_center_theme_key), themeImages[currentTheme]);// 将主题图片保存到Sharedpreference
					editor.putInt(getString(R.string.user_center_themecolor_key), themeColors[currentTheme]);
					if (editor.commit())
						Toast.makeText(getActivity(), "切换主题成功！", 1).show();
				}

			}
		}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		return builder.create();
	}

	// 初始化mListView
	private void init() {

		mListView = (ListView) view.findViewById(R.id.dialong_choose_theme_listview);
		mSkinListAdapter = new SkinListAdapter(getActivity(), getItemList());
		mListView.setAdapter(mSkinListAdapter);
		// 为主题列表中的选项定义点击事件
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mSkinListAdapter.setSelect(position);
				mTitleTextView.setBackgroundResource(themeThumbImages[position]);
				currentTheme = position;
			}
		});
		mTitleTextView = (TextView) view.findViewById(R.id.skinTitle);
	}

	// 获得每条记录的对象
	private List<SkinListItemData> getItemList() {
		List<SkinListItemData> list = new ArrayList<SkinListItemData>();
		int themecouts = themeThumbImages.length;
		for (int i = 0; i < themecouts; i++) {
			SkinListItemData data = new SkinListItemData();
			data.mImageViewLeftID = themeThumbImages[i];
			data.mTextView = text[i];
			data.mImageViewRightID = uncheckIcon;
			list.add(data);
		}
		return list;
	}

	public class SkinListItemData {
		public int mImageViewLeftID;
		public String mTextView;
		public int mImageViewRightID;
	}
}
