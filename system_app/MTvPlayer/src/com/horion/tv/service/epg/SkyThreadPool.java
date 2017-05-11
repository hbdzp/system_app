package com.horion.tv.service.epg;

import android.annotation.TargetApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SkyThreadPool {
    private static ExecutorService mExecutorFixThread = null;
    private static ScheduledExecutorService mScheduledExecutorService = null;
    private static ThreadPoolExecutor threadPoolManager = null;
    private static SynchronousQueue<Runnable> workQueue = null;

    public static void addFixTask(Runnable runnable) {
        generateFixThreadPools();
        if (runnable != null) {
            mExecutorFixThread.execute(runnable);
        }
    }

    public static void addScheduledTask(Runnable runnable, long j) {
        generateScheduledThreadPool();
        if (runnable != null) {
            mScheduledExecutorService.scheduleWithFixedDelay(runnable, 0, j, TimeUnit.MILLISECONDS);
        }
    }

    public static void addTask(Runnable runnable) {
        generateThreadPool();
        if (runnable != null) {
            threadPoolManager.execute(runnable);
        }
    }

    public static void cancelAll() {
        if (workQueue != null && !workQueue.isEmpty()) {
            workQueue.clear();
        }
    }

    private static void generateFixThreadPools() {
        if (mExecutorFixThread == null) {
            mExecutorFixThread = Executors.newFixedThreadPool(4);
        }
    }

    private static void generateScheduledThreadPool() {
        if (mScheduledExecutorService == null) {
            mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        }
    }

    @TargetApi(9)
    private static void generateThreadPool() {
        if (threadPoolManager == null) {
            workQueue = new SynchronousQueue();
            threadPoolManager = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 30, TimeUnit.SECONDS, workQueue);
        }
    }

    public static void shutDownFixThreadPool() {
        try {
            if (mExecutorFixThread != null) {
                mExecutorFixThread.shutdown();
                mExecutorFixThread = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutDownScheduledThreadPool() {
        try {
            if (mScheduledExecutorService != null) {
                mScheduledExecutorService.shutdownNow();
                mScheduledExecutorService = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutDownThreadPool() {
        try {
            if (threadPoolManager != null) {
                cancelAll();
                threadPoolManager.shutdownNow();
                threadPoolManager = null;
                workQueue = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
