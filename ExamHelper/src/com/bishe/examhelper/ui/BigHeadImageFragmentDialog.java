package com.bishe.examhelper.ui;

import com.bishe.examhelper.R;
import com.bishe.examhelper.customerwidget.DragImageView;
import com.bishe.examhelper.dbService.UserService;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.utils.DensityUtil;
import com.bishe.examhelper.utils.ImageTools;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：BigHeadImageFragmentDialog   
* 类描述：   显示大头像对话框
* 创建人：张帅  
* 创建时间：2014-4-1 下午8:53:30   
* 修改人：张帅   
* 修改时间：2014-4-1 下午8:53:30   
* 修改备注：   
* @version    
*    
*/
public class BigHeadImageFragmentDialog extends DialogFragment {
	private DragImageView dragImageView;// 自定义控件
	private View rootView;
	private Bitmap userBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		byte[] images = getArguments().getByteArray("com.bishe.examhelper.headimageBitmap");
		if (images != null) {
			userBitmap = ImageTools.byteToBitmap(images);
		}

		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_big_head_image, container, false);
		dragImageView = (DragImageView) rootView.findViewById(R.id.dragImageView);

		Bitmap drageBitmap = ImageTools.getBitmap(userBitmap, DensityUtil.getScreenWidthforPX(getActivity()),
				DensityUtil.getScreenHeightforPX(getActivity()));

		dragImageView.setImageBitmap(drageBitmap);
		dragImageView.setmActivity(getActivity());

		dragImageView.setScreen_H(DensityUtil.getScreenHeightforPX(getActivity())
				- DensityUtil.getStatusBarHeight(getActivity()));
		dragImageView.setScreen_W(DensityUtil.getScreenWidthforPX(getActivity()));

		return rootView;
	}

}
