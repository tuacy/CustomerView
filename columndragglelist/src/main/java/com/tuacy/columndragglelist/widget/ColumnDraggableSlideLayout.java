package com.tuacy.columndragglelist.widget;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class ColumnDraggableSlideLayout extends LinearLayout {

	public ColumnDraggableSlideLayout(Context context) {
		this(context, null);
	}

	public ColumnDraggableSlideLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColumnDraggableSlideLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	/**
	 * 获取所有view的总宽度
	 *
	 * @return 宽度
	 */
	public int getRealityWidth() {
		int realityWidth = 0;
		for (int index = 0; index < getChildCount(); index++) {
			realityWidth += getChildAt(index).getWidth();
		}
		return realityWidth;
	}

	public int smoothToColumnDdgeByX(int direction) {
		int scrollX = getScrollX();
		for (int index = 0; index < getChildCount(); index++) {

		}
		return 0;
	}
}
