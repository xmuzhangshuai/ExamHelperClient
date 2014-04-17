package com.bishe.examhelper.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.Question;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.service.UserService;
import com.bishe.examhelper.utils.CommonTools;
import com.bishe.examhelper.utils.DateTimeTools;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.ImageTools;
import com.bishe.examhelper.utils.NetworkUtils;
import com.jsonobjects.JQuerys;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 类名称：PublishQueryActivity
 * 类描述：发布疑问
 * 创建人： 张帅
 * 创建时间：2014-4-14 下午4:37:33
 *
 */
public class QueryPublishActivity extends BaseActivity {
	/*******VIEW*********/
	private TextView userNickName;
	private TextView location;
	private EditText queryConent;
	private ImageView headImageView;
	private ImageView contentImageView;

	/*******用户变量*********/
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private User user;
	private Question mQuestion;
	private Bitmap contentBitmap;
	private String mPhotoPath;
	private long questionID;
	private String queryImageUrl;
	private long questionTypeID;
	ProgressDialog dialog;

	// 提醒用户网络状况有异常
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (!NetworkUtils.isNetworkAvailable(QueryPublishActivity.this)) {
				NetworkUtils.networkStateTips(QueryPublishActivity.this);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_query);

		/**********从题目页面接收题目信息***********/
		mQuestion = (Question) getIntent().getSerializableExtra("question");

		user = UserService.getInstance(QueryPublishActivity.this).getCurrentUser();
		dialog = new ProgressDialog(QueryPublishActivity.this);

		findViewById();
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		QueryPublishActivity.this.registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 卸载广播
		if (broadcastReceiver != null) {
			QueryPublishActivity.this.unregisterReceiver(broadcastReceiver);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.publish_query_activity_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_publish:
			if (!NetworkUtils.isNetworkAvailable(QueryPublishActivity.this)) {
				NetworkUtils.networkStateTips(QueryPublishActivity.this);
			} else {
				publish();
			}
			break;
		case R.id.action_takephoto:
			Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
			mPhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.jpg";
			File mPhotoFile = new File(mPhotoPath);
			if (!mPhotoFile.exists()) {
				try {
					mPhotoFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
			startActivityForResult(getImageByCamera, 1);
			break;
		case R.id.action_getphoto:
			Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
			getImage.addCategory(Intent.CATEGORY_OPENABLE);
			getImage.setType("image/jpeg");
			startActivityForResult(getImage, 0);
			break;
		case R.id.action_addquestion:
			Intent getQuestionIntent = new Intent(QueryPublishActivity.this, CollectionListDisplayActivity.class);
			startActivityForResult(getQuestionIntent, 2);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		/** 
		* 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法， 
		* 所以为了区别到底选择了那个方式获取图片要进行判断，这里的requestCode跟startActivityForResult里面第二个参数对应 
		*/
		//从相册选择
		if (requestCode == 0) {

			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				mPhotoPath = cursor.getString(columnIndex);
				cursor.close();
				contentBitmap = BitmapFactory.decodeFile(mPhotoPath);
				contentImageView.setImageBitmap(contentBitmap);
				contentImageView.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception  
				e.printStackTrace();
			}

		}
		//拍照
		else if (requestCode == 1) {
			try {
				contentBitmap = BitmapFactory.decodeFile(mPhotoPath, null);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception  
			}
			contentImageView.setImageBitmap(contentBitmap);
			contentImageView.setVisibility(View.VISIBLE);
		} else if (requestCode == 2) {
			if (data != null) {
				String content = "";
				Object object = (Object) data.getSerializableExtra("question");
				/**********如果是单选题**********/
				if (object.getClass().getSimpleName().equals(SingleChoice.class.getSimpleName())) {
					SingleChoice singleChoice = (SingleChoice) object;
					questionTypeID = 1;
					questionID = singleChoice.getId();
					content = "单项选择题：" + "\n" + "    " + singleChoice.getQuestion_stem() + "\n" + "A."
							+ singleChoice.getOptionA() + "\n" + "B." + singleChoice.getOptionB() + "\n" + "C."
							+ singleChoice.getOptionC() + "\n" + "D." + singleChoice.getOptionD();
				}

				/**********如果是多选题**********/
				if (object.getClass().getSimpleName().equals(MultiChoice.class.getSimpleName())) {
					MultiChoice multiChoice = (MultiChoice) object;
					questionID = multiChoice.getId();
					questionTypeID = 2;
					content = "多项选择题：" + "\n" + "    " + multiChoice.getQuestion_stem() + "\n" + "A."
							+ multiChoice.getOptionA() + "\n" + "B." + multiChoice.getOptionB() + "\n" + "C."
							+ multiChoice.getOptionC() + "\n" + "D." + multiChoice.getOptionD();
				}

				/**********如果是材料题**********/
				if (object.getClass().getSimpleName().equals(MaterialAnalysis.class.getSimpleName())) {
					MaterialAnalysis materialAnalysis = (MaterialAnalysis) object;
					questionID = materialAnalysis.getId();
					questionTypeID = 3;
					content = "材料分析题：" + "\n" + "    " + materialAnalysis.getMaterial().substring(0, 200) + "...";
				}
				queryConent.setText(content);
			}

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (contentBitmap != null) {
			contentBitmap.recycle();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(QueryPublishActivity.this).setTitle("温馨提示").setMessage("是否放弃发布疑问？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						QueryPublishActivity.this.finish();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		userNickName = (TextView) findViewById(R.id.userNickName);
		location = (TextView) findViewById(R.id.location);
		queryConent = (EditText) findViewById(R.id.queryContent);
		headImageView = (ImageView) findViewById(R.id.headImage);
		contentImageView = (ImageView) findViewById(R.id.content_image);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("我要提问");
		getActionBar().setIcon(R.drawable.ic_title_compose);

		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions  
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.photoconor)// 设置图片下载期间显示的图片  
				.showImageForEmptyUri(R.drawable.default_hedad_iamge) // 设置图片Uri为空或是错误的时候显示的图片  
				.showImageOnFail(R.drawable.default_hedad_iamge) // 设置图片加载或解码过程中发生错误显示的图片      
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中  
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中  
				.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片  
				.build(); // 创建配置过得DisplayImageOption对象 

		String headImageURL = "";
		if (user != null) {
			//设置用户昵称
			userNickName.setText(user.getNickname());
			headImageURL = user.getAvatar();
		}

		/**如果传来的题目不为空，则初始化编辑区域***/
		if (mQuestion != null) {
			initEditConent();
		}

		//设置头像
		imageLoader.displayImage(HttpUtil.BASE_URL + headImageURL, headImageView, options, animateFirstListener);

		//设置地点
		location.setText(CommonTools.getDetailLocation(QueryPublishActivity.this));
		//点击地点事件
		location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(QueryPublishActivity.this, "定位中...", 1).show();
			}
		});
	}

	/**
	 * 发布疑问
	 */
	private void publish() {
		dialog.setMessage("请稍候，正在发布疑问...");
		dialog.setCancelable(false);
		dialog.show();
		UserService userService = UserService.getInstance(QueryPublishActivity.this);
		if (userService.getCurrentUser() == null) {
			Toast.makeText(QueryPublishActivity.this, "请先登录", 1).show();
		} else {
			/**************上传图片****************/
			if (mPhotoPath != null) {
				uploadImage(mPhotoPath);
			} else {
				pushToNet();
			}

		}
	}

	/**
	 * 把疑问信息发送到网络
	 */
	private void pushToNet() {

		JQuerys jQuerys = new JQuerys(null, questionID, DateTimeTools.getCurrentDate(), queryConent.getText()
				.toString(), 0, null, queryImageUrl, UserService.getInstance(QueryPublishActivity.this)
				.getCurrentUserID(), questionTypeID);
		String jsonString = FastJsonTool.createJsonString(jQuerys);
		new PublishQueryTask().execute(jsonString);
	}

	/**
	 * 展示Intent中的题目信息
	 */
	private void initEditConent() {
		String content = "";
		/**********如果是单选题**********/
		if (mQuestion.getQuestion_type().equals(SingleChoice.class.getSimpleName())) {
			SingleChoice singleChoice = (SingleChoice) mQuestion;
			questionID = singleChoice.getId();
			questionTypeID = 1;
			content = "单项选择题：" + "\n" + "    " + singleChoice.getQuestion_stem() + "\n" + "A."
					+ singleChoice.getOptionA() + "\n" + "B." + singleChoice.getOptionB() + "\n" + "C."
					+ singleChoice.getOptionC() + "\n" + "D." + singleChoice.getOptionD();
		}

		/**********如果是多选题**********/
		if (mQuestion.getQuestion_type().equals(MultiChoice.class.getSimpleName())) {
			MultiChoice multiChoice = (MultiChoice) mQuestion;
			questionID = multiChoice.getId();
			questionTypeID = 2;
			content = "多项选择题：" + "\n" + "    " + multiChoice.getQuestion_stem() + "\n" + "A."
					+ multiChoice.getOptionA() + "\n" + "B." + multiChoice.getOptionB() + "\n" + "C."
					+ multiChoice.getOptionC() + "\n" + "D." + multiChoice.getOptionD();
		}

		/**********如果是材料题**********/
		if (mQuestion.getQuestion_type().equals(MaterialAnalysis.class.getSimpleName())) {
			MaterialAnalysis materialAnalysis = (MaterialAnalysis) mQuestion;
			questionID = materialAnalysis.getId();
			questionTypeID = 3;
			content = "材料分析题：" + "\n" + "    " + materialAnalysis.getMaterial().substring(0, 200) + "...";
		}
		queryConent.setText(content);
	}

	/**
	 * 上传图片
	 */
	public void uploadImage(String filePath) {
		String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zoom.jpg";
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) {
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Bitmap bitmap = ImageTools.getPhotoFromSDCard(filePath);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(tempPath);
			if (bitmap != null) {
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fileOutputStream)) {
					try {
						fileOutputStream.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UserService userService = UserService.getInstance(QueryPublishActivity.this);
		String uploadHost = HttpUtil.BASE_URL + "ImageUploadServlet";
		RequestParams params = new RequestParams();
		params.addBodyParameter("userID", String.valueOf(userService.getCurrentUserID()));
		params.addBodyParameter("type", "queryImage");
		params.addBodyParameter(tempPath.replace("/", ""), new File(tempPath));
		uploadMethod(params, uploadHost);
	}

	/**
	 * 上传文件
	 * 
	 * @param params
	 * @param uploadHost
	 */
	public void uploadMethod(final RequestParams params, final String uploadHost) {
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
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
				// 重新更新用户内容
				queryImageUrl = responseInfo.result;
				pushToNet();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}

	/** 
	 * 图片加载第一次显示监听器 
	 * @author Administrator 
	 * 
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示  
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果  
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/**
	 * 
	 * 类名称：PublishQueryTask
	 * 类描述：发布疑问道网络异步任务
	 * 创建人： 张帅
	 * 创建时间：2014-4-16 下午3:03:36
	 *
	 */
	class PublishQueryTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "QueryServlet";
			String jsonString = params[0];
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "addquery");
			map.put("query", jsonString);
			try {
				HttpUtil.postRequest(url, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.cancel();
			}
			Toast.makeText(QueryPublishActivity.this, "发布成功！", 1).show();
			QueryPublishActivity.this.finish();
		}

	}
}
