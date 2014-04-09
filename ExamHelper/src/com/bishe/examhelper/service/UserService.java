package com.bishe.examhelper.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.bishe.examhelper.base.BaseApplication;
import com.bishe.examhelper.dao.DaoSession;
import com.bishe.examhelper.dao.UserDao;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;

public class UserService {
	private static final String TAG = CollectionService.class.getSimpleName();
	private static UserService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public UserDao userDao;
	boolean flag = false;

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
		if (getCurrentUser() != null) {
			return getCurrentUser().getId();
		}
		return (long) 1;
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
		List<User> users = userDao.loadAll();
		if (users != null) {
			for (int i = 0; i < users.size(); i++) {
				users.get(i).setCurrent(false);
				userDao.update(users.get(i));
			}
		}
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
	public User NetUserToUser(com.jsonobjects.JUser netUser) {
		User user = null;
		if (netUser != null) {
			user = new User((long) netUser.getId(), netUser.getMail(), netUser.getPassword(), netUser.getNickname(),
					netUser.getRealname(), netUser.getAge(), netUser.getPhone(), netUser.getGender(),
					netUser.getUser_state(), netUser.getProfession(), netUser.getArea(), netUser.getIntegral(),
					netUser.getAvatar(), false);
		}
		return user;
	}

	/**
	 * 本地User变为网络User
	 * @return
	 */
	public com.jsonobjects.JUser UserToNetUser(User user) {
		com.jsonobjects.JUser netUser = new com.jsonobjects.JUser(user.getId(), user.getMail(), user.getPassword(),
				user.getNickname(), user.getRealname(), user.getAge(), user.getPhone(), user.getGender(),
				user.getUser_state(), user.getProfession(), user.getArea(), user.getIntegral(), user.getAvatar());
		return netUser;

	}

	/**
	 * 从网络获取用户最新信息
	 * @return
	 */
	public void getUserFromNet() {
		User user = getCurrentUser();
		if (user != null) {
			final Map<String, String> map = new HashMap<String, String>();
			map.put("mail", user.getMail());
			map.put("pass", user.getPassword());
			final String url = "ManageUserServlet";

			try {
				com.jsonobjects.JUser netUser = FastJsonTool.getObject(HttpUtil.postRequest(url, map),
						com.jsonobjects.JUser.class);

				if (netUser != null) {
					User newUuser = NetUserToUser(netUser);
					newUuser.setCurrent(true);
					updateUser(newUuser);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 更新用户数据到网络
	 */
	public void updateUserToNet() {
		User user = getCurrentUser();
		final com.jsonobjects.JUser netUser = UserToNetUser(user);

		// TODO Auto-generated method stub
		if (netUser != null) {
			final String url = "ManageUserServlet";
			String userString = FastJsonTool.createJsonString(netUser);
			final Map<String, String> map = new HashMap<String, String>();
			map.put("com.bishe.examhelper.updateuser", userString);
			try {
				HttpUtil.postRequest(url, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
