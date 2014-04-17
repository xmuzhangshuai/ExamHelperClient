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
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�QuestionDisplayAdapter   
* ��������   ��ĿԤ��չʾAdapter��չʾ�ղ���Ŀ��������Ŀ����ĿԤ���б���ifDisplayRemarkΪ�棬����ʾ������Ŀ�������
* �����ˣ���˧  
* ����ʱ�䣺2014-1-8 ����3:22:14   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-8 ����3:22:14   
* �޸ı�ע��   
* @version    
*    
*/
public class QuestionDisplayAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	List<Object> questionList;// ��Ŀ�б�
	List<StudyRecord> studyRecordList;
	List<SingleChoice> singleChoiceList;// ��ѡ���б�
	List<MultiChoice> multiChoiceList;// ��ѡ���б�
	List<MaterialAnalysis> materialAnalysisList;// �������б�
	List<String> remarkList;//��ע�б�
	Boolean ifDisplayRemark;//�Ƿ���ʾ��ע

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

		/******�������displayRemarkΪ�棬����ʾ��ע**********/
		if (ifDisplayRemark) {
			display_remark.setVisibility(View.VISIBLE);
			display_remark.setText(remarkList.get(position));
		}

		// �������
		display_num.setText("" + (position + 1) + ".");

		/**********����ǵ�ѡ��**********/
		if (object.getClass().getSimpleName().equals(SingleChoice.class.getSimpleName())) {
			SingleChoice singleChoice = (SingleChoice) object;

			/**������ɲ�����󳤶�**/
			display_stem.setText(getSubString(DefaultValues.SINGLE_CHOICE + ": " + singleChoice.getQuestion_stem(),
					DefaultSetting.DISPLAY_STEM_LENGTH));
			/**���ô𰸲�����󳤶�**/
			display_answer.setText("�𰸣� "
					+ getSubString(getSingleAnswer(singleChoice), DefaultSetting.DISPLAY_ANSWER_LENGTH));
		}

		/**********����Ƕ�ѡ��**********/
		if (object.getClass().getSimpleName().equals(MultiChoice.class.getSimpleName())) {
			MultiChoice multiChoice = (MultiChoice) object;

			/**������ɲ�����󳤶�**/
			display_stem.setText(getSubString(DefaultValues.MULTI_CHOICE + ": " + multiChoice.getQuestion_stem(),
					DefaultSetting.DISPLAY_STEM_LENGTH));
			/**���ô𰸲�����󳤶�**/
			display_answer.setText("�𰸣� "
					+ getSubString(getMultiAnswer(multiChoice), DefaultSetting.DISPLAY_ANSWER_LENGTH));
		}

		/**********����ǲ�����**********/
		if (object.getClass().getSimpleName().equals(MaterialAnalysis.class.getSimpleName())) {
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) object;

			/**������ɲ�����󳤶�**/
			display_stem.setText(getSubString(DefaultValues.MATERIAL_ANALYSIS + ": " + materialAnalysis.getMaterial(),
					DefaultSetting.DISPLAY_STEM_LENGTH));
			/**����ʾ��**/
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

	/*****ɾ��ĳ��Item****/
	public void deleteItem(Object object) {
		questionList.remove(object);
		this.notifyDataSetChanged();
	}

	/*****ɾ������Item****/
	public void deleteAllItem() {
		questionList.removeAll(questionList);
		this.notifyDataSetChanged();
	}

	/**
	 * ��ù淶�����ַ��ķ���
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
	 * ȡ�õ�ѡ�����ȷ��
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
	 * ȡ�ö�ѡ�����ȷ��
	 * @param multiChoice
	 * @return
	 */
	private String getMultiAnswer(MultiChoice multiChoice) {
		String answer = "";
		if (multiChoice.getAnswerA()) {
			answer = answer + multiChoice.getOptionA() + "�� ";
		}
		if (multiChoice.getAnswerB()) {
			answer = answer + multiChoice.getOptionB() + "�� ";
		}
		if (multiChoice.getAnswerC()) {
			answer = answer + multiChoice.getOptionC() + "�� ";
		}
		if (multiChoice.getAnswerD()) {
			answer = answer + multiChoice.getOptionD() + "�� ";
		}
		return answer;
	}
}
