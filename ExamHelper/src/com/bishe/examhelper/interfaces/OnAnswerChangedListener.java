package com.bishe.examhelper.interfaces;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�OnAnswerChangedListener   
* ��������   ����ҳ��ص��ӿڣ�������ģ�⿼��ģʽʱ������Ŀ��Fragmentͨ���˽ӿ���MockExamActivity������
*          ���û��𰸴���Activity
* �����ˣ���˧  
* ����ʱ�䣺2014-1-10 ����11:09:50   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-10 ����11:09:50   
* �޸ı�ע��   
* @version    
*    
*/
public interface OnAnswerChangedListener {
	/**
	 * ���ش𰸽ӿ�
	 * @param questionNumber���
	 * @param answer��
	 */
	public void onAnswerChanged(int questionNumber, String answer);
}
