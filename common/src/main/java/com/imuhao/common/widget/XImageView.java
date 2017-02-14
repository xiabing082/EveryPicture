package com.imuhao.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.imuhao.common.R;

/**
 * Created by dafan on 2016/10/19 0019.
 */

public class XImageView extends ImageView {
	private Paint paint;
	private Paint paintBorder;
	private Bitmap mSrcBitmap;
	/**
	 * 圆角的弧度
	 */
	private float mRatio;
	private float mRadius = -1;
	private boolean mIsCircle;

	public XImageView(final Context context) {
		this(context, null);
	}

	public XImageView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.customImageViewStyle);
	}

	public XImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XImageView, defStyle, 0);

		mRadius = typedArray.getDimension(R.styleable.XImageView_radius, 0);
		mIsCircle = typedArray.getBoolean(R.styleable.XImageView_circle, false);
		mRatio = typedArray.getFloat(R.styleable.XImageView_ratio, -1);
		int srcResource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
		if (srcResource != 0) {
			mSrcBitmap = BitmapFactory.decodeResource(getResources(), srcResource);
		}

		typedArray.recycle();

		paint = new Paint();
		paint.setAntiAlias(true);
		paintBorder = new Paint();
		paintBorder.setAntiAlias(true);
	}

	public float getRatio() {
		return mRatio;
	}

	public void setRatio(float ratio) {
		mRatio = ratio;
	}

	public float getRadius() {
		return mRadius;
	}

	public void setRadius(float radius) {
		mRadius = radius;
	}

	public boolean isCircle() {
		return mIsCircle;
	}

	public void setCircle(boolean circle) {
		mIsCircle = circle;
	}

	@Override
	public void onDraw(Canvas canvas) {
		int width = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
		int height = canvas.getHeight() - getPaddingTop() - getPaddingBottom();
		Bitmap image = drawableToBitmap(getDrawable());
		if (mIsCircle) {
			Bitmap reSizeImage = reSizeImageC(image, width, height);
			canvas.drawBitmap(createCircleImage(reSizeImage, width, height),
					getPaddingLeft(), getPaddingTop(), null);

		} else {

			Bitmap reSizeImage = reSizeImage(image, width, height);
			canvas.drawBitmap(createRoundImage(reSizeImage, width, height),
					getPaddingLeft(), getPaddingTop(), null);
		}
	}

	/**
	 * 画圆角
	 *
	 * @param source
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap createRoundImage(Bitmap source, int width, int height) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, width, height);
		canvas.drawRoundRect(rect, mRadius, mRadius, paint);
		// 核心代码取两个图片的交集部分
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	/**
	 * 画圆
	 *
	 * @param source
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source, int width, int height) {

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2,
				paint);
		// 核心代码取两个图片的交集部分
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, (width - source.getWidth()) / 2,
				(height - source.getHeight()) / 2, paint);
		return target;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mRatio == -1f) {
			super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		} else {
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = MeasureSpec.getSize(heightMeasureSpec);
			if (width > 0)
				height = (int) ((float) width / mRatio);
			super.setMeasuredDimension(width, height);
		}
	}

	/**
	 * drawable转bitmap
	 *
	 * @param drawable
	 * @return
	 */
	private Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			if (mSrcBitmap != null) {
				return mSrcBitmap;
			} else {
				return null;
			}
		} else if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 重设Bitmap的宽高
	 *
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	private Bitmap reSizeImage(Bitmap bitmap, int newWidth, int newHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 计算出缩放比
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 矩阵缩放bitmap
		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	/**
	 * 重设Bitmap的宽高
	 *
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	private Bitmap reSizeImageC(Bitmap bitmap, int newWidth, int newHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int x = (newWidth - width) / 2;
		int y = (newHeight - height) / 2;
		if (x > 0 && y > 0) {
			return Bitmap.createBitmap(bitmap, 0, 0, width, height, null, true);
		}

		float scale = 1;

		if (width > height) {
			// 按照宽度进行等比缩放
			scale = ((float) newWidth) / width;

		} else {
			// 按照高度进行等比缩放
			// 计算出缩放比
			scale = ((float) newHeight) / height;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}
}