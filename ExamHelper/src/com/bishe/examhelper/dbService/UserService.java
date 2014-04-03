package com.bishe.examhelper.dbService;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.UserDao;
import com.bishe.examhelper.entities.User;

public class UserService {
	private static final String TAG = CollectionService.class.getSimpleName();
	private static UserService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public UserDao userDao;

	public UserService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static UserService getInstance(Context context) {
		if (instance == null) {
			instance = new UserService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.userDao = instance.mDaoSession.getUserDao();
		}
		return instance;
	}

	/**
	 * 返回当前用户ID
	 * @return
	 */
	public Long getCurrentUserID() {
		return getCurrentUser().getId();
	}

	/**
	 * 返回当前用户ID
	 * @return
	 */
	public User getCurrentUser() {
		return userDao.loadByRowId(1);
	}

	/**
	 * 更新个人信息
	 * @param user
	 */
	public void updateUser(User user) {
		userDao.update(user);
	}
	
	/**
	 * 删除用户信息
	 */
	public void deleteUser(){
		userDao.deleteAll();
	}
}
