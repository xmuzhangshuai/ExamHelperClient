package com.bishe.examhelper.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.CollectionDao;
import com.bishe.examhelper.dao.CollectionDao.Properties;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.QuestionTypeDao;
import com.bishe.examhelper.entities.Collection;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.utils.DateTimeTools;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.jsonobjects.JCollection;

public class CollectionService {

	private static final String TAG = CollectionService.class.getSimpleName();
	private static CollectionService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private CollectionDao mCollectionDao;
	private Collection mCollection;
	private QuestionTypeDao mQuestionTypeDao;

	public CollectionService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static CollectionService getInstance(Context context) {
		if (instance == null) {
			instance = new CollectionService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.mCollectionDao = instance.mDaoSession.getCollectionDao();
			instance.mQuestionTypeDao = instance.mDaoSession.getQuestionTypeDao();
		}
		return instance;
	}

	/**
	 * 根据ID返回实体
	 * @param id
	 * @return
	 */
	public Collection loadCollection(Long id) {
		return mCollectionDao.load(id);
	}

	/**
	 * 返回实体列表
	 * @return
	 */
	public List<Collection> loadAllCollections() {
		return mCollectionDao.loadAll();
	}

	/**
	 * 返回当前用户所有收藏
	 * @return
	 */
	public List<Collection> loadCurrentCollections() {
		return mCollectionDao.queryBuilder()
				.where(Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID()))
				.orderDesc(Properties.Collect_time).list();
	}

	/**
	 * 根据ID删除某条记录
	 */
	public void deleteCollectionById(Long id) {
		mCollectionDao.deleteByKey(id);
	}

	/**
	 * 删除某条记录
	 */
	public void deleteCollection(Collection collection) {
		mCollectionDao.delete(collection);
	}

	/**
	 * 从网络删除记录
	 */
	public void delCollectionFromNet(List<Collection> collections) {
		List<JCollection> jCollections = new ArrayList<JCollection>();
		for (int i = 0; i < collections.size(); i++) {
			Collection collection = collections.get(i);
			JCollection jCollection = new JCollection(collection.getId(), collection.getQuestion_id(),
					collection.getCollect_time(), collection.getUser_id(), collection.getQuestionType_id(),
					collection.getSection_id());
			jCollections.add(jCollection);
		}

		String jsonString = FastJsonTool.createJsonString(jCollections);
		String URL = "CollectionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("collection", jsonString);
		map.put("type", "deleteAll");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "单选题收藏出错");
		}
	}

	/**
	 * 判断是否本题已被收藏
	 * @param singleChoice
	 * @return
	 */
	public boolean isCollected(SingleChoice singleChoice) {
		boolean flag = false;

		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// 查找对应题型、题目的收藏
		Collection collection = mCollectionDao
				.queryBuilder()
				.where(Properties.Question_id.eq(singleChoice.getId()), Properties.QuestionType_id.eq(questionTypeId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();
		if (collection != null) {
			flag = true;
			mCollection = collection;
		}
		return flag;
	}

	/**
	 * 判断是否本题已被收藏
	 * @param singleChoice
	 * @return
	 */
	public boolean isCollected(MultiChoice multiChoice) {
		boolean flag = false;

		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// 查找对应题型、题目的收藏
		Collection collection = mCollectionDao
				.queryBuilder()
				.where(Properties.Question_id.eq(multiChoice.getId()), Properties.QuestionType_id.eq(questionTypeId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();
		if (collection != null) {
			flag = true;
			mCollection = collection;
		}
		return flag;
	}

	/**
	 * 判断是否本题已被收藏
	 * @param singleChoice
	 * @return
	 */
	public boolean isCollected(MaterialAnalysis materialAnalysis) {
		boolean flag = false;

		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// 查找对应题型、题目的收藏
		Collection collection = mCollectionDao
				.queryBuilder()
				.where(Properties.Question_id.eq(materialAnalysis.getId()),
						Properties.QuestionType_id.eq(questionTypeId),
						Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID())).unique();
		if (collection != null) {
			flag = true;
			mCollection = collection;
		}
		return flag;
	}

	/**
	 * 插入收藏实体
	 * @return
	 */
	public void insertCollection(SingleChoice singleChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// 新建实体
		mCollection = new Collection(null, singleChoice.getId(), DateTimeTools.getCurrentDate(), UserService
				.getInstance(appContext).getCurrentUserID(), questionTypeId, singleChoice.getSection_id());

		mCollectionDao.insertOrReplace(mCollection);
	}

	/**
	 * 插入收藏实体
	 * @return
	 */
	public void insertCollection(MultiChoice multiChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// 新建实体
		mCollection = new Collection(null, multiChoice.getId(), DateTimeTools.getCurrentDate(), UserService
				.getInstance(appContext).getCurrentUserID(), questionTypeId, multiChoice.getSection_id());

		mCollectionDao.insertOrReplace(mCollection);
	}

	/**
	 * 插入收藏实体
	 * @return
	 */
	public void insertCollection(MaterialAnalysis materialAnalysis) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// 新建实体
		mCollection = new Collection(null, materialAnalysis.getId(), DateTimeTools.getCurrentDate(), UserService
				.getInstance(appContext).getCurrentUserID(), questionTypeId, materialAnalysis.getSection_id());

		mCollectionDao.insertOrReplace(mCollection);
	}

	/**
	 * 将单选题收藏传送到网络
	 */
	public void addCollectionToNet(SingleChoice singleChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		com.jsonobjects.JCollection net = new com.jsonobjects.JCollection(null, singleChoice.getId(),
				DateTimeTools.getCurrentDate(), UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
				singleChoice.getSection_id());

		String jsonString = FastJsonTool.createJsonString(net);
		String URL = "CollectionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("collection", jsonString);
		map.put("type", "add");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "单选题收藏出错");
		}
	}

	/**
	 * 从网络端删除收藏
	 * @param singleChoice
	 */
	public void delCollectionFromNet(SingleChoice singleChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		com.jsonobjects.JCollection net = new com.jsonobjects.JCollection(null, singleChoice.getId(),
				DateTimeTools.getCurrentDate(), UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
				singleChoice.getSection_id());

		String jsonString = FastJsonTool.createJsonString(net);
		String URL = "CollectionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("collection", jsonString);
		map.put("type", "delete");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "单选题收藏出错");
		}
	}

	/**
	 * 将多选题收藏传送到网络
	 */
	public void addCollectionToNet(MultiChoice multiChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		com.jsonobjects.JCollection net = new com.jsonobjects.JCollection(null, multiChoice.getId(),
				DateTimeTools.getCurrentDate(), UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
				multiChoice.getSection_id());

		String jsonString = FastJsonTool.createJsonString(net);
		String URL = "CollectionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("collection", jsonString);
		map.put("type", "add");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "多选题收藏出错");
		}
	}

	/**
	 * 将多选题收藏从网络端删除
	 */
	public void delCollectionFromNet(MultiChoice multiChoice) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		com.jsonobjects.JCollection net = new com.jsonobjects.JCollection(null, multiChoice.getId(),
				DateTimeTools.getCurrentDate(), UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
				multiChoice.getSection_id());

		String jsonString = FastJsonTool.createJsonString(net);
		String URL = "CollectionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("collection", jsonString);
		map.put("type", "delete");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "多选题收藏出错");
		}
	}

	/**
	 * 将材料题收藏传送到网络
	 */
	public void addCollectionToNet(MaterialAnalysis materialAnalysis) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		com.jsonobjects.JCollection net = new com.jsonobjects.JCollection(null, materialAnalysis.getId(),
				DateTimeTools.getCurrentDate(), UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
				materialAnalysis.getSection_id());

		String jsonString = FastJsonTool.createJsonString(net);
		String URL = "CollectionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("collection", jsonString);
		map.put("type", "add");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "材料题收藏出错");
		}
	}

	/**
	 * 将材料题从网络端删除
	 */
	public void delCollectionFromNet(MaterialAnalysis materialAnalysis) {
		// 查找题型ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		com.jsonobjects.JCollection net = new com.jsonobjects.JCollection(null, materialAnalysis.getId(),
				DateTimeTools.getCurrentDate(), UserService.getInstance(appContext).getCurrentUserID(), questionTypeId,
				materialAnalysis.getSection_id());

		String jsonString = FastJsonTool.createJsonString(net);
		String URL = "CollectionServlet";
		Map<String, String> map = new HashMap<String, String>();
		map.put("collection", jsonString);
		map.put("type", "delete");
		try {
			HttpUtil.postRequest(URL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("网络", "材料题收藏出错");
		}
	}

	/**
	 * 获取该用户的收藏列表
	 */
	public void getNoteListFromNetByUser() {
		UserService userService = UserService.getInstance(appContext);
		if (userService.getCurrentUserID() > 1) {
			List<JCollection> jCollections = new ArrayList<JCollection>();
			String url = "CollectionServlet";
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "getCollectionListByUser");
			map.put("userId", String.valueOf(userService.getCurrentUserID()));
			try {
				String jsonString = HttpUtil.postRequest(url, map);
				jCollections = FastJsonTool.getObjectList(jsonString, JCollection.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<Collection> collections = loadCurrentCollections();
			if (collections != null) {
				for (Collection collection : collections) {
					deleteCollection(collection);
				}
			}

			if (jCollections != null) {
				for (JCollection jCollection : jCollections) {
					Collection collection = new Collection(null, jCollection.getQuestion_id(),
							jCollection.getCollect_time(), jCollection.getUser_id(), jCollection.getQuestionType_id(),
							jCollection.getSection_id());
					this.mCollectionDao.insert(collection);
				}
			}
		}

	}

	/**
	 * 在查找或者插入实体后，删除实体
	 */
	public boolean deleteCollection() {
		boolean falg = false;
		if (mCollection != null) {
			mCollectionDao.delete(mCollection);
			falg = true;
		}
		return falg;
	}

}
