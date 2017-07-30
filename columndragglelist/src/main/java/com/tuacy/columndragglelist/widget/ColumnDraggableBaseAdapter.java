package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.tuacy.columndragglelist.R;

import java.util.List;

public abstract class ColumnDraggableBaseAdapter extends BaseAdapter implements ColumnDraggableSlideListener {

	/**
	 * 数据改变监听
	 */
	public interface OnDataChangeListener {

		void onAdapterDataChange();
	}

	/**
	 * list view 水平滑动到的位置(getView里面convertView不复用的时候要滑动到制定的位置)
	 */
	private   int                  mColumnSlideTo;
	protected Context              mContext;
	private   ColumnDraggableData  mData;
	/**
	 * 从哪个column位置开始可以滑动
	 */
	protected int                  mSlideColumnStart;
	private   OnDataChangeListener mOnDataChangeListener;

	public ColumnDraggableBaseAdapter(Context context) {
		this(context, null);
	}

	public ColumnDraggableBaseAdapter(Context context, ColumnDraggableData data) {
		mContext = context;
		mData = data;
		mSlideColumnStart = 0;
	}

	public void setData(ColumnDraggableData data) {
		mData = data;
		mColumnSlideTo = 0;
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

	public void setSlideColumnStart(int start) {
		mSlideColumnStart = start;
		mColumnSlideTo = 0;
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
			//设置item高度
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
					if (index < mSlideColumnStart) {
						columnView = getFixedColumnView(index, fixedLayout);
						fixedLayout.addView(columnView);
					} else {
						columnView = getSlideColumnView(index, slideLayout);
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
		slideLayout.scrollTo(mColumnSlideTo, 0);

		if (itemData != null && !itemData.isEmpty()) {
			for (int index = 0; index < itemData.size(); index++) {
				if (holder.getColumnView(index) != null) {
					convertColumnViewData(index, holder.getColumnView(index), itemData.get(index), itemData);
				}
			}
		}
		return holder.getConvertView();
	}

	@Override
	public void onColumnSlideListener(int setX) {
		mColumnSlideTo = setX;
	}

	/**
	 * list view每一个item的高度
	 *
	 * @return item height
	 */
	public abstract int getItemViewHeight();

	/**
	 * 行里面，每个column的宽度
	 *
	 * @param columnIndex column下标
	 * @return column 宽度
	 */
	public abstract int getColumnWidth(int columnIndex);

	/**
	 * 获取固定column view
	 *
	 * @param columnIndex       column 下标
	 * @param fixedColumnLayout 固定column的父布局
	 * @return column view
	 */
	public abstract View getFixedColumnView(int columnIndex, LinearLayout fixedColumnLayout);

	/**
	 * 获取可滑动的column view
	 *
	 * @param columnIndex       column 下标
	 * @param slideColumnLayout 可滑动column的父布局
	 * @return column view
	 */
	public abstract View getSlideColumnView(int columnIndex, ColumnDraggableSlideLayout slideColumnLayout);

	/**
	 * 绑定数据
	 *
	 * @param columnIndex    column 下标
	 * @param columnView     column view
	 * @param columnData     adapter data
	 * @param columnDataList column list data
	 */
	public abstract void convertColumnViewData(int columnIndex, View columnView, String columnData, List<String> columnDataList);

}
