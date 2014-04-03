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
		List<User> users = userDao.loadAll();
		if (users != null) {
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getCurrent()) {
					return users.get(i);
				}
			}
		}

		return null;
	}

	/**
	 * 返回最后登录的User
	 */
	public User getLastUser() {
		List<User> users = userDao.loadAll();
		if (users.size() > 0) {
			return users.get(users.size() - 1);
		} else {
			return null;
		}
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
	public void deleteAllUser() {
		userDao.deleteAll();
	}

	/**
	 * 注销
	 */
	public void singOut() {
		getCurrentUser().setCurrent(false);
	}

	/**
	 * 保存用户
	 */
	public void saveUser(User user) {
		userDao.insertOrReplace(user);
	}

	/**
	 * 变为当前用户
	 */
	public void changeToCurrentUser(User user) {
		User currentUser = userDao.load(user.getId());
		currentUser.setCurrent(true);
	}

	/**
	 * 从网络传回数据变为本地User
	 */
	public User NetUserToUser(com.bieshe.examhelper.netdomains.User netUser) {
		byte[] avatar = null;
		byte[] small_avatar = null;
		if (netUser.getAvatar() != null) {
			avatar = netUser.getAvatar().getBytes();
		}

		if (netUser.getSmallAvatar() != null) {
			small_avatar = netUser.getSmallAvatar().getBytes();
		}
		User user = new User((long) netUser.getId(), netUser.getMail(), netUser.getPassword(), netUser.getNickname(),
				netUser.getRealname(), netUser.getAge(), netUser.getPhone(), netUser.getGender(),
				netUser.getUserState(), netUser.getProfession(), netUser.getArea(), netUser.getIntegral(), avatar,
				small_avatar, true);

		return user;
	}
}
