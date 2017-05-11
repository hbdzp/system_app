package com.horion.tv.framework.logicapi;

import android.content.Context;
import android.util.Log;

import com.horion.system.data.TCRetData;
import com.horion.system.data.TCSetData;
import com.horion.tv.framework.logicapi.framework.TvLogicGetFunction;
import com.horion.tv.framework.logicapi.framework.TvLogicSetFunction;
import com.horion.tv.framework.logicapi.framework.UiCmdParams;
////import com.horion.tv.utils.KtcTVDebug;
import com.horion.tv.utils.KtcTvCache;

public class LogicApiForUICmd {
    private static LogicApiForUICmd instance = null;
    private KtcTvCache<String, TvLogicGetFunction> getFunctionCache = new KtcTvCache();
    private Context mContext = null;
    private KtcTvCache<String, TvLogicSetFunction> setFunctionCache = new KtcTvCache();

    public static LogicApiForUICmd getInstance() {
        if (instance == null) {
            instance = new LogicApiForUICmd();
        }
        return instance;
    }

    public void init(Context context) {
        ////KtcTVDebug.debug("~~~~~~~~~~~~~LogicApiForUICmd init~~~~~~~");
        this.mContext = context;
    }

    public void setGetFunctionList(KtcTvCache<String, TvLogicGetFunction> ktcTvCache) {
        this.getFunctionCache = ktcTvCache;
        ////KtcTVDebug.debug("~~~~~~~~~~~~~getFunctionCache size~~~~~~~" + ktcTvCache.size());
    }

    public void setSetFunctionList(KtcTvCache<String, TvLogicSetFunction> ktcTvCache) {
        this.setFunctionCache = ktcTvCache;
        ////KtcTVDebug.debug("~~~~~~~~~~~~~setFunctionCache size~~~~~~~" + ktcTvCache.size());
    }

    public TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams) {
        TvLogicGetFunction tvLogicGetFunction = (TvLogicGetFunction) this.getFunctionCache.get(str);

        Log.d("Maxs","LogicApiForUICmd:uiGetCmd:tvLogicGetFunction==null = " + (tvLogicGetFunction == null));

        if (tvLogicGetFunction != null) {
            return tvLogicGetFunction.uiGetCmd(str, uiCmdParams);
        }
       //// KtcTVDebug.debug("langlang uiGetCmd function = null !!!!");
        return null;
    }

    public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
        TvLogicSetFunction tvLogicSetFunction = (TvLogicSetFunction) this.setFunctionCache.get(str);
        if (tvLogicSetFunction != null) {
            return tvLogicSetFunction.uiSetCmd(str, uiCmdParams);
        }
        ////KtcTVDebug.debug("langlang uiSetCmd function = null !!!!");
        return null;
    }
}
