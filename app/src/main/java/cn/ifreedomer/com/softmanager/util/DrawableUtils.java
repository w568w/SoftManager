package cn.ifreedomer.com.softmanager.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * 图片缩放工具类
 * @author 庄宏岩
 *
 */
public class DrawableUtils {

	// 放大缩小图片
	public static Bitmap scaleTo(final Bitmap bitmapOrg, final int newWidth, final int newHeight) {
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap b = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);

		return b;
	}

	public static Bitmap byte2Bitmap(Context mContext, byte[] b) {
		if (b != null) {
			if (b.length != 0) {
				return BitmapFactory.decodeByteArray(b, 0, b.length);
			} else {
				return BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.sym_def_app_icon);
			}
		}
		return BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.sym_def_app_icon);
	}

	public static Bitmap drawable2Bitmap(Context mContext, Drawable drawable) {
		return ((BitmapDrawable) drawable).getBitmap();
	}

	public static byte[] drawable2Byte(Drawable icon) {
		Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static byte[] bitmap2Byte(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Drawable byte2Drawable(Context mContext, byte[] b) {
		if (b != null) {
			if (b.length != 0) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
				return new BitmapDrawable(mContext.getResources(), bitmap);
			} else {
				return null;
			}
		}
		return null;
	}

	
	

}
