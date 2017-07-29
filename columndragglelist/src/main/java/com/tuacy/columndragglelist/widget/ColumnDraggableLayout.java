package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuacy.columndragglelist.R;

import java.util.List;


public class ColumnDraggableLayout extends LinearLayout
	implements ColumnDraggableBaseAdapter.OnDataChangeListener, ColumnDraggableSlideListener {

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
	private int                         mTileHeight;
	private List<String>                mTitle;

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
		initData();
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
		mTileHeight = attributes.getDimensionPixelOffset(R.styleable.ColumnDraggableLayout_column_title_height, dip2px(getContext(), 32));
		attributes.recycle();
	}

	private void initData() {

	}

	private void reloadTitleView() {
		if (mTitle == null || mTitle.isEmpty()) {
			mHeaderView.setVisibility(GONE);
		} else {
			mHeaderView.setVisibility(VISIBLE);
			mHeaderView.getFixedLayout().removeAllViews();
			mHeaderView.getSlideLayout().removeAllViews();
			for (int index = 0; index < mTitle.size(); index++) {
				TextView textView = new TextView(mHeaderView.getFixedLayout().getContext());
				LinearLayout.LayoutParams params = new LayoutParams(mAdapter.getColumnWidth(index), mTileHeight);
				textView.setLayoutParams(params);
				textView.setGravity(Gravity.CENTER);
				textView.setText(mTitle.get(index));
				textView.setBackgroundResource(R.drawable.shader_title_column);
				if (index < mDraggableColumnStart) {
					mHeaderView.getFixedLayout().addView(textView);
				} else {
					mHeaderView.getSlideLayout().addView(textView);
				}
			}
		}
	}

	private void setTitle(List<String> title) {
		if (mTitle == null) {
			mTitle = title;
			reloadTitleView();
		} else {
			if (title == null) {
				mTitle = null;
				reloadTitleView();
			} else {
				if (mTitle.size() != title.size()) {
					mTitle = title;
					reloadTitleView();
				} else {
					boolean change = false;
					for (int index = 0; index < mTitle.size(); index++) {
						if (!mTitle.get(index).equals(title.get(index))) {
							change = true;
							reloadTitleView();
						}
					}
					if (change) {
						mTitle = title;
						reloadTitleView();
					}
				}
			}
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mHeaderView = (ColumnDraggableHeaderLayout) findViewById(R.id.column_draggable_header_id);
		LinearLayout.LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.height = mTileHeight;
		mHeaderView.setLayoutParams(params);
		mHeaderView.setVisibility(GONE);
		mListView = (ColumnDraggableListView) findViewById(R.id.column_draggable_list_id);
		mListView.addOnSlideListener(this);
	}

	public void setAdapter(ColumnDraggableBaseAdapter adapter) {
		mAdapter = adapter;
		if (mAdapter != null) {
			mAdapter.setSlideColumnStart(mDraggableColumnStart);
			mAdapter.setOnDataChangeListener(this);
		}
		mListView.setAdapter(adapter);
		onAdapterDataChange();
	}

	public ColumnDraggableBaseAdapter getAdapter() {
		return mAdapter;
	}


	@Override
	public void onAdapterDataChange() {
		ColumnDraggableData data = mAdapter.getData();
		if (data == null) {
			setTitle(null);
		} else {
			setTitle(data.getTitle());
		}
	}

	private static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onColumnSlideListener(int setX) {
		mHeaderView.getSlideLayout().scrollTo(setX, 0);
	}
}
