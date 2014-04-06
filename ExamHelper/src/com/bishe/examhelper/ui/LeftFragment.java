package com.bishe.examhelper.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
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
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseV4Fragment;
import com.bishe.examhelper.dbService.UserService;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.utils.DensityUtil;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.ImageTools;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

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
	private Bitmap headBitmap;// 头像
	private Button signOutButton;// 注销按钮
	private Button changeImageButton;// 更改头像按钮
	private boolean isChanged = false;// 是否更改头像

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
		user = getCurrentUser();
		if (user != null) {
			String headImageURI = user.getAvatar();
			if (headImageURI != null) {
				AQuery aq = new AQuery(rootView);
				String thumbnail = HttpUtil.BASE_URL + headImageURI.trim();

				Bitmap preset = aq.getCachedImage(thumbnail);
				if (preset == null) {
					aq = aq.cache(thumbnail, 10000);
					preset = aq.getCachedImage(thumbnail);
				}

				if (preset != null && !isChanged) {
					headBitmap = preset;

					Bitmap smallAvatar = ImageTools.toRoundBitmap(ImageTools.zoomBitmap(preset,
							DensityUtil.dip2px(getActivity(), 100), DensityUtil.dip2px(getActivity(), 100)));
					headImage.setImageBitmap(smallAvatar);
				}

			} else {
				headImage.setImageResource(R.drawable.photo);
				headBitmap = null;
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
				if (headBitmap != null) {
					BigHeadImageFragmentDialog bigHeadImageFragmentDialog = new BigHeadImageFragmentDialog();
					Bundle bundle = new Bundle();
					bundle.putByteArray("com.bishe.examhelper.headimageBitmap", ImageTools.bitmapToBytes(headBitmap));
					bigHeadImageFragmentDialog.setArguments(bundle);
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
				showPicturePicker(getActivity());
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {

			case CROP:
				Uri uri = null;
				if (intent != null) {
					uri = intent.getData();
				} else {
					String fileName = getActivity().getSharedPreferences("temp", Context.MODE_PRIVATE).getString(
							"tempName", "");
					uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
				}
				cropImage(uri, 500, 500, CROP_PICTURE);
				break;

			case CROP_PICTURE:
				Bitmap photo = null;
				Bitmap smallBitmap = null;
				Uri photoUri = intent.getData();

				if (photoUri != null) {
					photo = BitmapFactory.decodeFile(photoUri.getPath());
					// uploadImage(photoUri.getPath());
				}
				if (photo == null) {
					Bundle bundle = intent.getExtras();
					if (bundle != null) {
						photo = (Bitmap) bundle.get("data");
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.PNG, 100, stream);

						String fileName = "" + getCurrentUser().getId() + "headImage.png";

						// 删除上次文件
						ImageTools.deletePhotoAtPathAndName(
								Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
						ImageTools.savePhotoToSDCard(photo,
								Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);

						File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);

						uploadImage(file.getPath());

						// 裁剪成100*100圆形小图片
						smallBitmap = ImageTools.toRoundBitmap(ImageTools.zoomBitmap(photo, 100, 100));
						isChanged = true;
						headImage.setImageBitmap(smallBitmap);
						headBitmap = photo;
					}
				}

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
	public void showPicturePicker(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "拍照", "相册" }, new DialogInterface.OnClickListener() {
			// 类型码
			int REQUEST_CODE;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				/**
				 * 拍照
				 */
				case TAKE_PICTURE:
					Uri imageUri = null;
					String fileName = null;
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 拍照裁剪
					REQUEST_CODE = CROP;
					// 删除上一次截图的临时文件
					SharedPreferences sharedPreferences = getActivity().getSharedPreferences("temp",
							Context.MODE_PRIVATE);
					ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(),
							sharedPreferences.getString("tempName", ""));

					// 保存本次截图临时文件名字
					fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
					Editor editor = sharedPreferences.edit();
					editor.putString("tempName", fileName);
					editor.commit();

					imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
					// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, REQUEST_CODE);
					break;

				/**
				 * 从图片库选择
				 */
				case CHOOSE_PICTURE:
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);

					// 图库选择裁剪
					REQUEST_CODE = CROP;
					// 打开图片库
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
	protected User getCurrentUser() {
		return UserService.getInstance(getActivity()).getCurrentUser();
	}

	/**
	 * 上传头像
	 */
	public void uploadImage(String filePath) {
		String uploadHost = HttpUtil.BASE_URL + "UploadServlet";
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_key",
				String.valueOf(UserService.getInstance(getActivity()).getCurrentUserID().intValue()));
		params.addBodyParameter(filePath.replace("/", ""), new File(filePath));
		uploadMethod(params, uploadHost);
	}

	/**
	 * 上传文件
	 * @param params
	 * @param uploadHost
	 */
	public void uploadMethod(final RequestParams params, final String uploadHost) {
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				Toast.makeText(getActivity(), "开始上传...", 1).show();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
					System.out.println("upload: " + current + "/" + total);
				} else {
					System.out.println("reply: " + current + "/" + total);
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Toast.makeText(getActivity(), "头像上传成功！", 1).show();
				// 重新更新用户内容
				UserService.getInstance(getActivity()).getUserFromNet();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(error.getExceptionCode() + ":" + msg);
				Toast.makeText(getActivity(), "头像上传失败！" + msg, 1).show();
			}
		});
	}

}
