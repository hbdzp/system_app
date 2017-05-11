package com.ktc.panasonichome.view.statebar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.ktc.panasonichome.LauncherActivity;
import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.HomeStateBarLayout;
import com.ktc.panasonichome.view.MyFocusFrame;

public class StateBarItem extends FrameLayout implements OnFocusChangeListener {
    private MyFocusFrame      focusView;
    private ImageView         iconView;
    private Context           mContext;
    private TextView          msgCountView;
    private AnimationDrawable netAnimationDrawable;
    private TextView          titleView;

    public StateBarItem(Context context, MyFocusFrame focusFrame) {
        super(context);
        this.mContext = context;
        this.focusView = focusFrame;
        initValue();
        initView();
    }

    public StateBarItem(Context context, String type, MyFocusFrame focusFrame) {
        super(context);
        this.mContext = context;
        this.focusView = focusFrame;
        initValue();
    }

    private void initValue() {
        setPadding(ScreenParams.getInstence(this.mContext).getResolutionValue(24), ScreenParams
                .getInstence(this.mContext).getResolutionValue(10), ScreenParams.getInstence(this
                .mContext).getResolutionValue(30), ScreenParams.getInstence(this.mContext)
                .getResolutionValue(8));
        setFocusable(false);
        setOnFocusChangeListener(this);
    }

    private void initView() {
        this.iconView = new ImageView(this.mContext);
        addView(this.iconView, new LayoutParams(-2, -2, 16));
        this.titleView = new TextView(this.mContext);
        this.titleView.setTextColor(Color.BLACK);
        this.titleView.setTextSize((float) ScreenParams.getInstence(this.mContext)
                .getTextDpiValue(32));
        LayoutParams title_p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        this.titleView.setVisibility(View.GONE);
        title_p.gravity = 16;
        title_p.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(56);
        addView(this.titleView, title_p);
    }

    public void initNetItemValue() {
        this.iconView.setImageResource(R.drawable.net_animation);
        this.netAnimationDrawable = (AnimationDrawable) this.iconView.getDrawable();
        this.netAnimationDrawable.setOneShot(false);
        this.netAnimationDrawable.start();
    }

    public void setItemValue(int res, int title, String msgCount) {
        if (this.netAnimationDrawable != null) {
            this.netAnimationDrawable.stop();
        }
        this.iconView.setImageResource(res);
        this.titleView.setText(title);
        this.titleView.post(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("dpz", "lastFocusView:" + LauncherActivity.lastFocusView + "-----" +
                        StateBarItem.this);
                if (LauncherActivity.lastFocusView == null || !LauncherActivity.lastFocusView.equals
                        (StateBarItem.this)) {
                    StateBarItem.this.titleView.setVisibility(View.GONE);
                } else if (LauncherActivity.lastFocusView != null && LauncherActivity.lastFocusView
                        .equals(StateBarItem.this)) {
                    StateBarItem.this.focusView.changeFocusPos(StateBarItem.this);
                }
            }
        });
        if (!TextUtils.isEmpty(msgCount) && this.msgCountView != null) {
            this.msgCountView.setText(msgCount);
            this.msgCountView.setVisibility(View.VISIBLE);
        }
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE && LauncherActivity.lastFocusView != null && LauncherActivity
                .lastFocusView.equals(this)) {
            post(new Runnable() {
                @Override
                public void run() {
                    LogUtils.v("dzp", "LauncherActivity.lastFocusView" + StateBarItem.this.titleView
                            .getText());
                    StateBarItem.this.focusView.changeFocusPos(StateBarItem.this);
                }
            });
        }
    }

    public void onFocusChange(final View v, boolean hasFocus) {
        this.titleView.clearAnimation();
        LogUtils.d("bonfiy", "StateBarItem focusChange");
        if (hasFocus) {
            LauncherActivity.lastFocusView = v;
            this.titleView.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, DefaultRetryPolicy
                    .DEFAULT_BACKOFF_MULT);
            alphaAnimation.setDuration(200);
            alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            if (LauncherActivity.lastFocusView == null || !LauncherActivity.lastFocusView.equals
                    (this)) {
                this.titleView.startAnimation(alphaAnimation);
            }
            post(new Runnable() {
                public void run() {
                    HomeStateBarLayout layout = (HomeStateBarLayout) v.getParent();
                    for (int i = 0; i < layout.indexOfChild(v); i++) {
                        if (layout.getChildAt(i).getVisibility() == View.VISIBLE) {
                            StateBarItem.this.focusView.changeFocusPos(v);
                            v.setBackgroundColor(-1);
                            LauncherActivity.lastFocusView = v;
                            return;
                        }
                    }
                    v.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                    StateBarItem.this.focusView.changeFocusPos(v);
                    v.setBackgroundColor(-1);
                }
            });
        } else {
            HomeStateBarLayout layout = (HomeStateBarLayout) v.getParent();
            for (int i = 0; i < layout.indexOfChild(v); i++) {
                if (layout.getChildAt(i).getVisibility() == View.VISIBLE) {
                    this.titleView.setVisibility(View.GONE);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    return;
                }
            }
            LinearLayout.LayoutParams item_p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            item_p.leftMargin = -ScreenParams.getInstence(this.mContext).getResolutionValue(8);
            v.setLayoutParams(item_p);
            this.titleView.setVisibility(View.GONE);
            v.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public String getItemName() {
        return this.titleView.getText().toString();
    }
}
