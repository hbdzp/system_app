package com.ktc.panasonichome.view.menu;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.MyFocusFrame;

public class SkyMenuItem_2 extends FrameLayout implements OnFocusChangeListener {
    private String colorString = "5b5b5b";
    private LinearLayout contentLayout;
    private SkyMenuData currentData;
    private int focusIconID = -1;
    private boolean isSelect = false;
    private ImageView    itemIconView;
    private TextView     itemTextView;
    private Context      mContext;
    private MyFocusFrame menuFocusView;
    private int textSize = 0;
    private int unFocusIconID = -1;

    class C03471 implements Runnable {
        C03471() {
        }

        public void run() {
            int x = (SkySecondMenu.preViewWidth + ScreenParams.getInstence(SkyMenuItem_2.this.mContext).getResolutionValue(50)) + ScreenParams.getInstence(SkyMenuItem_2.this.mContext).getResolutionValue(30);
            int y = ScreenParams.getInstence(SkyMenuItem_2.this.mContext).getResolutionValue(516);
            int width = SkyMenuItem_2.this.getWidth() + (ScreenParams.getInstence(SkyMenuItem_2.this.mContext).getResolutionValue(47) * 2);
            int height = ScreenParams.getInstence(SkyMenuItem_2.this.mContext).getResolutionValue(170);
        }
    }

    public SkyMenuItem_2(Context context) {
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
        this.contentLayout.setPadding(ScreenParams.getInstence(this.mContext).getResolutionValue(52), 0, 0, 0);
        this.contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.contentLayout.setGravity(17);
        this.itemIconView = new ImageView(this.mContext);
        LayoutParams img_p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        img_p.rightMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(15);
        this.contentLayout.addView(this.itemIconView, img_p);
        this.itemTextView = new TextView(this.mContext);
        this.itemTextView.setSingleLine(true);
        this.itemTextView.getPaint().setAntiAlias(true);
        this.itemTextView.setSingleLine();
        this.itemTextView.setEllipsize(TruncateAt.END);
        this.itemTextView.setMarqueeRepeatLimit(-1);
        this.itemTextView.setGravity(16);
        LayoutParams title_p = new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(200), -1);
        title_p.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(4);
        this.contentLayout.addView(this.itemTextView, title_p);
        addView(this.contentLayout, new LayoutParams(-1, -1));
    }

    public void setFocusView(MyFocusFrame menuFocusView) {
        this.menuFocusView = menuFocusView;
    }

    public void setTextAttribute(int size, int color) {
        if (this.itemTextView != null) {
            this.textSize = size;
            this.itemTextView.setTextSize((float) size);
            this.itemTextView.setTextColor(color);
        }
    }

    public void setSelectState(boolean select) {
        this.isSelect = select;
        if (this.isSelect) {
            this.itemIconView.setVisibility(View.VISIBLE);
        } else if (!this.isSelect) {
            this.itemIconView.setVisibility(View.INVISIBLE);
        }
    }

    public void refreshItemValue(SkyMenuData data) {
        if (data != null) {
            this.currentData = data;
            if (data.getItemTitle() != null) {
                this.itemTextView.setText(data.getItemTitle() + "　");
            }
            post(new C03471());
        }
    }

    public void updataItemValue(SkyMenuData data) {
        if (data != null) {
            this.currentData = data;
            if (data.getItemTitle() != null) {
                this.itemTextView.setText(data.getItemTitle() + "　");
            }
            ScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, this.colorString);
            if (data.getItemState()) {
                this.itemIconView.setVisibility(View.INVISIBLE);
                this.itemTextView.setPadding(ScreenParams.getInstence(this.mContext).getResolutionValue(16), 0, 0, 0);
                return;
            }
            this.itemIconView.setBackgroundResource(this.unFocusIconID);
            this.itemTextView.setPadding(ScreenParams.getInstence(this.mContext).getResolutionValue(4), 0, 0, 0);
        }
    }

    public SkyMenuData getItemData() {
        return this.currentData;
    }

    public void reSetItemState() {
        if (this.unFocusIconID != -1 && !this.currentData.getItemState()) {
            this.itemIconView.setBackgroundResource(this.unFocusIconID);
        }
    }

    public void focus(View v) {
        if (!(this.focusIconID == -1 || this.currentData.getItemState())) {
            this.itemIconView.setBackgroundResource(this.focusIconID);
        }
        ScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, "000000");
        int[] location = new int[2];
        v.getLocationInWindow(location);
        int shaderSize = ScreenParams.getInstence(this.mContext).getResolutionValue(83);
        int x = location[0] - shaderSize;
        int y = location[1] - shaderSize;
        int width = ScreenParams.getInstence(this.mContext).getResolutionValue(324) + (shaderSize * 2);
        int height = ScreenParams.getInstence(this.mContext).getResolutionValue(90) + (shaderSize * 2);
    }

    public void unfocus() {
        if (!(!this.isSelect || this.unFocusIconID == -1 || this.currentData.getItemState())) {
            this.itemIconView.setBackgroundResource(this.unFocusIconID);
        }
        ScreenParams.getInstence(this.mContext).settextAlpha(this.itemTextView, 255, this.colorString);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            LogUtils.v("dzp", " itemTextView  " + this.itemTextView.getText().toString());
            this.itemTextView.setEllipsize(TruncateAt.MARQUEE);
            this.itemTextView.setSelected(true);
            focus(v);
            return;
        }
        unfocus();
        this.itemTextView.setSelected(false);
        this.itemTextView.setEllipsize(TruncateAt.END);
    }
}
