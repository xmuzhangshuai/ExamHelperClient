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
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�SecurityModifyDialogFragment   
 * ��������   ��ȫ���öԻ����ڣ�������MainActivity,��Ҫ�����޸����룬�Դ��ڵ���ʽ
 * �����ˣ���˧     
 * ����ʱ�䣺2013-12-6 ����6:04:59   
 * �޸��ˣ���˧     
 * �޸�ʱ�䣺2013-12-15 ����3:43:58   
 * �޸ı�ע��   
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

		// ����builder��view�Ͱ�ť
		builder.setTitle("�޸�����").setView(view).setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "�޸ĳɹ���", 1).show();
			}
		}).setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		return builder.create();
	}

	/**
	 * �ж����ݿ��Ƿ��������
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
