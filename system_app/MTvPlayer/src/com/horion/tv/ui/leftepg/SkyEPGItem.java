package com.horion.tv.ui.leftepg;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.framework.skycommondefine.SkyworthBroadcastKey;
import com.ktc.util.KtcScreenParams;

public class SkyEPGItem extends FrameLayout implements OnFocusChangeListener {
    private ScaleAnimation acquireFocusAnim;
    private LinearLayout contentLayout;
    private SkyEPGData currentData;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 0:
                    KtcScreenParams.getInstence(SkyEPGItem.this.mContext).settextAlpha(SkyEPGItem.this.itemTextView, 255, "000000");
                    return;
                case 1:
                    KtcScreenParams.getInstence(SkyEPGItem.this.mContext).settextAlpha(SkyEPGItem.this.itemTextView, 255, "505050");
                    return;
                default:
                    return;
            }
        }
    };
    private boolean isSelect = false;
    private TextView itemTextView;
    private Context mContext;
    private OnItemFocusChangedListener mListener;
    private ScaleAnimation releaseFocusAnim;
    private int textSize = 0;

    public interface OnItemFocusChangedListener {
        void itemFocusListener(SkyEPGItem skyEPGItem, boolean z);
    }

    public SkyEPGItem(Context context) {
        super(context);
        this.mContext = context;
        initView();
        setFocusable(true);
        setOnFocusChangeListener(this);
    }

    private void initAnim() {
        this.acquireFocusAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, 1, 0.5f, 1, 0.5f);
        this.acquireFocusAnim.setFillAfter(true);
        this.acquireFocusAnim.setDuration(110);
        this.acquireFocusAnim.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                ///TVUIDebug.debug("SkyEPGItem acquireFocusAnim onAnimationEnd");
            }

            public void onAnimationRepeat(Animation animation) {
                ///TVUIDebug.debug("SkyEPGItem acquireFocusAnim onAnimationRepeat");
            }

            public void onAnimationStart(Animation animation) {
                ///TVUIDebug.debug("SkyEPGItem acquireFocusAnim onAnimationStart");
            }
        });
        this.releaseFocusAnim = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, 1, 0.5f, 1, 0.5f);
        this.releaseFocusAnim.setFillAfter(true);
        this.releaseFocusAnim.setDuration(110);
        this.releaseFocusAnim.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                ///TVUIDebug.debug("SkyEPGItem releaseFocusAnim onAnimationEnd");
                SkyEPGItem.this.contentLayout.clearAnimation();
            }

            public void onAnimationRepeat(Animation animation) {
                //TVUIDebug.debug("SkyEPGItem releaseFocusAnim onAnimationRepeat");
            }

            public void onAnimationStart(Animation animation) {
                ///TVUIDebug.debug("SkyEPGItem releaseFocusAnim onAnimationStart");
            }
        });
    }

    private void initView() {
        this.contentLayout = new LinearLayout(this.mContext);
        this.contentLayout.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(28), 0, KtcScreenParams.getInstence(this.mContext).getResolutionValue(28), 0);
        this.contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.contentLayout.setGravity(17);
        this.itemTextView = new TextView(this.mContext);
        this.itemTextView.setGravity(17);
        this.itemTextView.setSingleLine(true);
        this.contentLayout.addView(this.itemTextView, new LayoutParams(-2, -2));
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = Gravity.CENTER;
        addView(this.contentLayout, layoutParams);
        initAnim();
    }

    public void focus() {
        ///TVUIDebug.debug("SkyEPGItem focus isSelect:" + this.isSelect);
        if (!(this.isSelect || this.isSelect)) {
            this.contentLayout.startAnimation(this.acquireFocusAnim);
        }
        this.handler.sendEmptyMessageDelayed(0, 100);
    }

    public SkyEPGData getItemData() {
        return this.currentData;
    }

    public void onFocusChange(View view, boolean z) {
        ///TVUIDebug.debug("SkyEPGItem onFocusChange v:" + view + "  hasFocus:" + z);
        Log.d("Maxs24","SkyEPGItem onFocusChange v:" + view + "  hasFocus:" + z);
        Log.d("Maxs24","SkyEPGItem onFocusChange this.mListener != null = " +(mListener != null));
        if (this.mListener != null) {
            this.mListener.itemFocusListener(this, z);
        }
        if (z) {
            focus();
            LayoutParams layoutParams = (LayoutParams) SkyCommenEPG.arrowView.getLayoutParams();
            int resolutionValue = KtcScreenParams.getInstence(this.mContext).getResolutionValue(49) + getRight();
            int resolutionValue2 = KtcScreenParams.getInstence(this.mContext).getResolutionValue(SkyCommenEPG.EPG2_LEFT_MARGIN) - resolutionValue;
            layoutParams.leftMargin = ((resolutionValue2 / 2) + resolutionValue) - (SkyCommenEPG.arrowView.getWidth() / 2);
            ///TVUIDebug.debug("  SkyEPGItem right:" + resolutionValue + "  arrow width:" + SkyCommenEPG.arrowView.getWidth() + "  distance:" + resolutionValue2);
            SkyCommenEPG.arrowView.setLayoutParams(layoutParams);
            int width = getWidth() + (KtcScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2);
            resolutionValue = KtcScreenParams.getInstence(this.mContext).getResolutionValue(SkyworthBroadcastKey.SKY_BROADCAST_KEY_IMAGE_MODE);
           /// TVUIDebug.debug("SkyEPGItem.this.getLeft():" + getLeft());
            resolutionValue2 = (KtcScreenParams.getInstence(this.mContext).getResolutionValue(49) + getLeft()) - KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
            int resolutionValue3 = (KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080) / 2) - (KtcScreenParams.getInstence(this.mContext).getResolutionValue(SkyworthBroadcastKey.SKY_BROADCAST_KEY_IMAGE_MODE) / 2);
            if (SkyCommenEPG.focusView.getVisibility() != View.VISIBLE) {
                SkyCommenEPG.focusView.initStarPosition(resolutionValue2, resolutionValue3, width, resolutionValue);
                SkyCommenEPG.focusView.setVisibility(View.VISIBLE);
                return;
            }
            SkyCommenEPG.focusView.changeFocusPos(resolutionValue2, resolutionValue3, width, resolutionValue);
            return;
        }
        unfocus();
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
            alphaAnimation = new AlphaAnimation(0.3f, 0.7f);
            alphaAnimation.setDuration(200);
            alphaAnimation.setFillAfter(true);
            this.contentLayout.startAnimation(alphaAnimation);
        }
    }

    public void setOnItemFocusChangedListener(OnItemFocusChangedListener onItemFocusChangedListener) {
        this.mListener = onItemFocusChangedListener;
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
       /// TVUIDebug.debug("SkyEPGItem unfocus isSelect:" + this.isSelect);
        if (!this.isSelect && !this.isSelect) {
            this.contentLayout.startAnimation(this.releaseFocusAnim);
            this.handler.sendEmptyMessageDelayed(1, 100);
        }
    }

    public void updataItemValue(SkyEPGData skyEPGData) {
        if (skyEPGData != null) {
            this.currentData = skyEPGData;
            if (skyEPGData.getItemTitle() != null) {
                this.itemTextView.setText(skyEPGData.getItemTitle());
            }
            KtcScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "505050");
        }
    }
}
