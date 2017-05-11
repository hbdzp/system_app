package com.horion.tv.logic.yuv;

import android.content.Context;
import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
import com.horion.tv.framework.logicapi.framework.YUVLogicApi;
import com.horion.tv.logic.framework.TvLogic;

public class YUVLogic extends TvLogic implements YUVLogicApi {
    private static YUVLogic instance = null;

    public static YUVLogic getInstance() {
        if (instance == null) {
            instance = new YUVLogic();
        }
        return instance;
    }

    public void init(Context context, KtcCmdConnectorListener ktcCmdConnectorListener) {
        super.init(context, ktcCmdConnectorListener);
    }
}
