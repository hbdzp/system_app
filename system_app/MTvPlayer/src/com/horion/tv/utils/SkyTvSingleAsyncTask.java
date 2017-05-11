package com.horion.tv.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.UUID;

public abstract class SkyTvSingleAsyncTask implements ISkyTvSingleAsyncTask {
    private static HashMap<String, AsyncTaskThread> threadMap = new HashMap();
    private String name = "";
    private AsyncTaskThread thread = null;
    private Boolean threadLock = Boolean.valueOf(true);


    private class AsyncTaskThread extends Thread {
        private String id = UUID.randomUUID().toString();

        public AsyncTaskThread(String str) {
            super(str);
        }

        public String getID() {
            return this.id;
        }
    }

    public SkyTvSingleAsyncTask(String str) {
        this.name = str;
    }

    public SkyTvSingleAsyncTask() {
    }
    public static Object[] objects(Object... objArr) {
        return objArr;
    }

    public void start(final Object... objArr) {
        synchronized (this) {
            synchronized (this.threadLock) {
                ////SkyTVDebug.debug("SkyTvSingleAsyncTask", this.name + " start thread:" + this.thread);
                Log.d("Maxs24","SkyTvSingleAsyncTask:start1:thread == null = " + (thread == null));
                if (this.thread != null && threadMap.get(this.thread.getID()) == null) {
                    this.thread = null;
                }
                Log.d("Maxs24","SkyTvSingleAsyncTask:start2:thread == null = " + (thread == null));
               if (this.thread == null) {
                    this.thread = new AsyncTaskThread(this.name) {
                        public void run() {
                            try {
                                Object[] run = SkyTvSingleAsyncTask.this.run(objArr);
                                Thread.sleep(1);
                                SkyTvSingleAsyncTask.this.done(run);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                ///SkyTVDebug.debug("SkyTvSingleAsyncTask:" + this.name + " is interrupt!!!");
                            }
                            synchronized (SkyTvSingleAsyncTask.this.threadLock) {
                                SkyTvSingleAsyncTask.threadMap.remove(getID());
                                ///SkyTVDebug.debug("SkyTvSingleAsyncTask", this.name + " threadMap remove " + getID());
                            }
                        }
                    };
                    threadMap.put(this.thread.getID(), this.thread);
                    this.thread.start();
                }
            }
        }
    }

    public void stop() {
        synchronized (this) {
            synchronized (this.threadLock) {
                if (this.thread != null) {
                    threadMap.remove(this.thread.getID());
                    this.thread.interrupt();
                    this.thread = null;
                }
            }
        }
    }
}
