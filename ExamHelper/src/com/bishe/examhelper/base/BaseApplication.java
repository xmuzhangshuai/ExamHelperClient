package com.bishe.examhelper.base;
import com.bishe.examhelper.dao.DaoMaster;
import com.bishe.examhelper.dao.DaoMaster.OpenHelper;
import com.bishe.examhelper.dao.DaoSession;

import android.app.Application;
import android.content.Context;

/**   
 *    
 * ��Ŀ���ƣ�ExamHelper   
 * �����ƣ�BaseApplication   
 * ��������   ��ȡ��DaoMaster����ķ����ŵ�Application�����������δ�������Session����
 * �����ˣ���˧     
 * ����ʱ�䣺2013-12-20 ����9:10:55   
 * �޸��ˣ���˧     
 * �޸�ʱ�䣺2013-12-20 ����9:10:55   
 * �޸ı�ע��   
 * @version    
 *    
 */ 
public class BaseApplication extends Application {
	private static BaseApplication myApplication;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (myApplication == null)
			myApplication = this;
	}

	/** 
     * ȡ��DaoMaster 
	 *  
	 * @param context 
	 * @return 
	 */  
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			OpenHelper openHelper = new DaoMaster.DevOpenHelper(context, "ExamHelper.db", null);
			daoMaster = new DaoMaster(openHelper.getWritableDatabase());
		}
		return daoMaster;
	}
	
	/**
	 * ȡ��DaoSession 
	 * @param context
	 * @return
	 */
	public static DaoSession getDaoSession(Context context){
		if(daoSession == null){
			if(daoMaster == null){
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}
}


















