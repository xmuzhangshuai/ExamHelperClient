package com.bishe.examhelper.config;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�DefaultString   
* ��������   ������һЩĬ�ϵ��ַ���
* �����ˣ���˧  
* ����ʱ�䣺2014-1-2 ����10:37:53   
* �޸��ˣ���˧   
* �޸�ʱ�䣺2014-1-2 ����10:37:53   
* �޸ı�ע��   
* @version    
*    
*/
public class DefaultValues {
	// ΢�ſ���ƽ̨ע��Ӧ�õ�AppID
	public static final String WEIXIN_APP_ID = "wx967daebe835fbeac";

	//��ĿID
	public static final int SUBJECT_ID = 2;

	// ΢��ͼ�ķ����������һ��url
	public static final String WEIXIN_CONTENT_URL = "http://www.umeng.com/social";

	// ��Ŀ����
	public static final String SUBJECT_POLITICAL_EXAM = "��������";

	// ��ĿĬ��Ҫ��
	public static final String REQUEST_SINGLE_CHOICE = "����ѡ���⣺����ÿ��������ĸ����У�ֻ��һ��ѡ���Ƿ�����ĿҪ��ġ�";
	public static final String REQUEST_MULTI_CHOICE = "����ѡ���⣺����ÿ��������ĸ����У�����������������ѡ���Ƿ�����ĿҪ��ġ�";
	public static final String REQUEST_MATERIAL_ANALYSIS = "���Ϸ�����: ��ϲ��ϻش�����,����д��ָ��λ���ϡ�";

	// ��������
	public static final String SINGLE_CHOICE = "����ѡ����";
	public static final String MULTI_CHOICE = "����ѡ����";
	public static final String MATERIAL_ANALYSIS = "���Ϸ�����";

	// ������ϰ
	public static final String[] SORTS = new String[] { "ϰ�⼯", "��������", "ģ������" };
	public static final String SORT_TITILE_NAME = "������ϰ";

	// ���½�չʾ
	public static final String BY_COLLECTION = "�ղ�";
	public static final String BY_ERROR = "����";

	// ����ģʽ
	public static final String MODEL_MOCK_EXAM = "model_mock_exam";// ģ�⿼��ģʽ
	public static final String MODEL_EXERCISE = "model_exercise";// ��ϰģʽ
	public static final String MODEL_DISPLAY = "model_display";// ����ģ���Ծ�

	/********** ģ�⿼������*************/
	public static final String MOCK_EXAM_RANDOM = "mock_exam_randmon";// ��������Ծ�
	public static final String MOCK_EXAM_REAL = "mock_exam_real";// ����ģ���Ծ�
}
