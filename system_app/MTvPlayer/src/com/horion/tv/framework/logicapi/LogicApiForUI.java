package com.horion.tv.framework.logicapi;

import android.content.Context;
import com.horion.tv.framework.logicapi.framework.ATVLogicApi;
import com.horion.tv.framework.logicapi.framework.AVLogicApi;
import com.horion.tv.framework.logicapi.framework.DTMBLogicApi;
////import com.horion.tv.framework.logicapi.framework.DVBCLogicApi;
////import com.horion.tv.framework.logicapi.framework.EPGLogicApi;
import com.horion.tv.framework.logicapi.framework.HDMILogicApi;
import com.horion.tv.framework.logicapi.framework.IPLiveLogicApi;
import com.horion.tv.framework.logicapi.framework.SystemLogicApi;
import com.horion.tv.framework.logicapi.framework.VGALogicApi;
import com.horion.tv.framework.logicapi.framework.YUVLogicApi;
////import com.horion.tv.utils.KtcTVDebug;

public class LogicApiForUI {
    private static LogicApiForUI instance = null;
    private ATVLogicApi aTVLogicApi = null;
    private AVLogicApi aVLogicApi = null;
    private DTMBLogicApi dTMBLogicApi = null;
    ////private DVBCLogicApi dVBCLogicApi = null;
    ////private EPGLogicApi ePGLogicApi = null;
    private HDMILogicApi hDMILogicApi = null;
    private IPLiveLogicApi iPLiveLogicApi = null;
    private Context mContext = null;
    private SystemLogicApi systemLogicApi = null;
    private VGALogicApi vGALogicApi = null;
    private YUVLogicApi yUVLogicApi = null;

    public static LogicApiForUI getInstance() {
        if (instance == null) {
            instance = new LogicApiForUI();
        }
        return instance;
    }

    public ATVLogicApi getATVLogicApi() {
        return this.aTVLogicApi;
    }

    public AVLogicApi getAVLogicApi() {
        return this.aVLogicApi;
    }

    public DTMBLogicApi getDTMBLogicApi() {
        return this.dTMBLogicApi;
    }

    ////public DVBCLogicApi getDVBCLogicApi() {
   ////     return this.dVBCLogicApi;
    ////}

    ////public EPGLogicApi getEPGLogicApi() {
    ////    return this.ePGLogicApi;
   //// }

    public HDMILogicApi getHDMILogicApi() {
        return this.hDMILogicApi;
    }

    public IPLiveLogicApi getIPLiveLogicApi() {
        return this.iPLiveLogicApi;
    }

    public SystemLogicApi getSystemLogicApi() {
        return this.systemLogicApi;
    }

    public VGALogicApi getVGALogicApi() {
        return this.vGALogicApi;
    }

    public YUVLogicApi getYUVLogicApi() {
        return this.yUVLogicApi;
    }

    public void init(Context context) {
        ////KtcTVDebug.debug("~~~~~~~~~~~~~LogicApiForUI init~~~~~~~");
        this.mContext = context;
    }

    public void setATVLogicApi(ATVLogicApi aTVLogicApi) {
        this.aTVLogicApi = aTVLogicApi;
    }

    public void setAVLogicApi(AVLogicApi aVLogicApi) {
        this.aVLogicApi = aVLogicApi;
    }

    public void setDTMBLogicApi(DTMBLogicApi dTMBLogicApi) {
        this.dTMBLogicApi = dTMBLogicApi;
    }

    ////public void setDVBCLogicApi(DVBCLogicApi dVBCLogicApi) {
     ////   this.dVBCLogicApi = dVBCLogicApi;
    /////}

   ///// public void setEPGLogicApi(EPGLogicApi ePGLogicApi) {
     ////   this.ePGLogicApi = ePGLogicApi;
   //// }

    public void setHDMILogicApi(HDMILogicApi hDMILogicApi) {
        this.hDMILogicApi = hDMILogicApi;
    }

    public void setIPLiveLogicApi(IPLiveLogicApi iPLiveLogicApi) {
        this.iPLiveLogicApi = iPLiveLogicApi;
    }

    public void setSystemLogicApi(SystemLogicApi systemLogicApi) {
        this.systemLogicApi = systemLogicApi;
    }

    public void setVGALogicApi(VGALogicApi vGALogicApi) {
        this.vGALogicApi = vGALogicApi;
    }

    public void setYUVLogicApi(YUVLogicApi yUVLogicApi) {
        this.yUVLogicApi = yUVLogicApi;
    }
}
