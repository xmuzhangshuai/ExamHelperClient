package com.bishe.examhelper.config;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：DeflultSetting   
* 类描述：   程序默认设置，恢复默认设置的使用
* 创建人：张帅  
* 创建时间：2014-1-2 下午10:32:01   
* 修改人：张帅   
* 修改时间：2014-1-2 下午10:32:01   
* 修改备注：   
* @version    
*    
*/
public class DefaultSetting {
	// 需要分组的起始章节ID
	public static int START_SECTION_ID = 5;

	// 需要分组的结束章节ID
	public static int END_SECTION_ID = 9;

	// 每组题目的个数
	public static int GROUP_CONTENT = 5;

	// 题目浏览页面题干显示长度
	public static int DISPLAY_STEM_LENGTH = 100;

	// 题目浏览页面答案显示长度
	public static int DISPLAY_ANSWER_LENGTH = 25;

	// 默认屏幕常亮
	public static final boolean DEFAULT_PREF_IF_LIGHT_ON = false;

	// 默认夜间模式
	public static final boolean DEFAULT_PREF_NIGHT_MODEL = false;

	// 默认是否立即检查答案
	public static final boolean DEFAULT_PREF_CHECK_NOW = false;

	// 默认答错题后震动
	public static final boolean DEFAULT_PREF_VIBRATE_AFTER = false;

	// 默认选择字体
	public static final String DEFAULT_PREF_SWITCH_FONTSIZE = "middle";
}
