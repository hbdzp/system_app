package com.horion.tv.ui.leftepg;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.util.KtcScreenParams;
import com.horion.tv.R;
import com.horion.tv.ui.util.UIConstant;

import java.io.File;

public class SkyEPGItem_2 extends SkyEPGItem implements OnFocusChangeListener {
    private ScaleAnimation acquireFocusAnim;
    private TextView channelTextView;
    private LinearLayout contentLayout;
    private SkyEPGData currentData;
    private boolean isSelect = false;
    private TextView itemTextView;
    private ImageView lineView;
    private Context mContext;
    private ScaleAnimation releaseFocusAnim;
    private TextView secondTitelView;
    private boolean sortState = false;

    public SkyEPGItem_2(Context context) {
        super(context);
        this.mContext = context;
        this.sortState = false;
        initView();
        setFocusable(true);
        setOnFocusChangeListener(this);
    }

    private void changeFonts(TextView textView) {
        File file = new File("/system/fonts/AvantGarde-Book.ttf");
        if (file.exists()) {
            textView.setTypeface(Typeface.createFromFile(file));
        }
    }

    private void initAnim() {
        this.acquireFocusAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, 1, 0.0f, 1, 0.5f);
        this.acquireFocusAnim.setFillAfter(true);
        this.acquireFocusAnim.setDuration(80);
        this.releaseFocusAnim = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, 1, 0.0f, 1, 0.5f);
        this.releaseFocusAnim.setFillAfter(true);
        this.releaseFocusAnim.setDuration(110);
    }

    private void initView() {
        this.contentLayout = new LinearLayout(this.mContext);
        this.contentLayout.setFocusable(false);
        this.contentLayout.setPadding(0, 0, 0, KtcScreenParams.getInstence(this.mContext).getResolutionValue(0));
        this.contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.contentLayout.setGravity(17);
        this.itemTextView = new TextView(this.mContext);
        this.itemTextView.setFocusable(false);
        this.itemTextView.setGravity(48);
        this.itemTextView.setSingleLine(true);
        this.itemTextView.setTextColor(-1);
        this.itemTextView.setEllipsize(TruncateAt.MARQUEE);
        this.itemTextView.setMarqueeRepeatLimit(-1);
        this.itemTextView.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(20), 0, KtcScreenParams.getInstence(this.mContext).getResolutionValue(40), 0);
        this.itemTextView.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(35));
        this.secondTitelView = new TextView(this.mContext);
        this.secondTitelView.setEllipsize(TruncateAt.MARQUEE);
        this.secondTitelView.setMarqueeRepeatLimit(-1);
        this.secondTitelView.setSingleLine();
        this.secondTitelView.getPaint().setAntiAlias(true);
        this.secondTitelView.setFocusable(false);
        this.secondTitelView.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(23));
        this.secondTitelView.setGravity(48);
        this.secondTitelView.setSingleLine(true);
        this.secondTitelView.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(20), 0, KtcScreenParams.getInstence(this.mContext).getResolutionValue(40), 0);
        KtcScreenParams.getInstence(this.mContext).settextAlpha(this.secondTitelView, 255, "505050");
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        linearLayout.setFocusable(false);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(this.itemTextView, new FrameLayout.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(330), -2));
        linearLayout.addView(this.secondTitelView, new FrameLayout.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(350), -2));
        this.channelTextView = new TextView(this.mContext);
        this.channelTextView.setFocusable(false);
        this.channelTextView.setTextColor(-1);
        this.channelTextView.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(60));
        this.channelTextView.setGravity(16);
        this.channelTextView.setSingleLine(true);
        this.channelTextView.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(28), 0, KtcScreenParams.getInstence(this.mContext).getResolutionValue(20), 0);
        changeFonts(this.channelTextView);
        this.contentLayout.addView(this.channelTextView);
        this.lineView = new ImageView(this.mContext);
        this.lineView.setBackgroundResource(R.drawable.epg_second_line_unfocus55);
        this.contentLayout.addView(this.lineView);
        this.contentLayout.addView(linearLayout, new FrameLayout.LayoutParams(-2, -2));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.bottomMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(30);
        addView(this.contentLayout, layoutParams);
        initAnim();
    }

    public int channelTextViewNumberDec() {
        try {
            int intValue = Integer.valueOf(this.channelTextView.getText().toString()).intValue();
            this.channelTextView.setText(String.valueOf(intValue - 1));
            return intValue - 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int channelTextViewNumberInc() {
        try {
            int intValue = Integer.valueOf(this.channelTextView.getText().toString()).intValue();
            this.channelTextView.setText(String.valueOf(intValue + 1));
            return intValue + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void channelTextViewNumberSet(int i) {
        try {
            this.channelTextView.setText(String.valueOf(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void focus() {
        ///TVUIDebug.debug("focus  == " + this.itemTextView.getText().toString());
        Log.d("Maxs110","--->focus:epgMode = " + SkyCommenEPG.epgMode + " /isSelect = " + isSelect);
        if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_DIS) {
            if (this.isSelect) {
                this.itemTextView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
                this.channelTextView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
                this.secondTitelView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
                this.lineView.setBackgroundResource(R.drawable.epg_second_line_selected55);
            } else {
                this.itemTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.channelTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.secondTitelView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.lineView.setBackgroundResource(R.drawable.epg_second_line_focus55);
            }
        } else if ((SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT && !this.sortState) || SkyCommenEPG.epgMode == EPGMODE.EPGMODE_DELETED) {
            this.itemTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.channelTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.secondTitelView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.lineView.setBackgroundResource(R.drawable.epg_second_line_focus55);
        }
        this.itemTextView.setSelected(true);
        this.secondTitelView.setSelected(true);
        focusViewChangePos();
        if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_DELETED) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) SkyCommenEPG.recoveryHint.getLayoutParams();
            layoutParams.leftMargin = (((FrameLayout) getParent()).getLeft() + getWidth()) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(30);
            SkyCommenEPG.recoveryHint.setLayoutParams(layoutParams);
        }
        if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT) {
            FrameLayout.LayoutParams  layoutParams = (FrameLayout.LayoutParams) SkyCommenEPG.sortButton.getLayoutParams();
            ///layoutParams.leftMargin = (((FrameLayout) getParent()).getLeft() + getWidth()) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(30);
            layoutParams.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(519) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(30);
            Log.d("Maxs112","11getWidth() = " + (((FrameLayout) getParent()).getLeft() + getWidth()));
            SkyCommenEPG.sortButton.setLayoutParams(layoutParams);
        }
    }

    public void focusViewChangePos() {
        ///int resolutionValue = (KtcScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2) + getWidth();
        int resolutionValue = 0;
        Log.d("Maxs112","22getWidth() = " + getWidth() + " /SkyCommenEPG.epgMode = " + SkyCommenEPG.epgMode);
        if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_DELETED) {
            resolutionValue = (KtcScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(333);
        }else{
            resolutionValue = (KtcScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(424);
        }
        ///TVUIDebug.debug("focusViewChangePos width:" + resolutionValue);
        int resolutionValue2 = (KtcScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(104);
        int left = ((FrameLayout) getParent()).getLeft() - KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
        int resolutionValue3 = (((KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080) / 2) - (KtcScreenParams.getInstence(this.mContext).getResolutionValue(104) / 2)) - KtcScreenParams.getInstence(this.mContext).getResolutionValue(83)) - KtcScreenParams.getInstence(this.mContext).getResolutionValue(2);
        if (SkyCommenEPG.focusView.getVisibility() != View.VISIBLE) {
            ///TVUIDebug.debug("SkyEPGItem_2 focusView.getVisibility() != View.VISIBLE");
            SkyCommenEPG.focusView.initStarPosition(left, resolutionValue3, resolutionValue, resolutionValue2);
            SkyCommenEPG.focusView.setVisibility(View.VISIBLE);
            return;
        }
        ///TVUIDebug.debug("SkyEPGItem_2 focusView.getVisibility() == View.VISIBLE");
        SkyCommenEPG.focusView.changeFocusPos(left, resolutionValue3, resolutionValue, resolutionValue2);
    }

    public void focusViewSetPos() {
        int resolutionValue = (KtcScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2) + getWidth();
        ///TVUIDebug.debug("focusViewChangePos width:" + resolutionValue);
        int resolutionValue2 = KtcScreenParams.getInstence(this.mContext).getResolutionValue(104);
        int resolutionValue3 = KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
        int left = ((FrameLayout) getParent()).getLeft();
        int resolutionValue4 = KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
        int resolutionValue5 = KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080) / 2;
        int resolutionValue6 = KtcScreenParams.getInstence(this.mContext).getResolutionValue(104) / 2;
        int resolutionValue7 = KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
        int resolutionValue8 = KtcScreenParams.getInstence(this.mContext).getResolutionValue(2);
        if (SkyCommenEPG.focusView.getVisibility() != View.VISIBLE) {
            SkyCommenEPG.focusView.setVisibility(View.VISIBLE);
        }
        SkyCommenEPG.focusView.initStarPosition(left - resolutionValue4, ((resolutionValue5 - resolutionValue6) - resolutionValue7) - resolutionValue8, resolutionValue, resolutionValue2 + (resolutionValue3 * 2));
    }

    public int getChannelTextViewNumber() {
        try {
            return Integer.valueOf(this.channelTextView.getText().toString()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public SkyEPGData getItemData() {
        return this.currentData;
    }

    public void onFocusChange(View view, boolean z) {
        Log.d("Maxs25","SkyEPGItem_2 onFocusChange v:" + view + "  hasFocus:" + z + "   isInSortModeing:" + SkySecondEPG.isInSortModeing);
        ///TVUIDebug.debug("SkyEPGItem_2 onFocusChange v:" + view + "  hasFocus:" + z + "   isInSortModeing:" + SkySecondEPG.isInSortModeing);
        if (!SkySecondEPG.isInSortModeing) {
            if (z) {
                focus();
            } else {
                unfocus();
            }
        }
    }

    public void reSetItemState() {
        this.itemTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
    }

    public void setItemSortState(boolean z) {
        ///TVUIDebug.debug("setItemState sortState:" + z);
        if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT) {
            this.sortState = z;
            if (z) {
                this.channelTextView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
                this.itemTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
                this.lineView.setBackgroundResource(R.drawable.epg_second_line_unfocus55);
                if (this.secondTitelView.getVisibility() == View.VISIBLE) {
                    this.secondTitelView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
                    return;
                }
                return;
            }
            this.channelTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.itemTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.lineView.setBackgroundResource(R.drawable.epg_second_line_focus55);
            if (this.secondTitelView.getVisibility() == View.VISIBLE) {
                this.secondTitelView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            }
        }
    }

    public void setItemStateForSecond(boolean z) {
        if (SkyCommenEPG.epgMode != EPGMODE.EPGMODE_SORT) {
            return;
        }
        if (z) {
            Animation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
            alphaAnimation.setDuration(200);
            alphaAnimation.setFillAfter(true);
            this.contentLayout.startAnimation(alphaAnimation);
            return;
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setFillAfter(true);
        this.contentLayout.startAnimation(alphaAnimation);
    }

    public void setSelectState(boolean z) {
        this.isSelect = z;
        if (!this.isSelect || SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT) {
            this.itemTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
            this.channelTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
            this.secondTitelView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
            this.lineView.setBackgroundResource(R.drawable.epg_second_line_unfocus55);
            return;
        }
        ///TVUIDebug.debug("setSelectState isCurrent:" + this.isSelect);
        this.itemTextView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
        this.channelTextView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
        this.secondTitelView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
        this.lineView.setBackgroundResource(R.drawable.epg_second_line_selected55);
    }

    public void unfocus() {
        Log.d("Maxs110","unfocus  == " + this.itemTextView.getText().toString() + " /isSelect = " + this.isSelect);
        ///TVUIDebug.debug("unfocus  == " + this.itemTextView.getText().toString());
        if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_DIS) {
            if (this.isSelect) {
                this.itemTextView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
                this.channelTextView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
                this.secondTitelView.setTextColor(UIConstant.TEXTCOLOR_EPG_NOTIMPORTANCE);
                this.lineView.setBackgroundResource(R.drawable.epg_second_line_selected55);
            } else {
                this.itemTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
                this.channelTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
                this.secondTitelView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
                this.lineView.setBackgroundResource(R.drawable.epg_second_line_unfocus55);
            }
        } else if ((SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT && !this.sortState) || SkyCommenEPG.epgMode == EPGMODE.EPGMODE_DELETED) {
            this.itemTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
            this.channelTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
            this.secondTitelView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
            this.lineView.setBackgroundResource(R.drawable.epg_second_line_unfocus55);
        }
        this.itemTextView.setSelected(false);
        this.secondTitelView.setSelected(false);
    }

    public void updataItemValue(SkyEPGData skyEPGData) {
        if (skyEPGData != null) {
            this.currentData = skyEPGData;
            if (skyEPGData.getItemTitle() != null) {
                this.itemTextView.setText(skyEPGData.getItemTitle());
            }
            if (skyEPGData.getItemSecondTitle() != null) {
                if (this.secondTitelView.getVisibility() != View.VISIBLE) {
                    this.secondTitelView.setVisibility(View.VISIBLE);
                }
                this.secondTitelView.setText(skyEPGData.getItemSecondTitle());
                FrameLayout.LayoutParams layoutParams;
                if (skyEPGData.getItemSecondTitle().length() < 17) {
                    layoutParams = (FrameLayout.LayoutParams) this.secondTitelView.getLayoutParams();
                    layoutParams.width = -2;
                    this.secondTitelView.setLayoutParams(layoutParams);
                } else {
                    layoutParams = (FrameLayout.LayoutParams) this.secondTitelView.getLayoutParams();
                    layoutParams.width = KtcScreenParams.getInstence(this.mContext).getResolutionValue(350);
                    this.secondTitelView.setLayoutParams(layoutParams);
                    this.itemTextView.setLayoutParams((FrameLayout.LayoutParams) this.itemTextView.getLayoutParams());
                }
                postInvalidate();
            } else {
                this.secondTitelView.setVisibility(View.GONE);
            }
            if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_DELETED) {
                this.channelTextView.setVisibility(View.GONE);
                this.lineView.setVisibility(View.GONE);
            } else if (skyEPGData.getChannelNumber() != null) {
                this.channelTextView.setText(skyEPGData.getChannelNumber());
            }
            this.channelTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
            this.itemTextView.setTextColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
        }
    }
}
