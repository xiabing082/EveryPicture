package com.imuhao.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by dafan on 2016/10/13 0013.
 */

public class AutoHeightGridView extends GridView {
	public AutoHeightGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoHeightGridView(Context context) {
		super(context);
	}

	public AutoHeightGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
