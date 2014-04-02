package com.bishe.examhelper.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.dbService.UserService;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.utils.DensityUtil;
import com.bishe.examhelper.utils.ImageTools;

/**   
*    
* 项目名称：ExamHelper   
* 类名称：LeftFragment   
* 类描述：   主页面左滑菜单Fragment，个人中心页面
* 创建人：张帅  
* 创建时间：2013-12-22 下午7:24:30   
* 修改人：zhangshuai   
* 修改时间：2013-12-22 下午7:24:30   
* 修改备注：   
* @version    
*    
*/
public class LeftFragment extends BaseV4Fragment {
	private View userView;// 个人信息
	private ImageButton switchButton;// 切换主题按钮
	private ImageView headImage;// 头像

	private SharedPreferences sharedPreferences;

	private View rootView;
	private RelativeLayout relativeLayout;// 背景图片
	private TextView personalTextView;// 个人
	private TextView securityTextView;// 安全
	private TextView integralTextView;// 积分
	private TextView realNameTextView;// 真实姓名
	private TextView genderTextView;// 性别
	private TextView phoneTextView;// 手机号码
	private TextView integral;// 积分
	private Button login;// 登陆按钮
	private TextView nickNameTextView;// 昵称
	private TextView mailTextView;// 邮箱
	private User user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.main_left_fragment, container, false);

		findViewById();

		/*** 从sharedPreference中取出主题并设置，默认为1号主题***/
		int defaultThemeID = R.drawable.person_center_background01;
		int defaultThemeColorID = R.color.Ucenter_theme3;
		sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_user_center),
				Context.MODE_PRIVATE);
		int themeID = sharedPreferences.getInt(getString(R.string.user_center_theme_key), defaultThemeID);
		int themeColorID = sharedPreferences
				.getInt(getString(R.string.user_center_themecolor_key), defaultThemeColorID);
		relativeLayout.setBackgroundResource(themeID);
		personalTextView.setBackgroundResource(themeColorID);
		securityTextView.setBackgroundResource(themeColorID);
		integralTextView.setBackgroundResource(themeColorID);
		/*** 从sharedPreference中取出主题并设置，默认为1号主�?***/

		initView();
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 新建sharedPreferences存储用户信息
		sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_user_center),
				Context.MODE_PRIVATE);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// 如果用户已登录，从数据库中取用户数据
		user = getUserFromDb();
		if (user != null) {
			byte[] imageByte = user.getSmall_avatar();// 取出图片字节数组
			Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);// 将字节数组转化成Bitmap
			Bitmap headBitmap = ImageTools.toRoundBitmap(imageBitmap);
			headImage.setImageBitmap(ImageTools.zoomBitmap(headBitmap, DensityUtil.dip2px(getActivity(), 100),
					DensityUtil.dip2px(getActivity(), 100)));
			nickNameTextView.setText(user.getNickname());
			mailTextView.setText(user.getMail());
			login.setVisibility(View.GONE);
			userView.setVisibility(View.VISIBLE);
			realNameTextView.setText("姓名：" + user.getRealname());// 真实姓名
			genderTextView.setText("性别：" + user.getGender());// 性别
			phoneTextView.setText("绑定手机：" + user.getPhone());// 手机号码
			integral.setText("我的积分：" + user.getIntegral());// 积分
		} else {
			login.setVisibility(View.VISIBLE);
			userView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		/** 点击更换主题，显示更换主题的dialog **/
		switchButton = (ImageButton) getActivity().findViewById(R.id.pCenter_button_switchtheme);
		switchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fragmentManager = getActivity().getFragmentManager();
				ThemeSitcherDialogFragment themeSitcher = new ThemeSitcherDialogFragment();
				themeSitcher.show(fragmentManager, "com.bishe.examhelper.themesitcherdialogfragment");
			}
		});
		/** 点击更换主题，显示更换主题的dialog **/

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		userView = (LinearLayout) rootView.findViewById(R.id.pCenter_HeadImage_LinearLayout);
		relativeLayout = (RelativeLayout) rootView.findViewById(R.id.person_cente_relativeLayout);// 背景图片
		personalTextView = (TextView) rootView.findViewById(R.id.user_center_itemname1);// 个人背景颜色
		securityTextView = (TextView) rootView.findViewById(R.id.user_center_itemname2);// 安全背景颜色
		integralTextView = (TextView) rootView.findViewById(R.id.user_center_itemname3);// 积分背景颜色
		login = (Button) rootView.findViewById(R.id.login_btn);
		nickNameTextView = (TextView) rootView.findViewById(R.id.pCenter_name);// 昵称
		mailTextView = (TextView) rootView.findViewById(R.id.pCenter_account);// 邮箱
		headImage = (ImageView) rootView.findViewById(R.id.pCenter_button_headimage);// 头像
		realNameTextView = (TextView) rootView.findViewById(R.id.user_real_name);// 真实姓名
		genderTextView = (TextView) rootView.findViewById(R.id.user_gender);// 性别
		phoneTextView = (TextView) rootView.findViewById(R.id.user_phone);// 手机号码
		integral = (TextView) rootView.findViewById(R.id.user_integral);// 积分

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		rootView.findViewById(R.id.ivTitleBtnLeft).setVisibility(View.GONE);
		rootView.findViewById(R.id.ivTitleBtnRigh).setVisibility(View.GONE);
		TextView title = (TextView) rootView.findViewById(R.id.ivTitleName);
		title.setText("个人中心");

		// 登陆
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		// 放大头像
		headImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BigHeadImageFragmentDialog bigHeadImageFragmentDialog = new BigHeadImageFragmentDialog();
				bigHeadImageFragmentDialog.show(getActivity().getFragmentManager(),
						"com.bieshe.examhelper.bigHeadImageFragmentDialog");
			}
		});

	}

	/**
	 * 判断数据库是否存有数据
	 * @return
	 */
	protected User getUserFromDb() {
		User user = UserService.getInstance(getActivity()).getCurrentUser();
		if (user != null) {
			return user;
		} else {
			return null;
		}
	}

}