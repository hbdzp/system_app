package com.ktc.ui.menu;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
import com.ktc.util.KtcScreenParams;
import com.ktc.util.MyFocusFrame;
import com.horion.tv.R;


public class KtcMenuItem_2 extends FrameLayout implements OnFocusChangeListener {
    private String colorString = "5b5b5b";
    private LinearLayout contentLayout;
    private KtcMenuData currentData;
    private int focusIconID = -1;
    private boolean isSelect = false;
    private ImageView itemIconView;
    private TextView itemTextView;
    private Context mContext;
    private MyFocusFrame menuFocusView;
    private int textSize = 0;
    private int unFocusIconID = -1;

    public KtcMenuItem_2(Context context) {
        super(context);
        this.mContext = context;
        initView();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnFocusChangeListener(this);
    }

    private void initView() {
        this.focusIconID = R.drawable.ui_sdk_second_focus;
        this.unFocusIconID = R.drawable.ui_sdk_second_focus;
        this.contentLayout = new LinearLayout(this.mContext);
        this.contentLayout.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(52), 0, 0, 0);
        this.contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.contentLayout.setGravity(17);
        this.itemIconView = new ImageView(this.mContext);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams1.rightMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(15);
        this.contentLayout.addView(this.itemIconView, layoutParams1);
        this.itemTextView = new TextView(this.mContext);
        this.itemTextView.setSingleLine(true);
        this.itemTextView.getPaint().setAntiAlias(true);
        this.itemTextView.setSingleLine();
        this.itemTextView.setEllipsize(TruncateAt.END);
        this.itemTextView.setMarqueeRepeatLimit(-1);
        this.itemTextView.setGravity(16);
        ////LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(HttpStatus.SC_OK), -1);
        //// HttpStatus.SC_OK = 200
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(220), -1);
        layoutParams2.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(4);
        this.contentLayout.addView(this.itemTextView, layoutParams2);
        addView(this.contentLayout, new LayoutParams(-1, -1));
    }

    public void focus(View view) {
        Log.d("Maxs","KtcMenuItem_2:this.currentData.getItemState() =  " + this.currentData.getItemState());
        if (!(this.focusIconID == -1 || this.currentData.getItemState())) {
            this.itemIconView.setBackgroundResource(this.focusIconID);
        }
        KtcScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "000000");
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
        int i = iArr[0];
        int i2 = iArr[1];
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(324);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(90);
    }

    public KtcMenuData getItemData() {
        return this.currentData;
    }

    public void onFocusChange(View view, boolean z) {
        ////Log.d("Maxs","------------->KtcMenuItem_2:onFocusChange:z = " + z);
        if (z) {
        	////KtcLogger.v("xw", " itemTextView  " + this.itemTextView.getText().toString());
            this.itemTextView.setEllipsize(TruncateAt.MARQUEE);
            this.itemTextView.setSelected(true);
            focus(view);
            return;
        }
        unfocus();
        this.itemTextView.setSelected(false);
        this.itemTextView.setEllipsize(TruncateAt.END);
    }

    public void reSetItemState() {
        if (this.unFocusIconID != -1 && !this.currentData.getItemState()) {
            this.itemIconView.setBackgroundResource(this.unFocusIconID);
        }
    }

    public void refreshItemValue(KtcMenuData ktcMenuData) {
        if (ktcMenuData != null) {
            this.currentData = ktcMenuData;
            if (ktcMenuData.getItemTitle() != null) {
                this.itemTextView.setText(ktcMenuData.getItemTitle() + "　");
            }
            post(new Runnable() {
                public void run() {
                    int i = KtcSecondMenu.preViewWidth;
                    KtcScreenParams.getInstence(KtcMenuItem_2.this.mContext).getResolutionValue(50);
                    KtcScreenParams.getInstence(KtcMenuItem_2.this.mContext).getResolutionValue(30);
                    KtcScreenParams.getInstence(KtcMenuItem_2.this.mContext).getResolutionValue(516);
                    KtcMenuItem_2.this.getWidth();
                    KtcScreenParams.getInstence(KtcMenuItem_2.this.mContext).getResolutionValue(47);
                    KtcScreenParams.getInstence(KtcMenuItem_2.this.mContext).getResolutionValue(170);
                }
            });
        }
    }

    public void setFocusView(MyFocusFrame myFocusFrame) {
        this.menuFocusView = myFocusFrame;
    }

    public void setSelectState(boolean z) {
        this.isSelect = z;
        if (this.isSelect) {
            this.itemIconView.setVisibility(View.VISIBLE);
        } else if (!this.isSelect) {
            this.itemIconView.setVisibility(View.INVISIBLE);
        }
    }

    public void setTextAttribute(int i, int i2) {
        if (this.itemTextView != null) {
            this.textSize = i;
            this.itemTextView.setTextSize((float) i);
            this.itemTextView.setTextColor(i2);
        }
    }

    public void unfocus() {
        if (!(!this.isSelect || this.unFocusIconID == -1 || this.currentData.getItemState())) {
            this.itemIconView.setBackgroundResource(this.unFocusIconID);
        }
        KtcScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, this.colorString);
    }

    public void updataItemValue(KtcMenuData ktcMenuData) {
        if (ktcMenuData != null) {
            this.currentData = ktcMenuData;
            if (ktcMenuData.getItemTitle() != null) {
                this.itemTextView.setText(ktcMenuData.getItemTitle() + "　");
            }
            KtcScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, this.colorString);
            if (ktcMenuData.getItemState()) {
                this.itemIconView.setVisibility(View.INVISIBLE);
                this.itemTextView.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(16), 0, 0, 0);
                return;
            }
            this.itemIconView.setBackgroundResource(this.unFocusIconID);
            this.itemTextView.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(4), 0, 0, 0);
        }
    }
}
