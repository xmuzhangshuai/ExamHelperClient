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
	 * �õ�ʵ��
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
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public StudyRecord loadStudyRecord(Long id) {
		return studyRecordDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<StudyRecord> loadAllStudyRecords() {
		return studyRecordDao.queryBuilder()
				.where(Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID()))
				.orderDesc(Properties.Record_time).list();
	}

	/**
	 * ɾ����ǰ�û���ѧϰ��¼
	 */
	public void deleteRecordsOfCurrentUser() {
		List<StudyRecord> studyRecords = loadAllStudyRecords();
		for (StudyRecord studyRecord : studyRecords) {
			studyRecordDao.delete(studyRecord);
		}
	}

	/**
	 *����ʱ���Ⱥ󷵻��б�
	 * @return
	 */
	public List<StudyRecord> loadAllStudyRecordsByTime() {
		return studyRecordDao.queryBuilder().orderDesc(Properties.Record_time).list();
	}

	/**
	 * ����IDɾ��ĳ����¼
	 */
	public void deleteStudyRecordById(Long id) {
		studyRecordDao.deleteByKey(id);
	}

	/**
	 * ɾ��ĳ����¼
	 */
	public void deleteStudyRecord(StudyRecord studyRecord) {
		studyRecordDao.delete(studyRecord);
	}

	/**
	 * ����ѧϰ��¼
	 * @param singleChoice
	 * @return
	 */
	public void insertStudyRecord(Question question, String my_answer, Boolean is_right) {
		Long questionTypeId = null;
		Long questionId = null;
		StudyRecord studyRecord = null;

		if (question.getQuestion_type().equals(DefaultValues.SINGLE_CHOICE)) {// ����ǵ�ѡ��
			SingleChoice singleChoice = (SingleChoice) question;
			// ��������ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
							.eq(DefaultValues.SINGLE_CHOICE)).unique().getId();

			questionId = singleChoice.getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MULTI_CHOICE)) {// ����Ƕ�ѡ��
			MultiChoice multiChoice = (MultiChoice) question;
			// ��������ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
					.unique().getId();
			questionId = multiChoice.getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MATERIAL_ANALYSIS)) {// ����ǲ�����
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) question;
			// ��������ID
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
