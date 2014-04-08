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
						Properties.QuestionType_id.eq(questionTypeId)).unique();
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
		mCollection = new Collection(null, singleChoice.getId(), DateTimeTools.getCurrentDate(),
				UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, singleChoice.getSection_id());

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
		mCollection = new Collection(null, multiChoice.getId(), DateTimeTools.getCurrentDate(),
				UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, multiChoice.getSection_id());

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
		mCollection = new Collection(null, materialAnalysis.getId(), DateTimeTools.getCurrentDate(),
				UserService.getInstance(appContext).getCurrentUserID(), questionTypeId, materialAnalysis.getSection_id());

		mCollectionDao.insertOrReplace(mCollection);
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
