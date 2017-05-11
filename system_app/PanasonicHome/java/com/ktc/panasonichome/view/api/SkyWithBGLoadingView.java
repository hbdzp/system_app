package com.ktc.panasonichome.view.api;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.ScreenParams;

public class SkyWithBGLoadingView extends FrameLayout {
    private LinearLayout bgLayout;
    private SkyLoadingView loadingView;
    private LayoutParams mLoadP;

    public SkyWithBGLoadingView(Context context) {
        this(context, null);
    }

    public SkyWithBGLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkyWithBGLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.bgLayout = new LinearLayout(context);
        this.bgLayout.setGravity(17);
        this.mLoadP = new LayoutParams(ScreenParams.getInstence(context).getResolutionValue(180), ScreenParams.getInstence(context).getResolutionValue(180), 17);
        addView(this.bgLayout, this.mLoadP);
        this.bgLayout.setBackgroundResource(R.drawable.ui_sdk_loading_bg);
        this.loadingView = new SkyLoadingView(context);
        this.loadingView.setStrokeWidth(ScreenParams.getInstence(context).getResolutionValue(5));
        this.bgLayout.addView(this.loadingView, new LinearLayout.LayoutParams(ScreenParams.getInstence(context).getResolutionValue(70), ScreenParams.getInstence(context).getResolutionValue(70)));
        setVisibility(View.INVISIBLE);
    }

    public void setScaleW_H(int w, int h) {
        LinearLayout.LayoutParams loadP = (LinearLayout.LayoutParams) this.loadingView.getLayoutParams();
        loadP.width = (int) ((((float) (loadP.width * w)) / ((float) this.mLoadP.width)) + 0.5f);
        loadP.height = (int) ((((float) (loadP.height * h)) / ((float) this.mLoadP.height)) + 0.5f);
        this.loadingView.setLayoutParams(loadP);
        this.loadingView.setStrokeWidth((int) (((((float) w) * this.loadingView.getStokeWidth()) / ((float) this.mLoadP.width)) + 0.5f));
        this.mLoadP.width = (int) ((((float) (this.mLoadP.width * w)) / ((float) this.mLoadP.width)) + 0.5f);
        this.mLoadP.height = (int) ((((float) (this.mLoadP.height * h)) / ((float) this.mLoadP.height)) + 0.5f);
        this.bgLayout.setLayoutParams(this.mLoadP);
        ViewGroup.LayoutParams thisP = getLayoutParams();
        thisP.width = w;
        thisP.height = h;
        setLayoutParams(thisP);
    }

    public int getLoadingViewWidth() {
        return this.mLoadP.width;
    }

    public int getLoadingViewHeight() {
        return this.mLoadP.height;
    }

    public void showLoading() {
        setVisibility(View.VISIBLE);
        this.loadingView.startAnim();
    }

    public void setStrokeWidth(int width) {
        this.loadingView.setStrokeWidth(width);
    }

    public void dismissLoading() {
        this.loadingView.stopAnim();
        setVisibility(View.GONE);
    }

    public boolean isSpinning() {
        return this.loadingView.isSpinning();
    }
}
