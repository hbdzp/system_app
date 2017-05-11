package com.horion.tv.service.base;

import android.content.Context;
import android.util.Log;

import com.mstar.android.tv.TvCommonManager;
import com.horion.tv.define.object.Source;
import com.horion.tv.logic.TvLogicManager;
import com.horion.tv.util.Constants.SOURCE_NAME_ENUM;

/**
 * Created by xiacf on 2016/12/31.
 */

public class KtcTvController {
    private static KtcTvController ktcTvControl;
    public Context mServiceContext;
    private IKtcTvController listener;
    private void KtcTvControl(){
        this.mServiceContext = null;
        this.listener = null;
    }

    public static KtcTvController getInstance(){
        if (ktcTvControl == null){
            ktcTvControl = new KtcTvController();
        }
        return ktcTvControl;
    }
    public void initPrior() {
        Log.d("Maxs","------------------------KtcTvControler:initPrior------------------------");
        ////List sourceList = getInstance().getSourceList();
       //// KtcTvCmdHandler.getInstance().initPrior(KtcTvService.getInstance(), KtcTvService.getInstance(), sourceList);
       //// KtcTvLogicCmdHandler.getInstance().initPrior(KtcTvService.getInstance(), KtcTvService.getInstance(), sourceList);
        TvLogicManager.getInstance().initPrior(this.mServiceContext);
        TvLogicManager.getInstance().initRemain(this.mServiceContext);
    }

    public SOURCE_NAME_ENUM getSourceNameEnum(){
        int currInput = TvCommonManager.getInstance().getCurrentTvInputSource();
        switch(currInput){
            case TvCommonManager.INPUT_SOURCE_ATV:
                return SOURCE_NAME_ENUM.ATV;
            case TvCommonManager.INPUT_SOURCE_DTV:
                return SOURCE_NAME_ENUM.DTMB;
            case TvCommonManager.INPUT_SOURCE_CVBS:
                return SOURCE_NAME_ENUM.AV;
            case TvCommonManager.INPUT_SOURCE_HDMI:
            case TvCommonManager.INPUT_SOURCE_HDMI2:
            case TvCommonManager.INPUT_SOURCE_HDMI3:
                return SOURCE_NAME_ENUM.HDMI;
            default:
                return SOURCE_NAME_ENUM.ATV;
        }
    }

    public int getCurrInputSource(){
        return TvCommonManager.getInstance().getCurrentTvInputSource();
    }


    public Source getCurrentSource(){
       int currSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        switch (currSource){
            case TvCommonManager.INPUT_SOURCE_ATV:
                return  new Source("ATV");
            case TvCommonManager.INPUT_SOURCE_DTV:
                return  new Source("DTMB");
            case TvCommonManager.INPUT_SOURCE_CVBS:
                return  new Source("AV");
            case TvCommonManager.INPUT_SOURCE_HDMI:
                return  new Source("HDMI");
            case TvCommonManager.INPUT_SOURCE_HDMI3:
                return  new Source("HDMI");
            case TvCommonManager.INPUT_SOURCE_HDMI4:
                return  new Source("HDMI");
        }
        return null;

    }
    public void create(Context context, IKtcTvController iKtcTvController) {
        this.mServiceContext = context;
        this.listener = iKtcTvController;
    }

    public interface IKtcTvDefaultSourceLoader {
        ///TvPlugin getDefaultPlugin(Source source);
    }

    public interface IKtcTvController extends IKtcTvDefaultSourceLoader {
        void onTvCreate(String str);

        void onTvStart(Source source);
    }
}
