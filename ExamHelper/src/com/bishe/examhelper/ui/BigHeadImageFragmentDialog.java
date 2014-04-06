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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�BigHeadImageFragmentDialog   
* ��������   ��ʾ��ͷ��Ի���
* �����ˣ���˧  
* ����ʱ�䣺2014-4-1 ����8:53:30   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-4-1 ����8:53:30   
* �޸ı�ע��   
* @version    
*    
*/
public class BigHeadImageFragmentDialog extends DialogFragment {
	private DragImageView dragImageView;// �Զ���ؼ�
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
