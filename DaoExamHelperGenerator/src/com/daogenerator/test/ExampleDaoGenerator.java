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

		/*** �û��� ***/
		Entity userEntity = schema.addEntity("User");
		userEntity.implementsSerializable();
		userEntity.addIdProperty().autoincrement();
		userEntity.addStringProperty("mail").notNull();// ����
		userEntity.addStringProperty("password").notNull(); // ����
		userEntity.addStringProperty("nickname");// �ǳ�
		userEntity.addStringProperty("realname");// ��ʵ����
		userEntity.addIntProperty("age");// ����
		userEntity.addStringProperty("phone");// �ֻ�����
		userEntity.addStringProperty("gender");// �Ա�
		userEntity.addStringProperty("user_state");// ����ǩ��
		userEntity.addStringProperty("profession");// ְҵ
		userEntity.addStringProperty("area");// ����
		userEntity.addIntProperty("integral");// ����
		userEntity.addByteArrayProperty("avatar");// �û�ͷ��
		userEntity.addByteArrayProperty("small_avatar");// �û�Сͷ��
		userEntity.addBooleanProperty("current");//�Ƿ�Ϊ��ǰ�û�
		/*** �û��� ***/

		/*** ��Ŀ�� ***/
		Entity subject = schema.addEntity("Subject");
		subject.implementsSerializable();
		subject.addIdProperty().autoincrement();
		subject.addStringProperty("subject_name").notNull();// ��Ŀ����
		/*** ��Ŀ�� ***/

		/*** �½ڱ� ***/
		Entity section = schema.addEntity("Section");
		section.implementsSerializable();
		section.addIdProperty().autoincrement();
		section.addStringProperty("section_name").notNull();// �½�����
		Property subject_id = section.addLongProperty("subject_id").notNull().getProperty();// ��Ŀ���
		section.addToOne(subject, subject_id);
		/*** �½ڱ� ***/

		// ��Ŀ���ж���½�
		ToMany subjectToSection = subject.addToMany(section, subject_id);
		subjectToSection.setName("sectionList");

		/*** ��ѡ��� ***/
		Entity singleChoice = schema.addEntity("SingleChoice");
		singleChoice.implementsSerializable();
		singleChoice.setSuperclass("Question");
		singleChoice.addIdProperty().autoincrement();
		singleChoice.addStringProperty("question_stem").notNull();// ���
		singleChoice.addStringProperty("optionA");// ѡ��A
		singleChoice.addStringProperty("optionB");// ѡ��B
		singleChoice.addStringProperty("optionC");// ѡ��C
		singleChoice.addStringProperty("optionD");// ѡ��D
		singleChoice.addStringProperty("optionE");// ѡ��E
		singleChoice.addStringProperty("answer");// ��
		singleChoice.addStringProperty("analysis");// �𰸽���
		singleChoice.addStringProperty("remark");// ��ע
		singleChoice.addBooleanProperty("flag");// ��־λ
		Property singleChoice_section_id = singleChoice.addLongProperty("section_id").notNull().getProperty();// �½����
		singleChoice.addToOne(section, singleChoice_section_id);
		/*** ��ѡ��� ***/

		// �½����ж����ѡ��
		ToMany sectionToSingleChoice = section.addToMany(singleChoice, singleChoice_section_id);
		sectionToSingleChoice.setName("singleChoiceList");

		/*** ��ѡ��� ***/
		Entity MultiChoice = schema.addEntity("MultiChoice");
		MultiChoice.implementsSerializable();
		MultiChoice.setSuperclass("Question");
		MultiChoice.addIdProperty().autoincrement();
		MultiChoice.addStringProperty("question_stem").notNull();// ���
		MultiChoice.addStringProperty("optionA");// ѡ��A
		MultiChoice.addStringProperty("optionB");// ѡ��B
		MultiChoice.addStringProperty("optionC");// ѡ��C
		MultiChoice.addStringProperty("optionD");// ѡ��D
		MultiChoice.addStringProperty("optionE");// ѡ��E
		MultiChoice.addStringProperty("optionF");// ѡ��F
		MultiChoice.addBooleanProperty("answerA");// ѡ��A�Ƿ�Ϊ��ѡ��
		MultiChoice.addBooleanProperty("answerB");// ѡ��B�Ƿ�Ϊ��ѡ��
		MultiChoice.addBooleanProperty("answerC");// ѡ��C�Ƿ�Ϊ��ѡ��
		MultiChoice.addBooleanProperty("answerD");// ѡ��D�Ƿ�Ϊ��ѡ��
		MultiChoice.addBooleanProperty("answerE");// ѡ��E�Ƿ�Ϊ��ѡ��
		MultiChoice.addBooleanProperty("answerF");// ѡ��F�Ƿ�Ϊ��ѡ��
		MultiChoice.addStringProperty("analysis");// �𰸽���
		MultiChoice.addStringProperty("remark");// ��ע
		MultiChoice.addBooleanProperty("flag");// ��־λ
		Property multiChoice_section_id = MultiChoice.addLongProperty("section_id").notNull().getProperty();// �½����
		MultiChoice.addToOne(section, multiChoice_section_id);
		/*** ��ѡ��� ***/

		// �½����ж����ѡ��
		ToMany sectionToMultiChoice = section.addToMany(MultiChoice, multiChoice_section_id);
		sectionToMultiChoice.setName("multiChoiceList");

		/*** �ж���� ***/
		Entity trueOrFalse = schema.addEntity("TrueOrFalse");
		trueOrFalse.implementsSerializable();
		trueOrFalse.setSuperclass("Question");
		trueOrFalse.addIdProperty().autoincrement();
		trueOrFalse.addStringProperty("question_stem").notNull();// ���
		trueOrFalse.addBooleanProperty("answer");// ��
		trueOrFalse.addStringProperty("analysis");// �𰸽���
		trueOrFalse.addStringProperty("remark");// ��ע
		trueOrFalse.addBooleanProperty("flag");// ��־λ
		Property trueOrFalse_section_id = trueOrFalse.addLongProperty("section_id").notNull().getProperty();// �½����
		trueOrFalse.addToOne(section, trueOrFalse_section_id);
		/*** �ж���� ***/

		// �½����ж���ж���
		ToMany sectionToTrueOrFalse = section.addToMany(trueOrFalse, trueOrFalse_section_id);
		sectionToTrueOrFalse.setName("trueOrFalseList");

		/*** ���Ϸ������ ***/
		Entity MaterialAnalysis = schema.addEntity("MaterialAnalysis");
		MaterialAnalysis.implementsSerializable();
		MaterialAnalysis.setSuperclass("Question");
		MaterialAnalysis.addIdProperty().autoincrement();
		MaterialAnalysis.addStringProperty("material").notNull();// ����
		MaterialAnalysis.addByteArrayProperty("material_image");// ����ͼƬ
		MaterialAnalysis.addStringProperty("remark");// ��ע
		MaterialAnalysis.addBooleanProperty("flag");// ��־λ
		Property MaterialAnalysis_section_id = MaterialAnalysis.addLongProperty("section_id").notNull().getProperty();// �½����
		MaterialAnalysis.addToOne(section, MaterialAnalysis_section_id);
		/*** ���Ϸ������ ***/

		// �½����ж�����Ϸ�����
		ToMany sectionToMaterialAnalysis = section.addToMany(MaterialAnalysis, MaterialAnalysis_section_id);
		sectionToMaterialAnalysis.setName("MaterialAnalysisList");

		/*** ���Ϸ���������� ***/
		Entity questionsOfMaterial = schema.addEntity("QuestionsOfMaterial");
		questionsOfMaterial.implementsSerializable();
		questionsOfMaterial.addIdProperty().autoincrement();
		questionsOfMaterial.addIntProperty("qusetion_number");// ���
		questionsOfMaterial.addStringProperty("question_stem");// ���
		questionsOfMaterial.addStringProperty("answer");// ��
		questionsOfMaterial.addStringProperty("analysis");// �𰸽���
		questionsOfMaterial.addIntProperty("score");// ÿС���ֵ
		Property Questions_material_id = questionsOfMaterial.addLongProperty("material_id").notNull().getProperty();// �������
		questionsOfMaterial.addToOne(MaterialAnalysis, Questions_material_id);
		/*** ���Ϸ���������� ***/

		// һ�������ж��С��
		ToMany materialToQuestions = MaterialAnalysis.addToMany(questionsOfMaterial, Questions_material_id);
		materialToQuestions.setName("questionsOfMaterialList");

		/*** �Ծ�� ***/
		Entity examination = schema.addEntity("Examination");
		examination.implementsSerializable();
		examination.addIdProperty().autoincrement();
		examination.addStringProperty("exam_type");// �Ծ�����
		examination.addStringProperty("exam_name");// �Ծ���Ŀ
		examination.addStringProperty("exam_request");// �Ծ�Ҫ��
		examination.addIntProperty("exam_time");// ����ʱ��
		Property exam_subject_id = examination.addLongProperty("subject_id").notNull().getProperty();// ��Ŀ���
		examination.addToOne(subject, exam_subject_id);
		/*** �Ծ�� ***/

		// һ����Ŀ�ж����Ծ�
		ToMany subjectToExam = subject.addToMany(examination, exam_subject_id);
		subjectToExam.setName("examinationList");

		/*** ���ͱ� ***/
		Entity questionType = schema.addEntity("QuestionType");
		questionType.implementsSerializable();
		questionType.addIdProperty().autoincrement();
		questionType.addStringProperty("type_name").notNull();// ��������
		/*** ���ͱ� ***/

		/*** �Ծ����� ***/
		Entity examSection = schema.addEntity("ExamSection");
		examSection.implementsSerializable();
		examSection.addIdProperty().autoincrement();
		examSection.addStringProperty("request");// ����Ҫ��
		examSection.addIntProperty("question_num");// ��Ŀ����
		examSection.addIntProperty("question_score");// ÿС���ֵ
		Property questionType_id = examSection.addLongProperty("questionType_id").notNull().getProperty();// �������
		examSection.addToOne(questionType, questionType_id);
		Property exam_id = examSection.addLongProperty("exam_id").notNull().getProperty();// �Ծ����
		examSection.addToOne(examination, exam_id);
		/*** �Ծ����� ***/

		// һ�����Ͷ�Ӧ�������
		ToMany QuestionTypeToExamSection = questionType.addToMany(examSection, questionType_id);
		QuestionTypeToExamSection.setName("examSectionList");
		// һ���Ծ��Ӧ�������
		ToMany ExamToExamSection = examination.addToMany(examSection, exam_id);
		ExamToExamSection.setName("examSectionList");
		
		
		/*** �Ծ���Ŀ�� ***/
		Entity examQuestion = schema.addEntity("ExamQuestion");
		examQuestion.implementsSerializable();
		examQuestion.addIdProperty().autoincrement();
		examQuestion.addIntProperty("question_number").notNull();//���
		examQuestion.addLongProperty("question_id").notNull();//��ĿID
		Property examQuestion_examSection_id = examQuestion.addLongProperty("exanSection_id").notNull().getProperty();//����ɣ�
		examQuestion.addToOne(section, examQuestion_examSection_id);
		/*** �Ծ���Ŀ�� ***/
		
		//һ���������ж����Ŀ
		ToMany examSectionToQuestion = examSection.addToMany(examQuestion, examQuestion_examSection_id);
		examSectionToQuestion.setName("examQuestionList");
		

		/*** ����� ***/
		Entity errorQuestions = schema.addEntity("ErrorQuestions");
		errorQuestions.implementsSerializable();
		errorQuestions.addIdProperty().autoincrement();
		errorQuestions.addLongProperty("question_id").notNull();// ��ĿID
		errorQuestions.addDateProperty("error_time");// ����ʱ��
		errorQuestions.addIntProperty("error_num");// �������
		Property user_id = errorQuestions.addLongProperty("user_id").notNull().getProperty();// �û����
		errorQuestions.addToOne(userEntity, user_id);
		Property error_questionType_id = errorQuestions.addLongProperty("questionType_id").notNull().getProperty();// �������
		errorQuestions.addToOne(questionType, error_questionType_id);
		Property error_section_id = errorQuestions.addLongProperty("section_id").notNull().getProperty();//�½����
		errorQuestions.addToOne(section, error_section_id);
		/*** ����� ***/

		// һ���û��ж������
		ToMany UserToErrorQuestions = userEntity.addToMany(errorQuestions, user_id);
		UserToErrorQuestions.setName("errorQuestionsList");
		// һ�������ж������
		ToMany QuestionTypeToErrorQuestions = questionType.addToMany(errorQuestions, error_questionType_id);
		QuestionTypeToErrorQuestions.setName("errorQuestionsList");
		//һ���½����ж������
		ToMany SectionToError = section.addToMany(errorQuestions, error_section_id);
		SectionToError.setName("errorQuestionsList");

		/*** �ղر� ***/
		Entity collection = schema.addEntity("Collection");
		collection.implementsSerializable();
		collection.addIdProperty().autoincrement();
		collection.addLongProperty("question_id").notNull();// ��ĿID
		collection.addDateProperty("collect_time");// �ղ�ʱ��
		Property collect_user_id = collection.addLongProperty("user_id").notNull().getProperty();// �û����
		collection.addToOne(userEntity, collect_user_id);
		Property collection_questionType_id = collection.addLongProperty("questionType_id").notNull().getProperty();// �������
		collection.addToOne(questionType, collection_questionType_id);
		Property collect_section_id = collection.addLongProperty("section_id").notNull().getProperty();//�½����
		collection.addToOne(section, collect_section_id);
		/*** �ղر� ***/

		// һ���û��ж���ղ���Ŀ
		ToMany UserToCollection = userEntity.addToMany(collection, collect_user_id);
		UserToCollection.setName("collectionQuestionsList");
		// һ�������ж���ղ���Ŀ
		ToMany QuestionTypeToCollection = questionType.addToMany(collection, collection_questionType_id);
		QuestionTypeToCollection.setName("collectionQuestionsList");
		//һ���½��ж���ղ���Ŀ
		ToMany SectionToCollection = section.addToMany(collection, collect_section_id);
		SectionToCollection.setName("collectionList");

		/*** ���ʱ� ***/
		Entity querys = schema.addEntity("Querys");
		querys.implementsSerializable();
		querys.addIdProperty().autoincrement();
		querys.addLongProperty("question_id").notNull();// ��ĿID
		querys.addDateProperty("query_time");// ����ʱ��
		querys.addStringProperty("query_stem");// �������
		querys.addIntProperty("integral");// ��������
		querys.addLongProperty("adopt_user_id");// ���ô��û�ID
		Property query_user_id = querys.addLongProperty("user_id").notNull().getProperty();// �û����
		querys.addToOne(userEntity, query_user_id);
		Property query_questionType_id = querys.addLongProperty("questionType_id").notNull().getProperty();// �������
		querys.addToOne(questionType, query_questionType_id);
		/*** ���ʱ� ***/

		// һ���û��ж������
		ToMany UserToQuery = userEntity.addToMany(querys, query_user_id);
		UserToQuery.setName("queryList");
		// һ�������ж������
		ToMany QuestionTypeToQuery = questionType.addToMany(querys, query_questionType_id);
		QuestionTypeToQuery.setName("queryList");

		/*** ���ɱ� ***/
		Entity answerQuery = schema.addEntity("AnswerQuery");
		answerQuery.implementsSerializable();
		answerQuery.addIdProperty().autoincrement();
		answerQuery.addStringProperty("answer_content");// �ش�����
		answerQuery.addDateProperty("answer_time");// �ش�ʱ��
		Property answerQuery_user_id = answerQuery.addLongProperty("user_id").notNull().getProperty();// �û����
		answerQuery.addToOne(userEntity, answerQuery_user_id);
		Property answerQuery_query_id = answerQuery.addLongProperty("query_id").notNull().getProperty();// �������
		answerQuery.addToOne(querys, answerQuery_query_id);
		/*** ���ɱ� ***/

		// һ���û��ж������
		ToMany UserToAnswerQuery = userEntity.addToMany(answerQuery, answerQuery_user_id);
		UserToAnswerQuery.setName("answerQueryList");
		// һ�������ж������
		ToMany QueryToAnswer = querys.addToMany(answerQuery, answerQuery_query_id);
		QueryToAnswer.setName("answerQueryList");

		/*** �ʼ� ***/
		Entity note = schema.addEntity("Note");
		note.implementsSerializable();
		note.addIdProperty().autoincrement();
		note.addLongProperty("question_id").notNull();// ��ĿID
		note.addDateProperty("note_time");// ��ӱʼ�ʱ��
		note.addStringProperty("note_content");// �ʼ�����
		Property note_user_id = note.addLongProperty("user_id").notNull().getProperty();// �û����
		note.addToOne(userEntity, note_user_id);
		Property note_questionType_id = note.addLongProperty("questionType_id").notNull().getProperty();// �������
		note.addToOne(questionType, note_questionType_id);
		/*** �ʼ� ***/

		// һ���û��ж���ʼ�
		ToMany UserToNote = userEntity.addToMany(note, note_user_id);
		UserToNote.setName("noteList");
		// һ�������ж���ʼ�
		ToMany QuestionTypeToNote = questionType.addToMany(note, note_questionType_id);
		QuestionTypeToNote.setName("noteList");

		/*** ѧϰ��¼�� ***/
		Entity studyRecord = schema.addEntity("StudyRecord");
		studyRecord.implementsSerializable();
		studyRecord.addIdProperty().autoincrement();
		studyRecord.addLongProperty("question_id").notNull();// ��ĿID
		Property studyRecord_user_id = studyRecord.addLongProperty("user_id").notNull().getProperty();// �û����
		studyRecord.addToOne(userEntity, studyRecord_user_id);
		Property studyRecord_questionType_id = studyRecord.addLongProperty("questionType_id").notNull().getProperty();// �������
		studyRecord.addToOne(questionType, studyRecord_questionType_id);
		/*** ѧϰ��¼�� ***/

		// һ���û��ж��ѧϰ��¼
		ToMany UserToRecord = userEntity.addToMany(studyRecord, studyRecord_user_id);
		UserToRecord.setName("studyRecordList");
		// һ�������ж��ѧϰ��¼
		ToMany QuestionTypeToRecord = questionType.addToMany(studyRecord, studyRecord_questionType_id);
		QuestionTypeToRecord.setName("studyRecordList");

		/*** ����� ***/
		Entity groups = schema.addEntity("Groups");
		groups.implementsSerializable();
		groups.addIdProperty().autoincrement();
		groups.addIntProperty("question_num");// ������Ŀ����
		groups.addIntProperty("finished_num");// ���������
		groups.addIntProperty("error_num");// ��������
		groups.addStringProperty("group_name");//����
		Property group_section_id = groups.addLongProperty("section_id").notNull().getProperty();// �½����
		groups.addToOne(section, group_section_id);
		Property group_questionType_id = groups.addLongProperty("questionType_id").notNull().getProperty();// �������
		groups.addToOne(questionType, group_questionType_id);
		/*** ����� ***/

		// һ���½����ж������
		ToMany SectionToGroup = section.addToMany(groups, group_section_id);
		SectionToGroup.setName("groupList");
		// һ���������ж������
		ToMany QuestionTypeToGroup = questionType.addToMany(groups, group_questionType_id);
		QuestionTypeToGroup.setName("groupList");

		/*** ��Ŀ����� ***/
		Entity groupQuestion = schema.addEntity("GroupQuestion");
		groupQuestion.implementsSerializable();
		groupQuestion.addIdProperty().autoincrement();
		groupQuestion.addLongProperty("question_id").notNull();// ��ĿID
		Property groupQuestion_group_id = groupQuestion.addLongProperty("group_id").notNull().getProperty();//�������
		groupQuestion.addToOne(groups, groupQuestion_group_id);
		/*** ��Ŀ����� ***/
		
		//һ���������ж����Ŀ
		ToMany groupToQuestion = groups.addToMany(groupQuestion, groupQuestion_group_id);
		groupToQuestion.setName("questionList");
	}

}
