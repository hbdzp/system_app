package com.horion.tv.ui.leftepg;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktc.framework.skycommondefine.SkyworthBroadcastKey;
import com.ktc.util.KtcScreenParams;
import com.horion.tv.R;
import com.horion.tv.define.object.Channel.CHANNEL_TYPE;
import com.horion.tv.ui.util.UIConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
///import org.apache.http4.HttpStatus;

public class SkySecondEPG extends FrameLayout {
    public static boolean isInSortModeing = false;
    public static int preViewWidth = 0;
    private ImageView arrowDown;
    private int arrowHeight = 120;
    private ImageView arrowUp;
    private LayoutParams contentParams;
    private View currentItemView = null;
    private int dataCount = 0;
    private SkyEPGData firstData = null;
    private boolean isAnimStart = false;
    private boolean isCurrent = false;
    public boolean isDismisAnimStart = false;
    private boolean isInSortMode = false;
    private boolean isNomal = false;
    OnClickListener itemClickListener = new OnClickListener() {
        private   int[] EPGMODESwitchesValues;

        private  int[] getEPGMODESwitchesValues() {
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

        public void onClick(View view) {
            if (!SkySecondEPG.this.isAnimStart) {
                SkySecondEPG.this.midleData = ((SkyEPGItem_2) view).getItemData();
                switch (getEPGMODESwitchesValues()[SkyCommenEPG.epgMode.ordinal()]) {
                    case 1:
                        if (SkySecondEPG.this.mListener != null) {
                            SkySecondEPG.this.mListener.onSecondEPGItemOnClick(SkyCommenEPG.epgMode, view.getId(), view, ((SkyEPGItem_2) view).getItemData(), SkySecondEPG.this.menuItems, -1);
                            return;
                        }
                        return;
                    case 2:
                        if (SkySecondEPG.this.mListener != null) {
                            SkySecondEPG.this.mListener.onSecondEPGItemOnClick(SkyCommenEPG.epgMode, view.getId(), view, ((SkyEPGItem_2) view).getItemData(), SkySecondEPG.this.menuItems, -1);
                            return;
                        }
                        return;
                    case 3:
                        SkyEPGItem_2 skyEPGItem_2;
                        if (!SkySecondEPG.this.isInSortMode) {
                            SkySecondEPG.this.isInSortMode = true;
                            SkySecondEPG.this.arrowUp.setVisibility(View.VISIBLE);
                            SkySecondEPG.this.arrowDown.setVisibility(View.VISIBLE);
                            SkyCommenEPG.sortButton.setVisibility(View.VISIBLE);
                            SkyEPGItem_2 skyEPGItem_22 = (SkyEPGItem_2) view;
                            SkySecondEPG.this.orignalChannelNumber = skyEPGItem_22.getChannelTextViewNumber();
                            skyEPGItem_22.setItemSortState(true);
                            if (SkySecondEPG.this.menuItems != null) {
                                for (SkyEPGItem_2 skyEPGItem_23 : SkySecondEPG.this.menuItems) {
                                    if (skyEPGItem_23.getItemData().getItemIndexID() != skyEPGItem_22.getItemData().getItemIndexID()) {
                                        skyEPGItem_23.setItemStateForSecond(false);
                                    }
                                }
                                return;
                            }
                            return;
                        } else if (!SkySecondEPG.isInSortModeing) {
                            SkySecondEPG.this.isInSortMode = false;
                            SkySecondEPG.isInSortModeing = true;
                            SkyEPGItem_2 skyEPGItem_23 = (SkyEPGItem_2) view;
                            skyEPGItem_23.setItemSortState(false);
                            SkyCommenEPG.sortButton.setVisibility(View.INVISIBLE);
                            int channelTextViewNumber = skyEPGItem_23.getChannelTextViewNumber();
                            if (SkySecondEPG.this.mListener != null) {
                                SkySecondEPG.this.mListener.onSecondEPGItemOnClick(SkyCommenEPG.epgMode, view.getId(), view, ((SkyEPGItem_2) view).getItemData(), SkySecondEPG.this.menuItems, channelTextViewNumber);
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    };
    private int itemHeight = 145;
    private int itemMargin = 0;
    OnKeyListener itemOnKeyListener = new OnKeyListener() {
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyEvent.getAction() != 0) {
                return false;
            }
            Log.d("Maxs25","$$$SkySecondEPG:itemOnKeyListener:view.toString() " + ((SkyEPGItem_2) view).getItemData().getItemTitle() + " " +((SkyEPGItem_2) view).toString() );
            ///Log.d("Maxs25","####itemOnKeyListener:view = " + view.get)
            SkySecondEPG.this.lastFocusItemID = -1;
            int id = view.getId();
            ///TVUIDebug.debug("id :" + id + "  onKey keyCode:" + keyCode);
            SkyEPGItem_2 skyEPGItem_2;
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (SkySecondEPG.this.isAnimStart) {
                        return true;
                    }
                    if (SkyCommenEPG.epgMode != EPGMODE.EPGMODE_SORT || !SkySecondEPG.this.isInSortMode) {
                        return SkySecondEPG.this.mListener != null ? SkySecondEPG.this.mListener.onSecondEPGItemOnKeyBack(id, view) : false;
                    } else {
                        SkySecondEPG.this.isInSortMode = false;
                        SkySecondEPG.this.arrowUp.setVisibility(View.INVISIBLE);
                        SkySecondEPG.this.arrowDown.setVisibility(View.INVISIBLE);
                        SkyCommenEPG.sortButton.setVisibility(View.INVISIBLE);
                        skyEPGItem_2 = (SkyEPGItem_2) view;
                        if (SkySecondEPG.this.orignalChannelNumber >= 1) {
                            skyEPGItem_2.channelTextViewNumberSet(SkySecondEPG.this.orignalChannelNumber);
                        }
                        skyEPGItem_2.setItemSortState(false);
                        if (SkySecondEPG.this.menuItems != null) {
                            for (SkyEPGItem_2 skyEPGItem_22 : SkySecondEPG.this.menuItems) {
                                if (skyEPGItem_22.getItemData().getItemIndexID() != skyEPGItem_2.getItemData().getItemIndexID()) {
                                    skyEPGItem_22.setItemStateForSecond(true);
                                }
                            }
                        }
                        return true;
                    }
                case KeyEvent.KEYCODE_DPAD_UP:
                case SkyworthBroadcastKey.SKY_KEY_SHORT_SHUTTLE_LEFT /*764*/:
                    Log.d("Maxs25","-------->KEYCODE_DPAD_UP:SkyCommenEPG.epgMode = " + SkyCommenEPG.epgMode);
                    if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT && SkySecondEPG.isInSortModeing) {
                        return true;
                    }
                    if (System.currentTimeMillis() - SkySecondEPG.this.onKeyTime < 150) {
                        SkySecondEPG.this.up_down_duration = 50;
                    } else {
                        SkySecondEPG.this.up_down_duration = 160;
                    }
                    SkySecondEPG.this.onKeyTime = System.currentTimeMillis();
                    Log.d("Maxs25","-------->KEYCODE_DPAD_UP:isAnimStart = " + isAnimStart);
                    if (SkySecondEPG.this.isAnimStart) {
                        return true;
                    }
                    Log.d("Maxs25","-------->KEYCODE_DPAD_UP:isInSortMode = " + isInSortMode);
                    if (SkyCommenEPG.epgMode.equals(EPGMODE.EPGMODE_SORT) && SkySecondEPG.this.isInSortMode) {
                        skyEPGItem_2 = (SkyEPGItem_2) view;
                        if (skyEPGItem_2.getChannelTextViewNumber() <= 1) {
                            return true;
                        }
                        skyEPGItem_2.channelTextViewNumberDec();
                        return true;
                    }
                    if (SkySecondEPG.this.mListener != null) {
                        Log.d("Maxs25","SkySecondEPG:itemOnKeyListener:id = " + id + " /view.getItemData().getItemIndexID() " + ((SkyEPGItem_2) view).getItemData().getItemIndexID());
                        Log.d("Maxs25","SkySecondEPG:itemOnKeyListener:view.toString() " + ((SkyEPGItem_2) view).getItemData().getChannelNumber() + " " +((SkyEPGItem_2) view).toString() );
                        SkySecondEPG.this.mListener.onSecondEPGItemOnKeyUp(SkyCommenEPG.epgMode, id, view);
                    }
                    Log.d("Maxs25","------>isNomal = " + isNomal + " /id = " + id);
                    if (SkySecondEPG.this.isNomal) {
                        SkySecondEPG.this.onKeyItemMoveEvent(keyCode, view);
                        return true;
                    } else if (id == 0) {
                        return true;
                    } else {
                        SkySecondEPG.this.onKeyItemMoveEvent(keyCode, view);
                        return true;
                    }
                case KeyEvent.KEYCODE_DPAD_DOWN:
                case SkyworthBroadcastKey.SKY_KEY_SHORT_SHUTTLE_RIGHT /*765*/:
                    if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT && SkySecondEPG.isInSortModeing) {
                        return true;
                    }
                    if (System.currentTimeMillis() - SkySecondEPG.this.onKeyTime < 150) {
                        SkySecondEPG.this.up_down_duration = 50;
                    } else {
                        SkySecondEPG.this.up_down_duration = 160;
                    }
                    SkySecondEPG.this.onKeyTime = System.currentTimeMillis();
                    if (SkySecondEPG.this.isAnimStart) {
                        return true;
                    }
                    if (SkyCommenEPG.epgMode.equals(EPGMODE.EPGMODE_SORT) && SkySecondEPG.this.isInSortMode) {
                        skyEPGItem_2 = (SkyEPGItem_2) view;
                        if (((Integer) SkySecondEPG.this.maxChannelNumberMap.get(skyEPGItem_2.getItemData().channelType)).intValue() <= skyEPGItem_2.getChannelTextViewNumber()) {
                            return true;
                        }
                        skyEPGItem_2.channelTextViewNumberInc();
                        return true;
                    }
                    if (SkySecondEPG.this.mListener != null) {
                        SkySecondEPG.this.mListener.onSecondEPGItemOnKeyDown(SkyCommenEPG.epgMode, id, view);
                    }
                    if (SkySecondEPG.this.isNomal) {
                        SkySecondEPG.this.onKeyItemMoveEvent(keyCode, view);
                        return true;
                    } else if (id == SkySecondEPG.this.dataCount - 1) {
                        return true;
                    } else {
                        SkySecondEPG.this.onKeyItemMoveEvent(keyCode, view);
                        return true;
                    }
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    ///TVUIDebug.debug("isShowAnimStart == " + SkySecondEPG.this.isDismisAnimStart);
                    return SkySecondEPG.this.isAnimStart ? true : SkySecondEPG.this.mListener != null ? SkySecondEPG.this.mListener.onSecondEPGItemOnKeyLeft(id, view) : true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (SkySecondEPG.this.isAnimStart) {
                        return true;
                    }
                    if (!SkyCommenEPG.epgMode.equals(EPGMODE.EPGMODE_SORT) || !SkySecondEPG.this.isInSortMode) {
                        return SkySecondEPG.this.mListener != null ? SkySecondEPG.this.mListener.onSecondEPGItemOnKeyRight(id, view, ((SkyEPGItem_2) view).getItemData()) : true;
                    } else {
                        //TVUIDebug.debug("SkyCommenEPG.sortButton.requestFocus()");
                        SkySecondEPG.this.currentItemView = view;
                        SkyCommenEPG.sortButton.requestFocus();
                        return true;
                    }
                default:
                    return false;
            }
        }
    };
    private SkyEPGItem_2 item_temp;
    private int lastFocusItemID = -1;
    private SkyEPGAdapter mAdapter;
    private Context mContext;
    private int mFocuseId = 0;
    private OnSecondEPGItemOnkeyListener mListener;
    private HashMap<CHANNEL_TYPE, Integer> maxChannelNumberMap = new HashMap();
    private int menuHeight = 700;
    private List<SkyEPGItem_2> menuItems;
    private SkyEPGData midleData = null;
    private int midleIndex = 0;
    private int midlePostion = ((this.menuHeight / 2) - (this.itemHeight / 2));
    private long onKeyTime = 0;
    private int orignalChannelNumber = -1;
    private int pageCount = 5;
    private SkyEPGItem_2 postItem;
    private OnClickListener sortButtonOnClickListener = new OnClickListener() {
        public void onClick(View view) {
            if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT && !SkySecondEPG.isInSortModeing && SkySecondEPG.this.currentItemView != null) {
                SkySecondEPG.this.isInSortMode = false;
                SkySecondEPG.isInSortModeing = true;
                if (SkySecondEPG.this.mListener != null) {
                    ((SkyEPGItem_2) SkySecondEPG.this.currentItemView).setItemSortState(false);
                    SkySecondEPG.this.mListener.onSecondEPGItemOnClick(SkyCommenEPG.epgMode, SkySecondEPG.this.currentItemView.getId(), SkySecondEPG.this.currentItemView, ((SkyEPGItem_2) SkySecondEPG.this.currentItemView).getItemData(), SkySecondEPG.this.menuItems, 1);
                }
            }
        }
    };
    private OnKeyListener sortButtonOnKeyListener = new OnKeyListener() {
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0) {
                ///TVUIDebug.debug("onKey keyCode:" + i);
                switch (i) {
                    case 4:
                        ///TVUIDebug.debug("SkyCommenEPG.epgMode:" + SkyCommenEPG.epgMode + " isInSortMode:" + SkySecondEPG.this.isInSortMode);
                        if (SkySecondEPG.this.isAnimStart) {
                            return true;
                        }
                        if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT && SkySecondEPG.this.isInSortMode) {
                            SkySecondEPG.this.isInSortMode = false;
                            if (SkySecondEPG.this.currentItemView != null) {
                                SkySecondEPG.this.currentItemView.requestFocus();
                                SkyEPGItem_2 skyEPGItem_2 = (SkyEPGItem_2) SkySecondEPG.this.currentItemView;
                                if (SkySecondEPG.this.orignalChannelNumber >= 1) {
                                    skyEPGItem_2.channelTextViewNumberSet(SkySecondEPG.this.orignalChannelNumber);
                                }
                                skyEPGItem_2.setItemSortState(false);
                                if (SkySecondEPG.this.menuItems != null) {
                                    for (SkyEPGItem_2 skyEPGItem_22 : SkySecondEPG.this.menuItems) {
                                        if (skyEPGItem_22.getItemData().getItemIndexID() != skyEPGItem_2.getItemData().getItemIndexID()) {
                                            skyEPGItem_22.setItemStateForSecond(true);
                                        }
                                    }
                                }
                            }
                            SkySecondEPG.this.arrowUp.setVisibility(View.INVISIBLE);
                            SkySecondEPG.this.arrowDown.setVisibility(View.INVISIBLE);
                            SkyCommenEPG.sortButton.setVisibility(View.INVISIBLE);
                            return true;
                        }
                    case 19:
                    case 20:
                        return true;
                    case 21:
                        ///TVUIDebug.debug("isShowAnimStart == " + SkySecondEPG.this.isDismisAnimStart);
                        if (SkySecondEPG.this.isAnimStart) {
                            return true;
                        }
                        if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT && SkySecondEPG.this.isInSortMode) {
                            if (SkySecondEPG.this.currentItemView != null) {
                                SkySecondEPG.this.currentItemView.requestFocus();
                            }
                            return true;
                        }
                    case 22:
                        return true;
                    case 23:
                        return false;
                }
            }
            return false;
        }
    };
    private TextView tipsTextView;
    private int up_down_duration = 160;

    public interface OnSecondEPGItemOnkeyListener {
        boolean onSecondEPGItemOnClick(EPGMODE epgmode, int i, View view, SkyEPGData skyEPGData, List<SkyEPGItem_2> list, int i2);

        boolean onSecondEPGItemOnKeyBack(int i, View view);

        boolean onSecondEPGItemOnKeyDown(EPGMODE epgmode, int i, View view);

        boolean onSecondEPGItemOnKeyLeft(int i, View view);

        boolean onSecondEPGItemOnKeyRight(int i, View view, SkyEPGData skyEPGData);

        boolean onSecondEPGItemOnKeyUp(EPGMODE epgmode, int i, View view);
    }

    public SkySecondEPG(Context context) {
        super(context);
        this.mContext = context;
        this.menuHeight = KtcScreenParams.getInstence(this.mContext).getResolutionValue(700);
        this.itemHeight = KtcScreenParams.getInstence(this.mContext).getResolutionValue(145);
        this.itemMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(0);
        this.arrowHeight = KtcScreenParams.getInstence(this.mContext).getResolutionValue(120);
        this.midlePostion = (this.menuHeight / 2) - (this.itemHeight / 2);
        initView();
    }

    private SkyEPGData getLinkList(SkyEPGAdapter skyEPGAdapter, int mFocuseId) {
        Log.d("Maxs24","------>getLinkList:mFocuseId = " + mFocuseId + " /skyEPGAdapter.getCount() = " + skyEPGAdapter.getCount());
        int i2 = 0;
        int count = skyEPGAdapter.getCount();
        SkyEPGData ePGItemData = skyEPGAdapter.getEPGItemData(0);
        ePGItemData.next = ePGItemData;
        SkyEPGData skyEPGData = ePGItemData;
        for (int j = 1; j < count; j++) {
            SkyEPGData ePGItemData2 = skyEPGAdapter.getEPGItemData(j);
            ePGItemData2.next = skyEPGData;
            skyEPGData.next = ePGItemData2;
            ePGItemData2.pre = skyEPGData;
            skyEPGData = skyEPGData.next;
        }
        skyEPGData.next = ePGItemData;
        ePGItemData.pre = skyEPGData;
        SkyEPGData ePGItemData3 = skyEPGAdapter.getEPGItemData(mFocuseId);
        Log.d("Maxs24","----->ePGItemData3.getChannelNumber() =  " + ePGItemData3.getChannelNumber());
        Log.d("Maxs24","----->ePGItemData3ePGItemData3.getItemIndexID() =  " + ePGItemData3.getItemIndexID());
        for (int m = 0; m < count; m++) {
            if (skyEPGData.getItemIndexID() == ePGItemData3.getItemIndexID()) {
                ///TVUIDebug.debug(skyEPGData.getItemTitle() + "; --> " + skyEPGData.next.getItemTitle() + "; -- >" + skyEPGData.next.next.getItemTitle());
                Log.d("Maxs24",skyEPGData.getItemTitle() + "; --> " + skyEPGData.next.getItemTitle() + "; -- >" + skyEPGData.next.next.getItemTitle());
                break;
            }
            skyEPGData = skyEPGData.next;
        }
        while (i2 < count - 2) {
            skyEPGData = skyEPGData.next;
            i2++;
        }
        return skyEPGData;
    }

    private void initView() {
        this.menuItems = new ArrayList();
        isInSortModeing = false;
        this.contentParams = new LayoutParams(-2, this.menuHeight);
        setLayoutParams(this.contentParams);
    }

    private void onKeyItemMoveEvent(int keyCode, View view) {
        this.isAnimStart = true;
        final int id = view.getId();
        int i2 = this.itemHeight + this.itemMargin;
        int i3 = this.up_down_duration;
        LayoutParams layoutParams;
        AnimationSet animationSet;
        TranslateAnimation translateAnimation;
        SkyEPGItem_2 skyEPGItem_2;
        Animation translateAnimation2;
        AlphaAnimation alphaAnimation;
        SkyEPGItem_2 skyEPGItem_22;
        LayoutParams layoutParams2;
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == SkyworthBroadcastKey.SKY_KEY_SHORT_SHUTTLE_LEFT) {
            this.mFocuseId = ((SkyEPGItem_2) view).getItemData().getItemIndexID() - 1;
            if (this.mFocuseId == -1) {
                this.mFocuseId = this.mAdapter.getCount() - 1;
            }
            Log.d("Maxs25","KEYCODE_DPAD_UP,mFocuseId :" + this.mFocuseId + "  mAdapter.getCount():" + this.mAdapter.getCount());
            ///TVUIDebug.debug("KEYCODE_DPAD_UP,mFocuseId :" + this.mFocuseId + "  mAdapter.getCount():" + this.mAdapter.getCount());
            Log.d("Maxs25","----------->isNomal = " + isNomal);
            if (this.isNomal) {
                this.item_temp = this.postItem;
                this.firstData = this.firstData.pre;
                this.item_temp.updataItemValue(this.firstData);
                layoutParams = (LayoutParams) this.postItem.getLayoutParams();
                layoutParams.topMargin = ((LayoutParams) ((SkyEPGItem_2) this.menuItems.get(0)).getLayoutParams()).topMargin - i2;
                this.item_temp.setLayoutParams(layoutParams);
                this.postItem = (SkyEPGItem_2) this.menuItems.get(this.pageCount - 1);
                this.menuItems.remove(this.pageCount - 1);
                this.menuItems.add(0, this.item_temp);
                Log.d("Maxs25","----------->pageCount = " + pageCount);
                for (int m = 0; m < this.pageCount; m++) {
                    ((SkyEPGItem_2) this.menuItems.get(m)).setId(m);
                    ((SkyEPGItem_2) this.menuItems.get(m)).setSelectState(false);
                    ((SkyEPGItem_2) this.menuItems.get(m)).setOnKeyListener(this.itemOnKeyListener);
                    ((SkyEPGItem_2) this.menuItems.get(m)).setOnClickListener(this.itemClickListener);
                    if (this.midleData.equals(((SkyEPGItem_2) this.menuItems.get(m)).getItemData())) {
                      ///  TVUIDebug.debug("midleData== " + this.midleData.getItemTitle());
                        Log.d("Maxs25","midleData== " + this.midleData.getItemTitle());
                        if (this.isCurrent) {
                            ((SkyEPGItem_2) this.menuItems.get(m)).setSelectState(true);
                        }
                    }
                    Log.d("Maxs25","title == " + ((SkyEPGItem_2) this.menuItems.get(m)).getItemData().getItemTitle());
                    ///TVUIDebug.debug("title == " + ((SkyEPGItem_2) this.menuItems.get(m)).getItemData().getItemTitle());
                }

                /* 因为这是是向上按键，postItem = (SkyEPGItem_2) this.menuItems.get(this.pageCount - 1);
                 * postItem表示当前已经显示页面的五个条目中的最后一个条目，因为是向上按键，这里是为了隐藏
                 * 第五个条目，也即是第五个条目向下移动并隐藏，
                 */
                animationSet = new AnimationSet(false);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) i2);
                translateAnimation.setDuration((long) up_down_duration);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation);
                alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setDuration((long) up_down_duration);
                animationSet.addAnimation(alphaAnimation);
                this.postItem.startAnimation(animationSet);
                animationSet.setAnimationListener(new AnimationListener() {
                    public void onAnimationEnd(Animation animation) {
                        Log.d("Maxs25","up:##########the fith item is hide end !");
                        SkySecondEPG.this.postItem.setVisibility(View.INVISIBLE);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }
                });



                for (int k = 0; k < this.pageCount; k++) {
                    skyEPGItem_2 = (SkyEPGItem_2) this.menuItems.get(k);
                    Log.d("Maxs25","-- i = " + k + "--->" + skyEPGItem_2.getItemData().getItemTitle());
                    layoutParams = (LayoutParams) skyEPGItem_2.getLayoutParams();
                    layoutParams.topMargin += i2;
                    skyEPGItem_2.setLayoutParams(layoutParams);
                    skyEPGItem_2.clearAnimation();
                    animationSet = new AnimationSet(false);
                    translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, (float) (-i2), 0.0f);
                    translateAnimation2.setDuration((long) up_down_duration);
                    translateAnimation2.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation2);
                    if (skyEPGItem_2.getVisibility() != View.VISIBLE) {
                        skyEPGItem_2.setVisibility(View.VISIBLE);
                    }
                    if (k == 0) {
                        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                        alphaAnimation.setFillAfter(true);
                        alphaAnimation.setDuration((long) up_down_duration);
                        animationSet.addAnimation(alphaAnimation);
                    }
                    skyEPGItem_2.startAnimation(animationSet);
                    if (k == 0) {
                        Log.d("Maxs25","------>UP:k = 0<--------");
                        translateAnimation2.setAnimationListener(new AnimationListener() {
                            public void onAnimationEnd(Animation animation) {
                                Log.d("Maxs25","up:##########the first item is Visible end ! id = " + id);
                                if (id == 0 && SkySecondEPG.this.menuItems != null && SkySecondEPG.this.menuItems.size() > id) {
                                    ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id)).requestFocus();
                                }
                                SkySecondEPG.this.isAnimStart = false;
                            }

                            public void onAnimationRepeat(Animation animation) {
                            }

                            public void onAnimationStart(Animation animation) {
                                Log.d("Maxs25","up:start animal " + id);
                            }
                        });
                    } else if (k == id - 1) {
                        translateAnimation2.setAnimationListener(new AnimationListener() {
                            public void onAnimationEnd(Animation animation) {
                                if (id == 0) {
                                    SkySecondEPG.this.isAnimStart = false;
                                }
                                if (SkySecondEPG.this.menuItems != null && SkySecondEPG.this.menuItems.size() > id) {
                                    ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id)).requestFocus();
                                }
                            }

                            public void onAnimationRepeat(Animation animation) {
                            }

                            public void onAnimationStart(Animation animation) {
                            }
                        });
                    }
                }
                return;
            }


            /* 这下面部分表示频道小于5个的循环显示
             *
             */
            for (int k = 0; k < this.pageCount; k++) {
                skyEPGItem_22 = (SkyEPGItem_2) this.menuItems.get(k);
                layoutParams2 = (LayoutParams) skyEPGItem_22.getLayoutParams();
                layoutParams2.topMargin += i2;
                skyEPGItem_22.setLayoutParams(layoutParams2);
                skyEPGItem_22.clearAnimation();
                animationSet = new AnimationSet(false);
                translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, (float) (-i2), 0.0f);
                translateAnimation2.setDuration((long) up_down_duration);
                translateAnimation2.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation2);
                if (skyEPGItem_22.getVisibility() != View.VISIBLE) {
                    skyEPGItem_22.setVisibility(View.VISIBLE);
                }
                if (k == id - 3) {
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration((long) up_down_duration);
                    animationSet.addAnimation(alphaAnimation);
                }
                if (k == id + 2) {
                    alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration((long) up_down_duration);
                    animationSet.addAnimation(alphaAnimation);
                }
                skyEPGItem_22.startAnimation(animationSet);
                if (k == 0) {
                    translateAnimation2.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (id + 2 < SkySecondEPG.this.pageCount && SkySecondEPG.this.menuItems.get(id + 2) != null) {
                                ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id + 2)).setVisibility(View.INVISIBLE);
                            }
                            if (id - 1 == 0 && SkySecondEPG.this.menuItems != null && SkySecondEPG.this.menuItems.size() > id - 1) {
                                ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id - 1)).requestFocus();
                            }
                            SkySecondEPG.this.isAnimStart = false;
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                } else if (k == id - 1) {
                    translateAnimation2.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (id - 1 == 0) {
                                SkySecondEPG.this.isAnimStart = false;
                            }
                            if (SkySecondEPG.this.menuItems != null && SkySecondEPG.this.menuItems.size() > id - 1) {
                                ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id - 1)).requestFocus();
                            }
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                }
            }
            this.midleIndex--;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == SkyworthBroadcastKey.SKY_KEY_SHORT_SHUTTLE_RIGHT) {
            this.mFocuseId = ((SkyEPGItem_2) view).getItemData().getItemIndexID() + 1;
            if (this.mFocuseId == this.mAdapter.getCount()) {
                this.mFocuseId = 0;
            }
            Log.d("Maxs25","KEYCODE_DPAD_DOWN,mFocuseId :" + this.mFocuseId + "  mAdapter.getCount():" + this.mAdapter.getCount());
            //TVUIDebug.debug("KEYCODE_DPAD_DOWN,mFocuseId :" + this.mFocuseId + "  mAdapter.getCount():" + this.mAdapter.getCount());
            if (this.isNomal) {
                this.item_temp = this.postItem;
                this.firstData = this.firstData.next;
                this.item_temp.updataItemValue(this.firstData.next.next.next.next);
                layoutParams = (LayoutParams) this.postItem.getLayoutParams();
                layoutParams.topMargin = ((LayoutParams) ((SkyEPGItem_2) this.menuItems.get(this.pageCount - 1)).getLayoutParams()).topMargin + i2;
                this.item_temp.setLayoutParams(layoutParams);
                this.menuItems.add(this.item_temp);
                this.postItem = (SkyEPGItem_2) this.menuItems.get(0);
                this.menuItems.remove(0);
                for (int m = 0; m < this.pageCount; m++) {
                    ((SkyEPGItem_2) this.menuItems.get(m)).setId(m);
                    ((SkyEPGItem_2) this.menuItems.get(m)).setSelectState(false);
                    ((SkyEPGItem_2) this.menuItems.get(m)).setOnKeyListener(this.itemOnKeyListener);
                    ((SkyEPGItem_2) this.menuItems.get(m)).setOnClickListener(this.itemClickListener);
                    if (this.midleData.equals(((SkyEPGItem_2) this.menuItems.get(m)).getItemData())) {
                       // TVUIDebug.debug("midleData ==  " + this.midleData.getItemTitle());
                        if (this.isCurrent) {
                            ((SkyEPGItem_2) this.menuItems.get(m)).setSelectState(true);
                        }
                    }
                    //TVUIDebug.debug("title == " + ((SkyEPGItem_2) this.menuItems.get(m)).getItemData().getItemTitle());
                }
                animationSet = new AnimationSet(false);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-i2));
                translateAnimation.setDuration((long) up_down_duration);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation);
                alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration((long) up_down_duration);
                animationSet.addAnimation(translateAnimation);
                this.postItem.startAnimation(animationSet);
                animationSet.setAnimationListener(new AnimationListener() {
                    public void onAnimationEnd(Animation animation) {
                        Log.d("Maxs25","Down:##########the first item is hide end !");
                        SkySecondEPG.this.postItem.setVisibility(View.INVISIBLE);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }
                });
                for (int k = 0; k < this.pageCount; k++) {
                    skyEPGItem_2 = (SkyEPGItem_2) this.menuItems.get(k);
                    layoutParams = (LayoutParams) skyEPGItem_2.getLayoutParams();
                    layoutParams.topMargin -= i2;
                    skyEPGItem_2.setLayoutParams(layoutParams);
                    skyEPGItem_2.clearAnimation();
                    animationSet = new AnimationSet(false);
                    translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, (float) i2, 0.0f);
                    translateAnimation2.setDuration((long) up_down_duration);
                    translateAnimation2.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation2);
                    if (skyEPGItem_2.getVisibility() != View.VISIBLE) {
                        skyEPGItem_2.setVisibility(View.VISIBLE);
                    }
                    if (k == this.pageCount - 1) {
                        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                        alphaAnimation.setFillAfter(true);
                        alphaAnimation.setDuration((long) up_down_duration);
                        animationSet.addAnimation(alphaAnimation);
                    }
                    skyEPGItem_2.startAnimation(animationSet);
                    if (k == this.pageCount - 1) {
                        translateAnimation2.setAnimationListener(new AnimationListener() {
                            public void onAnimationEnd(Animation animation) {
                                Log.d("Maxs25","Down:##########the fith item is visible end ! = id = " + id);
                                if (id == SkySecondEPG.this.pageCount - 1 && SkySecondEPG.this.menuItems != null && SkySecondEPG.this.menuItems.size() > id) {
                                    ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id)).requestFocus();
                                }
                                SkySecondEPG.this.isAnimStart = false;
                            }

                            public void onAnimationRepeat(Animation animation) {
                            }

                            public void onAnimationStart(Animation animation) {
                            }
                        });
                    } else if (k == id) {
                        translateAnimation2.setAnimationListener(new AnimationListener() {
                            public void onAnimationEnd(Animation animation) {
                                if (SkySecondEPG.this.menuItems != null && SkySecondEPG.this.menuItems.size() > id) {
                                    ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id)).requestFocus();
                                }
                            }

                            public void onAnimationRepeat(Animation animation) {
                            }

                            public void onAnimationStart(Animation animation) {
                            }
                        });
                    }
                }
                return;
            }
            for (int k = 0; k < this.pageCount; k++) {
                skyEPGItem_22 = (SkyEPGItem_2) this.menuItems.get(k);
                layoutParams2 = (LayoutParams) skyEPGItem_22.getLayoutParams();
                layoutParams2.topMargin -= i2;
                skyEPGItem_22.setLayoutParams(layoutParams2);
                skyEPGItem_22.clearAnimation();
                animationSet = new AnimationSet(false);
                translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, (float) i2, 0.0f);
                translateAnimation2.setDuration((long) up_down_duration);
                translateAnimation2.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation2);
                if (skyEPGItem_22.getVisibility() != View.VISIBLE) {
                    skyEPGItem_22.setVisibility(View.VISIBLE);
                }
                if (k == id - 2) {
                    alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration((long) up_down_duration);
                    animationSet.addAnimation(alphaAnimation);
                }
                if (k == id + 3) {
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration((long) up_down_duration);
                    animationSet.addAnimation(alphaAnimation);
                }
                skyEPGItem_22.startAnimation(animationSet);
                if (k == this.pageCount - 1) {
                    translateAnimation2.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (id - 2 >= 0 && SkySecondEPG.this.menuItems.get(id - 2) != null) {
                                ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id - 2)).setVisibility(View.INVISIBLE);
                            }
                            if (id + 1 == SkySecondEPG.this.pageCount - 1 && SkySecondEPG.this.menuItems != null && SkySecondEPG.this.menuItems.size() > id + 1) {
                                ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id + 1)).requestFocus();
                            }
                            SkySecondEPG.this.isAnimStart = false;
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                } else if (k == id + 1) {
                    translateAnimation2.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (SkySecondEPG.this.menuItems != null && SkySecondEPG.this.menuItems.size() > id + 1) {
                                ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(id + 1)).requestFocus();
                            }
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                }
            }
            this.midleIndex++;
        }
    }

    public int getLastFocusID() {
        return this.lastFocusItemID;
    }

    public OnClickListener getSortButtonOnClickListener() {
        return this.sortButtonOnClickListener;
    }

    public OnKeyListener getSortButtonOnKeyListener() {
        return this.sortButtonOnKeyListener;
    }

    public void setEPGAdapter(SkyEPGAdapter skyEPGAdapter, int foucusIndex, boolean isCurrent) {
        removeAllViews();
        this.isInSortMode = false;
        //TVUIDebug.debug("setEPGAdapter isCurrent:" + isCurrent);
        if (SkyCommenEPG.epgMode != EPGMODE.EPGMODE_DIS) {
            this.arrowUp = new ImageView(this.mContext);
            this.arrowDown = new ImageView(this.mContext);
            this.arrowUp.setImageResource(R.drawable.ui_arrow_up);
            this.arrowDown.setImageResource(R.drawable.ui_arrow_down);
            LayoutParams layoutParams = new LayoutParams(-2, this.arrowHeight);
            layoutParams.topMargin = ((this.menuHeight / 2) - (this.itemHeight / 2)) - KtcScreenParams.getInstence(this.mContext).getResolutionValue(78);
            layoutParams.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(37);
            this.arrowUp.setLayoutParams(layoutParams);
            layoutParams = new LayoutParams(-2, this.arrowHeight);
            layoutParams.topMargin = ((this.menuHeight / 2) - (this.itemHeight / 2)) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(99);
            layoutParams.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(37);
            this.arrowDown.setLayoutParams(layoutParams);
            this.arrowUp.setVisibility(View.INVISIBLE);
            this.arrowDown.setVisibility(View.INVISIBLE);
            addView(this.arrowUp);
            addView(this.arrowDown);
        }
        if (this.menuItems != null) {
            this.menuItems.clear();
            this.menuItems = new ArrayList();
        }
        if (this.mAdapter != null) {
            this.mAdapter = null;
        }
        this.dataCount = 0;
        this.pageCount = 5;
        this.mFocuseId = foucusIndex;
        this.mAdapter = skyEPGAdapter;
        if (this.mAdapter != null) {
            this.isCurrent = isCurrent;
            this.dataCount = this.mAdapter.getCount();
            if (this.dataCount > 0) {
                if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT) {
                    int intValue;
                    Iterable<SkyEPGData> adapterData = this.mAdapter.getAdapterData();
                    this.maxChannelNumberMap.clear();
                    for (CHANNEL_TYPE put : CHANNEL_TYPE.values()) {
                        this.maxChannelNumberMap.put(put, Integer.valueOf(-1));
                    }
                    if (adapterData != null) {
                        for (SkyEPGData skyEPGData : adapterData) {
                            if (skyEPGData.getChannelNumber() != null) {
                                intValue = Integer.valueOf(skyEPGData.getChannelNumber()).intValue();
                                if (((Integer) this.maxChannelNumberMap.get(skyEPGData.getChannelType())).intValue() < intValue) {
                                    this.maxChannelNumberMap.put(skyEPGData.getChannelType(), Integer.valueOf(intValue));
                                }
                            }
                        }
                        for (CHANNEL_TYPE put2 : CHANNEL_TYPE.values()) {
                            if (((Integer) this.maxChannelNumberMap.get(put2)).intValue() < 0) {
                                this.maxChannelNumberMap.put(put2, Integer.valueOf(this.dataCount));
                            }
                            ///TVUIDebug.debug("CHANNEL_TYPE:" + put2 + "  maxChannelNumber" + this.maxChannelNumberMap.get(put2));
                        }
                    }
                }
                if (this.dataCount >= 5) {
                    this.pageCount = 5;
                    this.isNomal = true;
                } else {
                    this.pageCount = this.dataCount;
                    this.isNomal = false;
                }
                Log.d("Maxs25","------SkySecondEPG:isNomal = " + isNomal + " /pageCount = " + pageCount);
                if (this.isNomal) {
                    this.midleIndex = 3;
                   /// TVUIDebug.debug("setEPGAdapter normal, focuseID:" + foucusIndex);
                    this.firstData = getLinkList(skyEPGAdapter, foucusIndex);
                    this.postItem = new SkyEPGItem_2(this.mContext);
                    this.postItem.setVisibility(View.INVISIBLE);
                    this.postItem.setFocusable(true);
                    this.postItem.setSelectState(false);
                    this.postItem.setLayoutParams(new LayoutParams(-2, this.itemHeight));
                    addView(this.postItem);
                } else {
                    this.midleIndex = foucusIndex + 1;
                   /// TVUIDebug.debug("setEPGAdapter notnormal,midleIndex:" + this.midleIndex + "  focuseID:" + foucusIndex);
                }
                this.midlePostion = (this.itemHeight + this.itemMargin) * 2;
                int i2 = 0;
                while (i2 < this.pageCount) {
                    SkyEPGItem_2 skyEPGItem_2 = new SkyEPGItem_2(this.mContext);
                    skyEPGItem_2.setTextAttribute(KtcScreenParams.getInstence(this.mContext).getTextDpiValue(36), UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
                    skyEPGItem_2.setFocusable(true);
                    if (!this.isNomal && this.midleIndex == 1 && i2 == 0) {
                        skyEPGItem_2.setFocusable(false);
                    }
                    skyEPGItem_2.setId(i2);
                    skyEPGItem_2.setSelectState(false);
                    skyEPGItem_2.setOnKeyListener(this.itemOnKeyListener);
                    skyEPGItem_2.setOnClickListener(this.itemClickListener);
                    if (this.isNomal) {
                        if (i2 == 0) {
                            skyEPGItem_2.updataItemValue(this.firstData);
                        }
                        if (i2 == 1) {
                            skyEPGItem_2.updataItemValue(this.firstData.next);
                        }
                        if (i2 == 2) {
                            this.midleData = this.firstData.next.next;
                            skyEPGItem_2.updataItemValue(this.firstData.next.next);
                        }
                        if (i2 == 3) {
                            skyEPGItem_2.updataItemValue(this.firstData.next.next.next);
                        }
                        if (i2 == 4) {
                            skyEPGItem_2.updataItemValue(this.firstData.next.next.next.next);
                        }
                    } else {
                        if (this.mAdapter.getCount() > this.midleIndex - 1) {
                            this.midleData = this.mAdapter.getEPGItemData(this.midleIndex - 1);
                        }
                        skyEPGItem_2.updataItemValue(this.mAdapter.getEPGItemData(i2));
                    }
                    LayoutParams layoutParams2 = new LayoutParams(-2, this.itemHeight);
                    if (i2 == this.midleIndex - 1) {
                        layoutParams2.topMargin = this.midlePostion;
                    } else if (i2 < this.midleIndex - 1) {
                        layoutParams2.topMargin = this.midlePostion - (((this.midleIndex - 1) - i2) * this.itemHeight);
                    } else if (i2 > this.midleIndex - 1) {
                        layoutParams2.topMargin = this.midlePostion + ((i2 - (this.midleIndex - 1)) * this.itemHeight);
                    }
                    if (layoutParams2 != null) {
                        skyEPGItem_2.setLayoutParams(layoutParams2);
                    }
                    this.menuItems.add(skyEPGItem_2);
                    addView(skyEPGItem_2);
                    i2++;
                }
                if (getVisibility() != View.VISIBLE) {
                    setVisibility(View.VISIBLE);
                }
                if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_DIS) {
                    SkyCommenEPG.recoveryHint.setVisibility(View.GONE);
                    SkyCommenEPG.sortButton.setVisibility(View.GONE);
                } else if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_DELETED) {
                    SkyCommenEPG.recoveryHint.setVisibility(View.VISIBLE);
                    SkyCommenEPG.sortButton.setVisibility(View.GONE);
                } else if (SkyCommenEPG.epgMode == EPGMODE.EPGMODE_SORT) {
                    SkyCommenEPG.recoveryHint.setVisibility(View.GONE);
                    SkyCommenEPG.sortButton.setVisibility(View.INVISIBLE);
                }
                ///TVUIDebug.debug("setEPGAdapter isCurrent:" + isCurrent + "   midleIndex:" + this.midleIndex);
                if (isCurrent) {
                    ((SkyEPGItem_2) this.menuItems.get(this.midleIndex - 1)).setSelectState(true);
                }
                SkyCommenEPG.isRefreshSencond = false;
            }
        }
    }

    public void setLastFocusItemID(int i) {
        this.lastFocusItemID = i;
    }

    public void setPreViewWidth(int i) {
        preViewWidth = i;
    }

    public void setSecondEPGOnItemOnkeyListener(OnSecondEPGItemOnkeyListener onSecondEPGItemOnkeyListener) {
        this.mListener = onSecondEPGItemOnkeyListener;
    }

    public void setSecondEPGfocus() {
        if (this.menuItems != null && this.midleIndex != 0 && this.menuItems.size() - 1 >= this.midleIndex - 1) {
            ///TVUIDebug.debug("setSecondEPGfocus  midleIndex:" + this.midleIndex);
            ((SkyEPGItem_2) this.menuItems.get(this.midleIndex - 1)).invalidate();
            ((SkyEPGItem_2) this.menuItems.get(this.midleIndex - 1)).setFocusable(true);
            ((SkyEPGItem_2) this.menuItems.get(this.midleIndex - 1)).post(new Runnable() {
                public void run() {
                    if (!((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(SkySecondEPG.this.midleIndex - 1)).hasFocus()) {
                        ///TVUIDebug.debug("menuItems.get(midleIndex - 1).requestFocus()");
                        ///TVUIDebug.debug("SkyEPGItem_2 isInSortModeing:" + SkySecondEPG.isInSortModeing);
                        SkySecondEPG.isInSortModeing = false;
                        ((SkyEPGItem_2) SkySecondEPG.this.menuItems.get(SkySecondEPG.this.midleIndex - 1)).requestFocus();
                    }
                }
            });
        }
    }

    public void setTipsViewTips(String str) {
        removeAllViews();
        this.tipsTextView = new TextView(this.mContext);
        this.tipsTextView.setTextColor(-1);
        this.tipsTextView.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(30));
        this.tipsTextView.setText(str);
        addView(this.tipsTextView, new LayoutParams(-2, -2, 17));
    }

    public void show_dismissAnimation(SkyFirstEPG skyFirstEPG, final boolean z, final int i) {
        post(new Runnable() {
            public void run() {
                ///TVUIDebug.debug("midleIndex = 0");
                List subList = SkySecondEPG.this.menuItems.subList(i + -2 >= 0 ? i - 2 : 0, i + 2 <= SkySecondEPG.this.menuItems.size() + -1 ? i + 3 : SkySecondEPG.this.menuItems.size());
                int width = SkySecondEPG.this.getWidth();
                if (subList != null) {
                    int j = i;
                    int size = subList.size();
                    if (size == 5) {
                      // / TVUIDebug.debug("444 midleIndex = 2");
                        j = 2;
                    } else if (size < 5) {
                       // TVUIDebug.debug("55 midleIndex index:" + j);
                        j = j < 2 ? j : 2;
                    } else {
                        j = 0;
                    }
                    for (int i2 = 0; i2 < size; i2++) {
                        TranslateAnimation translateAnimation;
                        Animation alphaAnimation;
                        ((SkyEPGItem_2) subList.get(i2)).clearAnimation();
                        AnimationSet animationSet = new AnimationSet(false);
                        if (z) {
                            animationSet.setFillAfter(false);
                            translateAnimation = new TranslateAnimation((float) (-width), 0.0f, 0.0f, 0.0f);
                            alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                            translateAnimation.setInterpolator(new DecelerateInterpolator());
                            alphaAnimation.setInterpolator(new DecelerateInterpolator());
                        } else {
                            animationSet.setFillAfter(true);
                            translateAnimation = new TranslateAnimation(0.0f, (float) (-width), 0.0f, 0.0f);
                            alphaAnimation = new AlphaAnimation(0.6f, 0.0f);
                            translateAnimation.setInterpolator(new AccelerateInterpolator());
                            alphaAnimation.setInterpolator(new AccelerateInterpolator());
                        }
                        translateAnimation.setStartOffset((long) ((i2 * 200) / size));
                        translateAnimation.setDuration(200);
                        animationSet.addAnimation(translateAnimation);
                        alphaAnimation.setStartOffset((long) ((i2 * 200) / size));
                        alphaAnimation.setDuration(200);
                        animationSet.addAnimation(alphaAnimation);
                        ((SkyEPGItem_2) subList.get(i2)).setVisibility(View.VISIBLE);
                        ((SkyEPGItem_2) subList.get(i2)).startAnimation(animationSet);
                        if (i2 == j) {
                            final int i3 = j;
                            ///final boolean z = z;
                            final boolean z = false;
                            animationSet.setAnimationListener(new AnimationListener() {
                                public void onAnimationEnd(Animation animation) {
                                    if (i3 == SkySecondEPG.this.dataCount - 1 && !z) {
                                        SkySecondEPG.this.isDismisAnimStart = false;
                                    }
                                }

                                public void onAnimationRepeat(Animation animation) {
                                }

                                public void onAnimationStart(Animation animation) {
                                }
                            });
                        }
                        if (i2 == size - 1 && j != SkySecondEPG.this.dataCount - 1) {
                            final boolean z2 = z;
                            animationSet.setAnimationListener(new AnimationListener() {
                                public void onAnimationEnd(Animation animation) {
                                    if (!z2) {
                                        SkySecondEPG.this.isDismisAnimStart = false;
                                    }
                                }

                                public void onAnimationRepeat(Animation animation) {
                                }

                                public void onAnimationStart(Animation animation) {
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void upDataEPGAdapter(SkyEPGAdapter skyEPGAdapter, int i, boolean z, boolean z2) {
        if (this.mAdapter != null) {
            this.mAdapter = null;
        }
        this.dataCount = 0;
        this.pageCount = 5;
        this.mAdapter = skyEPGAdapter;
        if (this.mAdapter != null) {
            this.dataCount = this.mAdapter.getCount();
            if (this.dataCount > 0) {
                if (this.dataCount >= 5) {
                    this.pageCount = 5;
                    this.isNomal = true;
                } else {
                    this.pageCount = this.dataCount;
                    this.isNomal = false;
                }
                if (this.isNomal) {
                    ///TVUIDebug.debug("upDataEPGAdapter  normal,mFocuseId:" + this.mFocuseId);
                    this.firstData = getLinkList(skyEPGAdapter, this.mFocuseId);
                } else {
                   /// TVUIDebug.debug("midleIndex = mFocuseId,mFocuseId:" + this.mFocuseId);
                }
                int i2 = 0;
                while (i2 < this.pageCount && i2 < this.menuItems.size()) {
                    final SkyEPGItem_2 skyEPGItem_2 = (SkyEPGItem_2) this.menuItems.get(i2);
                    skyEPGItem_2.setSelectState(false);
                    if (this.isNomal) {
                        if (i2 == 0) {
                            skyEPGItem_2.updataItemValue(this.firstData);
                        }
                        if (i2 == 1) {
                            skyEPGItem_2.updataItemValue(this.firstData.next);
                        }
                        if (i2 == 2) {
                            skyEPGItem_2.updataItemValue(this.firstData.next.next);
                        }
                        if (i2 == 3) {
                            skyEPGItem_2.updataItemValue(this.firstData.next.next.next);
                        }
                        if (i2 == 4) {
                            skyEPGItem_2.updataItemValue(this.firstData.next.next.next.next);
                        }
                    } else {
                        skyEPGItem_2.updataItemValue(this.mAdapter.getEPGItemData(i2));
                    }
                    if (this.midleData != null && this.midleData.equals(skyEPGItem_2.getItemData())) {
                       /// TVUIDebug.debug("upDataEPGAdapter isCurrent:" + z);
                        if (z) {
                            skyEPGItem_2.setSelectState(true);
                        }
                    }
                    LayoutParams layoutParams = new LayoutParams(-2, this.itemHeight);
                    layoutParams.topMargin = ((LayoutParams) skyEPGItem_2.getLayoutParams()).topMargin;
                    skyEPGItem_2.setLayoutParams(layoutParams);
                    skyEPGItem_2.postInvalidate();
                    if (this.midleData != null && this.midleData.equals(skyEPGItem_2.getItemData()) && skyEPGItem_2.isFocused()) {
                        skyEPGItem_2.postDelayed(new Runnable() {
                            public void run() {
                                if (skyEPGItem_2.isFocused()) {
                                    skyEPGItem_2.focusViewChangePos();
                                }
                            }
                        }, 70);
                    }
                    i2++;
                }
                postInvalidate();
                SkyCommenEPG.isRefreshSencond = false;
            }
        }
    }

    public void upDataEPGAdapter(boolean z) {
        this.isCurrent = z;
    }
}
