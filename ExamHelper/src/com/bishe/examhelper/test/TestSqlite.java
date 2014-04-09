package com.bishe.examhelper.test;

import com.androidquery.AQuery;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.SingleChoiceDao;
import com.bishe.examhelper.service.StudyRecordService;
import com.bishe.examhelper.service.UserService;
import com.bishe.examhelper.utils.HttpUtil;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;

public class TestSqlite extends AndroidTestCase {
	DaoSession daoSession;
	SingleChoiceDao singleChoiceDao;

	public void privnale() {
		UserService userService = UserService.getInstance(getContext());
		// userService.deleteAllUser();
		System.out.println(userService.getCurrentUserID());
	}
}
