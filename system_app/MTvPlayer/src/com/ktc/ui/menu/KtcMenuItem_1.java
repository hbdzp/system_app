package com.ktc.ui.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
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
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
import com.ktc.util.KtcScreenParams;
import com.ktc.util.MyFocusFrame;
////import org.apache.http4.HttpStatus;

public class KtcMenuItem_1 extends FrameLayout implements OnFocusChangeListener {
    private LinearLayout contentLayout;
    private KtcMenuData currentData;
    private int focusIconID = -1;
    private int iconBgID = -1;
    private FrameLayout iconBglayout;//条目失去焦点时的背景,也就是坐标的圆圈
    private boolean isSelect = false;
    private ImageView itemFocusView;//条目获得焦点的icon,比如坐标亮色的声音模式图标
    private TextView itemTextView;//条目的显示字符.比如声音模式
    private ImageView itemUnfocusView;//条目失去焦点的icon,比如坐标灰色的声音模式图标
    private Context mContext;
    private MyFocusFrame menuFocusView;//第一级菜单获得焦点时的阴影框
    private int textSize = 0;
    private int unFocusIconID = -1;

    public KtcMenuItem_1(Context context) {
        super(context);
        this.mContext = context;
        initView();
        setFocusable(true);
        setOnFocusChangeListener(this);
    }

    private void initView() {
        Log.d("Maxs","KtcMenuItem_1:initView");
        this.contentLayout = new LinearLayout(this.mContext);
        this.contentLayout.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(10), 0, 0, 0);
        this.contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams1.gravity = Gravity.CENTER_VERTICAL;

        this.contentLayout.addView(frameLayout, layoutParams1);
        this.iconBglayout = new FrameLayout(this.mContext);
        frameLayout.addView(this.iconBglayout, new LinearLayout.LayoutParams(-2, -2));
        this.iconBglayout.setVisibility(View.GONE);
        this.itemFocusView = new ImageView(this.mContext);
        frameLayout.addView(this.itemFocusView, new LayoutParams(-2, -2));
        this.itemUnfocusView = new ImageView(this.mContext);
        frameLayout.addView(this.itemUnfocusView, new LayoutParams(-2, -2));
        this.itemTextView = new TextView(this.mContext);
        this.itemTextView.setSingleLine(true);
        this.itemTextView.getPaint().setAntiAlias(true);
        this.itemTextView.setEllipsize(TruncateAt.END);
        this.itemTextView.setMarqueeRepeatLimit(-1);
        this.itemTextView.setGravity(16);
        ////LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(HttpStatus.SC_OK), -1);
        ////HttpStatus.SC_OK
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(200), -1);
        layoutParams2.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(25);
        this.contentLayout.addView(this.itemTextView, layoutParams2);
        addView(this.contentLayout, new LayoutParams(-1, -1));
    }

    public void focus() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
        alphaAnimation.setDuration(150);
        this.iconBglayout.clearAnimation();
        this.iconBglayout.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(150);
        alphaAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                KtcMenuItem_1.this.iconBglayout.setVisibility(View.GONE);
                KtcMenuItem_1.this.iconBglayout.clearAnimation();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.itemUnfocusView.setVisibility(View.GONE);
        this.itemFocusView.setVisibility(View.VISIBLE);
        KtcScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "000000");
        if (this.itemFocusView.getBackground() != null) {
            Drawable background = this.itemFocusView.getBackground();
            background.mutate().setAlpha(255);
            this.itemFocusView.setBackgroundDrawable(background);
        }
    }

    public KtcMenuData getItemData() {
        return this.currentData;
    }

    public void onFocusChange(View view, boolean z) {
        Log.d("Maxs","------------->KtcMenuItem_1:onFocusChange:z = " + z);
        if (z) {
        	////KtcLogger.v("xw", " focus  size ==  " + this.textSize);
            this.contentLayout.clearAnimation();
            focus();
            this.itemTextView.setEllipsize(TruncateAt.MARQUEE);
            this.itemTextView.setSelected(true);
            int i = -KtcScreenParams.getInstence(this.mContext).getResolutionValue(36);
            KtcScreenParams.getInstence(this.mContext).getResolutionValue(496);
            KtcScreenParams.getInstence(this.mContext).getResolutionValue(358);
            KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
            KtcScreenParams.getInstence(this.mContext).getResolutionValue(100);
            KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
            return;
        }
      ////KtcLogger.v("xw", " unfocus  size ==  " + this.textSize);
      ////KtcLogger.v("xw", " itemTextView  " + this.itemTextView.getText().toString());
        this.itemTextView.setSelected(false);
        this.itemTextView.setEllipsize(TruncateAt.END);
        unfocus();
    }

    public void refreshItemValue(KtcMenuData ktcMenuData) {
        if (ktcMenuData != null) {
            this.currentData = ktcMenuData;
            if (ktcMenuData.getItemTitle() != null) {
                this.itemTextView.setText(ktcMenuData.getItemTitle() + "　");
            }
            if (ktcMenuData.getItemFocusIcon() != -1) {
                this.focusIconID = ktcMenuData.getItemFocusIcon();
            }
            if (ktcMenuData.getItemUnFocusIcon() != -1) {
                this.unFocusIconID = ktcMenuData.getItemUnFocusIcon();
            }
            if (ktcMenuData.getItemIconBg() != -1) {
                this.iconBgID = ktcMenuData.getItemIconBg();
            }
            post(new Runnable() {
                public void run() {
                    KtcScreenParams.getInstence(KtcMenuItem_1.this.mContext).getResolutionValue(77);
                    KtcScreenParams.getInstence(KtcMenuItem_1.this.mContext).getResolutionValue(47);
                    KtcScreenParams.getInstence(KtcMenuItem_1.this.mContext).getResolutionValue(516);
                    KtcMenuItem_1.this.getWidth();
                    KtcScreenParams.getInstence(KtcMenuItem_1.this.mContext).getResolutionValue(47);
                    KtcScreenParams.getInstence(KtcMenuItem_1.this.mContext).getResolutionValue(171);
                }
            });
        }
    }

    public void resetvIconBgState() {
        if (this.iconBglayout != null) {
            this.iconBglayout.clearAnimation();
            this.iconBglayout.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(150);
          ////KtcLogger.v("lgx", "iconBglayout.getVisibility == " + this.iconBglayout.getVisibility());
        }
    }

    public void setFocusView(MyFocusFrame myFocusFrame) {
        this.menuFocusView = myFocusFrame;
    }

    public void setItemStateForSecond(boolean z) {
        if (!this.isSelect) {
            Animation alphaAnimation;
            if (z) {
                alphaAnimation = new AlphaAnimation(0.6f, 0.3f);
                alphaAnimation.setDuration(200);
                alphaAnimation.setFillAfter(true);
                this.contentLayout.startAnimation(alphaAnimation);
                return;
            }
            alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
            alphaAnimation.setDuration(200);
            alphaAnimation.setFillAfter(true);
            this.contentLayout.startAnimation(alphaAnimation);
        }
    }

    public void setSelectState(boolean z) {
        this.isSelect = z;
    }

    public void setTextAttribute(int i, int i2) {
        if (this.itemTextView != null) {
            this.textSize = i;
            this.itemTextView.setTextSize((float) i);
            this.itemTextView.setTextColor(i2);
        }
    }

    public void unfocus() {
        AnimationSet animationSet;
        Animation scaleAnimation;
        Drawable background;
        if (this.isSelect) {
            animationSet = new AnimationSet(false);
            scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, 1, 0.5f, 1, 0.5f);
            scaleAnimation.setDuration(150);
            animationSet.addAnimation(scaleAnimation);
            scaleAnimation = new AlphaAnimation(0.1f, 1.0f);
            scaleAnimation.setDuration(150);
            animationSet.addAnimation(scaleAnimation);
            this.iconBglayout.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150);
            animationSet.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    KtcMenuItem_1.this.iconBglayout.setVisibility(View.VISIBLE);
                    KtcMenuItem_1.this.iconBglayout.clearAnimation();
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            this.itemUnfocusView.setVisibility(View.VISIBLE);
            this.itemFocusView.setVisibility(View.GONE);
            KtcScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "5b5b5b");
            if (this.itemUnfocusView.getBackground() != null) {
                background = this.itemUnfocusView.getBackground();
                background.mutate().setAlpha(255);
                this.itemUnfocusView.setBackgroundDrawable(background);
            }
            if (this.iconBglayout.getBackground() != null) {
                background = this.iconBglayout.getBackground();
                ////background.mutate().setAlpha(HttpStatus.SC_OK);
                ////HttpStatus.SC_OK = 200
                background.mutate().setAlpha(200);
                this.iconBglayout.setBackgroundDrawable(background);
            }
        } else if (!this.isSelect) {
            animationSet = new AnimationSet(false);
            scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, 1, 0.5f, 1, 0.5f);
            scaleAnimation.setDuration(150);
            animationSet.addAnimation(scaleAnimation);
            scaleAnimation = new AlphaAnimation(0.1f, 1.0f);
            scaleAnimation.setDuration(150);
            animationSet.addAnimation(scaleAnimation);
            this.iconBglayout.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150);
            animationSet.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    KtcMenuItem_1.this.iconBglayout.setVisibility(View.VISIBLE);
                    KtcMenuItem_1.this.iconBglayout.clearAnimation();
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            this.itemUnfocusView.setVisibility(View.VISIBLE);
            this.itemFocusView.setVisibility(View.GONE);
            KtcScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "5b5b5b");
            if (this.itemUnfocusView.getBackground() != null) {
                background = this.itemUnfocusView.getBackground();
                ////background.mutate().setAlpha(HttpStatus.SC_OK);
                ////HttpStatus.SC_OK = 200
                background.mutate().setAlpha(200);
                this.itemUnfocusView.setBackgroundDrawable(background);
            }
            if (this.iconBglayout.getBackground() != null) {
                background = this.iconBglayout.getBackground();
                ////background.mutate().setAlpha(HttpStatus.SC_OK);
                ////HttpStatus.SC_OK = 200
                background.mutate().setAlpha(200);
                this.iconBglayout.setBackgroundDrawable(background);
            }
        }
    }

    public void updataItemValue(KtcMenuData ktcMenuData) {
        if (ktcMenuData != null) {
            Drawable background;
            this.currentData = ktcMenuData;
            if (ktcMenuData.getItemTitle() != null) {
                this.itemTextView.setText(ktcMenuData.getItemTitle() + "　");
            }
            KtcScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "5b5b5b");
            this.iconBgID = ktcMenuData.getItemIconBg();
            this.focusIconID = ktcMenuData.getItemFocusIcon();
            this.unFocusIconID = ktcMenuData.getItemUnFocusIcon();
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
                background = this.itemUnfocusView.getBackground();
                ////background.mutate().setAlpha(HttpStatus.SC_OK);
                ////HttpStatus.SC_OK = 200
                background.mutate().setAlpha(200);
                this.itemUnfocusView.setBackgroundDrawable(background);
            }
            if (this.iconBglayout.getBackground() != null) {
                background = this.iconBglayout.getBackground();
                ////background.mutate().setAlpha(HttpStatus.SC_OK);
            ////HttpStatus.SC_OK = 200
                background.mutate().setAlpha(200);
                this.iconBglayout.setBackgroundDrawable(background);
            }
        }
    }
}
