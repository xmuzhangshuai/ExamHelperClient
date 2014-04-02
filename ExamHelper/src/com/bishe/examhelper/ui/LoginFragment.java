package com.bishe.examhelper.ui;

import java.util.HashMap;
import java.util.Map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;

/**
 * 
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�LoginFragment   
* ��������   ��½ҳ���Fragment
* �����ˣ���˧  
* ����ʱ�䣺2014-3-23 ����2:23:11   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-3-23 ����2:23:11   
* �޸ı�ע��   
* @version    
*
 */
public class LoginFragment extends BaseV4Fragment {

	public static final String EXTRA_EMAIL = "com.bishe.examhelper.extra.EMAIL";

	private UserLoginTask mAuthTask = null;

	// �û��������������
	private String mEmail;
	private String mPassword;

	// UI���
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_login, container, false);

		findViewById();
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mEmailView = (EditText) rootView.findViewById(R.id.email);
		mPasswordView = (EditText) rootView.findViewById(R.id.password);
		mLoginFormView = rootView.findViewById(R.id.login_form);
		mLoginStatusView = rootView.findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) rootView.findViewById(R.id.login_status_message);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		// ���Ĭ������
		mEmail = getActivity().getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView.setText(mEmail);

		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		rootView.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
	}

	/**
	 * ��½����ע�ᰴť������д��󣬼�����
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// ���ô���
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// �洢�û�ֵ
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

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

		if (cancel) {
			// �����������ʾ����
			focusView.requestFocus();
		} else {
			// û�д�����������½
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * ��ʾ��½����
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which
		// allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
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

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply
			// show and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * 
	*    
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�UserLoginTask   
	* ��������   ������¼
	* �����ˣ���˧  
	* ����ʱ�䣺2014-3-23 ����5:38:29   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-3-23 ����5:38:29   
	* �޸ı�ע��   
	* @version    
	*
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				login();
				showProgress(true);
			} catch (Exception e) {
				// return false;
				e.printStackTrace();
			}

			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				getActivity().finish();
			} else {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

		/**
		 *��֤��½
		 */
		public void login() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("mail", mEmail);
			map.put("pass", mPassword);
			String url = HttpUtil.BASE_URL + "LoginServlet";
			try {
				User user = FastJsonTool.getObject(HttpUtil.postRequest(url, map), User.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
