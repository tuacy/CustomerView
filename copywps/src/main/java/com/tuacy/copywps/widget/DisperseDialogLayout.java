package com.tuacy.copywps.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuacy.copywps.R;


public class DisperseDialogLayout extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {

	private LinearLayout mLayoutText;
	private LinearLayout mLayoutDisplay;
	private LinearLayout mLayoutTable;
	private TextView     mTextText;
	private TextView     mTextDisplay;
	private TextView     mTextTable;
	private ImageView    mImageShow;
	private boolean      isOpen;
	private OnDisperseListener mListener;

	@Override
	public void onGlobalLayout() {
		getViewTreeObserver().removeOnGlobalLayoutListener(this);
		show();
	}

	public interface OnDisperseListener {

		void onOpen();

		void onClose();
	}

	public DisperseDialogLayout(Context context) {
		this(context, null);
	}

	public DisperseDialogLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DisperseDialogLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
		initEvent();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.disperser_layout, this, true);
		mLayoutText = (LinearLayout) findViewById(R.id.layout_text);
		mLayoutDisplay = (LinearLayout) findViewById(R.id.layout_display);
		mLayoutTable = (LinearLayout) findViewById(R.id.layout_table);
		mTextText = (TextView) findViewById(R.id.text_text);
		mTextDisplay = (TextView) findViewById(R.id.text_display);
		mTextTable = (TextView) findViewById(R.id.text_table);
		mImageShow = (ImageView) findViewById(R.id.image_show);
		isOpen = false;
	}

	private void initEvent() {
		mImageShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isOpen) {
					dismiss();
				} else {
					show();
				}
			}
		});

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isOpen) {
					dismiss();
				}
			}
		});
	}

	public void show() {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int translateXDistance = width / 4;
		int translateYDistance = (int) (height * 0.2);

		//左边
		PropertyValuesHolder leftX = PropertyValuesHolder.ofFloat("TranslationX", 0f, -translateXDistance);
		PropertyValuesHolder leftY = PropertyValuesHolder.ofFloat("TranslationY", 0f, -translateYDistance);
		//中间
		PropertyValuesHolder centerY = PropertyValuesHolder.ofFloat("TranslationY", 0f, -translateYDistance);
		//右边
		PropertyValuesHolder rightX = PropertyValuesHolder.ofFloat("TranslationX", 0f, translateXDistance);
		PropertyValuesHolder rightY = PropertyValuesHolder.ofFloat("TranslationY", 0f, -translateYDistance);

		ObjectAnimator leftAnimator = ObjectAnimator.ofPropertyValuesHolder(mLayoutText, leftX, leftY);
		ObjectAnimator centerAnimator = ObjectAnimator.ofPropertyValuesHolder(mLayoutDisplay, centerY);
		ObjectAnimator rightAnimator = ObjectAnimator.ofPropertyValuesHolder(mLayoutTable, rightX, rightY);
		//添加自由落体效果插值器
		leftAnimator.setInterpolator(new OvershootInterpolator());
		centerAnimator.setInterpolator(new OvershootInterpolator());
		rightAnimator.setInterpolator(new OvershootInterpolator());

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(leftAnimator).with(centerAnimator).with(rightAnimator);
		animatorSet.setDuration(getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));
		animatorSet.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mTextText.setVisibility(VISIBLE);
				mTextDisplay.setVisibility(VISIBLE);
				mTextTable.setVisibility(VISIBLE);
				isOpen = true;
				if (mListener != null) {
					mListener.onOpen();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		animatorSet.start();

	}

	public void dismiss() {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int translateXDistance = width / 4;
		int translateYDistance = (int) (height * 0.2);

		mTextText.setVisibility(GONE);
		mTextDisplay.setVisibility(GONE);
		mTextTable.setVisibility(GONE);

		//左边
		PropertyValuesHolder leftX = PropertyValuesHolder.ofFloat("TranslationX", -translateXDistance, 0f);
		PropertyValuesHolder leftY = PropertyValuesHolder.ofFloat("TranslationY", -translateYDistance, 0f);
		//中间
		PropertyValuesHolder centerY = PropertyValuesHolder.ofFloat("TranslationY", -translateYDistance, 0f);
		//右边
		PropertyValuesHolder rightX = PropertyValuesHolder.ofFloat("TranslationX", translateXDistance, 0f);
		PropertyValuesHolder rightY = PropertyValuesHolder.ofFloat("TranslationY", -translateYDistance, 0f);

		ObjectAnimator leftAnimator = ObjectAnimator.ofPropertyValuesHolder(mLayoutText, leftX, leftY);
		ObjectAnimator centerAnimator = ObjectAnimator.ofPropertyValuesHolder(mLayoutDisplay, centerY);
		ObjectAnimator rightAnimator = ObjectAnimator.ofPropertyValuesHolder(mLayoutTable, rightX, rightY);
		//添加自由落体效果插值器
		leftAnimator.setInterpolator(new LinearInterpolator());
		centerAnimator.setInterpolator(new LinearInterpolator());
		rightAnimator.setInterpolator(new LinearInterpolator());

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(leftAnimator).with(centerAnimator).with(rightAnimator);
		animatorSet.setDuration(getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));
		animatorSet.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				isOpen = false;
				if (mListener != null) {
					mListener.onClose();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		animatorSet.start();
	}

	public void setOnDisperseListener(OnDisperseListener listener) {
		mListener = listener;
	}
}
