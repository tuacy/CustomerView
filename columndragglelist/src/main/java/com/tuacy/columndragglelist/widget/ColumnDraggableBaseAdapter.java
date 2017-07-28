package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.tuacy.columndragglelist.R;

import java.util.List;

public abstract class ColumnDraggableBaseAdapter extends BaseAdapter {

	protected Context             mContext;
	protected ColumnDraggableData mData;
	protected int                 mDraggableColumnStart;

	public ColumnDraggableBaseAdapter(Context context) {
		this(context, null);
	}

	public ColumnDraggableBaseAdapter(Context context, ColumnDraggableData data) {
		mContext = context;
		mData = data;
		mDraggableColumnStart = 0;
	}

	public void setData(ColumnDraggableData data) {
		mData = data;
		notifyDataSetChanged();
	}

	public void setDraggableColumnStart(int start) {
		mDraggableColumnStart = start;
	}

	@Override
	public int getCount() {
		if (mData == null || mData.getContent() == null) {
			return 0;
		}
		return mData.getContent().size();
	}

	@Override
	public List<String> getItem(int position) {
		return mData.getContent().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		List<String> itemData = getItem(position);
		ColumnDraggableHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_column_draggable_wrap, parent, false);
			AbsListView.LayoutParams params = (AbsListView.LayoutParams) convertView.getLayoutParams();
			params.height = (int) getItemViewHeight();
			convertView.setLayoutParams(params);
			holder = new ColumnDraggableHolder(convertView, position);
			LinearLayout fixedLayout = (LinearLayout) convertView.findViewById(R.id.column_draggable_item_fixed_id);
			LinearLayout slideLayout = (LinearLayout) convertView.findViewById(R.id.column_draggable_item_drag_id);
			//组合每一行的View,包含两部分，一个是固定的LinearLayout,一个是可滑动的LinearLayout
			if (itemData != null && !itemData.isEmpty()) {
				for (int index = 0; index < itemData.size(); index++) {
					View columnView;
					if (index < mDraggableColumnStart) {
						columnView = getFixedColumnViewByPosition(getItemViewHeight(), index, fixedLayout);
						fixedLayout.addView(columnView);
					} else {
						columnView = getSlideColumnViewPosition(getItemViewHeight(), index, slideLayout);
						slideLayout.addView(columnView);
					}
					holder.addColumnView(index, columnView);
				}
			}
			convertView.setTag(holder);
		} else {
			holder = (ColumnDraggableHolder) convertView.getTag();
			//更新下holder position的位置
			holder.setPosition(position);
		}
		if (itemData != null && !itemData.isEmpty()) {
			for (int index = 0; index < itemData.size(); index++) {
				if (holder.getColumnView(index) != null) {
					convertColumnViewDataByPosition(index, holder.getColumnView(index), itemData.get(index), itemData);
				}
			}
		}
		return holder.getConvertView();
	}

	public abstract float getItemViewHeight();

	public abstract View getFixedColumnViewByPosition(float itemHeight, int columnPosition, LinearLayout fixedColumnLayout);

	public abstract View getSlideColumnViewPosition(float itemHeight, int columnPosition, LinearLayout slideColumnLayout);

	public abstract View convertColumnViewDataByPosition(int columnPosition,
														 View columnView,
														 String columnData,
														 List<String> columnDataList);

}
