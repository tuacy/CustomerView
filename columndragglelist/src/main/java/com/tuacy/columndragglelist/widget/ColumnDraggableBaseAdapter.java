package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.tuacy.columndragglelist.R;

import java.util.List;

public abstract class ColumnDraggableBaseAdapter extends BaseAdapter {

	private Context             mContext;
	private ColumnDraggableData mData;

	public ColumnDraggableBaseAdapter(Context context) {
		this(context, null);
	}

	public ColumnDraggableBaseAdapter(Context context, ColumnDraggableData data) {
		mContext = context;
		mData = data;
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
		ColumnDraggableHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_column_draggable_wrap, parent, false);
			//组合每一行的View,包含两部分，一个是固定的LinearLayout,一个是可滑动的LinearLayout
			convertView(getItemViewHeight(), (LinearLayout) convertView.findViewById(R.id.column_draggable_item_fixed_id),
						(LinearLayout) convertView.findViewById(R.id.column_draggable_item_drag_id));
			holder = new ColumnDraggableHolder(convertView, position);
			convertView.setTag(holder);
		} else {
			holder = (ColumnDraggableHolder) convertView.getTag();
			//更新下holder position的位置
			holder.setPosition(position);
		}
		convertData(holder, getItem(position), position);
		return holder.getConvertView();
	}

	public abstract int getItemViewHeight();

	public abstract void convertView(int itemHeight, LinearLayout fixedColumnLayout, LinearLayout dragColumnLayout);

	public abstract void convertData(ColumnDraggableHolder viewHolder, List<String> itemData, int position);
}
