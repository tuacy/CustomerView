package com.tuacy.columndragglelist.weight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuacy.columndragglelist.R;
import com.tuacy.columndragglelist.widget.ColumnDraggableBaseAdapter;
import com.tuacy.columndragglelist.widget.ColumnDraggableData;
import com.tuacy.columndragglelist.widget.ColumnDraggableSlideLayout;

import java.util.List;

public class ColumnAdapter extends ColumnDraggableBaseAdapter {

	public ColumnAdapter(Context context) {
		super(context);
	}

	public ColumnAdapter(Context context, ColumnDraggableData data) {
		super(context, data);
	}

	@Override
	public int getItemViewHeight() {
		return (int) mContext.getResources().getDimension(R.dimen.item_height);
	}

	@Override
	public int getColumnWidth(int columnIndex) {
		if (columnIndex < mSlideColumnStart) {
			return (int) mContext.getResources().getDimension(R.dimen.item_fixed_column_width);
		} else {
			return (int) mContext.getResources().getDimension(R.dimen.item_slide_column_width);
		}
	}

	@Override
	public View getFixedColumnView(int columnIndex, LinearLayout fixedColumnLayout) {
		return LayoutInflater.from(mContext).inflate(R.layout.item_draggable_fixed_column_cell, fixedColumnLayout, false);
	}

	@Override
	public View getSlideColumnView(int columnIndex, ColumnDraggableSlideLayout slideColumnLayout) {
		return LayoutInflater.from(mContext).inflate(R.layout.item_draggable_slide_column_cell, slideColumnLayout, false);
	}

	@Override
	public void convertColumnViewData(int columnIndex, View columnView, String columnData, List<String> columnDataList) {
		String columnText = columnDataList.get(columnIndex);
		if (columnIndex < mSlideColumnStart) {
			TextView textView = (TextView) columnView.findViewById(R.id.text_fixed_cell_item);
			textView.setText(columnText);
		} else {
			TextView textView = (TextView) columnView.findViewById(R.id.text_slide_cell_item);
			textView.setText(columnText);
		}
	}

}