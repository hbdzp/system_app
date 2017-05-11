package com.ktc.panasonichome.view.api;

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

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.MyFocusFrame;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout.PAGETYPE;
import com.ktc.panasonichome.view.menu.SkyFirstMenu;
import com.ktc.panasonichome.view.menu.SkyFirstMenu.OnItemOnkeyListener;
import com.ktc.panasonichome.view.menu.SkyMenuAdapter;
import com.ktc.panasonichome.view.menu.SkyMenuData;
import com.ktc.panasonichome.view.menu.SkyMenuItem;
import com.ktc.panasonichome.view.menu.SkySecondMenu;
import com.ktc.panasonichome.view.menu.SkySecondMenu.OnSecondItemAnimListener;
import com.ktc.panasonichome.view.menu.SkySecondMenu.OnSecondMenuItemOnkeyListener;

import java.util.List;

public class SkyCommenMenu extends FrameLayout {
    private ImageView                arrowView;
    private BlurBgLayout             bgLayout;
    private ImageView                flagView;
    private Context                  mContext;
    private OnMenuOnKeyEventListener mListener;
    private LayoutParams             mShadowP;
    private ImageView                mShadowView;
    private SkyFirstMenu             menu1;
    private SkySecondMenu            menu2;
    private MyFocusFrame             menuFocusView;
    OnItemOnkeyListener menuListener = new C02861();
    private TextView menuTitleView;
    private int      menu_2_dataCount;
    OnSecondItemAnimListener      secondItemAnimListener = new C02883();
    OnSecondMenuItemOnkeyListener secondMenuListener     = new C02872();
    private LinearLayout titleLayout;
    private ImageView    titleLineView;

    class C02861 implements OnItemOnkeyListener {
        C02861() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onItemOnKeyLeft(int r3, View r4) {
            /*
            r2 = this;
            r1 = 1;
            r0 = com.skyworth.ui.api.SkyCommenMenu.this;
            r0 = r0.menu2;
            r0 = r0.isDismisAnimStart;
            if (r0 != 0) goto L_0x0015;
        L_0x000b:
            r0 = com.skyworth.ui.api.SkyCommenMenu.this;
            r0 = r0.menu2;
            r0 = r0.show_disAnimStart;
            if (r0 == 0) goto L_0x0016;
        L_0x0015:
            return r1;
        L_0x0016:
            r0 = com.skyworth.ui.api.SkyCommenMenu.this;
            r0 = r0.mListener;
            if (r0 == 0) goto L_0x0027;
        L_0x001e:
            r0 = com.skyworth.ui.api.SkyCommenMenu.this;
            r0 = r0.mListener;
            r0.onFirstMenuItemOnKeyLeft(r3, r4);
        L_0x0027:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ktc.ui.api" +
                    ".SkyCommenMenu.1" +
                    ".onItemOnKeyLeft(int, android.view.View):boolean");
        }

        public boolean onItemOnKeyRight(int itemID, View v, SkyMenuData currentData) {
            LogUtils.v("dzp", "menu2.isDismisAnimStart ==  " + SkyCommenMenu.this.menu2
                    .isDismisAnimStart);
            LogUtils.v("dzp", "menu2.show_disAnimStart ==  " + SkyCommenMenu.this.menu2
                    .show_disAnimStart);
            if (SkyCommenMenu.this.menu2.isDismisAnimStart || SkyCommenMenu.this.menu2.show_disAnimStart) {
                return true;
            }
            if (currentData.isHasSecondMenu()) {
                SkyCommenMenu.this.setShowingSecondMenu(true);
            }
            ((SkyMenuItem) v).setSelectState(true);
            SkyCommenMenu.this.menu1.setLastFocusItemID(itemID);
            if (SkyCommenMenu.this.mListener != null) {
                SkyCommenMenu.this.mListener.onFirstMenuItemOnKeyRight(itemID, v, currentData);
            }
            return true;
        }

        public boolean onItemOnClick(int itemIndex, View v, SkyMenuData currentData) {
            if (SkyCommenMenu.this.menu2.isDismisAnimStart || SkyCommenMenu.this.menu2.show_disAnimStart) {
                return true;
            }
            if (currentData.isHasSecondMenu()) {
                SkyCommenMenu.this.setShowingSecondMenu(true);
            }
            ((SkyMenuItem) v).setSelectState(true);
            SkyCommenMenu.this.menu1.setLastFocusItemID(itemIndex);
            if (SkyCommenMenu.this.mListener != null) {
                SkyCommenMenu.this.mListener.onFirstMenuItemOnClick(itemIndex, v, currentData);
            }
            return true;
        }

        public boolean onItemOnKeyBack(int itemID, View v) {
            if (SkyCommenMenu.this.menu2.isDismisAnimStart || SkyCommenMenu.this.menu2.show_disAnimStart) {
                return true;
            }
            LogUtils.v("dzp", "on First menu on key Back !!!!!!!!!!!!!");
            SkyCommenMenu.this.menu1.resetOnKeyListener();
            SkyCommenMenu.this.menu2.dismissBySecondLeft_back = false;
            if (SkyCommenMenu.this.mListener != null) {
                return SkyCommenMenu.this.mListener.onFirstMenuItemOnKeyBack(itemID, v);
            }
            return false;
        }

        public boolean onItemOnKeyOther(int itemID, View v, int keyCode) {
            if (SkyCommenMenu.this.mListener != null) {
                return SkyCommenMenu.this.mListener.onFirstMenuItemOnKeyOther(itemID, v, keyCode);
            }
            return false;
        }
    }

    class C02872 implements OnSecondMenuItemOnkeyListener {
        C02872() {
        }

        public boolean onSecondMenuItemOnKeyLeft(int itemID, View v) {
            if (SkyCommenMenu.this.menu2.isDismisAnimStart || SkyCommenMenu.this.menu2.show_disAnimStart) {
                return true;
            }
            SkyCommenMenu.this.changeBgViewParams(SkyCommenMenu.this.bgLayout, (LayoutParams) SkyCommenMenu.this
                    .bgLayout.getLayoutParams(), new LayoutParams(ScreenParams.getInstence(SkyCommenMenu.this
                    .mContext).getResolutionValue(460), ScreenParams.getInstence(SkyCommenMenu.this.mContext)
                    .getResolutionValue(1080)), false);
            LayoutParams lineTar_p = new LayoutParams(ScreenParams.getInstence(SkyCommenMenu.this.mContext)
                    .getResolutionValue(440), -2);
            lineTar_p.topMargin = ScreenParams.getInstence(SkyCommenMenu.this.mContext).getResolutionValue(120);
            SkyCommenMenu.this.changeBgViewParams(SkyCommenMenu.this.titleLineView, (LayoutParams) SkyCommenMenu.this
                    .titleLineView.getLayoutParams(), lineTar_p, false);
            if (SkyCommenMenu.this.menu1 != null) {
                SkyCommenMenu.this.menu1.setMenuItemFocus(SkyCommenMenu.this.menu1.getLastFocusID());
            }
            SkyCommenMenu.this.menu2.show_dismissAnimation(SkyCommenMenu.this.menu1, false, itemID, false);
            SkyCommenMenu.this.arrowView.animate().alpha(0.0f).setDuration(200).start();
            SkyCommenMenu.this.menu1.setItemStateShowSecond(false);
            SkyCommenMenu.this.menu1.setShowingSecondMenu(false);
            SkyCommenMenu.this.moveFocusView(false, itemID, SkyCommenMenu.this.menu_2_dataCount);
            if (SkyCommenMenu.this.mListener != null) {
                SkyCommenMenu.this.mListener.onSecondMenuItemOnKeyLeft(itemID, v);
            }
            SkyCommenMenu.this.menu2.isDismisAnimStart = true;
            SkyCommenMenu.this.menu2.dismissBySecondLeft_back = true;
            return true;
        }

        public boolean onSecondMenuItemOnKeyRight(int itemID, View v, SkyMenuData currentData) {
            if (SkyCommenMenu.this.mListener != null) {
                return SkyCommenMenu.this.mListener.onSecondMenuItemOnKeyRight(itemID, v, currentData);
            }
            return false;
        }

        public boolean onSecondMenuItemOnClick(int itemIndex, View v, SkyMenuData currentData) {
            SkyCommenMenu.this.menu2.dismissBySecondLeft_back = false;
            if (SkyCommenMenu.this.mListener != null) {
                SkyCommenMenu.this.mListener.onSecondMenuItemOnClick(itemIndex, v, currentData);
            }
            return true;
        }

        public boolean onSecondMenuItemOnKeyBack(int itemID, View v) {
            if (SkyCommenMenu.this.menu2.isDismisAnimStart || SkyCommenMenu.this.menu2.show_disAnimStart) {
                return true;
            }
            SkyCommenMenu.this.changeBgViewParams(SkyCommenMenu.this.bgLayout, (LayoutParams) SkyCommenMenu.this
                    .bgLayout.getLayoutParams(), new LayoutParams(ScreenParams.getInstence(SkyCommenMenu.this
                    .mContext).getResolutionValue(460), ScreenParams.getInstence(SkyCommenMenu.this.mContext)
                    .getResolutionValue(1080)), false);
            LayoutParams lineTar_p = new LayoutParams(ScreenParams.getInstence(SkyCommenMenu.this.mContext)
                    .getResolutionValue(440), -2);
            lineTar_p.topMargin = ScreenParams.getInstence(SkyCommenMenu.this.mContext).getResolutionValue(120);
            SkyCommenMenu.this.changeBgViewParams(SkyCommenMenu.this.titleLineView, (LayoutParams) SkyCommenMenu.this
                    .titleLineView.getLayoutParams(), lineTar_p, false);
            LogUtils.v("dzp", "on Second menu on key Back !!!!!!!!!!!!!");
            if (SkyCommenMenu.this.menu1 != null) {
                SkyCommenMenu.this.menu1.setMenuItemFocus(SkyCommenMenu.this.menu1.getLastFocusID());
            }
            SkyCommenMenu.this.menu2.show_dismissAnimation(SkyCommenMenu.this.menu1, false, itemID, false);
            SkyCommenMenu.this.arrowView.animate().alpha(0.0f).setDuration(200).start();
            SkyCommenMenu.this.menu1.setItemStateShowSecond(false);
            SkyCommenMenu.this.menu1.setShowingSecondMenu(false);
            SkyCommenMenu.this.moveFocusView(false, itemID, SkyCommenMenu.this.menu_2_dataCount);
            if (SkyCommenMenu.this.mListener != null) {
                SkyCommenMenu.this.mListener.onSecondMenuItemOnKeyBack(itemID, v);
            }
            SkyCommenMenu.this.menu2.isDismisAnimStart = true;
            SkyCommenMenu.this.menu2.dismissBySecondLeft_back = true;
            return true;
        }

        public boolean onSecondMenuOnKeyOther(int itemID, View v, int keyCode) {
            if (SkyCommenMenu.this.mListener != null) {
                return SkyCommenMenu.this.mListener.onSecondMenuItemOnKeyOther(itemID, v, keyCode);
            }
            return false;
        }
    }

    class C02883 implements OnSecondItemAnimListener {
        C02883() {
        }

        public void onSecondItemAnimEnd(boolean isShow) {
            if (!isShow) {
                SkyCommenMenu.this.menu1.dismissAnimtion(SkyCommenMenu.this, SkyCommenMenu.this.menu1.getLastFocusID());
            }
        }
    }

    class C02905 implements AnimationListener {
        C02905() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            SkyCommenMenu.this.arrowView.setVisibility(View.INVISIBLE);
            SkyCommenMenu.this.arrowView.clearAnimation();
        }
    }

    class C02916 implements TypeEvaluator<LayoutParams> {
        C02916() {
        }

        public LayoutParams evaluate(float fraction, LayoutParams startValue, LayoutParams endValue) {
            LayoutParams tarFp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            if (startValue == null || endValue == null) {
                return tarFp;
            }
            tarFp.leftMargin = (int) (((float) startValue.leftMargin) + (((float) (endValue.leftMargin - startValue
                    .leftMargin)) * fraction));
            tarFp.topMargin = (int) (((float) startValue.topMargin) + (((float) (endValue.topMargin - startValue
                    .topMargin)) * fraction));
            tarFp.width = (int) (((float) startValue.width) + (((float) (endValue.width - startValue.width)) *
                    fraction));
            tarFp.height = (int) (((float) startValue.height) + (((float) (endValue.height - startValue.height)) *
                    fraction));
            return tarFp;
        }
    }

    public interface OnMenuOnKeyEventListener {
        boolean onFirstMenuItemOnClick(int i, View view, SkyMenuData skyMenuData);

        boolean onFirstMenuItemOnKeyBack(int i, View view);

        boolean onFirstMenuItemOnKeyLeft(int i, View view);

        boolean onFirstMenuItemOnKeyOther(int i, View view, int i2);

        boolean onFirstMenuItemOnKeyRight(int i, View view, SkyMenuData skyMenuData);

        boolean onSecondMenuItemOnClick(int i, View view, SkyMenuData skyMenuData);

        boolean onSecondMenuItemOnKeyBack(int i, View view);

        boolean onSecondMenuItemOnKeyLeft(int i, View view);

        boolean onSecondMenuItemOnKeyOther(int i, View view, int i2);

        boolean onSecondMenuItemOnKeyRight(int i, View view, SkyMenuData skyMenuData);
    }

    public void setOnMenuOnKeyEventListener(OnMenuOnKeyEventListener listener) {
        this.mListener = listener;
    }

    public SkyCommenMenu(Context context) {
        super(context);
        initView(context);
    }

    public SkyCommenMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SkyCommenMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        setLayoutParams(new LayoutParams(-1, -1));
        this.bgLayout = new BlurBgLayout(this.mContext);
        this.bgLayout.setPageType(PAGETYPE.OTHER_PAGE);
        addView(this.bgLayout, new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(460),
                ScreenParams.getInstence(this.mContext).getResolutionValue(1080)));
        this.mShadowView = new ImageView(this.mContext);
        this.mShadowView.setBackgroundResource(R.drawable.ui_sdk_menu_leftside_shadow);
        this.mShadowP = new LayoutParams(-2, -1);
        this.mShadowP.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(460);
        addView(this.mShadowView, this.mShadowP);
        this.menuFocusView = new MyFocusFrame(this.mContext, ScreenParams.getInstence(this.mContext)
                .getResolutionValue(83));
        this.menuFocusView.setFocusable(false);
        this.menuFocusView.needAnimtion(false);
        this.menuFocusView.setImgResourse(R.drawable.ui_sdk_btn_focus_shadow_bg);
        LayoutParams lp = new LayoutParams(-2, ScreenParams.getInstence(this.mContext).getResolutionValue(171));
        lp.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(77) - ScreenParams.getInstence
                (this.mContext).getResolutionValue(47);
        lp.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(516);
        int x = ScreenParams.getInstence(this.mContext).getResolutionValue(52);
        int y = ScreenParams.getInstence(this.mContext).getResolutionValue(550);
        int width = ScreenParams.getInstence(this.mContext).getResolutionValue(100) + (ScreenParams.getInstence(this
                .mContext).getResolutionValue(47) * 2);
        int height = ScreenParams.getInstence(this.mContext).getResolutionValue(171);
        addView(this.menuFocusView);
        this.menuFocusView.setVisibility(View.INVISIBLE);
        this.menu1 = new SkyFirstMenu(this.mContext);
        this.menu1.setTag("menu1");
        this.menu1.setOnItemOnkeyListener(this.menuListener);
        this.menu1.setFocusView(this.menuFocusView);
        this.menu2 = new SkySecondMenu(this.mContext);
        this.menu2.setTag("menu2");
        this.menu2.setSecondMenuOnItemOnkeyListener(this.secondMenuListener);
        this.menu2.setOnSecondItemAnimListener(this.secondItemAnimListener);
        this.menu2.setFocusView(menuFocusView);
        LinearLayout mainLayout = new LinearLayout(this.mContext);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams mainP = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        mainP.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(57);
        mainP.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(314);
        addView(mainLayout, mainP);
        mainLayout.setGravity(16);
        mainLayout.addView(this.menu1);
        LinearLayout.LayoutParams midleLayoutP = new LinearLayout.LayoutParams(ScreenParams.getInstence(this
                .mContext).getResolutionValue(40), -1);
        FrameLayout midleLayout = new FrameLayout(this.mContext);
        this.arrowView = new ImageView(this.mContext);
        this.arrowView.setFocusable(false);
        this.arrowView.setVisibility(View.INVISIBLE);
        this.arrowView.setImageResource(R.drawable.ui_sdk_arrow_left);
        LayoutParams midleP = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        midleP.gravity = 19;
        this.arrowView.setLayoutParams(midleP);
        midleLayout.addView(this.arrowView);
        this.arrowView.setFocusable(false);
        mainLayout.addView(midleLayout, midleLayoutP);
        mainLayout.addView(this.menu2);
        this.menu2.setVisibility(View.INVISIBLE);
        this.titleLineView = new ImageView(this.mContext);
        this.titleLineView.setFocusable(false);
        this.titleLineView.setBackgroundResource(R.drawable.ui_sdk_menu_title_line);
        LayoutParams title_line_p = new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(440),
                -2);
        title_line_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(120);
        this.titleLineView.setVisibility(View.INVISIBLE);
        addView(this.titleLineView, title_line_p);
        this.titleLayout = new LinearLayout(this.mContext);
        this.titleLayout.setGravity(16);
        this.titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.titleLayout.setVisibility(View.INVISIBLE);
        this.menuTitleView = new TextView(this.mContext);
        this.menuTitleView.setFocusable(false);
        this.menuTitleView.setText(R.string.menu_title);
        this.menuTitleView.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(62));
        ScreenParams.getInstence(this.mContext).settextAlpha(this.menuTitleView, 255, "707070");
        this.titleLayout.addView(this.menuTitleView);
        this.flagView = new ImageView(this.mContext);
        LayoutParams flag_p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        flag_p.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(81);
        flag_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(150);
        addView(this.flagView, flag_p);
        LayoutParams title_p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        title_p.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(78);
        title_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(36);
        addView(this.titleLayout, title_p);
    }

    public void updataMenuTitle(String title) {
        if (this.menuTitleView != null && !TextUtils.isEmpty(title)) {
            this.menuTitleView.setText(title);
        }
    }

    @SuppressLint({"NewApi"})
    private void showTitleAnimtion() {
        this.titleLineView.clearAnimation();
        TranslateAnimation lineAnim = new TranslateAnimation((float) (-ScreenParams.getInstence(this.mContext)
                .getResolutionValue(450)), 0.0f, 0.0f, 0.0f);
        lineAnim.setDuration(200);
        lineAnim.setInterpolator(new AccelerateInterpolator());
        this.titleLineView.setVisibility(View.VISIBLE);
        this.titleLineView.startAnimation(lineAnim);
        this.titleLayout.clearAnimation();
        TranslateAnimation titleAnim = new TranslateAnimation(0.0f, 0.0f, (float) (-ScreenParams.getInstence(this
                .mContext).getResolutionValue(120)), 0.0f);
        titleAnim.setDuration(300);
        titleAnim.setInterpolator(new OvershootInterpolator(2.0f));
        this.titleLayout.setVisibility(View.VISIBLE);
        this.titleLayout.startAnimation(titleAnim);
        this.bgLayout.setLayoutParams(new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue
                (460), ScreenParams.getInstence(this.mContext).getResolutionValue(1080)));
        this.mShadowP.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(460);
        this.mShadowView.setLayoutParams(this.mShadowP);
        this.bgLayout.setVisibility(View.VISIBLE);
    }

    private void dismissTitleAnimtion() {
        this.titleLineView.clearAnimation();
        TranslateAnimation lineAnim = new TranslateAnimation(0.0f, (float) (-ScreenParams.getInstence(this.mContext)
                .getResolutionValue(450)), 0.0f, 0.0f);
        lineAnim.setDuration(200);
        lineAnim.setFillAfter(true);
        lineAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        this.titleLineView.setVisibility(View.VISIBLE);
        this.titleLineView.startAnimation(lineAnim);
        this.titleLayout.clearAnimation();
        TranslateAnimation titleAnim = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-ScreenParams.getInstence
                (this.mContext).getResolutionValue(120)));
        titleAnim.setDuration(300);
        titleAnim.setFillAfter(true);
        titleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        this.titleLayout.setVisibility(View.VISIBLE);
        this.titleLayout.startAnimation(titleAnim);
        changeBgViewParams(this.bgLayout, (LayoutParams) this.bgLayout.getLayoutParams(), new LayoutParams
                (ScreenParams.getInstence(this.mContext).getResolutionValue(460), ScreenParams.getInstence(this
                        .mContext).getResolutionValue(1080)), false);
        LayoutParams lineTar_p = new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(440), -2);
        lineTar_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(120);
        changeBgViewParams(this.titleLineView, (LayoutParams) this.titleLineView.getLayoutParams(), lineTar_p, false);
    }

    public void setFlagViewRes(int resid) {
        this.flagView.setBackgroundResource(resid);
    }

    public void setShowingSecondMenu(boolean isShowingSecondMenu) {
        this.menu1.setShowingSecondMenu(isShowingSecondMenu);
    }

    public boolean getMenuDismissState() {
        return this.menu1 != null ? this.menu1.endDismiss : true;
    }

    public void showMenu(final List<SkyMenuData> datas) {
        if (datas != null && datas.size() > 0) {
            this.menu1.removeAllViews();
            this.menu2.removeAllViews();
            if (getVisibility() != View.VISIBLE) {
                setVisibility(View.VISIBLE);
            }
            this.arrowView.setVisibility(View.INVISIBLE);
            this.menu2.isDismisAnimStart = false;
            this.menu2.show_disAnimStart = false;
            post(new Runnable() {
                public void run() {
                    SkyCommenMenu.this.menu1.showFitstMenu(new SkyMenuAdapter(datas));
                    SkyCommenMenu.this.showTitleAnimtion();
                }
            });
        }
    }

    public void refreshFirstMenuItem(SkyMenuData data, int index) {
        if (data != null && this.menu1 != null) {
            this.menu1.refreshItemData(data, index);
        }
    }

    public void setFirstMenuFocus(int index) {
        if (this.menu1 != null) {
            this.menu1.setMenuItemFocus(index);
        }
    }

    public void setSecondMenuFocus(int index) {
        if (this.menu2 != null) {
            this.menu2.setMenuItemFocus(index);
        }
    }

    public void dismissMenu(int id) {
        this.menu1.dismissAnimtion(this, id);
        dismissTitleAnimtion();
    }

    public void dismissMenuBySecond(int SecondMenuindex) {
        dismissTitleAnimtion();
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);
        alphaAnim.setDuration(200);
        alphaAnim.setFillAfter(true);
        this.arrowView.startAnimation(alphaAnim);
        alphaAnim.setAnimationListener(new C02905());
        this.menu1.setMenuItemFocus(this.menu1.getLastFocusID());
        this.menu2.show_dismissAnimation(this.menu1, false, SecondMenuindex, true);
    }

    @SuppressLint({"NewApi"})
    public void showSecondMenu(List<SkyMenuData> datas, int focusId) {
        if (datas != null && datas.size() > 0) {
            this.menu_2_dataCount = datas.size();
            moveFocusView(true, focusId, this.menu_2_dataCount);
            this.arrowView.setVisibility(View.VISIBLE);
            this.arrowView.animate().alpha(1.0f).setDuration(200).start();
            this.menu2.setMenuAdapter(this.menu1, new SkyMenuAdapter(datas), focusId);
            this.menu2.setPreViewWidth(this.menu1.getWidth());
            this.menu1.setItemStateShowSecond(true);
            changeBgViewParams(this.bgLayout, (LayoutParams) this.bgLayout.getLayoutParams(), new LayoutParams
                    (ScreenParams.getInstence(this.mContext).getResolutionValue(800), ScreenParams.getInstence(this
                            .mContext).getResolutionValue(1080)), true);
            LayoutParams lineTar_p = new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue
                    (760), -2);
            lineTar_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(120);
            changeBgViewParams(this.titleLineView, (LayoutParams) this.titleLineView.getLayoutParams(), lineTar_p,
                    true);
        }
    }

    private void changeBgViewParams(final View view, LayoutParams frame_p, final LayoutParams tar_p, boolean show) {
        ObjectAnimator animatorParams = ObjectAnimator.ofObject(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT), "layoutParams", new C02916
                (), new Object[]{frame_p, tar_p});
        animatorParams.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams tarFp = (LayoutParams) animation.getAnimatedValue();
                view.setLayoutParams(tarFp);
                if (view instanceof BlurBgLayout) {
                    SkyCommenMenu.this.mShadowP.leftMargin = tarFp.width;
                    SkyCommenMenu.this.mShadowView.setLayoutParams(SkyCommenMenu.this.mShadowP);
                }
            }
        });
        animatorParams.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                view.setLayoutParams(tar_p);
                if ((view instanceof BlurBgLayout) && SkyCommenMenu.this.mShadowP.leftMargin != tar_p.width) {
                    SkyCommenMenu.this.mShadowP.leftMargin = tar_p.width;
                    SkyCommenMenu.this.mShadowView.setLayoutParams(SkyCommenMenu.this.mShadowP);
                }
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setLayoutParams(tar_p);
                if ((view instanceof BlurBgLayout) && SkyCommenMenu.this.mShadowP.leftMargin != tar_p.width) {
                    SkyCommenMenu.this.mShadowP.leftMargin = tar_p.width;
                    SkyCommenMenu.this.mShadowView.setLayoutParams(SkyCommenMenu.this.mShadowP);
                }
                view.animate().cancel();
            }
        });
        animatorParams.setInterpolator(new AccelerateDecelerateInterpolator());
        if (show) {
            animatorParams.setDuration(100);
        } else {
            animatorParams.setDuration(400);
        }
        animatorParams.start();
    }

    public void refreshSecondMenuItem(SkyMenuData data, int index) {
        if (data != null && this.menu2 != null) {
            this.menu2.refreshItemData(data, index);
        }
    }

    private void moveFocusView(boolean isShowSecond, int index, int count) {
    }
}
