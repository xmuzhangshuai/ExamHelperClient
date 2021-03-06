package com.bishe.examhelper.service;

import java.util.ArrayList;
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
import com.jsonobjects.JNote;

public class NoteService {
	private static final String TAG = NoteService.class.getSimpleName();
	private static NoteService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public NoteDao noteDao;
	private QuestionTypeDao mQuestionTypeDao;

	/**
	 * 得到实例
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
	 * 根据ID返回实体
	 * @param id
	 * @return
	 */
	public Note loadNote(Long id) {
		return noteDao.load(id);
	}

	/**
	 * 返回实体列表
	 * @return
	 */
	public List<Note> loadAllNotes() {
		return noteDao.loadAll();
	}

	/**
	 *按照时间先后返回列表
	 * @return
	 */
	public List<Note> loadAllNotesByTime() {
		return noteDao.queryBuilder()
				.where(Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID()))
				.orderDesc(Properties.Note_time).list();
	}

	/**
	 * 根据ID删除某条记录
	 */
	public void deleteNoteById(Long id) {
		noteDao.deleteByKey(id);
	}

	/**
	 * 删除某条记录
	 */
	public void deleteNote(Note note) {
		noteDao.delete(note);
	}

	/**
	 * 更新实体
	 * @param note
	 */
	public void updateNote(Note note) {
		noteDao.update(note);
	}

	/**
	 * 根据单选题返回笔记实体
	 * @param singleChoice
	 * @return
	 */
	public Note loadNote(SingleChoice singleChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// 查找对应题型、题目的笔记
		Note note = noteDao.queryBuilder()
				.where(Properties.Question_id.eq(singleChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();

		return note;
	}

	/**
	 * 根据多选题返回笔记实体
	 * @param singleChoice
	 * @return
	 */
	public Note loadNote(MultiChoice multiChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// 查找对应题型、题目的笔记
		Note note = noteDao.queryBuilder()
				.where(Properties.Question_id.eq(multiChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();

		return note;
	}

	/**
	 * 根据材料题返回笔记实体
	 * @param singleChoice
	 * @return
	 */
	public Note loadNote(MaterialAnalysis materialAnalysis) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// 查找对应题型、题目的笔记
		Note note = noteDao
				.queryBuilder()
				.where(Properties.Question_id.eq(materialAnalysis.getId()),
						Properties.QuestionType_id.eq(questionTypeId)).unique();

		return note;
	}

	/**
	 * 返回笔记实体
	 * @param singleChoice
	 * @return
	 */
	public Note loadNote(Question question) {
		Long questionTypeId = null;
		Long questionId = null;
		Note note = null;

		if (question.getQuestion_type().equals(DefaultValues.SINGLE_CHOICE)) {// 如果是单选题
			SingleChoice singleChoice = (SingleChoice) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
							.eq(DefaultValues.SINGLE_CHOICE)).unique().getId();

			questionId = singleChoice.getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MULTI_CHOICE)) {// 如果是多选题
			MultiChoice multiChoice = (MultiChoice) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
					.unique().getId();
			questionId = multiChoice.getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MATERIAL_ANALYSIS)) {// 如果是材料题
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
							.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();
			questionId = materialAnalysis.getId();
		}

		note = noteDao
				.queryBuilder()
				.where(Properties.QuestionType_id.eq(questionTypeId), Properties.Question_id.eq(questionId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();

		return note;
	}

	/**
	 * 插入笔记
	 * @param singleChoice
	 * @return
	 */
	public void insertNote(Question question, String noteContent) {
		Long questionTypeId = null;
		Long questionId = null;
		Note note = null;

		if (question.getQuestion_type().equals(DefaultValues.SINGLE_CHOICE)) {// 如果是单选题
			SingleChoice singleChoice = (SingleChoice) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
							.eq(DefaultValues.SINGLE_CHOICE)).unique().getId();

			questionId = singleChoice.getId();
		} else if (question.getQuestion_type().equals(DefaultValues.MULTI_CHOICE)) {// 如果是多选题
			MultiChoice multiChoice = (MultiChoice) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
					.unique().getId();
			questionId = multiChoice.getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MATERIAL_ANALYSIS)) {// 如果是材料题
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) question;
			// 查找题型ID
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
	 * 插入笔记到网络
	 * @param singleChoice
	 * @return
	 */
	public void addNoteToNet(Question question, String noteContent) {
		Long questionTypeId = null;
		Long questionId = null;
		com.jsonobjects.JNote net = null;

		if (question.getQuestion_type().equals(DefaultValues.SINGLE_CHOICE)) {// 如果是单选题
			SingleChoice singleChoice = (SingleChoice) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
							.eq(DefaultValues.SINGLE_CHOICE)).unique().getId();

			questionId = singleChoice.getId();
		} else if (question.getQuestion_type().equals(DefaultValues.MULTI_CHOICE)) {// 如果是多选题
			MultiChoice multiChoice = (MultiChoice) question;
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
					.unique().getId();
			questionId = multiChoice.getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MATERIAL_ANALYSIS)) {// 如果是材料题
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) question;
			// 查找题型ID
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
		map.put("type", "add");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "添加笔记");
		}

	}

	/**
	 * 把变更更新到网络服务器
	 * @param note
	 */
	public void updateNoteToNet(Note note) {
		JNote jNote = new JNote(null, note.getQuestion_id(), note.getNote_time(), note.getNote_content(),
				note.getUser_id(), note.getQuestionType_id());

		String jsonString = FastJsonTool.createJsonString(jNote);
		String URL = "NoteServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("note", jsonString);
		map.put("type", "edit");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "添加笔记");
		}
	}

	/**
	 * 从服务器删除Note
	 * @param note
	 */
	public void delNoteFromNet(Note note) {
		JNote jNote = new JNote(null, note.getQuestion_id(), note.getNote_time(), note.getNote_content(),
				note.getUser_id(), note.getQuestionType_id());

		String jsonString = FastJsonTool.createJsonString(jNote);
		String URL = "NoteServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("note", jsonString);
		map.put("type", "delete");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "添加笔记");
		}
	}

	/**
	 * 根据题目获取题型ID
	 * @param question
	 * @return
	 */
	public Long getQuestionTypeID(Question question) {
		Long questionTypeId = null;

		if (question.getQuestion_type().equals(DefaultValues.SINGLE_CHOICE)) {// 如果是单选题
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
							.eq(DefaultValues.SINGLE_CHOICE)).unique().getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MULTI_CHOICE)) {// 如果是多选题
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
					.unique().getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MATERIAL_ANALYSIS)) {// 如果是材料题
			// 查找题型ID
			questionTypeId = mQuestionTypeDao
					.queryBuilder()
					.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
							.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();
		}

		return questionTypeId;

	}

	/**
	 * 根据题目获取题目ID
	 * @param question
	 * @return
	 */
	public Long getQuestionID(Question question) {
		Long questionId = null;

		if (question.getQuestion_type().equals(DefaultValues.SINGLE_CHOICE)) {// 如果是单选题
			SingleChoice singleChoice = (SingleChoice) question;

			questionId = singleChoice.getId();
		} else if (question.getQuestion_type().equals(DefaultValues.MULTI_CHOICE)) {// 如果是多选题
			MultiChoice multiChoice = (MultiChoice) question;
			questionId = multiChoice.getId();

		} else if (question.getQuestion_type().equals(DefaultValues.MATERIAL_ANALYSIS)) {// 如果是材料题
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) question;
			questionId = materialAnalysis.getId();
		}
		return questionId;

	}

	/**
	 * 从服务器获取某一题目的笔记列表
	 * @return
	 */
	public List<JNote> getNoteListFormNet(Question question) {
		List<JNote> jNotes = new ArrayList<JNote>();
		String url = "NoteServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "getNoteList");
		map.put("questionTypeId", String.valueOf(getQuestionTypeID(question)));
		map.put("questionId", String.valueOf(getQuestionID(question)));

		try {
			String jsonString = HttpUtil.postRequest(url, map);
			jNotes = FastJsonTool.getObjectList(jsonString, JNote.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jNotes;
	}

	/**
	 * 获取该用户的笔记列表
	 */
	public void getNoteListFromNetByUser() {
		UserService userService = UserService.getInstance(appContext);
		if (userService.getCurrentUserID() > 1) {
			List<JNote> jNotes = new ArrayList<JNote>();
			String url = "NoteServlet";
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "getNoteListByUser");
			map.put("userId", String.valueOf(userService.getCurrentUserID()));
			try {
				String jsonString = HttpUtil.postRequest(url, map);
				jNotes = FastJsonTool.getObjectList(jsonString, JNote.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<Note> notes = loadAllNotesByTime();
			if (notes != null) {
				for (Note note : notes) {
					deleteNote(note);
				}
			}

			if (jNotes != null) {
				for (JNote jNote : jNotes) {
					Note note = new Note(null, jNote.getQuestion_id(), jNote.getNote_time(),
							jNote.getNote_content(), jNote.getUser_id(), jNote.getQuestionType_id());
					this.noteDao.insert(note);
				}
			}
		}

	}

	/**
	 * 往服务器传入笔记列表
	 * @param notes
	 */
	public void addNoteListToNet(List<Note> notes) {
		List<JNote> jNotes = new ArrayList<JNote>();
		for (Note note : notes) {
			JNote jNote = new JNote(null, note.getQuestion_id(), note.getNote_time(), note.getNote_content(),
					UserService.getInstance(appContext).getCurrentUserID(), note.getQuestionType_id());
			jNotes.add(jNote);
		}

		String jsonString = FastJsonTool.createJsonString(jNotes);
		String URL = "NoteServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("note", jsonString);
		map.put("type", "addList");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "添加笔记");
		}
	}

}
