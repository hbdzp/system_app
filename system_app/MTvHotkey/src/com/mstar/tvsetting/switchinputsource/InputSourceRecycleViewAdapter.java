package com.mstar.tvsetting.switchinputsource;

import java.util.List;

import com.mstar.tvsetting.R;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
public class InputSourceRecycleViewAdapter extends RecyclerView.Adapter<InputSourceRecycleViewAdapter.ViewHolder> {
	private String[] mTitles;
	private int[] mImageResources;
	private LayoutInflater mLayoutInflater;
	public OnItemClickListener mOnItemClickListener;
	public OnItemFocusChangeListener mOnItemFocusChangeListener;

	public InputSourceRecycleViewAdapter(Context context, String[] titleDatas,int[] imageRess) {
		mLayoutInflater = LayoutInflater.from(context);
		mTitles = titleDatas;
		mImageResources=imageRess;
	}
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
	public void setItemFocusChangeListener(OnItemFocusChangeListener onItemFocusChangeListener) {
        this.mOnItemFocusChangeListener = onItemFocusChangeListener;
    }
    public interface OnItemClickListener {
	        void onItemClick(View view, int position);
	}
    public interface OnItemFocusChangeListener {
        void onItemFocusChange(TextView text,ImageView image, int position,boolean hasFocus);
    }
    
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,OnFocusChangeListener {
		ImageView image;
		TextView textView;
		public ViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			itemView.setFocusable(true);
			itemView.setOnFocusChangeListener(this);
		}

		@Override
		public void onClick(View v) {
			if (null != mOnItemClickListener) {
				mOnItemClickListener.onItemClick(v, getAdapterPosition());
			}
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (null != mOnItemFocusChangeListener) {
				mOnItemFocusChangeListener.onItemFocusChange(textView,image, getAdapterPosition(), hasFocus);
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View inflate = mLayoutInflater.inflate(R.layout.source_item, null, false);
		ViewHolder viewHolder = new ViewHolder(inflate);
		viewHolder.image = (ImageView) inflate.findViewById(R.id.image);
		viewHolder.textView = (TextView) inflate.findViewById(R.id.textview);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.textView.setText(mTitles[position]);
		holder.textView.setTextColor(R.color.ff061527);
		holder.image.setImageResource(mImageResources[position]);
	}

	@Override
	public int getItemCount() {
		if (null!=mTitles&&null!=mImageResources) {
			return Math.min(mTitles.length,mImageResources.length);
		}
		return 0;
	}
}
