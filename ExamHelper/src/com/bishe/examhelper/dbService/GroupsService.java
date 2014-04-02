package com.bishe.examhelper.dbService;

import java.util.List;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.GroupsDao;
import com.bishe.examhelper.dao.StudyRecordDao.Properties;
import com.bishe.examhelper.entities.GroupQuestion;
import com.bishe.examhelper.entities.Groups;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.entities.StudyRecord;

public class GroupsService {
	private static final String TAG = GroupsService.class.getSimpleName();
	private static GroupsService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public GroupsDao mGroupsDao;

	public GroupsService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �õ�ʵ��
	 * @param context
	 * @return
	 */
	public static GroupsService getInstance(Context context) {
		if (instance == null) {
			instance = new GroupsService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.mGroupsDao = instance.mDaoSession.getGroupsDao();
		}
		return instance;
	}

	/**
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public Groups loadGroups(Long id) {
		return mGroupsDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<Groups> loadAllGroups() {
		return mGroupsDao.loadAll();
	}

	/**
	 * ����IDɾ��ĳ����¼
	 */
	public void deleteGroupsById(Long id) {
		mGroupsDao.deleteByKey(id);
	}

	/**
	 * ɾ��ĳ����¼
	 */
	public void deleteGroups(Groups groups) {
		mGroupsDao.delete(groups);
	}

	/**
	 * ������������
	 * @param groups
	 */
	public void refresh(Groups groups) {
		List<GroupQuestion> groupQuestions = groups.getQuestionList();
		for (GroupQuestion groupQuestion : groupQuestions) {
			StudyRecordService studyRecordService = StudyRecordService.getInstance(appContext);
			StudyRecord studyRecord = studyRecordService.studyRecordDao
					.queryBuilder()
					.where(Properties.QuestionType_id.eq(groups.getQuestionType_id()),
							Properties.Question_id.eq(groupQuestion.getQuestion_id())).unique();
			if (studyRecord != null) {
				groups.setFinished_num(groups.getFinished_num() + 1);// ���������1
				if (studyRecord.getIs_right() == false) {
					groups.setError_num(groups.getError_num() + 1);// ����������1
				}
			}

		}
	}
}
