package com.ktc.ui.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.ui.blurbg.BlurBgLayout;
import com.ktc.ui.blurbg.BlurBgLayout.PAGETYPE;
import com.ktc.util.KtcScreenParams;
import com.ktc.util.MyFocusFrame;
import com.horion.tv.R;

////import org.apache.http4.HttpStatus;
@SuppressWarnings("ResourceType")
public class SkyDialogView extends FrameLayout {
    private AlphaAnimation alphaAnim;
    private AnimationSet animSet;
    OnFocusChangeListener changeListener = new OnFocusChangeListener() {
        public void onFocusChange(View view, boolean z) {
            ((TextView) view).setSelected(z);
            if (z) {
                ((TextView) view).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                view.setBackgroundResource(R.drawable.ui_sdk_shape_focus_white);
                SkyDialogView.this.focusView.changeFocusPos(view);
                return;
            }
            ((TextView) view).setTextColor(Color.BLACK);
            view.setBackgroundResource(R.drawable.ui_sdk_btn_unfocus_big_shadow);
        }
    };
    OnClickListener clickListener = new OnClickListener() {
        public void onClick(View view) {
            int id = view.getId();
            if (id == 111) {
                if (SkyDialogView.this.mListener != null) {
                    SkyDialogView.this.mListener.onFirstBtnOnClickEvent();
                }
            } else if (id == 222 && SkyDialogView.this.mListener != null) {
                SkyDialogView.this.mListener.onSecondBtnOnClickEvent();
            }
        }
    };
    private LinearLayout content;
    protected FrameLayout contentView;
    protected TextView firstBtn;
    private MyFocusFrame focusView;
    private boolean isTwoString = false;
    private Context mContext;
    private OnDialogOnKeyListener mListener;
    OnKeyListener onKeyListener = new OnKeyListener() {
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0) {
                if (i == 21) {
                    if (SkyDialogView.this.twoBtn) {
                        if (view.getId() == 111) {
                            return true;
                        }
                        if (view.getId() == 222) {
                            SkyDialogView.this.firstBtn.requestFocus();
                            return true;
                        }
                    } else if (view.getId() == 111) {
                        return true;
                    }
                } else if (i == 22) {
                    if (SkyDialogView.this.twoBtn) {
                        if (view.getId() == 222) {
                            return true;
                        }
                        if (view.getId() == 111) {
                            SkyDialogView.this.secondBtn.requestFocus();
                            return true;
                        }
                    } else if (view.getId() == 111) {
                        return true;
                    }
                } else if (i != 19 && i != 20) {
                    return SkyDialogView.this.mListener != null ? SkyDialogView.this.mListener.onDialogOnKeyEvent(i, keyEvent) : false;
                } else {
                    if (view.getId() == 111) {
                        return true;
                    }
                    if (view.getId() == 222) {
                        return true;
                    }
                }
            }
            return false;
        }
    };
    protected TextView secondBtn;
    private int shaderSize = 0;
    protected TextView tipsView_1;
    protected TextView tipsView_2;
    private TranslateAnimation transAnim;
    private boolean twoBtn = true;
    private int unfocusShaderSize = 0;

    public interface OnDialogOnKeyListener {
        boolean onDialogOnKeyEvent(int i, KeyEvent keyEvent);

        void onFirstBtnOnClickEvent();

        void onSecondBtnOnClickEvent();
    }

    public SkyDialogView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.shaderSize = KtcScreenParams.getInstence(this.mContext).getResolutionValue(94);
        this.unfocusShaderSize = KtcScreenParams.getInstence(this.mContext).getResolutionValue(12);
        initView();
    }

    public SkyDialogView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        this.shaderSize = KtcScreenParams.getInstence(this.mContext).getResolutionValue(94);
        this.unfocusShaderSize = KtcScreenParams.getInstence(this.mContext).getResolutionValue(12);
        initView();
    }

    public SkyDialogView(Context context, boolean z) {
        super(context);
        this.twoBtn = z;
        this.mContext = context;
        this.shaderSize = KtcScreenParams.getInstence(this.mContext).getResolutionValue(94);
        this.unfocusShaderSize = KtcScreenParams.getInstence(this.mContext).getResolutionValue(12);
        initView();
    }

    private void initAnim() {
        this.animSet = new AnimationSet(true);
        this.transAnim = new TranslateAnimation(0.0f, 0.0f, (float) (-KtcScreenParams.getInstence(this.mContext).getResolutionValue(495)), 0.0f);
        this.transAnim.setDuration(100);
        Animation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(100);
        this.alphaAnim = new AlphaAnimation(0.0f, 1.0f);
        this.alphaAnim.setDuration(100);
        this.animSet.addAnimation(scaleAnimation);
        this.animSet.addAnimation(this.alphaAnim);
    }

    private void initView() {
        initAnim();
        setFocusable(false);
        setLayoutParams(new LayoutParams(-1, -1));
        this.contentView = new FrameLayout(this.mContext);
        this.contentView.setFocusable(false);
        this.contentView.setBackgroundResource(R.drawable.ui_sdk_dialog_shadow);
        LayoutParams layoutParams = new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(1061) + (this.shaderSize * 2), KtcScreenParams.getInstence(this.mContext).getResolutionValue(492) + (this.shaderSize * 2));
        layoutParams.gravity = Gravity.CENTER;
        addView(this.contentView, layoutParams);
        BlurBgLayout blurBgLayout = new BlurBgLayout(this.mContext);
        blurBgLayout.setPageType(PAGETYPE.OTHER_PAGE);
        blurBgLayout.setBgAlpha(1.0f);
        this.contentView.addView(blurBgLayout, new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(1061), KtcScreenParams.getInstence(this.mContext).getResolutionValue(492)));
        this.focusView = new MyFocusFrame(this.mContext, KtcScreenParams.getInstence(this.mContext).getResolutionValue(83 - this.unfocusShaderSize));
        this.focusView.needAnimtion(true);
        this.focusView.setImgResourse(R.drawable.ui_sdk_btn_focus_shadow_no_bg);
        addView(this.focusView, new LayoutParams(-2, -2));
        this.tipsView_1 = new TextView(this.mContext);
        this.tipsView_1.setText(" ");
        this.tipsView_1.setFocusable(false);
        this.tipsView_1.setLineSpacing((float) KtcScreenParams.getInstence(this.mContext).getResolutionValue(13), 1.0f);
        this.tipsView_1.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(40));
        this.tipsView_1.setTextColor(this.mContext.getResources().getColor(R.color.setting_text_gray_dark));
        this.tipsView_1.setEllipsize(TruncateAt.END);
        this.tipsView_1.setGravity(17);
        this.tipsView_1.setVisibility(View.GONE);
        this.tipsView_2 = new TextView(this.mContext);
        this.tipsView_2.setFocusable(false);
        this.tipsView_2.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(32));
        KtcScreenParams.getInstence(this.mContext).settextAlpha(this.tipsView_2, 255, "5b5b5b");
        this.tipsView_2.setGravity(17);
        this.tipsView_2.setVisibility(View.GONE);
        this.firstBtn = new TextView(this.mContext);
        this.firstBtn.setFocusable(true);
        this.firstBtn.setFocusableInTouchMode(true);
        this.firstBtn.setId(111);
        this.firstBtn.setGravity(17);
        this.firstBtn.setSingleLine(true);
        this.firstBtn.setHorizontallyScrolling(true);
        this.firstBtn.setEllipsize(TruncateAt.MARQUEE);
        this.firstBtn.setMarqueeRepeatLimit(-1);
        this.firstBtn.setText(R.string.dialog_btn_1);
        this.firstBtn.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(36));
        this.firstBtn.setTextColor(this.mContext.getResources().getColor(R.color.ff5b5b5b));
        this.firstBtn.setOnKeyListener(this.onKeyListener);
        this.firstBtn.setOnClickListener(this.clickListener);
        this.firstBtn.setOnFocusChangeListener(this.changeListener);
        this.firstBtn.setBackgroundResource(R.drawable.ui_sdk_btn_unfocus_big_shadow);
        this.secondBtn = new TextView(this.mContext);
        this.secondBtn.setFocusable(true);
        this.secondBtn.setFocusableInTouchMode(true);
        this.secondBtn.setId(222);
        this.secondBtn.setGravity(17);
        this.secondBtn.setSingleLine(true);
        this.secondBtn.setHorizontallyScrolling(true);
        this.secondBtn.setEllipsize(TruncateAt.MARQUEE);
        this.secondBtn.setMarqueeRepeatLimit(-1);
        this.secondBtn.setText(R.string.dialog_btn_2);
        this.secondBtn.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(36));
        this.secondBtn.setTextColor(this.mContext.getResources().getColor(R.color.ff5b5b5b));
        this.secondBtn.setOnKeyListener(this.onKeyListener);
        this.secondBtn.setOnClickListener(this.clickListener);
        this.secondBtn.setOnFocusChangeListener(this.changeListener);
        this.secondBtn.setBackgroundResource(R.drawable.ui_sdk_btn_unfocus_big_shadow);
        this.content = new LinearLayout(this.mContext);
        this.content.setFocusable(false);
        this.content.setOrientation(LinearLayout.VERTICAL);
        this.content.setGravity(1);
        this.contentView.addView(this.content, new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(1061), KtcScreenParams.getInstence(this.mContext).getResolutionValue(492)));
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams1.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(82);
        this.content.addView(this.tipsView_1, layoutParams1);
        this.tipsView_1.setMaxWidth(KtcScreenParams.getInstence(this.mContext).getResolutionValue(845));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(20);
        this.content.addView(this.tipsView_2, layoutParams2);
        LinearLayout blurBgLayout1 = new LinearLayout(this.mContext);
        blurBgLayout1.setFocusable(false);
        blurBgLayout1.setOrientation(LinearLayout.HORIZONTAL);
        blurBgLayout1.setGravity(17);
        LayoutParams layoutParams3 = new LayoutParams(-1, KtcScreenParams.getInstence(this.mContext).getResolutionValue(73) + (this.unfocusShaderSize * 2));
        layoutParams3.gravity = 81;
        layoutParams3.bottomMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(85) - this.unfocusShaderSize;
        this.contentView.addView(blurBgLayout1, layoutParams3);
        blurBgLayout1.addView(this.firstBtn, new LinearLayout.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(300) + (this.unfocusShaderSize * 2), KtcScreenParams.getInstence(this.mContext).getResolutionValue(73) + (this.unfocusShaderSize * 2)));
        if (this.twoBtn) {
            LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(300) + (this.unfocusShaderSize * 2), KtcScreenParams.getInstence(this.mContext).getResolutionValue(73) + (this.unfocusShaderSize * 2));
            layoutParams4.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(58) - (this.unfocusShaderSize * 2);
            blurBgLayout1.addView(this.secondBtn, layoutParams4);
        }
        setVisibility(View.GONE);
    }

    private void setTextStyle() {
        LinearLayout.LayoutParams layoutParams;
        if (this.tipsView_1.getWidth() >= KtcScreenParams.getInstence(this.mContext).getResolutionValue(845)) {
            layoutParams = (LinearLayout.LayoutParams) this.tipsView_1.getLayoutParams();
            if (this.isTwoString) {
                layoutParams.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(82);
            } else {
                layoutParams.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(82);
            }
            layoutParams.width = KtcScreenParams.getInstence(this.mContext).getResolutionValue(845);
            layoutParams.height = KtcScreenParams.getInstence(this.mContext).getResolutionValue(110);
            this.tipsView_1.setLayoutParams(layoutParams);
            this.tipsView_1.setEllipsize(TruncateAt.END);
            layoutParams = (LinearLayout.LayoutParams) this.tipsView_2.getLayoutParams();
            layoutParams.width = KtcScreenParams.getInstence(this.mContext).getResolutionValue(845);
            this.tipsView_2.setLayoutParams(layoutParams);
            this.tipsView_1.setGravity(19);
            this.tipsView_1.postInvalidate();
            this.tipsView_2.setGravity(19);
            this.tipsView_2.postInvalidate();
            return;
        }
        layoutParams = (LinearLayout.LayoutParams) this.tipsView_1.getLayoutParams();
        if (!this.isTwoString) {
            layoutParams.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(143);
        }
        layoutParams.width = -2;
        layoutParams.height = -2;
        this.tipsView_1.setLayoutParams(layoutParams);
        layoutParams = (LinearLayout.LayoutParams) this.tipsView_2.getLayoutParams();
        layoutParams.width = -2;
        this.tipsView_2.setLayoutParams(layoutParams);
        this.tipsView_1.setGravity(17);
        this.tipsView_1.postInvalidate();
        this.tipsView_2.setGravity(17);
        this.tipsView_2.postInvalidate();
    }

    public void dismiss() {
        clearAnimation();
        AnimationSet animationSet = new AnimationSet(true);
        Animation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(100);
        animationSet.addAnimation(scaleAnimation);
        scaleAnimation = new AlphaAnimation(1.0f, 0.0f);
        scaleAnimation.setDuration(100);
        scaleAnimation.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);
        this.contentView.startAnimation(animationSet);
        animationSet.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                SkyDialogView.this.setVisibility(View.GONE);
                SkyDialogView.this.clearAnimation();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.tipsView_1.setText(" ");
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.tipsView_1.getLayoutParams();
        layoutParams.width = -2;
        this.tipsView_1.setLayoutParams(layoutParams);
        this.tipsView_2.setText(" ");
        layoutParams = (LinearLayout.LayoutParams) this.tipsView_2.getLayoutParams();
        layoutParams.width = -2;
        this.tipsView_2.setLayoutParams(layoutParams);
    }

    public void setBtnString(String str, String str2) {
        if (!(str == null || str.equals(""))) {
            this.firstBtn.setText(str);
        }
        if (str2 != null && !str2.equals("")) {
            this.secondBtn.setText(str2);
        }
    }

    public void setOnDialogOnKeyListener(OnDialogOnKeyListener onDialogOnKeyListener) {
        this.mListener = onDialogOnKeyListener;
    }

    @SuppressLint({"RtlHardcoded"})
    public void setTipsString(String str, String str2) {
        if (str == null || str.equals("")) {
            this.tipsView_1.setVisibility(View.GONE);
        } else {
            this.tipsView_1.setText(str);
            this.tipsView_1.post(new Runnable() {
                public void run() {
                    SkyDialogView.this.setTextStyle();
                    SkyDialogView.this.tipsView_1.setVisibility(View.VISIBLE);
                }
            });
        }
        if (str2 == null || str2.equals("")) {
            this.isTwoString = false;
            this.tipsView_2.setVisibility(View.GONE);
            return;
        }
        this.isTwoString = true;
        this.tipsView_2.setText(str2);
        this.tipsView_2.setVisibility(View.VISIBLE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
        this.contentView.startAnimation(this.animSet);
        this.animSet.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                SkyDialogView.this.setTextStyle();
                SkyDialogView.this.contentView.clearAnimation();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                if (!SkyDialogView.this.firstBtn.hasFocus()) {
                    SkyDialogView.this.firstBtn.requestFocus();
                }
            }
        });
    }
}
