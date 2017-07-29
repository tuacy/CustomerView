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

public abstract class ColumnDraggableBaseAdapter extends BaseAdapter implements ColumnDraggableSlideListener {

	public interface OnDataChangeListener {

		void onAdapterDataChange();
	}

	private   int                  mColumnDraggableScrollTo;
	protected Context              mContext;
	protected ColumnDraggableData  mData;
	protected int                  mDraggableColumnStart;
	private   OnDataChangeListener mOnDataChangeListener;

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
		mColumnDraggableScrollTo = 0;
		notifyDataSetChanged();
	}

	public ColumnDraggableData getData() {
		return mData;
	}

	public void setContent(List<List<String>> content) {
		if (mData == null) {
			mData = new ColumnDraggableData();
		}
		mData.setContent(content);
		notifyDataSetChanged();
	}

	public void appendContent(List<List<String>> content) {
		if (mData == null) {
			mData = new ColumnDraggableData();
		}
		if (mData.getContent() == null) {
			mData.setContent(content);
		} else {
			List<List<String>> history = mData.getContent();
			for (List<String> item : content) {
				history.add(item);
			}
			mData.setContent(history);
		}
		notifyDataSetChanged();

	}

	public void setDraggableColumnStart(int start) {
		mDraggableColumnStart = start;
		mColumnDraggableScrollTo = 0;
		notifyDataSetChanged();
	}

	public void setOnDataChangeListener(OnDataChangeListener listener) {
		mOnDataChangeListener = listener;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		// 检查标题的column的个数和内容的column个数是否对应
		if (mData != null) {
			if (mData.getTitle() != null && mData.getContent() != null) {
				for (List<String> item : mData.getContent()) {
					if (item == null) {
						throw new IllegalArgumentException("invalid content column (one column null)");
					}
					if (item.size() != mData.getTitle().size()) {
						throw new IllegalArgumentException("tile column and content column mismatching");
					}
				}
			}
		}
		if (mOnDataChangeListener != null) {
			mOnDataChangeListener.onAdapterDataChange();
		}
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
			params.height = getItemViewHeight();
			convertView.setLayoutParams(params);
			holder = new ColumnDraggableHolder(convertView, position);
			LinearLayout fixedLayout = (LinearLayout) convertView.findViewById(R.id.column_draggable_item_fixed_id);
			ColumnDraggableSlideLayout slideLayout = (ColumnDraggableSlideLayout) convertView.findViewById(
				R.id.column_draggable_item_drag_id);
			//组合每一行的View,包含两部分，一个是固定的LinearLayout,一个是可滑动的LinearLayout
			if (itemData != null && !itemData.isEmpty()) {
				for (int index = 0; index < itemData.size(); index++) {
					View columnView;
					if (index < mDraggableColumnStart) {
						columnView = getFixedColumnViewByPosition(index, fixedLayout);
						fixedLayout.addView(columnView);
					} else {
						columnView = getSlideColumnViewPosition(index, slideLayout);
						slideLayout.addView(columnView);
					}
					LinearLayout.LayoutParams columnParams = (LinearLayout.LayoutParams) columnView.getLayoutParams();
					columnParams.width = getColumnWidth(index);
					columnView.setLayoutParams(columnParams);
					holder.addColumnView(index, columnView);
				}
			}
			convertView.setTag(holder);
		} else {
			holder = (ColumnDraggableHolder) convertView.getTag();
			//更新下holder position的位置
			holder.setPosition(position);
		}
		//复用的时候不能滑倒指定位置
		ColumnDraggableSlideLayout slideLayout = (ColumnDraggableSlideLayout) holder.getConvertView()
																					.findViewById(R.id.column_draggable_item_drag_id);
		slideLayout.scrollTo(mColumnDraggableScrollTo, 0);

		if (itemData != null && !itemData.isEmpty()) {
			for (int index = 0; index < itemData.size(); index++) {
				if (holder.getColumnView(index) != null) {
					convertColumnViewDataByPosition(index, holder.getColumnView(index), itemData.get(index), itemData);
				}
			}
		}
		return holder.getConvertView();
	}

	@Override
	public void onColumnSlideListener(int setX) {
		mColumnDraggableScrollTo = setX;
	}

	public abstract int getItemViewHeight();

	public abstract int getColumnWidth(int columnIndex);

	public abstract View getFixedColumnViewByPosition(int columnPosition, LinearLayout fixedColumnLayout);

	public abstract View getSlideColumnViewPosition(int columnPosition, ColumnDraggableSlideLayout slideColumnLayout);

	public abstract void convertColumnViewDataByPosition(int columnPosition,
														 View columnView,
														 String columnData,
														 List<String> columnDataList);

}
