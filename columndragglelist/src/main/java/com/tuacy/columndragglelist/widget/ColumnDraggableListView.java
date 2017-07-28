package com.tuacy.columndragglelist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.tuacy.columndragglelist.R;

import java.util.ArrayList;
import java.util.List;


public class ColumnDraggableListView extends ListView {

	private static final int SCROLL_DIRECTION_NONE       = 0;
	private static final int SCROLL_DIRECTION_VERTICAL   = 1;
	private static final int SCROLL_DIRECTION_HORIZONTAL = 2;

	public static final int DIRECTION_NONE  = 0;
	public static final int DIRECTION_LEFT  = 1;
	public static final int DIRECTION_RIGHT = 2;

	private static final int SNAP_VELOCITY = 500;//速度

	private Scroller                           mScroller;
	private VelocityTracker                    mVelocityTracker;
	private int                                mTouchSlop;
	private int                                mScrollDirection;
	private float                              mLastMotionDownX;
	private float                              mLastMotionDownY;
	private float                              mLastMotionX;
	private boolean                            mIsSliding;
	private int                                mDirection;
	private List<ColumnDraggableSlideListener> mSlideListenerList;

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
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();//大于getScaledTouchSlop这个距离时才认为是触发事件
		mScrollDirection = SCROLL_DIRECTION_NONE;
		mDirection = DIRECTION_NONE;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if (!(adapter instanceof ColumnDraggableBaseAdapter)) {
			throw new IllegalArgumentException("adapter should abstract ColumnDraggableBaseAdapter");
		}
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
				mScrollDirection = SCROLL_DIRECTION_NONE;
				mLastMotionX = x;
				mLastMotionDownX = x;
				mLastMotionDownY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int) Math.abs(x - mLastMotionDownX);
				final int yDiff = (int) Math.abs(y - mLastMotionDownY);
				if (mScrollDirection == SCROLL_DIRECTION_NONE) {
					if (xDiff > mTouchSlop) {
						if (xDiff > yDiff) {
							mScrollDirection = SCROLL_DIRECTION_HORIZONTAL;
						} else {
							mScrollDirection = SCROLL_DIRECTION_VERTICAL;
						}
					}
				}
				//只有横向的滑动才认为有效
				mIsSliding = mScrollDirection == SCROLL_DIRECTION_HORIZONTAL;
				if (mIsSliding) {
					if (x > mLastMotionX) {
						mDirection = DIRECTION_RIGHT;
					} else {
						mDirection = DIRECTION_LEFT;
					}
					final int deltaX = (int) (mLastMotionX - x);//滑动的距离
					mLastMotionX = x;
					prepareSlide(deltaX);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mIsSliding) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000);//1000毫秒移动了多少像素
					int velocityX = (int) velocityTracker.getXVelocity();//当前的速度
					if (canScrollHorizontal()) {
						if (Math.abs(velocityX) < SNAP_VELOCITY) {
							//TODO:
						} else {
							//TODO:
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
				mScrollDirection = SCROLL_DIRECTION_NONE;
				break;
		}
		if (!mIsSliding) {
			super.onTouchEvent(ev);
		}
		return true;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
	}

	protected boolean canScrollHorizontal() {
		//TODO:
		return true;
	}

	private void prepareSlide(int deltaX) {
		int count = getChildCount();

		for (int i = 0; i < count; i++) {
			View itemView = getChildAt(i);
			ColumnDraggableSlideLayout slideView = (ColumnDraggableSlideLayout) itemView.findViewById(R.id.column_draggable_item_drag_id);
			//最多可以滑动的距离
			int maxScrollX = slideView.getRealityWidth() - slideView.getWidth();
			if (slideView.getScrollX() <= maxScrollX && slideView.getScrollX() >= 0) {
				slideView.scrollBy(deltaX, 0);
				//避免有些情况滑到范围之外去
				if (slideView.getScrollX() > maxScrollX) {
					slideView.scrollBy(maxScrollX - slideView.getScrollX(), 0);
				}
				if (slideView.getScrollX() < 0) {
					slideView.scrollBy(-slideView.getScrollX(), 0);
				}
			}
		}
	}

	private void notifySlideListener(int byX) {
		if (mSlideListenerList != null && !mSlideListenerList.isEmpty()) {
			for (ColumnDraggableSlideListener listener : mSlideListenerList) {
				listener.onColumnSlideListener(byX);
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
}
