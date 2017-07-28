package com.tuacy.columndragglelist.widget;

import android.util.SparseArray;
import android.view.View;

public class ColumnDraggableHolder {

	private View                mConvertView = null;
	private int                 mPosition    = -1;
	private SparseArray<View>   mColumnViews = null;

	public ColumnDraggableHolder(View contentView, int position) {
		mConvertView = contentView;
		mPosition = position;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	/**
	 * 记录每一列的View
	 *
	 * @param columnPosition 列的位置
	 * @param view           列的View
	 */
	public void addColumnView(int columnPosition, View view) {
		if (null == mColumnViews) {
			mColumnViews = new SparseArray<>();
		}
		mColumnViews.put(columnPosition, view);
	}

	/**
	 * 获取指定列的View
	 *
	 * @param columnPosition 列的位置
	 * @return 列的View
	 */
	public View getColumnView(int columnPosition) {
		if (null == mColumnViews) {
			return null;
		}
		return mColumnViews.get(columnPosition);
	}
}
