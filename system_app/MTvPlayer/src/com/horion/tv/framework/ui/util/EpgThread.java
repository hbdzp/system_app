package com.horion.tv.framework.ui.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EpgThread {
    private static EpgThread instance = null;
    public volatile boolean isEpgThreadIng = false;
    public Handler tHandler = null;
    private Thread thread = null;

    private EpgThread() {
        if (this.thread == null) {
            this.thread = new Thread() {
                public void run() {
                    Looper.prepare();
                    EpgThread.this.tHandler = new Handler(Looper.myLooper()) {
                        public void handleMessage(Message message) {
                            ((EpgRunnable) message.obj).run();
                        }
                    };
                    Log.d("Maxs24","EpgThread:tHandler == null = " + (tHandler == null));
                    EpgThread.this.isEpgThreadIng = true;
                    Looper.loop();
                }
            };
            this.thread.start();
        }
    }

    public static EpgThread getInstance() {
        synchronized (EpgThread.class) {
            try {
                if (instance == null) {
                    instance = new EpgThread();
                }
                EpgThread epgThread = instance;
                return epgThread;
            } finally {
                Object obj = EpgThread.class;
            }
        }
    }

    public void runOnThread(Runnable runnable, int i) {
        Message message = new Message();
        message.obj = runnable;
        message.what = i;
        this.tHandler.sendMessage(message);
    }

    public void runOnThreadWithDelay(Runnable runnable, int i, int i2) {
        Message message = new Message();
        message.obj = runnable;
        message.what = i;
        this.tHandler.sendMessageDelayed(message, (long) i2);
    }
}
