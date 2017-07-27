package com.tuacy.copywps.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tuacy.copywps.R;



public class VerticalDrawerLayout extends ViewGroup {

	private View    mViewBottom;
	private View    mViewTop;
	private float   mFaceRadio;
	private int     mDistanceMax;
	private int     mDistanceCurrent;
	private boolean mOpen;

	public VerticalDrawerLayout(Context context) {
		this(context, null);
	}

	public VerticalDrawerLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VerticalDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttrs(context, attrs);
		mDistanceCurrent = 0;
		mOpen = false;
	}

	/**
	 * 自定义属性的获取
	 */
	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.VerticalDrawerLayout);
		mFaceRadio = attributes.getFloat(R.styleable.VerticalDrawerLayout_drawer_face_ratio, 1 / 3f);
		attributes.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//测量底部的view 底下的View保持原来的大小
		measureChild(mViewBottom, widthMeasureSpec, heightMeasureSpec);
		//测量上部的view
		int parentHeight = getMeasuredHeight();
		int bottomViewHeight = mViewBottom.getMeasuredHeight();
		int topViewHeight = (int) (parentHeight - bottomViewHeight * mFaceRadio);
		mDistanceMax = (int) (bottomViewHeight * (1 - mFaceRadio));
		mViewTop.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(topViewHeight, MeasureSpec.EXACTLY));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mViewBottom.layout(l, t, l + mViewBottom.getMeasuredWidth(), t + mViewBottom.getMeasuredHeight());
		int topViewInitTop = (int) (mViewBottom.getMeasuredHeight() * mFaceRadio);
		mViewTop.layout(l, t + topViewInitTop + mDistanceCurrent, l + mViewTop.getMeasuredWidth(),
						t + topViewInitTop + mDistanceCurrent + mViewTop.getMeasuredHeight());
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() != 2) {
			throw new IllegalArgumentException("VerticalDrawerLayout child count should be 2");
		}
		mViewBottom = getChildAt(0);
		mViewTop = getChildAt(1);
	}

	private void setDrawerDistance(int distance) {
		mDistanceCurrent = distance;
		requestLayout();
	}

	public boolean getDrawerState() {
		return mOpen;
	}

	public void smoothSwitchDrawer() {
		ValueAnimator valueAnimator;
		if (mOpen) {
			valueAnimator = ValueAnimator.ofInt(mDistanceMax, 0);
		} else {
			valueAnimator = ValueAnimator.ofInt(0, mDistanceMax);
		}
		valueAnimator.setDuration(getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				setDrawerDistance((Integer) animation.getAnimatedValue());
			}
		});
		valueAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mOpen = !mOpen;
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		valueAnimator.start();
	}

}
