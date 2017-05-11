package com.horion.tv.ui.rightepg;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.horion.tv.R;
///import com.horion.tv.ui.util.TVUIDebug;
import com.horion.tv.ui.util.UIConstant;
import com.ktc.framework.skycommondefine.SkyworthBroadcastKey;
import com.ktc.ui.blurbg.BlurBgLayout;
import com.ktc.ui.blurbg.BlurBgLayout.PAGETYPE;
import com.ktc.util.KtcScreenParams;

import java.util.ArrayList;
import java.util.List;

public class SkyRightEPGView extends FrameLayout {
    private static final int ONKEY_DOWN = 18;
    private static final int ONKEY_UP = 17;
    private int animDuration = 180;
    private BlurBgLayout bgLayout = null;
    private SkyEventItem bottomItem;
    private LinearLayout channelLayout;
    private LayoutParams channelLp;
    private TextView channelName;
    private TextView channelNum;
    private int curChannelIndex = 0;
    private int curItemIndex = 0;
    private int dataCount = 0;
    private boolean inFastMode;
    private boolean isAnimRunning = false;
    private List<SkyEventData> itemDatas;
    private int itemHeight = 110;
    OnKeyListener itemOnKeyListener = new OnKeyListener() {
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            ///TVUIDebug.debug("OnKeyListener:" + i);
            if (keyEvent.getAction() != 0) {
                return false;
            }
            if (i == 19 || i == 20) {
                SkyRightEPGView.this.mListener.onResetRightEPGViewMonitorTimer();
                SkyRightEPGView.this.mListener.onRightEPGViewItemOnKeySlide(i, view);
                if (System.currentTimeMillis() - SkyRightEPGView.this.onKeyTime < 150) {
                    SkyRightEPGView.this.up_down_duration = SkyRightEPGView.this.shortAnimDuration;
                    SkyRightEPGView.this.inFastMode = true;
                } else {
                    SkyRightEPGView.this.up_down_duration = SkyRightEPGView.this.animDuration;
                    SkyRightEPGView.this.inFastMode = false;
                }
                SkyRightEPGView.this.onKeyTime = System.currentTimeMillis();
            } else {
                SkyRightEPGView.this.mListener.onRecallbackHideRightEPGView();
            }
            if (SkyRightEPGView.this.isAnimRunning) {
                return true;
            }
            switch (i) {
                case 19:
                    ///TVUIDebug.debug("KEYCODE_DPAD_UP:");
                    SkyEventUtils.getInstance().setFristFlag(false);
                    SkyRightEPGView.this.onKeyActionDeriction = 17;
                    SkyRightEPGView skyRightEPGView = SkyRightEPGView.this;
                    skyRightEPGView.curItemIndex = skyRightEPGView.curItemIndex - 1;
                    if (SkyRightEPGView.this.curItemIndex >= 0) {
                        SkyRightEPGView.this.onKeyItemMoveEvent(i, view);
                        return true;
                    }
                    SkyRightEPGView.this.curItemIndex = 0;
                    return true;
                case 20:
                    ///TVUIDebug.debug("KEYCODE_DPAD_DOWN");
                    SkyEventUtils.getInstance().setFristFlag(false);
                    SkyRightEPGView.this.onKeyActionDeriction = 18;
                    SkyRightEPGView skyRightEPGView2 = SkyRightEPGView.this;
                    skyRightEPGView2.curItemIndex = skyRightEPGView2.curItemIndex + 1;
                    if (SkyRightEPGView.this.curItemIndex <= SkyRightEPGView.this.dataCount - 5) {
                        SkyRightEPGView.this.onKeyItemMoveEvent(i, view);
                        return true;
                    }
                    SkyRightEPGView.this.curItemIndex = SkyRightEPGView.this.dataCount - 5;
                    return true;
                default:
                    return false;
            }
        }
    };
    private List<SkyEventItem> itemViewList = new ArrayList();
    private RelativeLayout.LayoutParams listLp;
    private Context mContext;
    private OnRightEPGViewItemOnkeyListener mListener;
    private final int maxShowCount = 5;
    private LayoutParams nameLp;
    private LayoutParams numLp;
    private int onKeyActionDeriction = 0;
    private long onKeyTime = 0;
    private RelativeLayout rootLayout;
    private LayoutParams rootLp;
    private LinearLayout scrollLayout;
    private int shortAnimDuration = 80;
    private SkyEventItem topItem;
    private int up_down_duration = 100;

    public interface OnRightEPGViewItemOnkeyListener {
        void onRecallbackHideRightEPGView();

        void onResetRightEPGViewMonitorTimer();

        boolean onRightEPGViewItemOnKeySlide(int i, View view);
    }

    public SkyRightEPGView(Context context) {
        super(context);
        this.mContext = context;
        this.itemHeight = KtcScreenParams.getInstence(this.mContext).getResolutionValue(110);
        setLayoutParams(new LayoutParams(-1, -1));
        initView();
    }

    private void fillItemData(SkyEventItem skyEventItem, int i, int i2, int i3) {
       /// TVUIDebug.debug("fillItemData: " + i + "| position:" + i2);
        if (i3 == 0) {
            skyEventItem.updataItemValue((SkyEventData) this.itemDatas.get(i));
            skyEventItem.setTextAttribute(36, UIConstant.TEXTCOLOR_NONFOCUS);
            if (i == this.curChannelIndex) {
                skyEventItem.updataItemImage(17);
            } else {
                skyEventItem.updataItemImage(SkyEventUtils.imageIds[i2]);
            }
        } else if (i3 == 20) {
            if (i2 == 0) {
                skyEventItem.updataItemValue((SkyEventData) this.itemDatas.get(i));
                skyEventItem.setTextAttribute(36, UIConstant.TEXTCOLOR_NONFOCUS);
                return;
            }
            skyEventItem.updataItemValue((SkyEventData) this.itemDatas.get(i - 1));
            skyEventItem.setTextAttribute(36, UIConstant.TEXTCOLOR_NONFOCUS);
            if (i == this.curChannelIndex) {
                skyEventItem.updataItemImage(17);
            } else {
                skyEventItem.updataItemImage(SkyEventUtils.imageIds[i2 - 1]);
            }
        } else if (i3 != 19) {
        } else {
            if (i2 == 0) {
                skyEventItem.updataItemValue((SkyEventData) this.itemDatas.get(i));
                skyEventItem.setTextAttribute(36, UIConstant.TEXTCOLOR_NONFOCUS);
                if (i == this.curChannelIndex) {
                    skyEventItem.updataItemImage(17);
                } else {
                    skyEventItem.updataItemImage(SkyEventUtils.imageIds[0]);
                }
            } else if (i2 != 4) {
                skyEventItem.updataItemValue((SkyEventData) this.itemDatas.get(i + 1));
                skyEventItem.setTextAttribute(36, UIConstant.TEXTCOLOR_NONFOCUS);
                if (i == this.curChannelIndex) {
                    skyEventItem.updataItemImage(17);
                } else {
                    skyEventItem.updataItemImage(SkyEventUtils.imageIds[i2 + 1]);
                }
            }
        }
    }

    private void initView() {
        int i = 0;
        this.rootLayout = new RelativeLayout(this.mContext);
        this.rootLp = new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(660), -2);
        this.rootLp.gravity = Gravity.RIGHT;
        this.bgLayout = new BlurBgLayout(this.mContext);
        this.bgLayout.setPageType(PAGETYPE.OTHER_PAGE);
        this.rootLayout.addView(this.bgLayout, new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(660), KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080)));
        this.channelLayout = new LinearLayout(this.mContext);
        this.channelLayout.setId(R.id.sky_rightepg_channellayout);
        this.channelLayout.setOrientation(LinearLayout.VERTICAL);
        this.channelLp = new LayoutParams(-1, -2);
        this.channelLp.gravity = Gravity.RIGHT;
        this.channelNum = new TextView(this.mContext);
        this.channelNum.setId(R.id.sky_rightepg_channelnum);
        this.channelNum.setSingleLine(true);
        this.channelNum.setTextColor(UIConstant.TEXTCOLOR_NONFOCUS);
        this.channelNum.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(100));
        this.channelNum.setGravity(5);
        this.numLp = new LayoutParams(-2, -2);
        this.numLp.rightMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(80);
        this.numLp.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(30);
        this.numLp.gravity = Gravity.RIGHT;
        this.channelLayout.addView(this.channelNum, this.numLp);
        this.channelName = new TextView(this.mContext);
        this.channelName.setId(R.id.sky_rightepg_channelname);
        this.channelName.setSingleLine(true);
        this.channelName.setTextColor(UIConstant.TEXTCOLOR_NONFOCUS);
        this.channelName.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(45));
        this.channelName.setGravity(5);
        this.nameLp = new LayoutParams(-2, -2);
        this.nameLp.rightMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(80);
        this.nameLp.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(3);
        this.nameLp.gravity = Gravity.RIGHT;
        this.channelLayout.addView(this.channelName, this.nameLp);
        this.rootLayout.addView(this.channelLayout, this.channelLp);
        this.scrollLayout = new LinearLayout(this.mContext);
        this.scrollLayout.setOrientation(LinearLayout.VERTICAL);
        this.scrollLayout.setGravity(5);
        this.scrollLayout.setPadding(0, KtcScreenParams.getInstence(this.mContext).getResolutionValue(10), 0, 0);
        this.listLp = new RelativeLayout.LayoutParams(-2, KtcScreenParams.getInstence(this.mContext).getResolutionValue(550));
        this.listLp.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(SkyworthBroadcastKey.SKY_BROADCAST_KEY_SENSE_BACK);
        this.listLp.rightMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(0);
        this.listLp.addRule(3, 4);
        this.listLp.addRule(7, 4);
        this.rootLayout.addView(this.scrollLayout, this.listLp);
        this.scrollLayout.setFocusable(true);
        this.scrollLayout.requestFocus();
        this.scrollLayout.setOnKeyListener(this.itemOnKeyListener);
        this.topItem = new SkyEventItem(this.mContext);
        this.bottomItem = new SkyEventItem(this.mContext);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.topMargin = -this.itemHeight;
        new LayoutParams(-2, -2).topMargin = this.itemHeight * 5;
        while (i < 5) {
            SkyEventItem skyEventItem = new SkyEventItem(this.mContext);
            this.itemViewList.add(skyEventItem);
            this.scrollLayout.addView(skyEventItem, new LayoutParams(-2, -2));
            i++;
        }
        this.scrollLayout.addView(this.topItem, layoutParams);
        this.scrollLayout.addView(this.bottomItem, layoutParams);
        addView(this.rootLayout, this.rootLp);
    }

    private void onKeyItemMoveEvent(int i, View view) {
        this.isAnimRunning = true;
        ///TVUIDebug.debug("inFastMode = " + this.inFastMode);
        TimeInterpolator linearInterpolator = null;
        if (this.inFastMode) {
            linearInterpolator = new LinearInterpolator();
        } else {
            AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        }
        int i2;
        if (i == 19) {
            ///TVUIDebug.debug("KEYCODE_DPAD_UP:" + this.curItemIndex);
            this.topItem.setAlpha(0.0f);
            fillItemData(this.topItem, this.curItemIndex, 0, 19);
           /// TVUIDebug.debug("itemHeight:" + this.itemHeight);
            for (i2 = 0; i2 < 5; i2++) {
                if (i2 != 0) {
                    fillItemData((SkyEventItem) this.itemViewList.get(i2), this.curItemIndex + i2, i2, 19);
                }
                if (i2 == 4) {
                    ((SkyEventItem) this.itemViewList.get(i2)).animate().alpha(0.0f).y((float) (this.itemHeight * (i2 + 1))).setInterpolator(linearInterpolator).setDuration(10).setListener(null);
                } else {
                    ((SkyEventItem) this.itemViewList.get(i2)).animate().y((float) (this.itemHeight * (i2 + 1))).setInterpolator(linearInterpolator).setDuration((long) this.up_down_duration).setListener(null);
                }
            }
            this.topItem.animate().alpha(1.0f).y(0.0f).setDuration((long) this.up_down_duration).setListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    SkyEventItem skyEventItem = (SkyEventItem) SkyRightEPGView.this.itemViewList.get(4);
                    SkyRightEPGView.this.itemViewList.remove(skyEventItem);
                    SkyRightEPGView.this.itemViewList.add(0, SkyRightEPGView.this.topItem);
                    SkyRightEPGView.this.topItem = skyEventItem;
                    SkyRightEPGView.this.topItem.setY((float) (-SkyRightEPGView.this.itemHeight));
                    SkyRightEPGView.this.topItem.setAlpha(0.0f);
                    SkyRightEPGView.this.isAnimRunning = false;
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                    SkyRightEPGView.this.isAnimRunning = true;
                }
            });
        } else if (i == 20) {
           /// TVUIDebug.debug("KEYCODE_DPAD_DOWN:" + this.curItemIndex);
            this.bottomItem.setY((float) (this.itemHeight * 5));
            this.bottomItem.setAlpha(0.0f);
            fillItemData(this.bottomItem, this.curItemIndex + 5, 5, 20);
            ///TVUIDebug.debug("itemHeight:" + this.itemHeight);
            for (i2 = 0; i2 < 5; i2++) {
                if (i2 != 0) {
                   /// TVUIDebug.debug("index:" + i2);
                    fillItemData((SkyEventItem) this.itemViewList.get(i2), this.curItemIndex + i2, i2, 20);
                }
                if (i2 == 0) {
                    ((SkyEventItem) this.itemViewList.get(i2)).animate().alpha(0.0f).y((float) (this.itemHeight * (i2 - 1))).setInterpolator(linearInterpolator).setDuration((long) this.up_down_duration).setListener(null);
                } else {
                    ((SkyEventItem) this.itemViewList.get(i2)).animate().y((float) (this.itemHeight * (i2 - 1))).setInterpolator(linearInterpolator).setDuration((long) this.up_down_duration).setListener(null);
                }
            }
            this.bottomItem.animate().alpha(1.0f).y((float) (this.itemHeight * 4)).setDuration((long) this.up_down_duration).setListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    ///TVUIDebug.debug("onAnimationEnd");
                    SkyEventItem skyEventItem = (SkyEventItem) SkyRightEPGView.this.itemViewList.get(0);
                    SkyRightEPGView.this.itemViewList.remove(skyEventItem);
                    SkyRightEPGView.this.itemViewList.add(SkyRightEPGView.this.bottomItem);
                    SkyRightEPGView.this.bottomItem = skyEventItem;
                    SkyRightEPGView.this.bottomItem.setY((float) (SkyRightEPGView.this.itemHeight * 5));
                    SkyRightEPGView.this.bottomItem.setAlpha(0.0f);
                    SkyRightEPGView.this.isAnimRunning = false;
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                    SkyRightEPGView.this.isAnimRunning = true;
                }
            });
        }
    }

    private void refreshList() {
        int i = this.dataCount < 5 ? this.dataCount : 5;
       /// TVUIDebug.debug(">>>refreshList:" + i);
        for (int i2 = 0; i2 < i; i2++) {
            fillItemData((SkyEventItem) this.itemViewList.get(i2), this.curItemIndex + i2, i2, 0);
        }
    }

    public void setRightEPGViewItemOnkeyListener(OnRightEPGViewItemOnkeyListener onRightEPGViewItemOnkeyListener) {
        this.mListener = onRightEPGViewItemOnkeyListener;
    }

    public void showChannelInfo(String str, String str2) {
        this.channelNum.setText(str);
        this.channelName.setText(str2);
    }

    public void showRightEPGView(List<SkyEventData> list, int i) {
        SkyEventUtils.getInstance().setFristFlag(true);
        this.curChannelIndex = i;
        this.curItemIndex = i;
        this.dataCount = list.size();
        this.itemDatas = list;
        refreshList();
    }
}
