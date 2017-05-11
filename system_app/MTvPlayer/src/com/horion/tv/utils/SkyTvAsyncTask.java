package com.horion.tv.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.HashMap;

public class SkyTvAsyncTask extends Thread {
    private static final int DEFAULT_MSG_WHAT = -1;
    private static SkyTvAsyncTask instance = null;
    private HashMap<Integer, AsyncTaskHandler> asyncTaskMap = new HashMap();
    private boolean bNotify = false;
    private Boolean lock = Boolean.valueOf(true);
    private Looper looper = null;

    public static abstract class IAsyncTask {
        public abstract void doAsyncTask(Object obj);

        public void onAsyncTaskBeOverride(Object obj) {
        }
    }

    public enum ASYNC_TASK_TAG {
        INIT_TV,
        START_TV,
        SWITCH_SOURCE,
        SWITCH_CHANNEL
    }

    private class AsyncTaskHandler extends Handler {
        public AsyncTaskHandler() {
            super(SkyTvAsyncTask.this.looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            AsyncTaskHandlerMessage asyncTaskHandlerMessage = (AsyncTaskHandlerMessage) message.obj;
            ///SkyTVDebug.debug("SkyTvAsyncTask start handle:" + i);
            asyncTaskHandlerMessage.task.doAsyncTask(asyncTaskHandlerMessage.params);
           /// SkyTVDebug.debug("SkyTvAsyncTask end handle:" + i);
        }
    }

    private class AsyncTaskHandlerMessage {
        public Object params = null;
        public IAsyncTask task = null;

        public AsyncTaskHandlerMessage(IAsyncTask iAsyncTask, Object obj) {
            this.task = iAsyncTask;
            this.params = obj;
        }
    }

    private SkyTvAsyncTask() {
        super("TvAsyncTaskThread");
    }

    public static SkyTvAsyncTask getInstance() {
        if (instance == null) {
            instance = new SkyTvAsyncTask();
            instance.start();
        }
        return instance;
    }

    public void clearTask(int i) {
        AsyncTaskHandler asyncTaskHandler = (AsyncTaskHandler) this.asyncTaskMap.get(Integer.valueOf(i));
        if (asyncTaskHandler != null) {
            asyncTaskHandler.removeMessages(i);
        }
    }

    public void run() {
        Looper.prepare();
        this.looper = Looper.myLooper();
        synchronized (this.lock) {
            this.lock.notify();
            this.bNotify = true;
        }
       /// SkyTVDebug.debug("SkyTvAsyncTask start loop");
        Looper.loop();
    }

    public void runAsyncTask(int i, IAsyncTask iAsyncTask, Object obj) {
        runAsyncTask(i, iAsyncTask, obj, 0);
    }

    public void runAsyncTask(int i, IAsyncTask iAsyncTask, Object obj, long j) {
        runAsyncTask(i, iAsyncTask, obj, j, false);
    }

    public void runAsyncTask(int i, IAsyncTask iAsyncTask, Object obj, long j, boolean z) {
        try {
            AsyncTaskHandler asyncTaskHandler;
            synchronized (this.lock) {
                if (this.looper == null && !this.bNotify) {
                    this.lock.wait();
                }
            }
            if (i != -1) {
                asyncTaskHandler = (AsyncTaskHandler) this.asyncTaskMap.get(Integer.valueOf(i));
                if (asyncTaskHandler == null) {
                    asyncTaskHandler = new AsyncTaskHandler();
                    this.asyncTaskMap.put(Integer.valueOf(i), asyncTaskHandler);
                }
                if (z && asyncTaskHandler.hasMessages(i)) {
                   /// SkyTVDebug.debug("SkyTvAsyncTask removeMessages tag:" + i);
                    asyncTaskHandler.removeMessages(i);
                }
            } else {
                asyncTaskHandler = new AsyncTaskHandler();
            }
            Message message = new Message();
            message.what = i;
            message.obj = new AsyncTaskHandlerMessage(iAsyncTask, obj);
            asyncTaskHandler.sendMessageDelayed(message, j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runAsyncTask(IAsyncTask iAsyncTask) {
        runAsyncTask(iAsyncTask, null);
    }

    public void runAsyncTask(IAsyncTask iAsyncTask, Object obj) {
        runAsyncTask(-1, iAsyncTask, obj);
    }
}
