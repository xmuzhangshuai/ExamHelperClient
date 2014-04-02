package com.bishe.examhelper.base;

import com.bishe.examhelper.R;
import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.ui.SettingActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class BaseQuestionFragment extends BaseV4Fragment {

	/**
	 * 答题模式：分为练习模式：DefaultValues.MODEL_EXERCISE 、模拟考试模式：DefaultValues.MODEL_MOCK_EXAM
	 *           展示模式：
	 * 默认为练习模式*/
	protected String model;

	/**** SharedPreferences，获得用户设置****/
	protected SharedPreferences sharedPreferences;

	/**** SharedPreferences值，true表示立即显示答案****/
	protected boolean isCheckNow;

	/****SharedPreferences值，true答错题后震动****/
	protected boolean isVibrateAfter;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		/************获取模式************/
		model = getArguments().getString(DefaultKeys.KEY_QUESTION_MODEL);
		// 默认为练习模式
		if (model == null) {
			model = DefaultValues.MODEL_EXERCISE;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/************获取用户设置*************/
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		isCheckNow = sharedPreferences.getBoolean(DefaultKeys.KEY_PREF_CHECK_NOW, false);
		isVibrateAfter = sharedPreferences.getBoolean(DefaultKeys.KEY_PREF_VIBRATE_AFTER, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		/*******如果是练习模式,显示设置按钮*******/
		if (model.equals(DefaultValues.MODEL_EXERCISE)) {
			inflater.inflate(R.menu.question, menu);
		} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// 如果是考试模式
			/**************************/
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// 如果点击了设置按钮
		if (item.getItemId() == R.id.setting) {
			Intent intent = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 判断答案正误
	 */
	protected void judgeAnswer() {

	}

	/**
	 * 显示答案，包括选项正误提示和解析区
	 */
	protected void displayAnswer() {

	}

	/**
	 * 初始化题目内容View
	 */
	protected void initQuestionView() {
	}

	/**
	 * 初始化Button View
	 */
	protected void initButtonView() {
	}

	@Override
	protected void findViewById() {
	}

	@Override
	protected void initView() {
	}

}
