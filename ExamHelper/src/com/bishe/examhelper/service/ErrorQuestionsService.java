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
import com.bishe.examhelper.dao.ErrorQuestionsDao;
import com.bishe.examhelper.dao.ErrorQuestionsDao.Properties;
import com.bishe.examhelper.dao.QuestionTypeDao;
import com.bishe.examhelper.entities.ErrorQuestions;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.utils.DateTimeTools;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.jsonobjects.JErrorQuestions;

public class ErrorQuestionsService {
	private static final String TAG = ErrorQuestionsService.class.getSimpleName();
	private static ErrorQuestionsService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public ErrorQuestionsDao errorQuestionsDao;
	public QuestionTypeDao mQuestionTypeDao;

	public ErrorQuestionsService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �õ�ʵ��
	 * @param context
	 * @return
	 */
	public static ErrorQuestionsService getInstance(Context context) {
		if (instance == null) {
			instance = new ErrorQuestionsService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.errorQuestionsDao = instance.mDaoSession.getErrorQuestionsDao();
			instance.mQuestionTypeDao = instance.mDaoSession.getQuestionTypeDao();
		}
		return instance;
	}

	/**
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public ErrorQuestions loadErrorQuestions(Long id) {
		return errorQuestionsDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<ErrorQuestions> loadAllErrorQuestions() {
		return errorQuestionsDao.loadAll();
	}

	/**
	 * ���ص�ǰ�û������б�
	 * @return
	 */
	public List<ErrorQuestions> loadCurrentQuestions() {
		return errorQuestionsDao.queryBuilder()
				.where(Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID()))
				.orderDesc(Properties.Error_time).list();
	}

	/**
	 * ����IDɾ��ĳ����¼
	 */
	public void deleteErrorQuestionsById(Long id) {
		errorQuestionsDao.deleteByKey(id);
	}

	/**
	 * ɾ��ĳ����¼
	 */
	public void deleteErrorQuestions(ErrorQuestions errorQuestions) {
		errorQuestionsDao.delete(errorQuestions);
	}

	/**
	 * ����һ����ѡ����⣬�����������������������1
	 * @param singleChoice
	 */
	public void addErrorQuestions(SingleChoice singleChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// ���Ҷ�Ӧ���͡��������Ŀ
		ErrorQuestions errorQuestions = errorQuestionsDao
				.queryBuilder()
				.where(Properties.Question_id.eq(singleChoice.getId()), Properties.QuestionType_id.eq(questionTypeId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();

		if (errorQuestions != null) {// ����Ѿ�����
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// ���������1
			errorQuestionsDao.insertOrReplace(errorQuestions);// �滻��ԭ�ȵ�ʵ��
		} else {// ���������
			// �½�ʵ��
			errorQuestions = new ErrorQuestions(null, singleChoice.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
					singleChoice.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * ����һ����ѡ����⣬�����������������������1
	 * @param singleChoice
	 */
	public void addErrorQuestions(MultiChoice multiChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// ���Ҷ�Ӧ���͡��������Ŀ
		ErrorQuestions errorQuestions = errorQuestionsDao
				.queryBuilder()
				.where(Properties.Question_id.eq(multiChoice.getId()), Properties.QuestionType_id.eq(questionTypeId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();

		if (errorQuestions != null) {// ����Ѿ�����
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// ���������1
			errorQuestionsDao.insertOrReplace(errorQuestions);// �滻��ԭ�ȵ�ʵ��
		} else {// ���������
			// �½�ʵ��
			errorQuestions = new ErrorQuestions(null, multiChoice.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, multiChoice.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * ����һ����������⣬�����������������������1
	 * @param singleChoice
	 */
	public void addErrorQuestions(MaterialAnalysis materialAnalysis) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// ���Ҷ�Ӧ���͡��������Ŀ
		ErrorQuestions errorQuestions = errorQuestionsDao
				.queryBuilder()
				.where(Properties.Question_id.eq(materialAnalysis.getId()),
						Properties.QuestionType_id.eq(questionTypeId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();

		if (errorQuestions != null) {// ����Ѿ�����
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// ���������1
			errorQuestionsDao.insertOrReplace(errorQuestions);// �滻��ԭ�ȵ�ʵ��
		} else {// ���������
			// �½�ʵ��
			errorQuestions = new ErrorQuestions(null, materialAnalysis.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
					materialAnalysis.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * ��һ����ѡ������뵽������������ݿ�
	 * @param singleChoice
	 */
	public void addErrorQuestionsToNet(SingleChoice singleChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		JErrorQuestions netErrorQuestions = new JErrorQuestions(null, singleChoice.getId(),
				DateTimeTools.getCurrentDate(), 1, UserService.getInstance(appContext).getCurrentUserID(),
				questionTypeId, singleChoice.getSection_id());

		String jsonString = FastJsonTool.createJsonString(netErrorQuestions);
		String URL = "ErrorQuestionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("errorQuestion", jsonString);
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("����", "��ѡ������뵽������������ݿ�");
		}
	}

	/**
	 * ��һ����ѡ������뵽������������ݿ�
	 * @param singleChoice
	 */
	public void addErrorQuestionsToNet(MultiChoice multiChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		JErrorQuestions netErrorQuestions = new JErrorQuestions(null, multiChoice.getId(),
				DateTimeTools.getCurrentDate(), 1, UserService.getInstance(appContext).getCurrentUserID(),
				questionTypeId, multiChoice.getSection_id());

		String jsonString = FastJsonTool.createJsonString(netErrorQuestions);
		String URL = "ErrorQuestionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("errorQuestion", jsonString);
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("����", "��ѡ������뵽������������ݿ�");
		}
	}

	/**
	 * ��һ�����Ϸ���������뵽������������ݿ�
	 * @param singleChoice
	 */
	public void addErrorQuestionsToNet(MaterialAnalysis materialAnalysis) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		JErrorQuestions netErrorQuestions = new JErrorQuestions(null, materialAnalysis.getId(),
				DateTimeTools.getCurrentDate(), 1, UserService.getInstance(appContext).getCurrentUserID(),
				questionTypeId, materialAnalysis.getSection_id());

		String jsonString = FastJsonTool.createJsonString(netErrorQuestions);
		String URL = "ErrorQuestionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("errorQuestion", jsonString);
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("����", "���Ϸ���������뵽������������ݿ�");
		}
	}

	/**
	 * ��ȡ���û��Ĵ����б�
	 */
	public void getNoteListFromNetByUser() {
		UserService userService = UserService.getInstance(appContext);
		if (userService.getCurrentUserID() > 1) {
			List<JErrorQuestions> jErrorQuestions = new ArrayList<JErrorQuestions>();
			String url = "ErrorQuestionServlet";
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "getErrorListByUser");
			map.put("userId", String.valueOf(userService.getCurrentUserID()));

			try {
				String jsonString = HttpUtil.postRequest(url, map);
				jErrorQuestions = FastJsonTool.getObjectList(jsonString, JErrorQuestions.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<ErrorQuestions> errorQuestionList = loadCurrentQuestions();
			if (errorQuestionList != null) {
				for (ErrorQuestions errorQuestion : errorQuestionList) {
					deleteErrorQuestions(errorQuestion);
				}
			}

			if (jErrorQuestions != null) {
				for (JErrorQuestions jErrorQuestion : jErrorQuestions) {
					ErrorQuestions errorQuestion = new ErrorQuestions(null, jErrorQuestion.getQuestion_id(),
							jErrorQuestion.getError_time(), jErrorQuestion.getError_num(), jErrorQuestion.getUser_id(),
							jErrorQuestion.getQuestionType_id(), jErrorQuestion.getSection_id());
					this.errorQuestionsDao.insert(errorQuestion);
				}
			}
		}

	}

}
