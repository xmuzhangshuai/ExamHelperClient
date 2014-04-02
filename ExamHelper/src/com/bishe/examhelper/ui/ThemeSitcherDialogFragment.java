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
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�ThemeSitcherDialogFragment   
 * ��������   ����ѡ�񴰿ڣ�������MainActivity������仯����Ϊ����ͼƬ�͹�����ɫ�ı仯���洢��sharedpreference��
 *          �´δ�sharedpreferenceȡ����
 * �����ˣ���˧     
 * ����ʱ�䣺2013-12-8 ����19:32:20   
 * �޸��ˣ���˧     
 * �޸�ʱ�䣺2013-12-15 ����3:51:28   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class ThemeSitcherDialogFragment extends DialogFragment {
	// ��������ͼ
	private final int[] themeThumbImages = new int[] { R.drawable.person_center_backthumb01,
			R.drawable.person_center_backthumb02, R.drawable.person_center_backthumb03,
			R.drawable.person_center_backthumb04, R.drawable.person_center_backthumb05, };
	// �����ֽ
	private final int[] themeImages = new int[] { R.drawable.person_center_background01,
			R.drawable.person_center_background02, R.drawable.person_center_background03,
			R.drawable.person_center_background04, R.drawable.person_center_background05, };
	// ��������
	private final String text[] = { "«έ����", "����ŨŨ", "��ɫ����", "��װ�ع�", "ʢ�Ĺ�ʵ" };
	// ������ɫ
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

		/*** sharedPreference��������ID ***/
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_user_center),
				Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = sharedPref.edit();

		init();
		// ����builder��view�Ͱ�ť
		builder.setView(view).setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// �������ȷ�����򽫵�ǰ��������Ϊѡ�е�ͼƬ
				if (currentTheme != -1) {
					RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(
							R.id.person_cente_relativeLayout);
					TextView textView1 = (TextView) getActivity().findViewById(R.id.user_center_itemname1);// ���ø��˱�����ɫ
					TextView textView2 = (TextView) getActivity().findViewById(R.id.user_center_itemname2);// ���ð�ȫ������ɫ
					TextView textView3 = (TextView) getActivity().findViewById(R.id.user_center_itemname3);// ���û��ֱ�����ɫ
					textView1.setBackgroundResource(themeColors[currentTheme]);
					textView2.setBackgroundResource(themeColors[currentTheme]);
					textView3.setBackgroundResource(themeColors[currentTheme]);

					relativeLayout.setBackgroundResource(themeImages[currentTheme]);
					editor.putInt(getString(R.string.user_center_theme_key), themeImages[currentTheme]);// ������ͼƬ���浽Sharedpreference
					editor.putInt(getString(R.string.user_center_themecolor_key), themeColors[currentTheme]);
					if (editor.commit())
						Toast.makeText(getActivity(), "�л�����ɹ���", 1).show();
				}

			}
		}).setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		return builder.create();
	}

	// ��ʼ��mListView
	private void init() {

		mListView = (ListView) view.findViewById(R.id.dialong_choose_theme_listview);
		mSkinListAdapter = new SkinListAdapter(getActivity(), getItemList());
		mListView.setAdapter(mSkinListAdapter);
		// Ϊ�����б��е�ѡ������¼�
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

	// ���ÿ����¼�Ķ���
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
