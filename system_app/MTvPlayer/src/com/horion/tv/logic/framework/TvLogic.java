package com.horion.tv.logic.framework;

import android.content.Context;
import android.util.Log;

import com.horion.tv.framework.api.KtcTvApiListener;
import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
import com.horion.tv.framework.logicapi.framework.TvLogicGetFunction;
import com.horion.tv.framework.logicapi.framework.TvLogicSetFunction;
////import com.horion.tv.utils.KtcTVDebug;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class TvLogic {
    private List<TvLogicGetFunction> getFunctionList = new ArrayList();
    protected KtcCmdConnectorListener listener = null;
    private List<KtcTvApiListener> listenerList = new ArrayList();
    protected Context mContext = null;
    private List<TvLogicSetFunction> setFunctionList = new ArrayList();

    public final List<TvLogicGetFunction> getGetFunctions() {
        return this.getFunctionList;
    }

    public final List<TvLogicSetFunction> getSetFunctions() {
        return this.setFunctionList;
    }

    public final List<KtcTvApiListener> getTvApiListeners() {
        return this.listenerList;
    }

    public void init(Context context, KtcCmdConnectorListener ktcCmdConnectorListener) {
        this.mContext = context;
        this.listener = ktcCmdConnectorListener;
        ///Log.d("Maxs","--------------------------------11111---------------------------------");
        ///Log.d("Maxs","getClass().getDeclaredFields().length = " + getClass().getDeclaredFields().length);
        ////Log.d("Maxs","--------------------------------22222---------------------------------");
        ///Log.d("Maxs","TvLogic:getClass().getDeclaredFields() = " + getClass().getDeclaredFields());
        setFunctionList.clear();
        getFunctionList.clear();
        listenerList.clear();
        Log.d("Maxs","TvLogic:init:getClass.getDeclaredFields.length = " + getClass().getDeclaredFields().length);
        Log.d("Maxs","-------->before:TvLogic:init:getFunctionList.size() = " + getFunctionList.size());
        Log.d("Maxs","-------->before:TvLogic:init:setFunctionList.size() = " + setFunctionList.size());
        for (Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj != null) {
                    if (KtcTvApiListener.class.isInstance(obj)) {
                        ////KtcTVDebug.debug(getClass().getName() + " add tvapilistener:<<<<<" + obj.getClass().getName() + ">>>>>");
                        this.listenerList.add((KtcTvApiListener) obj);
                    } else if (TvLogicGetFunction.class.isInstance(obj)) {
                        ////KtcTVDebug.debug(getClass().getName() + " add logicGetfunction:<<<<<" + ((TvLogicGetFunction) obj).cmd + ">>>>>");
                        this.getFunctionList.add((TvLogicGetFunction) obj);
                    } else if (TvLogicSetFunction.class.isInstance(obj)) {
                        ////KtcTVDebug.debug(getClass().getName() + " add logicSetfunction:<<<<<" + ((TvLogicSetFunction) obj).cmd + ">>>>>");
                        this.setFunctionList.add((TvLogicSetFunction) obj);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        Log.d("Maxs","----->after:TvLogic:init:getFunctionList.size() = " + getFunctionList.size());
        Log.d("Maxs","----->after:TvLogic:init:setFunctionList.size() = " + setFunctionList.size());
    }
}
