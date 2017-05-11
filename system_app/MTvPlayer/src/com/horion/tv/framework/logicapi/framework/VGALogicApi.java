package com.horion.tv.framework.logicapi.framework;

import com.horion.system.data.TCRangeSetData;

public interface VGALogicApi {
    TCRangeSetData getHPosition();

    TCRangeSetData getPhase();

    TCRangeSetData getRefreshRate();

    TCRangeSetData getVPosition();

    boolean setHPosition(int i);

    boolean setPhase(int i);

    boolean setRefreshRate(int i);

    boolean setVPosition(int i);

    boolean startAutoAdjust();
}
