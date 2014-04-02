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
	 * 得到实例
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
	 * 根据ID返回实体
	 * @param id
	 * @return
	 */
	public Groups loadGroups(Long id) {
		return mGroupsDao.load(id);
	}

	/**
	 * 返回实体列表
	 * @return
	 */
	public List<Groups> loadAllGroups() {
		return mGroupsDao.loadAll();
	}

	/**
	 * 根据ID删除某条记录
	 */
	public void deleteGroupsById(Long id) {
		mGroupsDao.deleteByKey(id);
	}

	/**
	 * 删除某条记录
	 */
	public void deleteGroups(Groups groups) {
		mGroupsDao.delete(groups);
	}

	/**
	 * 更新组内数据
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
				groups.setFinished_num(groups.getFinished_num() + 1);// 完成数量加1
				if (studyRecord.getIs_right() == false) {
					groups.setError_num(groups.getError_num() + 1);// 出错数量加1
				}
			}

		}
	}
}
