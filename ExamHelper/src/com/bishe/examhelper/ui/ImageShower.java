package com.bishe.examhelper.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bishe.examhelper.R;
import com.bishe.examhelper.utils.ImageTools;

/**
 * 类名称：ImageShower
 * 类描述：放大图片
 * 创建人： 张帅
 * 创建时间：2014-4-14 上午12:30:48
 *
 */
public class ImageShower extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshower);

		byte[] bitmapBytes = getIntent().getByteArrayExtra("imageToLoad");
		Bitmap bitmap = ImageTools.byteToBitmap(bitmapBytes);
		ImageView imageView = (ImageView) findViewById(R.id.imageToLoad);
		imageView.setImageBitmap(bitmap);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}
