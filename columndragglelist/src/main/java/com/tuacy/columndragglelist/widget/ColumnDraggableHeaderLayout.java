package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tuacy.columndragglelist.R;

/**
 * 标题栏布局，会一直存在在顶部,两部分组成一个是不能滑动的一些列，一个是可滑动的一些列
 */
public class ColumnDraggableHeaderLayout extends LinearLayout {

	/**
	 * 不能滑动的列，显示在左边
	 */
	private LinearLayout               mFixedLayout;
	/**
	 * 可滑动的列，显示在右边
	 */
	private ColumnDraggableSlideLayout mSlideLayout;

	public ColumnDraggableHeaderLayout(Context context) {
		this(context, null);
	}

	public ColumnDraggableHeaderLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColumnDraggableHeaderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.item_column_draggable_wrap, this, true);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mFixedLayout = (LinearLayout) findViewById(R.id.column_draggable_item_fixed_id);
		mSlideLayout = (ColumnDraggableSlideLayout) findViewById(R.id.column_draggable_item_drag_id);
	}

	/**
	 * 获取可滑动的列的父布局
	 *
	 * @return 可滑动列的父布局
	 */
	public ColumnDraggableSlideLayout getSlideLayout() {
		return mSlideLayout;
	}

	/**
	 * 获取不可滑动的列的父布局
	 *
	 * @return 不可滑动列的父布局
	 */
	public LinearLayout getFixedLayout() {
		return mFixedLayout;
	}
}
