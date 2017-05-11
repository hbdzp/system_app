package com.horion.tv.framework.logicapi.framework;

import com.horion.system.data.TCSetData;

public abstract class TvLogicGetFunction {
    public String cmd = null;

    public TvLogicGetFunction(String str) {
        this.cmd = str;
    }

    public abstract TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams);
}
