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
 * 项目名称：ExamHelper   
 * 类名称：PersonalModifyDialogFragment   
 * 类描述：   个人信息修改窗口，以dialog的形式，依附于MainActivity
 * 创建人：张帅     
 * 创建时间：2013-12-6 上午11:42:32   
 * 修改人：张帅     
 * 修改时间：2013-12-15 下午3:40:38   
 * 修改备注：   
 * @version    
 *    
 */
public class PersonalModifyDialogFragment extends DialogFragment {
	/**
	* 项目名称：ExamHelper   
	* 类名称：OnUserInfoChangedListener   
	* 类描述：   定义一个回调接口，用于和activity交互
	* 创建人：张帅  
	* @version    
	 */
	public interface OnUserInfoChangedListener {
		public void onUserInfoChanged(User user);
	}

	private View rootView;
	private EditText userNickName;// 昵称
	private EditText userRealname;// 姓名
	private EditText userProfession;// 职业
	private EditText userState;// 个性签名
	private RadioButton userGenderMan;// 男
	private RadioButton userGenderWoman;// 女
	private NumberPicker userAge;// 年龄
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

		// 设置builder的view和按钮
		builder.setTitle("修改个人信息").setView(rootView).setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				modifyUser();
			}
		}).setNegativeButton("取消", new OnClickListener() {
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
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		userAge.setMinValue(0);
		userAge.setMaxValue(100);

		// 如果用户已登录，从数据库中取用户数据
		User user = getUserFromDb();
		if (user != null) {
			userNickName.setText(user.getNickname());
			userRealname.setText(user.getRealname());
			userProfession.setText(user.getProfession());
			userState.setText(user.getUser_state());
			userAge.setValue(user.getAge());
			if (user.getGender().equals("男")) {
				userGenderMan.setChecked(true);
			} else {
				userGenderWoman.setChecked(true);
			}
		} else {
		}
	}

	/**
	 * 查找view
	 */
	public void findViewById() {
		userNickName = (EditText) rootView.findViewById(R.id.user_nickname);// 昵称
		userRealname = (EditText) rootView.findViewById(R.id.user_realname);// 姓名
		userProfession = (EditText) rootView.findViewById(R.id.user_profession);// 职业
		userState = (EditText) rootView.findViewById(R.id.user_state);// 个性签名
		userGenderMan = (RadioButton) rootView.findViewById(R.id.user_gender_man);// 性别
		userGenderWoman = (RadioButton) rootView.findViewById(R.id.user_gender_woman);// 性别
		userAge = (NumberPicker) rootView.findViewById(R.id.user_age);// 年龄
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

	/**
	 * 将修改的信息保存到数据库
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
				user.setGender("男");
			} else {
				user.setGender("女");
			}
			UserService userService = UserService.getInstance(getActivity());
			userService.updateUser(user);
			userChangedListener.onUserInfoChanged(user);
			Toast.makeText(getActivity(), "修改成功！", 1).show();
		}else {
			Toast.makeText(getActivity(), "请先登录！", 1).show();
		}

	}
}
