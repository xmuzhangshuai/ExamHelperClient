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
	 * 得到实例
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
	 * 根据ID返回实体
	 * @param id
	 * @return
	 */
	public ErrorQuestions loadErrorQuestions(Long id) {
		return errorQuestionsDao.load(id);
	}

	/**
	 * 返回实体列表
	 * @return
	 */
	public List<ErrorQuestions> loadAllErrorQuestions() {
		return errorQuestionsDao.loadAll();
	}

	/**
	 * 根据ID删除某条记录
	 */
	public void deleteErrorQuestionsById(Long id) {
		errorQuestionsDao.deleteByKey(id);
	}

	/**
	 * 删除某条记录
	 */
	public void deleteErrorQuestions(ErrorQuestions errorQuestions) {
		errorQuestionsDao.delete(errorQuestions);
	}

	/**
	 * 插入一条单选题错题，如果曾经出过错，则出错次数加1
	 * @param singleChoice
	 */
	public void addErrorQuestions(SingleChoice singleChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// 查找对应题型、出错的题目
		ErrorQuestions errorQuestions = errorQuestionsDao.queryBuilder()
				.where(Properties.Question_id.eq(singleChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();

		if (errorQuestions != null) {// 如果已经存在
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// 出错次数加1
			errorQuestionsDao.insertOrReplace(errorQuestions);// 替换掉原先的实体
		} else {// 如果不存在
			// 新建实体
			errorQuestions = new ErrorQuestions(null, singleChoice.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
					singleChoice.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * 插入一条单选题错题，如果曾经出过错，则出错次数加1
	 * @param singleChoice
	 */
	public void addErrorQuestionsToNet(SingleChoice singleChoice) {
		// 查找题型
		QuestionType questiontype = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique();
		
		//查找Section

//		com.netdomains.Errorquestions errorquestion = new com.netdomains.Errorquestions(UserService.getInstance(
//				appContext).getCurrentUser(), questiontype, section, singleChoice.getId(),
//				DateTimeTools.getCurrentDate(), 1);

	}

	/**
	 * 插入一条多选题错题，如果曾经出过错，则出错次数加1
	 * @param singleChoice
	 */
	public void addErrorQuestions(MultiChoice multiChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// 查找对应题型、出错的题目
		ErrorQuestions errorQuestions = errorQuestionsDao.queryBuilder()
				.where(Properties.Question_id.eq(multiChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();

		if (errorQuestions != null) {// 如果已经存在
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// 出错次数加1
			errorQuestionsDao.insertOrReplace(errorQuestions);// 替换掉原先的实体
		} else {// 如果不存在
			// 新建实体
			errorQuestions = new ErrorQuestions(null, multiChoice.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, multiChoice.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * 插入一条材料题错题，如果曾经出过错，则出错次数加1
	 * @param singleChoice
	 */
	public void addErrorQuestions(MaterialAnalysis materialAnalysis) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// 查找对应题型、出错的题目
		ErrorQuestions errorQuestions = errorQuestionsDao
				.queryBuilder()
				.where(Properties.Question_id.eq(materialAnalysis.getId()),
						Properties.QuestionType_id.eq(questionTypeId)).unique();

		if (errorQuestions != null) {// 如果已经存在
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// 出错次数加1
			errorQuestionsDao.insertOrReplace(errorQuestions);// 替换掉原先的实体
		} else {// 如果不存在
			// 新建实体
			errorQuestions = new ErrorQuestions(null, materialAnalysis.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
					materialAnalysis.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

}
