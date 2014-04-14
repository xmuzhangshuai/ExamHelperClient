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
 * �����ƣ�PublishQueryActivity
 * ����������������
 * �����ˣ� ��˧
 * ����ʱ�䣺2014-4-14 ����4:37:33
 *
 */
public class PublishQueryActivity extends BaseActivity {
	/*******VIEW*********/
	private TextView userNickName;
	private TextView location;
	private EditText queryConent;
	private ImageView headImageView;
	private ImageView contentImageView;

	/*******�û�����*********/
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
			Toast.makeText(PublishQueryActivity.this, "����", 1).show();
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
		* ��Ϊ���ַ�ʽ���õ���startActivityForResult�������������ִ����󶼻�ִ��onActivityResult������ 
		* ����Ϊ�����𵽵�ѡ�����Ǹ���ʽ��ȡͼƬҪ�����жϣ������requestCode��startActivityForResult����ڶ���������Ӧ 
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
			/**********����ǵ�ѡ��**********/
			if (object.getClass().getSimpleName().equals(SingleChoice.class.getSimpleName())) {
				SingleChoice singleChoice = (SingleChoice) object;
				content = "����ѡ���⣺" + "\n" + "    " + singleChoice.getQuestion_stem() + "\n" + "A."
						+ singleChoice.getOptionA() + "\n" + "B." + singleChoice.getOptionB() + "\n" + "C."
						+ singleChoice.getOptionC() + "\n" + "D." + singleChoice.getOptionD();
			}

			/**********����Ƕ�ѡ��**********/
			if (object.getClass().getSimpleName().equals(MultiChoice.class.getSimpleName())) {
				MultiChoice multiChoice = (MultiChoice) object;
				content = "����ѡ���⣺" + "\n" + "    " + multiChoice.getQuestion_stem() + "\n" + "A."
						+ multiChoice.getOptionA() + "\n" + "B." + multiChoice.getOptionB() + "\n" + "C."
						+ multiChoice.getOptionC() + "\n" + "D." + multiChoice.getOptionD();
			}

			/**********����ǲ�����**********/
			if (object.getClass().getSimpleName().equals(MaterialAnalysis.class.getSimpleName())) {
				MaterialAnalysis materialAnalysis = (MaterialAnalysis) object;
				content = "���Ϸ����⣺" + "\n" + "    " + materialAnalysis.getMaterial().substring(0, 200) + "...";
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
		new AlertDialog.Builder(PublishQueryActivity.this).setTitle("��ܰ��ʾ").setMessage("�Ƿ�����������ʣ�")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						PublishQueryActivity.this.finish();
					}
				}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
		getActionBar().setTitle("��Ҫ����");
		getActionBar().setIcon(R.drawable.ic_title_compose);

		// ʹ��DisplayImageOptions.Builder()����DisplayImageOptions  
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.photoconor)// ����ͼƬ�����ڼ���ʾ��ͼƬ  
				.showImageForEmptyUri(R.drawable.default_hedad_iamge) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
				.showImageOnFail(R.drawable.default_hedad_iamge) // ����ͼƬ���ػ��������з���������ʾ��ͼƬ      
				.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���  
				.cacheOnDisc(true) // �������ص�ͼƬ�Ƿ񻺴���SD����  
				.displayer(new RoundedBitmapDisplayer(20)) // ���ó�Բ��ͼƬ  
				.build(); // �������ù���DisplayImageOption���� 

		String headImageURL = "";
		if (user != null) {
			//�����û��ǳ�
			userNickName.setText(user.getNickname());
			headImageURL = user.getAvatar();
		}

		//����ͷ��
		imageLoader.displayImage(HttpUtil.BASE_URL + headImageURL, headImageView, options, animateFirstListener);

		//���õص�
		location.setText(CommonTools.getDetailLocation(PublishQueryActivity.this));
		//����ص��¼�
		location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(PublishQueryActivity.this, "��λ��...", 1).show();
			}
		});
	}

	/** 
	 * ͼƬ���ص�һ����ʾ������ 
	 * @author Administrator 
	 * 
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// �Ƿ��һ����ʾ  
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// ͼƬ����Ч��  
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
