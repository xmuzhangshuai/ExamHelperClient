package com.bishe.examhelper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.MaterialAnalysisDao;
import com.bishe.examhelper.dao.QuestionsOfMaterialDao;
import com.bishe.examhelper.entities.MaterialAnalysis;

public class MaterialAnalysisService {

	private static final String TAG = MaterialAnalysisService.class.getSimpleName();
	private static MaterialAnalysisService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public MaterialAnalysisDao materialAnalysisDao;
	public QuestionsOfMaterialDao questionsOfMaterialDao;

	public MaterialAnalysisService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
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
			instance.questionsOfMaterialDao = instance.mDaoSession.getQuestionsOfMaterialDao();
		}
		return instance;
	}

	/**
	 * 根据ID返回实体
	 * @param id
	 * @return
	 */
	public MaterialAnalysis loadMaterialAnalysis(Long id) {
		return materialAnalysisDao.load(id);
	}

	/**
	 * 返回实体Id列表
	 * @return
	 */
	public List<Integer> loadAllMaterialAnalysisId() {
		List<Integer> idList = new ArrayList<Integer>();
		if (loadAllMaterialAnalysis() != null) {
			for (MaterialAnalysis materialAnalysis : loadAllMaterialAnalysis()) {
				idList.add(materialAnalysis.getId().intValue());
			}
		}
		return idList;
	}

	/**
	 * 返回实体列表
	 * @return
	 */
	public List<MaterialAnalysis> loadAllMaterialAnalysis() {
		return materialAnalysisDao.loadAll();
	}

	/**
	 * 返回随机材料题
	 * @return
	 */
	public MaterialAnalysis getRandomMaterialAnalysis() {
		MaterialAnalysis materialAnalysis = null;
		Random random = new Random();
		if (materialAnalysisDao.count() > 0) {
			int id = random.nextInt((int) materialAnalysisDao.count());
			materialAnalysis = materialAnalysisDao.loadAll().get(id);
		}
		return materialAnalysis;
	}
}
