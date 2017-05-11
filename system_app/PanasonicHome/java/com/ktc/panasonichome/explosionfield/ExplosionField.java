package com.ktc.panasonichome.explosionfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;

import com.ktc.panasonichome.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dinzhip on 2016/12/28.
 */
public class ExplosionField extends View {
    private Context                 mContext;
    private int[]                   mExpandInset;
    private List<ExplosionAnimator> mExplosions;
    private OnExPlosionEndListener  mListener;

    public interface OnExPlosionEndListener {
        void onExplosionEnd();
    }

    public ExplosionField(Context context) {
        super(context);
        this.mExplosions = new ArrayList();
        this.mExpandInset = new int[2];
        this.mContext = context;
        init();
    }

    public ExplosionField(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mExplosions = new ArrayList();
        this.mExpandInset = new int[2];
        init();
    }

    public ExplosionField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mExplosions = new ArrayList();
        this.mExpandInset = new int[2];
        init();
    }

    public void setOnExPlosionEndListener(OnExPlosionEndListener listener) {
        this.mListener = listener;
    }

    private void init() {
        Arrays.fill(this.mExpandInset, Utils.dp2Px(32));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (ExplosionAnimator explosion : this.mExplosions) {
            explosion.draw(canvas);
        }
    }

    public void expandExplosionBound(int dx, int dy) {
        this.mExpandInset[0] = dx;
        this.mExpandInset[1] = dy;
    }

    public void explode(Bitmap bitmap, Rect bound, long startDelay, long duration) {
        ExplosionAnimator explosion = new ExplosionAnimator(this, bitmap, bound);
        explosion.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                ExplosionField.this.mExplosions.remove(animation);
                if (ExplosionField.this.mListener != null) {
                    ExplosionField.this.mListener.onExplosionEnd();
                }
            }
        });
        explosion.setStartDelay(startDelay);
        explosion.setDuration(duration);
        this.mExplosions.add(explosion);
        explosion.start();
    }

    public void explode(View view) {
        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        int[] location = new int[2];
        getLocationOnScreen(location);
        r.offset(-location[0], -location[1]);
        r.inset(-this.mExpandInset[0], -this.mExpandInset[1]);
        view.animate().setDuration(150).setStartDelay(100).alpha(0.0f).start();
        view.startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim
                .activity_anim_shake));
        explode(Utils.createBitmapFromView(view), r, 100, ExplosionAnimator.DEFAULT_DURATION);
    }

    public void clear() {
        this.mExplosions.clear();
        invalidate();
    }

    public static ExplosionField attach2Window(Activity activity) {
        ViewGroup      rootView       = (ViewGroup) activity.findViewById(Window
                .ID_ANDROID_CONTENT);
        ExplosionField explosionField = new ExplosionField(activity);
        rootView.addView(explosionField, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return explosionField;
    }

}
