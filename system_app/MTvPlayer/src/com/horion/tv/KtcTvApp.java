package com.horion.tv;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import com.horion.tv.define.object.Source;
import com.horion.tv.service.base.KtcTvController;
import com.horion.tv.service.epg.SkyLeftEPGDataGet;
import com.horion.tv.service.menu.KtcTvMenuImple;
import com.horion.tv.service.base.KtcTvController.IKtcTvController;
import com.horion.tv.utils.UITextUtilInterface;

/**
 * Created by xiacf on 2016/12/31.
 */

public class KtcTvApp implements IKtcTvController{
    private static KtcTvApp instance = null;
    private KtcTvMenuImple ktcTvMenuImple = null;
    private FrameLayout rootMenuView = null;
    private FrameLayout contentView = null;
    private Context mLauncherContext = null;
    private UITextUtilInterface.LogicTextResource logicTextResource = null;
    private Context mServiceContext = null;
    private KtcTvApp() {
    }

    public static KtcTvApp getInstance() {
        if (instance == null) {
            instance = new KtcTvApp();
        }
        return instance;
    }

    public KtcTvMenuImple getKtcTvMenuImple() {
        if (this.ktcTvMenuImple == null) {
            this.ktcTvMenuImple = new KtcTvMenuImple();
        }
        return this.ktcTvMenuImple;
    }



    public void initView(Context context, boolean z) {
        this.mLauncherContext = context;
        this.contentView = new FrameLayout(this.mLauncherContext);
        ///this.rootMenuView = new FrameLayout(this.mLauncherContext);
        ///this.contentView.addView(this.rootMenuView);
        this.logicTextResource = new UITextUtilInterface.LogicTextResource(this.mLauncherContext);



    }
    public FrameLayout setRootMenuView() {
        return this.rootMenuView;
    }
    public void createKtcTvController(Context context) {
        synchronized (this) {
            Log.d("Maxs","------------------------KtcTvApp:createKtcTvController------------------------");
            ///KtcTvUtils.startCountTimer("START_TV createKtcTvController");
            this.mServiceContext = context;
            ////this.hasExternalApi = false;
            KtcTvController.getInstance().create(this.mServiceContext,this);
            KtcTvController.getInstance().initPrior();
        }
    }

    @Override
    public void onTvCreate(String str) {

    }


    public void onTvStart(Source source) {

    }

   /* @Override
    public TvPlugin getDefaultPlugin(Source source) {
        return null;
    }*/
}
