package com.horion.tv.framework.logicapi;

import android.content.Context;

import com.horion.tv.framework.logicapi.framework.UiCmdParams;
import com.horion.tv.service.base.KtcTvController;
import com.horion.tv.service.menu.KtcTvMenuItem;
import com.horion.tv.util.Constants.SOURCE_NAME_ENUM;

import java.util.ArrayList;

/**
 * Created by xiacf on 2017/1/4.
 */

public class LogicApiForKtcUICmd {
    private static LogicApiForKtcUICmd instance = null;
    private Context mContext = null;


    public static LogicApiForKtcUICmd getInstance() {
        if (instance == null) {
            instance = new LogicApiForKtcUICmd();
        }
        return instance;
    }

    public ArrayList<KtcTvMenuItem> uiGetCmd(String str, UiCmdParams uiCmdParams) {
        SOURCE_NAME_ENUM curSourceEnum = KtcTvController.getInstance().getSourceNameEnum();
        if(curSourceEnum == SOURCE_NAME_ENUM.ATV){

        }else if(curSourceEnum == SOURCE_NAME_ENUM.DTMB){

        }else if(curSourceEnum == SOURCE_NAME_ENUM.AV){

        }else if(curSourceEnum == SOURCE_NAME_ENUM.HDMI){

        }
        return null;
    }

    public boolean uiSetCmd(String str, UiCmdParams uiCmdParams) {
        SOURCE_NAME_ENUM curSourceEnum = KtcTvController.getInstance().getSourceNameEnum();
        if(curSourceEnum == SOURCE_NAME_ENUM.ATV){

        }else if(curSourceEnum == SOURCE_NAME_ENUM.DTMB){

        }else if(curSourceEnum == SOURCE_NAME_ENUM.AV){

        }else if(curSourceEnum == SOURCE_NAME_ENUM.HDMI){

        }
        return false;
    }
}
