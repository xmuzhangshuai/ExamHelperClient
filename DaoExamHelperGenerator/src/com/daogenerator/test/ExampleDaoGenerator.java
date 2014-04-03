/**
 * 
 */
package com.daogenerator.test;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class ExampleDaoGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Schema schema = new Schema(1, "com.bishe.examhelper.entities");
		schema.setDefaultJavaPackageDao("com.bishe.examhelper.dao");
		schema.enableKeepSectionsByDefault();
		addQuestions(schema);
		new DaoGenerator().generateAll(schema, "../ExamHelper/src-models");
	}

	private static void addQuestions(Schema schema) {

		/*** 用户表 ***/
		Entity userEntity = schema.addEntity("User");
		userEntity.implementsSerializable();
		userEntity.addIdProperty().autoincrement();
		userEntity.addStringProperty("mail").notNull();// 邮箱
		userEntity.addStringProperty("password").notNull(); // 密码
		userEntity.addStringProperty("nickname");// 昵称
		userEntity.addStringProperty("realname");// 真实姓名
		userEntity.addIntProperty("age");// 年龄
		userEntity.addStringProperty("phone");// 手机号码
		userEntity.addStringProperty("gender");// 性别
		userEntity.addStringProperty("user_state");// 个人签名
		userEntity.addStringProperty("profession");// 职业
		userEntity.addStringProperty("area");// 区域
		userEntity.addIntProperty("integral");// 积分
		userEntity.addByteArrayProperty("avatar");// 用户头像
		userEntity.addByteArrayProperty("small_avatar");// 用户小头像
		userEntity.addBooleanProperty("current");//是否为当前用户
		/*** 用户表 ***/

		/*** 科目表 ***/
		Entity subject = schema.addEntity("Subject");
		subject.implementsSerializable();
		subject.addIdProperty().autoincrement();
		subject.addStringProperty("subject_name").notNull();// 科目名称
		/*** 科目表 ***/

		/*** 章节表 ***/
		Entity section = schema.addEntity("Section");
		section.implementsSerializable();
		section.addIdProperty().autoincrement();
		section.addStringProperty("section_name").notNull();// 章节名称
		Property subject_id = section.addLongProperty("subject_id").notNull().getProperty();// 科目外键
		section.addToOne(subject, subject_id);
		/*** 章节表 ***/

		// 科目中有多个章节
		ToMany subjectToSection = subject.addToMany(section, subject_id);
		subjectToSection.setName("sectionList");

		/*** 单选题表 ***/
		Entity singleChoice = schema.addEntity("SingleChoice");
		singleChoice.implementsSerializable();
		singleChoice.setSuperclass("Question");
		singleChoice.addIdProperty().autoincrement();
		singleChoice.addStringProperty("question_stem").notNull();// 题干
		singleChoice.addStringProperty("optionA");// 选项A
		singleChoice.addStringProperty("optionB");// 选项B
		singleChoice.addStringProperty("optionC");// 选项C
		singleChoice.addStringProperty("optionD");// 选项D
		singleChoice.addStringProperty("optionE");// 选项E
		singleChoice.addStringProperty("answer");// 答案
		singleChoice.addStringProperty("analysis");// 答案解析
		singleChoice.addStringProperty("remark");// 备注
		singleChoice.addBooleanProperty("flag");// 标志位
		Property singleChoice_section_id = singleChoice.addLongProperty("section_id").notNull().getProperty();// 章节外键
		singleChoice.addToOne(section, singleChoice_section_id);
		/*** 单选题表 ***/

		// 章节中有多个单选题
		ToMany sectionToSingleChoice = section.addToMany(singleChoice, singleChoice_section_id);
		sectionToSingleChoice.setName("singleChoiceList");

		/*** 多选题表 ***/
		Entity MultiChoice = schema.addEntity("MultiChoice");
		MultiChoice.implementsSerializable();
		MultiChoice.setSuperclass("Question");
		MultiChoice.addIdProperty().autoincrement();
		MultiChoice.addStringProperty("question_stem").notNull();// 题干
		MultiChoice.addStringProperty("optionA");// 选项A
		MultiChoice.addStringProperty("optionB");// 选项B
		MultiChoice.addStringProperty("optionC");// 选项C
		MultiChoice.addStringProperty("optionD");// 选项D
		MultiChoice.addStringProperty("optionE");// 选项E
		MultiChoice.addStringProperty("optionF");// 选项F
		MultiChoice.addBooleanProperty("answerA");// 选项A是否为答案选项
		MultiChoice.addBooleanProperty("answerB");// 选项B是否为答案选项
		MultiChoice.addBooleanProperty("answerC");// 选项C是否为答案选项
		MultiChoice.addBooleanProperty("answerD");// 选项D是否为答案选项
		MultiChoice.addBooleanProperty("answerE");// 选项E是否为答案选项
		MultiChoice.addBooleanProperty("answerF");// 选项F是否为答案选项
		MultiChoice.addStringProperty("analysis");// 答案解析
		MultiChoice.addStringProperty("remark");// 备注
		MultiChoice.addBooleanProperty("flag");// 标志位
		Property multiChoice_section_id = MultiChoice.addLongProperty("section_id").notNull().getProperty();// 章节外键
		MultiChoice.addToOne(section, multiChoice_section_id);
		/*** 多选题表 ***/

		// 章节中有多个多选题
		ToMany sectionToMultiChoice = section.addToMany(MultiChoice, multiChoice_section_id);
		sectionToMultiChoice.setName("multiChoiceList");

		/*** 判断题表 ***/
		Entity trueOrFalse = schema.addEntity("TrueOrFalse");
		trueOrFalse.implementsSerializable();
		trueOrFalse.setSuperclass("Question");
		trueOrFalse.addIdProperty().autoincrement();
		trueOrFalse.addStringProperty("question_stem").notNull();// 题干
		trueOrFalse.addBooleanProperty("answer");// 答案
		trueOrFalse.addStringProperty("analysis");// 答案解析
		trueOrFalse.addStringProperty("remark");// 备注
		trueOrFalse.addBooleanProperty("flag");// 标志位
		Property trueOrFalse_section_id = trueOrFalse.addLongProperty("section_id").notNull().getProperty();// 章节外键
		trueOrFalse.addToOne(section, trueOrFalse_section_id);
		/*** 判断题表 ***/

		// 章节中有多个判断题
		ToMany sectionToTrueOrFalse = section.addToMany(trueOrFalse, trueOrFalse_section_id);
		sectionToTrueOrFalse.setName("trueOrFalseList");

		/*** 材料分析题表 ***/
		Entity MaterialAnalysis = schema.addEntity("MaterialAnalysis");
		MaterialAnalysis.implementsSerializable();
		MaterialAnalysis.setSuperclass("Question");
		MaterialAnalysis.addIdProperty().autoincrement();
		MaterialAnalysis.addStringProperty("material").notNull();// 材料
		MaterialAnalysis.addByteArrayProperty("material_image");// 材料图片
		MaterialAnalysis.addStringProperty("remark");// 备注
		MaterialAnalysis.addBooleanProperty("flag");// 标志位
		Property MaterialAnalysis_section_id = MaterialAnalysis.addLongProperty("section_id").notNull().getProperty();// 章节外键
		MaterialAnalysis.addToOne(section, MaterialAnalysis_section_id);
		/*** 材料分析题表 ***/

		// 章节中有多个材料分析题
		ToMany sectionToMaterialAnalysis = section.addToMany(MaterialAnalysis, MaterialAnalysis_section_id);
		sectionToMaterialAnalysis.setName("MaterialAnalysisList");

		/*** 材料分析题问题表 ***/
		Entity questionsOfMaterial = schema.addEntity("QuestionsOfMaterial");
		questionsOfMaterial.implementsSerializable();
		questionsOfMaterial.addIdProperty().autoincrement();
		questionsOfMaterial.addIntProperty("qusetion_number");// 题号
		questionsOfMaterial.addStringProperty("question_stem");// 题干
		questionsOfMaterial.addStringProperty("answer");// 答案
		questionsOfMaterial.addStringProperty("analysis");// 答案解析
		questionsOfMaterial.addIntProperty("score");// 每小题分值
		Property Questions_material_id = questionsOfMaterial.addLongProperty("material_id").notNull().getProperty();// 材料外键
		questionsOfMaterial.addToOne(MaterialAnalysis, Questions_material_id);
		/*** 材料分析题问题表 ***/

		// 一个材料有多个小题
		ToMany materialToQuestions = MaterialAnalysis.addToMany(questionsOfMaterial, Questions_material_id);
		materialToQuestions.setName("questionsOfMaterialList");

		/*** 试卷表 ***/
		Entity examination = schema.addEntity("Examination");
		examination.implementsSerializable();
		examination.addIdProperty().autoincrement();
		examination.addStringProperty("exam_type");// 试卷类型
		examination.addStringProperty("exam_name");// 试卷题目
		examination.addStringProperty("exam_request");// 试卷要求
		examination.addIntProperty("exam_time");// 考试时间
		Property exam_subject_id = examination.addLongProperty("subject_id").notNull().getProperty();// 科目外键
		examination.addToOne(subject, exam_subject_id);
		/*** 试卷表 ***/

		// 一个科目有多张试卷
		ToMany subjectToExam = subject.addToMany(examination, exam_subject_id);
		subjectToExam.setName("examinationList");

		/*** 题型表 ***/
		Entity questionType = schema.addEntity("QuestionType");
		questionType.implementsSerializable();
		questionType.addIdProperty().autoincrement();
		questionType.addStringProperty("type_name").notNull();// 题型名称
		/*** 题型表 ***/

		/*** 试卷大题表 ***/
		Entity examSection = schema.addEntity("ExamSection");
		examSection.implementsSerializable();
		examSection.addIdProperty().autoincrement();
		examSection.addStringProperty("request");// 题型要求
		examSection.addIntProperty("question_num");// 题目数量
		examSection.addIntProperty("question_score");// 每小题分值
		Property questionType_id = examSection.addLongProperty("questionType_id").notNull().getProperty();// 题型外键
		examSection.addToOne(questionType, questionType_id);
		Property exam_id = examSection.addLongProperty("exam_id").notNull().getProperty();// 试卷外键
		examSection.addToOne(examination, exam_id);
		/*** 试卷大题表 ***/

		// 一个题型对应多个大题
		ToMany QuestionTypeToExamSection = questionType.addToMany(examSection, questionType_id);
		QuestionTypeToExamSection.setName("examSectionList");
		// 一张试卷对应多个大题
		ToMany ExamToExamSection = examination.addToMany(examSection, exam_id);
		ExamToExamSection.setName("examSectionList");
		
		
		/*** 试卷题目表 ***/
		Entity examQuestion = schema.addEntity("ExamQuestion");
		examQuestion.implementsSerializable();
		examQuestion.addIdProperty().autoincrement();
		examQuestion.addIntProperty("question_number").notNull();//题号
		examQuestion.addLongProperty("question_id").notNull();//题目ID
		Property examQuestion_examSection_id = examQuestion.addLongProperty("exanSection_id").notNull().getProperty();//大题ＩＤ
		examQuestion.addToOne(section, examQuestion_examSection_id);
		/*** 试卷题目表 ***/
		
		//一个答题中有多个题目
		ToMany examSectionToQuestion = examSection.addToMany(examQuestion, examQuestion_examSection_id);
		examSectionToQuestion.setName("examQuestionList");
		

		/*** 错题表 ***/
		Entity errorQuestions = schema.addEntity("ErrorQuestions");
		errorQuestions.implementsSerializable();
		errorQuestions.addIdProperty().autoincrement();
		errorQuestions.addLongProperty("question_id").notNull();// 题目ID
		errorQuestions.addDateProperty("error_time");// 出错时间
		errorQuestions.addIntProperty("error_num");// 出错次数
		Property user_id = errorQuestions.addLongProperty("user_id").notNull().getProperty();// 用户外键
		errorQuestions.addToOne(userEntity, user_id);
		Property error_questionType_id = errorQuestions.addLongProperty("questionType_id").notNull().getProperty();// 题型外键
		errorQuestions.addToOne(questionType, error_questionType_id);
		Property error_section_id = errorQuestions.addLongProperty("section_id").notNull().getProperty();//章节外键
		errorQuestions.addToOne(section, error_section_id);
		/*** 错题表 ***/

		// 一个用户有多个错题
		ToMany UserToErrorQuestions = userEntity.addToMany(errorQuestions, user_id);
		UserToErrorQuestions.setName("errorQuestionsList");
		// 一个题型有多个错题
		ToMany QuestionTypeToErrorQuestions = questionType.addToMany(errorQuestions, error_questionType_id);
		QuestionTypeToErrorQuestions.setName("errorQuestionsList");
		//一个章节下有多个错题
		ToMany SectionToError = section.addToMany(errorQuestions, error_section_id);
		SectionToError.setName("errorQuestionsList");

		/*** 收藏表 ***/
		Entity collection = schema.addEntity("Collection");
		collection.implementsSerializable();
		collection.addIdProperty().autoincrement();
		collection.addLongProperty("question_id").notNull();// 题目ID
		collection.addDateProperty("collect_time");// 收藏时间
		Property collect_user_id = collection.addLongProperty("user_id").notNull().getProperty();// 用户外键
		collection.addToOne(userEntity, collect_user_id);
		Property collection_questionType_id = collection.addLongProperty("questionType_id").notNull().getProperty();// 题型外键
		collection.addToOne(questionType, collection_questionType_id);
		Property collect_section_id = collection.addLongProperty("section_id").notNull().getProperty();//章节外键
		collection.addToOne(section, collect_section_id);
		/*** 收藏表 ***/

		// 一个用户有多个收藏题目
		ToMany UserToCollection = userEntity.addToMany(collection, collect_user_id);
		UserToCollection.setName("collectionQuestionsList");
		// 一个题型有多个收藏题目
		ToMany QuestionTypeToCollection = questionType.addToMany(collection, collection_questionType_id);
		QuestionTypeToCollection.setName("collectionQuestionsList");
		//一个章节有多个收藏题目
		ToMany SectionToCollection = section.addToMany(collection, collect_section_id);
		SectionToCollection.setName("collectionList");

		/*** 疑问表 ***/
		Entity querys = schema.addEntity("Querys");
		querys.implementsSerializable();
		querys.addIdProperty().autoincrement();
		querys.addLongProperty("question_id").notNull();// 题目ID
		querys.addDateProperty("query_time");// 提问时间
		querys.addStringProperty("query_stem");// 问题题干
		querys.addIntProperty("integral");// 奖励积分
		querys.addLongProperty("adopt_user_id");// 采用答案用户ID
		Property query_user_id = querys.addLongProperty("user_id").notNull().getProperty();// 用户外键
		querys.addToOne(userEntity, query_user_id);
		Property query_questionType_id = querys.addLongProperty("questionType_id").notNull().getProperty();// 题型外键
		querys.addToOne(questionType, query_questionType_id);
		/*** 疑问表 ***/

		// 一个用户有多个疑问
		ToMany UserToQuery = userEntity.addToMany(querys, query_user_id);
		UserToQuery.setName("queryList");
		// 一个题型有多个疑问
		ToMany QuestionTypeToQuery = questionType.addToMany(querys, query_questionType_id);
		QuestionTypeToQuery.setName("queryList");

		/*** 答疑表 ***/
		Entity answerQuery = schema.addEntity("AnswerQuery");
		answerQuery.implementsSerializable();
		answerQuery.addIdProperty().autoincrement();
		answerQuery.addStringProperty("answer_content");// 回答内容
		answerQuery.addDateProperty("answer_time");// 回答时间
		Property answerQuery_user_id = answerQuery.addLongProperty("user_id").notNull().getProperty();// 用户外键
		answerQuery.addToOne(userEntity, answerQuery_user_id);
		Property answerQuery_query_id = answerQuery.addLongProperty("query_id").notNull().getProperty();// 疑问外键
		answerQuery.addToOne(querys, answerQuery_query_id);
		/*** 答疑表 ***/

		// 一个用户有多个答疑
		ToMany UserToAnswerQuery = userEntity.addToMany(answerQuery, answerQuery_user_id);
		UserToAnswerQuery.setName("answerQueryList");
		// 一个疑问有多个答疑
		ToMany QueryToAnswer = querys.addToMany(answerQuery, answerQuery_query_id);
		QueryToAnswer.setName("answerQueryList");

		/*** 笔记 ***/
		Entity note = schema.addEntity("Note");
		note.implementsSerializable();
		note.addIdProperty().autoincrement();
		note.addLongProperty("question_id").notNull();// 题目ID
		note.addDateProperty("note_time");// 添加笔记时间
		note.addStringProperty("note_content");// 笔记内容
		Property note_user_id = note.addLongProperty("user_id").notNull().getProperty();// 用户外键
		note.addToOne(userEntity, note_user_id);
		Property note_questionType_id = note.addLongProperty("questionType_id").notNull().getProperty();// 题型外键
		note.addToOne(questionType, note_questionType_id);
		/*** 笔记 ***/

		// 一个用户有多个笔记
		ToMany UserToNote = userEntity.addToMany(note, note_user_id);
		UserToNote.setName("noteList");
		// 一个题型有多个笔记
		ToMany QuestionTypeToNote = questionType.addToMany(note, note_questionType_id);
		QuestionTypeToNote.setName("noteList");

		/*** 学习记录表 ***/
		Entity studyRecord = schema.addEntity("StudyRecord");
		studyRecord.implementsSerializable();
		studyRecord.addIdProperty().autoincrement();
		studyRecord.addLongProperty("question_id").notNull();// 题目ID
		Property studyRecord_user_id = studyRecord.addLongProperty("user_id").notNull().getProperty();// 用户外键
		studyRecord.addToOne(userEntity, studyRecord_user_id);
		Property studyRecord_questionType_id = studyRecord.addLongProperty("questionType_id").notNull().getProperty();// 题型外键
		studyRecord.addToOne(questionType, studyRecord_questionType_id);
		/*** 学习记录表 ***/

		// 一个用户有多个学习记录
		ToMany UserToRecord = userEntity.addToMany(studyRecord, studyRecord_user_id);
		UserToRecord.setName("studyRecordList");
		// 一个题型有多个学习记录
		ToMany QuestionTypeToRecord = questionType.addToMany(studyRecord, studyRecord_questionType_id);
		QuestionTypeToRecord.setName("studyRecordList");

		/*** 分组表 ***/
		Entity groups = schema.addEntity("Groups");
		groups.implementsSerializable();
		groups.addIdProperty().autoincrement();
		groups.addIntProperty("question_num");// 组中题目数量
		groups.addIntProperty("finished_num");// 已完成数量
		groups.addIntProperty("error_num");// 出错数量
		groups.addStringProperty("group_name");//组名
		Property group_section_id = groups.addLongProperty("section_id").notNull().getProperty();// 章节外键
		groups.addToOne(section, group_section_id);
		Property group_questionType_id = groups.addLongProperty("questionType_id").notNull().getProperty();// 题型外键
		groups.addToOne(questionType, group_questionType_id);
		/*** 分组表 ***/

		// 一个章节中有多个分组
		ToMany SectionToGroup = section.addToMany(groups, group_section_id);
		SectionToGroup.setName("groupList");
		// 一个题型中有多个分组
		ToMany QuestionTypeToGroup = questionType.addToMany(groups, group_questionType_id);
		QuestionTypeToGroup.setName("groupList");

		/*** 题目分组表 ***/
		Entity groupQuestion = schema.addEntity("GroupQuestion");
		groupQuestion.implementsSerializable();
		groupQuestion.addIdProperty().autoincrement();
		groupQuestion.addLongProperty("question_id").notNull();// 题目ID
		Property groupQuestion_group_id = groupQuestion.addLongProperty("group_id").notNull().getProperty();//分组外键
		groupQuestion.addToOne(groups, groupQuestion_group_id);
		/*** 题目分组表 ***/
		
		//一个分组中有多个题目
		ToMany groupToQuestion = groups.addToMany(groupQuestion, groupQuestion_group_id);
		groupToQuestion.setName("questionList");
	}

}
