package com.horion.tv.logic.av;

import android.util.Log;

import com.horion.system.data.TCEnumSetData;
import com.horion.system.data.TCRetData;
import com.horion.system.data.TCSetData;
import com.horion.tv.define.uilogic.UILOGIC_TVMENU_COMMAND;
import com.horion.tv.framework.logicapi.framework.AVLogicApi;
import com.horion.tv.framework.logicapi.framework.TvLogicGetFunction;
import com.horion.tv.framework.logicapi.framework.TvLogicSetFunction;
import com.horion.tv.framework.logicapi.framework.UiCmdParams;
import com.horion.tv.logic.framework.TvLogic;

import java.util.List;

public class AVLogic extends TvLogic implements AVLogicApi {
    private static AVLogic instance = null;
    private List<String> colorSystemValues = null;

    private TvLogicGetFunction getAVColorSystem = new TvLogicGetFunction(UILOGIC_TVMENU_COMMAND.AV_GET_COLOR_SYSTEM.toString()) {
        public TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams) {
            return AVLogic.this.atvMenuGetColorSystem();
        }
    };

    private TvLogicSetFunction setAVColorSystem = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.AV_SET_COLOR_SYSTEM.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            return new TCRetData(AVLogic.this.atvMenuSetColorSystem(uiCmdParams.selectIndex));
        }
    };

    public static AVLogic getInstance() {
        if (instance == null) {
            instance = new AVLogic();
            Log.d("Maxs","AVLogic:getInstance()");
        }
        return instance;
    }

    public TCEnumSetData atvMenuGetColorSystem() {
        /*
        Source currentSource = ExternalApi.getInstance().systemApi.getCurrentSource();
        if (this.colorSystemValues == null) {
            Iterable<COLORSYSTEM> colorSystemValues = ExternalApi.getInstance().avApi.getColorSystemValues(currentSource);
            this.colorSystemValues = new ArrayList();
            for (COLORSYSTEM colorsystem : colorSystemValues) {
                this.colorSystemValues.add(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + colorsystem.toString());
            }
        }*/
        ////String str = KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + ExternalApi.getInstance().avApi.getColorSystem(currentSource).toString();
        String str = null;
        TCEnumSetData tCEnumSetData = new TCEnumSetData();
        tCEnumSetData.setCurrent(str);
        tCEnumSetData.setEnumList(this.colorSystemValues);
        tCEnumSetData.setEnumCount(this.colorSystemValues.size());
        return tCEnumSetData;
    }

    public boolean atvMenuSetColorSystem(int i) {
        /*Source currentSource = ExternalApi.getInstance().systemApi.getCurrentSource();
        return ExternalApi.getInstance().avApi.setColorSystem(COLORSYSTEM.valueOf(((String) this.colorSystemValues.get(i)).substring(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD.length())), currentSource);*/return false;
    }
}
