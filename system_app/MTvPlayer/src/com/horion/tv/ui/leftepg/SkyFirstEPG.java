package com.horion.tv.ui.leftepg;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.ktc.framework.skycommondefine.SkyworthBroadcastKey;
import com.ktc.util.KtcScreenParams;
import com.horion.tv.ui.leftepg.SkyEPGItem.OnItemFocusChangedListener;
import com.horion.tv.ui.util.UIConstant;

import java.util.ArrayList;
import java.util.List;
///import org.apache.http4.HttpStatus;

public class SkyFirstEPG extends FrameLayout {
    private int dataCount = 0;
    OnItemFocusChangedListener focusListener = new OnItemFocusChangedListener() {
        public void itemFocusListener(SkyEPGItem skyEPGItem, boolean z) {
            Log.d("Maxs24","SkyFirstEPG:itemFocusListener:.mListener != null = " + (mListener != null));
            if (SkyFirstEPG.this.mListener != null) {
                SkyFirstEPG.this.mListener.onItemFocusChangeListener(skyEPGItem.getId(), skyEPGItem, skyEPGItem.getItemData(), z);
            }
        }
    };
    private boolean isAnimStart = false;
    OnClickListener itemClickListener = new OnClickListener() {
        public void onClick(View view) {
            if (!SkyFirstEPG.this.isAnimStart) {
                ((SkyEPGItem) view).setSelectState(true);
                if (SkyFirstEPG.this.mListener != null) {
                    SkyFirstEPG.this.mListener.onItemOnClick(view.getId(), view, ((SkyEPGItem) view).getItemData());
                }
            }
        }
    };
    private int itemHeight = 100;
    private int itemMargin = 0;
    OnKeyListener itemOnKeyListener = new OnKeyListener() {
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() != 0) {
                return false;
            }
            int i2;
            for (i2 = 0; i2 < SkyFirstEPG.this.dataCount; i2++) {
                ((SkyEPGItem) SkyFirstEPG.this.menuItems.get(i2)).setSelectState(false);
            }
            SkyFirstEPG.this.lastFocusItemID = -1;
            i2 = view.getId();
            switch (i) {
                case 4:
                    return SkyFirstEPG.this.isAnimStart ? true : SkyFirstEPG.this.mListener != null ? SkyFirstEPG.this.mListener.onItemOnKeyBack(i2, view) : false;
                case 19:
                case SkyworthBroadcastKey.SKY_KEY_SHORT_SHUTTLE_LEFT /*764*/:
                    if (System.currentTimeMillis() - SkyFirstEPG.this.onKeyTime < 150) {
                        SkyFirstEPG.this.startOffset = 100;
                    } else {
                        SkyFirstEPG.this.startOffset = 200;
                    }
                    SkyFirstEPG.this.onKeyTime = System.currentTimeMillis();
                    if (SkyFirstEPG.this.isAnimStart) {
                        return true;
                    }
                    if (i2 == 0) {
                        return true;
                    }
                    SkyFirstEPG.this.onKeyItemMoveEvent(i, view);
                    if (SkyFirstEPG.this.mListener != null) {
                        SkyFirstEPG.this.mListener.onItemOnKeyUp(i2, (View) SkyFirstEPG.this.menuItems.get(i2 - 1));
                    }
                    return true;
                case 20:
                case SkyworthBroadcastKey.SKY_KEY_SHORT_SHUTTLE_RIGHT /*765*/:
                    if (System.currentTimeMillis() - SkyFirstEPG.this.onKeyTime < 150) {
                        SkyFirstEPG.this.startOffset = 100;
                    } else {
                        SkyFirstEPG.this.startOffset = 200;
                    }
                    SkyFirstEPG.this.onKeyTime = System.currentTimeMillis();
                    if (SkyFirstEPG.this.isAnimStart) {
                        return true;
                    }
                    if (i2 == SkyFirstEPG.this.dataCount - 1) {
                        return true;
                    }
                    SkyFirstEPG.this.onKeyItemMoveEvent(i, view);
                    if (SkyFirstEPG.this.mListener != null) {
                        SkyFirstEPG.this.mListener.onItemOnKeyDown(i2, (View) SkyFirstEPG.this.menuItems.get(i2 + 1));
                    }
                    return true;
                case 21:
                    return SkyFirstEPG.this.isAnimStart ? true : SkyFirstEPG.this.mListener != null ? SkyFirstEPG.this.mListener.onItemOnKeyLeft(i2, view) : true;
                case 22:
                    ///TVUIDebug.debug("onKey  KEYCODE_DPAD_RIGHT,isAnimStart:" + SkyFirstEPG.this.isAnimStart);
                    return SkyFirstEPG.this.isAnimStart ? true : SkyFirstEPG.this.mListener != null ? SkyFirstEPG.this.mListener.onItemOnKeyRight(i2, view, ((SkyEPGItem) view).getItemData()) : true;
                default:
                    return false;
            }
        }
    };
    private int lastFocusItemID = -1;
    private SkyEPGAdapter mAdapter;
    private Context mContext;
    private OnItemOnkeyListener mListener;
    private int menuHeight = 700;
    private List<SkyEPGItem> menuItems;
    private long onKeyTime = 0;
    private int startOffset = 200;

    public interface OnItemOnkeyListener {
        void onItemFocusChangeListener(int i, View view, SkyEPGData skyEPGData, boolean z);

        boolean onItemOnClick(int i, View view, SkyEPGData skyEPGData);

        boolean onItemOnKeyBack(int i, View view);

        boolean onItemOnKeyDown(int i, View view);

        boolean onItemOnKeyLeft(int i, View view);

        boolean onItemOnKeyRight(int i, View view, SkyEPGData skyEPGData);

        boolean onItemOnKeyUp(int i, View view);
    }

    public SkyFirstEPG(Context context) {
        super(context);
        this.mContext = context;
        this.menuHeight = KtcScreenParams.getInstence(this.mContext).getResolutionValue(700);
        this.itemHeight = KtcScreenParams.getInstence(this.mContext).getResolutionValue(100);
        this.itemMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(0);
        initView();
    }

    private void initView() {
        this.menuItems = new ArrayList();
        setLayoutParams(new LayoutParams(-2, this.menuHeight));
    }

    private void onKeyItemMoveEvent(int i, View view) {
        this.isAnimStart = true;
        final int id = view.getId();
        int i2 = this.itemHeight + this.itemMargin;
        final int count = this.mAdapter.getCount();
        int i3;
        SkyEPGItem skyEPGItem;
        LayoutParams layoutParams;
        AnimationSet animationSet;
        TranslateAnimation translateAnimation;
        Animation alphaAnimation;
        if (i == 19 || i == SkyworthBroadcastKey.SKY_KEY_SHORT_SHUTTLE_LEFT) {
            ((SkyEPGItem) this.menuItems.get(id - 1)).requestFocus();
            for (i3 = count - 1; i3 >= 0; i3--) {
                skyEPGItem = (SkyEPGItem) this.menuItems.get(i3);
                layoutParams = (LayoutParams) skyEPGItem.getLayoutParams();
                layoutParams.topMargin += i2;
                skyEPGItem.setLayoutParams(layoutParams);
                skyEPGItem.clearAnimation();
                animationSet = new AnimationSet(false);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-i2), 0.0f);
                translateAnimation.setDuration(110);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation);
                if (skyEPGItem.getVisibility() != View.VISIBLE) {
                    skyEPGItem.setVisibility(View.VISIBLE);
                }
                if (i3 == id - 4) {
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(110);
                    animationSet.addAnimation(alphaAnimation);
                }
                if (i3 == id + 3) {
                    alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(110);
                    animationSet.addAnimation(alphaAnimation);
                }
                skyEPGItem.startAnimation(animationSet);
                if (i3 == 0) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (id + 3 < count && SkyFirstEPG.this.menuItems.get(id + 3) != null) {
                                ((SkyEPGItem) SkyFirstEPG.this.menuItems.get(id + 3)).setVisibility(View.INVISIBLE);
                            }
                            SkyFirstEPG.this.isAnimStart = false;
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                } else if (i3 == id - 1) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                }
            }
        } else if (i == 20 || i == SkyworthBroadcastKey.SKY_KEY_SHORT_SHUTTLE_RIGHT) {
            ((SkyEPGItem) this.menuItems.get(id + 1)).requestFocus();
            for (i3 = 0; i3 < count; i3++) {
                skyEPGItem = (SkyEPGItem) this.menuItems.get(i3);
                layoutParams = (LayoutParams) skyEPGItem.getLayoutParams();
                layoutParams.topMargin -= i2;
                skyEPGItem.setLayoutParams(layoutParams);
                skyEPGItem.clearAnimation();
                animationSet = new AnimationSet(false);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) i2, 0.0f);
                translateAnimation.setDuration(110);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation);
                if (skyEPGItem.getVisibility() != View.VISIBLE) {
                    skyEPGItem.setVisibility(View.VISIBLE);
                }
                if (i3 == id - 3) {
                    alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(110);
                    animationSet.addAnimation(alphaAnimation);
                }
                if (i3 == id + 4) {
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(110);
                    animationSet.addAnimation(alphaAnimation);
                }
                skyEPGItem.startAnimation(animationSet);
                if (i3 == count - 1) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (id - 3 >= 0 && SkyFirstEPG.this.menuItems.get(id - 3) != null) {
                                ((SkyEPGItem) SkyFirstEPG.this.menuItems.get(id - 3)).setVisibility(View.INVISIBLE);
                            }
                            SkyFirstEPG.this.isAnimStart = false;
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                } else if (i3 == id + 1) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
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

    private void setEPGAdapter(SkyEPGAdapter skyEPGAdapter) {
        removeAllViews();
        if (this.menuItems != null) {
            this.menuItems.clear();
        }
        if (this.mAdapter != null) {
            this.mAdapter = null;
        }
        this.dataCount = 0;
        this.mAdapter = skyEPGAdapter;
        if (this.mAdapter != null) {
            this.dataCount = this.mAdapter.getCount();
            Log.d("Maxs24","ShowFirstEPG:setEPGAdapter:dataCount = " + dataCount);
            if (this.dataCount > 0) {
                for (int i = 0; i < this.dataCount; i++) {
                    SkyEPGItem skyEPGItem = new SkyEPGItem(this.mContext);
                    skyEPGItem.setTextAttribute(KtcScreenParams.getInstence(this.mContext).getTextDpiValue(36), UIConstant.TEXTCOLOR_INDICATE_IMPORTANCE);
                    skyEPGItem.setSelectState(false);
                    skyEPGItem.setId(i);
                    skyEPGItem.setOnKeyListener(this.itemOnKeyListener);
                    skyEPGItem.setOnClickListener(this.itemClickListener);
                    LayoutParams layoutParams = new LayoutParams(-2, this.itemHeight);
                    layoutParams.topMargin = (i + 3) * (this.itemHeight + this.itemMargin);
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    if (layoutParams != null) {
                        skyEPGItem.setLayoutParams(layoutParams);
                    }
                    skyEPGItem.setVisibility(View.INVISIBLE);
                    skyEPGItem.updataItemValue(this.mAdapter.getEPGItemData(i));
                    this.menuItems.add(skyEPGItem);
                    addView(skyEPGItem);
                    skyEPGItem.setOnItemFocusChangedListener(this.focusListener);
                }
                showAnimation();
            }
        }
    }

    private void showAnimation() {
        post(new Runnable() {
            public void run() {
                Animation translateAnimation;
                int positon = SkyFirstEPG.this.dataCount < 7 ? SkyFirstEPG.this.dataCount : 7;
                for (int i = 0; i < positon; i++) {
                    ((SkyEPGItem) SkyFirstEPG.this.menuItems.get(i)).clearAnimation();
                    AnimationSet animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation((float) (-SkyFirstEPG.this.getWidth()), 0.0f, 0.0f, 0.0f);
                    translateAnimation.setStartOffset((long) ((i * 300) / 7));
                    translateAnimation.setDuration(200);
                    translateAnimation.setInterpolator(new OvershootInterpolator(1.0f));
                    animationSet.addAnimation(translateAnimation);
                    translateAnimation = new AlphaAnimation(0.0f, 1.0f);
                    translateAnimation.setStartOffset((long) ((i * 300) / 7));
                    translateAnimation.setDuration(200);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                    ((SkyEPGItem) SkyFirstEPG.this.menuItems.get(i)).setVisibility(View.VISIBLE);
                    ((SkyEPGItem) SkyFirstEPG.this.menuItems.get(i)).startAnimation(animationSet);
                }
                Log.d("Maxs24","SkyFirstEPG:showAnimation:menuItems == null = " + (menuItems == null));
                Log.d("Maxs24","SkyFirstEPG:showAnimation:menuItems.size() = " + menuItems.size());
                if (SkyFirstEPG.this.menuItems != null && SkyFirstEPG.this.menuItems.size() > 0) {
                    SkyFirstEPG.this.lastFocusItemID = 0;
                    ((SkyEPGItem) SkyFirstEPG.this.menuItems.get(0)).requestFocus();
                }
                if (SkyCommenEPG.focusView.getVisibility() != View.VISIBLE) {
                    SkyCommenEPG.focusView.setVisibility(View.VISIBLE);
                }
                translateAnimation = new AlphaAnimation(0.0f, 1.0f);
                translateAnimation.setDuration(200);
                SkyCommenEPG.focusView.startAnimation(translateAnimation);
            }
        });
    }

    public void dismissAnimtion(final SkyCommenEPG skyCommenEPG, int i) {
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(200);
        SkyCommenEPG.focusView.startAnimation(alphaAnimation);
        List subList = this.menuItems.subList(i + -3 >= 0 ? i - 3 : 0, i + 4 <= this.menuItems.size() + -1 ? i + 5 : this.menuItems.size());
        if (subList != null) {
            int size = subList.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((SkyEPGItem) subList.get(i2)).clearAnimation();
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.setFillAfter(true);
                alphaAnimation = new TranslateAnimation(0.0f, (float) (-getWidth()), 0.0f, 0.0f);
                alphaAnimation.setStartOffset((long) ((i2 * 200) / size));
                alphaAnimation.setDuration(200);
                alphaAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(alphaAnimation);
                alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setStartOffset((long) ((i2 * 200) / size));
                alphaAnimation.setDuration(100);
                alphaAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(alphaAnimation);
                ((SkyEPGItem) subList.get(i2)).startAnimation(animationSet);
                if (i2 == size - 1) {
                    animationSet.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            skyCommenEPG.setVisibility(View.GONE);
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

    public List<SkyEPGItem> getEpgItems() {
        return this.menuItems;
    }

    public int getLastFocusID() {
        return this.lastFocusItemID;
    }

    public void setEPGItemFocus(int i) {
        if (i == -1) {
            setEPGAdapter(this.mAdapter);
        } else if (this.menuItems != null && i < this.menuItems.size() && this.menuItems.get(i) != null) {
            ((SkyEPGItem) this.menuItems.get(i)).requestFocus();
        }
    }

    public void setItemStateShowSecond(boolean z) {
        int i = 0;
        int i2 = this.lastFocusItemID;
        List subList = this.menuItems.subList(i2 + -3 >= 0 ? i2 - 3 : 0, i2 + 4 <= this.menuItems.size() + -1 ? i2 + 5 : this.menuItems.size());
        while (i < subList.size()) {
            ((SkyEPGItem) subList.get(i)).setItemStateForSecond(z);
            i++;
        }
    }

    public void setLastFocusItemID(int i) {
        this.lastFocusItemID = i;
    }

    public void setOnItemOnkeyListener(OnItemOnkeyListener onItemOnkeyListener) {
        this.mListener = onItemOnkeyListener;
    }

    public void showFitstEPG(SkyEPGAdapter skyEPGAdapter) {
        Log.d("Maxs24","showFirstEPG:skyEPGAdapter == null) =  " + (skyEPGAdapter == null));
        setEPGAdapter(skyEPGAdapter);
    }
}
