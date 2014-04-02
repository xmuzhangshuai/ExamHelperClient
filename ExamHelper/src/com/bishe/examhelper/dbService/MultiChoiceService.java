package com.bishe.examhelper.dbService;

import java.util.List;
import java.util.Random;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.MultiChoiceDao;
import com.bishe.examhelper.entities.MultiChoice;

public class MultiChoiceService {
	private static final String TAG = MultiChoiceService.class.getSimpleName();
	private static MultiChoiceService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public MultiChoiceDao multiChoiceDao;

	public MultiChoiceService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �õ�ʵ��
	 * @param context
	 * @return
	 */
	public static MultiChoiceService getInstance(Context context) {
		if (instance == null) {
			instance = new MultiChoiceService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.multiChoiceDao = instance.mDaoSession.getMultiChoiceDao();
		}
		return instance;
	}

	/**
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public MultiChoice loadMultiChoice(Long id) {
		return multiChoiceDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<MultiChoice> loadAllMultiChoice() {
		return multiChoiceDao.loadAll();
	}
	
	/**
	 * ���������ѡ��
	 * @return
	 */
	public MultiChoice getRandomMultiChoice(){
		MultiChoice multiChoice;
		Random random = new Random();
		int id = random.nextInt((int)multiChoiceDao.count())+1;
		multiChoice = multiChoiceDao.load((long)id);
		return multiChoice;
	}


}
