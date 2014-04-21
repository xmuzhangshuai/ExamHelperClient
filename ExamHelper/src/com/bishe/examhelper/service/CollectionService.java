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
	 * �õ�ʵ��
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
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public Collection loadCollection(Long id) {
		return mCollectionDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<Collection> loadAllCollections() {
		return mCollectionDao.loadAll();
	}

	/**
	 * ���ص�ǰ�û������ղ�
	 * @return
	 */
	public List<Collection> loadCurrentCollections() {
		return mCollectionDao.queryBuilder()
				.where(Properties.User_id.eq(UserService.getInstance(appContext).getCurrentUserID()))
				.orderDesc(Properties.Collect_time).list();
	}

	/**
	 * ����IDɾ��ĳ����¼
	 */
	public void deleteCollectionById(Long id) {
		mCollectionDao.deleteByKey(id);
	}

	/**
	 * ɾ��ĳ����¼
	 */
	public void deleteCollection(Collection collection) {
		mCollectionDao.delete(collection);
	}

	/**
	 * ������ɾ����¼
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
			Log.e("����", "��ѡ���ղس���");
		}
	}

	/**
	 * �ж��Ƿ����ѱ��ղ�
	 * @param singleChoice
	 * @return
	 */
	public boolean isCollected(SingleChoice singleChoice) {
		boolean flag = false;

		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// ���Ҷ�Ӧ���͡���Ŀ���ղ�
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
	 * �ж��Ƿ����ѱ��ղ�
	 * @param singleChoice
	 * @return
	 */
	public boolean isCollected(MultiChoice multiChoice) {
		boolean flag = false;

		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// ���Ҷ�Ӧ���͡���Ŀ���ղ�
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
	 * �ж��Ƿ����ѱ��ղ�
	 * @param singleChoice
	 * @return
	 */
	public boolean isCollected(MaterialAnalysis materialAnalysis) {
		boolean flag = false;

		// ��������ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// ���Ҷ�Ӧ���͡���Ŀ���ղ�
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
	 * �����ղ�ʵ��
	 * @return
	 */
	public void insertCollection(SingleChoice singleChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.SINGLE_CHOICE))
				.unique().getId();

		// �½�ʵ��
		mCollection = new Collection(null, singleChoice.getId(), DateTimeTools.getCurrentDate(), UserService
				.getInstance(appContext).getCurrentUserID(), questionTypeId, singleChoice.getSection_id());

		mCollectionDao.insertOrReplace(mCollection);
	}

	/**
	 * �����ղ�ʵ��
	 * @return
	 */
	public void insertCollection(MultiChoice multiChoice) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name.eq(DefaultValues.MULTI_CHOICE))
				.unique().getId();

		// �½�ʵ��
		mCollection = new Collection(null, multiChoice.getId(), DateTimeTools.getCurrentDate(), UserService
				.getInstance(appContext).getCurrentUserID(), questionTypeId, multiChoice.getSection_id());

		mCollectionDao.insertOrReplace(mCollection);
	}

	/**
	 * �����ղ�ʵ��
	 * @return
	 */
	public void insertCollection(MaterialAnalysis materialAnalysis) {
		// ��������ID
		Long questionTypeId = mQuestionTypeDao
				.queryBuilder()
				.where(com.bishe.examhelper.dao.QuestionTypeDao.Properties.Type_name
						.eq(DefaultValues.MATERIAL_ANALYSIS)).unique().getId();

		// �½�ʵ��
		mCollection = new Collection(null, materialAnalysis.getId(), DateTimeTools.getCurrentDate(), UserService
				.getInstance(appContext).getCurrentUserID(), questionTypeId, materialAnalysis.getSection_id());

		mCollectionDao.insertOrReplace(mCollection);
	}

	/**
	 * ����ѡ���ղش��͵�����
	 */
	public void addCollectionToNet(SingleChoice singleChoice) {
		// ��������ID
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
			Log.e("����", "��ѡ���ղس���");
		}
	}

	/**
	 * �������ɾ���ղ�
	 * @param singleChoice
	 */
	public void delCollectionFromNet(SingleChoice singleChoice) {
		// ��������ID
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
			Log.e("����", "��ѡ���ղس���");
		}
	}

	/**
	 * ����ѡ���ղش��͵�����
	 */
	public void addCollectionToNet(MultiChoice multiChoice) {
		// ��������ID
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
			Log.e("����", "��ѡ���ղس���");
		}
	}

	/**
	 * ����ѡ���ղش������ɾ��
	 */
	public void delCollectionFromNet(MultiChoice multiChoice) {
		// ��������ID
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
			Log.e("����", "��ѡ���ղس���");
		}
	}

	/**
	 * ���������ղش��͵�����
	 */
	public void addCollectionToNet(MaterialAnalysis materialAnalysis) {
		// ��������ID
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
			Log.e("����", "�������ղس���");
		}
	}

	/**
	 * ��������������ɾ��
	 */
	public void delCollectionFromNet(MaterialAnalysis materialAnalysis) {
		// ��������ID
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
			Log.e("����", "�������ղس���");
		}
	}

	/**
	 * ��ȡ���û����ղ��б�
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
	 * �ڲ��һ��߲���ʵ���ɾ��ʵ��
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
