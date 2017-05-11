package com.horion.tv.service.epg;

import android.content.Context;
import android.util.Log;

import com.horion.tv.framework.ChannelListActivity;
import com.horion.tv.ui.leftepg.SkyEPGData;
import com.ktc.framework.ktcsdk.ipc.KtcApplication;
import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
///import com.ktc.framework.skysdk.logger.SkyLogger;
///import com.horion.media.api.SkyMediaApi;
import com.horion.tv.R;
///import com.horion.tv.api.tvApiforLogic.epg.SkyTvLogicEPGApi;
///import com.horion.tv.api.tvApiforLogic.system.SkyTvLogicSystemApi;
//import com.horion.tv.api.vod.SkyTvVODApi;
import com.horion.tv.define.object.Category;
import com.horion.tv.define.object.Category.CATEGORY_ENUM;
import com.horion.tv.define.object.Channel;
import com.horion.tv.define.object.Programme;
import com.horion.tv.define.object.Source;
import com.horion.tv.define.object.TvTime;
//import com.horion.tv.framework.ui.uidata.epg.ChannelCollectManager;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGCurProgItemData;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGCurProgItemData.ProgType;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGListDataBase;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGListItemDataBase;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGTVBriefData;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.vo.EnumServiceType;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
//import com.horion.tv.utils.SkyTVDebug;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SkyEPGApi {
    private static  int[] CATEGORY_ENUMSwitchesValues = null;
    private static final String COLLECT_CAT_ID = "cat_id:collect_category";
    private static final String DIS_CAT_ID = "cat_id:dis_category";
    private static SkyEPGApi instance = new SkyEPGApi();
    ///public SkyTvLogicEPGApi epgApi = null;
    public Context mContext;
    ///public SkyMediaApi mediaApi = null;
   /// public SkyTvLogicSystemApi systemApi = null;
   /// public SkyTvVODApi vodApi = null;

    private static  int[] getCATEGORY_ENUMSwitchesValues() {
        if (CATEGORY_ENUMSwitchesValues != null) {
            return CATEGORY_ENUMSwitchesValues;
        }
        int[] iArr = new int[CATEGORY_ENUM.values().length];
        try {
            iArr[CATEGORY_ENUM.ALL.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[CATEGORY_ENUM.CCTV.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[CATEGORY_ENUM.EXTERNAL.ordinal()] = 6;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[CATEGORY_ENUM.HD.ordinal()] = 3;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[CATEGORY_ENUM.OTHER.ordinal()] = 4;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[CATEGORY_ENUM.RADIO.ordinal()] = 7;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[CATEGORY_ENUM.STAR_TV.ordinal()] = 5;
        } catch (NoSuchFieldError e7) {
        }
        CATEGORY_ENUMSwitchesValues = iArr;
        return iArr;
    }

    public static SkyEPGApi getInstance() {
        return instance;
    }

    /* 获取频道类别列表:比如All,CCTV,HD,OTHERS
     *
     */
    public SkyEPGListDataBase getCategoryDataList(Context context) {
        SkyEPGListDataBase skyEPGListDataBase;
        int i = 0;
        synchronized (this) {
            skyEPGListDataBase = new SkyEPGListDataBase(SkyEPGListDataBase.categoryList);
            //ArrayList arrayList = new ArrayList();
           /// List<Category> channelCategory = getInstance().epgApi.getChannelCategory();
            ///SkyTVDebug.debug("getChannelCategory categoryList:" + channelCategory);
           /// if (channelCategory != null) {
               // SkyTVDebug.debug("getChannelCategory categoryList:" + channelCategory.size());
           /// }
            Category category = new Category(COLLECT_CAT_ID, context.getResources().getString(R.string.category_collect));
            try {
                //SkyTVDebug.debug("getChannelCategory before getCollectChannelList:");
                List collectChannelList = getInstance().getCollectChannelList();
                if (collectChannelList != null) {
                   // SkyLogger.d("lwr", "getChannelCategory getCollectChannelList size:" + collectChannelList.size());
                   // SkyTVDebug.debug("getChannelCategory getCollectChannelList size:" + collectChannelList.size());
                }
                if (collectChannelList != null && collectChannelList.size() > 0) {
                 ///   channelCategory.add(0, category);
                }
            } catch (Exception e) {
               /// SkyTVDebug.debug("getCollectChannelList Exception");
                e.printStackTrace();
            }
            ///SkyTVDebug.debug("getChannelCategory  categoryList size =" + channelCategory.size());
           /* for (Category category2 : channelCategory) {
                SkyEPGListItemDataBase skyEPGListItemDataBase = new SkyEPGListItemDataBase();
                ///SkyLogger.d("lwr", "getChannelCategory getCollectChannelList size:" + category2.id + "|" + category2.name);
                if (category2.category_enum != null) {
                    switch (getCATEGORY_ENUMSwitchesValues()[category2.category_enum.ordinal()]) {
                        case 1:
                            skyEPGListItemDataBase.setContent("全部");
                            break;
                        case 2:
                            skyEPGListItemDataBase.setContent("央视");
                            break;
                        case 3:
                            skyEPGListItemDataBase.setContent("高清");
                            break;
                        case 5:
                            skyEPGListItemDataBase.setContent("卫视");
                            break;
                        default:
                            skyEPGListItemDataBase.setContent("其他");
                            break;
                    }
                }
                skyEPGListItemDataBase.setContent(category2.name);
                category2.index = i;
                skyEPGListItemDataBase.setIndex(category2.index);
                skyEPGListItemDataBase.setObject(category2);
                ///SkyTVDebug.debug("getChannelCategory  name index =" + category2.name + "," + category2.index);
                skyEPGListDataBase.addCategoryItem(skyEPGListItemDataBase);
                i++;
            }
            skyEPGListDataBase.totalCount = channelCategory.size();
            skyEPGListDataBase.default_index = 0;*/
        }

        /* By Maxs,2017.03.08,鉴于进度的影响，这里频道类别只显示一种类别，也就是All列表
         * 诸如其他的HD，CCTV，OTHER类别，这里暂不显示,如果想显示其他类别，则可以根据
         * 频道名以及节目的属性来欺负CCTV和HD
         *
         */
        List<Category> channelCategory = new ArrayList<Category>();
        channelCategory.clear();
        Category category1 = new Category(DIS_CAT_ID,context.getString(R.string.category_all),CATEGORY_ENUM.ALL);
/*        Category category2 = new Category("12","CCTV",CATEGORY_ENUM.ALL);
        Category category3 = new Category("13","HD",CATEGORY_ENUM.CCTV);
        Category category4 = new Category("14","OTHER",CATEGORY_ENUM.CCTV);*/


        channelCategory.add(category1);
/*        channelCategory.add(category2);
        channelCategory.add(category3);
        channelCategory.add(category4);*/



        for (Category category : channelCategory) {
            SkyEPGListItemDataBase skyEPGListItemDataBase = new SkyEPGListItemDataBase();
            switch (category.category_enum.ordinal()) {
                case 1:
                    skyEPGListItemDataBase.setContent("全部");
                    break;
                case 2:
                    skyEPGListItemDataBase.setContent("央视");
                    break;
                case 3:
                    skyEPGListItemDataBase.setContent("高清");
                    break;
                case 5:
                    skyEPGListItemDataBase.setContent("卫视");
                    break;
                default:
                    skyEPGListItemDataBase.setContent("其他");
                    break;
            }
            ///skyEPGListItemDataBase.setContent("CCTV" + category2.index);
            category.index = i;
            skyEPGListItemDataBase.setIndex(category.index);
            skyEPGListItemDataBase.setObject(category);
            ///SkyTVDebug.debug("getChannelCategory  name index =" + category2.name + "," + category2.index);
            skyEPGListDataBase.totalCount = channelCategory.size();
            skyEPGListDataBase.default_index = 0;
            skyEPGListDataBase.addCategoryItem(skyEPGListItemDataBase);
        }


        return skyEPGListDataBase;
    }

    public void restoreDeleteChannel(SkyEPGData skyEPGData){
        //设置删除属性无效

            if (EnumServiceType.E_SERVICETYPE_ATV.ordinal() == skyEPGData.getServiceType()) {
                Log.d("Maxs60","DTV:skyEPGData.getChannelNumber() = " + (Integer.parseInt(skyEPGData.getChannelNumber()) - 1));
                Log.d("Maxs60","DTV:skyEPGData.getServiceType() = " + skyEPGData.getServiceType());
                int indexBase = TvChannelManager.getInstance()
                        .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
                if (0xFFFFFFFF == indexBase) {
                    indexBase = 0;
                }
                TvChannelManager.getInstance().setProgramAttribute(
                        TvChannelManager.PROGRAM_ATTRIBUTE_DELETE,
                        Integer.parseInt(skyEPGData.getChannelNumber()) - 1 + indexBase, skyEPGData.getServiceType(),
                        0x00, false);
                TvChannelManager.getInstance().changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
            } else if (EnumServiceType.E_SERVICETYPE_DTV.ordinal() == skyEPGData.getServiceType()) {
                Log.d("Maxs60","DTV:skyEPGData.getChannelNumber() = " + skyEPGData.getChannelNumber());
                Log.d("Maxs60","DTV:skyEPGData.getServiceType() = " + skyEPGData.getServiceType());
                TvChannelManager.getInstance().setProgramAttribute(
                        TvChannelManager.PROGRAM_ATTRIBUTE_DELETE,
                        Integer.parseInt(skyEPGData.getChannelNumber()), skyEPGData.getServiceType(),
                        0x00, false);
                TvChannelManager.getInstance().changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
            }

    }

    public List<Channel> getChannelListByCategory(Boolean bool, Category category) {
        List<Channel> collectList = null;
        synchronized (this) {
            ArrayList arrayList = new ArrayList();
            if (bool.booleanValue()) {
                if (category.id.equals(COLLECT_CAT_ID)) {
                   /// SkyTVDebug.debug("getChannelList COLLECT_CAT_ID");
                    //collectList = ChannelCollectManager.getInstance().getCollectList(getInstance().systemApi.getCurrentSource());
                } else {
                   /// SkyTVDebug.debug("getChannelList>>>22");
                    ///collectList = getInstance().epgApi.getChannelListByCategory(category);
                    collectList = getChannelListByCategory(category);
                }
            } else if (category.id.equals(COLLECT_CAT_ID)) {
               /// SkyTVDebug.debug("getChannelList COLLECT_CAT_ID");
                ///collectList = ChannelCollectManager.getInstance().getCollectList(getInstance().systemApi.getCurrentSource());
            } else {
               /// collectList = getInstance().epgApi.getChannelListByCategory(category);
            }
           /// SkyTVDebug.debug("channle list count=" + collectList.size());
        }
        return collectList;
    }

    public List<Channel> getCollectChannelList() {
       /* return ChannelCollectManager.getInstance().getCollectList(this.systemApi.getCurrentSource());*/
       return  null;
    }

    public boolean sortChannel(SkyEPGData skyEPGData, int sortTo){
        int m_u32Source = 0;
        int m_u32Target = 0;
        int m_dtvServiceNum = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if(curInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            m_u32Source = m_dtvServiceNum+ skyEPGData.getItemIndexID();
            m_u32Target = m_dtvServiceNum + (sortTo - 1);
            Log.d("Maxs30","ATV:sortchannel:skyEPGData.getItemIndexID() = " + skyEPGData.getItemIndexID());
            Log.d("Maxs30","ATV:sortchannel:m_u32Target = " + m_u32Target);
        }else{
            m_u32Source = skyEPGData.getItemIndexID();
            m_u32Target = sortTo - 1;
            Log.d("Maxs30","DTV:sortchannel:skyEPGData.getItemIndexID() = " + skyEPGData.getItemIndexID());
            Log.d("Maxs30","DTV:sortchannel:m_u32Target = " + m_u32Target);
        }
        if (m_u32Source != m_u32Target){
            TvChannelManager.getInstance().moveProgram(m_u32Source, m_u32Target);
            if(skyEPGData.getServiceType() == TvChannelManager.SERVICE_TYPE_ATV){
               /* TvChannelManager.getInstance().selectProgram(Integer.parseInt(skyEPGData.getChannelNumber()),
                        skyEPGData.getServiceType());*/
            }else if (skyEPGData.getServiceType() == TvChannelManager.SERVICE_TYPE_DTV) {
               /* TvChannelManager.getInstance().playDtvCurrentProgram();*/
            }
        }
        return false;
    }

    public List<SkyEPGData> getEpgDataList() {
        List<SkyEPGData> uiChannelItemList = new ArrayList<SkyEPGData>();
        /*SkyEPGData skyEPGData  = new SkyEPGData();
        skyEPGData.setChannelNumber("11");
        skyEPGData.setChannelType(Channel.CHANNEL_TYPE.TV);
        skyEPGData.setItemTitle("CCTV1");
        skyEPGData.setItemIndexID(0);

        uiChannelItemList.add(skyEPGData);*/

        Log.d("Maxs25","---->SkyEPGApi:getEpgDataList");
        int m_nServiceNum = 0;
        int indexBase = 0;
        ProgramInfo pgi = null;
        int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (currInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            indexBase = TvChannelManager.getInstance()
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            if (0xFFFFFFFF == indexBase) {
                indexBase = 0;
            }
            m_nServiceNum = TvChannelManager.getInstance()
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);

        } else {
            indexBase = 0;
            m_nServiceNum = TvChannelManager.getInstance()
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        }
        for (int k = indexBase, j = 0; k < m_nServiceNum; k++) {
            pgi = TvChannelManager.getInstance().getProgramInfoByIndex(k);

            if (pgi != null) {
                SkyEPGData skyEPGData  = new SkyEPGData();
                    if ((pgi.isDelete == true) || (pgi.isVisible == false)) {
                        Log.d("Maxs31","delete:pgi.number = " + pgi.number);
                        Log.d("Maxs31","delete:pgi.serviceType = " + pgi.serviceType);
                        continue;
                    } else {
                        if (currInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                            skyEPGData.setChannelNumber(String.valueOf(pgi.number + 1));
                            skyEPGData.setItemTitle("模拟电视"+String.valueOf(pgi.number + 1));
                        } else {
                            skyEPGData.setChannelNumber(String.valueOf(pgi.number));
                            skyEPGData.setItemTitle(pgi.serviceName);
                        }
                        skyEPGData.setChannelType(Channel.CHANNEL_TYPE.TV);
                        skyEPGData.setServiceType(pgi.serviceType);
                        Log.d("Maxs25","setItemIndex11ID:1111");
                        skyEPGData.setItemIndexID(j++);
                        uiChannelItemList.add(skyEPGData);
                    }
                }

        }
        return  uiChannelItemList;
    }

    public List<SkyEPGData> getDeletedChannelList(){
        List<SkyEPGData> deleteList = new ArrayList<SkyEPGData>();
        int m_nServiceNum = 0;
        int indexBase = 0;
        ProgramInfo pgi = null;
        int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (currInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            indexBase = TvChannelManager.getInstance()
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            if (0xFFFFFFFF == indexBase) {
                indexBase = 0;
            }
            m_nServiceNum = TvChannelManager.getInstance()
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
            Log.d("Maxs31","ATV:indexBase = " + indexBase + " /m_nServiceNum = " +m_nServiceNum);

        } else {
            indexBase = 0;
            m_nServiceNum = TvChannelManager.getInstance()
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            Log.d("Maxs31","DTV:indexBase = " + indexBase + " /m_nServiceNum = " +m_nServiceNum);
        }
        for (int k = indexBase, j = 0; k < m_nServiceNum; k++) {
            pgi = TvChannelManager.getInstance().getProgramInfoByIndex(k);
            Log.d("Maxs31","---->pgi.isDelete = " + pgi.isDelete);
            if (pgi != null) {
                SkyEPGData skyEPGData  = new SkyEPGData();
                if (pgi.isDelete == true) {
                    if (currInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                        skyEPGData.setChannelNumber(String.valueOf(pgi.number + 1 - indexBase));
                        skyEPGData.setItemTitle("模拟电视"+String.valueOf(pgi.number + 1- indexBase));
                    } else {
                        skyEPGData.setChannelNumber(String.valueOf(pgi.number));
                        skyEPGData.setItemTitle(pgi.serviceName);
                    }
                    Log.d("Maxs31","--->deleteName:pgi.serviceName = " + pgi.serviceName);

                    skyEPGData.setChannelType(Channel.CHANNEL_TYPE.TV);
                    skyEPGData.setServiceType(pgi.serviceType);
                    skyEPGData.setItemIndexID(j++);
                    deleteList.add(skyEPGData);
                }
            }

        }
        return  deleteList;
    }

    public int getCurrentChannelIndex(){
        int focusIndex = 0;
        List<SkyEPGData> progInfoList = getEpgDataList();
        ProgramInfo cpi = TvChannelManager.getInstance().getCurrentProgramInfo();
        Log.d("Maxs30","------->cpi.number = " + cpi.number);
        Log.d("Maxs30","------->cpi.serviceType = " + cpi.serviceType);
        int currentInput = TvCommonManager.getInstance().getCurrentTvInputSource();
        for (SkyEPGData skyEPGData : progInfoList) {
            Log.d("Maxs30","-------->Integer.parseInt(skyEPGData.getChannelNumber() = " + Integer.parseInt(skyEPGData.getChannelNumber()) + "");
            Log.d("Maxs30","-------->skyEPGData.getServiceType() = " + skyEPGData.getServiceType() + "");
            if (cpi.number == Integer.parseInt(skyEPGData.getChannelNumber())
                    && cpi.serviceType == skyEPGData.getServiceType()) {
                focusIndex = progInfoList.indexOf(skyEPGData);

                //对于ATV,去掉已删除的频道,否则会数组越界.因为DTV是删除后是重新排序的，ATV
                int focusIndexTemp = focusIndex + 1;
                if (currentInput == TvCommonManager.INPUT_SOURCE_ATV){
                    List<SkyEPGData> prgList = getDeletedChannelList();
                    if (prgList != null && prgList.size() > 0){
                        focusIndex += 1;
                        for (SkyEPGData skyEPGData1 : prgList){
                            if (Integer.parseInt(skyEPGData1.getChannelNumber()) >= focusIndexTemp) {
                                break;
                            }else{
                                focusIndex--;
                            }
                        }

                    }else{
                        focusIndex += 1;
                    }

                }else{
                    ///focusIndex += 1;
                }
                Log.d("Maxs30","----->focusIndex = " + focusIndex);
                break;
            }
        }
        return focusIndex;
    }


    public void  swithcChannel(int proNumber, int serviceType){
        Log.d("Maxs26","proNumber = " + proNumber + " /serviceType = " + serviceType);
        int currSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (currSource == TvCommonManager.INPUT_SOURCE_ATV){
            proNumber -= 1;
        }
        if (!isSameWithCurrentProgram(proNumber,serviceType)) {
            if (serviceType < TvChannelManager.SERVICE_TYPE_INVALID) {

                TvChannelManager.getInstance().selectProgram(proNumber,
                        serviceType);

            }
        }
    }

    private boolean isSameWithCurrentProgram(int proNumber, int serviceType) {
        boolean ret = false;
        ProgramInfo curProgInfo = TvChannelManager.getInstance().getCurrentProgramInfo();

        if ((curProgInfo.number == proNumber)
                && (curProgInfo.serviceType == serviceType)) {
            ret = true;
        }
        return ret;
    }

    public Channel getCurrentChannel() {
        Channel currentChannel = null;
        synchronized (this) {
            ///currentChannel = getInstance().systemApi.getCurrentChannel();
        }
        return currentChannel;
    }

    public SkyEPGListDataBase getCurrentEPGEvent(Channel channel) {
        SkyEPGListDataBase skyEPGListDataBase = null;
        synchronized (this) {
            ////SkyTVDebug.debug(">ZC<  getCurrentEPGEvent = " + channel.name);
           /* List epgEvent = getInstance().epgApi.getEpgEvent(channel, new TvTime());
            skyEPGListDataBase = new SkyEPGListDataBase("event");
            LinkedList<Programme> linkedList = new LinkedList();
            List epgEvent2 = getInstance().epgApi.getEpgEvent(getCurrentChannel(), new TvTime());
            int size = epgEvent.size();
            for (int i = 0; i < size; i++) {
                linkedList.add((Programme) epgEvent2.get(i));
            }
            for (Programme programme : linkedList) {
                SkyEPGCurProgItemData skyEPGCurProgItemData = new SkyEPGCurProgItemData(programme.startTime.name, programme.displayName, ProgType.NULL);
                ///SkyTVDebug.debug(">ZC<  getCurrentEPGEvent =" + programme.displayName);
                skyEPGListDataBase.addEventItem(skyEPGCurProgItemData);
            }
            skyEPGListDataBase.totalCount = epgEvent2.size();*/
        }
        return skyEPGListDataBase;
    }

    public Source getCurrentSource() {
        /*return this.systemApi.getCurrentSource();*/
        return  null;
    }

    public SkyEPGListDataBase getEPGEvent(Channel channel, TvTime tvTime) {
        SkyEPGListDataBase skyEPGListDataBase;
        synchronized (this) {
            skyEPGListDataBase = new SkyEPGListDataBase("event");
            LinkedList<Programme> linkedList = new LinkedList();
            ///List epgEvent = getInstance().epgApi.getEpgEvent(channel, tvTime);
            List epgEvent = null;
            int size = epgEvent.size();
            for (int i = 0; i < size; i++) {
                linkedList.add((Programme) epgEvent.get(i));
            }
            for (Programme programme : linkedList) {
                skyEPGListDataBase.addEventItem(new SkyEPGCurProgItemData(programme.startTime.formatToString("HH:mm"), programme.displayName, ProgType.NULL));
            }
            skyEPGListDataBase.totalCount = epgEvent.size();
        }
        return skyEPGListDataBase;
    }

    public SkyEPGTVBriefData getEPGEventFromNow(Channel channel) {
        Throwable th;
        SkyEPGTVBriefData skyEPGTVBriefData = null;
        synchronized (this) {
            try {
                ///SkyTVDebug.debug("getEPGEventFromNow:" + channel.name);
               /// List epgEventFromNow = getInstance().epgApi.getEpgEventFromNow(channel);
                List epgEventFromNow = null;
                if (epgEventFromNow == null) {
                  ///  SkyTVDebug.debug("getEPGEventFromNow list size 0 ");
                } else {
                    //SkyTVDebug.debug("getEPGEventFromNow list size = " + epgEventFromNow.size());
                }
                if (epgEventFromNow.isEmpty()) {
                } else {
                    Programme programme;
                    Programme programme2 = new Programme("", "");
                    Programme programme3 = new Programme("", "");
                    programme3 = new Programme("", "");
                    //SkyTVDebug.debug("getChannelLiveThumb channel:" + channel.getDisplayName());
                    if (epgEventFromNow.size() > 0) {
                        programme = (Programme) epgEventFromNow.get(0);
                        programme3 = programme;
                        skyEPGTVBriefData = new SkyEPGTVBriefData(programme.displayName, programme.startTime.formatToString("HH:mm"), "", "");
                    } else {
                        programme3 = programme2;
                    }
                    try {
                        if (epgEventFromNow.size() > 1) {
                            programme = (Programme) epgEventFromNow.get(1);
                            skyEPGTVBriefData = new SkyEPGTVBriefData(programme3.displayName, programme3.startTime.formatToString("HH:mm"), programme.displayName, programme.startTime.formatToString("HH:mm"));
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
                return skyEPGTVBriefData;
            } catch (Throwable th3) {
                th = th3;
                ///throw th;
            }
        }
        return  null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.horion.tv.framework.ui.uidata.epg.SkyEPGListDataBase getTimeEvent(List<com.horion.tv.define.object.TvTime> r8) {
        /*
        r7 = this;
        monitor-enter(r7);
        r1 = new com.horion.tv.framework.ui.uidata.epg.SkyEPGListDataBase;	 Catch:{ all -> 0x004b }
        r0 = "time";
        r1.<init>(r0);	 Catch:{ all -> 0x004b }
        if (r8 == 0) goto L_0x0010;
    L_0x000a:
        r0 = r8.size();	 Catch:{ all -> 0x004b }
        if (r0 != 0) goto L_0x0013;
    L_0x0010:
        monitor-exit(r7);
        r0 = 0;
    L_0x0012:
        return r0;
    L_0x0013:
        r2 = "一";
        r3 = new com.horion.tv.define.object.TvTime;	 Catch:{ all -> 0x004b }
        r3.<init>();	 Catch:{ all -> 0x004b }
        r4 = r8.iterator();	 Catch:{ all -> 0x004b }
    L_0x001e:
        r0 = r4.hasNext();	 Catch:{ all -> 0x004b }
        if (r0 == 0) goto L_0x0068;
    L_0x0024:
        r0 = r4.next();	 Catch:{ all -> 0x004b }
        r0 = (com.horion.tv.define.object.TvTime) r0;	 Catch:{ all -> 0x004b }
        r5 = r0.getDay();	 Catch:{ all -> 0x004b }
        r6 = r0.sameday(r3);	 Catch:{ all -> 0x004b }
        if (r6 == 0) goto L_0x004e;
    L_0x0034:
        r2 = "今天";
        r0 = r8.indexOf(r0);	 Catch:{ all -> 0x004b }
        r1.default_index = r0;	 Catch:{ all -> 0x004b }
    L_0x003c:
        r0 = r2;
    L_0x003d:
        r2 = new com.horion.tv.framework.ui.uidata.epg.SkyEPGTimeItemData;	 Catch:{ all -> 0x004b }
        r5 = java.lang.String.valueOf(r5);	 Catch:{ all -> 0x004b }
        r2.<init>(r5, r0);	 Catch:{ all -> 0x004b }
        r1.addTimeItem(r2);	 Catch:{ all -> 0x004b }
        r2 = r0;
        goto L_0x001e;
    L_0x004b:
        r0 = move-exception;
        monitor-exit(r7);
        throw r0;
    L_0x004e:
        r6 = r3.nextDay();	 Catch:{ all -> 0x004b }
        r6 = r0.sameday(r6);	 Catch:{ all -> 0x004b }
        if (r6 == 0) goto L_0x005b;
    L_0x0058:
        r0 = "明天";
        goto L_0x003d;
    L_0x005b:
        r6 = r3.lastDay();	 Catch:{ all -> 0x004b }
        r0 = r0.sameday(r6);	 Catch:{ all -> 0x004b }
        if (r0 == 0) goto L_0x003c;
    L_0x0065:
        r0 = "昨天";
        goto L_0x003d;
    L_0x0068:
        r0 = r8.size();	 Catch:{ all -> 0x004b }
        r1.totalCount = r0;	 Catch:{ all -> 0x004b }
        monitor-exit(r7);
        r0 = r1;
        goto L_0x0012;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.horion.tv.service.epg.SkyEPGApi.getTimeEvent(java.util.List):com.horion.tv.framework.ui.uidata.epg.SkyEPGListDataBase");
    }

    public void init(KtcApplication.KtcCmdConnectorListener skyCmdConnectorListener, Context context) {
       /* this.vodApi = new SkyTvVODApi(skyCmdConnectorListener);
        this.epgApi = new SkyTvLogicEPGApi(skyCmdConnectorListener);
        this.systemApi = new SkyTvLogicSystemApi(skyCmdConnectorListener);
        this.mediaApi = new SkyMediaApi(skyCmdConnectorListener);*/
        this.mContext = context;
    }

    public List<Channel> getChannelListByCategory(Category category){
        return null;
    }
}
