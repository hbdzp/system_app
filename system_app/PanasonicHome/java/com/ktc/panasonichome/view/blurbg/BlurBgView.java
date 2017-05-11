package com.ktc.panasonichome.view.blurbg;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ktc.panasonichome.utils.ScreenParams;

public class BlurBgView extends FrameLayout {
    private ImageView bgImgView;
    private int bgViewH;
    private int bgViewW;
    private AnimatorUpdateListener listener = new AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (BlurBgView.this.mBlurringView != null) {
                BlurBgView.this.mBlurringView.invalidate();
            }
        }
    };
    private BlurringView mBlurringView;
    private Context mContext;

    public BlurBgView(Context context) {
        super(context);
        this.mContext = context;
        this.bgViewW = ScreenParams.getInstence(this.mContext).getResolutionValue(1920);
        this.bgViewH = ScreenParams.getInstence(this.mContext).getResolutionValue(1080);
        this.bgImgView = new ImageView(context);
        addView(this.bgImgView, new LayoutParams(this.bgViewW, this.bgViewH));
        /////setBackgroundColor(-4402466);
        setBackgroundColor(Color.YELLOW);
    }

    private void start() {
        /*ObjectAnimator.ofFloat(this, View.SCALE_X, new float[]{1.0f, 2.0f}).addUpdateListener(this.listener);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.SCALE_Y, new float[]{1.0f, 2.0f});
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{r0, ofFloat});
        animatorSet.setDuration(20000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimationEndListener(this));
        animatorSet.start();*/

        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(this, View.SCALE_X, new float[] { 1.0F, 2.0F });
        localObjectAnimator1.addUpdateListener(this.listener);
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this, View.SCALE_Y, new float[] { 1.0F, 2.0F });
        AnimatorSet localAnimatorSet = new AnimatorSet();
        localAnimatorSet.playTogether(new Animator[] { localObjectAnimator1, localObjectAnimator2 });
        localAnimatorSet.setDuration(20000L);
        localAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        localAnimatorSet.addListener(new AnimationEndListener(this));
        localAnimatorSet.start();
    }

    public ImageView getBgImgView() {
        return this.bgImgView;
    }

    public void setBlurRes(Drawable drawable) {
        this.bgImgView.setBackground(drawable);
    }

    public void setBlurringView(BlurringView blurringView) {
        this.mBlurringView = blurringView;
    }
}
