package com.horion.tv.service.menu;

import android.content.Context;
import android.util.Log;
////import com.ktc.framework.ktcsdk.android.KtcSystemUtil;
////import com.horion.tv.KtcTvApp;
////import com.horion.tv.define.object.Source;
////import com.horion.tv.define.object.Source.SOURCE_NAME_ENUM;
import com.horion.tv.KtcTvApp;
import com.horion.tv.service.base.KtcTvController;
import com.horion.tv.service.menu.docment.TCXmlNode;
import com.horion.tv.util.Constants.SOURCE_NAME_ENUM;
////import com.horion.tv.service.base.KtcTvController;
////import com.horion.tv.service.dtv.DtvDigitalUiManager;
import com.horion.tv.service.menu.docment.KtcBaseDocment;
import com.horion.tv.service.menu.docment.TCMenuDocument;
import com.horion.tv.service.menu.docment.TCMenuNode;
import com.horion.tv.util.AssectsUtil;
import com.horion.tv.utils.UITextUtilInterface;
////import com.horion.tv.utils.KtcTVDebug;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KtcTvMenuManager {
    private static  int[] sourceEnum2IntArry;
    private static KtcTvMenuManager instance = null;
    private final String MENUDIR = "menu";
    private final String TAG = "langlang";
    private Context ctxServiceContext = null;
    private SOURCE_NAME_ENUM curSourceEnum = SOURCE_NAME_ENUM.ATV;
    private HashMap<SOURCE_NAME_ENUM, KtcBaseDocment> docMap = new HashMap();
    private HashMap<SOURCE_NAME_ENUM, List<KtcTvMenuItem>> menuMap = new HashMap();

    private static int[] getSourceEnum2IntArry() {
        if (sourceEnum2IntArry != null) {
            return sourceEnum2IntArry;
        }
        int[] iArr = new int[SOURCE_NAME_ENUM.values().length];
        try {
            iArr[SOURCE_NAME_ENUM.ATV.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.AV.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.DTMB.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.DVBC.ordinal()] = 4;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.EXTERNAL.ordinal()] = 5;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.HDMI.ordinal()] = 6;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.IPLIVE.ordinal()] = 7;
        } catch (NoSuchFieldError e7) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.REMEMBER.ordinal()] = 10;
        } catch (NoSuchFieldError e8) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.VGA.ordinal()] = 8;
        } catch (NoSuchFieldError e9) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.VOD.ordinal()] = 11;
        } catch (NoSuchFieldError e10) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.YUV.ordinal()] = 9;
        } catch (NoSuchFieldError e11) {
        }
        sourceEnum2IntArry = iArr;
        return iArr;
    }

    private KtcTvMenuManager() {
    }

    public static KtcTvMenuManager getInstance() {
        synchronized (KtcTvMenuManager.class) {
            try {
                if (instance == null) {
                    instance = new KtcTvMenuManager();
                }
                KtcTvMenuManager ktcTvMenuManager = instance;
                return ktcTvMenuManager;
            } finally {
                Object obj = KtcTvMenuManager.class;
            }
        }
    }

    private boolean getIsSecondMenuDisplay(String str) {
        /////return str.equals("KTC_CFG_TV_DTMB_DTV_INFO") ? !(this.curSourceEnum == SOURCE_NAME_ENUM.DTMB && KtcTvController.getInstance().isTvPluginSupportedSource(Source.DTMB())) && DtvDigitalUiManager.getInstance().loadCaUIApk() : true;
        return str.equals("KTC_CFG_TV_DTMB_DTV_INFO") ?
                !(this.curSourceEnum == SOURCE_NAME_ENUM.DTMB )  : true;

    }


    private List<KtcTvMenuItem> getMenuByName(String str, KtcBaseDocment ktcBaseDocment) {
        List<KtcTvMenuItem> arrayList = new ArrayList();
        ArrayList childs = ((TCMenuNode) ktcBaseDocment.getItemsByName(str.toString())).getChilds();
        Log.d("Maxs","getMenuByName:childs = " + childs.size());
        for (int i = 0; i < childs.size(); i++) {
            KtcTvMenuItem ktcTvMenuItem = new KtcTvMenuItem();
            TCMenuNode tCMenuNode = (TCMenuNode) childs.get(i);
            ktcTvMenuItem.setNodeKey(tCMenuNode.getName());
            ktcTvMenuItem.setFlipOutStatus(tCMenuNode.isFlipout());
            ktcTvMenuItem.setGetValueCommand(tCMenuNode.getGetvalueCmd());
            ktcTvMenuItem.setSetValueCommand(tCMenuNode.getSetvalueCmd());
            ktcTvMenuItem.setFlipOutCommand(tCMenuNode.getCommand());
            ktcTvMenuItem.setNeedRefresh(tCMenuNode.isNeedRefresh());
            ktcTvMenuItem.setContent(UITextUtilInterface.UITextUtil.getInstance().getTextByKey(tCMenuNode.getName()));
            Log.d("Maxs","tCMenuNode.getName() = " + tCMenuNode.getName());

            ArrayList<TCXmlNode> childs2 = ((TCMenuNode) ktcBaseDocment.getItemsByName(tCMenuNode.getName())).getChilds();
            Log.d("Maxs","KtcTvMenuManager:getMenuByName:childs2.size = " + childs2.size());
            List<KtcTvMenuItem> arrayList2 = new ArrayList<KtcTvMenuItem>();
            for (int i2 = 0; i2 < childs2.size(); i2++) {
                TCMenuNode tCMenuNode2 = (TCMenuNode) childs2.get(i2);

                if (getIsSecondMenuDisplay(tCMenuNode2.getName())) {
                    KtcTvMenuItem ktcTvMenuItem2 = new KtcTvMenuItem();
                    ktcTvMenuItem2.setNodeKey(tCMenuNode2.getName());
                    ktcTvMenuItem2.setContent(UITextUtilInterface.UITextUtil.getInstance().getTextByKey(tCMenuNode2.getName()));
                    ktcTvMenuItem2.setFlipOutStatus(tCMenuNode2.isFlipout());
                    ktcTvMenuItem2.setGetValueCommand(tCMenuNode2.getGetvalueCmd());
                    ktcTvMenuItem2.setSetValueCommand(tCMenuNode2.getSetvalueCmd());
                    ktcTvMenuItem2.setFlipOutCommand(tCMenuNode2.getCommand());
                    ktcTvMenuItem2.setParentMenu(ktcTvMenuItem);
                    arrayList2.add(ktcTvMenuItem2);

                }
            }
            ////KtcTVDebug.debug("langlang", tCMenuNode.getName() + " " + arrayList2.size());
            ktcTvMenuItem.setChildMenuList(arrayList2);
            String nodeKey = ktcTvMenuItem.getNodeKey();
            boolean isSupportStbSmartRc;
            if (nodeKey.equals("KTC_CFG_INTELLIGENT_IR_MODE")) {
                ////isSupportStbSmartRc = KtcTvApp.getInstance().getStbSettingApi().isSupportStbSmartRc();
                ////KtcTVDebug.debug(" KTC_CFG_INTELLIGENT_IR_MODE isSupportStbSmartRc:" + isSupportStbSmartRc);
               //// if (isSupportStbSmartRc) {
               ////     arrayList.add(ktcTvMenuItem);
              ///  }
            } else if (nodeKey.equals("KTC_CFG_TV_3D_SET")) {
               ///// isSupportStbSmartRc = KtcSystemUtil.isSupport3D();
                ////KtcTVDebug.debug("isSupport3D():" + isSupportStbSmartRc);
                ////if (isSupportStbSmartRc) {
                ////    arrayList.add(ktcTvMenuItem);
               /// }
            } else {
                arrayList.add(ktcTvMenuItem);
            }
        }
        return arrayList;
    }


    private String getTvMenuPath(SOURCE_NAME_ENUM source_name_enum) {
        switch (getSourceEnum2IntArry()[source_name_enum.ordinal()]) {
            case 1:
                return "atv_menu.xml";
            case 2:
                return "av_menu.xml";
            case 3:
                return "dtmb_menu.xml";
            case 4:
                return "dvbc_menu.xml";
            case 6:
                return "hdmi_menu.xml";
            case 7:
                return "iplive_menu.xml";
            case 8:
                return "vga_menu.xml";
            case 9:
                return "yuv_menu.xml";
            default:
                return "atv_menu.xml";
        }
    }

    private KtcBaseDocment initDocument(SOURCE_NAME_ENUM source_name_enum) {
        ////KtcTVDebug.debug("langlang", "initDocument " + source_name_enum);
        Log.d("Maxs","initDocument:getTvMenuPath(source_name_enum) = " + getTvMenuPath(source_name_enum));
        String assetsMenufile = AssectsUtil.getAssetsMenufile(this.ctxServiceContext, "menu", getTvMenuPath(source_name_enum));
        if (assetsMenufile == null) {
            ////KtcTVDebug.error("langlang", "assetsMenu not exsit!");
            Log.d("Maxs","assetsMenu not exsit!");
            return null;
        }
        ///KtcTVDebug.debug("langlang resPath = " + assetsMenufile);
        KtcBaseDocment tCMenuDocument = new TCMenuDocument(assetsMenufile, source_name_enum.toString());
        tCMenuDocument.init();
        return tCMenuDocument;
    }

    private boolean processKey(SOURCE_NAME_ENUM source_name_enum) {
    ////public List<KtcTvMenuItem> processKey(SOURCE_NAME_ENUM source_name_enum) {
        List<KtcTvMenuItem> list = (List<KtcTvMenuItem>) this.menuMap.get(source_name_enum);
        if (list == null || list.size() == 0) {
            KtcBaseDocment ktcBaseDocment = (KtcBaseDocment) this.docMap.get(source_name_enum);
            if (ktcBaseDocment == null) {
                ktcBaseDocment = initDocument(source_name_enum);
                if (ktcBaseDocment == null) {
                    /////KtcTVDebug.debug("langlang", source_name_enum + " mDoc == null !!!");
                    return false;
                }
                this.docMap.put(source_name_enum, ktcBaseDocment);
            }
            Log.d("Maxs","processKey:1111");
            list = getMenuByName(source_name_enum.toString(), ktcBaseDocment);
            this.menuMap.put(source_name_enum, list);
        }
        showMainMenu(list);
        return true;
    }

    private void showMainMenu(List<KtcTvMenuItem> list) {
        if (list.size() == 0) {
            //KtcTVDebug.debug("langlang", "menuList is null !!!");
            return;
        }
        Log.d("Maxs","------------------------KtcTvMenuMenuManager:showManMenu------------------------");
        KtcTvApp.getInstance().createKtcTvController(ctxServiceContext);
        KtcTvApp.getInstance().getKtcTvMenuImple().setRootMenuView();
        KtcTvApp.getInstance().getKtcTvMenuImple().showMainMenu(list, this.curSourceEnum);

    }

    public void init(Context context) {
        this.ctxServiceContext = context;
    }

    public void loadAssetsMenufile() {
        AssectsUtil.loadAssetsDirfile(this.ctxServiceContext, "menu");
    }

    public boolean onKeyMenu() {
        ////KtcTVDebug.debug("langlang", "onKeyMenu = " + source.getDisplayName());
       //// if (KtcTvApp.getInstance().getKtcTvMenuImple().bShowMenu) {
          ////  KtcTVDebug.debug("langlang", "menu bShowMenu = true");
       ///     return false;
       /// }
       //// KtcTvController.getInstance();
        ////if (KtcTvController.bSwitchingChannel) {
            ////KtcTVDebug.debug("langlang", "bSwitchingChannel = true");
            ////return false;
       //// }
        this.curSourceEnum = KtcTvController.getInstance().getSourceNameEnum();
        Log.d("Maxs","this.curSourceEnum.ordinal() = " + this.curSourceEnum.ordinal());
        Log.d("Maxs","onKeyMenu:getSourceEnum2IntArry()[this.curSourceEnum.ordinal()] = " + getSourceEnum2IntArry()[this.curSourceEnum.ordinal()]);
        switch (getSourceEnum2IntArry()[this.curSourceEnum.ordinal()]) {
            case 1:
                processKey(SOURCE_NAME_ENUM.ATV);
                break;
            case 2:
                processKey(SOURCE_NAME_ENUM.AV);
                break;
            case 3:
                processKey(SOURCE_NAME_ENUM.DTMB);
                break;
            ////case 4:
                ////if (!DtvDigitalUiManager.getInstance().getShowDigitalUI()) {
                ////    processKey(SOURCE_NAME_ENUM.DVBC);
               ///     break;
               /// }
               /// KtcTVDebug.debug("bShowDigitalUI true");
              ///  return false;
          ///  case 5:
           ////     if (source.equals(Source.External("KtcIPTV"))) {
           /////         processKey(SOURCE_NAME_ENUM.IPLIVE);
           ////         break;
          ///      }
           ////     break;
            case 6:
                processKey(SOURCE_NAME_ENUM.HDMI);
                break;
            case 7:
                processKey(SOURCE_NAME_ENUM.IPLIVE);
                break;
            case 8:
                processKey(SOURCE_NAME_ENUM.VGA);
                break;
            case 9:
                processKey(SOURCE_NAME_ENUM.YUV);
                break;
            default:
                processKey(SOURCE_NAME_ENUM.ATV);
                break;
        }
        return true;
    }
}
