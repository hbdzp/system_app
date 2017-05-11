package com.horion.tv.service.menu;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
////import com.ktc.framework.ktcsdk.logger.KtcServerLogger.LOGTYPE;
import com.horion.tv.ui.MainMenuActivity;
import com.ktc.framework.ktcsdk.util.KtcAddonTextUtils;
import com.ktc.ui.api.KtcCommenMenu;
import com.ktc.ui.api.KtcCommenMenu.OnMenuOnKeyEventListener;
import com.ktc.ui.menu.KtcMenuData;
import com.horion.tv.R;
////import com.horion.system.define.KtcConfigDefs;
////import com.horion.tv.define.object.Source.SOURCE_NAME_ENUM;
////import com.horion.tv.define.uilogic.UILOGIC_TVMENU_COMMAND;
////import com.horion.tv.framework.logicapi.LogicApiForUI;
////import com.horion.tv.framework.logicapi.LogicApiForUICmd;
////import com.horion.tv.framework.logicapi.framework.UiCmdParams;
////import com.horion.tv.service.base.KtcTvController;
////import com.horion.tv.ui.forlogic.KtcTvUI;
////import com.horion.tv.utils.UITextUtilInterface.UITextUtil;
////import com.horion.tv.util.DolbyUtil;
////import com.horion.tv.utils.DtvConfig;
////import com.horion.tv.utils.KtcTVDebug;
/////import com.horion.tv.utils.KtcTvConfig;
/////import com.horion.tv.utils.KtcTvSubmitLog;
import com.horion.system.data.TCSetData;
import com.horion.system.data.TCEnumSetData;
import com.horion.tv.define.uilogic.UILOGIC_TVMENU_COMMAND;
import com.horion.tv.framework.logicapi.LogicApiForUICmd;
import com.horion.tv.framework.logicapi.framework.UiCmdParams;
import com.horion.tv.util.Constants;
import com.horion.tv.util.Constants.SOURCE_NAME_ENUM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KtcTvMenuImple implements OnMenuOnKeyEventListener {
    private final String TAG;
    public boolean bShowMenu;
    private Context ctx;
    private int curMenuRank;
    private long curRefreshTime;
    private SOURCE_NAME_ENUM curSourceEnum;
    private int firstMenuIndex;
    private KtcCommenMenu menu;
    public List<KtcTvMenuItem> menuFirstList;
    private Handler menuHandler;
    public List<KtcTvMenuItem> menuSecondList;
    private long preRefreshTime;
    private FrameLayout rootMenuView;
    private int secondMenuIndex;

    public KtcTvMenuImple() {
        this.menuFirstList = null;
        this.menuSecondList = null;
        this.bShowMenu = false;
        this.TAG = "langlang";
        this.ctx = null;
        this.rootMenuView = null;
        this.menu = null;
        this.secondMenuIndex = 0;
        this.firstMenuIndex = 0;
        this.curSourceEnum = SOURCE_NAME_ENUM.ATV;
        this.preRefreshTime = 0;
        this.curRefreshTime = 0;
        this.curMenuRank = 0;
        this.menuHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                Log.d("Maxs","KtcTVMenuImple:menHandler:mssage.waht = " + message.what);
                switch (message.what) {
                    case 0:
                        KtcTvMenuImple.this.showSecondMenu(0);
                        return;
                    case 5:
                        KtcTvMenuImple.this.hideMenu(5, message.arg1);
                        return;
                    case 10:
                        KtcTvMenuImple.this.showFirstMenu(10);
                        return;
                    case 11:
                        KtcTvMenuImple.this.hideMenu(11, message.arg1);
                        return;
                    case 14:
                        KtcTvMenuImple.this.showFlipOutMenu(message.arg1, false);
                        //KtcTvController.getInstance().isWaitingShowFlipOutMenu = false;
                        return;
                    case 15:
                       //// KtcTVDebug.debug("lwr", "KEY_SCEOND_SHOW_GIDEMENU");
                        KtcTvMenuImple.this.showTipTost(message.arg2);
                        return;
                    default:
                        return;
                }
            }
        };
        this.bShowMenu = false;
    }

	private boolean getIsFirstMenuDisplay(KtcTvMenuItem ktcTvMenuItem) {
		if (!ktcTvMenuItem.getDisplay()) {
			return false;
		}
		ArrayList arrayList = new ArrayList();
		/*
		 * if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_DTV_SOUND_TRACK")
		 * || ktcTvMenuItem.getNodeKey().equals(KtcConfigDefs.
		 * KTC_CFG_TV_AV_COLOR_SYSTEM) ||
		 * ktcTvMenuItem.getNodeKey().equals(KtcConfigDefs.
		 * KTC_CFG_TV_DISPLAY_MODE)) { if
		 * (getSecondMenuData(ktcTvMenuItem).size() <= 1) { return false; } }
		 * else
		 */
		// if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_DTV_SUBTITLE")) {
		// if (getSecondMenuData(ktcTvMenuItem).size() <= 1) {
		// return false;
		// }
		// ///if (DtvConfig.isDtvSubtitleDisable()) {
		// /// return false;
		// /// }
		// }
		/*
		 * else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_SYSTEM_DEMO"))
		 * { return LogicApiForUI.getInstance().getSystemLogicApi() != null ?
		 * LogicApiForUI.getInstance().getSystemLogicApi().getQuickDemoStatus()
		 * : false; } else { if
		 * (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_CHANNEL_SETTING")) {
		 * return !KtcTvController.getInstance().ischannelMenuLock(); } }
		 */
		return true;
	}

    private void getItemIcon(KtcMenuData ktcMenuData, KtcTvMenuItem ktcTvMenuItem) {
        if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_COLLECT_CHANNEL")) {
            ktcMenuData.setItemFocusIcon(R.drawable.collect_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.collect);
        } /*else if (ktcTvMenuItem.getNodeKey().equals(KtcConfigDefs.KTC_CFG_TV_DISPLAY_MODE)) {
            ktcMenuData.setItemFocusIcon(R.drawable.displaymode_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.displaymode);
        }*/ else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_PICTURE_MODE")) {
            ktcMenuData.setItemFocusIcon(R.drawable.picturemode_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.picturemode);
        } else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_CHANNEL_SETTING")) {
            ktcMenuData.setItemFocusIcon(R.drawable.channel_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.channel);
        } else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_SOUND_MODE")) {
            ktcMenuData.setItemFocusIcon(R.drawable.sound_mode_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.sound_mode);
        } else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_DTV_SOUND_CHANNEL")) {
            ktcMenuData.setItemFocusIcon(R.drawable.sound_channel_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.sound_channel);
        } else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_DTV_SOUND_TRACK")) {
            ktcMenuData.setItemFocusIcon(R.drawable.sound_track_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.sound_track);
        } else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_DTV_SUBTITLE")) {
            ktcMenuData.setItemFocusIcon(R.drawable.subtitle_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.subtitle);
        } /*else if (ktcTvMenuItem.getNodeKey().equals(KtcConfigDefs.KTC_CFG_TV_AV_COLOR_SYSTEM)) {
            ktcMenuData.setItemFocusIcon(R.drawable.colorsystem_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.colorsystem);
        } */else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_SYSTEM_DEMO")) {
            ktcMenuData.setItemFocusIcon(R.drawable.tvdemo_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.tvdemo);
        } else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_3D_SET")) {
            ktcMenuData.setItemFocusIcon(R.drawable.icon3d_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.icon3d);
        } else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_VGA_AUTO_ADJUST")) {
            ktcMenuData.setItemFocusIcon(R.drawable.vgaautoadjust_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.vgaautoadjust);
        } else if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_INTELLIGENT_IR_MODE")) {
            ktcMenuData.setItemFocusIcon(R.drawable.intelligent_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.intelligent);
        } else {
            ktcMenuData.setItemFocusIcon(R.drawable.subtitle_focus);
            ktcMenuData.setItemUnFocusIcon(R.drawable.subtitle);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List<KtcTvMenuItem> getSecondMenuData(KtcTvMenuItem paramKtcTvMenuItem) {

        ArrayList localArrayList = new ArrayList();
        ////Object localObject1 = paramKtcTvMenuItem.getGetValueCommand();
        String cmdValue = paramKtcTvMenuItem.getGetValueCommand();
        ////KtcTVDebug.debug("langlang", "cmd = " + (String)localObject1);
        ////if (localObject1 != null)
        if (cmdValue != null)
        {
            ////localObject1 = LogicApiForUICmd.getInstance().uiGetCmd((String)localObject1, null);
            TCSetData tcSetData = LogicApiForUICmd.getInstance().uiGetCmd(cmdValue, null);
            if (tcSetData == null) {
                return localArrayList;
            }
            String tcSetDataType = tcSetData.getType();
           //// KtcTVDebug.debug("langlang", "dataType = " + (String)localObject2);
            if (TCSetData.KtcConfigType.KTC_CONFIG_ENUM.toString().equals(tcSetDataType))
            {
                ////localObject1 = (TCEnumSetData)localObject1;
                List<String> tcEnumSetData = ((TCEnumSetData)tcSetData).getEnumList();
                ///KtcTVDebug.debug("langlang", "llist = " + ((List)localObject2).size() + " " + ((TCEnumSetData)localObject1).getCurrent());

                ////int i = 0;
                Log.d("Maxs","----->KtcTvMenuImple:getSecondMenuList:((List)tcEnumSetData).size() =" + ((List)tcEnumSetData).size());
                ////if (i < ((List)tcEnumSetData).size())
                for (int i = 0; i < ((List)tcEnumSetData).size(); i++) {
                    KtcTvMenuItem localKtcTvMenuItem = new KtcTvMenuItem();
                    if (((String)((List)tcEnumSetData).get(i)).equals(((TCEnumSetData)tcSetData).getCurrent())) {
                        Log.d("Maxs","------>secondMenuIndex = " + secondMenuIndex);
                        this.secondMenuIndex = i;
                    }
                    localKtcTvMenuItem.setParentMenu(paramKtcTvMenuItem);
                    String str = KtcAddonTextUtils.getInstance().getText((String)((List)tcEnumSetData).get(i));
                    Log.d("Maxs","----->KtcTvMenuImple:getSecondMenuList:str = " +str);
                    if ((str != null) && (str.trim().length() > 0)) {
                        localKtcTvMenuItem.setContent(str);
                    }
                    localArrayList.add(localKtcTvMenuItem);
                   ///// for (;;)
                    ///{
                        ////localArrayList.add(localKtcTvMenuItem);
                       //// i += 1;
                        ////break;
                        ////localKtcTvMenuItem.setContent(UITextUtil.getInstance().getTextByKey((String)((List)localObject2).get(i)));
                   /// }
                }
            }
            ////else if (!TCSetData.KtcConfigType.KTC_CONFIG_ENUM.toString().equals(localObject2)) {}
        }
        return localArrayList;
        /*
        List<KtcTvMenuItem> ktcTvMenuItemList = new ArrayList<KtcTvMenuItem>();
        Log.d("Maxs","KtcTvMenuImple:getSecnodMenuData:paramKtcTvMenuItem.getGetValueCommand() = " + paramKtcTvMenuItem.getGetValueCommand());
        return ktcTvMenuItemList;*///by add Hisa
    }

    private boolean hideMenu(int i, int i2) {
        boolean z = true;
        synchronized (this) {
        	////KtcLogger.d("langlang", ">>>>hideMenu<<<");
            if (this.bShowMenu) {
               /// KtcTVDebug.debug("langlang", "key = " + i + "index = " + i2);
                switch (i) {
                    case 5:
                        this.menu.dismissMenuBySecond(i2);
                        break;
                    case 11:
                        this.menu.dismissMenu(i2);

                        break;
                }
                this.bShowMenu = false;
                /*if (LogicApiForUI.getInstance().getSystemLogicApi() != null) {
                    z = LogicApiForUI.getInstance().getSystemLogicApi().startShowScreenSaver(true);
                }*/
            } else {
                ////KtcTVDebug.debug("langlang", "menu id hide");
            }
        }
        return z;
    }

    private boolean resetMenuFristList(int i) {
        if (this.menuFirstList.size() > i && i >= 0) {
            KtcTvMenuItem ktcTvMenuItem = (KtcTvMenuItem) this.menuFirstList.get(i);
            Log.d("Maxs50","KtcTvMenuImple:resetMenuFristList:" + ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_COLLECT_CHANNEL"));
            if (ktcTvMenuItem.getNodeKey().equals("KTC_CFG_TV_COLLECT_CHANNEL")) {
                ArrayList arrayList = new ArrayList();
                TCEnumSetData tcEnumSetData = (TCEnumSetData)LogicApiForUICmd.getInstance().uiGetCmd(UILOGIC_TVMENU_COMMAND.TV_MENU_GET_COLLECT_CHANNEL.toString(), null);
                Log.d("Maxs50","tcEnumSetData.getCurrent() = " + KtcAddonTextUtils.getInstance().getText(tcEnumSetData.getCurrent()));
                this.menuFirstList.get(i).setContent(KtcAddonTextUtils.getInstance().getText(tcEnumSetData.getCurrent()));
                ///List<KtcTvMenuItem> secondMenuData = getSecondMenuData(ktcTvMenuItem);

                ///Log.d("Maxs","KtcTvMenuImple:resetMenuFirstList:secondMenuData.size() = " + secondMenuData.size());
                ///if (secondMenuData.size() > 0) {
                ////    ktcTvMenuItem = (KtcTvMenuItem) secondMenuData.get(0);

                ///    if (ktcTvMenuItem.getContent().equals(UITextUtil.getInstance().getTextByKey("KTC_CFG_TV_NO_CHANNEL_COLLECT"))) {//频道收藏是否显示
                ////        ((KtcTvMenuItem) this.menuFirstList.get(i)).setDisplay(false);
                ////    } else {
                ///        Log.d("Maxs","---------resetMenuFirstList-----");
                ///        ((KtcTvMenuItem) this.menuFirstList.get(i)).setContent(ktcTvMenuItem.getContent());
               ////     }
              ///  }
            }
        }
        return true;
    }

    private boolean setSecondMenu(int i) {
        if (this.menuFirstList == null || this.menuFirstList.size() < i + 1) {
            ////KtcTVDebug.debug("langlang", "itemIndex error !!!");
            return false;
        }
        KtcTvMenuItem ktcTvMenuItem = (KtcTvMenuItem) this.menuFirstList.get(i);
Log.d("Maxs","KTcTvMenuImple:ktcTvMenuItem.getNeedRefresh() = " + ktcTvMenuItem.getNeedRefresh());
        if (ktcTvMenuItem.getNeedRefresh()) {
            UiCmdParams uiCmdParams = new UiCmdParams();
            uiCmdParams.selectIndex = 0;
            LogicApiForUICmd.getInstance().uiSetCmd(ktcTvMenuItem.getSetValueCommand(), uiCmdParams);
           //// KtcTvConfig.getInstance().isNotShowMenuorSearchFlag = true;
            hideMenu(11, i);
            MainMenuActivity.getInstance().finish();
            return true;
        }

        boolean flipOutStatus = ktcTvMenuItem.getFlipOutStatus();//是否需要调整另一个界面
        ////KtcTVDebug.debug("getFlipOutCommand:" + ktcTvMenuItem.getFlipOutCommand() + "   getFlipOutStatus:" + flipOutStatus);
        if (flipOutStatus) {//是否需要调整另一个界面
            Message message = new Message();
            message.arg1 = i;
            message.what = 11;
            this.menuHandler.sendMessage(message);
            showFlipOutMenu(i, true);
            return true;
        }
        this.menuSecondList = ktcTvMenuItem.getChildMenuList();
        ////Log.d("Hisa","KtcTvMenuImple:(menuSecondList == null) = " + (menuSecondList == null));
        if (this.menuSecondList == null || this.menuSecondList.size() == 0) {
            this.menuSecondList = getSecondMenuData(ktcTvMenuItem);
            if (this.menuSecondList == null || this.menuSecondList.size() == 0) {
              ////  KtcTVDebug.error("menuSecondList null");
                this.menu.setShowingSecondMenu(false);
            }
        } else {
            this.secondMenuIndex = 0;
        }
        this.curMenuRank = 1;
        Log.d("Maxs","KtcTvMenuImple:setSecondMenu");
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.d("Maxs","KtcTvMenuImple:setSecondMenu13");
            showSecondMenu(0);
            return true;
        }
        Log.d("Maxs","KtcTvMenuImple:setSecondMenu11");
        this.menuHandler.sendEmptyMessage(0);
        return true;
    }

    private void setSecondMenuCommand(final int i) {
        KtcTvMenuItem parentMenu = ((KtcTvMenuItem) this.menuSecondList.get(i)).getParentMenu();
        final String setValueCommand = parentMenu.getSetValueCommand();
        ////KtcTVDebug.debug("langlang", "cmd = " + setValueCommand);
        if (setValueCommand != null) {
            if (parentMenu.getNeedRefresh()) {
                UiCmdParams uiCmdParams = new UiCmdParams();
                uiCmdParams.selectIndex = i;
                LogicApiForUICmd.getInstance().uiSetCmd(setValueCommand, uiCmdParams);
            } else {
                new Thread() {
                    public void run() {
                        UiCmdParams uiCmdParams = new UiCmdParams();
                        uiCmdParams.selectIndex = i;
                        LogicApiForUICmd.getInstance().uiSetCmd(setValueCommand, uiCmdParams);
                    }
                }.start();
            }
        }
        if (this.menu != null && parentMenu.getNeedRefresh()) {
            KtcMenuData ktcMenuData = new KtcMenuData();
            this.menuSecondList = getSecondMenuData(parentMenu);
            ktcMenuData.setItemTitle(((KtcTvMenuItem) this.menuSecondList.get(i)).getContent());
            this.menu.refreshSecondMenuItem(ktcMenuData, i);
        }
    }

    private void showFirstMenu(int i) {
        int i2;
        Exception e;
        ///if (this.menu == null) {
        ///Log.d("M")
            this.menu = new KtcCommenMenu(this.ctx);
        Log.d("Maxs","KtcTvMenuImple:showFirstMenu:rootMenuview == null = " + (rootMenuView == null ));
            if (this.rootMenuView != null) {
                Log.d("Maxs","rootMenuView = " + rootMenuView.toString());
                Log.d("Maxs","menu = " + menu.toString());
                Log.d("Maxs","KtcTvMenuImple:showFirstMenu:rootMenuview---11---- " );
                this.rootMenuView.addView(this.menu);

            }
            this.menu.setOnMenuOnKeyEventListener(this);
        ///}
        Log.d("Maxs","KtcTvMenuImple:showFirstMenu:(menu != null) = " + (menu != null));
        Log.d("Maxs","KtcTvMenuImple:showFirstMenu:menuFirstList.size() = " + menuFirstList.size());
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        for (int j  = 0; j < this.menuFirstList.size(); j++) {
            resetMenuFristList(j);
            KtcTvMenuItem ktcTvMenuItem = (KtcTvMenuItem) this.menuFirstList.get(j);
            ktcTvMenuItem.setDisplay(getIsFirstMenuDisplay(ktcTvMenuItem));
            if (ktcTvMenuItem.getDisplay()) {
                arrayList2.add(ktcTvMenuItem);
                KtcMenuData ktcMenuData = new KtcMenuData();
                ktcMenuData.setItemIconBg(R.drawable.ui_sdk_menu_icon_bg);
                if (ktcTvMenuItem.getFlipOutStatus()) {//是否有第二级菜单
                    ktcMenuData.setHasSecondMenu(false);
                } else if (ktcTvMenuItem.getNeedRefresh()) {
                    ktcMenuData.setHasSecondMenu(false);
                } else {
                    ktcMenuData.setHasSecondMenu(true);
                }
                getItemIcon(ktcMenuData, ktcTvMenuItem);
                ktcMenuData.setItemTitle(ktcTvMenuItem.getContent());
                arrayList.add(ktcMenuData);
            }
        }
        this.menuFirstList.clear();
        this.menuFirstList = arrayList2;
        int i3 = 0;
        Log.d("Maxs","-------.menu.getMenuDismissState() = -" + menu.getMenuDismissState());
        /*while (!this.menu.getMenuDismissState()) {
            try {
                Log.d("Maxs","-------------");
                StringBuilder append = new StringBuilder().append("showMenu time ");
                i2 = i3 + 1;
                try {
                ////    KtcTVDebug.error("langlang", append.append(i2).toString());
                    Thread.sleep(50);
                    i3 = i2;
                } catch (Exception e2) {
                    e = e2;
                    e.printStackTrace();
                    i3 = i2;
                    if (i3 > 30) {
                       //// KtcTVDebug.error("langlang", "showMenu > 1.5S!!!");
                        return;
                    }
                }
            } catch (Exception e3) {
                Exception exception = e3;
                i2 = i3;
                e = exception;
                e.printStackTrace();
                i3 = i2;
                if (i3 > 30) {
                   //// KtcTVDebug.error("langlang", "showMenu > 1.5S!!!");
                    return;
                }
            }
            if (i3 > 30) {
                ////KtcTVDebug.error("langlang", "showMenu > 1.5S!!!");
                return;
            }
        }*/
       ///// if (DolbyUtil.getInstance().isDolbyHdrValue()) {
         ////   this.menu.setFlagViewRes(R.drawable.dolby_icon_menu);
       //// } else {
           this.menu.setFlagViewRes(0);//去掉背景色?
       //// }
        Log.d("Maxs","----333----");
        this.menu.showMenu(arrayList);
        this.bShowMenu = true;
        this.curMenuRank = 0;
    }

    private void showFlipOutMenu(int i, boolean z) {
        KtcTvMenuItem ktcTvMenuItem;
        if (z) {
            if (this.menuFirstList == null || this.menuFirstList.size() < i + 1) {
              ////  KtcTVDebug.debug("langlang", "itemIndex error !!!");
                return;
            }
            ktcTvMenuItem = (KtcTvMenuItem) this.menuFirstList.get(i);
        } else if (this.menuSecondList == null || this.menuSecondList.size() < i + 1) {
           /// KtcTVDebug.debug("langlang", "showFlipOutMenu itemIndex error !!!");
            return;
        } else {
            ktcTvMenuItem = (KtcTvMenuItem) this.menuSecondList.get(i);
        }
        String flipOutCommand = ktcTvMenuItem.getFlipOutCommand();
      ////  KtcTVDebug.debug("langlang", "cmd = " + flipOutCommand);
        Log.d("Maxs","KtcTvMenuImple:cmd = " + flipOutCommand);
        try {
            LogicApiForUICmd.getInstance().uiSetCmd(flipOutCommand, null);
            MainMenuActivity.getInstance().finish();
        } catch (Exception e) {
           //// KtcTVDebug.debug("langlang", "showFlipOutMenu Exception !!!");
            e.printStackTrace();
        }
        try {
            Map hashMap = new HashMap();
            hashMap.put("source", this.curSourceEnum.toString());
            hashMap.put("menu_name", ktcTvMenuItem.getContent());
            ///KtcTvSubmitLog.submitAppLog(this.ctx, null, LOGTYPE.Action, "KtcTv", 3, hashMap, false);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void showSecondMenu(int i) {
        List arrayList = new ArrayList();
        Log.d("Maxs","KtcTvMenuImple:showSecondMenu:menuSecondList == null) = " + (menuSecondList == null));
        if (this.menuSecondList == null || this.menuSecondList.size() == 0) {
            ////KtcTVDebug.error("langlang", "menuSecondList error !!!");
            return;
        }

        for (int i2 = 0; i2 < this.menuSecondList.size(); i2++) {
            ////KtcTVDebug.debug("lwr", ">>showSecondMenu: " + ((KtcTvMenuItem) this.menuSecondList.get(i2)).getContent() + " getFlipOutCommand: " + ((KtcTvMenuItem) this.menuSecondList.get(i2)).getFlipOutCommand());
            KtcMenuData ktcMenuData;
            /*
            if (UILOGIC_TVMENU_COMMAND.DVBC_SHOW_GUIDE_MENU.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i2)).getFlipOutCommand())) {
                try {
                    int dtvBootGuideDisable = KtcTvController.getInstance().getDVBCApi().getDtvBootGuideDisable();
                    ////KtcTVDebug.debug("lwr", "bootGuideDisable" + dtvBootGuideDisable);
                    if (dtvBootGuideDisable == 0 || dtvBootGuideDisable == 1) {
                        ktcMenuData = new KtcMenuData();
                        String str = "";
                        switch (KtcTvController.getInstance().getDVBCApi().getDtvBootGuideDisable()) {
                            case 0:
                                str = UITextUtil.getInstance().getTextByKey("KTC_CFG_TV_DVBC_CLOSE_GUIDE");
                                break;
                            case 1:
                                str = UITextUtil.getInstance().getTextByKey("KTC_CFG_TV_DVBC_OPEN_GUIDE");
                                break;
                        }
                        ktcMenuData.setItemTitle(str);
                        ktcMenuData.setItemState(((KtcTvMenuItem) this.menuSecondList.get(i2)).getFlipOutStatus());
                        arrayList.add(ktcMenuData);
                    }
                } catch (Exception e) {
                    ////KtcTVDebug.debug("lwr", "DVBC_SHOW_GUIDE_MENU_Exception");
                    e.printStackTrace();
                }
            } else {
            */
                ktcMenuData = new KtcMenuData();
                ktcMenuData.setItemTitle(((KtcTvMenuItem) this.menuSecondList.get(i2)).getContent());
                ktcMenuData.setItemState(((KtcTvMenuItem) this.menuSecondList.get(i2)).getFlipOutStatus());
                arrayList.add(ktcMenuData);
           //// }
        }
        this.menu.showSecondMenu(arrayList, this.secondMenuIndex);
        try {
            Map hashMap = new HashMap();
            hashMap.put("source", this.curSourceEnum.toString());
            hashMap.put("menu_name", ((KtcTvMenuItem) this.menuSecondList.get(0)).getParentMenu().getContent());
            ////KtcTvSubmitLog.submitAppLog(this.ctx, null, LOGTYPE.Action, "KtcTv", 3, hashMap, false);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void showTipTost(int i) {
        /*String str = "";
        switch (i) {
            case 0:
                str = UITextUtil.getInstance().getTextByKey("open_tv_guide");
                break;
            case 1:
                str = UITextUtil.getInstance().getTextByKey("close_tv_guide");
                break;
        }
        KtcTvUI.getInstance().getSystemUI().showToast(str);*/
    }

    public boolean hideMenu() {
        ////KtcTVDebug.debug("langlang", "curMenuRank = " + this.curMenuRank + " secondMenuIndex = " + this.secondMenuIndex + " bShowMenu = " + this.bShowMenu);
        ////KtcTvConfig.getInstance().isNotShowMenuorSearchFlag = true;
        if (this.bShowMenu) {
            Message message;
            if (this.curMenuRank == 0) {
                message = new Message();
                message.arg1 = 0;
                message.what = 11;
                this.menuHandler.sendMessage(message);
            } else {
                message = new Message();
                message.arg1 = this.secondMenuIndex;
                message.what = 5;
                this.menuHandler.sendMessage(message);
            }
        }
        return true;
    }

    public boolean onFirstMenuItemOnClick(int position, View view, KtcMenuData ktcMenuData) {
        Log.d("Maxs","KtcTvMenuImple:onFirstMenuItemOnClick:position = " + position + " /view = " + view.toString() + " /ktcMenuData = " + ktcMenuData.getItemTitle());
        /////KtcTVDebug.debug("langlang", "onFirstMenuItemOnClick " + position);
        setSecondMenu(position);
        return true;
    }

    public boolean onFirstMenuItemOnKeyBack(int i, View view) {
        Log.d("Maxs","KtcTvMenuImple:onFirstMenuItemOnKeyBack");
        ////KtcTVDebug.debug("langlang", "onFirstMenuItemOnKeyBack " + i + " bShowMenu = " + this.bShowMenu);
        ////KtcTvConfig.getInstance().isNotShowMenuorSearchFlag = true;
        if (this.bShowMenu) {
            hideMenu(11, i);
        }
        MainMenuActivity.getInstance().finish();
        return true;
    }

    public boolean onFirstMenuItemOnKeyLeft(int i, View view) {
        /////KtcTVDebug.debug("langlang", "onFirstMenuItemOnKeyLeft " + i);
        return true;
    }

    public boolean onFirstMenuItemOnKeyOther(int i, View view, int i2) {
        ////KtcTVDebug.debug("langlang", "onFirstMenuItemOnKeyOther " + i + " " + i2);
        switch (i2) {
            case 19:
                if (i <= 0) {
                    this.firstMenuIndex = 0;
                    break;
                }
                this.firstMenuIndex = i - 1;
                break;
            case 20:
                if (this.menuFirstList != null && i < this.menuFirstList.size() - 1) {
                    this.firstMenuIndex = i + 1;
                    break;
                }
                this.firstMenuIndex = this.menuFirstList.size() - 1;
                break;

        }
        return false;
    }

    public boolean onFirstMenuItemOnKeyRight(int i, View view, KtcMenuData ktcMenuData) {
        Log.d("Maxs","KtcTvMenuImple:onFirstMenuItemOnKeyRight");

       //// KtcTVDebug.debug("langlang", "onFirstMenuItemOnKeyRight " + i);
        if (!((KtcTvMenuItem) this.menuFirstList.get(i)).getNeedRefresh()) {
            setSecondMenu(i);
        }

        return true;
    }

    public boolean onSecondMenuItemOnClick(int i, View view, KtcMenuData ktcMenuData) {
    	Log.d("Maxs","KtcTvMenuImple:onSecondMenuItemOnClick:i = " + i);
       //// KtcTVDebug.debug("langlang", "onSecondMenuItemOnClick " + i);
        if (this.menuSecondList == null || this.menuSecondList.size() < i + 1) {
           //// KtcTVDebug.debug("langlang", "showFlipOutMenu itemIndex error !!!");
            return false;
        }
       //// KtcTVDebug.debug("langlang", "getContent:" + ((KtcTvMenuItem) this.menuSecondList.get(i)).getContent());
       /////KtcTVDebug.debug("langlang", "cmd = " + ((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand());
        if (UILOGIC_TVMENU_COMMAND.ATV_SHOW_SEARCH_MENU.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.ATV_SHOW_CHANNEL_EDIT.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.DTMB_SHOW_SEARCH_MENU.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.DTMB_SHOW_CHANNEL_EDIT.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.DVBC_SHOW_SEARCH_MENU.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.DTMB_SHOW_CHANNELINFO_MENU.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.ATV_SHOW_CHANNELINFO_INFO.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.ATV_LOVECHANNEL_MENU.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.DTMB_LOVECHANNEL_MENU.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.ATV_CHANNEL_SORT.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.DTV_CHANNEL_SORT.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())
                || UILOGIC_TVMENU_COMMAND.DVBC_SHOW_CHANNELINFO_MENU.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())) {
            ////KtcTVDebug.debug("langlang", "-------------------------------------- ");
        } else {
            ////KtcTvConfig.getInstance().isNotShowMenuorSearchFlag = true;
        }
        /*
        if (UILOGIC_TVMENU_COMMAND.DVBC_SHOW_GUIDE_MENU.toString().equals(((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutCommand())) {
            Message obtainMessage = this.menuHandler.obtainMessage();
            try {
                if (KtcTvController.getInstance().getDVBCApi().getDtvBootGuideDisable() == 0) {
                    KtcTvController.getInstance().getDVBCApi().setDtvBootGuideDisable(1);
                    obtainMessage.arg2 = 1;
                } else if (KtcTvController.getInstance().getDVBCApi().getDtvBootGuideDisable() == 1) {
                    KtcTvController.getInstance().getDVBCApi().setDtvBootGuideDisable(0);
                    obtainMessage.arg2 = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            obtainMessage.what = 15;
            this.menuHandler.sendMessage(obtainMessage);
        }
        */
        if (((KtcTvMenuItem) this.menuSecondList.get(i)).getFlipOutStatus()) {
            boolean hideMenu = hideMenu(5, i);
            ////KtcTVDebug.debug("langlang hideMenu ret = " + hideMenu);
            if (hideMenu) {
                ////KtcTvController.getInstance().isWaitingShowFlipOutMenu = true;
                Message message = new Message();
                message.arg1 = i;
                message.what = 14;
                this.menuHandler.sendMessageDelayed(message, 450);
            }
        } else {
            this.curRefreshTime = System.currentTimeMillis();
            ////KtcTVDebug.debug("langlang " + this.curRefreshTime + " " + this.preRefreshTime);
            if (Math.abs(this.curRefreshTime - this.preRefreshTime) > 500) {
                setSecondMenuCommand(i);
                this.preRefreshTime = this.curRefreshTime;
                return true;
            }
        }

        return true;
    }

    public boolean onSecondMenuItemOnKeyBack(int id, View view) {
        ////KtcTVDebug.debug("langlang", "onSecondMenuItemOnKeyBack " + id);
        this.curMenuRank = 0;
        return true;
    }

    public boolean onSecondMenuItemOnKeyLeft(int id, View view) {
        ////KtcTVDebug.debug("langlang", "onSecondMenuItemOnKeyLeft " + id);
        this.curMenuRank = 0;
        return true;
    }

    public boolean onSecondMenuItemOnKeyOther(int id, View view, int keyCode) {
        ////KtcTVDebug.debug("langlang", "onSecondMenuItemOnKeyOther " + i + " " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (id <= 0) {
                    this.secondMenuIndex = 0;
                    break;
                }
                this.secondMenuIndex = id - 1;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (this.menuSecondList != null && id < this.menuSecondList.size() - 1) {
                    this.secondMenuIndex = id + 1;
                    break;
                }
                this.secondMenuIndex = this.menuSecondList.size() - 1;
                break;

        }
        return false;
    }

    public boolean onSecondMenuItemOnKeyRight(int i, View view, KtcMenuData ktcMenuData) {
        ////KtcTVDebug.debug("langlang", "onSecondMenuItemOnKeyRight " + i);
        return true;
    }

    public void resetTVMenuStatus() {
        if (this.menu != null && this.bShowMenu) {
            if (this.curMenuRank == 0) {
                this.menu.setFirstMenuFocus(this.firstMenuIndex);
            } else if (this.curMenuRank == 1) {
                this.menu.setSecondMenuFocus(this.secondMenuIndex);
            }
        }
    }

    public void setRootMenuView(FrameLayout fl, Context context) {
        if (this.rootMenuView == null) {
            ////this.rootMenuView = KtcTvApp.getInstance().getRootMenuView();
            this.ctx = context;
            rootMenuView = fl;
        }
    }
    public void setRootMenuView() {
        Log.d("Maxs","KtcTvMenuImple:setRootMenuView:this.rootMenuView == null = " + (this.rootMenuView == null));
       /// if (this.rootMenuView != null) {
            ////this.rootMenuView = KtcTvApp.getInstance().getRootMenuView();
            this.rootMenuView = MainMenuActivity.getInstance().getRootMenuView();
            this.ctx = MainMenuActivity.getInstance();
       /// }
    }

    /*public void setTVMenuStatus() {
    }*/

    public void showMainMenu(List<KtcTvMenuItem> list, Constants.SOURCE_NAME_ENUM source_name_enum) {
    ////public void showMainMenu(List<KtcTvMenuItem> list) {
        ////KtcTVDebug.debug("langlang", "showMainMenu = " + list.size() + " curSourceEnum = " + source_name_enum);
        ////KtcTvConfig.getInstance().isNotShowMenuorSearchFlag = false;
        ////this.curSourceEnum = source_name_enum;
        this.menuFirstList = list;
        this.menuHandler.sendEmptyMessage(10);
    }

}
