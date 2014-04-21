package com.bishe.examhelper.service;

import java.util.List;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.QuestionTypeDao;
import com.bishe.examhelper.dao.StudyRecordDao;
import com.bishe.examhelper.dao.StudyRecordDao.Properties;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.entities.StudyRecord;
import com.bishe.examhelper.utils.DateTimeTools;

public class StudyRecordService {
	private static final String TAG = StudyRecordService.class.getSimpleName();
	private static StudyRecordService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public StudyRecordDao studyRecordDao;
	private QuestionTypeDao mQuestionTypeDao;

	public StudyRecordService() {
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static StudyRecordService getInstance(Context context) {
		if (instance == null) {
			instance = new StudyRecordService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.studyRecordDao = instance.mDaoSession.getStudyRecordDao();
			instance.mQuestionTypeDao = instance.mDaoSession.getQuestionTypeDao();
		}
		return instance;
	}

	/**
	 * 根据ID返回实体
	 * @param id
	 * @return
	 */
	public StudyRecord loadStudyRecord(Long id) {
		return studyRecordDao.load(id);
	}

	/**
	 * 返回实体列表
	 * @return
	 */
	public List<StudyRecord> loadAllStudyRecords() {
		return studyRecordDao.queryBuilder()
				.where(Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID()))
				.orderDesc(Properties.Record_time).list();
	}

	/**
	 * 删除当前用户的学习记录
	 */
	public void deleteRecordsOfCurrentUser() {
		List<StudyRecord> studyRecords = loadAllStudyRecords();
		for (StudyRecord studyRecord : studyRecords) {
			studyRecordDao.delete(studyRecord);
		}
	}

	/**
	 *按照时间先后返回列表
	 * @return
	 */
	public List<StudyRecord> loadAllStudyRecordsByTime() {
		return studyRecordDao.queryBuilder().orderDesc(Properties.Record_time).list();
	}

	/**
	 * 根据ID删除某条记录
	 */
	public void deleteStudyRecordById(Long id) {
		studyRecordDao.deleteByKey(id);
	}

	/**
	 * 删除某条记录
	 */
	public void deleteStudyRecord(StudyRecord studyRecord) {
		studyRecordDao.delete(studyRecord);
	}

	/**
	 * 插入学习记录
	 * @param singleChoice
	 * @return
	 */
	public void insertStudyRecord(Question question, String my_answer, Boolean is_right) {
		Long questionTypeId = null;
		Long questionId = null;
		StudyRecord studyRecord = null;

		if (question.getQuestion_type().equals(DefaultValues.SINGLE_CHOICE)) {// 如果是单选题
			SingleChoice singleChoice = (SingleChoice) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
							.eq(DefaultValues.SINGLE_CHOICE)).unique().getId();

			questionId = singleChoice.getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MULTI_CHOICE)) {// 如果是多选题
			MultiChoice multiChoice = (MultiChoice) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
					.unique().getId();
			questionId = multiChoice.getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MATERIAL_ANALYSIS)) {// 如果是材料题
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
							.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();
			questionId = materialAnalysis.getId();
		}

		studyRecord = studyRecordDao.queryBuilder()
				.where(Properties.QuestionType_id.eq(questionTypeId), Properties.Question_id.eq(questionId)).unique();

		if (studyRecord == null) {
			studyRecord = new StudyRecord(null, questionId, my_answer, is_right, DateTimeTools.getCurrentDate(),
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId);
			studyRecordDao.insert(studyRecord);
		} else {
			studyRecord.setMy_answer(my_answer);
			studyRecord.setIs_right(is_right);
			studyRecord.setRecord_time(DateTimeTools.getCurrentDate());
			studyRecordDao.update(studyRecord);
		}
	}
}
