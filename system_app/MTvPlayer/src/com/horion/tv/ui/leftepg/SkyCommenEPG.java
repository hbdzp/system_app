package com.horion.tv.ui.leftepg;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.horion.tv.define.object.Channel;
import com.ktc.framework.skycommondefine.SkyworthBroadcastKey;
import com.ktc.ui.blurbg.BlurBgLayout;
import com.ktc.util.KtcScreenParams;
import com.ktc.util.MyFocusFrame;
///import com.ktc.framework.skycommondefine.SkyworthBroadcastKey;


import com.horion.tv.R;
import com.horion.tv.ui.leftepg.SkyFirstEPG.OnItemOnkeyListener;
import com.horion.tv.ui.leftepg.SkySecondEPG.OnSecondEPGItemOnkeyListener;
///import com.horion.tv.ui.util.TVUIDebug;
import com.horion.tv.ui.util.UIConstant;

import java.util.ArrayList;
import java.util.List;

public class SkyCommenEPG extends FrameLayout {
    private static  int[] EPGMODESwitchesValues = null;
    public static final int EPG1_LEFT_MARGIN = 49;
    public static final int EPG2_LEFT_MARGIN = 286;
    public static final int SHADER_SIZE = 83;
    public static final int SHADER_SIZE_FIRST = 83;
    public static ImageView arrowView;
    public static EPGMODE epgMode = EPGMODE.EPGMODE_DIS;
    public static MyFocusFrame focusView;
    public static boolean isRefreshSencond = false;
    public static TextView recoveryHint;
    public static Button sortButton;
    private int SORTBUTTON_HEIGHT;
    private int SORTBUTTON_WIDTH;
    private BlurBgLayout bgLayout;
    private SkyFirstEPG epg1;
    private SkySecondEPG epg2;
    OnItemOnkeyListener epgListener;
    private boolean isFristRefresh;
    public boolean isSkyFirstEPGShowed;
    private Context mContext;
    private OnEPGOnKeyEventListener mListener;
    private int menu_2_dataCount;
    private  boolean needExit_OnSecondEPGItemOnKeyBack;
    OnSecondEPGItemOnkeyListener secondEPGListener;

    public interface OnEPGOnKeyEventListener {
        void onFirstEPGItemFocusChangeListener(int i, View view, SkyEPGData skyEPGData, boolean z);

        boolean onFirstEPGItemOnClick(int i, View view, SkyEPGData skyEPGData);

        boolean onFirstEPGItemOnKeyBack(int i, View view);

        boolean onFirstEPGItemOnKeyDown(int i, View view);

        boolean onFirstEPGItemOnKeyLeft(int i, View view);

        boolean onFirstEPGItemOnKeyRight(int i, View view, SkyEPGData skyEPGData);

        boolean onFirstEPGItemOnKeyUp(int i, View view);

        boolean onSecondEPGItemOnClick(EPGMODE epgmode, int i, View view, SkyEPGData skyEPGData, int i2);

        boolean onSecondEPGItemOnKeyBack(int i, View view);

        boolean onSecondEPGItemOnKeyDown(EPGMODE epgmode, int i, View view);

        boolean onSecondEPGItemOnKeyLeft(int i, View view);

        boolean onSecondEPGItemOnKeyRight(int i, View view, SkyEPGData skyEPGData);

        boolean onSecondEPGItemOnKeyUp(EPGMODE epgmode, int i, View view);
    }

    private static int[] getEPGMODESwitchesValues() {
        if (EPGMODESwitchesValues != null) {
            return EPGMODESwitchesValues;
        }
        int[] iArr = new int[EPGMODE.values().length];
        try {
            iArr[EPGMODE.EPGMODE_DELETED.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[EPGMODE.EPGMODE_DIS.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[EPGMODE.EPGMODE_SORT.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        EPGMODESwitchesValues = iArr;
        return iArr;
    }

    private SkyCommenEPG(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isFristRefresh = false;
        this.needExit_OnSecondEPGItemOnKeyBack = true;
        this.isSkyFirstEPGShowed = false;
        this.SORTBUTTON_WIDTH = 140;
        this.SORTBUTTON_HEIGHT = 67;

        this.isSkyFirstEPGShowed = false;
        initView(context);
    }

    private SkyCommenEPG(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isFristRefresh = false;
        this.needExit_OnSecondEPGItemOnKeyBack = true;
        this.isSkyFirstEPGShowed = false;
        this.SORTBUTTON_WIDTH = 140;
        this.SORTBUTTON_HEIGHT = 67;
        ///this.epgListener = /* anonymous class already generated */;
        ///this.secondEPGListener = /* anonymous class already generated */;
        this.isSkyFirstEPGShowed = false;
        initView(context);
    }

    public SkyCommenEPG(Context context, EPGMODE epgmode) {
        super(context);
        this.isFristRefresh = false;
        this.needExit_OnSecondEPGItemOnKeyBack = true;
        this.isSkyFirstEPGShowed = false;
        this.SORTBUTTON_WIDTH = 140;
        this.SORTBUTTON_HEIGHT = 67;
        ///this.epgListener = /* anonymous class already generated */;
        ///this.secondEPGListener = /* anonymous class already generated */;
        epgMode = epgmode;
        this.isSkyFirstEPGShowed = false;
        initView(context);
    }

    private void initView(Context context) {
        this.epgListener = new OnItemOnkeyListener() {
            public void onItemFocusChangeListener(int i, View view, SkyEPGData skyEPGData, boolean z) {
                if (SkyCommenEPG.this.mListener != null) {
                    SkyCommenEPG.this.mListener.onFirstEPGItemFocusChangeListener(i, view, skyEPGData, z);
                }
            }

            public boolean onItemOnClick(int i, View view, SkyEPGData skyEPGData) {
                if (!(SkyCommenEPG.this.epg2.isDismisAnimStart || SkyCommenEPG.isRefreshSencond)) {
                    SkyCommenEPG.this.epg1.setLastFocusItemID(i);
                    ((SkyEPGItem) view).setSelectState(true);
                    SkyCommenEPG.this.epg1.setItemStateShowSecond(true);
                    SkyCommenEPG.this.setSecondEPGfocus();
                    if (SkyCommenEPG.this.mListener != null) {
                        SkyCommenEPG.this.mListener.onFirstEPGItemOnClick(i, view, skyEPGData);
                    }
                }
                return true;
            }

            public boolean onItemOnKeyBack(int i, View view) {
                return SkyCommenEPG.this.epg2.isDismisAnimStart ? true : SkyCommenEPG.this.mListener != null ? SkyCommenEPG.this.mListener.onFirstEPGItemOnKeyBack(i, view) : false;
            }

            public boolean onItemOnKeyDown(int i, View view) {
                if (!SkyCommenEPG.this.epg2.isDismisAnimStart) {
                    SkyCommenEPG.this.isFristRefresh = false;
                    if (SkyCommenEPG.this.mListener != null) {
                        SkyCommenEPG.this.mListener.onFirstEPGItemOnKeyDown(i, view);
                    }
                }
                return true;
            }

            public boolean onItemOnKeyLeft(int i, View view) {
                if (!(SkyCommenEPG.this.epg2.isDismisAnimStart || SkyCommenEPG.this.mListener == null)) {
                    SkyCommenEPG.this.mListener.onFirstEPGItemOnKeyLeft(i, view);
                }
                return true;
            }

            public boolean onItemOnKeyRight(int i, View view, SkyEPGData skyEPGData) {
                ///TVUIDebug.debug("onKey  epg2.isDismisAnimStart:" + SkyCommenEPG.this.epg2.isDismisAnimStart);
                if (!SkyCommenEPG.this.epg2.isDismisAnimStart) {
                    ///TVUIDebug.debug("onKey isRefreshSencond:" + SkyCommenEPG.isRefreshSencond);
                    if (!SkyCommenEPG.isRefreshSencond) {
                        SkyCommenEPG.this.epg1.setLastFocusItemID(i);
                        ((SkyEPGItem) view).setSelectState(true);
                        SkyCommenEPG.this.epg1.setItemStateShowSecond(true);
                        SkyCommenEPG.this.setSecondEPGfocus();
                        if (SkyCommenEPG.this.mListener != null) {
                            SkyCommenEPG.this.mListener.onFirstEPGItemOnKeyRight(i, view, skyEPGData);
                        }
                    }
                }
                return true;
            }

            public boolean onItemOnKeyUp(int i, View view) {
                if (!SkyCommenEPG.this.epg2.isDismisAnimStart) {
                    SkyCommenEPG.this.isFristRefresh = false;
                    if (SkyCommenEPG.this.mListener != null) {
                        SkyCommenEPG.this.mListener.onFirstEPGItemOnKeyUp(i, view);
                    }
                }
                return true;
            }
        };
        this.secondEPGListener = new OnSecondEPGItemOnkeyListener() {
            private  int[] EPGMODESwitchesValues;
            //final  int[] $SWITCH_TABLE$com$horion$tv$ui$leftepg$EPGMODE;

            private   int[] getEPGMODESwitchesValues() {
                if (EPGMODESwitchesValues != null) {
                    return EPGMODESwitchesValues;
                }
                int[] iArr = new int[EPGMODE.values().length];
                try {
                    iArr[EPGMODE.EPGMODE_DELETED.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[EPGMODE.EPGMODE_DIS.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[EPGMODE.EPGMODE_SORT.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                EPGMODESwitchesValues = iArr;
                return iArr;
            }

            public boolean onSecondEPGItemOnClick(EPGMODE epgmode, int i, View view, SkyEPGData skyEPGData, List<SkyEPGItem_2> list, int i2) {
                switch (getEPGMODESwitchesValues()[epgmode.ordinal()]) {
                    case 1:
                        if (SkyCommenEPG.this.mListener != null) {
                            SkyCommenEPG.this.mListener.onSecondEPGItemOnClick(epgmode, i, view, skyEPGData, i2);
                            break;
                        }
                        break;
                    case 2:
                        if (list != null && list.size() > 0) {
                            int size = list.size();
                            for (int i3 = 0; i3 < size; i3++) {
                                if (view.getId() != i3) {
                                    ((SkyEPGItem_2) list.get(i3)).setSelectState(false);
                                    ((SkyEPGItem_2) list.get(i3)).reSetItemState();
                                }
                            }
                            ((SkyEPGItem_2) view).setSelectState(true);
                        }
                        if (SkyCommenEPG.this.mListener != null) {
                            SkyCommenEPG.this.mListener.onSecondEPGItemOnClick(epgmode, i, view, skyEPGData, i2);
                            break;
                        }
                        break;
                    case 3:
                        if (SkyCommenEPG.this.mListener != null) {
                            SkyCommenEPG.this.mListener.onSecondEPGItemOnClick(epgmode, i, view, skyEPGData, i2);
                            break;
                        }
                        break;
                }
                return true;
            }

            public boolean onSecondEPGItemOnKeyBack(int i, View view) {
                if (SkyCommenEPG.this.mListener != null) {
                    SkyCommenEPG.this.mListener.onSecondEPGItemOnKeyBack(i, view);
                }
                SkyCommenEPG.this.dismissMenuBySecond(i);
                return false;
            }

            public boolean onSecondEPGItemOnKeyDown(EPGMODE epgmode, int i, View view) {
                if (SkyCommenEPG.this.mListener != null) {
                    SkyCommenEPG.this.mListener.onSecondEPGItemOnKeyDown(epgmode, i, view);
                }
                return false;
            }

            public boolean onSecondEPGItemOnKeyLeft(int i, View view) {
                SkyCommenEPG.this.isFristRefresh = false;
                if (SkyCommenEPG.this.mListener != null) {
                    SkyCommenEPG.this.mListener.onSecondEPGItemOnKeyLeft(i, view);
                }
                SkyCommenEPG.this.epg1.setEPGItemFocus(SkyCommenEPG.this.epg1.getLastFocusID());
                SkyCommenEPG.this.epg1.setItemStateShowSecond(false);
                return true;
            }

            public boolean onSecondEPGItemOnKeyRight(int i, View view, SkyEPGData skyEPGData) {
                return SkyCommenEPG.this.mListener != null ? SkyCommenEPG.this.mListener.onSecondEPGItemOnKeyRight(i, view, skyEPGData) : false;
            }

            public boolean onSecondEPGItemOnKeyUp(EPGMODE epgmode, int i, View view) {
                if (SkyCommenEPG.this.mListener != null) {
                    SkyCommenEPG.this.mListener.onSecondEPGItemOnKeyUp(epgmode, i, view);
                }
                return false;
            }
        };


        setFocusable(false);
        this.mContext = context;
        this.SORTBUTTON_WIDTH = KtcScreenParams.getInstence(this.mContext).getResolutionValue(140);
        this.SORTBUTTON_HEIGHT = KtcScreenParams.getInstence(this.mContext).getResolutionValue(67);
        setLayoutParams(new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(1109), -1));
        this.bgLayout = new BlurBgLayout(this.mContext);
        this.bgLayout.setPageType(BlurBgLayout.PAGETYPE.OTHER_PAGE);
        addView(this.bgLayout, new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(830), KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080)));
        focusView = new MyFocusFrame(this.mContext, 0);
        focusView.setFocusable(false);
        focusView.setImgResourse(R.drawable.ui_sdk_btn_focus_shadow_bg);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(49);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
        int resolutionValue = KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080) / 2;
        resolutionValue = KtcScreenParams.getInstence(this.mContext).getResolutionValue(SkyworthBroadcastKey.SKY_BROADCAST_YELLOW_KEY_MESSAGEE) / 2;
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(100);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(85);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(83);
        addView(focusView);
        focusView.setVisibility(View.INVISIBLE);
        this.epg1 = new SkyFirstEPG(this.mContext);
        this.epg1.setFocusable(false);
        this.epg1.setTag("epg1");
        this.epg1.setOnItemOnkeyListener(this.epgListener);
        this.epg2 = new SkySecondEPG(this.mContext);
        this.epg2.setFocusable(false);
        this.epg2.setTag("epg2");
        this.epg2.setSecondEPGOnItemOnkeyListener(this.secondEPGListener);
        LayoutParams layoutParams = (LayoutParams) this.epg1.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(49);
        addView(this.epg1, layoutParams);
        arrowView = new ImageView(this.mContext);
        arrowView.setVisibility(View.INVISIBLE);
        arrowView.setImageResource(R.drawable.epg_left_arrow);
        LayoutParams layoutParams2 = new LayoutParams(-2, -2);
        layoutParams2.gravity = Gravity.CENTER_VERTICAL;
        layoutParams2.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(238);
        arrowView.setLayoutParams(layoutParams2);
        arrowView.setFocusable(false);
        addView(arrowView);
        layoutParams = (LayoutParams) this.epg2.getLayoutParams();
        switch (getEPGMODESwitchesValues()[epgMode.ordinal()]) {
            case 2:
                layoutParams.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(EPG2_LEFT_MARGIN);
                break;
            default:
                layoutParams.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(95);
                break;
        }
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        addView(this.epg2);
        this.epg2.setVisibility(View.INVISIBLE);
        recoveryHint = new TextView(this.mContext);
        recoveryHint.setText(getResources().getString(R.string.pressOkRestore));
        recoveryHint.setTextColor(getResources().getColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE));
        recoveryHint.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(32));
        LayoutParams layoutParams3 = new LayoutParams(-2, -2);
        layoutParams3.gravity = Gravity.CENTER_VERTICAL;
        layoutParams3.leftMargin = (((LayoutParams) this.epg2.getLayoutParams()).leftMargin + this.epg2.getWidth()) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(30);
        addView(recoveryHint, layoutParams3);
        recoveryHint.setVisibility(View.INVISIBLE);
        sortButton = new Button(this.mContext);
        sortButton.setBackgroundColor(UIConstant.BUTTONCOLOR_UNFOCUS);
        sortButton.setFocusable(true);
        sortButton.setFocusableInTouchMode(true);
        sortButton.setSingleLine(true);
        sortButton.setText(getResources().getString(R.string.placetop));
        sortButton.setTextColor(getResources().getColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE));
        sortButton.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(32));
        LayoutParams layoutParams4 = new LayoutParams(this.SORTBUTTON_WIDTH, this.SORTBUTTON_HEIGHT);
        layoutParams4.gravity = Gravity.CENTER_VERTICAL;
        layoutParams4.leftMargin = (((LayoutParams) this.epg2.getLayoutParams()).leftMargin + this.epg2.getWidth()) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(30);
        addView(sortButton, layoutParams4);
        sortButton.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    SkyCommenEPG.sortButton.setBackground(null);
                    int width = SkyCommenEPG.sortButton.getWidth();
                    int resolutionValue = KtcScreenParams.getInstence(SkyCommenEPG.this.mContext).getResolutionValue(83);
                    int resolutionValue2 = KtcScreenParams.getInstence(SkyCommenEPG.this.mContext).getResolutionValue(SkyworthBroadcastKey.SKY_BROADCAST_KEY_MUTE);
                    int left = SkyCommenEPG.sortButton.getLeft();
                    int resolutionValue3 = KtcScreenParams.getInstence(SkyCommenEPG.this.mContext).getResolutionValue(83);
                    int resolutionValue4 = KtcScreenParams.getInstence(SkyCommenEPG.this.mContext).getResolutionValue(1080) / 2;
                    int resolutionValue5 = KtcScreenParams.getInstence(SkyCommenEPG.this.mContext).getResolutionValue(SkyworthBroadcastKey.SKY_BROADCAST_KEY_MUTE) / 2;
                    if (SkyCommenEPG.focusView.getVisibility() != View.VISIBLE) {
                        SkyCommenEPG.focusView.setVisibility(View.VISIBLE);
                    }
                    SkyCommenEPG.focusView.changeFocusPos(left - resolutionValue3, resolutionValue4 - resolutionValue5, width + (resolutionValue * 2), resolutionValue2);
                    SkyCommenEPG.sortButton.setBackgroundColor(-1);
                    SkyCommenEPG.sortButton.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                } else if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT) {
                    SkyCommenEPG.sortButton.postDelayed(new Runnable() {
                        public void run() {
                            SkyCommenEPG.sortButton.setBackgroundColor(UIConstant.BUTTONCOLOR_UNFOCUS);
                            SkyCommenEPG.sortButton.setTextColor(getResources().getColor(UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE));
                        }
                    }, 100);
                }
            }
        });
        sortButton.setOnClickListener(this.epg2.getSortButtonOnClickListener());
        sortButton.setOnKeyListener(this.epg2.getSortButtonOnKeyListener());
        sortButton.setVisibility(View.INVISIBLE);
    }

    public void dismissEPG(int i) {
        this.epg1.dismissAnimtion(this, i);
    }

    public void dismissMenuBySecond(int i) {
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setFillAfter(true);
        if (epgMode == EPGMODE.EPGMODE_DIS) {
            arrowView.startAnimation(alphaAnimation);
        }
        this.epg2.show_dismissAnimation(this.epg1, false, i);
        if (epgMode == EPGMODE.EPGMODE_DIS) {
            this.epg1.dismissAnimtion(this, this.epg1.getLastFocusID());
        }
        clearAnimation();
        alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        startAnimation(alphaAnimation);
    }

    public void setOnEPGOnKeyEventListener(OnEPGOnKeyEventListener onEPGOnKeyEventListener) {
        this.mListener = onEPGOnKeyEventListener;
    }

    public void setSecondEPGfocus() {
        this.epg2.setSecondEPGfocus();
    }

    public void setSecondTipsViewTips(String str) {
        this.epg2.setTipsViewTips(str);
    }

    public void showEPG(List<SkyEPGData> list) {
        if (list != null && list.size() > 0) {
            this.isSkyFirstEPGShowed = true;
            this.isFristRefresh = true;
            if (getVisibility() != View.VISIBLE) {
                setVisibility(View.VISIBLE);
            }
            clearAnimation();
            Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(200);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setInterpolator(new AccelerateInterpolator());
            startAnimation(alphaAnimation);
            this.epg1.showFitstEPG(new SkyEPGAdapter(list));
        }
    }

    public void showSecondEPG(EPGMODE epgmode, List<SkyEPGData> list, int foucusIndex, boolean isCurrent) {
/*        List<SkyEPGData> list = new ArrayList<SkyEPGData>();
        SkyEPGData skyEPGData  = new SkyEPGData();
        skyEPGData.setChannelNumber("11");
        skyEPGData.setChannelType(Channel.CHANNEL_TYPE.TV);
        skyEPGData.setItemTitle("CCTV1");
        skyEPGData.setItemIndexID(0);

        SkyEPGData skyEPGData2  = new SkyEPGData();
        skyEPGData2.setChannelNumber("12");
        skyEPGData2.setChannelType(Channel.CHANNEL_TYPE.TV);
        skyEPGData2.setItemTitle("CCTV2");
        skyEPGData2.setItemIndexID(1);
        SkyEPGData skyEPGData3  = new SkyEPGData();
        skyEPGData2.setChannelNumber("12");
        skyEPGData2.setChannelType(Channel.CHANNEL_TYPE.TV);
        skyEPGData2.setItemTitle("CCTV2");
        skyEPGData2.setItemIndexID(1);
        SkyEPGData skyEPGData4  = new SkyEPGData();
        skyEPGData2.setChannelNumber("12");
        skyEPGData2.setChannelType(Channel.CHANNEL_TYPE.TV);
        skyEPGData2.setItemTitle("CCTV2");
        skyEPGData2.setItemIndexID(1);
        list.add(skyEPGData);
        list.add(skyEPGData2);
        list.add(skyEPGData3);
        list.add(skyEPGData4);*/

        Log.d("Maxs32","SkyCommenEPG:showSecondEPG = " + getEPGMODESwitchesValues()[epgmode.ordinal()]);
        Log.d("Maxs32","list == null = " + (list == null));

        if (list != null && list.size() > 0) {
            epgMode = epgmode;
            isRefreshSencond = true;
            this.menu_2_dataCount = list.size();
            ///TVUIDebug.debug("showSecondEPG isSkyFirstEPGShowed:" + this.isSkyFirstEPGShowed);
            switch (getEPGMODESwitchesValues()[epgmode.ordinal()]) {
                case 1:
                    if (getVisibility() != View.VISIBLE) {
                        setVisibility(View.VISIBLE);
                    }
                    clearAnimation();
                    this.epg1.setVisibility(View.INVISIBLE);
                    arrowView.clearAnimation();
                    arrowView.setVisibility(View.INVISIBLE);
                    this.epg2.setPreViewWidth(this.epg1.getWidth());
                    this.epg2.setEPGAdapter(new SkyEPGAdapter(list), foucusIndex, isCurrent);
                    setSecondEPGfocus();
                    return;
                case 2:
                    Log.d("Maxs25","showSecondEPG:2");
                    arrowView.clearAnimation();
                    arrowView.setVisibility(View.VISIBLE);
                    this.epg2.setPreViewWidth(this.epg1.getWidth());
                    this.epg2.setEPGAdapter(new SkyEPGAdapter(list), foucusIndex, isCurrent);
                    if (this.isFristRefresh) {
                        this.epg1.setLastFocusItemID(0);
                        if (this.epg1.getEpgItems() != null && this.epg1.getEpgItems().size() > 0) {
                            ((SkyEPGItem) this.epg1.getEpgItems().get(0)).setSelectState(true);
                        }
                        this.epg1.setItemStateShowSecond(true);

                        setSecondEPGfocus();
                        return;
                    }
                    return;
                case 3:
                    if (getVisibility() != View.VISIBLE) {
                        setVisibility(View.VISIBLE);
                    }
                    clearAnimation();
                    this.epg1.setVisibility(View.INVISIBLE);
                    arrowView.clearAnimation();
                    arrowView.setVisibility(View.INVISIBLE);
                    this.epg2.setPreViewWidth(this.epg1.getWidth());
                    this.epg2.setEPGAdapter(new SkyEPGAdapter(list), foucusIndex, isCurrent);
                    setSecondEPGfocus();
                    return;
                default:
                    return;
            }
        }
    }

    public void upDataSecondEPG(List<SkyEPGData> list, int i, boolean z) {
        if (list != null && list.size() > 0) {
            ///TVUIDebug.debug("upDataSecondEPG isCurrentChannelInList:" + z);
            isRefreshSencond = true;
            this.menu_2_dataCount = list.size();
            arrowView.setVisibility(View.VISIBLE);
            this.epg2.setPreViewWidth(this.epg1.getWidth());
            this.epg2.upDataEPGAdapter(new SkyEPGAdapter(list), i, z, this.isFristRefresh);
        }
    }

    public void upDateSecondEPG(boolean z) {
        if (this.epg2 != null) {
            this.epg2.upDataEPGAdapter(z);
        }
    }
}
