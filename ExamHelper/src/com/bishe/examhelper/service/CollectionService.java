package com.bishe.examhelper.service;

import java.util.List;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.CollectionDao;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.QuestionTypeDao;
import com.bishe.examhelper.dao.CollectionDao.Properties;
import com.bishe.examhelper.entities.Collection;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.utils.DateTimeTools;

import android.content.Context;

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
		Collection collection = mCollectionDao.queryBuilder()
				.where(Properties.Question_id.eq(singleChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();
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
		Collection collection = mCollectionDao.queryBuilder()
				.where(Properties.Question_id.eq(multiChoice.getId()), Properties.QuestionType_id.eq(questionTypeId))
				.unique();
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
						Properties.QuestionType_id.eq(questionTypeId)).unique();
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
		mCollection = new Collection(null, singleChoice.getId(), DateTimeTools.getCurrentDate(),
				UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, singleChoice.getSection_id());

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
		mCollection = new Collection(null, multiChoice.getId(), DateTimeTools.getCurrentDate(),
				UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, multiChoice.getSection_id());

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
		mCollection = new Collection(null, materialAnalysis.getId(), DateTimeTools.getCurrentDate(),
				UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, materialAnalysis.getSection_id());

		mCollectionDao.insertOrReplace(mCollection);
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
