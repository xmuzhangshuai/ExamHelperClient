package com.bishe.examhelper.ui;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.service.UserService;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.NetworkUtils;

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
	private View rootView;
	private EditText oldPass;
	private EditText newPass;
	private EditText secPass;
	private String oldPassString;
	private String newPassString;
	private String secPassString;
	UserService userService;

	@SuppressLint("ShowToast")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		rootView = inflater.inflate(R.layout.security_modify, null);
		findViewById();
		userService = UserService.getInstance(getActivity());

		// ����builder��view�Ͱ�ť
		builder.setTitle("�޸�����").setView(rootView).setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (NetworkUtils.isNetworkAvailable(getActivity())) {
					if (userService.getCurrentUser() != null) {
						modifyPass();
					} else {
						Toast.makeText(getActivity(), "���ȵ�¼..", 1).show();
					}
				} else {
					NetworkUtils.networkStateTips(getActivity());
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

	/**
	 * �޸�����
	 */
	public void modifyPass() {
		oldPassString = oldPass.getText().toString();
		newPassString = newPass.getText().toString();
		secPassString = secPass.getText().toString();

		if (oldPassString != null) {
			if (oldPassString.equals(userService.getCurrentUser().getPassword())) {
				if (newPassString.equals(secPassString)) {
					if (newPassString.length() < 6) {
						Toast.makeText(getActivity(), "���볤��̫�̣�", 1).show();
					} else {
						User user = userService.getCurrentUser();
						user.setPassword(newPassString);
						userService.userDao.update(user);
						new Thread() {
							public void run() {
								String url = "ManageUserServlet";
								Map<String, String> map = new HashMap<String, String>();
								map.put("type", "modifyPass");
								map.put("newPass", newPassString);
								map.put("userId", String.valueOf(userService.getCurrentUserID()));
								try {
									HttpUtil.postRequest(url, map);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							};
						}.start();
					}
					Toast.makeText(getActivity(), "�޸ĳɹ���", 1).show();
				} else {
					Toast.makeText(getActivity(), "�����������벻һ��", 1).show();
				}
			} else {
				Toast.makeText(getActivity(), "�������벻��ȷ������������", 1).show();
			}
		} else {
			Toast.makeText(getActivity(), "�����������", 1).show();
		}
	}

	public void findViewById() {
		oldPass = (EditText) rootView.findViewById(R.id.oldPass);
		newPass = (EditText) rootView.findViewById(R.id.newPass);
		secPass = (EditText) rootView.findViewById(R.id.secPass);
	}

}
