package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


public class ColumnDraggableListView extends ListView {

	public ColumnDraggableListView(Context context) {
		this(context, null);
	}

	public ColumnDraggableListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColumnDraggableListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
}
