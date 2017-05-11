package com.ktc.framework.ktcsdk.ipc;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import com.horion.tv.service.epg.SkyLeftEPGDataGet;
import com.horion.tv.ui.search.LittleDownTimer;
import com.horion.tv.ui.search.TimeZoneSetter;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvLanguage;

import java.util.Locale;
////import com.ktc.framework.ktcsdk.crashhandler.TCLogCrashHandler;
////import com.ktc.framework.ktcsdk.crashhandler.TCLogCrashHandler.UserCrashListener;
////import com.ktc.framework.ktcsdk.ipc.IIPCConnector.Stub;
////import com.ktc.framework.ktcsdk.ipc.KtcCmdHandler.KtcCmdHandlerListener;
////import com.ktc.framework.ktcsdk.ipc.KtcCmdHandler.KtcCmdResListener;
////import com.ktc.framework.ktcsdk.ipc.KtcCmdURI.KtcCmdPathErrorException;
/////import com.ktc.framework.ktcsdk.logger.KtcLogger;
/////import com.ktc.framework.ktcsdk.schema.Priority;
////import com.ktc.framework.ktcsdk.schema.KtcCmdHeader;


/////public class KtcApplication extends Application implements KtcCmdResListener, KtcCmdHandlerListener, ICmdBaseProceess {
public class KtcApplication extends Application {
   //// private static /* synthetic */ int[] -com_ktcworth_framework_ktcsdk_ipc_KtcStateEnumSwitchesValues;
    /*
    private static KtcApplication application;
    private static ICmdBaseProceess cmdprocess;
    private static Priority priority = Priority.MIN;
    private BindWatchDog bindWatchDog;
    private KtcCmdHandler cmdHandler = null;
    private String fakePackageName = null;
    private boolean isCmdConnectorInit = false;
    private TCLogCrashHandler logCrashHandler;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            KtcApplication.this.bindWatchDog.stopWatchDog();
            KtcContext.ipcService = Stub.asInterface(iBinder);
            try {
                int transportID = KtcContext.ipcService.getTransportID();
                KtcApplication.this.cmdHandler = new KtcCmdHandler(transportID);
                KtcApplication.this.cmdHandler.setListener(KtcApplication.this);
                KtcLogger.d("application", "fakepackage=" + KtcApplication.this.getFakePackageName());
                KtcContext.ipcService.addReceiver(transportID, KtcApplication.this.getModulePackage(), KtcCmdTransporterIPC.getInstance().getIPCReceiver());
                KtcApplication.this.isCmdConnectorInit = true;
                KtcApplication.this.dispatchCmdConnectInit();
            } catch (RemoteException e) {
                KtcLogger.e("TIANCI", "bind service throws  RemoteException " + e.getMessage());
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            try {
                if (KtcContext.ipcService != null && KtcApplication.this.cmdHandler != null) {
                    KtcContext.ipcService.removeReceiver(KtcApplication.this.cmdHandler.getTransportId());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private ConcurrentHashMap<String, WeakReference<KtcActivity>> ktcActivityCollector = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Boolean> ktcActivityStatusMap = new ConcurrentHashMap();
    private ConcurrentHashMap<String, WeakReference<KtcService>> ktcServiceCollector = new ConcurrentHashMap();
*/
    public interface KtcCmdConnectorListener {
        String getCmdClassName();

        byte[] onHandler(String str, String str2, byte[] bArr);

        void onResult(String str, String str2, byte[] bArr);

        @Deprecated
        byte[] requestPause(String str, String str2, byte[] bArr);

        @Deprecated
        byte[] requestRelease(String str, String str2, byte[] bArr);

        @Deprecated
        byte[] requestResume(String str, String str2, byte[] bArr);

        @Deprecated
        byte[] requestStartToForground(String str, String str2, byte[] bArr);

        @Deprecated
        byte[] requestStartToVisible(String str, String str2, byte[] bArr);
    }
/*
    static final class BindWatchDog implements Runnable {
        int THREE_SECOND = 3000;
        int TIMEOUT_SECOND = 5000;
        WeakReference<ServiceConnection> connRef;
        boolean isBindSuccess = false;
        private Handler watchDogHandler = new Handler();

        public BindWatchDog(ServiceConnection serviceConnection) {
            this.connRef = new WeakReference(serviceConnection);
        }

        public void run() {
            if (!this.isBindSuccess) {
                Intent intent = new Intent();
                intent.setClassName("com.horion.ipc", "com.ktc.tvos.ipcservice.KtcIPCService");
                if (this.connRef != null && this.connRef.get() != null) {
                    boolean bindService = KtcApplication.getInstance().bindService(intent, (ServiceConnection) this.connRef.get(), 1);
                    ////KtcLogger.i("TIANCI", "bind service success? : " + bindService);
                    if (bindService) {
                        ////KtcLogger.i("TIANCI", "bind succee and start bind service timeout watchdog: " + KtcApplication.getInstance().getPackageName());
                        this.watchDogHandler.postDelayed(this, (long) this.TIMEOUT_SECOND);
                        return;
                    }
                    this.watchDogHandler.postDelayed(this, (long) this.THREE_SECOND);
                }
            }
        }

        public void startWatchDog() {
            this.watchDogHandler.post(this);
        }

        public void stopWatchDog() {
            this.isBindSuccess = true;
            this.watchDogHandler.removeCallbacks(this);
        }
    }

    public class KtcActivityLifecycle implements ActivityLifecycleCallbacks {
        public void onActivityCreated(Activity activity, Bundle bundle) {
            if (activity instanceof KtcActivity) {
                KtcApplication.this.ktcActivityCollector.put(activity.getClass().getName(), new WeakReference((KtcActivity) activity));
                KtcApplication.this.ktcActivityStatusMap.put(activity.getClass().getName(), Boolean.valueOf(true));
                if (KtcApplication.this.isCmdConnectorInit) {
                    ((KtcActivity) activity).onCmdConnectorInit();
                }
            }
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
            if (activity.isFinishing() && (activity instanceof KtcActivity)) {
                KtcApplication.this.ktcActivityCollector.remove(activity.getClass().getName());
                KtcApplication.this.ktcActivityStatusMap.put(activity.getClass().getName(), Boolean.valueOf(false));
            }
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
            KtcApplication.this.ktcActivityStatusMap.put(activity.getClass().getName(), Boolean.valueOf(true));
        }

        public void onActivityStopped(Activity activity) {
            KtcApplication.this.ktcActivityStatusMap.put(activity.getClass().getName(), Boolean.valueOf(false));
        }
    }
*/
   //// private static /* synthetic */ int[] -getcom_ktcworth_framework_ktcsdk_ipc_KtcStateEnumSwitchesValues() {
    /*
        if (-com_ktcworth_framework_ktcsdk_ipc_KtcStateEnumSwitchesValues != null) {
            return -com_ktcworth_framework_ktcsdk_ipc_KtcStateEnumSwitchesValues;
        }
        int[] iArr = new int[KtcStateEnum.values().length];
        try {
            iArr[KtcStateEnum.pause.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[KtcStateEnum.release.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[KtcStateEnum.restart_to_forground.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[KtcStateEnum.restart_to_visible.ordinal()] = 4;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[KtcStateEnum.resume.ordinal()] = 5;
        } catch (NoSuchFieldError e5) {
        }
        -com_ktcworth_framework_ktcsdk_ipc_KtcStateEnumSwitchesValues = iArr;
        return iArr;
    }
    */
/*
    private void clearAllCmdConllector() {
        this.ktcActivityCollector.clear();
        this.ktcServiceCollector.clear();
        this.ktcActivityStatusMap.clear();
    }

    private void dispatchCmdConnectInit() {
        for (String str : this.ktcActivityCollector.keySet()) {
            if (!this.ktcActivityStatusMap.containsKey(str)) {
                KtcLogger.i("APPLICATION", "dispatchCmdConnectInit " + str + " false because ktcActivityStatusMap.containsKey(key) false");
            } else if (((WeakReference) this.ktcActivityCollector.get(str)).get() == null || !((Boolean) this.ktcActivityStatusMap.get(str)).booleanValue()) {
                KtcLogger.i("APPLICATION", "dispatchCmdConnectInit " + str + " false because ktcActivityStatusMap.get(key).booleanValue() false");
            } else {
                ((KtcActivity) ((WeakReference) this.ktcActivityCollector.get(str)).get()).onCmdConnectorInit();
            }
        }
        for (String str2 : this.ktcServiceCollector.keySet()) {
            if (((WeakReference) this.ktcServiceCollector.get(str2)).get() != null) {
                ((KtcService) ((WeakReference) this.ktcServiceCollector.get(str2)).get()).onCmdConnectorInit();
            }
        }
    }

    private KtcCmd execCommand(int i, KtcCmd ktcCmd) {
        return this.cmdHandler != null ? this.cmdHandler.execCommand(i, ktcCmd) : null;
    }

    public static ICmdBaseProceess getApplication() {
        return cmdprocess;
    }

    public static KtcApplication getInstance() {
        return application;
    }

    private String getModulePackage() {
        return this.fakePackageName == null ? getPackageName() : this.fakePackageName;
    }

    private void initKtcCmdConnector() {
        this.bindWatchDog = new BindWatchDog(this.mConnection);
        this.bindWatchDog.startWatchDog();
    }

    private void onBroadcast(String str, String str2, byte[] bArr) {
        for (String str3 : this.ktcActivityCollector.keySet()) {
            if (!this.ktcActivityStatusMap.containsKey(str3)) {
                KtcLogger.i("APPLICATION", "onBroadcast not return because ktcActivityStatusMap.containsKey(key) == false");
            } else if (((WeakReference) this.ktcActivityCollector.get(str3)).get() != null && ((Boolean) this.ktcActivityStatusMap.get(str3)).booleanValue()) {
                try {
                    ((KtcActivity) ((WeakReference) this.ktcActivityCollector.get(str3)).get()).onHandler(str, str2, bArr);
                } catch (Exception e) {
                    KtcLogger.e("TIANCI", str3 + " process broadcast received exception!" + e.toString());
                }
            }
        }
        for (String str32 : this.ktcServiceCollector.keySet()) {
            try {
                if (((WeakReference) this.ktcServiceCollector.get(str32)).get() != null) {
                    ((KtcService) ((WeakReference) this.ktcServiceCollector.get(str32)).get()).onHandler(str, str2, bArr);
                }
            } catch (Exception e2) {
                KtcLogger.e("TIANCI", str32 + " process broadcast received exception!" + e2.toString());
            }
        }
    }
*/
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] onHandler(String r9, String r10, String r11, byte[] r12) {
        /*
        r8 = this;
        r4 = 0;
        r3 = 0;
        r0 = r8.ktcActivityCollector;
        r0 = r0.keySet();
        r1 = r0.iterator();
    L_0x000c:
        r0 = r1.hasNext();
        if (r0 == 0) goto L_0x01a4;
    L_0x0012:
        r0 = r1.next();
        r0 = (java.lang.String) r0;
        r2 = r0.equals(r9);
        if (r2 == 0) goto L_0x000c;
    L_0x001e:
        r5 = com.ktc.framework.ktcsdk.ipc.KtcStateEnum.values();
        r6 = r5.length;
        r2 = r4;
    L_0x0024:
        if (r2 >= r6) goto L_0x02d7;
    L_0x0026:
        r1 = r5[r2];
        r7 = r1.toString();
        r7 = r7.equals(r11);
        if (r7 == 0) goto L_0x0043;
    L_0x0032:
        r2 = r1;
    L_0x0033:
        r1 = r8.ktcActivityStatusMap;
        r1 = r1.containsKey(r0);
        if (r1 != 0) goto L_0x0047;
    L_0x003b:
        r0 = "APPLICATION";
        r1 = "onHandler not return because ktcActivityStatusMap.containsKey(key) == false";
        com.ktc.framework.ktcsdk.logger.KtcLogger.i(r0, r1);
    L_0x0042:
        return r3;
    L_0x0043:
        r1 = r2 + 1;
        r2 = r1;
        goto L_0x0024;
    L_0x0047:
        if (r2 != 0) goto L_0x0078;
    L_0x0049:
        r1 = r8.ktcActivityCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x0078;
    L_0x0057:
        r1 = r8.ktcActivityStatusMap;
        r1 = r1.get(r0);
        r1 = (java.lang.Boolean) r1;
        r1 = r1.booleanValue();
        if (r1 == 0) goto L_0x0078;
    L_0x0065:
        r1 = r8.ktcActivityCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcActivity) r0;
        r3 = r0.onHandler(r10, r11, r12);
        goto L_0x0042;
    L_0x0078:
        r1 = -getcom_ktcworth_framework_ktcsdk_ipc_KtcStateEnumSwitchesValues();
        r2 = r2.ordinal();
        r1 = r1[r2];
        switch(r1) {
            case 1: goto L_0x00b4;
            case 2: goto L_0x0114;
            case 3: goto L_0x0174;
            case 4: goto L_0x0144;
            case 5: goto L_0x00e4;
            default: goto L_0x0085;
        };
    L_0x0085:
        r1 = r8.ktcActivityCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x01a4;
    L_0x0093:
        r1 = r8.ktcActivityStatusMap;
        r1 = r1.get(r0);
        r1 = (java.lang.Boolean) r1;
        r1 = r1.booleanValue();
        if (r1 == 0) goto L_0x01a4;
    L_0x00a1:
        r1 = r8.ktcActivityCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcActivity) r0;
        r3 = r0.onHandler(r10, r11, r12);
        goto L_0x0042;
    L_0x00b4:
        r1 = r8.ktcActivityCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x00e4;
    L_0x00c2:
        r1 = r8.ktcActivityStatusMap;
        r1 = r1.get(r0);
        r1 = (java.lang.Boolean) r1;
        r1 = r1.booleanValue();
        if (r1 == 0) goto L_0x00e4;
    L_0x00d0:
        r1 = r8.ktcActivityCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcActivity) r0;
        r3 = r0.requestPause(r10, r11, r12);
        goto L_0x0042;
    L_0x00e4:
        r1 = r8.ktcActivityCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x0114;
    L_0x00f2:
        r1 = r8.ktcActivityStatusMap;
        r1 = r1.get(r0);
        r1 = (java.lang.Boolean) r1;
        r1 = r1.booleanValue();
        if (r1 == 0) goto L_0x0114;
    L_0x0100:
        r1 = r8.ktcActivityCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcActivity) r0;
        r3 = r0.requestResume(r10, r11, r12);
        goto L_0x0042;
    L_0x0114:
        r1 = r8.ktcActivityCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x0144;
    L_0x0122:
        r1 = r8.ktcActivityStatusMap;
        r1 = r1.get(r0);
        r1 = (java.lang.Boolean) r1;
        r1 = r1.booleanValue();
        if (r1 == 0) goto L_0x0144;
    L_0x0130:
        r1 = r8.ktcActivityCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcActivity) r0;
        r3 = r0.requestRelease(r10, r11, r12);
        goto L_0x0042;
    L_0x0144:
        r1 = r8.ktcActivityCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x0174;
    L_0x0152:
        r1 = r8.ktcActivityStatusMap;
        r1 = r1.get(r0);
        r1 = (java.lang.Boolean) r1;
        r1 = r1.booleanValue();
        if (r1 == 0) goto L_0x0174;
    L_0x0160:
        r1 = r8.ktcActivityCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcActivity) r0;
        r3 = r0.requestStartToVisible(r10, r11, r12);
        goto L_0x0042;
    L_0x0174:
        r1 = r8.ktcActivityCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x0085;
    L_0x0182:
        r1 = r8.ktcActivityStatusMap;
        r1 = r1.get(r0);
        r1 = (java.lang.Boolean) r1;
        r1 = r1.booleanValue();
        if (r1 == 0) goto L_0x0085;
    L_0x0190:
        r1 = r8.ktcActivityCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcActivity) r0;
        r3 = r0.requestStartToForground(r10, r11, r12);
        goto L_0x0042;
    L_0x01a4:
        r0 = r8.ktcServiceCollector;
        r0 = r0.keySet();
        r1 = r0.iterator();
    L_0x01ae:
        r0 = r1.hasNext();
        if (r0 == 0) goto L_0x0042;
    L_0x01b4:
        r0 = r1.next();
        r0 = (java.lang.String) r0;
        r2 = r0.equals(r9);
        if (r2 == 0) goto L_0x01ae;
    L_0x01c0:
        r5 = com.ktc.framework.ktcsdk.ipc.KtcStateEnum.values();
        r6 = r5.length;
        r1 = r4;
    L_0x01c6:
        if (r1 >= r6) goto L_0x02d4;
    L_0x01c8:
        r2 = r5[r1];
        r4 = r2.toString();
        r4 = r4.equals(r11);
        if (r4 == 0) goto L_0x01f8;
    L_0x01d4:
        if (r2 != 0) goto L_0x01fb;
    L_0x01d6:
        r1 = r8.ktcServiceCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x01fb;
    L_0x01e4:
        r1 = r8.ktcServiceCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcService) r0;
        r3 = r0.onHandler(r10, r11, r12);
        goto L_0x0042;
    L_0x01f8:
        r1 = r1 + 1;
        goto L_0x01c6;
    L_0x01fb:
        r1 = -getcom_ktcworth_framework_ktcsdk_ipc_KtcStateEnumSwitchesValues();
        r2 = r2.ordinal();
        r1 = r1[r2];
        switch(r1) {
            case 1: goto L_0x022a;
            case 2: goto L_0x026e;
            case 3: goto L_0x02b2;
            case 4: goto L_0x0290;
            case 5: goto L_0x024c;
            default: goto L_0x0208;
        };
    L_0x0208:
        r1 = r8.ktcServiceCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x0042;
    L_0x0216:
        r1 = r8.ktcServiceCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcService) r0;
        r3 = r0.onHandler(r10, r11, r12);
        goto L_0x0042;
    L_0x022a:
        r1 = r8.ktcServiceCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x024c;
    L_0x0238:
        r1 = r8.ktcServiceCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcService) r0;
        r3 = r0.requestPause(r10, r11, r12);
        goto L_0x0042;
    L_0x024c:
        r1 = r8.ktcServiceCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x026e;
    L_0x025a:
        r1 = r8.ktcServiceCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcService) r0;
        r3 = r0.requestResume(r10, r11, r12);
        goto L_0x0042;
    L_0x026e:
        r1 = r8.ktcServiceCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x0290;
    L_0x027c:
        r1 = r8.ktcServiceCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcService) r0;
        r3 = r0.requestRelease(r10, r11, r12);
        goto L_0x0042;
    L_0x0290:
        r1 = r8.ktcServiceCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x02b2;
    L_0x029e:
        r1 = r8.ktcServiceCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcService) r0;
        r3 = r0.requestStartToVisible(r10, r11, r12);
        goto L_0x0042;
    L_0x02b2:
        r1 = r8.ktcServiceCollector;
        r1 = r1.get(r0);
        r1 = (java.lang.ref.WeakReference) r1;
        r1 = r1.get();
        if (r1 == 0) goto L_0x0208;
    L_0x02c0:
        r1 = r8.ktcServiceCollector;
        r0 = r1.get(r0);
        r0 = (java.lang.ref.WeakReference) r0;
        r0 = r0.get();
        r0 = (com.ktc.framework.ktcsdk.ipc.KtcService) r0;
        r3 = r0.requestStartToForground(r10, r11, r12);
        goto L_0x0042;
    L_0x02d4:
        r2 = r3;
        goto L_0x01d4;
    L_0x02d7:
        r2 = r3;
        goto L_0x0033;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ktc.framework.ktcsdk.ipc.KtcApplication.onHandler(java.lang.String, java.lang.String, java.lang.String, byte[]):byte[]");
    }
/*
    private void onResult(String str, String str2, String str3, byte[] bArr) {
        for (String str4 : this.ktcActivityCollector.keySet()) {
            if (str4.equals(str)) {
                if (!this.ktcActivityStatusMap.containsKey(str4)) {
                    KtcLogger.i("APPLICATION", "onResult not return because ktcActivityStatusMap.containsKey(key) == false");
                } else if (((WeakReference) this.ktcActivityCollector.get(str4)).get() != null && ((Boolean) this.ktcActivityStatusMap.get(str4)).booleanValue()) {
                    ((KtcActivity) ((WeakReference) this.ktcActivityCollector.get(str4)).get()).onResult(str2, str3, bArr);
                }
                for (String str42 : this.ktcServiceCollector.keySet()) {
                    if (str42.equals(str)) {
                        if (((WeakReference) this.ktcServiceCollector.get(str42)).get() != null) {
                            ((KtcService) ((WeakReference) this.ktcServiceCollector.get(str42)).get()).onResult(str2, str3, bArr);
                            return;
                        }
                        return;
                    }
                }
            }
        }
        for (String str422 : this.ktcServiceCollector.keySet()) {
            if (str422.equals(str)) {
                if (((WeakReference) this.ktcServiceCollector.get(str422)).get() != null) {
                    ((KtcService) ((WeakReference) this.ktcServiceCollector.get(str422)).get()).onResult(str2, str3, bArr);
                    return;
                }
                return;
            }
        }
    }

    private void sendCommand(int i, KtcCmd ktcCmd) {
        if (this.cmdHandler != null) {
            this.cmdHandler.sendCommand(i, ktcCmd, this);
        }
    }

    public static void setCmdBaseProcess(ICmdBaseProceess iCmdBaseProceess) {
        cmdprocess = iCmdBaseProceess;
    }

    public void broadcastCmd(KtcCmdConnectorListener ktcCmdConnectorListener, KtcCmdURI ktcCmdURI, byte[] bArr) {
        if (this.cmdHandler != null && ktcCmdConnectorListener != null) {
            this.cmdHandler.broadcast(new KtcCmd(new KtcCmdHeader("horion://" + getModulePackage() + "/" + ktcCmdConnectorListener.getCmdClassName(), ktcCmdURI.getCmdUrl(), ktcCmdURI.getCmd(), Priority.MID, false, false), bArr));
        }
    }

    public byte[] execCommand(KtcCmdConnectorListener ktcCmdConnectorListener, KtcCmdURI ktcCmdURI, byte[] bArr) {
        if (!(this.cmdHandler == null || ktcCmdConnectorListener == null)) {
            String cmdPackage = ktcCmdURI.getCmdPackage();
            try {
                int targetTransportID = KtcContext.ipcService.getTargetTransportID(cmdPackage);
                KtcLogger.i("TIANCI", "application " + getPackageName() + "execute to " + cmdPackage + "target id=" + targetTransportID);
                if (cmdPackage.contains("com.horion")) {
                    priority = Priority.MAX;
                }
                KtcCmd execCommand = execCommand(targetTransportID, new KtcCmd(new KtcCmdHeader("horion://" + getModulePackage() + "/" + ktcCmdConnectorListener.getCmdClassName(), ktcCmdURI.getCmdUrl(), ktcCmdURI.getCmd(), priority, ktcCmdURI.isCmdNeedAck(), ktcCmdURI.isCmdIsOverride()), bArr));
                if (execCommand != null) {
                    return execCommand.getCmdBody();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getFakePackageName() {
        return this.fakePackageName;
    }

    public void onCreate() {
        KtcLogger.v("WTF", "application reOncreate?");
        registerActivityLifecycleCallbacks(new KtcActivityLifecycle());
        KtcContext.context = getApplicationContext();
        application = this;
        cmdprocess = this;
        initKtcCmdConnector();
        if (this.logCrashHandler == null) {
            this.logCrashHandler = new TCLogCrashHandler();
        }
        this.logCrashHandler.active(getApplicationContext());
        super.onCreate();
    }

    public KtcCmd onHandler(KtcCmd ktcCmd) {
        if (ktcCmd != null) {
            String str = ktcCmd.getCmdHeader().getFrom().toString();
            try {
                KtcCmdURI ktcCmdURI = new KtcCmdURI(ktcCmd.getCmdHeader().getTo().toString());
                if (ktcCmdURI.isBroadcastUri()) {
                    onBroadcast(str, ktcCmdURI.getCmd(), ktcCmd.getCmdBody());
                    return null;
                }
                String targetClassName = ktcCmdURI.getTargetClassName();
                byte[] onHandler = onHandler(targetClassName, str, ktcCmdURI.getCmd(), ktcCmd.getCmdBody());
                if (new KtcCmdURI(str).getCmdPackage().contains("com.horion")) {
                    priority = Priority.MAX;
                }
                return new KtcCmd(new KtcCmdHeader("horion://" + getModulePackage() + "/" + targetClassName, str, ktcCmdURI.getCmd(), priority, false, false), onHandler);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (KtcCmdPathErrorException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public void onResult(KtcCmd ktcCmd) {
        if (ktcCmd != null) {
            try {
                onResult(new KtcCmdURI(ktcCmd.getCmdHeader().getTo().toString()).getTargetClassName(), ktcCmd.getCmdHeader().getFrom().toString(), ktcCmd.getCmdHeader().getCmd().toString(), ktcCmd.getCmdBody());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (KtcCmdPathErrorException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void registerServiceName(KtcService ktcService) {
        this.ktcServiceCollector.put(ktcService.getCmdClassName(), new WeakReference(ktcService));
        if (this.isCmdConnectorInit) {
            ktcService.onCmdConnectorInit();
        }
    }

    public void sendCommand(KtcCmdConnectorListener ktcCmdConnectorListener, KtcCmdURI ktcCmdURI, byte[] bArr) {
        if (this.cmdHandler != null && ktcCmdConnectorListener != null) {
            String cmdPackage = ktcCmdURI.getCmdPackage();
            try {
                int targetTransportID = KtcContext.ipcService.getTargetTransportID(cmdPackage);
                KtcLogger.i("TIANCI", "application " + getPackageName() + "send to " + cmdPackage + "target id=" + targetTransportID);
                if (cmdPackage.contains("com.horion")) {
                    priority = Priority.MAX;
                }
                sendCommand(targetTransportID, new KtcCmd(new KtcCmdHeader("horion://" + getModulePackage() + "/" + ktcCmdConnectorListener.getCmdClassName(), ktcCmdURI.getCmdUrl(), ktcCmdURI.getCmd(), priority, ktcCmdURI.isCmdNeedAck(), ktcCmdURI.isCmdIsOverride()), bArr));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCrashListener(UserCrashListener userCrashListener) {
        if (this.logCrashHandler != null) {
            this.logCrashHandler.setCrashListener(userCrashListener);
        }
    }

    public void setFakePackageName(String str) {
        this.fakePackageName = str;
    }

    public void unRegisterServiceName(KtcService ktcService) {
        this.ktcServiceCollector.remove(ktcService.getCmdClassName());
    }
    */

    private final static String TAG = "TVRootApp";

    public final static int TEXT_STATUS_TTX = 1;

    public final static int TEXT_STATUS_MHEG5 = 2;

    public final static int TEXT_STATUS_HBBTV = 3;

    public final static int TEXT_STATUS_GINGA = 4;

    public final static int TEXT_STATUS_NONE = 5;

    // for hot key
    public static boolean isSoundSettingDirty = false;

    public static boolean isVideoSettingDirty = false;

    public static boolean isPicModeSettingDirty = false;

    public static boolean isFactoryDirty = false;

    // for misc
    public static boolean isMiscSoundDirty = false;

    public static boolean isMiscPictureDirty = false;

    public static boolean isMiscS3DDirty = false;

    public static boolean isLoadDBOver = false;

    public static boolean isSourceDirty = false;
    public static boolean isSourceDirtyPre = false;

    private boolean isPVREnable = false;

    private boolean isTTXEnable = false;

    private RootBroadCastReceiver rootBroadCastReceiver;
    private static Context mContext;

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        };
    };

    /**
     * start modefied by jachensy 2012-6-28
     */
    Runnable run = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isLoadDBOver = true;
            rootBroadCastReceiver = new RootBroadCastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH");
            registerReceiver(rootBroadCastReceiver, filter);
            initLittleDownCounter();
            Looper.loop();
        }
    };

    /**
     * end modefied by jachensy 2012-6-28
     */
    @Override
    public void onCreate() {
        super.onCreate();
        initConfigData();
        mContext = this;
        /**
         * start modefied by jachensy 2012-6-28
         */
        Thread t = new Thread(run);
        t.start();
        new TimeZoneSetter(this).updateTimeZone();
        TvCommonManager.getInstance().disableTvosIr();
        SkyLeftEPGDataGet.getInstance().init(this);
    }
    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onTerminate() {
        this.unregisterReceiver(rootBroadCastReceiver);
        super.onTerminate();
    }

    private void initConfigData() {
        Cursor snconfig = getContentResolver().query(
                Uri.parse("content://mstar.tv.usersetting/snconfig"), null,
                null, null, null);
        if (snconfig != null) {
            if (snconfig.moveToFirst()) {
                isPVREnable = snconfig.getInt(snconfig
                        .getColumnIndex("PVR_ENABLE")) == 1 ? true : false;
                isTTXEnable = snconfig.getInt(snconfig
                        .getColumnIndex("TTX_ENABLE")) == 1 ? true : false;
            }
            snconfig.close();
        } else {
            isPVREnable = true;
            isTTXEnable = true;
        }
    }

    public boolean isPVREnable() {
        return isPVREnable;
    }

    public boolean isTTXEnable() {
        return isTTXEnable;
    }

    public void initLanguageCfig() {
        int languageindex = TvCommonManager.getInstance().getOsdLanguage();
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        if (languageindex == TvLanguage.ENGLISH) {
            config.locale = Locale.ENGLISH;
        } else {
            if (languageindex == TvLanguage.CHINESE) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            } else {
                config.locale = Locale.TAIWAN;
            }
        }
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(config, dm);
    }

    public static void setSourceDirty(boolean isDirty) {
        Log.v(TAG, "set source dirty to " + isDirty);
        isSourceDirty = isDirty;
    }

    public static boolean getSourceDirty() {
        return isSourceDirty;
    }

    private void initLittleDownCounter() {
        LittleDownTimer.getInstance().start();
        int value = TvCommonManager.getInstance().getOsdTimeoutInSecond();
        if (value < 1) {
            value = 5;
        }
        if (value > 30) {
            LittleDownTimer.stopMenu();
        } else {
            LittleDownTimer.setMenuStatus(value);
        }
    }

    // Ui setting root broadcast receiver.
    private class RootBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // mute
           /* if (action
                    .equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH")) {
            } else {
                //Log.e("root broadcast reciever!", "Unknown Key Event!");
            }*/
        }
    }
}
