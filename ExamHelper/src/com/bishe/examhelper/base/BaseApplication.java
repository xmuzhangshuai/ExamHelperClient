package com.bishe.examhelper.base;
import com.bishe.examhelper.dao.DaoMaster;
import com.bishe.examhelper.dao.DaoMaster.OpenHelper;
import com.bishe.examhelper.dao.DaoSession;

import android.app.Application;
import android.content.Context;

/**   
 *    
 * 项目名称：ExamHelper   
 * 类名称：BaseApplication   
 * 类描述：   将取得DaoMaster对象的方法放到Application层这样避免多次创建生成Session对象。
 * 创建人：张帅     
 * 创建时间：2013-12-20 下午9:10:55   
 * 修改人：张帅     
 * 修改时间：2013-12-20 下午9:10:55   
 * 修改备注：   
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
     * 取得DaoMaster 
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
	 * 取得DaoSession 
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


















