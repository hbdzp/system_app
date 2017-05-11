package com.horion.tv.service.epg;

import android.content.Context;
import android.util.Log;

import com.horion.tv.R;
import com.horion.tv.define.object.Category;
import com.horion.tv.define.object.Category.CATEGORY_ENUM;
import com.horion.tv.define.object.Channel;
import com.horion.tv.define.object.Source;
import com.horion.tv.define.object.Source.SOURCE_NAME_ENUM;
import com.horion.tv.framework.epg.SkyLeftEPGConstant.CATTYPE;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGListDataBase;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGListItemDataBase;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGTVBriefData;
///import com.horion.tv.framework.ui.util.EpgRunnable;
///import com.horion.tv.framework.ui.util.EpgThread;
import com.horion.tv.framework.ui.util.EpgRunnable;
import com.horion.tv.framework.ui.util.EpgThread;
import com.horion.tv.logic.ExternalApi;
import com.horion.tv.service.epg.SkyLeftEPGParam.GET_REMAINCHANNEL_MODE;
import com.horion.tv.service.epg.SkyLeftEPGParam.GET_REMAINCHANNEL_PARAM;
import com.horion.tv.service.epg.SkyLeftEPGParam.RESUME_CHANNELLIST_PARAM;
import com.horion.tv.service.epg.SkyLeftEPGParam.SORT_CHANNELLIST_PARAM;
import com.horion.tv.ui.leftepg.SkyEPGData;
import com.horion.tv.ui.leftepg.SkyEPGData.SECONDTITLE_STATE;
//import com.horion.tv.utils.AsyncTask;
//import com.horion.tv.utils.SkyTVDebug;
import com.horion.tv.utils.AsyncTask;
import com.horion.tv.utils.SkyTvSingleAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
///import org.apache.http4.HttpStatus;

public class SkyLeftEPGDataGet {
    private static int[] GET_REMAINCHANNEL_MODESwitchesValues;
    private static SkyLeftEPGDataGet instance = null;
    private final int KEY_DELAY_RESPONSE;
    private boolean USE_CATCache;
    private Category catAll;
    private Category catFirst;
    private Context context;
    public List<SkyEPGData> deleteEPGDataList;
    private HashMap<String, SkyLeftEPGDataChannel> epgDataChannelMap;
    private SkyLeftEPGDataChannel epgDataChannelTemp;
    private SkyTvSingleAsyncTask getCategoryDataOnstartTask;
    private boolean isCatallDataGetted;
    private boolean isInitted;
    private volatile Boolean needMsgGetEventinfoContinue;
    public volatile boolean needStopGetEventData;
    private OnEpgDataGetListener onEpgDataGetListener;
    private SkyLeftEPGDataCategory skyLeftEPGDataCategory;
    private List<SkyEPGData> uiChannelItem;
    private onUpdateEPGFromNowListener updateListener;

    private class GetChannelDataByCategoryRunnable extends EpgRunnable {
        public GetChannelDataByCategoryRunnable(int i) {
            this.obj = Integer.valueOf(i);
        }

        public void run() {
            int i = 0;
            try {
                Category category;
                Integer num = (Integer) this.obj;
             ///   SkyTVDebug.debug("selectIndexId:" + num);
                for (SkyEPGListItemDataBase skyEPGListItemDataBase : SkyLeftEPGDataGet.this.skyLeftEPGDataCategory.getCategoryListdata().getCategoryListData()) {
             ///       SkyTVDebug.debug("item:" + skyEPGListItemDataBase.getContent());
                    if (i == num.intValue()) {
                        category = (Category) skyEPGListItemDataBase.getObject();
                ///        SkyTVDebug.debug("selectCat : " + skyEPGListItemDataBase.getObject().toString());
                        break;
                    }
                    i++;
                }
                category = null;
                //SkyTVDebug.debug("sortdata categoryList currentCat =" + category.id + "  " + category.category_enum);
                SkyLeftEPGDataGet.this.getChannelDataByCategorySync(category, num.intValue(), false, false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnEpgDataGetListener {
        void onEpgCatallDataGet(boolean z);

        void onEpgCatfirstDataGet();

        void onEpgSecondDataGetted(SkyLeftEPGDataChannel skyLeftEPGDataChannel);

        void onEpgSecondDataUpdated();
    }

    /* 频道EPG相关的类别信息，例如ALL，CCTV，HD和OTHER
     *
     */
    public class SkyLeftEPGDataCategory {
        private SkyEPGListDataBase categoryListdata = null;
        private List<SkyEPGData> uiCategoryItem = new ArrayList();

        public SkyEPGListDataBase getCategoryListdata() {
            return this.categoryListdata;
        }

        public List<SkyEPGData> getUiCategoryItem() {
            return this.uiCategoryItem;
        }

        public void setCategoryListdata(SkyEPGListDataBase skyEPGListDataBase) {
            this.categoryListdata = skyEPGListDataBase;
        }

        public void setUiCategoryItem(List<SkyEPGData> list) {
            this.uiCategoryItem = list;
        }
    }
    /* 频道相关信息
        *
        */
    public class SkyLeftEPGDataChannel {
        private int default_index = -1;
        private boolean isCurrent = true;
        private int needUseChannellistOrigin = -1;
        private List<SkyEPGData> uiChannelItemList = new ArrayList();

        public boolean getCurrent() {
            return this.isCurrent;
        }

        public int getDefault_index() {
            return this.default_index;
        }

        public int getNeedUseChannellistOrigin() {
            return this.needUseChannellistOrigin;
        }

        public List<SkyEPGData> getUiChannelItemList() {
            return this.uiChannelItemList;
        }

        public boolean isCurrent() {
            return this.isCurrent;
        }

        public void setCurrent(boolean isCurrent) {
            this.isCurrent = isCurrent;
        }

        public void setDefault_index(int default_index) {
            this.default_index = default_index;
        }

        public void setNeedUseChannellistOrigin(int needUseChannellistOrigin) {
            this.needUseChannellistOrigin = needUseChannellistOrigin;
        }

        public void setSkyLeftEPGDataChannel(List<SkyEPGData> list, int i, int i2, int default_index, boolean isCurrent, int needUseChannellistOrigin) {
            this.uiChannelItemList = list;
            this.default_index = default_index;
            this.isCurrent = isCurrent;
            this.needUseChannellistOrigin = needUseChannellistOrigin;
        }

        public void setUiChannelItemList(List<SkyEPGData> list) {
            this.uiChannelItemList = list;
        }
    }

    private class getChannelDataRemaninRunnable extends EpgRunnable {
        public getChannelDataRemaninRunnable(GET_REMAINCHANNEL_PARAM get_remainchannel_param) {
            this.obj = get_remainchannel_param;
        }

        public void run() {
            GET_REMAINCHANNEL_PARAM get_remainchannel_param = (GET_REMAINCHANNEL_PARAM) this.obj;
            if (SkyLeftEPGDataGet.this.needMsgGetEventinfoContinue.booleanValue()) {
                SkyLeftEPGDataGet.this.getRemainingChannelData(get_remainchannel_param);
                if (SkyLeftEPGDataGet.this.needMsgGetEventinfoContinue.booleanValue()) {
                    SkyLeftEPGDataGet.this.onEpgDataGetListener.onEpgSecondDataUpdated();
                }
            }
        }
    }

    public interface onUpdateEPGFromNowListener {
        void onCallBack(List<SkyEPGData> list);
    }

    private class resumeChannelListRunnable extends EpgRunnable {
        public resumeChannelListRunnable(RESUME_CHANNELLIST_PARAM resume_channellist_param) {
            this.obj = resume_channellist_param;
        }

        public void run() {
            RESUME_CHANNELLIST_PARAM resume_channellist_param = (RESUME_CHANNELLIST_PARAM) this.obj;
           /// ExternalApi.getInstance().epgApi.resumeChannelListDb(resume_channellist_param.resumeChannelList, resume_channellist_param.resumeIndexList, resume_channellist_param.sourceIn);
        }
    }

    private class sortChannelListRunnable extends EpgRunnable {
        public sortChannelListRunnable(SORT_CHANNELLIST_PARAM sort_channellist_param) {
            this.obj = sort_channellist_param;
        }

        public void run() {
            SORT_CHANNELLIST_PARAM sort_channellist_param = (SORT_CHANNELLIST_PARAM) this.obj;
           /// ExternalApi.getInstance().epgApi.sortChannelListDb(sort_channellist_param.sortChannelList, sort_channellist_param.sortIndexList, sort_channellist_param.sourceIn);
        }
    }

    private static int[] getGET_REMAINCHANNEL_MODESwitchesValues() {
        if (GET_REMAINCHANNEL_MODESwitchesValues != null) {
            return GET_REMAINCHANNEL_MODESwitchesValues;
        }
        int[] iArr = new int[GET_REMAINCHANNEL_MODE.values().length];
        try {
            iArr[GET_REMAINCHANNEL_MODE.KEYDOWN.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[GET_REMAINCHANNEL_MODE.KEYRIGHT.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[GET_REMAINCHANNEL_MODE.KEYUP.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        GET_REMAINCHANNEL_MODESwitchesValues = iArr;
        return iArr;
    }

    private SkyLeftEPGDataGet() {
        this.context = null;
        this.isCatallDataGetted = false;
        this.catAll = null;
        this.catFirst = null;
        this.needMsgGetEventinfoContinue = Boolean.valueOf(false);
        this.needStopGetEventData = false;
        this.KEY_DELAY_RESPONSE = 200;
        this.onEpgDataGetListener = null;
        this.skyLeftEPGDataCategory = new SkyLeftEPGDataCategory();
        this.epgDataChannelMap = new HashMap();
        this.isInitted = false;
        this.USE_CATCache = false;
        this.getCategoryDataOnstartTask = new SkyTvSingleAsyncTask() {
            private   int[] CATEGORY_ENUMSwitchesValues;
             ///int[] $SWITCH_TABLE$com$tianci$tv$define$object$Category$CATEGORY_ENUM;

            private   int[] getCATEGORY_ENUMSwitchesValues() {
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

            public void done(Object... objArr) throws InterruptedException {
                SkyLeftEPGDataGet.this.getCategoryDataOnstartTask.stop();
            }

            public Object[] run(Object... objArr) throws InterruptedException {
                Log.d("Maxs30","SkyTvSingleAsyncTask:run()");
                SkyEPGListDataBase skyEPGListDataBase;
                Object obj;
                Category category;
                SkyLeftEPGDataChannel skyLeftEPGDataChannel;
                SkyLeftEPGDataChannel skyLeftEPGDataChannel2;
               /// SkyTVDebug.debug("getCategoryDataAsyncTask run");
                List<SkyEPGData> uiCategoryItem = SkyLeftEPGDataGet.this.skyLeftEPGDataCategory.getUiCategoryItem();
                CATTYPE cattype = (CATTYPE) objArr[0];
                boolean booleanValue = ((Boolean) objArr[1]).booleanValue();
                Channel channel = (Channel) objArr[2];
                if (!SkyLeftEPGDataGet.this.USE_CATCache) {
                    SkyLeftEPGDataGet.this.skyLeftEPGDataCategoryClear();
                    skyEPGListDataBase = null;
                    obj = null;
                } else if (uiCategoryItem == null || uiCategoryItem.size() <= 0) {
                    skyEPGListDataBase = null;
                    obj = null;
                } else {
                    skyEPGListDataBase = SkyLeftEPGDataGet.this.skyLeftEPGDataCategory.getCategoryListdata();
                    obj = 1;
                }
                Log.d("Maxs24","SkyTvSingleAsyncTask:run():obj == null = " + (obj == null));
                if (obj == null) {
                    try {
                        SkyEPGListDataBase categoryDataList = null;
                        ///SkyTVDebug.debug("getCategoryDataAsyncTask catType:" + cattype);
                        Log.d("Maxs28","---->getCategoryDataAsyncTask catType:" + cattype);
                        try {
                            Log.d("Maxs24","SkyLeftEPGDataGet:context == null = " + (context == null));

                            /* 获取频道类别列表有哪几种:比如All,CCTV,HD,OTHERS
                             *
                             */
                            categoryDataList = SkyEPGApi.getInstance().getCategoryDataList(SkyLeftEPGDataGet.this.context);
                            Log.d("Maxs24","SkyLeftEPGDataGet:categoryDataList == null = " + (categoryDataList == null));
                        } catch (Exception e) {

                        }
                        if (categoryDataList == null) {
                              ///  SkyTVDebug.debug("sortdata categoryListdata null");
                            return SkyTvSingleAsyncTask.objects(Boolean.FALSE);
                        }
                       /// SkyTVDebug.debug("sortdata categoryList size =" + categoryDataList.totalCount);
                        Log.d("Maxs24","SkyLeftEPGDataGet:categoryDataList.totalCount = " + categoryDataList.totalCount);
                        Log.d("Maxs24","SkyLeftEPGDataGet:(categoryDataList.getCategoryListData() == null) = " + (categoryDataList.getCategoryListData() == null));
                        Log.d("Maxs24","SkyLeftEPGDataGet:categoryDataList.getCategoryListData().size() = " + categoryDataList.getCategoryListData().size());
                        int i = 0;
                        for (SkyEPGListItemDataBase skyEPGListItemDataBase : categoryDataList.getCategoryListData()) {
                            SkyEPGData skyEPGData = new SkyEPGData();
                            category = (Category) skyEPGListItemDataBase.getObject();
                          //  SkyTVDebug.debug(">>>>>>>>>>category: " + skyEPGListItemDataBase.getObject().toString() + " || " + category.id);
                            Log.d("Maxs24",">>>>>>>>>>category: " + skyEPGListItemDataBase.getObject().toString() + " || " + category.id);
                            if (category.category_enum != null) {
                                switch (getCATEGORY_ENUMSwitchesValues()[category.category_enum.ordinal()]) {
                                    case 1:
                                        skyEPGData.setItemTitle(SkyLeftEPGDataGet.this.context.getResources().getString(R.string.category_all));
                                        SkyLeftEPGDataGet.this.catAll = category;
                                        break;
                                    case 2:
                                        skyEPGData.setItemTitle(SkyLeftEPGDataGet.this.context.getResources().getString(R.string.category_cctv));
                                        break;
                                    case 3:
                                        skyEPGData.setItemTitle(SkyLeftEPGDataGet.this.context.getResources().getString(R.string.category_hd));
                                        break;
                                    case 4:
                                        skyEPGData.setItemTitle(SkyLeftEPGDataGet.this.context.getResources().getString(R.string.category_other));
                                        break;
                                    case 5:
                                        skyEPGData.setItemTitle(SkyLeftEPGDataGet.this.context.getResources().getString(R.string.category_startv));
                                        break;
                                    default:
                                        skyEPGData.setItemTitle(SkyLeftEPGDataGet.this.context.getResources().getString(R.string.category_all));
                                        break;
                                }
                            }
                            skyEPGData.setItemTitle(category.name);
                            Log.d("Maxs25","setItemIndex11ID:222");
                            skyEPGData.setItemIndexID(i);
                            uiCategoryItem.add(skyEPGData);
                            i++;
                        }
                        ///SkyTVDebug.debug("getCategoryDataAsyncTask uiCategoryItem size:" + uiCategoryItem.size());
                        Log.d("Maxs24","getCategoryDataAsyncTask uiCategoryItem size:" + uiCategoryItem.size());
                        skyEPGListDataBase = categoryDataList;
                    } catch (Exception e5) {

                    }
                }
                category = null;
                if (uiCategoryItem.size() > 0) {
                    SkyLeftEPGDataGet.this.skyLeftEPGDataCategory.setCategoryListdata(skyEPGListDataBase);
                    SkyLeftEPGDataGet.this.catFirst = (Category) ((SkyEPGListItemDataBase) skyEPGListDataBase.getCategoryListData().get(0)).getObject();
                    Log.d("Maxs24","**********catFirst.catFirst.id = " + catFirst.id);
                    category = SkyLeftEPGDataGet.this.catFirst;
                    if (cattype.equals(CATTYPE.CAT_ALL)) {
                        category = SkyLeftEPGDataGet.this.catAll;
                    }
                    skyLeftEPGDataChannel = new SkyLeftEPGDataChannel();
                    skyLeftEPGDataChannel.setNeedUseChannellistOrigin(0);
                    List<SkyEPGData> uiChannelItemListTemp = SkyEPGApi.getInstance().getEpgDataList();
                    skyLeftEPGDataChannel.setUiChannelItemList(uiChannelItemListTemp);
                    skyLeftEPGDataChannel.setDefault_index(SkyEPGApi.getInstance().getCurrentChannelIndex());
                    skyLeftEPGDataChannel2 = (SkyLeftEPGDataChannel) SkyLeftEPGDataGet.this.epgDataChannelMap.get(category.id);
                   /// if (skyLeftEPGDataChannel2 != null || skyLeftEPGDataChannel2.getUiChannelItemList() == null || skyLeftEPGDataChannel2.getUiChannelItemList().size() <= 0) {
                        SkyLeftEPGDataGet.this.epgDataChannelMap.put(category.id, skyLeftEPGDataChannel);
                        SkyLeftEPGDataGet.this.getChannelDataByCategorySync(category, -1, true, booleanValue, channel);
                    ///} else {
                        //SkyTVDebug.debug("getCategoryDataOnstartTask channelCat.getUiChannelItemList().size():" + skyLeftEPGDataChannel2.getUiChannelItemList().size());
                   /// }
                }
                if (category != null && category.category_enum == CATEGORY_ENUM.ALL) {
                    SkyLeftEPGDataGet.this.setCatallDataGetted(true);
                }
                Log.d("Maxs24","skyLeftEPGDataCategory.getUiCategoryItem().size() =" + skyLeftEPGDataCategory.getUiCategoryItem().size());
                Log.d("Maxs24","onEpgDataGetListener == null =" + (onEpgDataGetListener == null));
                Log.d("Maxs24","cattype.equals(CATTYPE.CAT_ALL) =" + cattype.equals(CATTYPE.CAT_ALL));
                ////if (SkyLeftEPGDataGet.this.skyLeftEPGDataCategory.getUiCategoryItem().size() <= 0) {
                if (SkyLeftEPGDataGet.this.skyLeftEPGDataCategory.getUiCategoryItem().size() >= 0) {
                    //SkyTVDebug.debug(">>>>>>>>>>>>showFistEPG<<<<<<<<<<<<");
                    if (SkyLeftEPGDataGet.this.onEpgDataGetListener != null) {
                        if (cattype.equals(CATTYPE.CAT_ALL)) {
                            if (booleanValue) {
                            }
                            SkyLeftEPGDataGet.this.onEpgDataGetListener.onEpgCatallDataGet(booleanValue);
                        } else {
                            SkyLeftEPGDataGet.this.onEpgDataGetListener.onEpgCatfirstDataGet();
                        }
                    }
                } else if (SkyLeftEPGDataGet.this.onEpgDataGetListener != null && cattype.equals(CATTYPE.CAT_ALL)) {
                    if (booleanValue) {
                    }
                    SkyLeftEPGDataGet.this.onEpgDataGetListener.onEpgCatallDataGet(booleanValue);
                }
                return SkyTvSingleAsyncTask.objects(Boolean.TRUE);
            }
        };
        this.uiChannelItem = null;
        this.epgDataChannelTemp = null;
        this.updateListener = null;
        this.isInitted = false;
    }

    private void asyncTaskUpdateEPGFromNowData(final List<Channel> list, final int i) {
        AsyncTask.runOnThread(new Runnable() {
            public void run() {
                SkyLeftEPGDataGet.this.getEpgFromNowDataSync(list, i);
               /// SkyTVDebug.debug("<<<< AsysnTask >>>> : " + SkyLeftEPGDataGet.this.uiChannelItem.size());
                SkyLeftEPGDataGet.this.updateListener.onCallBack(SkyLeftEPGDataGet.this.uiChannelItem);
            }
        });
    }

    private void epgDataChannelMapClear() {
        for (Entry entry : this.epgDataChannelMap.entrySet()) {
            entry.getKey();
            SkyLeftEPGDataChannel skyLeftEPGDataChannel = (SkyLeftEPGDataChannel) entry.getValue();
            if (skyLeftEPGDataChannel.getUiChannelItemList() != null) {
                skyLeftEPGDataChannel.getUiChannelItemList().clear();
            }
            skyLeftEPGDataChannel.setDefault_index(-1);
        }
        this.epgDataChannelMap.clear();
    }

    private SkyLeftEPGDataChannel getChannelDataByCategorySync(Category category, int needUseChannellistOrigin, boolean isOnStart, boolean useSorChannel, Channel channel) {
        Channel currentChannel = SkyEPGApi.getInstance().getCurrentChannel();
        ///SkyTVDebug.debug("getChannelDataByCategorySync ,isOnStart:" + isOnStart + "  useSorChannel:" + z2);
        if (useSorChannel && channel != null) {
           /// SkyTVDebug.debug("getChannelDataByCategorySync useSorChannel:" + z2 + "  sortChannel.id:" + channel.id + channel.displayName);
        }
        if (isOnStart) {
            this.epgDataChannelTemp = (SkyLeftEPGDataChannel) this.epgDataChannelMap.get(category.id);
        } else if (!this.USE_CATCache) {
            this.epgDataChannelTemp = new SkyLeftEPGDataChannel();
            this.epgDataChannelMap.put(category.id, this.epgDataChannelTemp);
        } else if (this.epgDataChannelMap.containsKey(category.id)) {
           /// SkyTVDebug.debug("getChannelDataByCategorySync ,found selectCat:" + category.category_enum);
            ((SkyLeftEPGDataChannel) this.epgDataChannelMap.get(category.id)).setNeedUseChannellistOrigin(needUseChannellistOrigin);
            this.onEpgDataGetListener.onEpgSecondDataGetted((SkyLeftEPGDataChannel) this.epgDataChannelMap.get(category.id));
            return (SkyLeftEPGDataChannel) this.epgDataChannelMap.get(category.id);
        }
        this.uiChannelItem = ((SkyLeftEPGDataChannel) this.epgDataChannelMap.get(category.id)).getUiChannelItemList();
        List<Channel> channelListByCategory = SkyEPGApi.getInstance().getChannelListByCategory(Boolean.valueOf(true), category);
        if (channelListByCategory == null || channelListByCategory.size() == 0) {
            ///SkyTVDebug.debug("getChannelDataByCategorySync selectCat:" + category.category_enum + "  return channelList size 0");
            return (SkyLeftEPGDataChannel) this.epgDataChannelMap.get(category.id);
        }
        boolean z3;
       /// SkyTVDebug.debug("getChannelDataByCategorySync selectCat:" + category.category_enum + "  return channelList size:" + channelListByCategory.size());
        int i2 = 0;
        int i3 = -1;
        int i4 = 0;
        for (Channel channel2 : channelListByCategory) {
            if (channel2 != null) {
                if (useSorChannel) {
                   /// SkyTVDebug.debug("getChannelDataByCategorySync c.id:" + channel2.id + "  name:" + channel2.displayName);
                    if (channel2.equals(channel)) {
                       /// SkyTVDebug.debug("getChannelDataByCategorySync useSorChannel,default_index:" + i2);
                        i3 = i2;
                    }
                } else if (channel2.equals(currentChannel)) {
                    i3 = i2;
                }
                SkyEPGData skyEPGData = new SkyEPGData();
                ///SkyTVDebug.debug("displayName:" + channel2.displayName + " name:" + channel2.name);
                if (channel2.source.equals(Source.ATV())) {
                    skyEPGData.setItemTitle("模拟电视" + channel2.name);
                } else {
                    skyEPGData.setItemTitle(channel2.name);
                }
                skyEPGData.setData(channel2);
                skyEPGData.setChannelNumber(String.valueOf(channel2.mapindex));
                skyEPGData.setChannelType(channel2.type);
                Log.d("Maxs25","setItemIndex11ID:333");
                skyEPGData.setItemIndexID(i4);
                this.uiChannelItem.add(skyEPGData);
                i2++;
                i4++;
            }
        }
        if (i3 == -1) {
            i3 = 0;
            z3 = false;
        } else {
            z3 = true;
        }
       /// SkyTVDebug.debug(">>>>>>uiChannelItem:" + this.uiChannelItem.size() + " isOnStart: " + z);
        if (isOnStart) {
            getEpgFromNowDataSync(channelListByCategory, i3);
            this.epgDataChannelTemp.setDefault_index(i3);
            this.epgDataChannelTemp.setCurrent(z3);
        } else {
           /// SkyTVDebug.debug(">>>>>>selectIndexId:" + needUseChannellistOrigin + " isOnStart: " + z);
            this.epgDataChannelTemp.setDefault_index(i3);
            this.epgDataChannelTemp.setCurrent(true);
            ((SkyLeftEPGDataChannel) this.epgDataChannelMap.get(category.id)).setNeedUseChannellistOrigin(needUseChannellistOrigin);
            this.onEpgDataGetListener.onEpgSecondDataGetted((SkyLeftEPGDataChannel) this.epgDataChannelMap.get(category.id));
            asyncTaskUpdateEPGFromNowData(channelListByCategory, i3);
        }
        return this.epgDataChannelTemp;
    }

    private void getEpgFromNowDataSync(List<Channel> list, int i) {
        int i2 = 0;
        int i3 = -1;
        Channel channel;
        if (list.size() <= 6) {
            for (Channel channel2 : list) {
                if (channel2 != null) {
                    if (!this.needStopGetEventData) {
                        try {
                            SkyEPGTVBriefData ePGEventFromNow = SkyEPGApi.getInstance().getEPGEventFromNow(channel2);
                            if (ePGEventFromNow == null) {
                              ///  SkyTVDebug.debug("EventInfo is null");
                                ((SkyEPGData) this.uiChannelItem.get(i2)).setItemSecondTitleState(SECONDTITLE_STATE.TRYERROR);
                            } else {
                                ((SkyEPGData) this.uiChannelItem.get(i2)).setItemSecondTitleState(SECONDTITLE_STATE.TRYSUCCESS);
                                ((SkyEPGData) this.uiChannelItem.get(i2)).setItemSecondTitleTitle(ePGEventFromNow.current_time + " " + ePGEventFromNow.current_info);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        i2++;
                    } else {
                        return;
                    }
                }
            }
            return;
        }
        int i4 = 1;
        int i5 = -2;
        int i6 = -1;
        while (i5 <= 3) {
            ///SkyTVDebug.debug("index:" + i5);
            if (!this.needStopGetEventData) {
                int i7 = (i + 0) + i5;
                int size = i7 < 0 ? i7 + list.size() : i7 % list.size();
                if (size < list.size()) {
                    if (size < 0) {
                        i7 = i4;
                        size = i6;
                        i4 = i3;
                    } else {
                       /// SkyTVDebug.debug("index:" + i5 + " channelListIndex:" + size);
                        if (i4 != 0) {
                            i6 = size;
                            i4 = 0;
                            i3 = size;
                        } else {
                            if (i6 > size) {
                                i6 = size;
                            }
                            if (i3 < size) {
                                i3 = size;
                            }
                        }
                        Channel channel2 = (Channel) list.get(size);
                        if (channel2 != null) {
                            try {
                                SkyEPGTVBriefData ePGEventFromNow2 = SkyEPGApi.getInstance().getEPGEventFromNow(channel2);
                                if (ePGEventFromNow2 == null) {
                                    ///SkyTVDebug.debug("EventInfo is null");
                                    ((SkyEPGData) this.uiChannelItem.get(size)).setItemSecondTitleState(SECONDTITLE_STATE.TRYERROR);
                                    i7 = i4;
                                    size = i6;
                                    i4 = i3;
                                } else {
                                    ///SkyTVDebug.debug("cuttent:" + ePGEventFromNow2.current_time + " " + ePGEventFromNow2.current_info);
                                    ((SkyEPGData) this.uiChannelItem.get(size)).setItemSecondTitleState(SECONDTITLE_STATE.TRYSUCCESS);
                                    ((SkyEPGData) this.uiChannelItem.get(size)).setItemSecondTitleTitle(ePGEventFromNow2.current_time + " " + ePGEventFromNow2.current_info);
                                    i7 = i4;
                                    size = i6;
                                    i4 = i3;
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                                i7 = i4;
                                size = i6;
                                i4 = i3;
                            }
                        }
                    }
                    i5++;
                    i3 = i4;
                    i4 = i7;
                    i6 = size;
                }
                i7 = i4;
                size = i6;
                i4 = i3;
                i5++;
                i3 = i4;
                i4 = i7;
                i6 = size;
            } else {
                return;
            }
        }
    }

    public static SkyLeftEPGDataGet getInstance() {
        synchronized (SkyLeftEPGDataGet.class) {
            try {
                if (instance == null) {
                    instance = new SkyLeftEPGDataGet();
                }
                SkyLeftEPGDataGet skyLeftEPGDataGet = instance;
                return skyLeftEPGDataGet;
            } finally {
                Object obj = SkyLeftEPGDataGet.class;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean getRemainingChannelData(SkyLeftEPGParam.GET_REMAINCHANNEL_PARAM r10) {
        /*
        r9 = this;
        r1 = 2;
        r3 = 1;
        r2 = 0;
        r0 = 8;
        r4 = -getGET_REMAINCHANNEL_MODESwitchesValues();
        r5 = r10.mode;
        r5 = r5.ordinal();
        r4 = r4[r5];
        switch(r4) {
            case 1: goto L_0x004d;
            case 2: goto L_0x004b;
            case 3: goto L_0x0016;
            default: goto L_0x0014;
        };
    L_0x0014:
        r0 = r2;
        r1 = r2;
    L_0x0016:
        r0 = 0 - r0;
        r5 = r0;
    L_0x0019:
        if (r5 > r1) goto L_0x0108;
    L_0x001b:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r4 = "index:";
        r0 = r0.append(r4);
        r0 = r0.append(r5);
        r4 = "  needMsgGetEventinfoContinue:";
        r0 = r0.append(r4);
        r4 = r9.needMsgGetEventinfoContinue;
        r0 = r0.append(r4);
        r0 = r0.toString();
        com.horion.tv.utils.SkyTVDebug.debug(r0);
        r6 = r9.needMsgGetEventinfoContinue;
        monitor-enter(r6);
        r0 = r9.needMsgGetEventinfoContinue;	 Catch:{ all -> 0x00be }
        r0 = r0.booleanValue();	 Catch:{ all -> 0x00be }
        if (r0 != 0) goto L_0x0051;
    L_0x0048:
        r0 = r2;
    L_0x0049:
        monitor-exit(r6);
    L_0x004a:
        return r0;
    L_0x004b:
        r1 = r0;
        goto L_0x0016;
    L_0x004d:
        r8 = r1;
        r1 = r0;
        r0 = r8;
        goto L_0x0016;
    L_0x0051:
        r0 = r10.currentIndex;	 Catch:{ Exception -> 0x00b9 }
        r0 = r0 + r5;
        if (r0 >= 0) goto L_0x006d;
    L_0x0056:
        r4 = r10.uiChannelItem;	 Catch:{ Exception -> 0x00b9 }
        r4 = r4.size();	 Catch:{ Exception -> 0x00b9 }
        r0 = r0 + r4;
        r4 = r0;
    L_0x005e:
        r0 = r10.uiChannelItem;	 Catch:{ Exception -> 0x00b9 }
        r0 = r0.size();	 Catch:{ Exception -> 0x00b9 }
        if (r4 >= r0) goto L_0x0068;
    L_0x0066:
        if (r4 >= 0) goto L_0x0076;
    L_0x0068:
        monitor-exit(r6);
        r0 = r5 + 1;
        r5 = r0;
        goto L_0x0019;
    L_0x006d:
        r4 = r10.uiChannelItem;	 Catch:{ Exception -> 0x00b9 }
        r4 = r4.size();	 Catch:{ Exception -> 0x00b9 }
        r0 = r0 % r4;
        r4 = r0;
        goto L_0x005e;
    L_0x0076:
        r0 = r10.uiChannelItem;	 Catch:{ Exception -> 0x00b9 }
        r0 = r0.get(r4);	 Catch:{ Exception -> 0x00b9 }
        r0 = (com.horion.tv.ui.leftepg.SkyEPGData) r0;	 Catch:{ Exception -> 0x00b9 }
        r0 = r0.getItemSecondTitleState();	 Catch:{ Exception -> 0x00b9 }
        r7 = com.horion.tv.ui.leftepg.SkyEPGData.SECONDTITLE_STATE.NOTTRY;	 Catch:{ Exception -> 0x00b9 }
        if (r0 != r7) goto L_0x0068;
    L_0x0086:
        r7 = com.horion.tv.service.epg.SkyEPGApi.getInstance();	 Catch:{ Exception -> 0x00b9 }
        r0 = r10.uiChannelItem;	 Catch:{ Exception -> 0x00b9 }
        r0 = r0.get(r4);	 Catch:{ Exception -> 0x00b9 }
        r0 = (com.horion.tv.ui.leftepg.SkyEPGData) r0;	 Catch:{ Exception -> 0x00b9 }
        r0 = r0.getData();	 Catch:{ Exception -> 0x00b9 }
        r0 = (com.horion.tv.define.object.Channel) r0;	 Catch:{ Exception -> 0x00b9 }
        r7 = r7.getEPGEventFromNow(r0);	 Catch:{ Exception -> 0x00b9 }
        if (r7 != 0) goto L_0x00c1;
    L_0x009e:
        r0 = "EventInfo is null";
        com.horion.tv.utils.SkyTVDebug.debug(r0);	 Catch:{ Exception -> 0x00b9 }
        r0 = r9.needMsgGetEventinfoContinue;	 Catch:{ Exception -> 0x00b9 }
        r0 = r0.booleanValue();	 Catch:{ Exception -> 0x00b9 }
        if (r0 == 0) goto L_0x0105;
    L_0x00ab:
        r0 = r10.uiChannelItem;	 Catch:{ Exception -> 0x00b9 }
        r0 = r0.get(r4);	 Catch:{ Exception -> 0x00b9 }
        r0 = (com.horion.tv.ui.leftepg.SkyEPGData) r0;	 Catch:{ Exception -> 0x00b9 }
        r4 = com.horion.tv.ui.leftepg.SkyEPGData.SECONDTITLE_STATE.TRYERROR;	 Catch:{ Exception -> 0x00b9 }
        r0.setItemSecondTitleState(r4);	 Catch:{ Exception -> 0x00b9 }
        goto L_0x0068;
    L_0x00b9:
        r0 = move-exception;
    L_0x00ba:
        r0.printStackTrace();	 Catch:{ all -> 0x00be }
        goto L_0x0068;
    L_0x00be:
        r0 = move-exception;
        monitor-exit(r6);
        throw r0;
    L_0x00c1:
        r0 = r9.needMsgGetEventinfoContinue;	 Catch:{ Exception -> 0x00ff }
        r0 = r0.booleanValue();	 Catch:{ Exception -> 0x00ff }
        if (r0 == 0) goto L_0x0102;
    L_0x00c9:
        r0 = r10.uiChannelItem;	 Catch:{ Exception -> 0x00ff }
        r0 = r0.get(r4);	 Catch:{ Exception -> 0x00ff }
        r0 = (com.horion.tv.ui.leftepg.SkyEPGData) r0;	 Catch:{ Exception -> 0x00ff }
        r2 = com.horion.tv.ui.leftepg.SkyEPGData.SECONDTITLE_STATE.TRYSUCCESS;	 Catch:{ Exception -> 0x00ff }
        r0.setItemSecondTitleState(r2);	 Catch:{ Exception -> 0x00ff }
        r0 = r10.uiChannelItem;	 Catch:{ Exception -> 0x00ff }
        r0 = r0.get(r4);	 Catch:{ Exception -> 0x00ff }
        r0 = (com.horion.tv.ui.leftepg.SkyEPGData) r0;	 Catch:{ Exception -> 0x00ff }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ff }
        r2.<init>();	 Catch:{ Exception -> 0x00ff }
        r4 = r7.current_time;	 Catch:{ Exception -> 0x00ff }
        r2 = r2.append(r4);	 Catch:{ Exception -> 0x00ff }
        r4 = " ";
        r2 = r2.append(r4);	 Catch:{ Exception -> 0x00ff }
        r4 = r7.current_info;	 Catch:{ Exception -> 0x00ff }
        r2 = r2.append(r4);	 Catch:{ Exception -> 0x00ff }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00ff }
        r0.setItemSecondTitleTitle(r2);	 Catch:{ Exception -> 0x00ff }
        r2 = r3;
        goto L_0x0068;
    L_0x00ff:
        r0 = move-exception;
        r2 = r3;
        goto L_0x00ba;
    L_0x0102:
        r0 = r3;
        goto L_0x0049;
    L_0x0105:
        r0 = r2;
        goto L_0x0049;
    L_0x0108:
        r0 = r2;
        goto L_0x004a;
        */
       /// throw new UnsupportedOperationException("Method not decompiled: com.horion.tv.service.epg.SkyLeftEPGDataGet.getRemainingChannelData(com.horion.tv.service.epg.SkyLeftEPGParam$GET_REMAINCHANNEL_PARAM):boolean");
        return false;
    }

    private void skyLeftEPGDataCategoryClear() {
        List<SkyEPGData> uiCategoryItem = this.skyLeftEPGDataCategory.getUiCategoryItem();
        this.skyLeftEPGDataCategory.getCategoryListdata();
        if (uiCategoryItem != null) {
            uiCategoryItem.clear();
        }
        this.skyLeftEPGDataCategory.setCategoryListdata(null);
    }

    public void clear() {
        clearMsgGetEventinfoContinue();
        setCatallDataGetted(false);
        skyLeftEPGDataCategoryClear();
        epgDataChannelMapClear();
    }

    public void clearEventType() {
        for (Entry value : this.epgDataChannelMap.entrySet()) {
            Iterable<SkyEPGData> uiChannelItemList = ((SkyLeftEPGDataChannel) value.getValue()).getUiChannelItemList();
            if (uiChannelItemList != null) {
                for (SkyEPGData skyEPGData : uiChannelItemList) {
                    if (skyEPGData != null) {
                        if (skyEPGData.getItemSecondTitleState() == SECONDTITLE_STATE.TRYERROR) {
                            skyEPGData.setItemSecondTitleState(SECONDTITLE_STATE.NOTTRY);
                        }
                        if (skyEPGData.getItemSecondTitleState() == SECONDTITLE_STATE.TRYSUCCESS) {
                            skyEPGData.setItemSecondTitleState(SECONDTITLE_STATE.NOTTRY);
                        }
                    }
                }
            }
        }
    }

    public void clearMsgGetEventinfoContinue() {
       /* if (EpgThread.getInstance() != null && EpgThread.getInstance().tHandler != null && EpgThread.getInstance().isEpgThreadIng) {
            if (EpgThread.getInstance().tHandler.hasMessages(2)) {
                EpgThread.getInstance().tHandler.removeMessages(2);
            }
            synchronized (this.needMsgGetEventinfoContinue) {
                this.needMsgGetEventinfoContinue = Boolean.valueOf(false);
            }
        }*/
    }

    public void getCatFirstEpgDataAsync() {
        this.getCategoryDataOnstartTask.start(CATTYPE.CAT_FIRST, Boolean.valueOf(false), null);
    }

    public void getChannelDataByCategory(int i) {
        if (EpgThread.getInstance().tHandler.hasMessages(1)) {
            EpgThread.getInstance().tHandler.removeMessages(1);
        }
        EpgThread.getInstance().runOnThreadWithDelay(new GetChannelDataByCategoryRunnable(i), 1, 200);
    }

    public void getChannelDataRemanin(GET_REMAINCHANNEL_PARAM get_remainchannel_param) {
        Log.d("Maxs24","------->getChannelDataRemanin:tHandler == null = " + (EpgThread.getInstance().tHandler == null));
        if (EpgThread.getInstance().tHandler.hasMessages(2)) {
            EpgThread.getInstance().tHandler.removeMessages(2);
        }
        this.needMsgGetEventinfoContinue = Boolean.valueOf(true);
        EpgThread.getInstance().runOnThread(new getChannelDataRemaninRunnable(get_remainchannel_param), 2);
    }

    public List<SkyEPGData> getDeleteChannelBySQ() {
        /*Source currentSource = SkyEPGApi.getInstance().getCurrentSource();
        ////Iterable<Channel> deletedChannelList = ExternalApi.getInstance().epgApi.getDeletedChannelList(currentSource);
        Iterable<Channel> deletedChannelList = new ArrayList<Channel>();
        List<SkyEPGData> arrayList = new ArrayList();
        int i = 0;
        for (Channel channel : deletedChannelList) {
            SkyEPGData skyEPGData = new SkyEPGData();
            if (Source.ATV().equals(currentSource)) {
                skyEPGData.setItemTitle("模拟电视" + channel.displayName);
            } else {
                skyEPGData.setItemTitle(channel.displayName);
            }
            skyEPGData.setData(channel);
            skyEPGData.setChannelNumber(String.valueOf(channel.mapindex));
            skyEPGData.setChannelType(channel.type);
            Log.d("Maxs25","setItemIndex11ID:444");
            skyEPGData.setItemIndexID(i);
            arrayList.add(skyEPGData);
            i++;
        }
        this.deleteEPGDataList = arrayList;
        return arrayList;*/
        deleteEPGDataList = SkyEPGApi.getInstance().getDeletedChannelList();
        return deleteEPGDataList;
    }

    public SkyLeftEPGDataCategory getLeftEPGDataCat0yFirstSync() {
        return this.skyLeftEPGDataCategory;
    }

    public SkyLeftEPGDataChannel getLeftEPGDataCatallChannelSync() {
        Log.d("Maxs28","catAll.id = " + catAll.id);
        return this.catAll == null ? null : (SkyLeftEPGDataChannel) this.epgDataChannelMap.get(this.catAll.id);
    }

    public SkyLeftEPGDataChannel getLeftEPGDataChannelOfCatfirstSync() {
        Log.d("Maxs24","catFirst.id = " + catFirst.id);
        return this.catFirst == null ? null : (SkyLeftEPGDataChannel) this.epgDataChannelMap.get(this.catFirst.id);
    }

    public void getLeftEpgCatallAsync(boolean z, Channel channel) {
        Log.d("Maxs30","------------->getLeftEpgCatallAsync:isCatallDataGetted() = " + isCatallDataGetted());
       /// if (!isCatallDataGetted()) {
            this.getCategoryDataOnstartTask.start(CATTYPE.CAT_ALL, Boolean.valueOf(z), channel);
        ///}
    }

    public void init(Context context) {
        if (!this.isInitted) {
            this.isInitted = true;
            this.context = context;
        }
    }

    public boolean isCatallDataGetted() {
        return this.isCatallDataGetted;
    }

    public boolean isEpgSource(Source source) {
        return source == null ? false : (source.equals(Source.DVBC()) || source.equals(Source.DTMB()) || source.equals(Source.IPLive()) || source.getSourceNameEnum() == SOURCE_NAME_ENUM.EXTERNAL) ? true : source.equals(Source.ATV()) ? false : false;
    }

    public void resumeChannelList(RESUME_CHANNELLIST_PARAM resume_channellist_param) {
        if (EpgThread.getInstance().tHandler.hasMessages(4)) {
            EpgThread.getInstance().tHandler.removeMessages(4);
        }
        EpgThread.getInstance().runOnThread(new resumeChannelListRunnable(resume_channellist_param), 4);

    }

    public void setCatallDataGetted(boolean z) {
        this.isCatallDataGetted = z;
    }

    public void setOnEpgDataGetListener(OnEpgDataGetListener onEpgDataGetListener) {
        this.onEpgDataGetListener = onEpgDataGetListener;
    }

    public void setOnUpdateEPGFromNowListener(onUpdateEPGFromNowListener com_tianci_tv_service_epg_SkyLeftEPGDataGet_onUpdateEPGFromNowListener) {
        this.updateListener = com_tianci_tv_service_epg_SkyLeftEPGDataGet_onUpdateEPGFromNowListener;
    }

    public void sortChannelList(SORT_CHANNELLIST_PARAM sort_channellist_param) {
       /* if (EpgThread.getInstance().tHandler.hasMessages(3)) {
            EpgThread.getInstance().tHandler.removeMessages(3);
        }
        EpgThread.getInstance().runOnThread(new sortChannelListRunnable(sort_channellist_param), 3);*/
    }

    public void updateDefaultIndexAndIsCurrent(Channel channel) {
        for (Entry entry : this.epgDataChannelMap.entrySet()) {
            entry.getKey();
            SkyLeftEPGDataChannel skyLeftEPGDataChannel = (SkyLeftEPGDataChannel) entry.getValue();
            if (!(skyLeftEPGDataChannel == null || skyLeftEPGDataChannel.getUiChannelItemList() == null)) {
                if (channel == null) {
                    skyLeftEPGDataChannel.setDefault_index(0);
                    skyLeftEPGDataChannel.setCurrent(false);
                } else {
                    boolean z;
                    int i = 0;
                    for (SkyEPGData skyEPGData : skyLeftEPGDataChannel.getUiChannelItemList()) {
                        if (!(skyEPGData == null || skyEPGData.getData() == null)) {
                            Channel channel2 = (Channel) skyEPGData.getData();
                            if (channel2 == null) {
                                continue;
                            } else if (channel2.equals(channel)) {
                                break;
                            } else {
                                i++;
                            }
                        }
                    }
                    i = -1;
                    if (i == -1) {
                        z = false;
                        i = 0;
                    } else {
                        z = true;
                    }
                    skyLeftEPGDataChannel.setDefault_index(i);
                    skyLeftEPGDataChannel.setCurrent(z);
                }
            }
        }
    }
}
