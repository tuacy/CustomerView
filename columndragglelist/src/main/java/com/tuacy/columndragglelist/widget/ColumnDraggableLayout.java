package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ColumnDraggableLayout extends LinearLayout{

	public ColumnDraggableLayout(Context context) {
		this(context, null);
	}

	public ColumnDraggableLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColumnDraggableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
}
