package com.horion.tv.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncTask {
    private static final int THREAD_COUNT = 2;
    private static ThreadPoolExecutor executor = null;
    private static BlockingQueue<Runnable> queue = new LinkedBlockingQueue();

    public static void runOnThread(Runnable runnable) {
        synchronized (AsyncTask.class) {
            try {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(2, 2, 999, TimeUnit.DAYS, queue, new ThreadFactory() {
                        public Thread newThread(Runnable runnable) {
                            return new Thread(runnable, "AsyncTask" + System.currentTimeMillis());
                        }
                    });
                }
                ThreadPoolExecutor obj = executor;
                obj.execute(runnable);
            } finally {
                Class cls = AsyncTask.class;
            }
        }
    }
}
