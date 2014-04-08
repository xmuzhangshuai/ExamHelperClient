package com.bishe.examhelper.service;

import java.util.List;

import android.content.Context;
import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.QuestionTypeDao;
import com.bishe.examhelper.dao.QuestionTypeDao.Properties;
import com.bishe.examhelper.entities.QuestionType;

public class QuestionTypeService {
	private static final String TAG = QuestionTypeService.class.getSimpleName();
	private static QuestionTypeService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public QuestionTypeDao questionTypeDao;

	public QuestionTypeService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �õ�ʵ��
	 * @param context
	 * @return
	 */
	public static QuestionTypeService getInstance(Context context) {
		if (instance == null) {
			instance = new QuestionTypeService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.questionTypeDao = instance.mDaoSession.getQuestionTypeDao();
		}
		return instance;
	}

	/**
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public QuestionType loadQuestionType(Long id) {
		return questionTypeDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<QuestionType> loadAllQuestionTypes() {
		return questionTypeDao.loadAll();
	}

	/**
	 * �����������Ʒ���ID
	 * @param typeName
	 * @return
	 */
	public Long getIdByName(String typeName) {
		Long id;
		id = questionTypeDao.queryBuilder().where(Properties.Type_name.eq(typeName)).unique().getId();
		return id;
	}
}
