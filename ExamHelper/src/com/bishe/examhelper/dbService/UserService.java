package com.bishe.examhelper.dbService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

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
	public User NetUserToUser(com.netdomains.User netUser) {

		return null;
	}

	/**
	 * 通过多线程把User改变更新到服务器
	 */
	public void updateUserToNet() {
		User user = getCurrentUser();
		String avatar = null;
		String small_avatar = null;
		if (user.getAvatar() != null) {
			avatar = new String(user.getAvatar());

		}

		final com.netdomains.User nUser = new com.netdomains.User(user.getMail(), user.getPassword(),
				user.getNickname(), user.getRealname(), user.getAge(), user.getPhone(), user.getGender(),
				user.getUser_state(), user.getProfession(), user.getArea(), user.getIntegral(), user.getAvatar()
						.toString(), null, null, null, null, null, null);

		nUser.setId(user.getId().intValue());

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = "ManageUserServlet";
				Map<String, String> map = new HashMap<String, String>();
				try {
					map.put("com.bishe.examhelper.updateuser", FastJsonTool.createJsonString(nUser));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("fastjson解析", "fastjson解析User出错");
				}
				try {
					String msg = HttpUtil.postRequest(url, map);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("网络存储", "网络存储用户出错");
				}
			}
		}).start();

	}
}
