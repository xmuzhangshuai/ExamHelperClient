package com.bishe.examhelper.test;

import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.SingleChoiceDao;
import com.bishe.examhelper.dbService.StudyRecordService;
import com.bishe.examhelper.dbService.UserService;

import android.test.AndroidTestCase;

public class TestSqlite extends AndroidTestCase {
	DaoSession daoSession;
	SingleChoiceDao singleChoiceDao;

	public void privnale() {
		UserService userService = UserService.getInstance(getContext());
		String avatar = new String(userService.getCurrentUser().getAvatar());
		System.out.println("����Ϊ��" + avatar.length());
		System.out.println("Сͷ��"
				+ new String(UserService.getInstance(getContext()).getCurrentUser().getSmall_avatar()).length());
	}
}
