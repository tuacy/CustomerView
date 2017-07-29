package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tuacy.columndragglelist.R;

public class ColumnDraggableHeaderLayout extends LinearLayout {

	private LinearLayout               mFixedLayout;
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

	public ColumnDraggableSlideLayout getSlideLayout() {
		return mSlideLayout;
	}

	public LinearLayout getFixedLayout() {
		return mFixedLayout;
	}
}
