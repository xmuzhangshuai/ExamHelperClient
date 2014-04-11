package com.bishe.examhelper.service;

import java.util.ArrayList;
import java.util.List;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.SectionDao;
import com.bishe.examhelper.dao.SubjectDao;
import com.bishe.examhelper.dao.SubjectDao.Properties;
import com.bishe.examhelper.entities.Collection;
import com.bishe.examhelper.entities.ErrorQuestions;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.Section;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.entities.StudyRecord;
import com.bishe.examhelper.entities.Subject;

import android.content.Context;

public class SectionService {

	private static final String TAG = SectionService.class.getSimpleName();
	private static SectionService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public SectionDao sectionDao;
	private SubjectDao subjectDao;

	public SectionService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static SectionService getInstance(Context context) {
		if (instance == null) {
			instance = new SectionService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.sectionDao = instance.mDaoSession.getSectionDao();
			instance.subjectDao = instance.mDaoSession.getSubjectDao();
		}
		return instance;
	}

	/**
	 * 根据ID返回实体
	 * @param id
	 * @return
	 */
	public Section loadCollection(Long id) {
		return sectionDao.load(id);
	}

	/**
	 * 根据Subject名称返回章节列表
	 * @param id
	 * @return
	 */
	public List<Section> getSectionsBySubjectName(String subjectName) {
		List<Section> sections = new ArrayList<Section>();
		Subject subject = subjectDao.queryBuilder().where(Properties.Subject_name.eq(subjectName.trim())).unique();
		if (subject != null)
			sections = subject.getSectionList();
		return sections;
	}

	/**
	 * 根据章节名称返回ID
	 * @return
	 */
	public Long getIdBySectionName(String sectionName) {
		return sectionDao.queryBuilder()
				.where(com.bishe.examhelper.dao.SectionDao.Properties.Section_name.eq(sectionName)).unique().getId();
	}

	/**
	 * 根据章节返回章节下题目数量
	 * @param sectionName
	 * @return
	 */
	public int getQuestionNum(Section section) {
		return section.getSingleChoiceList().size() + section.getMultiChoiceList().size()
				+ section.getMaterialAnalysisList().size();
	}

	/**
	 * 根据章节返回章节下已完成题目数量
	 * @param section
	 * @return
	 */
	public int getFinishedNum(Section section) {
		int count = 0;
		StudyRecordService service = StudyRecordService.getInstance(appContext);
		List<StudyRecord> studyRecordList = service.loadAllStudyRecords();
		for (StudyRecord studyRecord : studyRecordList) {
			if (studyRecord.getQuestionType().getType_name().equals(DefaultValues.SINGLE_CHOICE)) {// 如果是单选题
				SingleChoice singleChoice = SingleChoiceService.getInstance(appContext).loadSingleChoice(
						studyRecord.getQuestion_id());
				if (singleChoice.getSection().equals(section)) {
					count++;
				}
			}
			if (studyRecord.getQuestionType().getType_name().equals(DefaultValues.MULTI_CHOICE)) {// 如果是多选题
				MultiChoice multiChoice = MultiChoiceService.getInstance(appContext).loadMultiChoice(
						studyRecord.getQuestion_id());
				if (multiChoice.getSection().equals(section)) {
					count++;
				}
			}
			if (studyRecord.getQuestionType().getType_name().equals(DefaultValues.MATERIAL_ANALYSIS)) {// 如果是材料题
				MaterialAnalysis materialAnalysis = MaterialAnalysisService.getInstance(appContext)
						.loadMaterialAnalysis(studyRecord.getQuestion_id());
				if (materialAnalysis.getSection().equals(section)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 返回当前用户章节下的收藏列表
	 * @return
	 */
	public List<Collection> getCurrentCollections(Section section) {
		List<Collection> temp = section.getCollectionList();
		List<Collection> cList = new ArrayList<Collection>();
		for (Collection collection : temp) {
			if (collection.getUser_id() == UserService.getInstance(appContext).getCurrentUserID()) {
				cList.add(collection);
			}
		}

		return cList;
	}

	/**
	 * 返回当前用户章节下的错题列表
	 * @return
	 */
	public List<ErrorQuestions> getCurrentErrors(Section section) {
		List<ErrorQuestions> temp = section.getErrorQuestionsList();
		List<ErrorQuestions> eList = new ArrayList<ErrorQuestions>();
		for (ErrorQuestions errorQuestion : temp) {
			if (errorQuestion.getUser_id() == UserService.getInstance(appContext).getCurrentUserID()) {
				eList.add(errorQuestion);
			}
		}

		return eList;
	}
}
