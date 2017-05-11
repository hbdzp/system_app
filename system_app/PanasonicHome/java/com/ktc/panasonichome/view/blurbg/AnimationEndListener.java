package com.ktc.panasonichome.view.blurbg;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.view.View;

@SuppressLint({"InlinedApi"})
public class AnimationEndListener implements AnimatorListener {
    private View mView;

    public AnimationEndListener(View view) {
        this.mView = view;
    }

    public void onAnimationCancel(Animator animator) {
        this.mView.setLayerType(0, null);
    }

    public void onAnimationEnd(Animator animator) {
        this.mView.setLayerType(0, null);
    }

    public void onAnimationRepeat(Animator animator) {
    }

    public void onAnimationStart(Animator animator) {
        this.mView.setLayerType(2, null);
    }
}
