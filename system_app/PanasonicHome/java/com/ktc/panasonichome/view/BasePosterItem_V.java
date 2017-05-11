package com.ktc.panasonichome.view;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BasePosterItem_V extends BaseItem {
    public BasePosterItem_V(Context context, MyFocusFrame focusFrame) {
        super(context, focusFrame);
    }
    public BasePosterItem_V(Context context, MyFocusFrame focusFrame,String ShadowPicType) {
        super(context, focusFrame,ShadowPicType);
    }

    @Override
    protected void initBotomShadowImageView(String shadowPicType) {
        super.initBotomShadowImageView(shadowPicType);//bixu
        LogUtils.d("dzp","initBotomShadowImageView ");
        switch (shadowPicType){
            case "movie":
                this.botomShadowImageView=new ImageView(this.mContext);
                this.botomShadowImageView.setScaleType(ScaleType.FIT_XY);
                this.botomShadowImageView.setImageResource(R.drawable.shadow_movie);
                break;
            case "game":
                this.botomShadowImageView=new ImageView(this.mContext);
                this.botomShadowImageView.setScaleType(ScaleType.FIT_XY);
                this.botomShadowImageView.setImageResource(R.drawable.shadow_game);
                break;
            case "edu":
                this.botomShadowImageView=new ImageView(this.mContext);
                this.botomShadowImageView.setScaleType(ScaleType.FIT_XY);
                this.botomShadowImageView.setImageResource(R.drawable.shadow_edu);
                break;
            case "music":
                this.botomShadowImageView=new ImageView(this.mContext);
                this.botomShadowImageView.setScaleType(ScaleType.FIT_XY);
                this.botomShadowImageView.setImageResource(R.drawable.shadow_music);
                break;
            case "app":
                this.botomShadowImageView=new ImageView(this.mContext);
                this.botomShadowImageView.setScaleType(ScaleType.FIT_XY);
                this.botomShadowImageView.setImageResource(R.drawable.shadow_app);
                break;
            case "news":
                this.botomShadowImageView=new ImageView(this.mContext);
                this.botomShadowImageView.setScaleType(ScaleType.FIT_XY);
                this.botomShadowImageView.setImageResource(R.drawable.shadow_news);
                break;
        }
    }

    public void initView() {
        setItemWidth(ScreenParams.getInstence(this.mContext).getResolutionValue(302));
        setItemHeight(ScreenParams.getInstence(this.mContext).getResolutionValue(470));
        FrameLayout layout = new FrameLayout(this.mContext);
        LayoutParams paramsLayout = new LayoutParams(this.itemWidth + ScreenParams.getInstence(this.mContext).getResolutionValue(24), this.itemHeight + ScreenParams.getInstence(this.mContext).getResolutionValue(24));
        paramsLayout.gravity = 17;
        layout.setBackgroundResource(R.drawable.ui_sdk_btn_unfocus_big_shadow);
        addView(layout, paramsLayout);
        LayoutParams params = new LayoutParams(this.itemWidth, this.itemHeight);
        params.gravity = 17;
        this.contentLayout = new FrameLayout(this.mContext);
        addView(this.contentLayout, params);
        this.iconView = new ImageView(this.mContext);
        LayoutParams icon_p = new LayoutParams(-1, -1);
        icon_p.gravity = 17;
        this.contentLayout.addView(this.iconView, icon_p);
        //dzp add 3-1 start
        if(botomShadowImageView!=null){
            LayoutParams shadow_param = new LayoutParams(this.itemWidth,ScreenParams.getInstence(this
                    .mContext).getResolutionValue(144));
            shadow_param.gravity= Gravity.BOTTOM|Gravity.END;
            this.contentLayout.addView(botomShadowImageView,shadow_param);
        }
        //dzp add 3-1 end
        this.titleView = new TextView(this.mContext);
        this.titleView.getPaint().setAntiAlias(true);
        this.titleView.setSingleLine(true);
        this.titleView.setHorizontallyScrolling(true);
        this.titleView.setEllipsize(TruncateAt.MARQUEE);
        this.titleView.setMarqueeRepeatLimit(-1);
        this.titleView.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(32));
        this.titleView.setTextColor(-1);
        this.titleView.setGravity(17);
        LayoutParams title_p = new LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(150), -2);
        title_p.gravity = 81;
        title_p.bottomMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(25);
        this.contentLayout.addView(this.titleView, title_p);
    }

    public void setItemValue(String url, String title, String action) {
        if (url.startsWith("R.drawable.")) {
            recycleBitmap(this.iconView);
            int resourceIdByFilter = getResourceIdByFilter(url.substring("R.drawable.".length
                    ()));
            Picasso.with(this.mContext).load(resourceIdByFilter).resize(getItemWidth(),
                    getItemHeight()).into(this.iconView);
            if (!TextUtils.isEmpty(action)) {
                if (action.startsWith("http")) {
                    //TODO 处理从网络图片加载的逻辑
                    Picasso.with(this.mContext).load(action).error(resourceIdByFilter).resize(getItemWidth(),
                            getItemHeight()).into(this.iconView);
                }
            } else if (url.startsWith("R.drawable.")) {
                //TODO 处理图片从资源文件加载的逻辑
                Picasso.with(this.mContext).load(resourceIdByFilter).resize(getItemWidth(),
                        getItemHeight()).into(this.iconView);
            } else {
//                //TODO 处理图片从本地加载的逻辑
//                Picasso.with(this.mContext).load(new File(StartApi.getInstence(this.mContext)
//                        .getImgLocalPath() + url)).resize(getItemWidth(), getItemHeight()).into
//                        (this.iconView);
            }
        }
        if (!TextUtils.isEmpty(title)) {
            this.titleView.setText(title);
        }
        if (!TextUtils.isEmpty(action)) {
            this.startAction = action;
        }
    }

    public void focus(View view) {
        if (this.titleView != null) {
            this.titleView.setSelected(true);
        }
    }

    public void unfocus(View view) {
        if (this.titleView != null) {
            this.titleView.setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        LogUtils.d("dzp","this.picType:"+this.picType);
        switch (this.picType){
            case "movie":
                Intent intentMEDIA = new Intent();
                ComponentName componentNameMEDIA = new ComponentName("com.ktcp.tvvideo","com.ktcp.video.activity.MainActivity");
                intentMEDIA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intentMEDIA.setComponent(componentNameMEDIA);
                if (isIntentAvailable(mContext, intentMEDIA)) {
                    mContext.startActivity(intentMEDIA);
                } else {
                    Toast.makeText(mContext,getResources().getString(R.string
                            .toast_app_not_installed),Toast.LENGTH_SHORT).show();
                }
                return;
            case "music":
                Intent intentApp = new Intent();
                ComponentName componentNameApp = new ComponentName("com.audiocn.kalaok.tv.k63","com.audiocn.karaoke.tv.main.TvMainActivity");
                intentApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intentApp.setComponent(componentNameApp);
                if (isIntentAvailable(mContext, intentApp)) {
                    mContext.startActivity(intentApp);
                } else {
                    Toast.makeText(mContext,getResources().getString(R.string
                            .toast_app_not_installed),Toast.LENGTH_SHORT).show();
                }
                return;
            case "game":
                Intent intentForGame = new Intent();
                intentForGame.setAction(Intent.ACTION_MAIN);
                intentForGame.setPackage("com.shafa.panasonic.appstore");
                intentForGame.putExtra("com.shafa.market.item" , "control_game");
                intentForGame.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                if (isIntentAvailable(mContext, intentForGame)) {
                    mContext.startActivity(intentForGame);
                } else {
                    Toast.makeText(mContext,getResources().getString(R.string
                            .toast_app_not_installed),Toast.LENGTH_SHORT).show();
                }
                return;
            case "edu":
                Intent intentForEdu = new Intent();
                intentForEdu.setAction(Intent.ACTION_MAIN);
                intentForEdu.setPackage("com.shafa.panasonic.appstore");
                intentForEdu.putExtra("com.shafa.market.item" , "edu_app");
                intentForEdu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                if (isIntentAvailable(mContext, intentForEdu)) {
                    mContext.startActivity(intentForEdu);
                } else {
                    Toast.makeText(mContext,getResources().getString(R.string
                            .toast_app_not_installed),Toast.LENGTH_SHORT).show();
                }
                return;
            case "app":
                Intent intentForApp = new Intent();
                intentForApp.setAction(Intent.ACTION_MAIN);
                intentForApp.setPackage("com.shafa.panasonic.appstore");
                intentForApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                if (isIntentAvailable(mContext, intentForApp)) {
                    mContext.startActivity(intentForApp);
                } else {
                    Toast.makeText(mContext, getResources().getString(R.string
                            .toast_app_not_installed), Toast.LENGTH_SHORT).show();
                }
                return;
            case "news":
                Intent intentForNews = new Intent();
                intentForNews.setAction(Intent.ACTION_MAIN);
                intentForNews.setPackage("tv.icntv.ott");
                intentForNews.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                if (isIntentAvailable(mContext, intentForNews)) {
                    mContext.startActivity(intentForNews);
                } else {
                    Toast.makeText(mContext, getResources().getString(R.string
                            .toast_app_not_installed), Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }
    public static void launcherAppAccordingToPackageName(Context context, String packageName) {
        Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(mIntent);
            } catch (ActivityNotFoundException anf) {
                Toast.makeText(context, context.getResources().getString(R.string
                        .toast_app_not_installed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }
    private int getResourceIdByFilter(String name) {
        Resources res = this.mContext.getResources();
        return res.getIdentifier(name, "drawable", this.mContext.getPackageName());
    }
}
