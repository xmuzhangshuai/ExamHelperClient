package com.bishe.examhelper.service;

import java.util.List;

import android.content.Context;
import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.ExamQuestionDao;
import com.bishe.examhelper.dao.ExamSectionDao;
import com.bishe.examhelper.dao.ExaminationDao;
import com.bishe.examhelper.entities.Examination;

public class ExaminationService {
	private static final String TAG = ExaminationService.class.getSimpleName();
	private static ExaminationService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public ExaminationDao mExaminationDao;
	public ExamQuestionDao mExamQuestionDao;
	public ExamSectionDao mExamSectionDao;

	public ExaminationService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static ExaminationService getInstance(Context context) {
		if (instance == null) {
			instance = new ExaminationService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.mExaminationDao = instance.mDaoSession.getExaminationDao();
			instance.mExamQuestionDao = instance.mDaoSession.getExamQuestionDao();
			instance.mExamSectionDao = instance.mDaoSession.getExamSectionDao();
		}
		return instance;
	}

	/**
	 * 根据ID返回试卷
	 * @param id
	 * @return
	 */
	public Examination loadExamination(Long id) {
		return mExaminationDao.load(id);
	}

	/**
	 * 返回试卷列表
	 * @return
	 */
	public List<Examination> loadAllExaminations() {
		return mExaminationDao.loadAll();
	}


}
