package com.bishe.examhelper.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.bishe.examhelper.R;
import com.bishe.examhelper.dbService.UserService;
import com.bishe.examhelper.entities.User;

/**   
 *    
 * 项目名称：ExamHelper   
 * 类名称：SecurityModifyDialogFragment   
 * 类描述：   安全设置对话窗口，依附于MainActivity,主要功能修改密码，以窗口的形式
 * 创建人：张帅     
 * 创建时间：2013-12-6 下午6:04:59   
 * 修改人：张帅     
 * 修改时间：2013-12-15 下午3:43:58   
 * 修改备注：   
 * @version    
 *    
 */
public class SecurityModifyDialogFragment extends DialogFragment {
	private View view;

	@SuppressLint("ShowToast")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.security_modify, null);

		// 设置builder的view和按钮
		builder.setTitle("修改密码").setView(view).setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "修改成功！", 1).show();
			}
		}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		return builder.create();
	}

	/**
	 * 判断数据库是否存有数据
	 * @return
	 */
	protected User getUserFromDb() {
		User user = UserService.getInstance(getActivity()).getCurrentUser();
		if (user != null) {
			return user;
		} else {
			return null;
		}
	}
}
