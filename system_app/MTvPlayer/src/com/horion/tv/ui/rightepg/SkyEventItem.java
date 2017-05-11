package com.horion.tv.ui.rightepg;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.horion.tv.R;
///import com.horion.tv.ui.util.TVUIDebug;
import com.ktc.util.KtcScreenParams;
///import org.apache.http4.HttpStatus;

public class SkyEventItem extends LinearLayout {
    private SkyEventData currentData;
    private TextView fistItemTextView;
    private LinearLayout imgContent;
    private boolean isSelect = false;
    private ImageView itemIconView;
    private Context mContext;
    private TextView secondItemTextView;

    public SkyEventItem(Context context) {
        super(context);
        ///TVUIDebug.debug("SkyEventItem");
        this.mContext = context;
        setFocusable(true);
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(5);
        this.fistItemTextView = new TextView(this.mContext);
        this.fistItemTextView.setGravity(21);
        this.fistItemTextView.setSingleLine(true);
        this.fistItemTextView.setEllipsize(TruncateAt.MARQUEE);
        this.fistItemTextView.setMarqueeRepeatLimit(-1);
        this.fistItemTextView.setTextColor(-1);
        this.fistItemTextView.setSelected(true);
        this.fistItemTextView.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(2), KtcScreenParams.getInstence(this.mContext).getResolutionValue(0), KtcScreenParams.getInstence(this.mContext).getResolutionValue(20), 0);
        addView(this.fistItemTextView, new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(390), KtcScreenParams.getInstence(this.mContext).getResolutionValue(50)));
        this.itemIconView = new ImageView(this.mContext);
        this.imgContent = new LinearLayout(this.mContext);
        this.imgContent.setOrientation(LinearLayout.VERTICAL);
        this.imgContent.setGravity(Gravity.CENTER);
        LayoutParams layoutParams = new LayoutParams(-2, KtcScreenParams.getInstence(this.mContext).getResolutionValue(110));
        layoutParams.gravity = Gravity.CENTER;
        this.imgContent.addView(this.itemIconView, layoutParams);
        addView(this.imgContent, new LayoutParams(-2, KtcScreenParams.getInstence(this.mContext).getResolutionValue(110)));
        this.secondItemTextView = new TextView(this.mContext);
        this.secondItemTextView.setTextColor(-1);
        this.secondItemTextView.setGravity(19);
        this.secondItemTextView.setSingleLine(true);
        this.secondItemTextView.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(20), KtcScreenParams.getInstence(this.mContext).getResolutionValue(0), KtcScreenParams.getInstence(this.mContext).getResolutionValue(2), 0);
        addView(this.secondItemTextView, new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(200), KtcScreenParams.getInstence(this.mContext).getResolutionValue(50)));
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
        if (this.fistItemTextView != null) {
            this.fistItemTextView.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(i));
            this.fistItemTextView.setTextColor(i2);
        }
        if (this.secondItemTextView != null) {
            this.secondItemTextView.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(i));
            this.secondItemTextView.setTextColor(i2);
        }
    }

    public void updataItemImage(int i) {
        switch (i) {
            case 17:
                this.imgContent.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(5), KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(0));
                this.itemIconView.setBackgroundResource(R.drawable.rightepg_curindexbg);
                return;
            case 18:
                this.imgContent.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(5), KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(0));
                this.itemIconView.setBackgroundResource(R.drawable.rightepg_1bg);
                return;
            case 19:
                this.imgContent.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(5), KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(0));
                this.itemIconView.setBackgroundResource(R.drawable.rightepg_2bg);
                return;
            case 20:
                this.imgContent.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(5), KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(0));
                this.itemIconView.setBackgroundResource(R.drawable.rightepg_3bg);
                return;
            case 21:
                this.imgContent.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(5), KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(0));
                this.itemIconView.setBackgroundResource(R.drawable.rightepg_4bg);
                return;
            case 22:
                this.imgContent.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(5), KtcScreenParams.getInstence(this.mContext).getResolutionValue(15), KtcScreenParams.getInstence(this.mContext).getResolutionValue(0));
                this.itemIconView.setBackgroundResource(R.drawable.rightepg_5bg);
                return;
            default:
                return;
        }
    }

    public void updataItemValue(SkyEventData skyEventData) {
        if (skyEventData != null) {
            this.currentData = skyEventData;
            try {
                if (skyEventData.getItemTitle() != null) {
                    this.fistItemTextView.setText(skyEventData.getItemTitle());
                }
                if (skyEventData.getItemSecondTitle() != null) {
                    this.secondItemTextView.setText(skyEventData.getItemSecondTitle());
                }
            } catch (Exception e) {
                ///TVUIDebug.debug("updataItemValue Exception ");
                e.printStackTrace();
            }
        }
    }
}
