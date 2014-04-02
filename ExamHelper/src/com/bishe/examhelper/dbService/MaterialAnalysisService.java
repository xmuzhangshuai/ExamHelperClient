package com.bishe.examhelper.dbService;

import java.util.List;
import java.util.Random;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.MaterialAnalysisDao;
import com.bishe.examhelper.entities.MaterialAnalysis;

public class MaterialAnalysisService {
	
	private static final String TAG = MaterialAnalysisService.class.getSimpleName();
	private static MaterialAnalysisService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public MaterialAnalysisDao materialAnalysisDao;

	public MaterialAnalysisService() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * �õ�ʵ��
	 * @param context
	 * @return
	 */
	public static MaterialAnalysisService getInstance(Context context) {
		if (instance == null) {
			instance = new MaterialAnalysisService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.materialAnalysisDao = instance.mDaoSession.getMaterialAnalysisDao();
		}
		return instance;
	}

	/**
	 * ����ID����ʵ��
	 * @param id
	 * @return
	 */
	public MaterialAnalysis loadMaterialAnalysis(Long id) {
		return materialAnalysisDao.load(id);
	}

	/**
	 * ����ʵ���б�
	 * @return
	 */
	public List<MaterialAnalysis> loadAllMaterialAnalysis() {
		return materialAnalysisDao.loadAll();
	}
	
	/**
	 * �������������
	 * @return
	 */
	public MaterialAnalysis getRandomMaterialAnalysis(){
		MaterialAnalysis materialAnalysis;
		Random random = new Random();
		int id = random.nextInt((int)materialAnalysisDao.count())+1;
		materialAnalysis = materialAnalysisDao.load((long)id);
		return materialAnalysis;
	}
}
