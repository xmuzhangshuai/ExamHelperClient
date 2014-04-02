package com.bishe.examhelper.test;

import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.SingleChoiceDao;
import com.bishe.examhelper.dbService.StudyRecordService;

import android.test.AndroidTestCase;

public class TestSqlite extends AndroidTestCase {
	DaoSession daoSession;
	SingleChoiceDao singleChoiceDao;

	public void test() {
		StudyRecordService service = StudyRecordService.getInstance(getContext());
		service.studyRecordDao.deleteAll();
	}
}
