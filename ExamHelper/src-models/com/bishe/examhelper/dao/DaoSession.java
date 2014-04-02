package com.bishe.examhelper.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.entities.Subject;
import com.bishe.examhelper.entities.Section;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.TrueOrFalse;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.QuestionsOfMaterial;
import com.bishe.examhelper.entities.Examination;
import com.bishe.examhelper.entities.QuestionType;
import com.bishe.examhelper.entities.ExamSection;
import com.bishe.examhelper.entities.ExamQuestion;
import com.bishe.examhelper.entities.ErrorQuestions;
import com.bishe.examhelper.entities.Collection;
import com.bishe.examhelper.entities.Querys;
import com.bishe.examhelper.entities.AnswerQuery;
import com.bishe.examhelper.entities.Note;
import com.bishe.examhelper.entities.StudyRecord;
import com.bishe.examhelper.entities.Groups;
import com.bishe.examhelper.entities.GroupQuestion;

import com.bishe.examhelper.dao.UserDao;
import com.bishe.examhelper.dao.SubjectDao;
import com.bishe.examhelper.dao.SectionDao;
import com.bishe.examhelper.dao.SingleChoiceDao;
import com.bishe.examhelper.dao.MultiChoiceDao;
import com.bishe.examhelper.dao.TrueOrFalseDao;
import com.bishe.examhelper.dao.MaterialAnalysisDao;
import com.bishe.examhelper.dao.QuestionsOfMaterialDao;
import com.bishe.examhelper.dao.ExaminationDao;
import com.bishe.examhelper.dao.QuestionTypeDao;
import com.bishe.examhelper.dao.ExamSectionDao;
import com.bishe.examhelper.dao.ExamQuestionDao;
import com.bishe.examhelper.dao.ErrorQuestionsDao;
import com.bishe.examhelper.dao.CollectionDao;
import com.bishe.examhelper.dao.QuerysDao;
import com.bishe.examhelper.dao.AnswerQueryDao;
import com.bishe.examhelper.dao.NoteDao;
import com.bishe.examhelper.dao.StudyRecordDao;
import com.bishe.examhelper.dao.GroupsDao;
import com.bishe.examhelper.dao.GroupQuestionDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig subjectDaoConfig;
    private final DaoConfig sectionDaoConfig;
    private final DaoConfig singleChoiceDaoConfig;
    private final DaoConfig multiChoiceDaoConfig;
    private final DaoConfig trueOrFalseDaoConfig;
    private final DaoConfig materialAnalysisDaoConfig;
    private final DaoConfig questionsOfMaterialDaoConfig;
    private final DaoConfig examinationDaoConfig;
    private final DaoConfig questionTypeDaoConfig;
    private final DaoConfig examSectionDaoConfig;
    private final DaoConfig examQuestionDaoConfig;
    private final DaoConfig errorQuestionsDaoConfig;
    private final DaoConfig collectionDaoConfig;
    private final DaoConfig querysDaoConfig;
    private final DaoConfig answerQueryDaoConfig;
    private final DaoConfig noteDaoConfig;
    private final DaoConfig studyRecordDaoConfig;
    private final DaoConfig groupsDaoConfig;
    private final DaoConfig groupQuestionDaoConfig;

    private final UserDao userDao;
    private final SubjectDao subjectDao;
    private final SectionDao sectionDao;
    private final SingleChoiceDao singleChoiceDao;
    private final MultiChoiceDao multiChoiceDao;
    private final TrueOrFalseDao trueOrFalseDao;
    private final MaterialAnalysisDao materialAnalysisDao;
    private final QuestionsOfMaterialDao questionsOfMaterialDao;
    private final ExaminationDao examinationDao;
    private final QuestionTypeDao questionTypeDao;
    private final ExamSectionDao examSectionDao;
    private final ExamQuestionDao examQuestionDao;
    private final ErrorQuestionsDao errorQuestionsDao;
    private final CollectionDao collectionDao;
    private final QuerysDao querysDao;
    private final AnswerQueryDao answerQueryDao;
    private final NoteDao noteDao;
    private final StudyRecordDao studyRecordDao;
    private final GroupsDao groupsDao;
    private final GroupQuestionDao groupQuestionDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        subjectDaoConfig = daoConfigMap.get(SubjectDao.class).clone();
        subjectDaoConfig.initIdentityScope(type);

        sectionDaoConfig = daoConfigMap.get(SectionDao.class).clone();
        sectionDaoConfig.initIdentityScope(type);

        singleChoiceDaoConfig = daoConfigMap.get(SingleChoiceDao.class).clone();
        singleChoiceDaoConfig.initIdentityScope(type);

        multiChoiceDaoConfig = daoConfigMap.get(MultiChoiceDao.class).clone();
        multiChoiceDaoConfig.initIdentityScope(type);

        trueOrFalseDaoConfig = daoConfigMap.get(TrueOrFalseDao.class).clone();
        trueOrFalseDaoConfig.initIdentityScope(type);

        materialAnalysisDaoConfig = daoConfigMap.get(MaterialAnalysisDao.class).clone();
        materialAnalysisDaoConfig.initIdentityScope(type);

        questionsOfMaterialDaoConfig = daoConfigMap.get(QuestionsOfMaterialDao.class).clone();
        questionsOfMaterialDaoConfig.initIdentityScope(type);

        examinationDaoConfig = daoConfigMap.get(ExaminationDao.class).clone();
        examinationDaoConfig.initIdentityScope(type);

        questionTypeDaoConfig = daoConfigMap.get(QuestionTypeDao.class).clone();
        questionTypeDaoConfig.initIdentityScope(type);

        examSectionDaoConfig = daoConfigMap.get(ExamSectionDao.class).clone();
        examSectionDaoConfig.initIdentityScope(type);

        examQuestionDaoConfig = daoConfigMap.get(ExamQuestionDao.class).clone();
        examQuestionDaoConfig.initIdentityScope(type);

        errorQuestionsDaoConfig = daoConfigMap.get(ErrorQuestionsDao.class).clone();
        errorQuestionsDaoConfig.initIdentityScope(type);

        collectionDaoConfig = daoConfigMap.get(CollectionDao.class).clone();
        collectionDaoConfig.initIdentityScope(type);

        querysDaoConfig = daoConfigMap.get(QuerysDao.class).clone();
        querysDaoConfig.initIdentityScope(type);

        answerQueryDaoConfig = daoConfigMap.get(AnswerQueryDao.class).clone();
        answerQueryDaoConfig.initIdentityScope(type);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        studyRecordDaoConfig = daoConfigMap.get(StudyRecordDao.class).clone();
        studyRecordDaoConfig.initIdentityScope(type);

        groupsDaoConfig = daoConfigMap.get(GroupsDao.class).clone();
        groupsDaoConfig.initIdentityScope(type);

        groupQuestionDaoConfig = daoConfigMap.get(GroupQuestionDao.class).clone();
        groupQuestionDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        subjectDao = new SubjectDao(subjectDaoConfig, this);
        sectionDao = new SectionDao(sectionDaoConfig, this);
        singleChoiceDao = new SingleChoiceDao(singleChoiceDaoConfig, this);
        multiChoiceDao = new MultiChoiceDao(multiChoiceDaoConfig, this);
        trueOrFalseDao = new TrueOrFalseDao(trueOrFalseDaoConfig, this);
        materialAnalysisDao = new MaterialAnalysisDao(materialAnalysisDaoConfig, this);
        questionsOfMaterialDao = new QuestionsOfMaterialDao(questionsOfMaterialDaoConfig, this);
        examinationDao = new ExaminationDao(examinationDaoConfig, this);
        questionTypeDao = new QuestionTypeDao(questionTypeDaoConfig, this);
        examSectionDao = new ExamSectionDao(examSectionDaoConfig, this);
        examQuestionDao = new ExamQuestionDao(examQuestionDaoConfig, this);
        errorQuestionsDao = new ErrorQuestionsDao(errorQuestionsDaoConfig, this);
        collectionDao = new CollectionDao(collectionDaoConfig, this);
        querysDao = new QuerysDao(querysDaoConfig, this);
        answerQueryDao = new AnswerQueryDao(answerQueryDaoConfig, this);
        noteDao = new NoteDao(noteDaoConfig, this);
        studyRecordDao = new StudyRecordDao(studyRecordDaoConfig, this);
        groupsDao = new GroupsDao(groupsDaoConfig, this);
        groupQuestionDao = new GroupQuestionDao(groupQuestionDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(Subject.class, subjectDao);
        registerDao(Section.class, sectionDao);
        registerDao(SingleChoice.class, singleChoiceDao);
        registerDao(MultiChoice.class, multiChoiceDao);
        registerDao(TrueOrFalse.class, trueOrFalseDao);
        registerDao(MaterialAnalysis.class, materialAnalysisDao);
        registerDao(QuestionsOfMaterial.class, questionsOfMaterialDao);
        registerDao(Examination.class, examinationDao);
        registerDao(QuestionType.class, questionTypeDao);
        registerDao(ExamSection.class, examSectionDao);
        registerDao(ExamQuestion.class, examQuestionDao);
        registerDao(ErrorQuestions.class, errorQuestionsDao);
        registerDao(Collection.class, collectionDao);
        registerDao(Querys.class, querysDao);
        registerDao(AnswerQuery.class, answerQueryDao);
        registerDao(Note.class, noteDao);
        registerDao(StudyRecord.class, studyRecordDao);
        registerDao(Groups.class, groupsDao);
        registerDao(GroupQuestion.class, groupQuestionDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        subjectDaoConfig.getIdentityScope().clear();
        sectionDaoConfig.getIdentityScope().clear();
        singleChoiceDaoConfig.getIdentityScope().clear();
        multiChoiceDaoConfig.getIdentityScope().clear();
        trueOrFalseDaoConfig.getIdentityScope().clear();
        materialAnalysisDaoConfig.getIdentityScope().clear();
        questionsOfMaterialDaoConfig.getIdentityScope().clear();
        examinationDaoConfig.getIdentityScope().clear();
        questionTypeDaoConfig.getIdentityScope().clear();
        examSectionDaoConfig.getIdentityScope().clear();
        examQuestionDaoConfig.getIdentityScope().clear();
        errorQuestionsDaoConfig.getIdentityScope().clear();
        collectionDaoConfig.getIdentityScope().clear();
        querysDaoConfig.getIdentityScope().clear();
        answerQueryDaoConfig.getIdentityScope().clear();
        noteDaoConfig.getIdentityScope().clear();
        studyRecordDaoConfig.getIdentityScope().clear();
        groupsDaoConfig.getIdentityScope().clear();
        groupQuestionDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public SubjectDao getSubjectDao() {
        return subjectDao;
    }

    public SectionDao getSectionDao() {
        return sectionDao;
    }

    public SingleChoiceDao getSingleChoiceDao() {
        return singleChoiceDao;
    }

    public MultiChoiceDao getMultiChoiceDao() {
        return multiChoiceDao;
    }

    public TrueOrFalseDao getTrueOrFalseDao() {
        return trueOrFalseDao;
    }

    public MaterialAnalysisDao getMaterialAnalysisDao() {
        return materialAnalysisDao;
    }

    public QuestionsOfMaterialDao getQuestionsOfMaterialDao() {
        return questionsOfMaterialDao;
    }

    public ExaminationDao getExaminationDao() {
        return examinationDao;
    }

    public QuestionTypeDao getQuestionTypeDao() {
        return questionTypeDao;
    }

    public ExamSectionDao getExamSectionDao() {
        return examSectionDao;
    }

    public ExamQuestionDao getExamQuestionDao() {
        return examQuestionDao;
    }

    public ErrorQuestionsDao getErrorQuestionsDao() {
        return errorQuestionsDao;
    }

    public CollectionDao getCollectionDao() {
        return collectionDao;
    }

    public QuerysDao getQuerysDao() {
        return querysDao;
    }

    public AnswerQueryDao getAnswerQueryDao() {
        return answerQueryDao;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public StudyRecordDao getStudyRecordDao() {
        return studyRecordDao;
    }

    public GroupsDao getGroupsDao() {
        return groupsDao;
    }

    public GroupQuestionDao getGroupQuestionDao() {
        return groupQuestionDao;
    }

}