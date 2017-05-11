package com.horion.tv.logic;

import android.content.Context;
import android.util.Log;

import com.horion.tv.framework.api.KtcTvApiListener;
import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
import com.horion.tv.define.object.Source;
import com.horion.tv.framework.logicapi.LogicApiForUI;
import com.horion.tv.framework.logicapi.LogicApiForUICmd;
import com.horion.tv.framework.logicapi.framework.ATVLogicApi;
import com.horion.tv.framework.logicapi.framework.AVLogicApi;
import com.horion.tv.framework.logicapi.framework.DTMBLogicApi;
////import com.horion.tv.framework.logicapi.framework.DVBCLogicApi;
////import com.horion.tv.framework.logicapi.framework.EPGLogicApi;
import com.horion.tv.framework.logicapi.framework.HDMILogicApi;
import com.horion.tv.framework.logicapi.framework.IPLiveLogicApi;
import com.horion.tv.framework.logicapi.framework.SystemLogicApi;
import com.horion.tv.framework.logicapi.framework.TvLogicGetFunction;
import com.horion.tv.framework.logicapi.framework.TvLogicSetFunction;
import com.horion.tv.framework.logicapi.framework.VGALogicApi;
import com.horion.tv.framework.logicapi.framework.YUVLogicApi;
import com.horion.tv.logic.atv.ATVLogic;
import com.horion.tv.logic.av.AVLogic;
import com.horion.tv.logic.dtmb.DTMBLogic;
////import com.horion.tv.logic.dvbc.DVBCLogic;
////import com.horion.tv.logic.epg.EPGLogic;
import com.horion.tv.logic.framework.TvLogic;
import com.horion.tv.logic.hdmi.HDMILogic;
////import com.horion.tv.logic.system.NoSignalManager;
import com.horion.tv.logic.system.SystemLogic;
////import com.horion.tv.logic.utils.EPGLoader;
////import com.horion.tv.logic.utils.SwitchChannelManagerL;
////import com.horion.tv.logic.vga.VGALogic;
import com.horion.tv.logic.yuv.YUVLogic;
////import com.horion.tv.utils.KtcTVDebug;
import com.horion.tv.utils.KtcTvCache;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class TvLogicManager {
    private static  int[] ktcTvCacheOperSwitchesValues;
    private static TvLogicManager instance = null;
    ////private static final KtcTvApiListener[] specialListener = new KtcTvApiListener[]{EPGLoader.getInstance()};
    private static final KtcTvApiListener[] specialListener = new KtcTvApiListener[]{};
    private Source currentSource = null;
    private KtcTvCache<String, TvLogicGetFunction> getFunctionCache = new KtcTvCache();
    private KtcCmdConnectorListener listener = null;
    public Context mContext = null;
    private KtcTvCache<String, TvLogicSetFunction> setFunctionCache = new KtcTvCache();
    private KtcTvCache<String, KtcTvApiListener> tvApiCallbackCache = new KtcTvCache();
    private HashMap<LOGIC_NAME_ENUM, TvLogic> tvLogicFistInitMap = new HashMap();
    private HashMap<LOGIC_NAME_ENUM, TvLogic> tvLogicRestInitMap = new HashMap();

    public enum LOGIC_NAME_ENUM {
        ATVLOGIC,
        AVLOGIC,
        IPLIVELOGIC,
        DVBCLOGIC,
        DTMBLOGIC,
        SYSTEMLOGIC,
        HDMILOGIC,
        YUVLOGIC,
        EPGLOGIC,
        VGALOGIC
    }

    private enum KtcTvCacheOper {
        CLEAR,
        ADD,
        GET
    }

    private static  int[] getktcTvCacheOperSwitchesValues() {
        if (ktcTvCacheOperSwitchesValues != null) {
            return ktcTvCacheOperSwitchesValues;
        }
        int[] iArr = new int[KtcTvCacheOper.values().length];
        try {
            iArr[KtcTvCacheOper.ADD.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[KtcTvCacheOper.CLEAR.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[KtcTvCacheOper.GET.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        ktcTvCacheOperSwitchesValues = iArr;
        return iArr;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private TvLogicGetFunction getFunctionCacheOperate(KtcTvCacheOper r3, String r4, TvLogicGetFunction r5) {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = getktcTvCacheOperSwitchesValues();	 Catch:{ all -> 0x0016 }
        r1 = r3.ordinal();	 Catch:{ all -> 0x0016 }
        r0 = r0[r1];	 Catch:{ all -> 0x0016 }
        switch(r0) {
            case 1: goto L_0x0019;
            case 2: goto L_0x0010;
            case 3: goto L_0x001f;
            default: goto L_0x000e;
        };
    L_0x000e:
        monitor-exit(r2);
    L_0x000f:
        return r5;
    L_0x0010:
        r0 = r2.getFunctionCache;	 Catch:{ all -> 0x0016 }
        r0.clear();	 Catch:{ all -> 0x0016 }
        goto L_0x000e;
    L_0x0016:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
    L_0x0019:
        r0 = r2.getFunctionCache;	 Catch:{ all -> 0x0016 }
        r0.add(r4, r5);	 Catch:{ all -> 0x0016 }
        goto L_0x000e;
    L_0x001f:
        r0 = r2.getFunctionCache;	 Catch:{ all -> 0x0016 }
        r0 = r0.get(r4);	 Catch:{ all -> 0x0016 }
        r0 = (com.horion.tv.framework.logicapi.framework.TvLogicGetFunction) r0;	 Catch:{ all -> 0x0016 }
        monitor-exit(r2);
        r5 = r0;
        goto L_0x000f;

        throw new UnsupportedOperationException("Method not decompiled: com.horion.tv.logic.TvLogicManager.getFunctionCacheOperate(com.horion.tv.logic.TvLogicManager$KtcTvCacheOper, java.lang.String, com.horion.tv.framework.logicapi.framework.TvLogicGetFunction):com.horion.tv.framework.logicapi.framework.TvLogicGetFunction");
        */
        int[]  arrar = getktcTvCacheOperSwitchesValues();
        Log.d("Maxs","TvLogicManager:TvLogicGetFunction = arrar[r3.ordinal()] = " + arrar[r3.ordinal()]);
        switch (arrar[r3.ordinal()]){
            case 1:
                Log.d("Maxs","TvLogicManager:TvLogicGetFunction r4 =" + r4 + " /r5 = " + r5);
                getFunctionCache.add(r4,r5);
                break;
            case 2:
                getFunctionCache.clear();
                break;
            case 3:
                getFunctionCache.get(r4);
                break;
        }
        return null;
    }

    public static TvLogicManager getInstance() {
        if (instance == null) {
            instance = new TvLogicManager();
        }
        return instance;
    }

    private void logicApiForUICmdInit() {
        Log.d("Maxs","TvLogicManager:logicApiForUICmdInit();");
        LogicApiForUICmd.getInstance().init(this.mContext);
        LogicApiForUICmd.getInstance().setGetFunctionList(this.getFunctionCache);
        LogicApiForUICmd.getInstance().setSetFunctionList(this.setFunctionCache);
    }

    private void logicApiForUIInit() {
        LogicApiForUI.getInstance().init(this.mContext);
        LogicApiForUI.getInstance().setATVLogicApi((ATVLogicApi) this.tvLogicRestInitMap.get(LOGIC_NAME_ENUM.ATVLOGIC));
        LogicApiForUI.getInstance().setAVLogicApi((AVLogicApi) this.tvLogicRestInitMap.get(LOGIC_NAME_ENUM.AVLOGIC));
        LogicApiForUI.getInstance().setDTMBLogicApi((DTMBLogicApi) this.tvLogicRestInitMap.get(LOGIC_NAME_ENUM.DTMBLOGIC));
        ////LogicApiForUI.getInstance().setDVBCLogicApi((DVBCLogicApi) this.tvLogicRestInitMap.get(LOGIC_NAME_ENUM.DVBCLOGIC));
        ////LogicApiForUI.getInstance().setEPGLogicApi((EPGLogicApi) this.tvLogicRestInitMap.get(LOGIC_NAME_ENUM.EPGLOGIC));
        LogicApiForUI.getInstance().setHDMILogicApi((HDMILogicApi) this.tvLogicRestInitMap.get(LOGIC_NAME_ENUM.HDMILOGIC));
        LogicApiForUI.getInstance().setIPLiveLogicApi((IPLiveLogicApi) this.tvLogicRestInitMap.get(LOGIC_NAME_ENUM.IPLIVELOGIC));
        LogicApiForUI.getInstance().setSystemLogicApi((SystemLogicApi) this.tvLogicFistInitMap.get(LOGIC_NAME_ENUM.SYSTEMLOGIC));
        LogicApiForUI.getInstance().setVGALogicApi((VGALogicApi) this.tvLogicRestInitMap.get(LOGIC_NAME_ENUM.VGALOGIC));
        LogicApiForUI.getInstance().setYUVLogicApi((YUVLogicApi) this.tvLogicRestInitMap.get(LOGIC_NAME_ENUM.YUVLOGIC));
    }

    private void logicApiInit() {
        logicApiForUICmdInit();
        logicApiForUIInit();
        for (KtcTvApiListener ktcTvApiListener : specialListener) {
            String[] handleCmds = ktcTvApiListener.getHandleCmds();
            if (handleCmds != null) {
                for (String tvApiCallbackCacheOperate : handleCmds) {
                    tvApiCallbackCacheOperate(KtcTvCacheOper.ADD, tvApiCallbackCacheOperate, ktcTvApiListener);
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private TvLogicSetFunction setFunctionCacheOperate(KtcTvCacheOper r3, String r4, TvLogicSetFunction r5) {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = -getktcTvCacheOperSwitchesValues;	 Catch:{ all -> 0x0016 }
        r1 = r3.ordinal();	 Catch:{ all -> 0x0016 }
        r0 = r0[r1];	 Catch:{ all -> 0x0016 }
        switch(r0) {
            case 1: goto L_0x0019;
            case 2: goto L_0x0010;
            case 3: goto L_0x001f;
            default: goto L_0x000e;
        };
    L_0x000e:
        monitor-exit(r2);
    L_0x000f:
        return r5;
    L_0x0010:
        r0 = r2.setFunctionCache;	 Catch:{ all -> 0x0016 }
        r0.clear();	 Catch:{ all -> 0x0016 }
        goto L_0x000e;
    L_0x0016:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
    L_0x0019:
        r0 = r2.setFunctionCache;	 Catch:{ all -> 0x0016 }
        r0.add(r4, r5);	 Catch:{ all -> 0x0016 }
        goto L_0x000e;
    L_0x001f:
        r0 = r2.setFunctionCache;	 Catch:{ all -> 0x0016 }
        r0 = r0.get(r4);	 Catch:{ all -> 0x0016 }
        r0 = (com.horion.tv.framework.logicapi.framework.TvLogicSetFunction) r0;	 Catch:{ all -> 0x0016 }
        monitor-exit(r2);
        r5 = r0;
        goto L_0x000f;
        */
        ////throw new UnsupportedOperationException("Method not decompiled: com.horion.tv.logic.TvLogicManager.setFunctionCacheOperate(com.horion.tv.logic.TvLogicManager$KtcTvCacheOper, java.lang.String, com.horion.tv.framework.logicapi.framework.TvLogicSetFunction):com.horion.tv.framework.logicapi.framework.TvLogicSetFunction");
        int[] arrar = getktcTvCacheOperSwitchesValues();
        switch (arrar[r3.ordinal()]){
            case 1:
                setFunctionCache.add(r4,r5);
                break;
            case 2:
                setFunctionCache.clear();
                break;
            case 3:
                break;
        }
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private KtcTvApiListener tvApiCallbackCacheOperate(KtcTvCacheOper r3, String r4, KtcTvApiListener r5) {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = -getcom_tianci_tv_logic_TvLogicManager$KtcTvCacheOperSwitchesValues();	 Catch:{ all -> 0x0016 }
        r1 = r3.ordinal();	 Catch:{ all -> 0x0016 }
        r0 = r0[r1];	 Catch:{ all -> 0x0016 }
        switch(r0) {
            case 1: goto L_0x0019;
            case 2: goto L_0x0010;
            case 3: goto L_0x001f;
            default: goto L_0x000e;
        };
    L_0x000e:
        monitor-exit(r2);
    L_0x000f:
        return r5;
    L_0x0010:
        r0 = r2.tvApiCallbackCache;	 Catch:{ all -> 0x0016 }
        r0.clear();	 Catch:{ all -> 0x0016 }
        goto L_0x000e;
    L_0x0016:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
    L_0x0019:
        r0 = r2.tvApiCallbackCache;	 Catch:{ all -> 0x0016 }
        r0.add(r4, r5);	 Catch:{ all -> 0x0016 }
        goto L_0x000e;
    L_0x001f:
        r0 = r2.tvApiCallbackCache;	 Catch:{ all -> 0x0016 }
        r0 = r0.get(r4);	 Catch:{ all -> 0x0016 }
        r0 = (com.horion.tv.framework.api.KtcTvApiListener) r0;	 Catch:{ all -> 0x0016 }
        monitor-exit(r2);
        r5 = r0;
        goto L_0x000f;
        */
       //// throw new UnsupportedOperationException("Method not decompiled: com.horion.tv.logic.TvLogicManager.tvApiCallbackCacheOperate(com.horion.tv.logic.TvLogicManager$KtcTvCacheOper, java.lang.String, com.horion.tv.framework.api.KtcTvApiListener):com.horion.tv.framework.api.KtcTvApiListener");
        return null;
    }

    public Source getCurrentSource() {
        /*if (this.currentSource == null) {
            this.currentSource = ExternalApi.getInstance().systemApi.getCurrentSource();
       }
        return this.currentSource;*/return null;
    }

    ////public void initPrior(Context context, KtcCmdConnectorListener ktcCmdConnectorListener) {
    public void initPrior(Context context) {
        this.mContext = context;
        ///this.listener = ktcCmdConnectorListener;
        getFunctionCacheOperate(KtcTvCacheOper.CLEAR, null, null);
        setFunctionCacheOperate(KtcTvCacheOper.CLEAR, null, null);
        tvApiCallbackCacheOperate(KtcTvCacheOper.CLEAR, null, null);
       ////EPGLoader.getInstance().clear();
       //// ExternalApi.getInstance().init(ktcCmdConnectorListener, context);
        tvLogicFistInitMap.clear();
        this.tvLogicFistInitMap.put(LOGIC_NAME_ENUM.SYSTEMLOGIC, SystemLogic.getInstance());
        ////NoSignalManager.getInstance().init(context);
        Log.d("Maxs","TvLogicManager:initPrior------------------------------------");
        initTvLogicData(this.tvLogicFistInitMap);
    }

    ////public void initRemain(Context context, KtcCmdConnectorListener ktcCmdConnectorListener) {
    public void initRemain(Context context) {
        tvLogicRestInitMap.clear();
        this.tvLogicRestInitMap.put(LOGIC_NAME_ENUM.ATVLOGIC, ATVLogic.getInstance());
        this.tvLogicRestInitMap.put(LOGIC_NAME_ENUM.AVLOGIC, AVLogic.getInstance());
        ////this.tvLogicRestInitMap.put(LOGIC_NAME_ENUM.DVBCLOGIC, DVBCLogic.getInstance());
        this.tvLogicRestInitMap.put(LOGIC_NAME_ENUM.DTMBLOGIC, DTMBLogic.getInstance());
        this.tvLogicRestInitMap.put(LOGIC_NAME_ENUM.HDMILOGIC, HDMILogic.getInstance());
        this.tvLogicRestInitMap.put(LOGIC_NAME_ENUM.YUVLOGIC, YUVLogic.getInstance());
        ////this.tvLogicRestInitMap.put(LOGIC_NAME_ENUM.EPGLOGIC, EPGLogic.getInstance());
        ////this.tvLogicRestInitMap.put(LOGIC_NAME_ENUM.VGALOGIC, VGALogic.getInstance());
        Log.d("Maxs","----------------->TvLogicManager:initRemain:tvLogicRestInitMap.size() = " + tvLogicRestInitMap.size());
        initTvLogicData(this.tvLogicRestInitMap);
        ////this.currentSource = ExternalApi.getInstance().systemApi.getCurrentSource();
        ////SwitchChannelManagerL.getInstance().init();
        ////SwitchChannelManagerL.getInstance().setCurrentChannel(SwitchChannelManagerL.getInstance().getCurrentChannel());
        TvLogicBroadcast.getInstance().init(context);
        logicApiInit();
        ////KtcTVDebug.debug("initRemain end");
    }

    public void initTvLogicData(HashMap<LOGIC_NAME_ENUM, TvLogic> hashMap) {
        synchronized (this) {
           // Log.d("Maxs","initTvLogicData:---------------------------");\
            Log.d("Maxs","initTvLogicData:hashMap.entrySet().size() = " + hashMap.entrySet().size());
            for (Entry entry : hashMap.entrySet()) {
                ///Log.d("Maxs","initTvLogicData:----------1111-----------------");
                ///Log.d("Maxs",getClass().getName() + " initTvLogicData logicNameEnum:<<<<<" + ((LOGIC_NAME_ENUM) entry.getKey()) + ">>>>>");
                ////KtcTVDebug.debug(getClass().getName() + " initTvLogicData logicNameEnum:<<<<<" + ((LOGIC_NAME_ENUM) entry.getKey()) + ">>>>>");
                TvLogic tvLogic = (TvLogic) entry.getValue();
                ///Log.d("Maxs","------------------>" + tvLogic.toString() + " tvLogic ==null = " + (tvLogic == null));
                tvLogic.init(this.mContext, this.listener);
                ////Log.d("Maxs","----------------------------------------------------------------------------");
                List<TvLogicGetFunction> getFunctions = tvLogic.getGetFunctions();
                ////KtcTVDebug.debug(getClass().getName() + " logicGetfunction getList size:<<<<<" + getFunctions.size() + ">>>>>");
                if (getFunctions != null) {
                    Log.d("Maxs","initTvLogicData:getFunctions.size() = " + getFunctions.size());
                    for (TvLogicGetFunction tvLogicGetFunction : getFunctions) {
                        ////Log.d("Maxs",getClass().getName() + " KtcTvCacheOper.ADD logicGetfunction:<<<<<" + tvLogicGetFunction.cmd + ">>>>>");
                        ////KtcTVDebug.debug(getClass().getName() + " KtcTvCacheOper.ADD logicGetfunction:<<<<<" + tvLogicGetFunction.cmd + ">>>>>");
                        getFunctionCacheOperate(KtcTvCacheOper.ADD, tvLogicGetFunction.cmd, tvLogicGetFunction);
                    }
                }
                Iterable<TvLogicSetFunction> setFunctions = tvLogic.getSetFunctions();
                ////KtcTVDebug.debug(getClass().getName() + "logicSetfunction size:" + getFunctions.size() + ">>>>>");
                if (setFunctions != null) {
                    for (TvLogicSetFunction tvLogicSetFunction : setFunctions) {
                        ///Log.d("Maxs",getClass().getName() + " KtcTvCacheOper.ADD logicSetfunction:<<<<<" + tvLogicSetFunction.cmd + ">>>>>");
                        ////KtcTVDebug.debug(getClass().getName() + " KtcTvCacheOper.ADD logicSetfunction:<<<<<" + tvLogicSetFunction.cmd + ">>>>>");
                        setFunctionCacheOperate(KtcTvCacheOper.ADD, tvLogicSetFunction.cmd, tvLogicSetFunction);
                    }
                }
                try {
                    Iterable<KtcTvApiListener> tvApiListeners = tvLogic.getTvApiListeners();
                    if (tvApiListeners != null) {
                        for (KtcTvApiListener ktcTvApiListener : tvApiListeners) {
                            String[] handleCmds = ktcTvApiListener.getHandleCmds();
                            if (handleCmds != null) {
                                for (String tvApiCallbackCacheOperate : handleCmds) {
                                    tvApiCallbackCacheOperate(KtcTvCacheOper.ADD, tvApiCallbackCacheOperate, ktcTvApiListener);
                                }
                                continue;
                            }
                        }
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ////KtcTVDebug.error("TvLogicManager TVService has problem!!!!!!");
                }
            }
        }
        return;
    }

    ////public boolean isEpgSource(Source source) {
     ////   return source == null ? false : (source.equals(Source.DVBC()) || source.equals(Source.DTMB()) || source.equals(Source.IPLive()) || source.getSourceNameEnum() == SOURCE_NAME_ENUM.EXTERNAL) ? true : source.equals(Source.ATV()) ? false : false;
   //// }

    ////public void setCurrentSource(Source source) {
    ////    this.currentSource = source;
    ////}

   //// public void updateCurrentSourceEPG() {
    ////    EPGLoader.getInstance().clear(this.currentSource);
    ////    EPGLoader.getInstance().getChannelList(this.currentSource);
    ////    SwitchChannelManagerL.getInstance().setCurrentChannel(ExternalApi.getInstance().systemApi.getCurrentChannel());
   //// }
}
