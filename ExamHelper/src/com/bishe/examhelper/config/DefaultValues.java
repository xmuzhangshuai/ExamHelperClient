package com.bishe.examhelper.config;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：DefaultString   
* 类描述：   程序中一些默认的字符串
* 创建人：张帅  
* 创建时间：2014-1-2 下午10:37:53   
* 修改人：张帅   
* 修改时间：2014-1-2 下午10:37:53   
* 修改备注：   
* @version    
*    
*/
public class DefaultValues {
	// 微信开发平台注册应用的AppID
	public static final String WEIXIN_APP_ID = "wx967daebe835fbeac";

	//科目ID
	public static final int SUBJECT_ID = 2;

	// 微信图文分享必须设置一个url
	public static final String WEIXIN_CONTENT_URL = "http://www.umeng.com/social";

	// 科目名称
	public static final String SUBJECT_POLITICAL_EXAM = "考研政治";

	// 题目默认要求
	public static final String REQUEST_SINGLE_CHOICE = "单项选择题：下列每题给出的四个答案中，只有一个选项是符合题目要求的。";
	public static final String REQUEST_MULTI_CHOICE = "多项选择题：下列每题给出的四个答案中，有两个或两个以上选项是符合题目要求的。";
	public static final String REQUEST_MATERIAL_ANALYSIS = "材料分析题: 结合材料回答问题,将答案写在指定位置上。";

	// 题型名称
	public static final String SINGLE_CHOICE = "单项选择题";
	public static final String MULTI_CHOICE = "多项选择题";
	public static final String MATERIAL_ANALYSIS = "材料分析题";

	// 分类练习
	public static final String[] SORTS = new String[] { "习题集", "历年真题", "模拟试题" };
	public static final String SORT_TITILE_NAME = "分类练习";

	// 按章节展示
	public static final String BY_COLLECTION = "收藏";
	public static final String BY_ERROR = "错题";

	// 答题模式
	public static final String MODEL_MOCK_EXAM = "model_mock_exam";// 模拟考试模式
	public static final String MODEL_EXERCISE = "model_exercise";// 练习模式
	public static final String MODEL_DISPLAY = "model_display";// 真题模拟试卷

	/********** 模拟考试类型*************/
	public static final String MOCK_EXAM_RANDOM = "mock_exam_randmon";// 随机生成试卷
	public static final String MOCK_EXAM_REAL = "mock_exam_real";// 真题模拟试卷
}
