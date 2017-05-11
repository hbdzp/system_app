package com.horion.tv.logic;

////import com.ktc.framework.ktcsdk.ipc.KtcCmdURI;
////import com.horion.de.api.KtcDEApi;
////import com.horion.media.api.KtcMediaApi;
////import com.horion.net.api.NetApiForCommon;
////import com.horion.system.api.TCSystemService;
////import com.horion.tv.api.tvApiforLogic.atv.KtcTvLogicATVApi;
////import com.horion.tv.api.tvApiforLogic.av.KtcTvLogicAvApi;
////import com.horion.tv.api.tvApiforLogic.dtmb.KtcTvLogicDTMBApi;
////import com.horion.tv.api.tvApiforLogic.dvbc.KtcTvLogicDVBCApi;
////import com.horion.tv.api.tvApiforLogic.epg.KtcTvLogicEPGApi;
////import com.horion.tv.api.tvApiforLogic.hdmi.KtcTvLogicHDMIApi;
////import com.horion.tv.api.tvApiforLogic.setting.KtcTvLogicSettingApi;
////import com.horion.tv.api.tvApiforLogic.system.KtcTvLogicSystemApi;
////import com.horion.tv.api.tvApiforLogic.vga.KtcTvLogicVGAApi;
////import com.horion.tv.api.tvApiforLogic.yuv.KtcTvLogicYUVApi;
////import com.horion.tv.service.epg.OpenEPGActivityApi;

public class ExternalApi {
    /*
    private static ExternalApi instance = null;

    public KtcTvLogicATVApi atvApi = null;
    public KtcTvLogicAvApi avApi = null;
    public KtcDEApi deApi = null;
    public KtcTvLogicDTMBApi dtmbApi = null;
    public KtcTvLogicDVBCApi dvbcApi = null;
    public KtcTvLogicEPGApi epgApi = null;
    public KtcTvLogicHDMIApi hdmiApi = null;
    private KtcCmdConnectorListener listener = null;
    public KtcMediaApi mediaApi = null;
    public NetApiForCommon netCommonApi = null;
    public OpenEPGActivityApi openEPGActivityApi = null;
    public KtcTvLogicSettingApi settingApi = null;
    public KtcTvLogicSystemApi systemApi = null;
    public TCSystemService systemServiceApi = null;
    public KtcTvLogicVGAApi vgaApi = null;
    public KtcTvLogicYUVApi yuvApi = null;

    private ExternalApi() {
    }

    public static ExternalApi getInstance() {
        if (instance == null) {
            instance = new ExternalApi();
        }
        return instance;
    }

    ////public void execCommand(KtcCmdURI ktcCmdURI, byte[] bArr) {
     ////   KtcApplication.getApplication().execCommand(this.listener, ktcCmdURI, bArr);
   //// }

    public void init(KtcCmdConnectorListener ktcCmdConnectorListener, Context context) {

        this.listener = ktcCmdConnectorListener;
        this.systemApi = new KtcTvLogicSystemApi(ktcCmdConnectorListener);
        this.settingApi = new KtcTvLogicSettingApi(ktcCmdConnectorListener);
        this.atvApi = new KtcTvLogicATVApi(ktcCmdConnectorListener);
        this.epgApi = new KtcTvLogicEPGApi(ktcCmdConnectorListener);
        this.dvbcApi = new KtcTvLogicDVBCApi(ktcCmdConnectorListener);
        this.dtmbApi = new KtcTvLogicDTMBApi(ktcCmdConnectorListener);
        this.mediaApi = new KtcMediaApi(ktcCmdConnectorListener);
        this.avApi = new KtcTvLogicAvApi(ktcCmdConnectorListener);
        this.vgaApi = new KtcTvLogicVGAApi(ktcCmdConnectorListener);
        this.yuvApi = new KtcTvLogicYUVApi(ktcCmdConnectorListener);
        this.deApi = new KtcDEApi(ktcCmdConnectorListener);
        this.hdmiApi = new KtcTvLogicHDMIApi(ktcCmdConnectorListener);
        this.systemServiceApi = new TCSystemService(ktcCmdConnectorListener);
        this.netCommonApi = new NetApiForCommon(ktcCmdConnectorListener);
        this.openEPGActivityApi = new OpenEPGActivityApi(ktcCmdConnectorListener, context);

    }
    */
    private static ExternalApi instance = null;
    private ExternalApi() {
    }

    public static ExternalApi getInstance() {
        if (instance == null) {
            instance = new ExternalApi();
        }
        return instance;
    }
}
