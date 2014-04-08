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
		// 删除分组所有记录
		groupsDao.deleteAll();
		groupQuestionDao.deleteAll();

		SectionDao sectionDao = daoSession.getSectionDao();
		QuestionTypeDao questionTypeDao = daoSession.getQuestionTypeDao();
		QuestionType questionType;

		int startSectionID = 5;// 需要分组的起始章节ID
		int endSectionID = 9;// 需要分组的结束章节ID
		int count = 0;// 题目总数量
		int groupContent = DefaultSetting.GROUP_CONTENT;// 一组内题目数量
		int groupNum = 0;// 组数
		int remain = 0;// 不满一组的剩余的题目数量

		/***单选题***/
		// 查找单选题对应的题型，用于获得题型ID
		questionType = questionTypeDao.queryBuilder().where(Properties.Type_name.eq("单项选择题")).unique();
		// 把每个对应的章节中的单选题进行分组
		for (int sectionID = startSectionID; sectionID <= endSectionID; sectionID++) {
			Section mySection = sectionDao.load((long) sectionID);
			List<SingleChoice> singleChoices = mySection.getSingleChoiceList();
			count = singleChoices.size();
			groupNum = count / groupContent;
			remain = count % groupContent;

			// 按照每组的个数进行分组
			for (int i = 0; i < groupNum; i++) {
				Groups myGroup = new Groups(null, groupContent, 0, 0, "单选题组" + (i + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int j = 0; j < groupContent; j++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, singleChoices.get(i * groupContent + j)
							.getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

			// 剩余的题目放在一组里，但是不满
			if (remain > 0) {

				Groups myGroup = new Groups(null, remain, 0, 0, "单选题组" + (groupNum + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int k = 0; k < remain; k++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, singleChoices
							.get(groupNum * groupContent + k).getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

		}
		/***单选题***/

		/***多选题***/
		// 查找多选题对应的题型，用于获得题型ID
		questionType = questionTypeDao.queryBuilder().where(Properties.Type_name.eq("多项选择题")).unique();
		// 把每个对应的章节中的多选题进行分组
		for (int sectionID = startSectionID; sectionID <= endSectionID; sectionID++) {
			Section mySection = sectionDao.load((long) sectionID);
			List<MultiChoice> multiChoices = mySection.getMultiChoiceList();
			count = multiChoices.size();
			groupNum = count / groupContent;
			remain = count % groupContent;

			// 按照每组的个数进行分组
			for (int i = 0; i < groupNum; i++) {
				Groups myGroup = new Groups(null, groupContent, 0, 0, "多选题组" + (i + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int j = 0; j < groupContent; j++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, multiChoices.get(i * groupContent + j)
							.getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

			// 剩余的题目放在一组里，但是不满
			if (remain > 0) {
				Groups myGroup = new Groups(null, remain, 0, 0, "多选题组" + (groupNum + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int k = 0; k < remain; k++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, multiChoices.get(groupNum * groupContent + k)
							.getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

		}
		/***多选题***/

		/***材料分析题***/
		// 查找材料分析题对应的题型，用于获得题型ID
		questionType = questionTypeDao.queryBuilder().where(Properties.Type_name.eq("材料分析题")).unique();
		// 把每个对应的章节中的材料分析题进行分组
		for (int sectionID = startSectionID; sectionID <= endSectionID; sectionID++) {
			Section mySection = sectionDao.load((long) sectionID);
			List<MaterialAnalysis> materialAnalysis = mySection.getMaterialAnalysisList();
			count = materialAnalysis.size();
			groupNum = count / groupContent;
			remain = count % groupContent;

			// 按照每组的个数进行分组
			for (int i = 0; i < groupNum; i++) {
				Groups myGroup = new Groups(null, groupContent, 0, 0, "材料分析题" + (i + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int j = 0; j < groupContent; j++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, materialAnalysis.get(i * groupContent + j)
							.getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

			// 剩余的题目放在一组里，但是不满
			if (remain > 0) {
				Groups myGroup = new Groups(null, remain, 0, 0, "材料分析题" + (groupNum + 1), mySection.getId(),
						questionType.getId());
				groupsDao.insert(myGroup);
				for (int k = 0; k < remain; k++) {
					GroupQuestion groupQuestion = new GroupQuestion(null, materialAnalysis.get(
							groupNum * groupContent + k).getId(), myGroup.getId());
					groupQuestionDao.insert(groupQuestion);
				}
			}

		}
		/***材料分析题***/
	}
}
