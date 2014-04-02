package com.bishe.examhelper.interfaces;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：OnAnswerChangedListener   
* 类描述：   做题页面回调接口，当进入模拟考试模式时，各题目的Fragment通过此接口与MockExamActivity交互，
*          将用户答案传回Activity
* 创建人：张帅  
* 创建时间：2014-1-10 下午11:09:50   
* 修改人：张帅   
* 修改时间：2014-1-10 下午11:09:50   
* 修改备注：   
* @version    
*    
*/
public interface OnAnswerChangedListener {
	/**
	 * 传回答案接口
	 * @param questionNumber题号
	 * @param answer答案
	 */
	public void onAnswerChanged(int questionNumber, String answer);
}
