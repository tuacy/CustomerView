package com.tuacy.layoutanimation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

	private Context mContext;

	private int mSource[] = {R.drawable.one,
							 R.drawable.two,
							 R.drawable.three,
							 R.drawable.four,
							 R.drawable.five,
							 R.drawable.six};

	public ImageAdapter(Context context) {
		mContext = context;
	}

	@Override
	public ImageAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false));
	}

	@Override
	public void onBindViewHolder(ImageAdapter.ImageViewHolder holder, int position) {
		holder.mImageView.setImageResource(mSource[position % mSource.length]);
	}

	@Override
	public int getItemCount() {
		return 50;
	}

	class ImageViewHolder extends RecyclerView.ViewHolder {

		ImageView mImageView;

		ImageViewHolder(View view) {
			super(view);
			mImageView = (ImageView) view.findViewById(R.id.item_image_view);
		}
	}
}
