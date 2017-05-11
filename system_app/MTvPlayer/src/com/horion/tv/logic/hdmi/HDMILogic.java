package com.horion.tv.logic.hdmi;

import android.content.Context;
import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
import com.horion.tv.framework.logicapi.framework.HDMILogicApi;
import com.horion.tv.logic.framework.TvLogic;

public class HDMILogic extends TvLogic implements HDMILogicApi {
    private static HDMILogic instance = null;

    public static HDMILogic getInstance() {
        if (instance == null) {
            instance = new HDMILogic();
        }
        return instance;
    }

    public void init(Context context, KtcCmdConnectorListener ktcCmdConnectorListener) {
        super.init(context, ktcCmdConnectorListener);
    }
}
