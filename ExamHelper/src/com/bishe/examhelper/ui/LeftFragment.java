package com.bishe.examhelper.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
	private Button signOutButton;// 注销按钮
	private Button changeImageButton;// 更改头像按钮

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

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;
	private static final int SCALE = 5;// 照片缩小比例

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
			if (imageByte != null) {
				Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);// 将字节数组转化成Bitmap
				Bitmap headBitmap = ImageTools.toRoundBitmap(imageBitmap);
				headImage.setImageBitmap(ImageTools.zoomBitmap(headBitmap, DensityUtil.dip2px(getActivity(), 100),
						DensityUtil.dip2px(getActivity(), 100)));
			} else {
				headImage.setBackgroundResource(R.drawable.photo);
			}

			login.setVisibility(View.GONE);
			userView.setVisibility(View.VISIBLE);

			// 昵称
			nickNameTextView.setText(user.getNickname());

			// 邮箱
			mailTextView.setText(user.getMail());

			// 真实姓名
			if (user.getRealname() != null) {
				realNameTextView.setText("姓名：" + user.getRealname());
			} else {
				realNameTextView.setText("姓名：");
			}

			// 性别
			if (user.getGender() != null) {
				genderTextView.setText("性别：" + user.getGender());
			} else {
				genderTextView.setText("性别：");
			}

			phoneTextView.setText("绑定手机：" + user.getPhone());// 手机号码
			integral.setText("我的积分：" + user.getIntegral());// 积分
		} else {
			login.setVisibility(View.VISIBLE);
			userView.setVisibility(View.GONE);
			realNameTextView.setText("姓名：");// 真实姓名
			genderTextView.setText("性别：");// 性别
			phoneTextView.setText("绑定手机：");// 手机号码
			integral.setText("我的积分：");// 积分
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
		signOutButton = (Button) rootView.findViewById(R.id.sign_out);// 注销
		changeImageButton = (Button) rootView.findViewById(R.id.change_headimage);// 更改头像

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
				if (getUserFromDb().getAvatar() != null) {
					BigHeadImageFragmentDialog bigHeadImageFragmentDialog = new BigHeadImageFragmentDialog();
					bigHeadImageFragmentDialog.show(getActivity().getFragmentManager(),
							"com.bieshe.examhelper.bigHeadImageFragmentDialog");
				}

			}
		});

		/**
		 * 注销用户
		 */
		signOutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setIcon(R.drawable.icon_warning).setTitle("温馨提示").setMessage("是否注销？")
						.setPositiveButton("是", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								UserService userService = UserService.getInstance(getActivity());
								// 注销
								userService.singOut();
								LeftFragment.this.onResume();
							}
						}).setNegativeButton("否", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						}).show();
			}
		});

		// 更改头像按钮
		changeImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPicturePicker(getActivity(), true);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				// 将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
				// 大头像存进数据库
				saveAvatar(bitmap, false);

				// 裁剪成100*100圆形小图片
				Bitmap newBitmap = ImageTools.toRoundBitmap(ImageTools.zoomBitmap(bitmap,
						DensityUtil.dip2px(getActivity(), 100), DensityUtil.dip2px(getActivity(), 100)));
				// 把小头像存进数据库
				saveAvatar(newBitmap, true);

				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();

				// 将处理过的图片显示在界面上，并保存到本地
				headImage.setImageBitmap(newBitmap);
				ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(),
						String.valueOf(System.currentTimeMillis()));

				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getActivity().getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				try {
					// 使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					if (photo != null) {
						// 大头像存进数据库
						saveAvatar(photo, false);

						// 裁剪成100*100圆形小图片
						Bitmap smallBitmap = ImageTools.toRoundBitmap(ImageTools.zoomBitmap(photo,
								DensityUtil.dip2px(getActivity(), 100), DensityUtil.dip2px(getActivity(), 100)));
						// 把小头像存进数据库
						saveAvatar(smallBitmap, true);

						// 释放原始图片占用的内存，防止out of memory异常发生
						photo.recycle();

						headImage.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			case CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
				} else {
					String fileName = getActivity().getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE)
							.getString("tempName", "");
					uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
				}
				cropImage(uri, 500, 500, CROP_PICTURE);
				break;

			case CROP_PICTURE:
				Bitmap photo = null;
				Bitmap smallBitmap = null;
				Uri photoUri = data.getData();
				if (photoUri != null) {
					photo = BitmapFactory.decodeFile(photoUri.getPath());
				}
				if (photo == null) {
					Bundle extra = data.getExtras();
					if (extra != null) {
						photo = (Bitmap) extra.get("data");
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);

						// 大头像存进数据库
						saveAvatar(photo, false);

						// 裁剪成100*100圆形小图片
						smallBitmap = ImageTools.toRoundBitmap(ImageTools.zoomBitmap(photo,
								DensityUtil.dip2px(getActivity(), 100), DensityUtil.dip2px(getActivity(), 100)));
						// 把小头像存进数据库
						saveAvatar(smallBitmap, true);
					}
				}
				headImage.setImageBitmap(smallBitmap);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 显示对话框，从拍照和相册选择图片来源
	 * @param context
	 * @param isCrop
	 */
	public void showPicturePicker(Context context, boolean isCrop) {
		final boolean crop = isCrop;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "拍照", "相册" }, new DialogInterface.OnClickListener() {
			// 类型码
			int REQUEST_CODE;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case TAKE_PICTURE:
					Uri imageUri = null;
					String fileName = null;
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (crop) {
						REQUEST_CODE = CROP;
						// 删除上一次截图的临时文件
						SharedPreferences sharedPreferences = getActivity().getSharedPreferences("temp",
								Context.MODE_WORLD_WRITEABLE);
						ImageTools.deletePhotoAtPathAndName(
								Environment.getExternalStorageDirectory().getAbsolutePath(),
								sharedPreferences.getString("tempName", ""));

						// 保存本次截图临时文件名字
						fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
						Editor editor = sharedPreferences.edit();
						editor.putString("tempName", fileName);
						editor.commit();
					} else {
						REQUEST_CODE = TAKE_PICTURE;
						fileName = "image.jpg";
					}
					imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
					// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, REQUEST_CODE);
					break;

				case CHOOSE_PICTURE:
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
					if (crop) {
						REQUEST_CODE = CROP;
					} else {
						REQUEST_CODE = CHOOSE_PICTURE;
					}
					openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(openAlbumIntent, REQUEST_CODE);
					break;

				default:
					break;
				}
			}
		});
		builder.create().show();
	}

	// 截取图片
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 判断数据库是否存有数据
	 * @return
	 */
	protected User getUserFromDb() {
		return UserService.getInstance(getActivity()).getCurrentUser();
	}

	/**
	 * 存贮用户头像
	 * @param bitmap
	 * @param is_small_avatar是否是小头像
	 */
	public void saveAvatar(Bitmap bitmap, boolean is_small_avatar) {
		UserService userService = UserService.getInstance(getActivity());
		User user = userService.getCurrentUser();
		// 如果是小头像
		if (is_small_avatar && user != null) {
			user.setSmall_avatar(ImageTools.bitmapToBytes(bitmap));
		} else if (!is_small_avatar && user != null) {
			user.setAvatar(ImageTools.bitmapToBytes(bitmap));
		} else {
			Log.e("存储头像", "存入本地数据库出错！");
		}

		userService.updateUser(user);
	}

}
