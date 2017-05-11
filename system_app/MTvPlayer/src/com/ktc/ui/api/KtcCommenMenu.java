package com.ktc.ui.api;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
////import com.ktc.framework.ktccommondefine.KtcworthBroadcastKey;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
import com.ktc.ui.blurbg.BlurBgLayout;
import com.ktc.ui.blurbg.BlurBgLayout.PAGETYPE;
import com.ktc.ui.menu.KtcFirstMenu;
import com.ktc.ui.menu.KtcFirstMenu.OnItemOnkeyListener;
import com.ktc.ui.menu.KtcMenuAdapter;
import com.ktc.ui.menu.KtcMenuData;
import com.ktc.ui.menu.KtcMenuItem_1;
import com.ktc.ui.menu.KtcSecondMenu;
import com.ktc.ui.menu.KtcSecondMenu.OnSecondItemAnimListener;
import com.ktc.ui.menu.KtcSecondMenu.OnSecondMenuItemOnkeyListener;
import com.ktc.util.KtcScreenParams;
import com.ktc.util.MyFocusFrame;

import java.util.List;
import com.horion.tv.R;

public class KtcCommenMenu extends FrameLayout {
    private ImageView arrowView;
    private BlurBgLayout bgLayout;
    private ImageView flagView;
    private Context mContext;
    private OnMenuOnKeyEventListener mListener;
    private LayoutParams mShadowP;
    private ImageView mShadowView;
    private KtcFirstMenu menu1;
    private KtcSecondMenu menu2;
    private MyFocusFrame menuFocusView;
    OnItemOnkeyListener menuListener = new OnItemOnkeyListener() {
        public boolean onItemOnClick(int position, View view, KtcMenuData ktcMenuData) {
            Log.d("Maxs","KtcCommenMenu:menuListener:onItemOnClick()");
            if (!(KtcCommenMenu.this.menu2.isDismisAnimStart || KtcCommenMenu.this.menu2.show_disAnimStart)) {
                if (ktcMenuData.isHasSecondMenu()) {
                    KtcCommenMenu.this.setShowingSecondMenu(true);
                }
                ((KtcMenuItem_1) view).setSelectState(true);
                Log.d("Maxs","KtcCommenMenu:menuListener:onItemCliek:position = " + position);
                KtcCommenMenu.this.menu1.setLastFocusItemID(position);
            Log.d("Maxs","KtcCommenMenu:menuListener:(mListener == null) = " + (mListener == null));

                if (KtcCommenMenu.this.mListener != null) {
                    Log.d("Maxs","KtcCommonMenu:menuListener.onFirstMenuItemOnClick");
                    Log.d("Maxs","" + mListener.toString());
                    KtcCommenMenu.this.mListener.onFirstMenuItemOnClick(position, view, ktcMenuData);
                }
            }
            return true;
        }

        public boolean onItemOnKeyBack(int i, View view) {
            if (KtcCommenMenu.this.menu2.isDismisAnimStart || KtcCommenMenu.this.menu2.show_disAnimStart) {
                return true;
            }
          ////KtcLogger.v("lgx", "on First menu on key Back !!!!!!!!!!!!!");
            KtcCommenMenu.this.menu1.resetOnKeyListener();
            KtcCommenMenu.this.menu2.dismissBySecondLeft_back = false;
            return KtcCommenMenu.this.mListener != null ? KtcCommenMenu.this.mListener.onFirstMenuItemOnKeyBack(i, view) : false;
        }

        public boolean onItemOnKeyLeft(int i, View view) {
            if (!(KtcCommenMenu.this.menu2.isDismisAnimStart || KtcCommenMenu.this.menu2.show_disAnimStart || KtcCommenMenu.this.mListener == null)) {
                KtcCommenMenu.this.mListener.onFirstMenuItemOnKeyLeft(i, view);
            }
            return true;
        }

        public boolean onItemOnKeyOther(int i, View view, int i2) {
            return KtcCommenMenu.this.mListener != null ? KtcCommenMenu.this.mListener.onFirstMenuItemOnKeyOther(i, view, i2) : false;
        }

        public boolean onItemOnKeyRight(int i, View view, KtcMenuData ktcMenuData) {
            ////KtcLogger.v("lgx", "menu2.isDismisAnimStart ==  " + KtcCommenMenu.this.menu2.isDismisAnimStart);
        	////KtcLogger.v("lgx", "menu2.show_disAnimStart ==  " + KtcCommenMenu.this.menu2.show_disAnimStart);
            if (!(KtcCommenMenu.this.menu2.isDismisAnimStart || KtcCommenMenu.this.menu2.show_disAnimStart)) {
                if (ktcMenuData.isHasSecondMenu()) {
                    KtcCommenMenu.this.setShowingSecondMenu(true);
                }
                ((KtcMenuItem_1) view).setSelectState(true);
                KtcCommenMenu.this.menu1.setLastFocusItemID(i);
                if (KtcCommenMenu.this.mListener != null) {
                    KtcCommenMenu.this.mListener.onFirstMenuItemOnKeyRight(i, view, ktcMenuData);
                }
            }
            return true;
        }
    };
    private TextView menuTitleView;
    private int menu_2_dataCount;
    OnSecondItemAnimListener secondItemAnimListener = new OnSecondItemAnimListener() {
        public void onSecondItemAnimEnd(boolean z) {
            if (!z) {
                KtcCommenMenu.this.menu1.dismissAnimtion(KtcCommenMenu.this, KtcCommenMenu.this.menu1.getLastFocusID());
            }
        }
    };
    OnSecondMenuItemOnkeyListener secondMenuListener = new OnSecondMenuItemOnkeyListener() {
        public boolean onSecondMenuItemOnClick(int i, View view, KtcMenuData ktcMenuData) {
            KtcCommenMenu.this.menu2.dismissBySecondLeft_back = false;
            if (KtcCommenMenu.this.mListener != null) {
                KtcCommenMenu.this.mListener.onSecondMenuItemOnClick(i, view, ktcMenuData);
            }
            return true;
        }

        public boolean onSecondMenuItemOnKeyBack(int id, View view) {
            if (!(KtcCommenMenu.this.menu2.isDismisAnimStart || KtcCommenMenu.this.menu2.show_disAnimStart)) {
                KtcCommenMenu.this.changeBgViewParams(KtcCommenMenu.this.bgLayout, (LayoutParams) KtcCommenMenu.this.bgLayout.getLayoutParams(), new LayoutParams(KtcScreenParams.getInstence(KtcCommenMenu.this.mContext).getResolutionValue(460), KtcScreenParams.getInstence(KtcCommenMenu.this.mContext).getResolutionValue(1080)), false);
                LayoutParams layoutParams = new LayoutParams(KtcScreenParams.getInstence(KtcCommenMenu.this.mContext).getResolutionValue(440), -2);
                layoutParams.topMargin = KtcScreenParams.getInstence(KtcCommenMenu.this.mContext).getResolutionValue(120);
                KtcCommenMenu.this.changeBgViewParams(KtcCommenMenu.this.titleLineView, (LayoutParams) KtcCommenMenu.this.titleLineView.getLayoutParams(), layoutParams, false);
              ////KtcLogger.v("lgx", "on Second menu on key Back !!!!!!!!!!!!!");
                if (KtcCommenMenu.this.menu1 != null) {
                    KtcCommenMenu.this.menu1.setMenuItemFocus(KtcCommenMenu.this.menu1.getLastFocusID());
                }
                KtcCommenMenu.this.menu2.show_dismissAnimation(KtcCommenMenu.this.menu1, false, id, false);
                KtcCommenMenu.this.arrowView.animate().alpha(0.0f).setDuration(200).start();
                KtcCommenMenu.this.menu1.setItemStateShowSecond(false);
                KtcCommenMenu.this.menu1.setShowingSecondMenu(false);
                KtcCommenMenu.this.moveFocusView(false, id, KtcCommenMenu.this.menu_2_dataCount);
                if (KtcCommenMenu.this.mListener != null) {
                    KtcCommenMenu.this.mListener.onSecondMenuItemOnKeyBack(id, view);
                }
                KtcCommenMenu.this.menu2.isDismisAnimStart = true;
                KtcCommenMenu.this.menu2.dismissBySecondLeft_back = true;
            }
            return true;
        }

        public boolean onSecondMenuItemOnKeyLeft(int id, View view) {
            if (!(KtcCommenMenu.this.menu2.isDismisAnimStart || KtcCommenMenu.this.menu2.show_disAnimStart)) {
                KtcCommenMenu.this.changeBgViewParams(KtcCommenMenu.this.bgLayout, (LayoutParams) KtcCommenMenu.this.bgLayout.getLayoutParams(), new LayoutParams(KtcScreenParams.getInstence(KtcCommenMenu.this.mContext).getResolutionValue(460), KtcScreenParams.getInstence(KtcCommenMenu.this.mContext).getResolutionValue(1080)), false);
                LayoutParams layoutParams = new LayoutParams(KtcScreenParams.getInstence(KtcCommenMenu.this.mContext).getResolutionValue(440), -2);
                layoutParams.topMargin = KtcScreenParams.getInstence(KtcCommenMenu.this.mContext).getResolutionValue(120);
                KtcCommenMenu.this.changeBgViewParams(KtcCommenMenu.this.titleLineView, (LayoutParams) KtcCommenMenu.this.titleLineView.getLayoutParams(), layoutParams, false);
                if (KtcCommenMenu.this.menu1 != null) {
                    KtcCommenMenu.this.menu1.setMenuItemFocus(KtcCommenMenu.this.menu1.getLastFocusID());
                }
                KtcCommenMenu.this.menu2.show_dismissAnimation(KtcCommenMenu.this.menu1, false, id, false);
                KtcCommenMenu.this.arrowView.animate().alpha(0.0f).setDuration(200).start();
                KtcCommenMenu.this.menu1.setItemStateShowSecond(false);
                KtcCommenMenu.this.menu1.setShowingSecondMenu(false);
                KtcCommenMenu.this.moveFocusView(false, id, KtcCommenMenu.this.menu_2_dataCount);
                if (KtcCommenMenu.this.mListener != null) {
                    KtcCommenMenu.this.mListener.onSecondMenuItemOnKeyLeft(id, view);
                }
                KtcCommenMenu.this.menu2.isDismisAnimStart = true;
                KtcCommenMenu.this.menu2.dismissBySecondLeft_back = true;
            }
            return true;
        }

        public boolean onSecondMenuItemOnKeyRight(int i, View view, KtcMenuData ktcMenuData) {
            return KtcCommenMenu.this.mListener != null ? KtcCommenMenu.this.mListener.onSecondMenuItemOnKeyRight(i, view, ktcMenuData) : false;
        }

        public boolean onSecondMenuOnKeyOther(int id, View view, int keyCode) {
            return KtcCommenMenu.this.mListener != null ? KtcCommenMenu.this.mListener.onSecondMenuItemOnKeyOther(id, view, keyCode) : false;
        }
    };
    private LinearLayout titleLayout;
    private ImageView titleLineView;

    public interface OnMenuOnKeyEventListener {
        boolean onFirstMenuItemOnClick(int position, View view, KtcMenuData ktcMenuData);

        boolean onFirstMenuItemOnKeyBack(int i, View view);

        boolean onFirstMenuItemOnKeyLeft(int i, View view);

        boolean onFirstMenuItemOnKeyOther(int i, View view, int i2);

        boolean onFirstMenuItemOnKeyRight(int i, View view, KtcMenuData ktcMenuData);

        boolean onSecondMenuItemOnClick(int i, View view, KtcMenuData ktcMenuData);

        boolean onSecondMenuItemOnKeyBack(int id, View view);

        boolean onSecondMenuItemOnKeyLeft(int id, View view);

        boolean onSecondMenuItemOnKeyOther(int id, View view, int keyCode);

        boolean onSecondMenuItemOnKeyRight(int i, View view, KtcMenuData ktcMenuData);
    }

    public KtcCommenMenu(Context context) {

        super(context);
        initView(context);
        Log.d("Maxs","KtcCommenMenu:initView1");
    }

    public KtcCommenMenu(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
        Log.d("Maxs","KtcCommenMenu:initView2");
    }

    public KtcCommenMenu(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
        Log.d("Maxs","KtcCommenMenu:initView3");
    }

    private void changeBgViewParams(final View view, LayoutParams layoutParams, final LayoutParams layoutParams2, boolean z) {
        ObjectAnimator ofObject = ObjectAnimator.ofObject(new LayoutParams(-2, -2), "layoutParams", new TypeEvaluator<LayoutParams>() {
            public LayoutParams evaluate(float f, LayoutParams layoutParams, LayoutParams layoutParams2) {
                LayoutParams layoutParams3 = new LayoutParams(-2, -2);
                if (!(layoutParams == null || layoutParams2 == null)) {
                    layoutParams3.leftMargin = (int) (((float) layoutParams.leftMargin) + (((float) (layoutParams2.leftMargin - layoutParams.leftMargin)) * f));
                    layoutParams3.topMargin = (int) (((float) layoutParams.topMargin) + (((float) (layoutParams2.topMargin - layoutParams.topMargin)) * f));
                    layoutParams3.width = (int) (((float) layoutParams.width) + (((float) (layoutParams2.width - layoutParams.width)) * f));
                    layoutParams3.height = (int) (((float) layoutParams.height) + (((float) (layoutParams2.height - layoutParams.height)) * f));
                }
                return layoutParams3;
            }
        }, new Object[]{layoutParams, layoutParams2});
        ofObject.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParams layoutParams = (LayoutParams) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
                if (view instanceof BlurBgLayout) {
                    KtcCommenMenu.this.mShadowP.leftMargin = layoutParams.width;
                    KtcCommenMenu.this.mShadowView.setLayoutParams(KtcCommenMenu.this.mShadowP);
                }
            }
        });
        ofObject.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                view.setLayoutParams(layoutParams2);
                if ((view instanceof BlurBgLayout) && KtcCommenMenu.this.mShadowP.leftMargin != layoutParams2.width) {
                    KtcCommenMenu.this.mShadowP.leftMargin = layoutParams2.width;
                    KtcCommenMenu.this.mShadowView.setLayoutParams(KtcCommenMenu.this.mShadowP);
                }
            }

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                view.setLayoutParams(layoutParams2);
                if ((view instanceof BlurBgLayout) && KtcCommenMenu.this.mShadowP.leftMargin != layoutParams2.width) {
                    KtcCommenMenu.this.mShadowP.leftMargin = layoutParams2.width;
                    KtcCommenMenu.this.mShadowView.setLayoutParams(KtcCommenMenu.this.mShadowP);
                }
                view.animate().cancel();
            }
        });
        ofObject.setInterpolator(new AccelerateDecelerateInterpolator());
        if (z) {
            ofObject.setDuration(100);
        } else {
            ofObject.setDuration(400);
        }
        ofObject.start();
    }

    private void dismissTitleAnimtion() {
        this.titleLineView.clearAnimation();
        Animation translateAnimation = new TranslateAnimation(0.0f, (float) (-KtcScreenParams.getInstence(this.mContext).getResolutionValue(450)), 0.0f, 0.0f);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        this.titleLineView.setVisibility(View.VISIBLE);
        this.titleLineView.startAnimation(translateAnimation);
        this.titleLayout.clearAnimation();
        translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-KtcScreenParams.getInstence(this.mContext).getResolutionValue(120)));
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        this.titleLayout.setVisibility(View.VISIBLE);
        this.titleLayout.startAnimation(translateAnimation);
        changeBgViewParams(this.bgLayout, (LayoutParams) this.bgLayout.getLayoutParams(), new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(460), KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080)), false);
        LayoutParams layoutParams = new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(440), -2);
        layoutParams.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(120);
        changeBgViewParams(this.titleLineView, (LayoutParams) this.titleLineView.getLayoutParams(), layoutParams, false);
    }

    private void initView(Context context) {
        this.mContext = context;
        setLayoutParams(new LayoutParams(-1, -1));
        Log.d("Maxs","KtcCommenMenu:initView");
        this.bgLayout = new BlurBgLayout(this.mContext);
        this.bgLayout.setPageType(PAGETYPE.OTHER_PAGE);
        Log.d("Maxs","KtcScreenParams.getInstence(this.mContext).getResolutionValue(460) = " + KtcScreenParams.getInstence(this.mContext).getResolutionValue(460));
        addView(this.bgLayout, new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(460), KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080)));
        this.mShadowView = new ImageView(this.mContext);
        this.mShadowView.setBackgroundResource(R.drawable.ui_sdk_menu_leftside_shadow);
        this.mShadowP = new LayoutParams(-2, -1);
        this.mShadowP.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(460);
        addView(this.mShadowView, this.mShadowP);
        this.menuFocusView = new MyFocusFrame(this.mContext, KtcScreenParams.getInstence(this.mContext).getResolutionValue(83));
        this.menuFocusView.setFocusable(false);
        this.menuFocusView.needAnimtion(false);
        this.menuFocusView.setImgResourse(R.drawable.ui_sdk_btn_focus_shadow_bg);
        LayoutParams layoutParams = new LayoutParams(-2, KtcScreenParams.getInstence(this.mContext).getResolutionValue(171));
        layoutParams.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(77) - KtcScreenParams.getInstence(this.mContext).getResolutionValue(47);
        layoutParams.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(516);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(52);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(550);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(100);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(47);
        KtcScreenParams.getInstence(this.mContext).getResolutionValue(171);
        addView(this.menuFocusView);
        this.menuFocusView.setVisibility(View.INVISIBLE);
        this.menu1 = new KtcFirstMenu(this.mContext);
        this.menu1.setTag("menu1");
        this.menu1.setOnItemOnkeyListener(this.menuListener);
        this.menu1.setFocusView(this.menuFocusView);
        this.menu2 = new KtcSecondMenu(this.mContext);
        this.menu2.setTag("menu2");
        this.menu2.setSecondMenuOnItemOnkeyListener(this.secondMenuListener);
        this.menu2.setOnSecondItemAnimListener(this.secondItemAnimListener);
        this.menu2.setFocusView(this.menuFocusView);
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams layoutParams2 = new LayoutParams(-2, -2);
        layoutParams2.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(57);
        layoutParams2.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(314);
        addView(linearLayout, layoutParams2);
        linearLayout.setGravity(16);
        linearLayout.addView(this.menu1);
        
        
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(40), -1);
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        this.arrowView = new ImageView(this.mContext);
        this.arrowView.setFocusable(false);
        this.arrowView.setVisibility(View.INVISIBLE);
        this.arrowView.setImageResource(R.drawable.ui_sdk_arrow_left);
        LayoutParams layoutParams4 = new LayoutParams(-2, -2);
        layoutParams4.gravity = 19;
        this.arrowView.setLayoutParams(layoutParams4);
        frameLayout.addView(this.arrowView);
        this.arrowView.setFocusable(false);
        linearLayout.addView(frameLayout, layoutParams3);
        linearLayout.addView(this.menu2);
        
        
        this.menu2.setVisibility(View.INVISIBLE);
        this.titleLineView = new ImageView(this.mContext);
        this.titleLineView.setFocusable(false);
        this.titleLineView.setBackgroundResource(R.drawable.ui_sdk_menu_title_line);
        LayoutParams layoutParams5 = new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(440), -2);
        layoutParams5.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(120);
        this.titleLineView.setVisibility(View.INVISIBLE);
        addView(this.titleLineView, layoutParams5);
        
        
        this.titleLayout = new LinearLayout(this.mContext);
        this.titleLayout.setGravity(16);
        this.titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.titleLayout.setVisibility(View.INVISIBLE);
        this.menuTitleView = new TextView(this.mContext);
        this.menuTitleView.setFocusable(false);
        this.menuTitleView.setText(R.string.menu_title);
        this.menuTitleView.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(62));
        KtcScreenParams.getInstence(this.mContext).settextAlpha(this.menuTitleView, 255, "707070");
        this.titleLayout.addView(this.menuTitleView);
        
        this.flagView = new ImageView(this.mContext);
        LayoutParams layoutParams6 = new LayoutParams(-2, -2);
        layoutParams6.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(81);
        layoutParams6.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(150);
        addView(this.flagView, layoutParams6);
        
        LayoutParams layoutParams7 = new LayoutParams(-2, -2);
        layoutParams7.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(78);
        layoutParams7.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(36);
        addView(this.titleLayout, layoutParams7);
    }

    private void moveFocusView(boolean z, int i, int i2) {
    }

    @SuppressLint({"NewApi"})
    private void showTitleAnimtion() {
        Log.d("Maxs","showTitleAnimation");
        this.titleLineView.clearAnimation();
        Animation translateAnimation = new TranslateAnimation((float) (-KtcScreenParams.getInstence(this.mContext).getResolutionValue(450)), 0.0f, 0.0f, 0.0f);
        translateAnimation.setDuration(200);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        this.titleLineView.setVisibility(View.VISIBLE);
        this.titleLineView.startAnimation(translateAnimation);
        this.titleLayout.clearAnimation();
        translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-KtcScreenParams.getInstence(this.mContext).getResolutionValue(120)), 0.0f);
        translateAnimation.setDuration(300);
        translateAnimation.setInterpolator(new OvershootInterpolator(2.0f));
        this.titleLayout.setVisibility(View.VISIBLE);
        this.titleLayout.startAnimation(translateAnimation);
        this.bgLayout.setLayoutParams(new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(460), KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080)));
        this.mShadowP.leftMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(460);
        this.mShadowView.setLayoutParams(this.mShadowP);
        this.bgLayout.setVisibility(View.VISIBLE);
    }

    public void dismissMenu(int i) {
        this.menu1.dismissAnimtion(this, i);
        dismissTitleAnimtion();
    }

    /*dismissMenuBySecond
     * 在二级菜单上点击ok跳转到第三方菜单时,一级和二级菜单都消失,比如点击二级菜单的删除频道
     */
    public void dismissMenuBySecond(int i) {
        dismissTitleAnimtion();
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setFillAfter(true);
        this.arrowView.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                KtcCommenMenu.this.arrowView.setVisibility(View.INVISIBLE);
                KtcCommenMenu.this.arrowView.clearAnimation();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.menu1.setMenuItemFocus(this.menu1.getLastFocusID());
        this.menu2.show_dismissAnimation(this.menu1, false, i, true);
    }

    public boolean getMenuDismissState() {
        return this.menu1 != null ? this.menu1.endDismiss : true;
    }

    public void refreshFirstMenuItem(KtcMenuData ktcMenuData, int i) {
        if (ktcMenuData != null && this.menu1 != null) {
            this.menu1.refreshItemData(ktcMenuData, i);
        }
    }

    public void refreshSecondMenuItem(KtcMenuData ktcMenuData, int i) {
        if (ktcMenuData != null && this.menu2 != null) {
            this.menu2.refreshItemData(ktcMenuData, i);
        }
    }

    public void setFirstMenuFocus(int i) {
        if (this.menu1 != null) {
            this.menu1.setMenuItemFocus(i);
        }
    }

    public void setFlagViewRes(int i) {
        this.flagView.setBackgroundResource(i);
    }

    public void setOnMenuOnKeyEventListener(OnMenuOnKeyEventListener onMenuOnKeyEventListener) {
        this.mListener = onMenuOnKeyEventListener;
    }

    public void setSecondMenuFocus(int i) {
        if (this.menu2 != null) {
            this.menu2.setMenuItemFocus(i);
        }
    }

    public void setShowingSecondMenu(boolean z) {
        this.menu1.setShowingSecondMenu(z);
    }

    public void showMenu(final List<KtcMenuData> list) {
        Log.d("Maxs","KtcCommenMenu:shwoMenu:list == null) = " + (list == null));
        Log.d("Maxs","KtcCommenMenu:shwoMenu:list.size() = " + list.size());
        if (list != null && list.size() > 0) {
            Log.d("Maxs","+=++====+++");
            this.menu1.removeAllViews();
            this.menu2.removeAllViews();
            if (getVisibility() != View.VISIBLE) {
                setVisibility(View.VISIBLE);
            }
            Log.d("Maxs","+=++==22112==+++");
            this.arrowView.setVisibility(View.INVISIBLE);
            this.menu2.isDismisAnimStart = false;
            this.menu2.show_disAnimStart = false;
            Log.d("Maxs","+=++==221132232==+++");
            post(new Runnable() {
                public void run() {
                    Log.d("Maxs","+=++==222==+++");
                    KtcCommenMenu.this.menu1.showFitstMenu(new KtcMenuAdapter(list));
                    KtcCommenMenu.this.showTitleAnimtion();
                }
            });
        }
    }

    @SuppressLint({"NewApi"})
    public void showSecondMenu(List<KtcMenuData> list, int secondMenuIndex) {
        Log.d("Maxs50","showSecondMenu:secondMenuIndex = " + secondMenuIndex );
        if (list != null && list.size() > 0) {
            Log.d("Maxs50","showSecondMenu");
            this.menu_2_dataCount = list.size();
            moveFocusView(true, secondMenuIndex, this.menu_2_dataCount);
            this.arrowView.setVisibility(View.VISIBLE);
            this.arrowView.animate().alpha(1.0f).setDuration(200).start();
            this.menu2.setMenuAdapter(this.menu1, new KtcMenuAdapter(list), secondMenuIndex);
            this.menu2.setPreViewWidth(this.menu1.getWidth());
            this.menu1.setItemStateShowSecond(true);
            changeBgViewParams(this.bgLayout, (LayoutParams) this.bgLayout.getLayoutParams(), new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(800), KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080)), true);
            ////KtcworthBroadcastKey.KTC_BROADCAST_KEY_COOCAA_TV = 760
            ////LayoutParams layoutParams = new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(KtcworthBroadcastKey.KTC_BROADCAST_KEY_COOCAA_TV), -2);
            LayoutParams layoutParams = new LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(760), -2);
            layoutParams.topMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(120);
            changeBgViewParams(this.titleLineView, (LayoutParams) this.titleLineView.getLayoutParams(), layoutParams, true);
        }
    }

    public void updataMenuTitle(String str) {
        if (this.menuTitleView != null && !TextUtils.isEmpty(str)) {
            this.menuTitleView.setText(str);
        }
    }
}
