package com.ktc.panasonichome.view.api;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.List;

public class SkyImgLoadingView extends ImageView {
    private static int ANIM_FRAME = 2;
    private static int ANIM_ROTATE = 1;
    private int animType = -1;
    private AnimationDrawable framAnim;
    private Context mContext;
    private Animation rotateAnim;

    public SkyImgLoadingView(Context context) {
        super(context);
        this.mContext = context;
        setVisibility(View.INVISIBLE);
    }

    public SkyImgLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setVisibility(View.INVISIBLE);
    }

    public SkyImgLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setVisibility(View.INVISIBLE);
    }

    public void initRotateAnim(Drawable drawable) {
        this.animType = ANIM_ROTATE;
        this.rotateAnim = new RotateAnimation(0.0f, 359.0f, 1, 0.5f, 1, 0.5f);
        this.rotateAnim.setDuration(1000);
        this.rotateAnim.setInterpolator(new LinearInterpolator());
        this.rotateAnim.setRepeatMode(1);
        this.rotateAnim.setRepeatCount(-1);
        if (drawable != null) {
            setImageDrawable(drawable);
        }
    }

    public void initFrameAnim(List<Drawable> frames, int duration) {
        this.animType = ANIM_FRAME;
        if (frames != null && frames.size() > 1) {
            this.framAnim = new AnimationDrawable();
            for (int i = 0; i < frames.size(); i++) {
                if (frames.get(i) != null) {
                    this.framAnim.addFrame((Drawable) frames.get(i), duration);
                }
            }
            this.framAnim.setOneShot(false);
            setImageDrawable(this.framAnim);
        }
    }

    public void startAnim() {
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        if (this.animType == ANIM_ROTATE) {
            startAnimation(this.rotateAnim);
        } else if (this.animType == ANIM_FRAME && this.framAnim != null && !this.framAnim.isRunning()) {
            this.framAnim.start();
        }
    }

    public void stopAnim() {
        if (this.animType == ANIM_FRAME && this.framAnim != null && this.framAnim.isRunning()) {
            this.framAnim.stop();
        }
        clearAnimation();
        setVisibility(View.INVISIBLE);
    }
}
