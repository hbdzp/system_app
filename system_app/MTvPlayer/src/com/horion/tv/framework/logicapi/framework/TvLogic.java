package com.horion.tv.framework.logicapi.framework;

import android.content.Context;
import android.util.Log;
////import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
import com.horion.tv.framework.api.KtcTvApiListener;
////import com.horion.tv.utils.KtcTVDebug;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class TvLogic {
    private List<TvLogicGetFunction> getFunctionList = new ArrayList();
   //// protected KtcCmdConnectorListener listener = null;
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

    /////public void init(Context context, KtcCmdConnectorListener ktcCmdConnectorListener) {
    public void init(Context context) {
        this.mContext = context;
       ///// this.listener = ktcCmdConnectorListener;
        Log.d("Maxs","getClass().getDeclaredFields() = " + getClass().getDeclaredFields());
        for (Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj != null) {
                    if (KtcTvApiListener.class.isInstance(obj)) {
                       //// KtcTVDebug.debug(getClass().getName() + " add tvapilistener:<<<<<" + obj.getClass().getName() + ">>>>>");
                        this.listenerList.add((KtcTvApiListener) obj);
                    } else if (TvLogicGetFunction.class.isInstance(obj)) {
                       //// KtcTVDebug.debug(getClass().getName() + " add logicfunction:<<<<<" + ((TvLogicGetFunction) obj).cmd + ">>>>>");
                        this.getFunctionList.add((TvLogicGetFunction) obj);
                    } else if (TvLogicSetFunction.class.isInstance(obj)) {
                       //// KtcTVDebug.debug(getClass().getName() + " add logicfunction:<<<<<" + ((TvLogicSetFunction) obj).cmd + ">>>>>");
                        this.setFunctionList.add((TvLogicSetFunction) obj);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
    }
}
