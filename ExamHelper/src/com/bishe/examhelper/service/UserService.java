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
	 * �õ�ʵ��
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
	 * ���ص�ǰ�û�ID
	 * @return
	 */
	public Long getCurrentUserID() {
		if (getCurrentUser() != null) {
			return getCurrentUser().getId();
		}
		return (long) 1;
	}

	/**
	 * ���ص�ǰ�û�ID
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
	 * ��������¼��User
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
	 * ���¸�����Ϣ
	 * @param user
	 */
	public void updateUser(User user) {
		userDao.update(user);
	}

	/**
	 * ɾ���û���Ϣ
	 */
	public void deleteAllUser() {
		userDao.deleteAll();
	}

	/**
	 * ע��
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
	 * �����û�
	 */
	public void saveUser(User user) {
		userDao.insertOrReplace(user);
	}

	/**
	 * ��Ϊ��ǰ�û�
	 */
	public void changeToCurrentUser(User user) {
		User currentUser = userDao.load(user.getId());
		currentUser.setCurrent(true);
	}

	/**
	 * �����紫�����ݱ�Ϊ����User
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
	 * ����User��Ϊ����User
	 * @return
	 */
	public com.jsonobjects.JUser UserToNetUser(User user) {
		com.jsonobjects.JUser netUser = new com.jsonobjects.JUser(user.getId(), user.getMail(), user.getPassword(),
				user.getNickname(), user.getRealname(), user.getAge(), user.getPhone(), user.getGender(),
				user.getUser_state(), user.getProfession(), user.getArea(), user.getIntegral(), user.getAvatar());
		return netUser;

	}

	/**
	 * �������ȡ�û�������Ϣ
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
	 * �����û����ݵ�����
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
