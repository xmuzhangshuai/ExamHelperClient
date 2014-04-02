package com.bishe.examhelper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.bishe.examhelper.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**   
*    
* ��Ŀ���ƣ�ExamHelper   
* �����ƣ�CommonTools   
* ��������   ͨ�õĹ�����
* �����ˣ���˧  
* ����ʱ�䣺2013-12-22 ����7:26:29   
* �޸��ˣ���˧    
* �޸�ʱ�䣺2013-12-22 ����7:26:29   
* �޸ı�ע��   
* @version    
*    
*/
public class CommonTools {

	/**
	 * ������ʾToast��Ϣ
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShortToast(Context context, String message) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(message);
		Toast toast = new Toast(context);
		toast.setDuration(2000);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 300);
		toast.setView(view);
		toast.show();
	}

	/**
	 * �ж��ֻ�����
	 */
	public static boolean isMobileNO(String mobiles) {

		Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(mobiles);

		return matcher.matches();

	}

}
