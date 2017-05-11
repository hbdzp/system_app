package com.horion.tv.framework.logicapi.framework;

import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import com.horion.system.data.TCEnumSetData;
////import com.horion.tv.define.object.Channel;
////import com.horion.tv.define.object.Source;
////import com.horion.tv.define.uilogic.params.TVOnKeydownParams;

public interface SystemLogicApi {
    boolean addPopUp(View view);

    boolean addPopUp(View view, LayoutParams layoutParams);

    boolean closeSearchGuide();

    boolean dismissPopUp(View view, boolean z);

    TCEnumSetData getCollectChannel();

    String getCollectChannelValue();

    String getLocation();

    boolean getQuickDemoStatus();

   //// boolean onCollectInfoSelect(Channel channel);

    ////boolean onKeyDown(TVOnKeydownParams tVOnKeydownParams);

   //// boolean onSignalPlugChangeUICB(Source source, int i);

    boolean removePopUp(View view, boolean z);

    boolean resetNosignalCout();

    boolean screenDisplay();

    boolean setCollectChannel();

    boolean setLocation(String str, boolean z);

    ////boolean showMenu(Source source);

    boolean startShowScreenSaver(boolean z);

    TCEnumSetData tvMenuGetDisplayMode();

    TCEnumSetData tvMenuGetPictureMode();

    TCEnumSetData tvMenuGetSoundMode();

    boolean tvMenuSetDisplayMode(int i);

    boolean tvMenuSetPictureMode(int i);

    boolean tvMenuSetSoundMode(int i);
}
