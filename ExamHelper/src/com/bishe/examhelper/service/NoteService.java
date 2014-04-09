package com.bishe.examhelper.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.NoteDao;
import com.bishe.examhelper.dao.NoteDao.Properties;
import com.bishe.examhelper.dao.QuestionTypeDao;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.Note;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.utils.DateTimeTools;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;

public class NoteService {
	private static final String TAG = NoteService.class.getSimpleName();
	private static NoteService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public NoteDao noteDao;
	private QuestionTypeDao mQuestionTypeDao;

	/**
	 * �õ�ʵ��
	 * @param context
	 * @return
	 */
	public static NoteService getInstance(Context context) {
		if (instance == null) {
			instance = new NoteService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.noteDao = instance.mDaoSession.getNoteDao();
			instance.mQuestionTypeDao = instance.mDaoSession.getQuestionTypeDao();
		}
		return instance;
	}

	/**
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public Note loadNote(Long id) {
		return noteDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<Note> loadAllNotes() {
		return noteDao.loadAll();
	}

	/**
	 *����ʱ���Ⱥ󷵻��б�
	 * @return
	 */
	public List<Note> loadAllNotesByTime() {
		return noteDao.queryBuilder().orderDesc(Properties.Note_time).list();
	}

	/**
	 * ����IDɾ��ĳ����¼
	 */
	public void deleteNoteById(Long id) {
		noteDao.deleteByKey(id);
	}

	/**
	 * ɾ��ĳ����¼
	 */
	public void deleteNote(Note note) {
		noteDao.delete(note);
	}

	/**
	 * ����ʵ��
	 * @param note
	 */
	public void updateNote(Note note) {
		noteDao.update(note);
	}

	/**
	 * ���ݵ�ѡ�ⷵ�رʼ�ʵ��
	 * @param singleChoice
	 * @return
	 */
	public Note loadNote(SingleChoice singleChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// ���Ҷ�Ӧ���͡���Ŀ�ıʼ�
		Note note = noteDao.queryBuilder()
				.where(Properties.Question_id.eq(singleChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();

		return note;
	}

	/**
	 * ���ݶ�ѡ�ⷵ�رʼ�ʵ��
	 * @param singleChoice
	 * @return
	 */
	public Note loadNote(MultiChoice multiChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// ���Ҷ�Ӧ���͡���Ŀ�ıʼ�
		Note note = noteDao.queryBuilder()
				.where(Properties.Question_id.eq(multiChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();

		return note;
	}

	/**
	 * ���ݲ����ⷵ�رʼ�ʵ��
	 * @param singleChoice
	 * @return
	 */
	public Note loadNote(MaterialAnalysis materialAnalysis) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// ���Ҷ�Ӧ���͡���Ŀ�ıʼ�
		Note note = noteDao
				.queryBuilder()
				.where(Properties.Question_id.eq(materialAnalysis.getId()),
						Properties.QuestionType_id.eq(questionTypeId)).unique();

		return note;
	}

	/**
	 * ���رʼ�ʵ��
	 * @param singleChoice
	 * @return
	 */
	public Note loadNote(Question question) {
		Long questionTypeId = null;
		Long questionId = null;
		Note note = null;

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

		note = noteDao.queryBuilder()
				.where(Properties.QuestionType_id.eq(questionTypeId), Properties.Question_id.eq(questionId)).unique();

		return note;
	}

	/**
	 * ����ʼ�
	 * @param singleChoice
	 * @return
	 */
	public void insertNote(Question question, String noteContent) {
		Long questionTypeId = null;
		Long questionId = null;
		Note note = null;

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

		if (questionTypeId != null && questionId != null) {
			note = new Note(null, questionId, DateTimeTools.getCurrentDate(), noteContent, UserService.getInstance(
					appContext).getCurrentUserID(), questionTypeId);
		}
		noteDao.insert(note);
	}

	/**
	 * ����ʼǵ�����
	 * @param singleChoice
	 * @return
	 */
	public void addNoteToNet(Question question, String noteContent) {
		Long questionTypeId = null;
		Long questionId = null;
		com.jsonobjects.JNote net = null;

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

		if (questionTypeId != null && questionId != null) {
			net = new com.jsonobjects.JNote(null, questionId, DateTimeTools.getCurrentDate(), noteContent, UserService
					.getInstance(appContext).getCurrentUserID(), questionTypeId);
		}

		String jsonString = FastJsonTool.createJsonString(net);
		String URL = "NoteServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("note", jsonString);
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("����", "��ӱʼ�");
		}

	}
}
