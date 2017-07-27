package com.tuacy.layoutanimation.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;

/**
 * 在RecyclerView上面做一些小的动画
 */

public class AnimationRecyclerView extends RecyclerView {

	public AnimationRecyclerView(Context context) {
		this(context, null);
	}

	public AnimationRecyclerView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AnimationRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
		if (getAdapter() != null && getLayoutManager() != null) {
			if (getLayoutManager() instanceof LinearLayoutManager) {
				GridLayoutAnimationController.AnimationParameters animationParams
					= (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;
				if (animationParams == null) {
					animationParams = new GridLayoutAnimationController.AnimationParameters();
					params.layoutAnimationParameters = animationParams;
				}
				LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
				if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
					animationParams.count = count;
					animationParams.index = index;
					animationParams.columnsCount = count;
					animationParams.rowsCount = 1;

					animationParams.column = index;
					animationParams.row = 1;
				} else {
					animationParams.count = count;
					animationParams.index = index;
					animationParams.columnsCount = 1;
					animationParams.rowsCount = count;

					animationParams.column = 1;
					animationParams.row = index;
				}

			} else if (getLayoutManager() instanceof GridLayoutManager) {
				GridLayoutAnimationController.AnimationParameters animationParams
					= (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;
				if (animationParams == null) {
					animationParams = new GridLayoutAnimationController.AnimationParameters();
					params.layoutAnimationParameters = animationParams;
				}
				GridLayoutManager gridLayoutManager = (GridLayoutManager) getLayoutManager();
				int columns = gridLayoutManager.getSpanCount();
				animationParams.count = count;
				animationParams.index = index;
				animationParams.columnsCount = columns;
				animationParams.rowsCount = count / columns;

				final int invertedIndex = count - 1 - index;
				animationParams.column = columns - 1 - (invertedIndex % columns);
				animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns;
			} else {
				super.attachLayoutAnimationParameters(child, params, index, count);
			}
		} else {
			super.attachLayoutAnimationParameters(child, params, index, count);
		}
	}
}
