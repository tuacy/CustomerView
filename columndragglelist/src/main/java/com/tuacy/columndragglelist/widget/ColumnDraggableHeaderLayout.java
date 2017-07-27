package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ColumnDraggableHeaderLayout extends LinearLayout {

	private ColumnDraggableLayout   mHeaderView;
	private ColumnDraggableListView mListView;

	public ColumnDraggableHeaderLayout(Context context) {
		this(context, null);
	}

	public ColumnDraggableHeaderLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColumnDraggableHeaderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
}
