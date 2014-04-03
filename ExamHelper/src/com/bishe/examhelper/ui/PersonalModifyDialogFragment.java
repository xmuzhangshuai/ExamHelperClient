package com.bishe.examhelper.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;
import com.bishe.examhelper.R;
import com.bishe.examhelper.dbService.UserService;
import com.bishe.examhelper.entities.User;

/**   
 *    
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�PersonalModifyDialogFragment   
 * ��������   ������Ϣ�޸Ĵ��ڣ���dialog����ʽ��������MainActivity
 * �����ˣ���˧     
 * ����ʱ�䣺2013-12-6 ����11:42:32   
 * �޸��ˣ���˧     
 * �޸�ʱ�䣺2013-12-15 ����3:40:38   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class PersonalModifyDialogFragment extends DialogFragment {
	/**
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�OnUserInfoChangedListener   
	* ��������   ����һ���ص��ӿڣ����ں�activity����
	* �����ˣ���˧  
	* @version    
	 */
	public interface OnUserInfoChangedListener {
		public void onUserInfoChanged(User user);
	}

	private View rootView;
	private EditText userNickName;// �ǳ�
	private EditText userRealname;// ����
	private EditText userProfession;// ְҵ
	private EditText userState;// ����ǩ��
	private RadioButton userGenderMan;// ��
	private RadioButton userGenderWoman;// Ů
	private NumberPicker userAge;// ����
	private OnUserInfoChangedListener userChangedListener;

	@SuppressLint("ShowToast")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		rootView = inflater.inflate(R.layout.personal_modify, null);

		findViewById();

		initView();

		// ����builder��view�Ͱ�ť
		builder.setTitle("�޸ĸ�����Ϣ").setView(rootView).setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				modifyUser();
			}
		}).setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			userChangedListener = (OnUserInfoChangedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnUserInfoChangedListener");
		}
	}

	/**
	 * ��ʼ��View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		userAge.setMinValue(0);
		userAge.setMaxValue(100);

		// ����û��ѵ�¼�������ݿ���ȡ�û�����
		User user = getUserFromDb();
		if (user != null) {
			userNickName.setText(user.getNickname());
			userRealname.setText(user.getRealname());
			userProfession.setText(user.getProfession());
			userState.setText(user.getUser_state());
			userAge.setValue(user.getAge());
			if (user.getGender().equals("��")) {
				userGenderMan.setChecked(true);
			} else {
				userGenderWoman.setChecked(true);
			}
		} else {
		}
	}

	/**
	 * ����view
	 */
	public void findViewById() {
		userNickName = (EditText) rootView.findViewById(R.id.user_nickname);// �ǳ�
		userRealname = (EditText) rootView.findViewById(R.id.user_realname);// ����
		userProfession = (EditText) rootView.findViewById(R.id.user_profession);// ְҵ
		userState = (EditText) rootView.findViewById(R.id.user_state);// ����ǩ��
		userGenderMan = (RadioButton) rootView.findViewById(R.id.user_gender_man);// �Ա�
		userGenderWoman = (RadioButton) rootView.findViewById(R.id.user_gender_woman);// �Ա�
		userAge = (NumberPicker) rootView.findViewById(R.id.user_age);// ����
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

	/**
	 * ���޸ĵ���Ϣ���浽���ݿ�
	 * @param user
	 */
	public void modifyUser() {
		User user = getUserFromDb();
		if (user != null) {
			user.setAge(userAge.getValue());
			user.setNickname(userNickName.getText().toString());
			user.setRealname(userRealname.getText().toString());
			user.setProfession(userProfession.getText().toString());
			user.setUser_state(userState.getText().toString());
			if (userGenderMan.isChecked()) {
				user.setGender("��");
			} else {
				user.setGender("Ů");
			}
			UserService userService = UserService.getInstance(getActivity());
			userService.updateUser(user);
			userChangedListener.onUserInfoChanged(user);
			Toast.makeText(getActivity(), "�޸ĳɹ���", 1).show();
		}else {
			Toast.makeText(getActivity(), "���ȵ�¼��", 1).show();
		}

	}
}
