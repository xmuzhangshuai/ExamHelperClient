package com.bishe.examhelper.adapters;

import java.util.List;

import com.bishe.examhelper.R;
import com.bishe.examhelper.config.DefaultSetting;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.entities.StudyRecord;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：QuestionDisplayAdapter   
* 类描述：   题目预览展示Adapter，展示收藏题目、出错题目的题目预览列表。若ifDisplayRemark为真，则显示出错题目出错次数
* 创建人：张帅  
* 创建时间：2014-1-8 下午3:22:14   
* 修改人：张帅   
* 修改时间：2014-1-8 下午3:22:14   
* 修改备注：   
* @version    
*    
*/
public class QuestionDisplayAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	List<Object> questionList;// 题目列表
	List<StudyRecord> studyRecordList;
	List<SingleChoice> singleChoiceList;// 单选题列表
	List<MultiChoice> multiChoiceList;// 多选题列表
	List<MaterialAnalysis> materialAnalysisList;// 分析题列表
	List<String> remarkList;//备注列表
	Boolean ifDisplayRemark;//是否显示备注

	public QuestionDisplayAdapter(Context context, List<Object> questionList, List<StudyRecord> studyRecords) {
		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(context);
		this.questionList = questionList;
		this.studyRecordList = studyRecords;
		this.ifDisplayRemark = false;
	}

	public QuestionDisplayAdapter(Context context, List<Object> questionList) {
		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(context);
		this.questionList = questionList;
		this.ifDisplayRemark = false;
	}

	public QuestionDisplayAdapter(Context context, List<Object> questionList, List<String> remarks,
			boolean displayRemark) {
		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(context);
		this.questionList = questionList;
		this.remarkList = remarks;
		this.ifDisplayRemark = displayRemark;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return questionList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.display_list_item, null);
		}

		TextView display_num = (TextView) convertView.findViewById(R.id.display_num);
		TextView display_stem = (TextView) convertView.findViewById(R.id.display_stem);
		TextView display_answer = (TextView) convertView.findViewById(R.id.display_answer);
		TextView display_remark = (TextView) convertView.findViewById(R.id.display_remark);

		Object object = questionList.get(position);

		/******如果参数displayRemark为真，则显示备注**********/
		if (ifDisplayRemark) {
			display_remark.setVisibility(View.VISIBLE);
			display_remark.setText(remarkList.get(position));
		}

		// 设置题号
		display_num.setText("" + (position + 1) + ".");

		/**********如果是单选题**********/
		if (object.getClass().getSimpleName().equals(SingleChoice.class.getSimpleName())) {
			SingleChoice singleChoice = (SingleChoice) object;

			/**设置题干并且最大长度**/
			display_stem.setText(getSubString(DefaultValues.SINGLE_CHOICE + ": " + singleChoice.getQuestion_stem(),
					DefaultSetting.DISPLAY_STEM_LENGTH));
			/**设置答案并且最大长度**/
			display_answer.setText("答案： "
					+ getSubString(getSingleAnswer(singleChoice), DefaultSetting.DISPLAY_ANSWER_LENGTH));
		}

		/**********如果是多选题**********/
		if (object.getClass().getSimpleName().equals(MultiChoice.class.getSimpleName())) {
			MultiChoice multiChoice = (MultiChoice) object;

			/**设置题干并且最大长度**/
			display_stem.setText(getSubString(DefaultValues.MULTI_CHOICE + ": " + multiChoice.getQuestion_stem(),
					DefaultSetting.DISPLAY_STEM_LENGTH));
			/**设置答案并且最大长度**/
			display_answer.setText("答案： "
					+ getSubString(getMultiAnswer(multiChoice), DefaultSetting.DISPLAY_ANSWER_LENGTH));
		}

		/**********如果是材料题**********/
		if (object.getClass().getSimpleName().equals(MaterialAnalysis.class.getSimpleName())) {
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) object;

			/**设置题干并且最大长度**/
			display_stem.setText(getSubString(DefaultValues.MATERIAL_ANALYSIS + ": " + materialAnalysis.getMaterial(),
					DefaultSetting.DISPLAY_STEM_LENGTH));
			/**不显示答案**/
			display_answer.setVisibility(View.GONE);
		}
		if (studyRecordList != null) {
			if (!studyRecordList.get(position).getIs_right()) {
				display_answer.setTextColor(Color.MAGENTA);
			} else {
				display_answer.setTextColor(Color.GREEN);
			}
		}

		return convertView;
	}

	/*****删除某项Item****/
	public void deleteItem(Object object) {
		questionList.remove(object);
		this.notifyDataSetChanged();
	}

	/*****删除所有Item****/
	public void deleteAllItem() {
		questionList.removeAll(questionList);
		this.notifyDataSetChanged();
	}

	/**
	 * 获得规范长度字符的方法
	 * @return
	 */
	private String getSubString(String oldString, int length) {
		if (oldString.length() > length) {
			return oldString.substring(0, length) + "...";
		} else {
			return oldString;
		}
	}

	/**
	 * 取得单选题的正确答案
	 * @param singleChoice
	 * @return
	 */
	private String getSingleAnswer(SingleChoice singleChoice) {
		String answer = singleChoice.getAnswer();
		if (answer.equals("A")) {
			return singleChoice.getOptionA();
		} else if (answer.equals("B")) {
			return singleChoice.getOptionB();
		} else if (answer.equals("C")) {
			return singleChoice.getOptionC();
		} else if (answer.equals("D")) {
			return singleChoice.getOptionD();
		} else {
			return null;
		}
	}

	/**
	 * 取得多选题的正确答案
	 * @param multiChoice
	 * @return
	 */
	private String getMultiAnswer(MultiChoice multiChoice) {
		String answer = "";
		if (multiChoice.getAnswerA()) {
			answer = answer + multiChoice.getOptionA() + "、 ";
		}
		if (multiChoice.getAnswerB()) {
			answer = answer + multiChoice.getOptionB() + "、 ";
		}
		if (multiChoice.getAnswerC()) {
			answer = answer + multiChoice.getOptionC() + "、 ";
		}
		if (multiChoice.getAnswerD()) {
			answer = answer + multiChoice.getOptionD() + "、 ";
		}
		return answer;
	}
}
