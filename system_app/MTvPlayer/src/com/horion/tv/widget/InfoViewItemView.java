package com.horion.tv.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.horion.tv.R;

/**
 * Created by xiacf on 2017/1/12.
 */

public class InfoViewItemView extends RelativeLayout {
    private TextView keyTextView;
    private ImageView lineImageView;
    private TextView valueTextView;
    private float div = 1.0f;
    private float dipDiv = 1.0f;

    public InfoViewItemView(Context context) {
        super(context);
        this.keyTextView = new TextView(context);
        this.keyTextView.setGravity(19);
        this.keyTextView.setTextSize(36.0f / dipDiv);
        this.keyTextView.setTextColor(Color.parseColor("#B2000000"));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (266.0f / div), -2);
        layoutParams.leftMargin = (int) (14.0f / div);
        layoutParams.addRule(15, -1);
        addView(this.keyTextView, layoutParams);
        this.valueTextView = new TextView(context);
        this.valueTextView.setGravity(19);
        this.valueTextView.setTextSize(28.0f / dipDiv);
        this.valueTextView.setTextColor(Color.parseColor("#B2000000"));
        layoutParams = new RelativeLayout.LayoutParams((int) (510.0f / div), -2);
        layoutParams.leftMargin = (int) (280.0f / div);
        layoutParams.addRule(15, -1);
        addView(this.valueTextView, layoutParams);
        this.lineImageView = new ImageView(context);
        this.lineImageView.setBackgroundResource(R.drawable.ui_sdk_menu_title_line);
        layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(12, -1);
        addView(this.lineImageView, layoutParams);
    }

    public void setLineInVisible() {
        this.lineImageView.setVisibility(View.INVISIBLE);
    }

    public void updateData(String str, String str2) {
        this.keyTextView.setText(str);
        this.valueTextView.setText(str2);
    }
}
