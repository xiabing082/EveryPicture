package com.imuhao.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.imuhao.common.R;
import com.imuhao.common.glide.GlideUtils;
import com.imuhao.common.utils.DensityUtil;

import java.util.List;


/**
 * @author shoyu
 * @ClassName MultiImageView.java
 * @Description: 显示1~N张图片的View
 */

public class MultiImageView extends LinearLayout {
	public static int MAX_WIDTH = 0;

	// 照片的Url列表
	private List<String> imagesList;

	/**
	 * 长度 单位为Pixel
	 **/
	private int pxOneMaxWandH;  // 单张图最大允许宽高
	private int pxMoreWandH = 0;// 多张图的宽高
	private int pxImagePadding = DensityUtil.dp2px(getContext(), 3);// 图片间的间距

	private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

	private LayoutParams onePicPara;
	private LayoutParams morePara, moreParaColumnFirst;
	private LayoutParams rowPara;

	private OnItemClickListener mOnItemClickListener;
	private OnItemLongClickListener mOnItemLongClickListener;
	// 图片点击事件
	private OnClickListener mImageViewOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(view, (Integer) view.getTag(R.string.app_name));
			}
		}
	};

	private OnLongClickListener mImageViewOnLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View view) {
			if (mOnItemLongClickListener != null)
				return mOnItemLongClickListener.onItemLongClick(view, (Integer) view.getTag(R.string.app_name));
			else
				return false;
		}
	};

	public MultiImageView(Context context) {
		super(context);
	}

	public MultiImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
		this.mOnItemLongClickListener = onItemLongClickListener;
	}

	public void setList(List<String> lists) throws IllegalArgumentException {
		if (lists == null) {
			//			throw new IllegalArgumentException("imageList is null...");
			return;
		}
		imagesList = lists;

		if (MAX_WIDTH > 0) {
			pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) * 43 / 140; //解决右侧图片和内容对不齐问题
			pxOneMaxWandH = MAX_WIDTH * 100 / 143;
			initImageLayoutParams();
		}

		initView();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (MAX_WIDTH == 0) {
			int width = measureWidth(widthMeasureSpec);
			if (width > 0) {
				MAX_WIDTH = width;
				if (imagesList != null && imagesList.size() > 0) {
					setList(imagesList);
				}
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * Determines the width of this view
	 *
	 * @param measureSpec A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			// result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
			// + getPaddingRight();
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private void initImageLayoutParams() {
		int wrap = LayoutParams.WRAP_CONTENT;
		int match = LayoutParams.MATCH_PARENT;

		onePicPara = new LayoutParams(pxOneMaxWandH, wrap);

		moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
		morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
		morePara.setMargins(pxImagePadding, 0, 0, 0);

		rowPara = new LayoutParams(match, wrap);
	}

	// 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
	private void initView() {
		this.setOrientation(VERTICAL);
		this.removeAllViews();
		if (MAX_WIDTH == 0) {
			//为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
			addView(new View(getContext()));
			return;
		}

		if (imagesList == null || imagesList.size() == 0) {
			return;
		}

		if (imagesList.size() == 1) {
			addView(createImageView(0, false));
		} else {
			int allCount = imagesList.size();
			if (allCount == 4) {
				MAX_PER_ROW_COUNT = 2;
			} else {
				MAX_PER_ROW_COUNT = 3;
			}
			int rowCount = allCount / MAX_PER_ROW_COUNT
					+ (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);// 行数
			for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
				LinearLayout rowLayout = new LinearLayout(getContext());
				rowLayout.setOrientation(LinearLayout.HORIZONTAL);

				rowLayout.setLayoutParams(rowPara);
				if (rowCursor != 0) {
					rowLayout.setPadding(0, pxImagePadding, 0, 0);
				}

				int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT
						: allCount % MAX_PER_ROW_COUNT;//每行的列数
				if (rowCursor != rowCount - 1) {
					columnCount = MAX_PER_ROW_COUNT;
				}
				addView(rowLayout);

				int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
				for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
					int position = columnCursor + rowOffset;
					rowLayout.addView(createImageView(position, true));
				}
			}
		}
	}

	private ImageView createImageView(int position, final boolean isMultiImage) {
		String url = imagesList.get(position);
		ImageView imageView = new ColorFilterImageView(getContext());
		if (position != 0) imageView.setBackgroundColor(Color.parseColor("#000000"));

		if (isMultiImage) {
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
		} else {
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.FIT_START);
			imageView.setMaxHeight(pxOneMaxWandH);
			imageView.setLayoutParams(onePicPara);
		}

		imageView.setTag(R.string.app_name, position);
		imageView.setId(url.hashCode());
		imageView.setOnClickListener(mImageViewOnClickListener);
		imageView.setOnLongClickListener(mImageViewOnLongClickListener);
		GlideUtils.loadImage(getContext(), imageView, url);
		return imageView;
	}

	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}

	public interface OnItemLongClickListener {
		boolean onItemLongClick(View view, int position);
	}
}