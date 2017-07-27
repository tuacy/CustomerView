package com.tuacy.layoutparams.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CustomerSearchView extends LinearLayout {

	public CustomerSearchView(Context context) {
		this(context, null);
	}

	public CustomerSearchView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomerSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
}
