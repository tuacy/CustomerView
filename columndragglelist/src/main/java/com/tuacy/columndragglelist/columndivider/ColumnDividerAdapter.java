package com.tuacy.columndragglelist.columndivider;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuacy.columndragglelist.R;
import com.tuacy.columndragglelist.widget.ColumnDraggableBaseAdapter;
import com.tuacy.columndragglelist.widget.ColumnDraggableData;
import com.tuacy.columndragglelist.widget.ColumnDraggableSlideLayout;

import java.util.List;

public class ColumnDividerAdapter extends ColumnDraggableBaseAdapter {

	public ColumnDividerAdapter(Context context) {
		this(context, null);
	}

	public ColumnDividerAdapter(Context context, ColumnDraggableData data) {
		super(context, data);
	}

	@Override
	public int getItemViewHeight() {
		return (int) mContext.getResources().getDimension(R.dimen.item_height);
	}


	@Override
	public LinearLayout.LayoutParams getColumnWidth(int position, int columnIndex, int columnCount) {
		if (columnIndex < mSlideColumnStart) {
			return new LinearLayout.LayoutParams((int) mContext.getResources().getDimension(R.dimen.item_fixed_column_width),
												 ViewGroup.LayoutParams.MATCH_PARENT);
		} else {
			return new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

		}
	}


	@Override
	public View getFixedColumnView(int position, int columnIndex, int columnCount, LinearLayout fixedColumnLayout) {
		return LayoutInflater.from(mContext).inflate(R.layout.item_draggable_fixed_column_cell, fixedColumnLayout, false);
	}

	@Override
	public View getSlideColumnView(int position, int columnIndex, int columnCount, ColumnDraggableSlideLayout slideColumnLayout) {
		return LayoutInflater.from(mContext).inflate(R.layout.item_draggable_slide_column_cell, slideColumnLayout, false);
	}

	@Override
	public void convertColumnViewData(int position,
									  int columnIndex,
									  View columnView,
									  View rowView,
									  String columnData,
									  List<String> columnDataList) {
		String columnText = columnDataList.get(columnIndex);
		if (columnIndex < mSlideColumnStart) {
			TextView textView = (TextView) columnView.findViewById(R.id.text_fixed_cell_item);
			textView.setText(columnText);
		} else {
			TextView textView = (TextView) columnView.findViewById(R.id.text_slide_cell_item);
			textView.setText(columnText);
		}

		if (columnIndex < mSlideColumnStart) {
			columnView.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
		} else {
			if ((columnIndex - mSlideColumnStart) % 2 == 0) {
				columnView.setBackgroundColor(mContext.getResources().getColor(R.color.color_radix));
			} else {
				columnView.setBackgroundColor(mContext.getResources().getColor(R.color.color_even));
			}
		}


	}
}
