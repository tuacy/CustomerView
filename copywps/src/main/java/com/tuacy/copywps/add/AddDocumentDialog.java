package com.tuacy.copywps.add;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;

import com.tuacy.copywps.R;
import com.tuacy.copywps.widget.DisperseDialogLayout;


public class AddDocumentDialog extends Dialog {

	public AddDocumentDialog(Context context) {
		super(context, R.style.NoTitleDialog);
		initView();
	}

	private void initView() {
		View parentView = View.inflate(getContext(), R.layout.dialog_waitting, null);
		setContentView(parentView);
		setCanceledOnTouchOutside(true);
		DisperseDialogLayout disperseDialogLayout = (DisperseDialogLayout) parentView.findViewById(R.id.layout_disperse);
		disperseDialogLayout.setOnDisperseListener(new DisperseDialogLayout.OnDisperseListener() {
			@Override
			public void onOpen() {

			}

			@Override
			public void onClose() {
				dismiss();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
	}
}
