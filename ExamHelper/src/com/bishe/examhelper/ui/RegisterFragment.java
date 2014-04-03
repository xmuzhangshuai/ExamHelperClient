package com.bishe.examhelper.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.dbService.UserService;
import com.bishe.examhelper.entities.User;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：RegisterFragment   
* 类描述：   注册Fragment
* 创建人：张帅  
* 创建时间：2014-4-3 上午11:23:19   
* 修改人：张帅   
* 修改时间：2014-4-3 上午11:23:19   
* 修改备注：   
* @version    
*    
*/
public class RegisterFragment extends BaseV4Fragment {

	/**
	 * 用户注册异步任务
	 */
	private UserRegisterTask mRegisterTask = null;

	// 用户输入邮箱和密码和手机号
	private String mEmail;
	private String mPassword;
	private String mPhone;

	// UI组件
	private View rootView;
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mPhoneView;
	private View mLoginStatusView;// 缓冲
	private Button registerButton;// 注册

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_regist, container, false);
		findViewById();
		initView();
		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mEmailView = (EditText) rootView.findViewById(R.id.email);
		mPasswordView = (EditText) rootView.findViewById(R.id.password);
		mPhoneView = (EditText) rootView.findViewById(R.id.phone);
		registerButton = (Button) rootView.findViewById(R.id.register_btn);
		mLoginStatusView = rootView.findViewById(R.id.login_status);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.register_btn || id == EditorInfo.IME_NULL) {
					attemptRegist();
					return true;
				}
				return false;
			}
		});

		// 注册
		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attemptRegist();
			}
		});
	}

	/**
	 * 注册按钮，如果有错误，检查错误
	 */
	public void attemptRegist() {
		if (mRegisterTask != null) {
			return;
		}

		// 重置错误
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mPhoneView.setError(null);

		// 存储用户值
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPhone = mPhoneView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// 检查密码
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// 检查邮箱格式
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		// 检查手机号
		if (TextUtils.isEmpty(mPhone)) {
			mPhoneView.setError(getString(R.string.error_field_required));
			focusView = mPhoneView;
			cancel = true;
		} else if (mPhone.length() != 11) {
			mPhoneView.setError(getString(R.string.error_phone));
			focusView = mPhoneView;
			cancel = true;
		}

		if (cancel) {
			// 如果错误，则提示错误
			focusView.requestFocus();
		} else {
			// 没有错误，则联网登陆
			showProgress(true);
			mRegisterTask = new UserRegisterTask();
			mRegisterTask.execute((Void) null);
		}
	}

	/**
	 * 显示注册缓冲
	 */
	public void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply
			// show and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			registerButton.setEnabled(!show);
		}
	}

	/**   
	*    
	* 项目名称：ExamHelper   
	* 类名称：UserRegisterTask   
	* 类描述：   异步任务注册
	* 创建人：张帅  
	* 创建时间：2014-4-3 下午3:34:13   
	* 修改人：张帅   
	* 修改时间：2014-4-3 下午3:34:13   
	* 修改备注：   
	* @version    
	*    
	*/
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				// Simulate network access.
				UserService userService = UserService.getInstance(getActivity());
				User user = new User(null, mEmail, mPassword, null, null, null, mPhone, null, null, null, null, 0,
						null, null);
				userService.saveUser(user);
			} catch (Exception e) {
				Log.e("注册", "注册失败！");
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mRegisterTask = null;
			showProgress(false);

			if (success) {
				Toast.makeText(getActivity(), "恭喜您注册成功！请尽快完善个人资料...", 2000).show();
				getActivity().finish();
			} else {
				Toast.makeText(getActivity(), "注册失败", 1).show();
			}
		}

		@Override
		protected void onCancelled() {
			mRegisterTask = null;
			showProgress(false);
		}
	}

}
