package com.bishe.examhelper.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultSetting;
import com.bishe.examhelper.service.UserService;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

/**   
 *    
 * 项目名称：ExamHelper   
 * 类名称：RightFragment   
 * 类描述：   主页面右滑菜单，依附于MainActivity，继承了继承pereferenceFragment，自动保存到sharePreferences中?
 * 创建人：张帅     
 * 创建时间�?013-12-5 上午11:21:04   
 * 修改人：张帅     
 * 修改时间�?013-12-15 下午3:42:09   
 * 修改备注�?  
 * @version    
 *    
 */
public class RightFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener,
		OnPreferenceClickListener {

	SharedPreferences sharedPref;
	FeedbackAgent agent;
	OnSignOutPressedListener mListener;

	/**
	 * 
	 * 类名称：OnSignOutPressedListener
	 * 类描述：回调接口，通知Activity更新LeftFragment
	 * 创建人： 张帅
	 * 创建时间：2014-4-19 下午4:56:02
	 *
	 */
	public interface OnSignOutPressedListener {
		public void onSignOutPressed();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (OnSignOutPressedListener) activity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString() + " must implement OnSignOutPressedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		agent = new FeedbackAgent(getActivity());

		/*******设置字体大小的Summary**********/
		ListPreference fontsPref = (ListPreference) findPreference(DefaultKeys.KEY_PREF_SWITCH_FONTSIZE);
		fontsPref.setSummary("当前字体大小：" + fontsPref.getEntry());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		agent.sync();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		/*******给恢复默认绑定事件**********/
		Preference settingDefault = findPreference(DefaultKeys.KEY_PREF_SETTING_DEFAULT);
		settingDefault.setOnPreferenceClickListener(this);

		/*******给精彩推荐绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_COMMENT).setOnPreferenceClickListener(this);

		/*******给注销登陆绑定事件**********/
		findPreference(DefaultKeys.KEY_SING_OUT).setOnPreferenceClickListener(this);

		/*******给科目切换绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_COURSE_SWITCH).setOnPreferenceClickListener(this);

		/*******给题库管理绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_LIBRARY_MANAGE).setOnPreferenceClickListener(this);

		/*******给意见反馈绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_ADVICE_FEEDBACK).setOnPreferenceClickListener(this);

		/*******给检查更新绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_CHECK_UPDATE).setOnPreferenceClickListener(this);

		/*******给关于软件绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_ABOUT_SOFTWARE).setOnPreferenceClickListener(this);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		/*******给恢复默认绑定事件**********/
		Preference settingDefault = findPreference(DefaultKeys.KEY_PREF_SETTING_DEFAULT);
		settingDefault.setOnPreferenceClickListener(this);

		/*******给精彩推荐绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_COMMENT).setOnPreferenceClickListener(this);

		/*******给注销登陆绑定事件**********/
		findPreference(DefaultKeys.KEY_SING_OUT).setOnPreferenceClickListener(this);

		/*******给科目切换绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_COURSE_SWITCH).setOnPreferenceClickListener(this);

		/*******给题库管理绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_LIBRARY_MANAGE).setOnPreferenceClickListener(this);

		/*******给意见反馈绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_ADVICE_FEEDBACK).setOnPreferenceClickListener(this);

		/*******给检查更新绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_CHECK_UPDATE).setOnPreferenceClickListener(this);

		/*******给关于软件绑定事件**********/
		findPreference(DefaultKeys.KEY_PREF_ABOUT_SOFTWARE).setOnPreferenceClickListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		// 选择字体
		if (key.equals(DefaultKeys.KEY_PREF_SWITCH_FONTSIZE)) {
			ListPreference fontsPref = (ListPreference) findPreference(key);
			fontsPref.setSummary("当前字体大小：" + fontsPref.getEntry());
		}

	}

	@SuppressLint("ShowToast")
	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		/************如果点击了恢复默认************/
		if (preference.getKey().equals(DefaultKeys.KEY_PREF_SETTING_DEFAULT)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("恢复默认").setMessage("是否全部恢复默认选项？点击确定将删除当前用户设置。")
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							setToDeflult();
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					}).show();
		}
		/************如果点击了精彩推荐************/
		else if (preference.getKey().equals(DefaultKeys.KEY_PREF_COMMENT)) {
			Intent intent = new Intent(getActivity(), AdCommentActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}

		/************如果点击了关于软件************/
		else if (preference.getKey().equals(DefaultKeys.KEY_PREF_ABOUT_SOFTWARE)) {
			Toast.makeText(getActivity(), "点击了关于软件", 1).show();
		}

		/************如果点击了检查更新************/
		else if (preference.getKey().equals(DefaultKeys.KEY_PREF_CHECK_UPDATE)) {
			UmengUpdateAgent.forceUpdate(getActivity());
		}

		/************如果点击了意见反馈************/
		else if (preference.getKey().equals(DefaultKeys.KEY_PREF_ADVICE_FEEDBACK)) {
			// 当开发者回复用户反馈后，需要提醒用户
			agent.startFeedbackActivity();
		}

		/************如果点击了题库管理************/
		else if (preference.getKey().equals(DefaultKeys.KEY_PREF_LIBRARY_MANAGE)) {
			Toast.makeText(getActivity(), "点击了题库管理", 1).show();
		}

		/************如果点击了科目切换************/
		else if (preference.getKey().equals(DefaultKeys.KEY_PREF_COURSE_SWITCH)) {
			Toast.makeText(getActivity(), "点击了科目切换", 1).show();
		}

		/************如果点击了注销登陆************/
		else if (preference.getKey().equals(DefaultKeys.KEY_SING_OUT)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setIcon(R.drawable.icon_warning).setTitle("温馨提示").setMessage("是否注销？")
					.setPositiveButton("是", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							UserService userService = UserService.getInstance(getActivity());
							// 注销
							userService.singOut();
							mListener.onSignOutPressed();
						}
					}).setNegativeButton("否", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					}).show();
		}

		return true;
	}

	/************恢复默认设置***************/
	private void setToDeflult() {
		SharedPreferences settings = getPreferenceScreen().getSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(DefaultKeys.KEY_PREF_IF_LIGHT_ON, DefaultSetting.DEFAULT_PREF_IF_LIGHT_ON);
		editor.putBoolean(DefaultKeys.KEY_PREF_NIGHT_MODEL, DefaultSetting.DEFAULT_PREF_NIGHT_MODEL);
		editor.putBoolean(DefaultKeys.KEY_PREF_CHECK_NOW, DefaultSetting.DEFAULT_PREF_CHECK_NOW);
		editor.putBoolean(DefaultKeys.KEY_PREF_VIBRATE_AFTER, DefaultSetting.DEFAULT_PREF_VIBRATE_AFTER);
		editor.putString(DefaultKeys.KEY_PREF_SWITCH_FONTSIZE, DefaultSetting.DEFAULT_PREF_SWITCH_FONTSIZE);
		editor.commit();

		/**********重新加载当前Fragment**********/
		Fragment setttingFragment = new RightFragment();
		FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.main_right_fragment, setttingFragment);
		fragmentTransaction.commit();
	}

}
