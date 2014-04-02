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
	 * ����ģʽ����Ϊ��ϰģʽ��DefaultValues.MODEL_EXERCISE ��ģ�⿼��ģʽ��DefaultValues.MODEL_MOCK_EXAM
	 *           չʾģʽ��
	 * Ĭ��Ϊ��ϰģʽ*/
	protected String model;

	/**** SharedPreferences������û�����****/
	protected SharedPreferences sharedPreferences;

	/**** SharedPreferencesֵ��true��ʾ������ʾ��****/
	protected boolean isCheckNow;

	/****SharedPreferencesֵ��true��������****/
	protected boolean isVibrateAfter;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		/************��ȡģʽ************/
		model = getArguments().getString(DefaultKeys.KEY_QUESTION_MODEL);
		// Ĭ��Ϊ��ϰģʽ
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
		/************��ȡ�û�����*************/
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		isCheckNow = sharedPreferences.getBoolean(DefaultKeys.KEY_PREF_CHECK_NOW, false);
		isVibrateAfter = sharedPreferences.getBoolean(DefaultKeys.KEY_PREF_VIBRATE_AFTER, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		/*******�������ϰģʽ,��ʾ���ð�ť*******/
		if (model.equals(DefaultValues.MODEL_EXERCISE)) {
			inflater.inflate(R.menu.question, menu);
		} else if (model.equals(DefaultValues.MODEL_MOCK_EXAM)) {// ����ǿ���ģʽ
			/**************************/
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// �����������ð�ť
		if (item.getItemId() == R.id.setting) {
			Intent intent = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * �жϴ�����
	 */
	protected void judgeAnswer() {

	}

	/**
	 * ��ʾ�𰸣�����ѡ��������ʾ�ͽ�����
	 */
	protected void displayAnswer() {

	}

	/**
	 * ��ʼ����Ŀ����View
	 */
	protected void initQuestionView() {
	}

	/**
	 * ��ʼ��Button View
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
