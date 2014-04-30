package com.bishe.examhelper.service;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.SubjectDao;
import com.bishe.examhelper.entities.Subject;

public class SubjectService {
	private static final String TAG = SubjectService.class.getSimpleName();
	private static SubjectService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public SubjectDao subjectDao;

	public SubjectService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static SubjectService getInstance(Context context) {
		if (instance == null) {
			instance = new SubjectService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.subjectDao = instance.mDaoSession.getSubjectDao();
		}
		return instance;
	}

	/**
	 * 得到当前科目
	 * @return
	 */
	public Subject getCurrentSubject() {
		return subjectDao.load(new Long(getCurrentSubjectId()));
	}

	/**
	 * 得到当前科目Id
	 * @return
	 */
	public int getCurrentSubjectId() {
		if (subjectDao.loadAll().get(0) != null) {
			return subjectDao.loadAll().get(0).getId().intValue();
		}
		return 0;
	}

}
