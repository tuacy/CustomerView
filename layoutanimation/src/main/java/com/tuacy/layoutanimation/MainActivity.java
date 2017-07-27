package com.tuacy.layoutanimation;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.tuacy.layoutanimation.base.MobileActivity;
import com.tuacy.layoutanimation.widget.AnimationRecyclerView;

public class MainActivity extends MobileActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		initData();
	}

	private void initView() {
		AnimationRecyclerView recyclerView = findView(R.id.recycler_view);
		recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
//		recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		recyclerView.setAdapter(new ImageAdapter(mContext));
	}

	private void initEvent() {

	}

	private void initData() {

	}
}
