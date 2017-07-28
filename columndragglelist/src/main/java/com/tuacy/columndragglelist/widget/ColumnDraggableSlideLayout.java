package com.tuacy.columndragglelist.widget;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
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

	public int slideToColumnDdgeByX(int direction) {
		int moveX = 0;
		int scrollX = getScrollX();
		int hideWidth = 0;
		int flagViewPosition = -1;
		for (int index = 0; index < getChildCount(); index++) {
			hideWidth += getChildAt(index).getWidth();
			if (hideWidth > scrollX) {
				flagViewPosition = index;
				break;
			}
		}
		if (flagViewPosition != -1) {
			int distance = hideWidth - scrollX;
			View flagView = getChildAt(flagViewPosition);
			if (distance > flagView.getWidth() / 2) {
				moveX = -(flagView.getWidth() - direction);
			} else {
				moveX = distance;
			}
		}
		return moveX;
	}
}
