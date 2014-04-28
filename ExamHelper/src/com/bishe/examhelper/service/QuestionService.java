package com.bishe.examhelper.service;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.MaterialAnalysisDao;
import com.bishe.examhelper.dao.MultiChoiceDao;
import com.bishe.examhelper.dao.SingleChoiceDao;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.QuestionType;
import com.bishe.examhelper.entities.SingleChoice;

public class QuestionService {
	private static final String TAG = QuestionService.class.getSimpleName();
	private static QuestionService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public SingleChoiceDao singleChoiceDao;
	public MultiChoiceDao multiChoiceDao;
	public MaterialAnalysisDao materialAnalysisDao;

	public QuestionService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static QuestionService getInstance(Context context) {
		if (instance == null) {
			instance = new QuestionService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.singleChoiceDao = instance.mDaoSession.getSingleChoiceDao();
			instance.multiChoiceDao = instance.mDaoSession.getMultiChoiceDao();
			instance.materialAnalysisDao = instance.mDaoSession.getMaterialAnalysisDao();
		}
		return instance;
	}

	/**
	 * 根据题型ID和问题ID得到题干
	 * @param questionId
	 * @param questionTypeId
	 * @return
	 */
	public String getQuestionStem(Long questionId, Long questionTypeId) {
		String questionStem = null;
		QuestionTypeService questionTypeService = QuestionTypeService.getInstance(appContext);
		QuestionType questionType = questionTypeService.loadQuestionType(questionTypeId);
		if (questionType != null) {
			if (questionType.getType_name().equals(DefaultValues.SINGLE_CHOICE)) {
				SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(appContext);
				SingleChoice singleChoice = singleChoiceService.loadSingleChoice(questionId);
				if (singleChoice != null) {
					questionStem = singleChoice.getQuestion_stem();
				}
			}

			if (questionType.getType_name().equals(DefaultValues.MULTI_CHOICE)) {
				MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(appContext);
				MultiChoice multiChoice = multiChoiceService.loadMultiChoice(questionId);
				if (multiChoice != null) {
					questionStem = multiChoice.getQuestion_stem();
				}
			}

			if (questionType.getType_name().equals(DefaultValues.MATERIAL_ANALYSIS)) {
				MaterialAnalysisService materialAnalysisService = MaterialAnalysisService.getInstance(appContext);
				MaterialAnalysis materialAnalysis = materialAnalysisService.loadMaterialAnalysis(questionId);
				if (materialAnalysis != null) {
					if (materialAnalysis.getMaterial().length() > 100) {
						questionStem = materialAnalysis.getMaterial().substring(0, 100) + "...";
					} else {
						questionStem = materialAnalysis.getMaterial();
					}

				}
			}
		}

		return questionStem;
	}

	/**
	 * 根据题型ID和问题ID得到题目
	 * @param questionId
	 * @param questionTypeId
	 * @return
	 */
	public Object getQuestionObject(Long questionId, Long questionTypeId) {
		QuestionTypeService questionTypeService = QuestionTypeService.getInstance(appContext);
		QuestionType questionType = questionTypeService.loadQuestionType(questionTypeId);
		if (questionType != null) {
			if (questionType.getType_name().equals(DefaultValues.SINGLE_CHOICE)) {
				SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(appContext);
				SingleChoice singleChoice = singleChoiceService.loadSingleChoice(questionId);
				if (singleChoice != null) {
					return singleChoice;
				}
			}

			if (questionType.getType_name().equals(DefaultValues.MULTI_CHOICE)) {
				MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(appContext);
				MultiChoice multiChoice = multiChoiceService.loadMultiChoice(questionId);
				if (multiChoice != null) {
					return multiChoice;
				}
			}

			if (questionType.getType_name().equals(DefaultValues.MATERIAL_ANALYSIS)) {
				MaterialAnalysisService materialAnalysisService = MaterialAnalysisService.getInstance(appContext);
				MaterialAnalysis materialAnalysis = materialAnalysisService.loadMaterialAnalysis(questionId);
				if (materialAnalysis != null) {
					return materialAnalysis;
				}
			}
		}
		return null;
	}

	/**
	 * 根据题型ID和问题ID得到正确答案
	 * @param questionId
	 * @param questionTypeId
	 * @return
	 */
	public String getRightAnswer(Long questionId, Long questionTypeId) {
		String rightAnswer = "";
		QuestionTypeService questionTypeService = QuestionTypeService.getInstance(appContext);
		QuestionType questionType = questionTypeService.loadQuestionType(questionTypeId);
		if (questionType != null) {
			if (questionType.getType_name().equals(DefaultValues.SINGLE_CHOICE)) {
				SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(appContext);
				SingleChoice singleChoice = singleChoiceService.loadSingleChoice(questionId);
				if (singleChoice != null) {
					if (singleChoice.getAnswer().equals("A")) {
						rightAnswer = singleChoice.getOptionA();
					}
					if (singleChoice.getAnswer().equals("B")) {
						rightAnswer = singleChoice.getOptionB();
					}
					if (singleChoice.getAnswer().equals("C")) {
						rightAnswer = singleChoice.getOptionC();
					}
					if (singleChoice.getAnswer().equals("D")) {
						rightAnswer = singleChoice.getOptionD();
					}
				}
			}

			if (questionType.getType_name().equals(DefaultValues.MULTI_CHOICE)) {
				MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(appContext);
				MultiChoice multiChoice = multiChoiceService.loadMultiChoice(questionId);
				if (multiChoice != null) {
					if (multiChoice.getAnswerA()) {
						rightAnswer = multiChoice.getOptionA();
					}
					if (multiChoice.getAnswerB()) {
						rightAnswer = multiChoice.getOptionB();
					}
					if (multiChoice.getAnswerC()) {
						rightAnswer = multiChoice.getOptionC();
					}
					if (multiChoice.getAnswerD()) {
						rightAnswer = multiChoice.getOptionD();
					}
				}
			}

		}
		return rightAnswer;
	}
}
