package com.tuacy.columndragglelist.widget;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ColumnDraggableHolder {

	private Map<String, Object> mMap         = null;
	private SparseArray<View>   mViews       = null;
	private View                mConvertView = null;
	private int                 mPosition    = -1;

	public ColumnDraggableHolder(View contentView, int position) {
		mConvertView = contentView;
		mPosition = position;
		mViews = new SparseArray<>();
	}

	/**
	 * get view by view id
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (null == view) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
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

	public ColumnDraggableHolder setTag(int viewId, final Object tag) {
		View view = getView(viewId);
		view.setTag(tag);
		return this;
	}

	public Object getTag(int viewId) {
		View view = getView(viewId);
		return view.getTag();
	}

	public void setMap(String key, Object value) {
		if (mMap == null) {
			mMap = new HashMap<String, Object>();
		}
		mMap.put(key, value);
	}

	public Object getMap(String key) {
		return mMap == null ? null : mMap.get(key);
	}

	public ColumnDraggableHolder setPadding(int viewId, int left, int top, int right, int bottom) {
		View view = getView(viewId);
		view.setPadding(left, top, right, bottom);
		return this;
	}

	public ColumnDraggableHolder setVisibility(int viewId, int visibility) {
		View view = getView(viewId);
		view.setVisibility(visibility);
		return this;
	}

	/**
	 * TextView
	 */
	public ColumnDraggableHolder setText(int viewId, String text) {
		TextView textView = getView(viewId);
		textView.setText(text);
		return this;
	}

	public ColumnDraggableHolder setText(int viewId, Integer resourceId) {
		TextView textView = getView(viewId);
		textView.setText(resourceId);
		return this;
	}

	public CharSequence getText(int viewId) {
		TextView textView = getView(viewId);
		return textView.getText();
	}

	/**
	 * ImageView
	 */
	public ColumnDraggableHolder setImageResource(int viewId, Integer resourceId) {
		ImageView imageView = getView(viewId);
		imageView.setImageResource(resourceId);
		return this;
	}

	public ColumnDraggableHolder setBackgroundResource(int viewId, Integer resourceId) {
		ImageView imageView = getView(viewId);
		imageView.setBackgroundResource(resourceId);
		return this;
	}
}
