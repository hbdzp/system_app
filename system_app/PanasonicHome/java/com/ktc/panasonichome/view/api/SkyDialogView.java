package com.ktc.panasonichome.view.api;

import android.content.Context;
import android.graphics.Color;
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

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.MyFocusFrame;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout.PAGETYPE;

public class SkyDialogView extends FrameLayout {
    private AlphaAnimation alphaAnim;
    private AnimationSet animSet;
    OnFocusChangeListener changeListener = new C02941();
    OnClickListener clickListener = new C02963();
    private   LinearLayout content;
    protected FrameLayout  contentView;
    protected TextView     firstBtn;
    private   MyFocusFrame focusView;
    private boolean isTwoString = false;
    private Context mContext;
    private OnDialogOnKeyListener mListener;
    OnKeyListener onKeyListener = new C02952();
    protected TextView secondBtn;
    private int shaderSize = 0;
    protected TextView tipsView_1;
    protected TextView tipsView_2;
    private TranslateAnimation transAnim;
    private boolean twoBtn = true;
    private int unfocusShaderSize = 0;

    class C02941 implements OnFocusChangeListener {
        C02941() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            ((TextView) v).setSelected(hasFocus);
            if (hasFocus) {
                ((TextView) v).setTextColor(Color.BLACK);
                v.setBackgroundResource(R.drawable.ui_sdk_shape_focus_white);
                SkyDialogView.this.focusView.changeFocusPos(v);
                return;
            }
            ((TextView) v).setTextColor(Color.parseColor("#FF5B5B5B"));
            v.setBackgroundResource(R.drawable.ui_sdk_btn_unfocus_big_shadow);
        }
    }

    class C02952 implements OnKeyListener {
        C02952() {
        }

        @SuppressWarnings("ResourceType")
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (SkyDialogView.this.twoBtn) {
                        if (v.getId() == 111) {
                            return true;
                        }
                        if (v.getId() == 222) {
                            SkyDialogView.this.firstBtn.requestFocus();
                            return true;
                        }
                    } else if (v.getId() == 111) {
                        return true;
                    }
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (SkyDialogView.this.twoBtn) {
                        if (v.getId() == 222) {
                            return true;
                        }
                        if (v.getId() == 111) {
                            SkyDialogView.this.secondBtn.requestFocus();
                            return true;
                        }
                    } else if (v.getId() == 111) {
                        return true;
                    }
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent
                        .KEYCODE_DPAD_DOWN) {
                    if (v.getId() == 111 || v.getId() == 222) {
                        return true;
                    }
                } else if (SkyDialogView.this.mListener != null) {
                    return SkyDialogView.this.mListener.onDialogOnKeyEvent(keyCode, event);
                } else {
                    return false;
                }
            }
            return false;
        }
    }

    class C02963 implements OnClickListener {
        C02963() {
        }

        public void onClick(View v) {
            int id = v.getId();
            if (id == 111) {
                if (SkyDialogView.this.mListener != null) {
                    SkyDialogView.this.mListener.onFirstBtnOnClickEvent();
                }
            } else if (id == 222 && SkyDialogView.this.mListener != null) {
                SkyDialogView.this.mListener.onSecondBtnOnClickEvent();
            }
        }
    }

    class C02974 implements Runnable {
        C02974() {
        }

        public void run() {
            SkyDialogView.this.setTextStyle();
            SkyDialogView.this.tipsView_1.setVisibility(View.VISIBLE);
        }
    }

    class C02985 implements AnimationListener {
        C02985() {
        }

        public void onAnimationStart(Animation animation) {
            if (!SkyDialogView.this.firstBtn.hasFocus()) {
                SkyDialogView.this.firstBtn.requestFocus();
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            SkyDialogView.this.setTextStyle();
            SkyDialogView.this.contentView.clearAnimation();
        }
    }

    class C02996 implements AnimationListener {
        C02996() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            SkyDialogView.this.setVisibility(View.GONE);
            SkyDialogView.this.clearAnimation();
        }
    }

    public interface OnDialogOnKeyListener {
        boolean onDialogOnKeyEvent(int i, KeyEvent keyEvent);

        void onFirstBtnOnClickEvent();

        void onSecondBtnOnClickEvent();
    }

    public SkyDialogView(Context context, boolean twoBtn) {
        super(context);
        this.twoBtn = twoBtn;
        this.mContext = context;
        this.shaderSize = ScreenParams.getInstence(this.mContext).getResolutionValue(94);
        this.unfocusShaderSize = ScreenParams.getInstence(this.mContext).getResolutionValue(12);
        initView();
    }

    public SkyDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.shaderSize = ScreenParams.getInstence(this.mContext).getResolutionValue(94);
        this.unfocusShaderSize = ScreenParams.getInstence(this.mContext).getResolutionValue(12);
        initView();
    }

    public SkyDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        this.shaderSize = ScreenParams.getInstence(this.mContext).getResolutionValue(94);
        this.unfocusShaderSize = ScreenParams.getInstence(this.mContext).getResolutionValue(12);
        initView();
    }

    public void setOnDialogOnKeyListener(OnDialogOnKeyListener listener) {
        this.mListener = listener;
    }

    @SuppressWarnings("ResourceType")
    private void initView() {
        initAnim();
        setFocusable(false);
        setLayoutParams(new LayoutParams(-1, -1));
        this.contentView = new FrameLayout(this.mContext);
        this.contentView.setFocusable(false);
        this.contentView.setBackgroundResource(R.drawable.ui_sdk_dialog_shadow);
        LayoutParams content_p = new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(1061) + (this.shaderSize * 2), ScreenParams.getInstence(this.mContext).getResolutionValue(492) + (this.shaderSize * 2));
        content_p.gravity = 17;
        addView(this.contentView, content_p);
        BlurBgLayout blurBgLayout = new BlurBgLayout(this.mContext);
        blurBgLayout.setPageType(PAGETYPE.OTHER_PAGE);
        blurBgLayout.setBgAlpha(1.0f);
        this.contentView.addView(blurBgLayout, new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(1061), ScreenParams.getInstence(this.mContext).getResolutionValue(492)));
        this.focusView = new MyFocusFrame(this.mContext, ScreenParams.getInstence(this.mContext).getResolutionValue(83 - this.unfocusShaderSize));
        this.focusView.needAnimtion(true);
        this.focusView.setImgResourse(R.drawable.ui_sdk_btn_focus_shadow_no_bg);
        addView(this.focusView, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        this.tipsView_1 = new TextView(this.mContext);
        this.tipsView_1.setText(" ");
        this.tipsView_1.setFocusable(false);
        this.tipsView_1.setLineSpacing((float) ScreenParams.getInstence(this.mContext).getResolutionValue(13), 1.0f);
        this.tipsView_1.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(40));
        this.tipsView_1.setTextColor(this.mContext.getResources().getColor(R.color.ff333333));
        this.tipsView_1.setEllipsize(TruncateAt.END);
        this.tipsView_1.setGravity(17);
        this.tipsView_1.setVisibility(View.GONE);
        this.tipsView_2 = new TextView(this.mContext);
        this.tipsView_2.setFocusable(false);
        this.tipsView_2.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(32));
        ScreenParams.getInstence(this.mContext).settextAlpha(this.tipsView_2, 255, "5b5b5b");
        this.tipsView_2.setGravity(Gravity.CENTER);
        this.tipsView_2.setVisibility(View.GONE);
        this.firstBtn = new TextView(this.mContext);
        this.firstBtn.setFocusable(true);
        this.firstBtn.setFocusableInTouchMode(true);
        this.firstBtn.setId(111);
        this.firstBtn.setGravity(Gravity.CENTER);
        this.firstBtn.setSingleLine(true);
        this.firstBtn.setHorizontallyScrolling(true);
        this.firstBtn.setEllipsize(TruncateAt.MARQUEE);
        this.firstBtn.setMarqueeRepeatLimit(-1);
        this.firstBtn.setText(R.string.dialog_btn_1);
        this.firstBtn.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(36));
        this.firstBtn.setTextColor(this.mContext.getResources().getColor(R.color.ff5b5b5b));
        this.firstBtn.setOnKeyListener(this.onKeyListener);
        this.firstBtn.setOnClickListener(this.clickListener);
        this.firstBtn.setOnFocusChangeListener(this.changeListener);
        this.firstBtn.setBackgroundResource(R.drawable.ui_sdk_btn_unfocus_big_shadow);
        this.secondBtn = new TextView(this.mContext);
        this.secondBtn.setFocusable(true);
        this.secondBtn.setFocusableInTouchMode(true);
        this.secondBtn.setId(222);
        this.secondBtn.setGravity(Gravity.CENTER);
        this.secondBtn.setSingleLine(true);
        this.secondBtn.setHorizontallyScrolling(true);
        this.secondBtn.setEllipsize(TruncateAt.MARQUEE);
        this.secondBtn.setMarqueeRepeatLimit(-1);
        this.secondBtn.setText(R.string.dialog_btn_2);
        this.secondBtn.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(36));
        this.secondBtn.setTextColor(this.mContext.getResources().getColor(R.color.ff5b5b5b));
        this.secondBtn.setOnKeyListener(this.onKeyListener);
        this.secondBtn.setOnClickListener(this.clickListener);
        this.secondBtn.setOnFocusChangeListener(this.changeListener);
        this.secondBtn.setBackgroundResource(R.drawable.ui_sdk_btn_unfocus_big_shadow);
        this.content = new LinearLayout(this.mContext);
        this.content.setFocusable(false);
        this.content.setOrientation(LinearLayout.VERTICAL);
        this.content.setGravity(Gravity.CENTER_HORIZONTAL);
        this.contentView.addView(this.content, new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(1061), ScreenParams.getInstence(this.mContext).getResolutionValue(492)));
        LinearLayout.LayoutParams tip_1_p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        tip_1_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(82);
        this.content.addView(this.tipsView_1, tip_1_p);
        this.tipsView_1.setMaxWidth(ScreenParams.getInstence(this.mContext).getResolutionValue(845));
        LinearLayout.LayoutParams tip_2_p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        tip_2_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(20);
        this.content.addView(this.tipsView_2, tip_2_p);
        LinearLayout btnLayout = new LinearLayout(this.mContext);
        btnLayout.setFocusable(false);
        btnLayout.setOrientation(LinearLayout.HORIZONTAL);
        btnLayout.setGravity(Gravity.CENTER);
        LayoutParams btnLayout_p = new LayoutParams(-1, ScreenParams.getInstence(this.mContext).getResolutionValue(73) + (this.unfocusShaderSize * 2));
        btnLayout_p.gravity = 81;
        btnLayout_p.bottomMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(85) - this.unfocusShaderSize;
        this.contentView.addView(btnLayout, btnLayout_p);
        btnLayout.addView(this.firstBtn, new LinearLayout.LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(300) + (this.unfocusShaderSize * 2), ScreenParams.getInstence(this.mContext).getResolutionValue(73) + (this.unfocusShaderSize * 2)));
        if (this.twoBtn) {
            LinearLayout.LayoutParams btn_second_p = new LinearLayout.LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(300) + (this.unfocusShaderSize * 2), ScreenParams.getInstence(this.mContext).getResolutionValue(73) + (this.unfocusShaderSize * 2));
            btn_second_p.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(58) - (this.unfocusShaderSize * 2);
            btnLayout.addView(this.secondBtn, btn_second_p);
        }
        setVisibility(View.GONE);
    }

    private void initAnim() {
        this.animSet = new AnimationSet(true);
        this.transAnim = new TranslateAnimation(0.0f, 0.0f, (float) (-ScreenParams.getInstence(this.mContext).getResolutionValue(495)), 0.0f);
        this.transAnim.setDuration(100);
        ScaleAnimation acquireFocusAnim = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f, 1, 0.5f, 1, 0.5f);
        acquireFocusAnim.setFillAfter(true);
        acquireFocusAnim.setDuration(100);
        this.alphaAnim = new AlphaAnimation(0.0f, 1.0f);
        this.alphaAnim.setDuration(100);
        this.animSet.addAnimation(acquireFocusAnim);
        this.animSet.addAnimation(this.alphaAnim);
    }

    public void setTipsString(String tips_1, String tips_2) {
        if (tips_1 == null || tips_1.equals("")) {
            this.tipsView_1.setVisibility(View.GONE);
        } else {
            this.tipsView_1.setText(tips_1);
            this.tipsView_1.post(new C02974());
        }
        if (tips_2 == null || tips_2.equals("")) {
            this.isTwoString = false;
            this.tipsView_2.setVisibility(View.GONE);
            return;
        }
        this.isTwoString = true;
        this.tipsView_2.setText(tips_2);
        this.tipsView_2.setVisibility(View.VISIBLE);
    }

    private void setTextStyle() {
        LinearLayout.LayoutParams tip_1_p;
        LinearLayout.LayoutParams tip_2_p;
        if (this.tipsView_1.getWidth() >= ScreenParams.getInstence(this.mContext).getResolutionValue(845)) {
            tip_1_p = (LinearLayout.LayoutParams) this.tipsView_1.getLayoutParams();
            if (this.isTwoString) {
                tip_1_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(82);
            } else {
                tip_1_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(82);
            }
            tip_1_p.width = ScreenParams.getInstence(this.mContext).getResolutionValue(845);
            tip_1_p.height = ScreenParams.getInstence(this.mContext).getResolutionValue(110);
            this.tipsView_1.setLayoutParams(tip_1_p);
            this.tipsView_1.setEllipsize(TruncateAt.END);
            tip_2_p = (LinearLayout.LayoutParams) this.tipsView_2.getLayoutParams();
            tip_2_p.width = ScreenParams.getInstence(this.mContext).getResolutionValue(845);
            this.tipsView_2.setLayoutParams(tip_2_p);
            this.tipsView_1.setGravity(19);
            this.tipsView_1.postInvalidate();
            this.tipsView_2.setGravity(19);
            this.tipsView_2.postInvalidate();
            return;
        }
        tip_1_p = (LinearLayout.LayoutParams) this.tipsView_1.getLayoutParams();
        if (!this.isTwoString) {
            tip_1_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(143);
        }
        tip_1_p.width = -2;
        tip_1_p.height = -2;
        this.tipsView_1.setLayoutParams(tip_1_p);
        tip_2_p = (LinearLayout.LayoutParams) this.tipsView_2.getLayoutParams();
        tip_2_p.width = -2;
        this.tipsView_2.setLayoutParams(tip_2_p);
        this.tipsView_1.setGravity(17);
        this.tipsView_1.postInvalidate();
        this.tipsView_2.setGravity(17);
        this.tipsView_2.postInvalidate();
    }

    public void setBtnString(String btn_1, String btn_2) {
        if (!(btn_1 == null || btn_1.equals(""))) {
            this.firstBtn.setText(btn_1);
        }
        if (btn_2 != null && !btn_2.equals("")) {
            this.secondBtn.setText(btn_2);
        }
    }

    public void show() {
        setVisibility(View.VISIBLE);
        this.contentView.startAnimation(this.animSet);
        this.animSet.setAnimationListener(new C02985());
    }

    public void dismiss() {
        clearAnimation();
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation acquireFocusAnim = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f, 1, 0.5f, 1, 0.5f);
        acquireFocusAnim.setFillAfter(true);
        acquireFocusAnim.setDuration(100);
        set.addAnimation(acquireFocusAnim);
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);
        alphaAnim.setDuration(100);
        alphaAnim.setFillAfter(true);
        set.addAnimation(alphaAnim);
        this.contentView.startAnimation(set);
        set.setAnimationListener(new C02996());
        this.tipsView_1.setText(" ");
        LinearLayout.LayoutParams tip_1_p = (LinearLayout.LayoutParams) this.tipsView_1.getLayoutParams();
        tip_1_p.width = -2;
        this.tipsView_1.setLayoutParams(tip_1_p);
        this.tipsView_2.setText(" ");
        LinearLayout.LayoutParams tip_2_p = (LinearLayout.LayoutParams) this.tipsView_2.getLayoutParams();
        tip_2_p.width = -2;
        this.tipsView_2.setLayoutParams(tip_2_p);
    }
}
