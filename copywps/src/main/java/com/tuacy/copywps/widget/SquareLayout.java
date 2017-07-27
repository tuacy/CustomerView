package com.tuacy.copywps.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tuacy.copywps.R;


/**
 * 实现的功能：
 */

public class SquareLayout extends ViewGroup {

	private int mRow;
	private int mColumn;

	public SquareLayout(Context context) {
		this(context, null);
	}

	public SquareLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttrs(context, attrs);
	}

	/**
	 * 自定义属性的获取
	 */
	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SquareLayout);
		mRow = attributes.getInt(R.styleable.SquareLayout_square_row, 1);
		mColumn = attributes.getInt(R.styleable.SquareLayout_square_column, 2);
		attributes.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//对于SquareLayout我们肯定是要得到确切的宽度和高度的
		int widthValid = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		int heightValid = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
		int eachChildWidth = widthValid / mColumn;
		int eachChildHeight = heightValid / mRow;
		int visibilityCount = 0;
		for (int index = 0; index < getChildCount(); index++) {
			View child = this.getChildAt(index);
			if (child.getVisibility() == GONE) {
				continue;
			}
			visibilityCount++;
		}
		if (visibilityCount > mRow * mColumn) {
			throw new IllegalArgumentException("view visibility count more then row * column");
		}
		for (int index = 0; index < getChildCount(); index++) {
			View child = this.getChildAt(index);
			if (child.getVisibility() == GONE) {
				continue;
			}
			measureChildWithMargins(child, MeasureSpec.makeMeasureSpec(eachChildWidth, MeasureSpec.EXACTLY), 0,
									MeasureSpec.makeMeasureSpec(eachChildHeight, MeasureSpec.EXACTLY), 0);
		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int widthValid = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
			int heightValid = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
			int eachChildWidth = widthValid / mColumn;
			int eachChildHeight = heightValid / mRow;
			int visibilityIndex = 0;
			for (int index = 0; index < getChildCount(); index++) {
				View child = this.getChildAt(index);
				if (child.getVisibility() == GONE) {
					continue;
				}
				LayoutParams params = (LayoutParams) child.getLayoutParams();
				int childRow = visibilityIndex / mColumn;
				int childColumn = visibilityIndex % mColumn;
				int left = getPaddingLeft() + childColumn * eachChildWidth + (eachChildWidth - child.getMeasuredWidth()) / 2 -
						   params.leftMargin;
				int right = left + params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
				int top = getPaddingTop() + childRow * eachChildHeight + (eachChildHeight - child.getMeasuredHeight()) / 2 +
						  params.topMargin;
				int bottom = top + params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
				child.layout(left, top, right, bottom);
				visibilityIndex++;
			}
		}
	}

	@Override
	public SquareLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new SquareLayout.LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof SquareLayout.LayoutParams;
	}

	/**
	 * 继承MarginLayoutParams
	 */
	static class LayoutParams extends MarginLayoutParams {

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}
	}
}
