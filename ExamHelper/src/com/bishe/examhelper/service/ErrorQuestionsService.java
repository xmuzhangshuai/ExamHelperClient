package com.bishe.examhelper.service;

import java.util.List;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.ErrorQuestionsDao;
import com.bishe.examhelper.dao.ErrorQuestionsDao.Properties;
import com.bishe.examhelper.dao.QuestionTypeDao;
import com.bishe.examhelper.entities.ErrorQuestions;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.QuestionType;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.utils.DateTimeTools;

public class ErrorQuestionsService {
	private static final String TAG = ErrorQuestionsService.class.getSimpleName();
	private static ErrorQuestionsService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private ErrorQuestionsDao errorQuestionsDao;
	private QuestionTypeDao mQuestionTypeDao;

	public ErrorQuestionsService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �õ�ʵ��
	 * @param context
	 * @return
	 */
	public static ErrorQuestionsService getInstance(Context context) {
		if (instance == null) {
			instance = new ErrorQuestionsService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.errorQuestionsDao = instance.mDaoSession.getErrorQuestionsDao();
			instance.mQuestionTypeDao = instance.mDaoSession.getQuestionTypeDao();
		}
		return instance;
	}

	/**
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public ErrorQuestions loadErrorQuestions(Long id) {
		return errorQuestionsDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<ErrorQuestions> loadAllErrorQuestions() {
		return errorQuestionsDao.loadAll();
	}

	/**
	 * ����IDɾ��ĳ����¼
	 */
	public void deleteErrorQuestionsById(Long id) {
		errorQuestionsDao.deleteByKey(id);
	}

	/**
	 * ɾ��ĳ����¼
	 */
	public void deleteErrorQuestions(ErrorQuestions errorQuestions) {
		errorQuestionsDao.delete(errorQuestions);
	}

	/**
	 * ����һ����ѡ����⣬�����������������������1
	 * @param singleChoice
	 */
	public void addErrorQuestions(SingleChoice singleChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// ���Ҷ�Ӧ���͡��������Ŀ
		ErrorQuestions errorQuestions = errorQuestionsDao.queryBuilder()
				.where(Properties.Question_id.eq(singleChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();

		if (errorQuestions != null) {// ����Ѿ�����
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// ���������1
			errorQuestionsDao.insertOrReplace(errorQuestions);// �滻��ԭ�ȵ�ʵ��
		} else {// ���������
			// �½�ʵ��
			errorQuestions = new ErrorQuestions(null, singleChoice.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
					singleChoice.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * ����һ����ѡ����⣬�����������������������1
	 * @param singleChoice
	 */
	public void addErrorQuestionsToNet(SingleChoice singleChoice) {
		// ��������
		QuestionType questiontype = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique();
		
		//����Section

//		com.netdomains.Errorquestions errorquestion = new com.netdomains.Errorquestions(UserService.getInstance(
//				appContext).getCurrentUser(), questiontype, section, singleChoice.getId(),
//				DateTimeTools.getCurrentDate(), 1);

	}

	/**
	 * ����һ����ѡ����⣬�����������������������1
	 * @param singleChoice
	 */
	public void addErrorQuestions(MultiChoice multiChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// ���Ҷ�Ӧ���͡��������Ŀ
		ErrorQuestions errorQuestions = errorQuestionsDao.queryBuilder()
				.where(Properties.Question_id.eq(multiChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();

		if (errorQuestions != null) {// ����Ѿ�����
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// ���������1
			errorQuestionsDao.insertOrReplace(errorQuestions);// �滻��ԭ�ȵ�ʵ��
		} else {// ���������
			// �½�ʵ��
			errorQuestions = new ErrorQuestions(null, multiChoice.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, multiChoice.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * ����һ����������⣬�����������������������1
	 * @param singleChoice
	 */
	public void addErrorQuestions(MaterialAnalysis materialAnalysis) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// ���Ҷ�Ӧ���͡��������Ŀ
		ErrorQuestions errorQuestions = errorQuestionsDao
				.queryBuilder()
				.where(Properties.Question_id.eq(materialAnalysis.getId()),
						Properties.QuestionType_id.eq(questionTypeId)).unique();

		if (errorQuestions != null) {// ����Ѿ�����
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// ���������1
			errorQuestionsDao.insertOrReplace(errorQuestions);// �滻��ԭ�ȵ�ʵ��
		} else {// ���������
			// �½�ʵ��
			errorQuestions = new ErrorQuestions(null, materialAnalysis.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
					materialAnalysis.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

}
