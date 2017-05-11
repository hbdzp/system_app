package com.horion.tv.service.epg;

import android.content.Context;
import android.content.Intent;

import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
import com.horion.tv.utils.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenEPGActivityApi {
    public static String BROADCAST_TVOPENEPGACTIVITY = "com.ktc.openepgactivityexit";
    private Context context = null;
    protected KtcCmdConnectorListener listener = null;

    public OpenEPGActivityApi(KtcCmdConnectorListener skyCmdConnectorListener, Context context) {
        this.listener = skyCmdConnectorListener;
        this.context = context;
    }

    public String formatToString(long j) {
        return new SimpleDateFormat("mm:ss:ms").format(new Date(j));
    }

    public void startShowChannelDeletedView() {
        if (!SkyOpenEPGActivity.bStartActivity) {
            SkyOpenEPGActivity.bStartActivity = true;
            AsyncTask.runOnThread(new Runnable() {
                public void run() {
                    try {
                        Intent intent = new Intent();
                        intent.setClass(OpenEPGActivityApi.this.context, SkyOpenEPGActivity.class);
                        intent.putExtra("viewtype", "channelEditDeleted");
                        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                        OpenEPGActivityApi.this.context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void startShowChannelSortView() {
        if (!SkyOpenEPGActivity.bStartActivity) {
            SkyOpenEPGActivity.bStartActivity = true;
            AsyncTask.runOnThread(new Runnable() {
                public void run() {
                    try {
                        Intent intent = new Intent();
                        intent.setClass(OpenEPGActivityApi.this.context, SkyOpenEPGActivity.class);
                        intent.putExtra("viewtype", "channelEditSort");
                        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                        OpenEPGActivityApi.this.context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void startShowLeftEpgView() {
        if (!SkyOpenEPGActivity.bStartActivity) {
            SkyOpenEPGActivity.bStartActivity = true;
            AsyncTask.runOnThread(new Runnable() {
                public void run() {
                    try {
                        Intent intent = new Intent();
                        intent.setClass(OpenEPGActivityApi.this.context, SkyOpenEPGActivity.class);
                        intent.putExtra("viewtype", "leftview");
                        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                        OpenEPGActivityApi.this.context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void startShowRightEpgView() {
        if (!SkyOpenEPGActivity.bStartActivity) {
            SkyOpenEPGActivity.bStartActivity = true;
            AsyncTask.runOnThread(new Runnable() {
                public void run() {
                    try {
                        Intent intent = new Intent();
                        intent.setClass(OpenEPGActivityApi.this.context, SkyOpenEPGActivity.class);
                        intent.putExtra("viewtype", "rightview");
                        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                        OpenEPGActivityApi.this.context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void stopOpenEpgActivity() {
        try {
            this.context.sendBroadcast(new Intent(BROADCAST_TVOPENEPGACTIVITY));
        } catch (Exception e) {
        }
    }
}
