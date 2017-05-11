package com.horion.tv.logic;

import android.content.Context;

public class TvLogicBroadcast {
    private static TvLogicBroadcast instance = null;

    private TvLogicBroadcast() {
    }

    public static TvLogicBroadcast getInstance() {
        if (instance == null) {
            instance = new TvLogicBroadcast();
        }
        return instance;
    }

    public void init(Context context) {
    }
}
