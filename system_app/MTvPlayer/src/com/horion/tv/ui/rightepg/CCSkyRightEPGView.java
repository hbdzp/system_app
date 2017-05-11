package com.horion.tv.ui.rightepg;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.horion.tv.R;
///import com.ktc.framework.skysdk.logger.SkyLogger;
import com.ktc.util.KtcScreenParams;

import java.util.List;

public class CCSkyRightEPGView extends FrameLayout {
    private static final int ONKEY_DOWN = 18;
    private static final int ONKEY_UP = 17;
    private LinearLayout channelLayout;
    private LayoutParams channelLp;
    private TextView channelName;
    private TextView channelNum;
    private int curChannelIndex = 0;
    private int curItemIndex = 0;
    private int dataCount = 0;
    private boolean isAnimStart = false;
    OnItemSelectedListener itemOnItemSelectedListener = new OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            //SkyLogger.d("lwr", "onItemSelected:" + CCSkyRightEPGView.this.curItemIndex + "position:" + i + "" + view.toString());
            if (SkyEventUtils.getInstance().getFristFlag()) {
                CCSkyRightEPGView.this.mFirstListItemPostion = CCSkyRightEPGView.this.listView.getFirstVisiblePosition();
               /// SkyLogger.d("lwr", "onItemSelected>>firstListItemPosition:" + CCSkyRightEPGView.this.mFirstListItemPostion);
                CCSkyRightEPGView.this.listView.requestFocus();
                CCSkyRightEPGView.this.listView.requestFocusFromTouch();
                CCSkyRightEPGView.this.listView.setSelectionFromTop(CCSkyRightEPGView.this.curChannelIndex, 0);
                return;
            }
            CCSkyRightEPGView.this.listView.requestFocus();
            CCSkyRightEPGView.this.listView.requestFocusFromTouch();
            CCSkyRightEPGView.this.listView.setSelectionFromTop(CCSkyRightEPGView.this.curItemIndex, 0);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            //SkyLogger.d("lwr", "onNothingSelected:");
        }
    };
    OnKeyListener itemOnKeyListener = new OnKeyListener() {
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            ///SkyLogger.d("lwr", "OnKeyListener:" + i);
            if (keyEvent.getAction() == 0) {
                if (CCSkyRightEPGView.this.isAnimStart) {
                    return true;
                }
                CCSkyRightEPGView cCSkyRightEPGView;
                switch (i) {
                    case 19:
                      ///  SkyLogger.d("lwr", "KEYCODE_DPAD_UP:");
                        SkyEventUtils.getInstance().setFristFlag(false);
                        CCSkyRightEPGView.this.onKeyActionDeriction = 17;
                        cCSkyRightEPGView = CCSkyRightEPGView.this;
                        cCSkyRightEPGView.curItemIndex = cCSkyRightEPGView.curItemIndex - 1;
                        if (System.currentTimeMillis() - CCSkyRightEPGView.this.onKeyTime < 150) {
                            CCSkyRightEPGView.this.up_down_duration = 10;
                        } else {
                            CCSkyRightEPGView.this.up_down_duration = 100;
                        }
                        CCSkyRightEPGView.this.onKeyTime = System.currentTimeMillis();
                        if (CCSkyRightEPGView.this.curItemIndex >= 0) {
                            CCSkyRightEPGView.this.mFirstListItemPostion = CCSkyRightEPGView.this.listView.getFirstVisiblePosition();
                            CCSkyRightEPGView.this.mLastlistItemPostion = (CCSkyRightEPGView.this.mFirstListItemPostion + CCSkyRightEPGView.this.listView.getChildCount()) - 1;
                           /// SkyLogger.d("lwr", "<<<<firstListItemPosition:" + CCSkyRightEPGView.this.mFirstListItemPostion + "lastListItemPosition:" + CCSkyRightEPGView.this.mLastlistItemPostion);
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mFirstListItemPostion) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion, CCSkyRightEPGView.this.listView)).updataItemImage(18);
                            }
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mFirstListItemPostion + 1) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion + 1, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion + 1, CCSkyRightEPGView.this.listView)).updataItemImage(19);
                            }
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mFirstListItemPostion + 2) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion + 2, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion + 2, CCSkyRightEPGView.this.listView)).updataItemImage(20);
                            }
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mFirstListItemPostion + 3) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion + 3, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion + 3, CCSkyRightEPGView.this.listView)).updataItemImage(21);
                            }
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mFirstListItemPostion + 4) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion + 4, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mFirstListItemPostion + 4, CCSkyRightEPGView.this.listView)).updataItemImage(22);
                            }
                            CCSkyRightEPGView.this.onKeyItemMoveEvent(i, view);
                            return false;
                        }
                        CCSkyRightEPGView.this.curItemIndex = 0;
                        return true;
                    case 20:
                        //SkyLogger.d("lwr", "KEYCODE_DPAD_DOWN");
                        SkyEventUtils.getInstance().setFristFlag(false);
                        CCSkyRightEPGView.this.onKeyActionDeriction = 18;
                        cCSkyRightEPGView = CCSkyRightEPGView.this;
                        cCSkyRightEPGView.curItemIndex = cCSkyRightEPGView.curItemIndex + 1;
                        if (System.currentTimeMillis() - CCSkyRightEPGView.this.onKeyTime < 150) {
                            CCSkyRightEPGView.this.up_down_duration = 10;
                        } else {
                            CCSkyRightEPGView.this.up_down_duration = 100;
                        }
                        CCSkyRightEPGView.this.onKeyTime = System.currentTimeMillis();
                        if (CCSkyRightEPGView.this.curItemIndex <= CCSkyRightEPGView.this.dataCount - 5) {
                            CCSkyRightEPGView.this.mFirstListItemPostion = CCSkyRightEPGView.this.listView.getFirstVisiblePosition();
                            CCSkyRightEPGView.this.mLastlistItemPostion = (CCSkyRightEPGView.this.mFirstListItemPostion + CCSkyRightEPGView.this.listView.getChildCount()) - 1;
                            ///SkyLogger.d("lwr", ">>>>firstListItemPosition:" + CCSkyRightEPGView.this.mFirstListItemPostion + "lastListItemPosition:" + CCSkyRightEPGView.this.mLastlistItemPostion);
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mLastlistItemPostion - 4) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion - 4, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion - 4, CCSkyRightEPGView.this.listView)).updataItemImage(18);
                            }
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mLastlistItemPostion - 3) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion - 3, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion - 3, CCSkyRightEPGView.this.listView)).updataItemImage(19);
                            }
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mLastlistItemPostion - 2) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion - 2, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion - 2, CCSkyRightEPGView.this.listView)).updataItemImage(20);
                            }
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mLastlistItemPostion - 1) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion - 1, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion - 1, CCSkyRightEPGView.this.listView)).updataItemImage(21);
                            }
                            if (CCSkyRightEPGView.this.curChannelIndex == CCSkyRightEPGView.this.mLastlistItemPostion) {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion, CCSkyRightEPGView.this.listView)).updataItemImage(17);
                            } else {
                                ((SkyEventItem) CCSkyRightEPGView.this.getViewByPosition(CCSkyRightEPGView.this.mLastlistItemPostion, CCSkyRightEPGView.this.listView)).updataItemImage(22);
                            }
                            CCSkyRightEPGView.this.onKeyItemMoveEvent(i, view);
                            return false;
                        }
                        CCSkyRightEPGView.this.curItemIndex = CCSkyRightEPGView.this.dataCount - 5;
                        return true;
                }
            }
            return false;
        }
    };
    private RelativeLayout.LayoutParams listLp;
    private ListView listView;
    private Context mContext;
    private int mFirstListItemPostion = 0;
    private int mLastlistItemPostion = 0;
    private LayoutParams nameLp;
    private LayoutParams numLp;
    private int onKeyActionDeriction = 0;
    private long onKeyTime = 0;
    private RelativeLayout rootLayout;
    private RelativeLayout.LayoutParams rootLp;
    private SkyEventListAdapter skyEventListAdapter;
    private int up_down_duration = 100;

    public CCSkyRightEPGView(Context context) {
        super(context);
       /// SkyLogger.d("lwr", ">>>SkyRightEPGView<<<");
        this.mContext = context;
        setBackgroundColor(0);
        setBackgroundResource(R.drawable.rightepg_bg);
        setLayoutParams(new LayoutParams(-1, -1));
        initView();
    }

    private void initView() {
        this.rootLayout = new RelativeLayout(this.mContext);
        this.rootLp = new RelativeLayout.LayoutParams(-1, -2);
        this.channelLayout = new LinearLayout(this.mContext);
        this.channelLayout.setId(R.id.sky_rightepg_channellayout);
        this.channelLayout.setOrientation(LinearLayout.VERTICAL);
        this.channelLp = new LayoutParams(-1, -2);
        this.channelLp.gravity = Gravity.RIGHT;
        this.channelNum = new TextView(this.mContext);
        this.channelNum.setId(R.id.sky_rightepg_channelnum);
        this.channelNum.setSingleLine(true);
        this.channelNum.setTextColor(Color.RED);
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
        this.channelName.setTextColor(Color.RED);
        this.channelName.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(45));
        this.channelName.setGravity(5);
        this.nameLp = new LayoutParams(-2, -2);
        this.nameLp.rightMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(80);
        this.nameLp.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(3);
        this.nameLp.gravity = Gravity.RIGHT;
        this.channelLayout.addView(this.channelName, this.nameLp);
        this.rootLayout.addView(this.channelLayout, this.channelLp);
        this.listView = new ListView(this.mContext);
        this.listView.setId(R.id.ccskyrightepgview_listview);
        this.listView.setDividerHeight(-1);
        this.listView.setSelector(R.drawable.selectedbg);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setPadding(0, KtcScreenParams.getInstence(this.mContext).getResolutionValue(10), 0, 0);
        this.listLp = new RelativeLayout.LayoutParams(-2, KtcScreenParams.getInstence(this.mContext).getResolutionValue(556));
        this.listLp.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(220);
        this.listLp.rightMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(0);
        this.listLp.addRule(3, 4);
        this.listLp.addRule(7, 4);
        this.rootLayout.addView(this.listView, this.listLp);
        this.listView.setOnItemSelectedListener(this.itemOnItemSelectedListener);
        this.listView.setOnKeyListener(this.itemOnKeyListener);
        addView(this.rootLayout, this.rootLp);
    }

    private void onKeyItemMoveEvent(int i, View view) {
        TranslateAnimation translateAnimation = null;
        AlphaAnimation alphaAnimation = null;
        this.isAnimStart = true;
       // SkyLogger.d("lwr", ">>>>>onKeyItemMoveEvent<<<<<<：" + this.curItemIndex + "|| " + this.mFirstListItemPostion + "lastListItemPosition:" + this.mLastlistItemPostion);
        int resolutionValue = KtcScreenParams.getInstence(this.mContext).getResolutionValue(110);
        int i2;
        Animation animation2;
        int i3;
        AnimationSet animationSet;
        if (i == 19) {
            i2 = 0;
            animation2 = null;
            i3 = this.mFirstListItemPostion;
            while (i3 < this.mFirstListItemPostion + 5) {
                try {
                    ((SkyEventItem) getViewByPosition(this.mFirstListItemPostion + i2, this.listView)).clearAnimation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //SkyLogger.d("lwr", ">>>up index:" + i2);
                if (i2 == 0) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-resolutionValue), 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                } else {
                    ///animationSet = animation;
                    ///animation = animation2;
                }
                if (i2 == 1) {
                    animationSet = new AnimationSet(false);
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration((long) this.up_down_duration);
                    animationSet.addAnimation(alphaAnimation);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-resolutionValue), 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                }
                if (i2 == 2) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-resolutionValue), 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                }
                if (i2 == 3) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-resolutionValue), 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                }
                if (i2 == 4) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-resolutionValue), 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                    animation2 = translateAnimation;
                    ////ranslateAnimation = animationSet;
                } else {
                    animation2 = translateAnimation;
                    ////translateAnimation = animationSet;
                }
                if (((SkyEventItem) getViewByPosition(this.mFirstListItemPostion + i2, this.listView)).getVisibility() != View.VISIBLE) {
                    ((SkyEventItem) getViewByPosition(this.mFirstListItemPostion + i2, this.listView)).setVisibility(View.VISIBLE);
                }
                ((SkyEventItem) getViewByPosition(this.mFirstListItemPostion + i2, this.listView)).startAnimation(translateAnimation);
                animation2.setAnimationListener(new AnimationListener() {
                    public void onAnimationEnd(Animation animation) {
                        CCSkyRightEPGView.this.isAnimStart = false;
                        if (CCSkyRightEPGView.this.onKeyActionDeriction != 17) {
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }
                });
                i3++;
                i2++;
            }
        } else if (i == 20) {
            i2 = 0;
            animation2 = null;
            i3 = this.mLastlistItemPostion - 5;
            while (i3 <= this.mLastlistItemPostion) {
                try {
                    ((SkyEventItem) getViewByPosition((this.mLastlistItemPostion - 5) + i2, this.listView)).clearAnimation();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (i2 == 0) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) resolutionValue, 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                } else {
                    ////animationSet = translateAnimation;
                    ///translateAnimation = animation2;
                }
                if (i2 == 1) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) resolutionValue, 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                }
                if (i2 == 2) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) resolutionValue, 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                }
                if (i2 == 3) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) resolutionValue, 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                }
                if (i2 == 4) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) resolutionValue, 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                }
                if (i2 == 5) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) resolutionValue, 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                    animation2 = new AlphaAnimation(0.0f, 1.0f);
                    animation2.setFillAfter(true);
                    animation2.setDuration((long) this.up_down_duration);
                    animationSet.addAnimation(animation2);
                }
                if (i2 == 6) {
                    animationSet = new AnimationSet(false);
                    translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) resolutionValue, 0.0f);
                    translateAnimation.setDuration((long) this.up_down_duration);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(translateAnimation);
                    animation2 = new AlphaAnimation(0.0f, 1.0f);
                    animation2.setFillAfter(true);
                    animation2.setDuration((long) this.up_down_duration);
                    animationSet.addAnimation(animation2);
                    animation2 = translateAnimation;
                    ///translateAnimation = animationSet;
                } else {
                    animation2 = translateAnimation;
                    ///translateAnimation = animationSet;
                }
                if (((SkyEventItem) getViewByPosition((this.mLastlistItemPostion - 5) + i2, this.listView)).getVisibility() != View.VISIBLE) {
                    ((SkyEventItem) getViewByPosition((this.mLastlistItemPostion - 5) + i2, this.listView)).setVisibility(View.VISIBLE);
                }
                ((SkyEventItem) getViewByPosition((this.mLastlistItemPostion - 5) + i2, this.listView)).startAnimation(translateAnimation);
                animation2.setAnimationListener(new AnimationListener() {
                    public void onAnimationEnd(Animation animation) {
                        ///SkyLogger.d("lwr", "frist item onAnimationEnd");
                        CCSkyRightEPGView.this.isAnimStart = false;
                        if (CCSkyRightEPGView.this.onKeyActionDeriction != 18) {
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }
                });
                i3++;
                i2++;
            }
        }
    }

    public View getViewByPosition(int i, ListView listView) {
        if (i < this.mFirstListItemPostion || i > this.mLastlistItemPostion) {
            return listView.getChildAt(i - this.mFirstListItemPostion);
        }
        int i2 = i - this.mFirstListItemPostion;
        ////SkyLogger.d("lwr", "childIndex:" + i2);
        return listView.getChildAt(i2);
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
        this.skyEventListAdapter = new SkyEventListAdapter(this.mContext, list, i);
        this.listView.setAdapter(this.skyEventListAdapter);
        this.skyEventListAdapter.notifyDataSetChanged();
    }
}
