package com.ktc.util;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

/*
 * MyFocusFrame是item获得焦点时的显示控件,其背景就是长方框内高亮而四周都是阴影
 */
public class MyFocusFrame extends FrameLayout {
    private long animDuration = 200;
    private int bgId = 0;
    private Context context = null;
    private int desHeight;
    private int desLeft;
    private int desTop;
    private int desWidth;
    private LayoutParams frame_p;
    private boolean isAnimOn = true;
    private View lastFocus = null;
    private int[] location = new int[2];
    private Interpolator mInterpolator;
    private int shaderSize = 0;
    private List<View> viewCache = new ArrayList();

    public MyFocusFrame(Context context, int i) {
        super(context);
        this.shaderSize = i;
        this.context = context;
    }

    private void beginAnimation() {
        View view;
        if (this.lastFocus != null) {
            if (this.isAnimOn) {
                startAlphaAnimation(this.lastFocus, 1.0f, 0.0f, this.animDuration, null);
            } else {
                this.lastFocus.clearAnimation();
                this.lastFocus.setVisibility(View.GONE);
            }
        }
        if (this.viewCache.size() > 0) {
            view = (View) this.viewCache.remove(0);
        } else {
            view = new ImageView(this.context);
            view.setLayoutParams(new LayoutParams(-2, -2));
            addView(view);
        }
        view.setVisibility(View.VISIBLE);
        view.setBackgroundResource(this.bgId);
        setFocusPos(view, this.desLeft, this.desTop, this.desWidth, this.desHeight);
        this.lastFocus = view;
    }

    private void setFocusPos(final View view, int i, int i2, int i3, int i4) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = i;
        layoutParams.topMargin = i2;
        layoutParams.width = i3;
        layoutParams.height = i4;
        view.setLayoutParams(layoutParams);
        if (this.isAnimOn) {
            startAlphaAnimation(view, 0.0f, 1.0f, this.animDuration, new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    MyFocusFrame.this.viewCache.add(view);
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }
            });
            return;
        }
        view.clearAnimation();
        this.viewCache.add(view);
        view.setVisibility(View.VISIBLE);
        if (view.getAlpha() != 1.0f) {
            view.setAlpha(1.0f);
        }
    }

    private void startAlphaAnimation(View view, float f, float f2, long j, AnimatorListener animatorListener) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "alpha", new float[]{f, f2});
        if (this.mInterpolator != null) {
            ofFloat.setInterpolator(this.mInterpolator);
        }
        ofFloat.setDuration(j);
        if (animatorListener != null) {
            ofFloat.addListener(animatorListener);
        }
        ofFloat.start();
    }

    public void changeFocusPos(int i, int i2, int i3, int i4) {
        Log.d("Maxs","MyFocusFrame:changeFocusPos: i = " + i + " /i2 = " + i2 + " /i3 = " + i3 + "/i4 = " + i4);
        this.desLeft = i;
        this.desTop = i2;
        this.desWidth = i3;
        this.desHeight = i4;
        Log.i("focuspos", "x " + i + " y " + i2 + " w " + i3 + " h " + i4);
        beginAnimation();
    }

    public void changeFocusPos(View view) {
        if (view != null) {
            view.getLocationInWindow(this.location);
            this.desLeft = this.location[0] - this.shaderSize;
            this.desTop = this.location[1] - this.shaderSize;
            this.desWidth = view.getWidth() + (this.shaderSize * 2);
            this.desHeight = view.getHeight() + (this.shaderSize * 2);
            beginAnimation();
        }
    }

    public long getAnimDuration() {
        return this.animDuration;
    }

    public void initStarPosition(int i, int i2, int i3, int i4) {
        this.desLeft = i;
        this.desTop = i2;
        this.desWidth = i3;
        this.desHeight = i4;
        beginAnimation();
    }

    public void initStarPosition(View view) {
        view.getLocationInWindow(this.location);
        this.desLeft = this.location[0] - this.shaderSize;
        this.desTop = this.location[1] - this.shaderSize;
        this.desWidth = view.getWidth() + (this.shaderSize * 2);
        this.desHeight = view.getHeight() + (this.shaderSize * 2);
        beginAnimation();
    }

    public void needAnimtion(boolean z) {
        this.isAnimOn = z;
    }

    public void setAnimDuration(long j) {
        this.animDuration = j;
    }

    public void setFocusPos(int i, int i2, int i3, int i4) {
        this.isAnimOn = false;
        changeFocusPos(i, i2, i3, i4);
    }

    public void setImgResourse(int i) {
        this.bgId = i;
        for (View view : this.viewCache) {
            if (view.equals(this.lastFocus)) {
                view.setBackgroundResource(i);
            }
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }
}
