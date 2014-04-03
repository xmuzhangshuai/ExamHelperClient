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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�RegisterFragment   
* ��������   ע��Fragment
* �����ˣ���˧  
* ����ʱ�䣺2014-4-3 ����11:23:19   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-4-3 ����11:23:19   
* �޸ı�ע��   
* @version    
*    
*/
public class RegisterFragment extends BaseV4Fragment {

	/**
	 * �û�ע���첽����
	 */
	private UserRegisterTask mRegisterTask = null;

	// �û����������������ֻ���
	private String mEmail;
	private String mPassword;
	private String mPhone;

	// UI���
	private View rootView;
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mPhoneView;
	private View mLoginStatusView;// ����
	private Button registerButton;// ע��

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

		// ע��
		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attemptRegist();
			}
		});
	}

	/**
	 * ע�ᰴť������д��󣬼�����
	 */
	public void attemptRegist() {
		if (mRegisterTask != null) {
			return;
		}

		// ���ô���
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mPhoneView.setError(null);

		// �洢�û�ֵ
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPhone = mPhoneView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// �������
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// ��������ʽ
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		// ����ֻ���
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
			// �����������ʾ����
			focusView.requestFocus();
		} else {
			// û�д�����������½
			showProgress(true);
			mRegisterTask = new UserRegisterTask();
			mRegisterTask.execute((Void) null);
		}
	}

	/**
	 * ��ʾע�Ỻ��
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
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�UserRegisterTask   
	* ��������   �첽����ע��
	* �����ˣ���˧  
	* ����ʱ�䣺2014-4-3 ����3:34:13   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-4-3 ����3:34:13   
	* �޸ı�ע��   
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
				Log.e("ע��", "ע��ʧ�ܣ�");
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
				Toast.makeText(getActivity(), "��ϲ��ע��ɹ����뾡�����Ƹ�������...", 2000).show();
				getActivity().finish();
			} else {
				Toast.makeText(getActivity(), "ע��ʧ��", 1).show();
			}
		}

		@Override
		protected void onCancelled() {
			mRegisterTask = null;
			showProgress(false);
		}
	}

}
