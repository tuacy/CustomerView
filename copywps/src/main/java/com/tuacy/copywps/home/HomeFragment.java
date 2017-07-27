package com.tuacy.copywps.home;


import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import com.tuacy.copywps.R;
import com.tuacy.copywps.base.MobileBaseFragment;
import com.tuacy.copywps.widget.VerticalDrawerLayout;

public class HomeFragment extends MobileBaseFragment {

	private VerticalDrawerLayout mDrawerLayout;
	private ImageView mImageSwitch;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_home;
	}

	@Override
	protected void initViews(View root) {
		mDrawerLayout = (VerticalDrawerLayout) root.findViewById(R.id.drawer_layout);
		mImageSwitch = (ImageView) root.findViewById(R.id.image_drawer_switch);
	}

	@Override
	protected void initListeners() {
		mImageSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDrawerLayout.getDrawerState()) {
					mImageSwitch.setImageResource(R.drawable.ic_down_arrow);
				} else {
					mImageSwitch.setImageResource(R.drawable.ic_up_arrow);
				}
				mDrawerLayout.smoothSwitchDrawer();

			}
		});
	}

	@Override
	protected void initData() {

	}
}
