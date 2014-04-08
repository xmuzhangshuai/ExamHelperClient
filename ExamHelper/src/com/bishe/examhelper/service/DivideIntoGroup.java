package com.bishe.examhelper.service;

import java.util.List;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultSetting;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.GroupQuestionDao;
import com.bishe.examhelper.dao.GroupsDao;
import com.bishe.examhelper.dao.QuestionTypeDao;
import com.bishe.examhelper.dao.QuestionTypeDao.Properties;
import com.bishe.examhelper.dao.SectionDao;
import com.bishe.examhelper.entities.GroupQuestion;
import com.bishe.examhelper.entities.Groups;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.QuestionType;
import com.bishe.examhelper.entities.Section;
import com.bishe.examhelper.entities.SingleChoice;

public class DivideIntoGroup {

	private Context myContext;

	public DivideIntoGroup(Context context) {
		this.myContext = context;
	}

	public void divideIntoGroup() {
		DaoSession daoSession = BaseApplication.getDaoSession(myContext);
		GroupsDao groupsDao = daoSession.getGroupsDao();
		GroupQuestionDao groupQuestionDao = daoSession.getGroupQuestionDao();
		// ɾ���������м�¼
		groupsDao.deleteAll();
		groupQuestionDao.deleteAll();

		SectionDao sectionDao = daoSession.getSectionDao();
		QuestionTypeDao questionTypeDao = daoSession.getQuestionTypeDao();
		QuestionType questionType;

		int startSectionID = 5;// ��Ҫ�������ʼ�½�ID
		int endSectionID = 9;// ��Ҫ����Ľ����½�ID
		int count = 0;// ��Ŀ������
		int groupContent = DefaultSetting.GROUP_CONTENT;// һ������Ŀ����
		int groupNum = 0;// ����
		int remain = 0;// ����һ���ʣ�����Ŀ����

		/***��ѡ��***/
		// ���ҵ�ѡ���Ӧ�����ͣ����ڻ������ID
		questionType = questionTypeDao.queryBuilder().where(Properties.Type_name.eq("����ѡ����")).unique();
		// ��ÿ����Ӧ���½��еĵ�ѡ����з���
		for (int sectionID = startSectionID; sectionID <= endSectionID; sectionID++) {
			Section mySection = sectionDao.load((long) sectionID);
			List<SingleChoice> singleChoices = mySection.getSingleChoiceList();
			count = singleChoices.size();
			groupNum = count / groupContent;
			remain = count % groupContent;

			// ����ÿ��ĸ������з���
			for (int i = 0; i < groupNum; i++) {
				Groups myGroup = new Groups(null, groupContent, 0, 0, "��ѡ����" + (i + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int j = 0; j < groupContent; j++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, singleChoices.get(i * groupContent + j)
							.getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

			// ʣ�����Ŀ����һ������ǲ���
			if (remain > 0) {

				Groups myGroup = new Groups(null, remain, 0, 0, "��ѡ����" + (groupNum + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int k = 0; k < remain; k++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, singleChoices
							.get(groupNum * groupContent + k).getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

		}
		/***��ѡ��***/

		/***��ѡ��***/
		// ���Ҷ�ѡ���Ӧ�����ͣ����ڻ������ID
		questionType = questionTypeDao.queryBuilder().where(Properties.Type_name.eq("����ѡ����")).unique();
		// ��ÿ����Ӧ���½��еĶ�ѡ����з���
		for (int sectionID = startSectionID; sectionID <= endSectionID; sectionID++) {
			Section mySection = sectionDao.load((long) sectionID);
			List<MultiChoice> multiChoices = mySection.getMultiChoiceList();
			count = multiChoices.size();
			groupNum = count / groupContent;
			remain = count % groupContent;

			// ����ÿ��ĸ������з���
			for (int i = 0; i < groupNum; i++) {
				Groups myGroup = new Groups(null, groupContent, 0, 0, "��ѡ����" + (i + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int j = 0; j < groupContent; j++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, multiChoices.get(i * groupContent + j)
							.getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

			// ʣ�����Ŀ����һ������ǲ���
			if (remain > 0) {
				Groups myGroup = new Groups(null, remain, 0, 0, "��ѡ����" + (groupNum + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int k = 0; k < remain; k++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, multiChoices.get(groupNum * groupContent + k)
							.getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

		}
		/***��ѡ��***/

		/***���Ϸ�����***/
		// ���Ҳ��Ϸ������Ӧ�����ͣ����ڻ������ID
		questionType = questionTypeDao.queryBuilder().where(Properties.Type_name.eq("���Ϸ�����")).unique();
		// ��ÿ����Ӧ���½��еĲ��Ϸ�������з���
		for (int sectionID = startSectionID; sectionID <= endSectionID; sectionID++) {
			Section mySection = sectionDao.load((long) sectionID);
			List<MaterialAnalysis> materialAnalysis = mySection.getMaterialAnalysisList();
			count = materialAnalysis.size();
			groupNum = count / groupContent;
			remain = count % groupContent;

			// ����ÿ��ĸ������з���
			for (int i = 0; i < groupNum; i++) {
				Groups myGroup = new Groups(null, groupContent, 0, 0, "���Ϸ�����" + (i + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int j = 0; j < groupContent; j++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, materialAnalysis.get(i * groupContent + j)
							.getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

			// ʣ�����Ŀ����һ������ǲ���
			if (remain > 0) {
				Groups myGroup = new Groups(null, remain, 0, 0, "���Ϸ�����" + (groupNum + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int k = 0; k < remain; k++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, materialAnalysis.get(
							groupNum * groupContent + k).getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

		}
		/***���Ϸ�����***/
	}
}
