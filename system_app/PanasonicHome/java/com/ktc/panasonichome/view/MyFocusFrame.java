package com.ktc.panasonichome.view;

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

    public MyFocusFrame(Context context, int shaderSize) {
        super(context);
        this.shaderSize = shaderSize;
        this.context = context;
    }

    public void setImgResourse(int img_id) {
        this.bgId = img_id;
        for (View view : this.viewCache) {
            if (view.equals(this.lastFocus)) {
                view.setBackgroundResource(img_id);
            }
        }
    }

    public void setAnimDuration(long time) {
        this.animDuration = time;
    }

    public long getAnimDuration() {

        return this.animDuration;
    }

    public void needAnimtion(boolean need) {
        this.isAnimOn = need;
    }

    public void initStarPosition(View v) {
        v.getLocationInWindow(this.location);
        this.desLeft = this.location[0] - this.shaderSize;
        this.desTop = this.location[1] - this.shaderSize;
        this.desWidth = v.getWidth() + (this.shaderSize * 2);
        this.desHeight = v.getHeight() + (this.shaderSize * 2);
        beginAnimation();
    }

    public void initStarPosition(int posX, int posY, int destWidth, int destHeight) {
        this.desLeft = posX;
        this.desTop = posY;
        this.desWidth = destWidth;
        this.desHeight = destHeight;
        beginAnimation();
    }

    public void setInterpolator(Interpolator interpolator) {

        this.mInterpolator = interpolator;
    }

    public void changeFocusPos(View v) {
        if (v != null) {
            v.getLocationInWindow(this.location);
            this.desLeft = this.location[0] - this.shaderSize;
            this.desTop = this.location[1] - this.shaderSize;
            this.desWidth = v.getWidth() + (this.shaderSize * 2);
            this.desHeight = v.getHeight() + (this.shaderSize * 2);
            beginAnimation();
        }
    }

    public void changeFocusPos(int posX, int posY, int destWidth, int destHeight) {
        this.desLeft = posX;
        this.desTop = posY;
        this.desWidth = destWidth;
        this.desHeight = destHeight;
        Log.i("focuspos", "x " + posX + " y " + posY + " w " + destWidth + " h " + destHeight);
        beginAnimation();
    }

    private void startAlphaAnimation(View v, float from, float to, long during, AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "alpha", new float[]{from, to});
        if (this.mInterpolator != null) {
            animator.setInterpolator(this.mInterpolator);
        }
        animator.setDuration(during);
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.start();
    }

    private void beginAnimation() {
        View focus;
        if (this.lastFocus != null) {
            if (this.isAnimOn) {
                startAlphaAnimation(this.lastFocus, 1.0f, 0.0f, this.animDuration, null);
            } else {
                this.lastFocus.clearAnimation();
                this.lastFocus.setVisibility(View.GONE);
            }
        }
        if (this.viewCache.size() > 0) {
            focus = (View) this.viewCache.remove(0);
        } else {
            focus = new ImageView(this.context);
            focus.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
            addView(focus);
        }
        focus.setVisibility(View.VISIBLE);
        focus.setBackgroundResource(this.bgId);
        setFocusPos(focus, this.desLeft, this.desTop, this.desWidth, this.desHeight);
        this.lastFocus = focus;
    }

    public void setFocusPos(int posX, int posY, int destWidth, int destHeight) {
        this.isAnimOn = false;
        changeFocusPos(posX, posY, destWidth, destHeight);
    }

    private void setFocusPos(final View view, int posX, int posY, int destWidth, int destHeight) {
        LayoutParams frameParams = (LayoutParams) view.getLayoutParams();
        frameParams.leftMargin = posX;
        frameParams.topMargin = posY;
        frameParams.width = destWidth;
        frameParams.height = destHeight;
        view.setLayoutParams(frameParams);
        if (this.isAnimOn) {
            startAlphaAnimation(view, 0.0f, 1.0f, this.animDuration, new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    MyFocusFrame.this.viewCache.add(view);
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
            return;
        }
        view.clearAnimation();
        this.viewCache.add(view);
        view.setVisibility(View.GONE);
        if (view.getAlpha() != 1.0f) {
            view.setAlpha(1.0f);
        }
    }
}
