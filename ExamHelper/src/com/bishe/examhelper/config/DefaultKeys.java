package com.bishe.examhelper.config;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：Bundle_params   
* 类描述：   此类用于intent传递参数时的key名称
* 创建人：张帅  
* 创建时间：2014-1-2 下午12:34:05   
* 修改人：张帅   
* 修改时间：2014-1-2 下午12:34:05   
* 修改备注：   
* @version    
*    
*/
public class DefaultKeys {

	/********** ListActivity*************/
	public static final String BUNDLE_LIST_TYPE = "com.bieshi.examhelper.bundle_list_type";// ListActivity的标题名称
	public static final String BUNDLE_TITLE_NAME = "com.bieshi.examhelper.bundle_title_name";// ListActivity的标题名称

	/********** ExamActivity*************/
	public static final String BUNDLE_EXAMACTIVITY_EXAMID = "com.bieshi.examhelper.bundle_examactivity_examID";// ListActivity的标题名称

	/********** ExerciseActivity/ExamActivity*************/
	public static final String BUNDLE_QUESTION = "bundle_question";
	public static final String BUNDLE_SINGLE_CHOICE = "bundle_single_choice";
	public static final String BUNDLE_SINGLE_CHOICE_POSITION = "bundle_single_choice_position";
	public static final String BUNDLE_MULTI_CHOICE = "bundle_multi_choice";
	public static final String BUNDLE_MULTI_CHOICE_POSITION = "bundle_multi_choice_position";
	public static final String BUNDLE_MATERIAL_ANALYSIS = "bundle_material_analysis";
	public static final String BUNDLE_MATERIAL_ANALYSIS_POSITION = "bundle_material_analysis_position";
	public static final String INTENT_GROUP_ID = "intent_group_id";// ExpandableListActivity到ExerciseActivity的传送group

	/********** MyErrorActivity*************/
	public static final String BUNDLE_SECTION_NUMBER = "bundle_section_number";

	/********** QuestionDisplayActivity*************/
	public static final String COLLECTION_LIST = "collection_list";
	public static final String ERROR_LIST = "error_list";

	/********** BySectionFragment*************/
	public static final String BY_SECTION_FRAGMENT_TYPE = "by_section_fragment";

	/********** 默认Preference键值*************/
	public static final String KEY_PREF_COURSE_SWITCH = "pref_course_switch";// 科目切换
	public static final String KEY_PREF_LIBRARY_MANAGE = "pref_library_mangage";// 题库管理
	public static final String KEY_PREF_IF_LIGHT_ON = "pref_if_light_on";// 屏幕常亮
	public static final String KEY_PREF_NIGHT_MODEL = "pref_night_model";// 夜间模式
	public static final String KEY_PREF_CHECK_NOW = "pref_check_now";// 是否立即检查答案
	public static final String KEY_PREF_VIBRATE_AFTER = "pref_vibrate_after";// 答错题后震动
	public static final String KEY_PREF_SWITCH_FONTSIZE = "pref_switch_fontsize";// 选择字体
	public static final String KEY_PREF_SETTING_DEFAULT = "pref_setting_default";// 恢复默认
	public static final String KEY_PREF_ADVICE_FEEDBACK = "pref_advice_feedback";// 意见反馈
	public static final String KEY_PREF_CHECK_UPDATE = "pref_check_update";// 检查更新
	public static final String KEY_PREF_ABOUT_SOFTWARE = "pref_about_software";// 关于软件
	public static final String KEY_PREF_COMMENT = "pref_comment";//精彩推荐

	/********** 答题模式*************/
	public static final String KEY_QUESTION_MODEL = "key_question_model";// 模拟考试模式

	/********** 模拟考试*************/
	public static final String KEY_MOCK_EXAM_INCLUDE = "key_mock_exam_include";// 是否包含材料题
	public static final String KEY_MOCK_EXAM_ID = "key_mock_exam_id";// 题目类型，-1为随机生成，>-1为试卷ID
	public static final String KEY_MOCK_EXAM_PREVIEW_LIST = "key_mock_exam_preview_list";// 预览list
	public static final String KEY_MOCK_EXAM_PREVIEW_BACK = "key_mock_exam_preview_back";// 预览页面传回的参数
	public static final String KEY_MOCK_EXAM_SINGLE_RESULT = "key_mock_exam_single_result";// 往MockExamResultActivity页面传参数,单选题
	public static final String KEY_MOCK_EXAM_MULTI_RESULT = "key_mock_exam_multi_result";// 往MockExamResultActivity页面传参数,多选题
	public static final String KEY_MOCK_EXAM_TITLE = "key_mock_exam_title";// 试卷题目
	public static final String KEY_USER_ANSWER = "key_user_answer";// 用户答案

	/***********考试指南************/
	public static final String 	BUNDLE_EXAMGUIDE_TYPE = "bundle_examguide_type";// 考试指南目录
}
