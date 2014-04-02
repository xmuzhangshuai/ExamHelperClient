package com.bishe.examhelper.adapters;

import java.io.Serializable;
import java.util.List;

import com.bishe.examhelper.config.DefaultKeys;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.ui.MaterialAnalysisFragment;
import com.bishe.examhelper.ui.MultiChoiceFragment;
import com.bishe.examhelper.ui.SingleChoiceFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RandomExerciseAdapter extends FragmentPagerAdapter {
	List<Question> questionList;

	public RandomExerciseAdapter(FragmentManager fm, List<Question> questions) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.questionList = questions;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		// ��������ǵ���ѡ����
		if (questionList.get(position).getQuestion_type().equals(DefaultValues.SINGLE_CHOICE)) {
			SingleChoiceFragment mySingleChoiceFragment = new SingleChoiceFragment();
			Bundle localBundle = new Bundle();

			// ����һ��singleChoice����
			localBundle.putSerializable(DefaultKeys.BUNDLE_SINGLE_CHOICE, (Serializable) questionList.get(position));
			localBundle.putInt(DefaultKeys.BUNDLE_SINGLE_CHOICE_POSITION, position + 1);
			mySingleChoiceFragment.setArguments(localBundle);
			return mySingleChoiceFragment;
		}

		// ����Ƕ���ѡ����
		else if (questionList.get(position).getQuestion_type().equals(DefaultValues.MULTI_CHOICE)) {
			MultiChoiceFragment myMultiChoiceFragment = new MultiChoiceFragment();
			Bundle bundle = new Bundle();

			// ����һ��MultiChoice����
			bundle.putSerializable(DefaultKeys.BUNDLE_MULTI_CHOICE, (Serializable) questionList.get(position));
			bundle.putInt(DefaultKeys.BUNDLE_MULTI_CHOICE_POSITION, position + 1);
			myMultiChoiceFragment.setArguments(bundle);
			return myMultiChoiceFragment;
		}

		// ����ǲ��Ϸ�����
		else if (questionList.get(position).getQuestion_type().equals(DefaultValues.MATERIAL_ANALYSIS)) {
			MaterialAnalysisFragment materialAnalysisFragment = new MaterialAnalysisFragment();
			Bundle bundle = new Bundle();

			// ����һ��MaterialAnalysis����
			bundle.putSerializable(DefaultKeys.BUNDLE_MATERIAL_ANALYSIS, (Serializable) questionList.get(position));
			materialAnalysisFragment.setArguments(bundle);
			return materialAnalysisFragment;
		} else {
			return null;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return questionList.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return questionList.get(position).getQuestion_type() + (position + 1);
	}

}
