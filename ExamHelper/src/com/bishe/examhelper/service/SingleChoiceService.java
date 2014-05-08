package com.bishe.examhelper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.SingleChoiceDao;
import com.bishe.examhelper.entities.SingleChoice;

import android.content.Context;

public class SingleChoiceService {

	private static final String TAG = SingleChoiceService.class.getSimpleName();
	private static SingleChoiceService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public SingleChoiceDao singleChoiceDao;

	public SingleChoiceService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �õ�ʵ��
	 * @param context
	 * @return
	 */
	public static SingleChoiceService getInstance(Context context) {
		if (instance == null) {
			instance = new SingleChoiceService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.singleChoiceDao = instance.mDaoSession.getSingleChoiceDao();
		}
		return instance;
	}

	/**
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public SingleChoice loadSingleChoice(Long id) {
		return singleChoiceDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<SingleChoice> loadAllSingleChoice() {
		return singleChoiceDao.loadAll();
	}

	/**
	 * ����ʵ��Id�б�
	 * @return
	 */
	public List<Integer> loadAllSingleChoiceId() {
		List<Integer> idList = new ArrayList<Integer>();
		if (loadAllSingleChoice() != null) {
			for (SingleChoice singleChoice : loadAllSingleChoice()) {
				idList.add(singleChoice.getId().intValue());
			}
		}
		return idList;
	}

	/**
	 * ���������ѡ��
	 * @return
	 */
	public SingleChoice getRandomSingleChoice() {
		SingleChoice singleChoice = null;
		Random random = new Random();
		if (singleChoiceDao.count() > 0) {
			int rowId = random.nextInt((int) singleChoiceDao.count());
			singleChoice = singleChoiceDao.loadAll().get(rowId);
		}
		return singleChoice;
	}

}
