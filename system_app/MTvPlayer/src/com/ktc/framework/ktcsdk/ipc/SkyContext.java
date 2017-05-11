package com.ktc.framework.ktcsdk.ipc;

import android.content.Context;

import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
///import com.ktc.framework.skysdk.logger.SkyLogger;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class SkyContext {
    public static Context context;
   //// public static IIPCConnector ipcService = null;
    private static HashMap<String, WeakReference<KtcCmdConnectorListener>> listeners = new HashMap();

    public static Context getContext() {
        return context;
    }

   //// public static IIPCConnector getIPCService() {
    ///    return ipcService;
  ///  }

    public static KtcCmdConnectorListener getListener() {
        for (WeakReference weakReference : listeners.values()) {
            if (weakReference.get() != null) {
                return (KtcCmdConnectorListener) weakReference.get();
            }
        }
        return null;
    }

    public static void setCmdConnectorListener(KtcCmdConnectorListener skyCmdConnectorListener) {
       /// SkyLogger.d("CmdConnectorListener", "set listener=" + skyCmdConnectorListener);
        listeners.put(skyCmdConnectorListener.getCmdClassName(), new WeakReference(skyCmdConnectorListener));
    }
}
