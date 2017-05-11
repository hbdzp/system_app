package com.horion.tv.framework.logicapi.framework;

import com.horion.system.data.TCEnumSetData;
import com.horion.system.data.TCSwitchSetData;

public interface DTMBLogicApi {
    boolean dtmbExitSearch(String str, boolean z);

    TCSwitchSetData dtmbMenuGetChannelSkip();

    boolean dtmbMenuSetChannelSkip(int i);

    TCEnumSetData getDTMBSubTitle();

    boolean setDTMBSubTitle(int i);

    boolean showChannelInfo();

    boolean showDeleteChannelDialog();

    boolean showDtvInfo();

    boolean startAutoSearch();

    boolean startDTMBManualSearchFromUi(float f, float f2, float f3);

    boolean startManualSearch();

    boolean stopAutoSearch();

    String stopManualSearch();
}
