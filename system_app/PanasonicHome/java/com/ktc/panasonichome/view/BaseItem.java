package com.ktc.panasonichome.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktc.panasonichome.LauncherActivity;
import com.ktc.panasonichome.R;
import com.ktc.panasonichome.model.Language;
//import com.ktc.panasonichome.protocol.ActionProtocolCenter;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.HomeMainPageGroup.PositionType;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by dinzhip on 2016/12/28.
 */
public abstract class BaseItem extends FrameLayout implements OnFocusChangeListener, OnClickListener {
    public static final int POST_ITEM_BIG   = 3;
    public static final int POST_ITEM_H     = 1;
    public static final int POST_ITEM_SMAIL = 2;
    public static final int POST_ITEM_V     = 0;
    public    FrameLayout  contentLayout;
    protected ImageView    iconView;
    protected int          itemHeight;
    protected int          itemLeft;
    protected int          itemTop;
    protected int          itemWidth;
    protected Context      mContext;
    protected MyFocusFrame myFocusView;
    private MyHandler myHandler = new MyHandler(this);
    protected PositionType positionType;
    protected String       startAction;
    protected TextView     titleView;

    //dzp add for botomShadowImageview 3-1
    protected ImageView botomShadowImageView=null;
    protected String picType="";
    //dzp add for botomShadowImageview 3-1

    private static class MyHandler extends Handler {
        private final WeakReference<BaseItem> mLayout;

        public MyHandler(BaseItem layout) {
            this.mLayout = new WeakReference(layout);
        }

        public void handleMessage(Message msg) {
            BaseItem layout = (BaseItem) this.mLayout.get();
            if (layout == null) {
                return;
            }
            if (msg.what == 0) {
                layout.focus((View) msg.obj);
            } else if (msg.what == 1) {
                layout.unfocus((View) msg.obj);
            }
        }
    }

    public abstract void focus(View view);

    public abstract void initView();

    public abstract void setItemValue(String str, String str2, String str3);

    public abstract void unfocus(View view);

    public BaseItem(Context context, MyFocusFrame focusView) {
        super(context);
        this.myFocusView = focusView;
        this.mContext = context;
        initView();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnFocusChangeListener(this);
        setOnClickListener(this);
    }
    //dzp add 3-1 start
    public BaseItem(Context context, MyFocusFrame focusView,String ShadowPicType) {
        super(context);
        this.myFocusView = focusView;
        this.mContext = context;
        initBotomShadowImageView(ShadowPicType);
        initView();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnFocusChangeListener(this);
        setOnClickListener(this);
    }

    protected void initBotomShadowImageView(String shadowPicType) {
        picType=shadowPicType;
    }


    //dzp add 3-1 end
    public BaseItem(Context context, MyFocusFrame focusView, int w, int h) {
        super(context);
        this.myFocusView = focusView;
        this.mContext = context;
        setItemWidth(w);
        setItemHeight(h);
        initView();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnFocusChangeListener(this);
        setOnClickListener(this);
    }

    public String getItemTitle(String titleId, Language language) {
        String title = "";
        if (titleId == null) {
            return title;
        }
        String lanKey = "cn";
        if (this.mContext.getResources().getConfiguration().locale.getLanguage().endsWith("zh")) {
            lanKey = "cn";
        }else{
            lanKey="en";
        }
        if (((HashMap) language.languagemap.get(lanKey)).containsKey(titleId)) {
            return (String) ((HashMap) language.languagemap.get(lanKey)).get(titleId);
        }
        return title;
    }

    public void recycleBitmap(ImageView view) {
        try {
            if (view.getDrawable() != null && (view.getDrawable() instanceof BitmapDrawable)) {
                Bitmap oldBm = ((BitmapDrawable) view.getDrawable()).getBitmap();
                view.setImageBitmap(null);
                if (oldBm != null) {
                    oldBm.recycle();
                    System.gc();
                }
            }
        } catch (Exception e) {
        }
    }

    public ImageView getIconView() {
        return this.iconView;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public PositionType getItemPositionType() {
        return this.positionType;
    }

    public void setItemWidth(int width) {
        this.itemWidth = width;
    }

    public int getItemWidth() {
        return this.itemWidth;
    }

    public void setItemHeight(int height) {
        this.itemHeight = height;
    }

    public int getItemHeight() {
        return this.itemHeight;
    }

    public void setItemLeft(int left) {
        this.itemLeft = left;
    }

    public int getItemLeft() {
        return this.itemLeft;
    }

    public void setItemTop(int top) {
        this.itemTop = top;
    }

    public int getItemTop() {
        return this.itemTop;
    }

    public void onClick(View v) {
        LauncherActivity.lastFocusView = v;
//        ActionProtocolCenter.getInstance((LauncherActivity) this.mContext).doIt(this.startAction);
        LogUtils.d("ZC", "onClick startAction ==  " + this.startAction);
    }

    public void setFocusFrame(View view, boolean needAnim) {
        if (getId() == 0) {
            LogUtils.d("cff", "getId() = " + getId());
            int x = (ScreenParams.getInstence(this.mContext).getResolutionValue(108) - ScreenParams.getInstence(this.mContext).getResolutionValue(120)) - ScreenParams.getInstence(this.mContext).getResolutionValue(15);
            int y = (ScreenParams.getInstence(this.mContext).getResolutionValue(424) - ScreenParams
                    .getInstence(this.mContext).getResolutionValue(120)) - ScreenParams.getInstence(this.mContext).getResolutionValue(15);
            int w = (ScreenParams.getInstence(this.mContext).getResolutionValue(302) +
                    (ScreenParams.getInstence(this.mContext).getResolutionValue(149) * 2)) - (ScreenParams.getInstence(this.mContext).getResolutionValue(15) * 2);
            int h = (ScreenParams.getInstence(this.mContext).getResolutionValue(470) + (ScreenParams.getInstence(this.mContext).getResolutionValue(149) * 2)) - (ScreenParams.getInstence(this.mContext).getResolutionValue(15) * 2);
            if (needAnim) {
                this.myFocusView.changeFocusPos(x, y, w, h);
            } else {
                this.myFocusView.initStarPosition(x, y, w, h);
            }
            if (this.myFocusView.getVisibility() != View.VISIBLE) {
                this.myFocusView.setVisibility(View.VISIBLE);
            }
        } else {
            this.myFocusView.changeFocusPos(this.contentLayout);
        }
        this.myFocusView.setImgResourse(R.drawable.home_focus);
    }

    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            if (this.myFocusView != null) {
                setFocusFrame(view, true);
            }
            if (this.myHandler.hasMessages(0)) {
                this.myHandler.removeMessages(0);
            }
            Message msg = this.myHandler.obtainMessage(0);
            msg.obj = view;
            this.myHandler.sendMessageDelayed(msg, 120);
            LauncherActivity.lastFocusView = view;
            return;
        }
        if (this.myHandler.hasMessages(1)) {
            this.myHandler.removeMessages(1);
        }
        if (this.myHandler.hasMessages(0)) {
            this.myHandler.removeMessages(0);
        }
        Message msg = this.myHandler.obtainMessage(1);
        msg.obj = view;
        this.myHandler.sendMessage(msg);
    }
}
