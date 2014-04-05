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
		return getCurrentUser().getId();
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
		getCurrentUser().setCurrent(false);
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
	public User NetUserToUser(com.netdomains.User netUser) {

		return null;
	}

	/**
	 * ͨ�����̰߳�User�ı���µ�������
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
					Log.e("fastjson����", "fastjson����User����");
				}
				try {
					String msg = HttpUtil.postRequest(url, map);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("����洢", "����洢�û�����");
				}
			}
		}).start();

	}
}
