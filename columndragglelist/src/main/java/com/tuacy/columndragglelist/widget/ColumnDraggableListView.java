package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.tuacy.columndragglelist.R;

import java.util.ArrayList;
import java.util.List;


public class ColumnDraggableListView extends ListView {

	private static final int TYPE_REFRESH_CONTENT_VIEW   = 0x100;//刷新type
	private static final int TYPE_LOAD_MORE_CONTENT_VIEW = 0x101;//加载更多type
	private static final int SNAP_VELOCITY               = 500;//速度

	private boolean                            mEnableRefresh;
	private RefreshHeader                      mRefreshHeader;
	private boolean                            mEnableLoadMore;
	private Scroller                           mScroller;
	private VelocityTracker                    mVelocityTracker;
	private int                                mTouchSlop;
	private float                              mLastMotionDownX;
	private float                              mLastMotionDownY;
	private float                              mLastMotionX;
	private boolean                            mIsSliding;
	private List<ColumnDraggableSlideListener> mSlideListenerList;
	private DataObserver                       mDataObserver;
	private WrapAdapter                        mWrapAdapter;

	public ColumnDraggableListView(Context context) {
		this(context, null);
	}

	public ColumnDraggableListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColumnDraggableListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData();
	}

	private void initData() {
		mScroller = new Scroller(getContext());
		mEnableRefresh = true;
		mEnableLoadMore = false;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();//大于getScaledTouchSlop这个距离时才认为是触发事件
		mDataObserver = new DataObserver();
		mRefreshHeader = new RefreshHeader(getContext());
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (!(adapter instanceof ColumnDraggableBaseAdapter)) {
			throw new IllegalArgumentException("adapter should abstract ColumnDraggableBaseAdapter");
		}
		addOnSlideListener((ColumnDraggableBaseAdapter) adapter);
		mWrapAdapter = new WrapAdapter((ColumnDraggableBaseAdapter) adapter);//使用内部Adapter包装用户的Adapter
		super.setAdapter(mWrapAdapter);
		adapter.registerDataSetObserver(mDataObserver);//注册Adapter数据监听器
		mDataObserver.onChanged();
	}

	@Override
	public ListAdapter getAdapter() {
		if (mWrapAdapter != null) {
			return mWrapAdapter.mAdapter;
		}
		return null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();//跟踪触摸事件滑动的帮助类
		}
		mVelocityTracker.addMovement(ev);
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				// 首先停止滚动
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
				mIsSliding = false;
				mLastMotionX = x;
				mLastMotionDownX = x;
				mLastMotionDownY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int) Math.abs(x - mLastMotionDownX);
				final int yDiff = (int) Math.abs(y - mLastMotionDownY);
				if (!mIsSliding) {
					if (xDiff > mTouchSlop) {
						mIsSliding = xDiff > yDiff;
					}
				}
				//只有横向的滑动才认为有效
				if (mIsSliding) {
					final int deltaX = (int) (mLastMotionX - x);//滑动的距离
					mLastMotionX = x;
					prepareSlideMove(deltaX);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mIsSliding) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000);//1000毫秒移动了多少像素
					int velocityX = (int) velocityTracker.getXVelocity();//当前的速度
					if (canSlideHorizontal()) {
						if (Math.abs(velocityX) < SNAP_VELOCITY) {
							prepareSlideToColumnEdge();
						} else {
							prepareFling(-velocityX);
						}
					}
					if (mVelocityTracker != null) {
						mVelocityTracker.recycle();
						mVelocityTracker = null;
					}

					mIsSliding = false;
					ev.setAction(MotionEvent.ACTION_CANCEL);
					super.onTouchEvent(ev);
					return true;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				mIsSliding = false;
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				break;
		}
		if (!mIsSliding) {
			super.onTouchEvent(ev);
		}
		return true;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			prepareSlideSet(mScroller.getCurrX());
			postInvalidate();
		}
	}

	/**
	 * 判断是否可以滑动
	 */
	protected boolean canSlideHorizontal() {
		for (int i = 0; i < getChildCount(); i++) {
			View itemView = getChildAt(i);
			ColumnDraggableSlideLayout slideView = (ColumnDraggableSlideLayout) itemView.findViewById(R.id.column_draggable_item_drag_id);
			if (slideView == null) {
				// 这个时候是refresh item 或者 load more item
				continue;
			}
			//最多可以滑动的距离
			int maxScrollX = slideView.getRealityWidth() - slideView.getWidth();
			if (slideView.getScrollX() < maxScrollX && slideView.getScrollX() > 0) {
				return true;
			}
		}
		return false;
	}

	private int getMaxScrollX() {
		for (int i = 0; i < getChildCount(); i++) {
			View itemView = getChildAt(i);
			ColumnDraggableSlideLayout slideView = (ColumnDraggableSlideLayout) itemView.findViewById(R.id.column_draggable_item_drag_id);
			if (slideView == null) {
				// 这个时候是refresh item 或者 load more item
				continue;
			}
			return slideView.getRealityWidth() - slideView.getWidth();
		}
		return 0;
	}

	private int getCurrentScrollX() {
		for (int i = 0; i < getChildCount(); i++) {
			View itemView = getChildAt(i);
			ColumnDraggableSlideLayout slideView = (ColumnDraggableSlideLayout) itemView.findViewById(R.id.column_draggable_item_drag_id);
			if (slideView == null) {
				// 这个时候是refresh item 或者 load more item
				continue;
			}
			return slideView.getScrollX();
		}
		return 0;
	}

	private void prepareSlideSet(int setX) {
		boolean notifyListener = false;
		for (int i = 0; i < getChildCount(); i++) {
			View itemView = getChildAt(i);
			ColumnDraggableSlideLayout slideView = (ColumnDraggableSlideLayout) itemView.findViewById(R.id.column_draggable_item_drag_id);
			if (slideView == null) {
				// 这个时候是refresh item 或者 load more item
				continue;
			}
			//最多可以滑动的距离
			int maxScrollX = slideView.getRealityWidth() - slideView.getWidth();
			if (slideView.getScrollX() <= maxScrollX && slideView.getScrollX() >= 0) {
				slideView.scrollTo(setX, 0);
				//避免有些情况滑到范围之外去
				if (slideView.getScrollX() > maxScrollX) {
					slideView.scrollTo(maxScrollX, 0);
				}
				if (slideView.getScrollX() < 0) {
					slideView.scrollTo(0, 0);
				}
			}
			if (!notifyListener) {
				notifySlideListener(slideView.getScrollX());
				notifyListener = true;
			}
		}
	}

	private void prepareSlideMove(int moveX) {
		boolean notifyListener = false;
		for (int i = 0; i < getChildCount(); i++) {
			View itemView = getChildAt(i);
			ColumnDraggableSlideLayout slideView = (ColumnDraggableSlideLayout) itemView.findViewById(R.id.column_draggable_item_drag_id);
			if (slideView == null) {
				// 这个时候是refresh item 或者 load more item
				continue;
			}
			//最多可以滑动的距离
			int maxScrollX = slideView.getRealityWidth() - slideView.getWidth();
			if (slideView.getScrollX() <= maxScrollX && slideView.getScrollX() >= 0) {
				slideView.scrollBy(moveX, 0);
				//避免有些情况滑到范围之外去
				if (slideView.getScrollX() > maxScrollX) {
					slideView.scrollBy(maxScrollX - slideView.getScrollX(), 0);
				}
				if (slideView.getScrollX() < 0) {
					slideView.scrollBy(-slideView.getScrollX(), 0);
				}
			}
			if (!notifyListener) {
				notifySlideListener(slideView.getScrollX());
				notifyListener = true;
			}
		}
	}

	private void prepareSlideToColumnEdge() {
		//TODO:
	}

	private void prepareFling(int velocityX) {
		mScroller.fling(getCurrentScrollX(), 0, velocityX, 0, 0, getMaxScrollX(), 0, 0);
		invalidate();
	}

	private void notifySlideListener(int setX) {
		if (mSlideListenerList != null && !mSlideListenerList.isEmpty()) {
			for (ColumnDraggableSlideListener listener : mSlideListenerList) {
				listener.onColumnSlideListener(setX);
			}
		}
	}

	public void addOnSlideListener(ColumnDraggableSlideListener listener) {
		if (mSlideListenerList == null) {
			mSlideListenerList = new ArrayList<>();
		}
		mSlideListenerList.add(listener);
	}

	public void removeOnSlideListener(ColumnDraggableSlideListener listener) {
		if (mSlideListenerList != null && mSlideListenerList.contains(listener)) {
			mSlideListenerList.remove(listener);
		}
	}

	private boolean isTop() {
		return mRefreshHeader.getParent() != null;
	}


	private class WrapAdapter extends BaseAdapter {

		private ColumnDraggableBaseAdapter mAdapter;

		WrapAdapter(ColumnDraggableBaseAdapter adapter) {
			mAdapter = adapter;
		}

		@Override
		public int getCount() {
			int count = mAdapter.getCount();
			if (mEnableRefresh) {
				count++;
			}

			if (mEnableLoadMore) {
				count++;
			}
			return count;
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			if (mEnableRefresh && position == 0) {
				return TYPE_REFRESH_CONTENT_VIEW;
			}
			if (mEnableLoadMore && position == getCount() - 1) {
				return TYPE_LOAD_MORE_CONTENT_VIEW;
			}
			return super.getItemViewType(position);
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			if (getItemViewType(i) == TYPE_REFRESH_CONTENT_VIEW) {
				return mRefreshHeader;
			} else if (getItemViewType(i) == TYPE_LOAD_MORE_CONTENT_VIEW) {
				//TODO:
				return null;
			}
			if (view != null && view instanceof RefreshHeader) {
				view = null;
			}
			return mAdapter.getView(mEnableRefresh ? i - 1 : i, view, viewGroup);//其它为自定义Adapter里面的Item类型
		}
	}

	private class DataObserver extends DataSetObserver {//Adapter数据监听器 与 WrapAdapter联动作用


		DataObserver() {
			super();
		}

		@Override
		public void onChanged() {
			if (mWrapAdapter != null) {
				mWrapAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onInvalidated() {
			if (mWrapAdapter != null) {
				mWrapAdapter.notifyDataSetInvalidated();
			}
		}
	}
}
