package com.bishe.examhelper.ui;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.entities.User;
import com.bishe.examhelper.service.UserService;
import com.bishe.examhelper.utils.CommonTools;
import com.bishe.examhelper.utils.HttpUtil;
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
public class PublishQueryActivity extends BaseActivity {
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
	private Bitmap contentBitmap;
	private byte[] contentImageBytes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_query);

		user = UserService.getInstance(PublishQueryActivity.this).getCurrentUser();

		findViewById();
		initView();
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
			Toast.makeText(PublishQueryActivity.this, "发布", 1).show();
			break;
		case R.id.action_takephoto:
			Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(getImageByCamera, 1);
			break;
		case R.id.action_getphoto:
			Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
			getImage.addCategory(Intent.CATEGORY_OPENABLE);
			getImage.setType("image/jpeg");
			startActivityForResult(getImage, 0);
			break;
		case R.id.action_addquestion:
			Intent getQuestionIntent = new Intent(PublishQueryActivity.this, CollectionListDisplayActivity.class);
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
		if (requestCode == 0) {

			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				contentBitmap = BitmapFactory.decodeFile(picturePath);
				contentImageView.setImageBitmap(contentBitmap);
				contentImageView.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception  
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				Bundle extras = data.getExtras();
				contentBitmap = (Bitmap) extras.get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				contentBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				contentImageBytes = baos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception  
			}
			contentImageView.setImageBitmap(contentBitmap);
			contentImageView.setVisibility(View.VISIBLE);
		} else if (requestCode == 2) {
			String content = "";
			Object object = (Object) data.getSerializableExtra("question");
			/**********如果是单选题**********/
			if (object.getClass().getSimpleName().equals(SingleChoice.class.getSimpleName())) {
				SingleChoice singleChoice = (SingleChoice) object;
				content = "单项选择题：" + "\n" + "    " + singleChoice.getQuestion_stem() + "\n" + "A."
						+ singleChoice.getOptionA() + "\n" + "B." + singleChoice.getOptionB() + "\n" + "C."
						+ singleChoice.getOptionC() + "\n" + "D." + singleChoice.getOptionD();
			}

			/**********如果是多选题**********/
			if (object.getClass().getSimpleName().equals(MultiChoice.class.getSimpleName())) {
				MultiChoice multiChoice = (MultiChoice) object;
				content = "多项选择题：" + "\n" + "    " + multiChoice.getQuestion_stem() + "\n" + "A."
						+ multiChoice.getOptionA() + "\n" + "B." + multiChoice.getOptionB() + "\n" + "C."
						+ multiChoice.getOptionC() + "\n" + "D." + multiChoice.getOptionD();
			}

			/**********如果是材料题**********/
			if (object.getClass().getSimpleName().equals(MaterialAnalysis.class.getSimpleName())) {
				MaterialAnalysis materialAnalysis = (MaterialAnalysis) object;
				content = "材料分析题：" + "\n" + "    " + materialAnalysis.getMaterial().substring(0, 200) + "...";
			}
			queryConent.setText(content);
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
		new AlertDialog.Builder(PublishQueryActivity.this).setTitle("温馨提示").setMessage("是否放弃发布疑问？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						PublishQueryActivity.this.finish();
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

		//设置头像
		imageLoader.displayImage(HttpUtil.BASE_URL + headImageURL, headImageView, options, animateFirstListener);

		//设置地点
		location.setText(CommonTools.getDetailLocation(PublishQueryActivity.this));
		//点击地点事件
		location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(PublishQueryActivity.this, "定位中...", 1).show();
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
}
