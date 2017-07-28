package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tuacy.columndragglelist.R;

public class ColumnDraggableLayout extends LinearLayout {

	/**
	 * 头部布局
	 */
	private ColumnDraggableHeaderLayout mHeaderView;
	/**
	 * list view
	 */
	private ColumnDraggableListView     mListView;
	/**
	 * list view adapter
	 */
	private ColumnDraggableBaseAdapter  mAdapter;
	/**
	 * 从哪一列开始可以滑动
	 */
	private int                         mDraggableColumnStart;

	public ColumnDraggableLayout(Context context) {
		this(context, null);
	}

	public ColumnDraggableLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColumnDraggableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttrs(context, attrs);
		initView();
	}

	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.layout_draggable_wrap, this, true);
	}

	/**
	 * 自定义属性的获取
	 */
	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ColumnDraggableLayout);
		mDraggableColumnStart = attributes.getInt(R.styleable.ColumnDraggableLayout_column_draggable_start, 0);
		attributes.recycle();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mHeaderView = (ColumnDraggableHeaderLayout) findViewById(R.id.column_draggable_header_id);
		mListView = (ColumnDraggableListView) findViewById(R.id.column_draggable_list_id);
	}

	public void setAdapter(ColumnDraggableBaseAdapter adapter) {
		mAdapter = adapter;
		if (mAdapter != null) {
			adapter.setDraggableColumnStart(mDraggableColumnStart);
		}
		mListView.setAdapter(adapter);
	}

	public ColumnDraggableBaseAdapter getAdapter() {
		return mAdapter;
	}
}
