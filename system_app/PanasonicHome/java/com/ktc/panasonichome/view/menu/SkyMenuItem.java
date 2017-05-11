package com.ktc.panasonichome.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.MyFocusFrame;

public class SkyMenuItem extends FrameLayout implements OnFocusChangeListener {
    private LinearLayout contentLayout;
    private SkyMenuData currentData;
    private int focusIconID = -1;
    private int iconBgID = -1;
    private FrameLayout iconBglayout;
    private boolean isSelect = false;
    private ImageView    itemFocusView;
    private TextView     itemTextView;
    private ImageView    itemUnfocusView;
    private Context      mContext;
    private MyFocusFrame menuFocusView;
    private int textSize = 0;
    private int unFocusIconID = -1;

    class C03431 implements Runnable {
        C03431() {
        }

        public void run() {
            int x = ScreenParams.getInstence(SkyMenuItem.this.mContext).getResolutionValue(77) - ScreenParams.getInstence(SkyMenuItem.this.mContext).getResolutionValue(47);
            int y = ScreenParams.getInstence(SkyMenuItem.this.mContext).getResolutionValue(516);
            int width = SkyMenuItem.this.getWidth() + (ScreenParams.getInstence(SkyMenuItem.this.mContext).getResolutionValue(47) * 2);
            int height = ScreenParams.getInstence(SkyMenuItem.this.mContext).getResolutionValue(171);
        }
    }

    class C03442 implements AnimationListener {
        C03442() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            SkyMenuItem.this.iconBglayout.setVisibility(View.GONE);
            SkyMenuItem.this.iconBglayout.clearAnimation();
        }
    }

    class C03453 implements AnimationListener {
        C03453() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            SkyMenuItem.this.iconBglayout.setVisibility(View.VISIBLE);
            SkyMenuItem.this.iconBglayout.clearAnimation();
        }
    }

    class C03464 implements AnimationListener {
        C03464() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            SkyMenuItem.this.iconBglayout.setVisibility(View.VISIBLE);
            SkyMenuItem.this.iconBglayout.clearAnimation();
        }
    }

    public SkyMenuItem(Context context) {
        super(context);
        this.mContext = context;
        initView();
        setFocusable(true);
        setOnFocusChangeListener(this);
    }

    private void initView() {
        this.contentLayout = new LinearLayout(this.mContext);
        this.contentLayout.setPadding(ScreenParams.getInstence(this.mContext).getResolutionValue(10), 0, 0, 0);
        this.contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        FrameLayout iconLayout = new FrameLayout(this.mContext);
        LayoutParams iconParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        iconParams.gravity = 16;
        this.contentLayout.addView(iconLayout, iconParams);
        this.iconBglayout = new FrameLayout(this.mContext);
        iconLayout.addView(this.iconBglayout, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        this.iconBglayout.setVisibility(View.GONE);
        this.itemFocusView = new ImageView(this.mContext);
        iconLayout.addView(this.itemFocusView, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        this.itemUnfocusView = new ImageView(this.mContext);
        iconLayout.addView(this.itemUnfocusView, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        this.itemTextView = new TextView(this.mContext);
        this.itemTextView.setSingleLine(true);
        this.itemTextView.getPaint().setAntiAlias(true);
        this.itemTextView.setEllipsize(TruncateAt.END);
        this.itemTextView.setMarqueeRepeatLimit(-1);
        this.itemTextView.setGravity(16);
        LayoutParams title_p = new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(200), -1);
        title_p.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(25);
        this.contentLayout.addView(this.itemTextView, title_p);
        addView(this.contentLayout, new LayoutParams(-1, -1));
    }

    public void setFocusView(MyFocusFrame menuFocusView) {
        this.menuFocusView = menuFocusView;
    }

    public void setTextAttribute(int size, int color) {
        if (this.itemTextView != null) {
            this.textSize = size;
            this.itemTextView.setTextSize((float) size);
            this.itemTextView.setTextColor(color);
        }
    }

    public void setSelectState(boolean select) {
        this.isSelect = select;
    }

    public void refreshItemValue(SkyMenuData data) {
        if (data != null) {
            this.currentData = data;
            if (data.getItemTitle() != null) {
                this.itemTextView.setText(data.getItemTitle() + "　");
            }
            if (data.getItemFocusIcon() != -1) {
                this.focusIconID = data.getItemFocusIcon();
            }
            if (data.getItemUnFocusIcon() != -1) {
                this.unFocusIconID = data.getItemUnFocusIcon();
            }
            if (data.getItemIconBg() != -1) {
                this.iconBgID = data.getItemIconBg();
            }
            post(new C03431());
        }
    }

    public void updataItemValue(SkyMenuData data) {
        if (data != null) {
            Drawable image;
            this.currentData = data;
            if (data.getItemTitle() != null) {
                this.itemTextView.setText(data.getItemTitle() + "　");
            }
            ScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "5b5b5b");
            this.iconBgID = data.getItemIconBg();
            this.focusIconID = data.getItemFocusIcon();
            this.unFocusIconID = data.getItemUnFocusIcon();
            this.iconBglayout.setBackgroundResource(this.iconBgID);
            this.itemUnfocusView.setBackgroundResource(this.unFocusIconID);
            this.itemFocusView.setBackgroundResource(this.focusIconID);
            if (this.isSelect) {
                this.iconBglayout.setVisibility(View.VISIBLE);
                this.itemUnfocusView.setVisibility(View.VISIBLE);
                this.itemFocusView.setVisibility(View.GONE);
            } else {
                this.iconBglayout.setVisibility(View.VISIBLE);
                this.itemUnfocusView.setVisibility(View.VISIBLE);
                this.itemFocusView.setVisibility(View.GONE);
            }
            if (this.itemUnfocusView.getBackground() != null) {
                image = this.itemUnfocusView.getBackground();
                image.mutate().setAlpha(200);
                this.itemUnfocusView.setBackgroundDrawable(image);
            }
            if (this.iconBglayout.getBackground() != null) {
                image = this.iconBglayout.getBackground();
                image.mutate().setAlpha(200);
                this.iconBglayout.setBackgroundDrawable(image);
            }
        }
    }

    public SkyMenuData getItemData() {
        return this.currentData;
    }

    public void setItemStateForSecond(boolean show) {
        if (!this.isSelect) {
            AlphaAnimation alphaAnim;
            if (show) {
                alphaAnim = new AlphaAnimation(0.6f, 0.3f);
                alphaAnim.setDuration(200);
                alphaAnim.setFillAfter(true);
                this.contentLayout.startAnimation(alphaAnim);
                return;
            }
            alphaAnim = new AlphaAnimation(0.3f, 1.0f);
            alphaAnim.setDuration(200);
            alphaAnim.setFillAfter(true);
            this.contentLayout.startAnimation(alphaAnim);
        }
    }

    public void resetvIconBgState() {
        if (this.iconBglayout != null) {
            this.iconBglayout.clearAnimation();
            this.iconBglayout.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(150);
            LogUtils.v("dzp", "iconBglayout.getVisibility == " + this.iconBglayout.getVisibility());
        }
    }

    public void focus() {
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.1f);
        alphaAnim.setDuration(150);
        this.iconBglayout.clearAnimation();
        this.iconBglayout.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(150);
        alphaAnim.setAnimationListener(new C03442());
        this.itemUnfocusView.setVisibility(View.GONE);
        this.itemFocusView.setVisibility(View.VISIBLE);
        ScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "000000");
        if (this.itemFocusView.getBackground() != null) {
            Drawable image = this.itemFocusView.getBackground();
            image.mutate().setAlpha(255);
            this.itemFocusView.setBackgroundDrawable(image);
        }
    }

    public void unfocus() {
        AnimationSet animSet;
        ScaleAnimation scaleAnim;
        AlphaAnimation alphaAnim;
        Drawable image;
        if (this.isSelect) {
            animSet = new AnimationSet(false);
            scaleAnim = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, 1, 0.5f, 1, 0.5f);
            scaleAnim.setDuration(150);
            animSet.addAnimation(scaleAnim);
            alphaAnim = new AlphaAnimation(0.1f, 1.0f);
            alphaAnim.setDuration(150);
            animSet.addAnimation(alphaAnim);
            this.iconBglayout.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150);
            animSet.setAnimationListener(new C03453());
            this.itemUnfocusView.setVisibility(View.VISIBLE);
            this.itemFocusView.setVisibility(View.GONE);
            ScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "5b5b5b");
            if (this.itemUnfocusView.getBackground() != null) {
                image = this.itemUnfocusView.getBackground();
                image.mutate().setAlpha(255);
                this.itemUnfocusView.setBackgroundDrawable(image);
            }
            if (this.iconBglayout.getBackground() != null) {
                image = this.iconBglayout.getBackground();
                image.mutate().setAlpha(200);
                this.iconBglayout.setBackgroundDrawable(image);
            }
        } else if (!this.isSelect) {
            animSet = new AnimationSet(false);
            scaleAnim = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, 1, 0.5f, 1, 0.5f);
            scaleAnim.setDuration(150);
            animSet.addAnimation(scaleAnim);
            alphaAnim = new AlphaAnimation(0.1f, 1.0f);
            alphaAnim.setDuration(150);
            animSet.addAnimation(alphaAnim);
            this.iconBglayout.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150);
            animSet.setAnimationListener(new C03464());
            this.itemUnfocusView.setVisibility(View.VISIBLE);
            this.itemFocusView.setVisibility(View.GONE);
            ScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "5b5b5b");
            if (this.itemUnfocusView.getBackground() != null) {
                image = this.itemUnfocusView.getBackground();
                image.mutate().setAlpha(200);
                this.itemUnfocusView.setBackgroundDrawable(image);
            }
            if (this.iconBglayout.getBackground() != null) {
                image = this.iconBglayout.getBackground();
                image.mutate().setAlpha(200);
                this.iconBglayout.setBackgroundDrawable(image);
            }
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            LogUtils.v("dzp", " focus  size ==  " + this.textSize);
            this.contentLayout.clearAnimation();
            focus();
            this.itemTextView.setEllipsize(TruncateAt.MARQUEE);
            this.itemTextView.setSelected(true);
            int x = -ScreenParams.getInstence(this.mContext).getResolutionValue(36);
            int y = ScreenParams.getInstence(this.mContext).getResolutionValue(496);
            int width = ScreenParams.getInstence(this.mContext).getResolutionValue(358) + (ScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2);
            int resolutionValue = ScreenParams.getInstence(this.mContext).getResolutionValue(100) + (ScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2);
            return;
        }
        LogUtils.v("dzp", " unfocus  size ==  " + this.textSize);
        LogUtils.v("dzp", " itemTextView  " + this.itemTextView.getText().toString());
        this.itemTextView.setSelected(false);
        this.itemTextView.setEllipsize(TruncateAt.END);
        unfocus();
    }
}
