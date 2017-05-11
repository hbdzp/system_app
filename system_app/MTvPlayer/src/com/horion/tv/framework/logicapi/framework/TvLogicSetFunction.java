package com.horion.tv.framework.logicapi.framework;

import com.horion.system.data.TCRetData;

public abstract class TvLogicSetFunction {
    public String cmd = null;

    public TvLogicSetFunction(String str) {
        this.cmd = str;
    }

    public abstract TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams);
}
