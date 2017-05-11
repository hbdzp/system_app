package com.horion.tv.utils;

public interface ISkyTvSingleAsyncTask {
    void done(Object... objArr) throws InterruptedException;

    Object[] run(Object... objArr) throws InterruptedException;
}
