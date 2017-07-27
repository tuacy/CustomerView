package com.tuacy.copywps.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tuacy.copywps.R;
import com.tuacy.copywps.add.AddDocumentDialog;
import com.tuacy.copywps.base.MobileBaseActivity;
import com.tuacy.copywps.home.HomeFragment;

public class MainActivity extends MobileBaseActivity {

	private HomeFragment      mFragmentHome;
	private AddDocumentDialog mAddDocumentDialog;
	private LinearLayout      mLayoutAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		initData();
	}

	private void initView() {
		mLayoutAdd = findView(R.id.layout_main_tab_add);
	}

	private void initEvent() {
		mLayoutAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAddDocumentDialog.show();
			}
		});
	}

	private void initData() {
		mAddDocumentDialog = new AddDocumentDialog(mContext);
		mFragmentHome = new HomeFragment();
		switchFragment(R.id.layout_main_content, mFragmentHome);
	}

}
