package com.ktc.panasonichome.view;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.ScreenParams;

public class BasePostItem_Big extends BaseItem {
    public BasePostItem_Big(Context context, MyFocusFrame focusView) {
        super(context, focusView);
    }

    public void initView() {
        setItemWidth(ScreenParams.getInstence(this.mContext).getResolutionValue(470));
        setItemHeight(ScreenParams.getInstence(this.mContext).getResolutionValue(470));
        FrameLayout layout = new FrameLayout(this.mContext);
        LayoutParams paramsLayout = new LayoutParams(this.itemWidth + ScreenParams.getInstence(this.mContext).getResolutionValue(24), this.itemHeight + ScreenParams.getInstence(this.mContext).getResolutionValue(24));
        paramsLayout.gravity = 17;
        layout.setBackgroundResource(R.drawable.ui_sdk_btn_unfocus_big_shadow);
        addView(layout, paramsLayout);
        LayoutParams params = new LayoutParams(this.itemWidth, this.itemHeight);
        params.gravity = 17;
        this.contentLayout = new FrameLayout(this.mContext);
        addView(this.contentLayout, params);
        this.iconView = new ImageView(this.mContext);
        LayoutParams icon_p = new LayoutParams(-1, -1);
        icon_p.gravity = 17;
        this.contentLayout.addView(this.iconView, icon_p);
        this.titleView = new TextView(this.mContext);
        this.titleView.getPaint().setAntiAlias(true);
        this.titleView.setSingleLine(true);
        this.titleView.setHorizontallyScrolling(true);
        this.titleView.setEllipsize(TruncateAt.MARQUEE);
        this.titleView.setMarqueeRepeatLimit(-1);
        this.titleView.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(32));
        this.titleView.setTextColor(-1);
        this.titleView.setGravity(17);
        LayoutParams title_p = new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(400), -2);
        title_p.gravity = 81;
        title_p.bottomMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(30);
        this.contentLayout.addView(this.titleView, title_p);
    }

    public void setItemValue(String url, String title, String action) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http")) {
                //TODO 处理从网络图片加载的逻辑
//                Picasso.with(this.mContext).load(url).resize(getItemWidth(), getItemHeight()).into(this.iconView);
            } else {
                //TODO 处理图片从本地加载的逻辑
//                Picasso.with(this.mContext).load(new File(StartApi.getInstence(this.mContext).getImgLocalPath() + url)).resize(getItemWidth(), getItemHeight()).into(this.iconView);
            }
        }
        if (!TextUtils.isEmpty(title)) {
            this.titleView.setText(title);
        }
        if (!TextUtils.isEmpty(action)) {
            this.startAction = action;
        }
    }

    public void focus(View view) {
        if (this.titleView != null) {
            this.titleView.setSelected(true);
        }
    }

    public void unfocus(View view) {
        if (this.titleView != null) {
            this.titleView.setSelected(false);
        }
    }
}
