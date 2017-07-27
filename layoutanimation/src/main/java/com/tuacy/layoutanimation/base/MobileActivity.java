package com.tuacy.layoutanimation.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MobileActivity extends AppCompatActivity {

	protected Context mContext;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}
}
