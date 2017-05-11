package com.horion.tv.framework.logicapi.framework;

import com.horion.system.data.TCEnumSetData;
import com.horion.system.data.TCRangeSetData;
import com.horion.system.data.TCSwitchSetData;

public interface ATVLogicApi {
    boolean atvExitSearch(String str, boolean z);

    TCSwitchSetData atvMenuGetChannelSkip();

    TCEnumSetData atvMenuGetColorSystem();

    TCEnumSetData atvMenuGetSoundSystem();

    TCRangeSetData atvMenuGetVolumeCompensation();

    boolean atvMenuSetChannelSkip(int i);

    boolean atvMenuSetColorSystem(int i);

    boolean atvMenuSetSoundSystem(int i);

    boolean atvMenuSetVolumeCompensation(int i);

    TCEnumSetData getATVNicamType();

    boolean processFreqFine(int i);

    boolean processManualSearch(int i);

    boolean setAtvNicamType(int i);

    boolean showDeleteChannelDialog();

    boolean startAutoSearch();

    boolean startFreqFine();

    boolean startManualSearch();

    boolean stopAutoSearch();

    boolean stopFreqFine();

    boolean stopManualSearch();
}
