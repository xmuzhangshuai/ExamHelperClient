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
	 * 得到实例
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
	 * 根据ID返回实体
	 * @param id
	 * @return
	 */
	public ErrorQuestions loadErrorQuestions(Long id) {
		return errorQuestionsDao.load(id);
	}

	/**
	 * 返回实体列表
	 * @return
	 */
	public List<ErrorQuestions> loadAllErrorQuestions() {
		return errorQuestionsDao.loadAll();
	}

	/**
	 * 返回当前用户错题列表
	 * @return
	 */
	public List<ErrorQuestions> loadCurrentQuestions() {
		return errorQuestionsDao.queryBuilder()
				.where(Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID()))
				.orderDesc(Properties.Error_time).list();
	}

	/**
	 * 根据ID删除某条记录
	 */
	public void deleteErrorQuestionsById(Long id) {
		errorQuestionsDao.deleteByKey(id);
	}

	/**
	 * 删除某条记录
	 */
	public void deleteErrorQuestions(ErrorQuestions errorQuestions) {
		errorQuestionsDao.delete(errorQuestions);
	}

	/**
	 * 插入一条单选题错题，如果曾经出过错，则出错次数加1
	 * @param singleChoice
	 */
	public void addErrorQuestions(SingleChoice singleChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// 查找对应题型、出错的题目
		ErrorQuestions errorQuestions = errorQuestionsDao
				.queryBuilder()
				.where(Properties.Question_id.eq(singleChoice.getId()), Properties.QuestionType_id.eq(questionTypeId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();

		if (errorQuestions != null) {// 如果已经存在
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// 出错次数加1
			errorQuestionsDao.insertOrReplace(errorQuestions);// 替换掉原先的实体
		} else {// 如果不存在
			// 新建实体
			errorQuestions = new ErrorQuestions(null, singleChoice.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
					singleChoice.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * 插入一条多选题错题，如果曾经出过错，则出错次数加1
	 * @param singleChoice
	 */
	public void addErrorQuestions(MultiChoice multiChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// 查找对应题型、出错的题目
		ErrorQuestions errorQuestions = errorQuestionsDao
				.queryBuilder()
				.where(Properties.Question_id.eq(multiChoice.getId()), Properties.QuestionType_id.eq(questionTypeId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();

		if (errorQuestions != null) {// 如果已经存在
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// 出错次数加1
			errorQuestionsDao.insertOrReplace(errorQuestions);// 替换掉原先的实体
		} else {// 如果不存在
			// 新建实体
			errorQuestions = new ErrorQuestions(null, multiChoice.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, multiChoice.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * 插入一条材料题错题，如果曾经出过错，则出错次数加1
	 * @param singleChoice
	 */
	public void addErrorQuestions(MaterialAnalysis materialAnalysis) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// 查找对应题型、出错的题目
		ErrorQuestions errorQuestions = errorQuestionsDao
				.queryBuilder()
				.where(Properties.Question_id.eq(materialAnalysis.getId()),
						Properties.QuestionType_id.eq(questionTypeId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();

		if (errorQuestions != null) {// 如果已经存在
			errorQuestions.setError_num(errorQuestions.getError_num() + 1);// 出错次数加1
			errorQuestionsDao.insertOrReplace(errorQuestions);// 替换掉原先的实体
		} else {// 如果不存在
			// 新建实体
			errorQuestions = new ErrorQuestions(null, materialAnalysis.getId(), DateTimeTools.getCurrentDate(), 1,
					UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
					materialAnalysis.getSection_id());
			errorQuestionsDao.insert(errorQuestions);
		}
	}

	/**
	 * 将一条单选错题插入到网络服务器数据库
	 * @param singleChoice
	 */
	public void addErrorQuestionsToNet(SingleChoice singleChoice) {
		// 查找题型ID
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
			Log.e("网络", "单选错题插入到网络服务器数据库");
		}
	}

	/**
	 * 将一条多选错题插入到网络服务器数据库
	 * @param singleChoice
	 */
	public void addErrorQuestionsToNet(MultiChoice multiChoice) {
		// 查找题型ID
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
			Log.e("网络", "多选错题插入到网络服务器数据库");
		}
	}

	/**
	 * 将一条材料分析错题插入到网络服务器数据库
	 * @param singleChoice
	 */
	public void addErrorQuestionsToNet(MaterialAnalysis materialAnalysis) {
		// 查找题型ID
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
			Log.e("网络", "材料分析错题插入到网络服务器数据库");
		}
	}

	/**
	 * 获取该用户的错题列表
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
