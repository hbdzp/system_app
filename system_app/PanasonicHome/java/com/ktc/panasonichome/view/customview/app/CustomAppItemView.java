package com.ktc.panasonichome.view.customview.app;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.listview.AdapterItem;
import com.ktc.panasonichome.view.listview.MetroListItem;

public class CustomAppItemView extends MetroListItem implements AdapterItem<CustomAppInfo> {
    private ImageView bgView;
    private CustomAppInfo curInfo;
    private ImageView iconView;
    private Context mContext;
    private FrameLayout mShaderLayout;
    private ImageView rightFlagView;
    private TextView titleView;

    public CustomAppItemView(Context context) {
        super(context);
        this.mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        initView();
    }

    private void initView() {
        int padding = getResolutionValue(6);
        setPadding(padding, padding, padding, padding);
        this.mShaderLayout = new FrameLayout(this.mContext);
        this.mShaderLayout.setBackgroundResource(R.drawable.ui_sdk_btn_unfocus_big_shadow);
        addView(this.mShaderLayout);
        this.bgView = new ImageView(this.mContext);
        this.iconView = new ImageView(this.mContext);
        this.titleView = new TextView(this.mContext);
        this.titleView.setTextColor(Color.BLACK);
        this.titleView.setTextSize((float) getTextDpiValue(30));
        this.titleView.setGravity(17);
        this.titleView.setSingleLine();
        this.titleView.setEllipsize(TruncateAt.MARQUEE);
        this.titleView.setMarqueeRepeatLimit(-1);
        this.rightFlagView = new ImageView(this.mContext);
        this.mShaderLayout.addView(this.bgView, new LayoutParams(getResolutionValue(238), getResolutionValue(238)));
        LayoutParams icon_p = new LayoutParams(getResolutionValue(145), getResolutionValue(145));
        icon_p.gravity = 1;
        icon_p.topMargin = getResolutionValue(30);
        this.mShaderLayout.addView(this.iconView, icon_p);
        LayoutParams title_p = new LayoutParams(getResolutionValue(185), -2);
        title_p.topMargin = getResolutionValue(185);
        title_p.gravity = 1;
        this.mShaderLayout.addView(this.titleView, title_p);
        LayoutParams flag_p = new LayoutParams(getResolutionValue(48), getResolutionValue(48));
        flag_p.gravity = 53;
        this.mShaderLayout.addView(this.rightFlagView, flag_p);
        this.rightFlagView.setVisibility(View.INVISIBLE);
    }

    public View getLayout() {
        return this;
    }

    public void onUpdateItemValue(CustomAppInfo model, int position, int viewType) {
        this.curInfo = model;
        this.mViewType = viewType;
    }

    public void clearItem() {
        if (this.iconView != null) {
            this.iconView.setBackgroundColor(Color.TRANSPARENT);
        }
        if (this.titleView != null) {
            this.titleView.setVisibility(View.INVISIBLE);
        }
    }

    public void refreshView() {
        if (this.curInfo != null) {
            this.iconView.setBackground(this.curInfo.icon);
            this.titleView.setText(this.curInfo.title);
            this.titleView.setVisibility(View.VISIBLE);
            setRightFlagState(this.curInfo.isChoosed);
        }
    }

    public CustomAppInfo getCurInfo() {
        return this.curInfo;
    }

    public void setRightFlagState(boolean isChoosed) {
        this.curInfo.isChoosed = isChoosed;
        if (isChoosed) {
            this.rightFlagView.setImageResource(R.drawable.ic_custom_item_selected);
            this.rightFlagView.setVisibility(View.VISIBLE);
            return;
        }
        this.rightFlagView.setImageResource(R.drawable.ic_custom_item_unselected);
    }

    public void focus() {
        this.titleView.setSelected(true);
        this.bgView.setBackgroundColor(Color.WHITE);
        this.rightFlagView.setVisibility(View.VISIBLE);
    }

    public void unfocus() {
        this.titleView.setSelected(false);
        this.bgView.setBackgroundColor(Color.TRANSPARENT);
        if (this.curInfo.isChoosed) {
            this.rightFlagView.setVisibility(View.VISIBLE);
        } else {
            this.rightFlagView.setVisibility(View.INVISIBLE);
        }
    }

    public int getResolutionValue(int value) {
        return ScreenParams.getInstence(this.mContext).getResolutionValue(value);
    }

    public int getTextDpiValue(int textSize) {
        return ScreenParams.getInstence(this.mContext).getTextDpiValue(textSize);
    }
}
