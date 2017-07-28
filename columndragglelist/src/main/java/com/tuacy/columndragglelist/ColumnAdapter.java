package com.tuacy.columndragglelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tuacy.columndragglelist.widget.ColumnDraggableBaseAdapter;
import com.tuacy.columndragglelist.widget.ColumnDraggableData;

import java.util.List;

public class ColumnAdapter extends ColumnDraggableBaseAdapter {

	public ColumnAdapter(Context context) {
		super(context);
	}

	public ColumnAdapter(Context context, ColumnDraggableData data) {
		super(context, data);
	}

	@Override
	public float getItemViewHeight() {
		return mContext.getResources().getDimension(R.dimen.item_height);
	}

	@Override
	public View getFixedColumnViewByPosition(float itemHeight, int columnPosition, LinearLayout fixedColumnLayout) {
		return LayoutInflater.from(mContext).inflate(R.layout.item_draggable_fixed_cell, fixedColumnLayout, false);
	}

	@Override
	public View getSlideColumnViewPosition(float itemHeight, int columnPosition, LinearLayout slideColumnLayout) {
		return LayoutInflater.from(mContext).inflate(R.layout.item_draggable_drag_cell, slideColumnLayout, false);
	}

	@Override
	public View convertColumnViewDataByPosition(int columnPosition, View columnView, String columnData, List<String> columnDataList) {
		return null;
	}

}
