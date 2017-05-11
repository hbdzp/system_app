package com.ktc.panasonichome.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ktc.panasonichome.LauncherActivity;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ShareUtils;
import com.ktc.panasonichome.utils.UIUtils;
import com.ktc.panasonichome.utils.VolleySingleton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HomePageDataCategory {
    private WeakReference<LauncherActivity> wfActivity;
    HomePageData homePageData = null;

    public HomePageDataCategory(LauncherActivity context) {
        this.wfActivity = new WeakReference(context);
        loadHomePageDataDefault();
    }

    private void loadHomePageDataDefault() {
        loadImageUrls();
        homePageData=new HomePageData();
        homePageData.category=null;
        homePageData.language=new Language();
        homePageData.language.defaultLan="cn";
        homePageData.language.languagemap=new HashMap<String, HashMap<String, String>>();
        //language  inner hashmap start
        //for cn
        HashMap<String, String> languageInnerHashMapForCN=new HashMap<String, String>();
        languageInnerHashMapForCN.put("ktc_000","影视中心");
        languageInnerHashMapForCN.put("ktc_001","游戏中心");
        languageInnerHashMapForCN.put("ktc_002","教育中心");
        languageInnerHashMapForCN.put("ktc_003","天籁K歌");
        languageInnerHashMapForCN.put("ktc_004","应用中心");
        languageInnerHashMapForCN.put("ktc_005","资讯中心");
        //For en
        HashMap<String, String> languageInnerHashMapForEN=new HashMap<String, String>();
        languageInnerHashMapForEN.put("ktc_000","Movie Center");
        languageInnerHashMapForEN.put("ktc_001","Game Center");
        languageInnerHashMapForEN.put("ktc_002","Education Center");
        languageInnerHashMapForEN.put("ktc_003","Music Center");
        languageInnerHashMapForEN.put("ktc_004","App Center");
        languageInnerHashMapForEN.put("ktc_005","News Center");
        //language  inner hashmap end
        //put into hashmap
        homePageData.language.languagemap.put("cn",languageInnerHashMapForCN);
        homePageData.language.languagemap.put("en",languageInnerHashMapForEN);
        homePageData.pockets=new ArrayList<Pockets>();
        for (int i = 0; i < languageInnerHashMapForCN.size(); i++) {
            if (i == 0) {
                Pockets pockets = new Pockets();
                pockets.animationtype = 0;
                pockets.expand = "";
                pockets.pocketitems = new ArrayList<PocketItem>();
                for (int j = 0; j < 1; j++) {
                    PocketItem pocketItem = new PocketItem();
                    pocketItem.action = new ClickCmdAction();
                    //value指定网络加载路径，没有则为空
                    pocketItem.action.value = "";
                    pocketItem.detail = new PocketItemDetail();
                    pocketItem.detail.background = "";
                    pocketItem.detail.floaticon = "";
                    pocketItem.detail.foreground = "movie";
                    pocketItem.detail.icon = "";
                    int    picNo     = new Random().nextInt(3)+1;
                    pocketItem.detail.mixicon = "R.drawable.mixicon_movie_"+picNo;
                    pockets.pocketitems.add(pocketItem);
                }
                pockets.pocketitemtexts = new ArrayList<PocketItemText>();

                pockets.textanimationtype = 0;
                pockets.titleid = "ktc_00" + i;
                homePageData.pockets.add(pockets);
            }else if(i == 1){
                Pockets pockets = new Pockets();
                pockets.animationtype = 0;
                pockets.expand = "";
                pockets.pocketitems = new ArrayList<PocketItem>();
                for (int j = 0; j < 1; j++) {
                    PocketItem pocketItem = new PocketItem();
                    pocketItem.action = new ClickCmdAction();
                    //value指定网络加载路径，没有则为空
                    pocketItem.action.value = (String) ShareUtils.get(UIUtils.getContext(),"game_image_url","");
                    LogUtils.i("HomePageDataCategory.java HomePageDataCategory game_image_url  "+pocketItem.action.value);
                    pocketItem.detail = new PocketItemDetail();
                    pocketItem.detail.background = "";
                    pocketItem.detail.floaticon = "";
                    pocketItem.detail.foreground = "game";
                    pocketItem.detail.icon = "";
                    pocketItem.detail.mixicon = "R.drawable.mixicon_game";
                    pockets.pocketitems.add(pocketItem);
                }
                pockets.pocketitemtexts = new ArrayList<PocketItemText>();
                pockets.textanimationtype = 0;
                pockets.titleid = "ktc_00" + i;
                homePageData.pockets.add(pockets);

            }else if(i == 2){
                Pockets pockets = new Pockets();
                pockets.animationtype = 0;
                pockets.expand = "";
                pockets.pocketitems = new ArrayList<PocketItem>();
                for (int j = 0; j < 1; j++) {
                    PocketItem pocketItem = new PocketItem();
                    pocketItem.action = new ClickCmdAction();
                    //value指定网络加载路径，没有则为空
                    pocketItem.action.value = (String) ShareUtils.get(UIUtils.getContext(),"edu_image_url","");
                    LogUtils.i("HomePageDataCategory.java HomePageDataCategory edu_image_url  "+pocketItem.action.value);
//                    pocketItem.action.value = "";
                    pocketItem.detail = new PocketItemDetail();
                    pocketItem.detail.background = "";
                    pocketItem.detail.floaticon = "";
                    pocketItem.detail.foreground = "edu";
                    pocketItem.detail.icon = "";
                    pocketItem.detail.mixicon = "R.drawable.mixicon_edu";
                    pockets.pocketitems.add(pocketItem);
                }
                pockets.pocketitemtexts = new ArrayList<PocketItemText>();
                pockets.textanimationtype = 0;
                pockets.titleid = "ktc_00" + i;
                homePageData.pockets.add(pockets);

            }else if(i == 3){
                Pockets pockets = new Pockets();
                pockets.animationtype = 0;
                pockets.expand = "";
                pockets.pocketitems = new ArrayList<PocketItem>();
                for (int j = 0; j < 1; j++) {
                    PocketItem pocketItem = new PocketItem();
                    pocketItem.action = new ClickCmdAction();
                    //value指定网络加载路径，没有则为空
                    pocketItem.action.value = (String) ShareUtils.get(UIUtils.getContext(),"music_image_url","");
                    LogUtils.i("HomePageDataCategory.java HomePageDataCategory music_image_url  "+pocketItem.action.value);
//                    pocketItem.action.value = "";
                    pocketItem.detail = new PocketItemDetail();
                    pocketItem.detail.background = "";
                    pocketItem.detail.floaticon = "";
                    pocketItem.detail.foreground = "music";
                    pocketItem.detail.icon = "";
                    int    picNo     = new Random().nextInt(3)+1;
                    pocketItem.detail.mixicon = "R.drawable.mixicon_music_"+picNo;
                    pockets.pocketitems.add(pocketItem);
                }
                pockets.pocketitemtexts = new ArrayList<PocketItemText>();

                pockets.textanimationtype = 0;
                pockets.titleid = "ktc_00" + i;
                homePageData.pockets.add(pockets);
            }else if(i == 4){
                Pockets pockets = new Pockets();
                pockets.animationtype = 0;
                pockets.expand = "";
                pockets.pocketitems = new ArrayList<PocketItem>();
                for (int j = 0; j < 1; j++) {
                    PocketItem pocketItem = new PocketItem();
                    pocketItem.action = new ClickCmdAction();
                    //value指定网络加载路径，没有则为空
                    pocketItem.action.value = (String) ShareUtils.get(UIUtils.getContext(),"app_image_url","");
                    LogUtils.i("HomePageDataCategory.java HomePageDataCategory app_image_url  "+pocketItem.action.value);
//                    pocketItem.action.value = "";
                    pocketItem.detail = new PocketItemDetail();
                    pocketItem.detail.background = "";
                    pocketItem.detail.floaticon = "";
                    pocketItem.detail.foreground = "app";
                    pocketItem.detail.icon = "";
                    pocketItem.detail.mixicon = "R.drawable.mixicon_app";
                    pockets.pocketitems.add(pocketItem);
                }
                pockets.pocketitemtexts = new ArrayList<PocketItemText>();
                pockets.textanimationtype = 0;
                pockets.titleid = "ktc_00" + i;
                homePageData.pockets.add(pockets);
            }
            else if(i == 5){
                Pockets pockets = new Pockets();
                pockets.animationtype = 0;
                pockets.expand = "";
                pockets.pocketitems = new ArrayList<PocketItem>();
                for (int j = 0; j < 1; j++) {
                    PocketItem pocketItem = new PocketItem();
                    pocketItem.action = new ClickCmdAction();
                    //value指定网络加载路径，没有则为空
                    pocketItem.action.value = "";
                    pocketItem.detail = new PocketItemDetail();
                    pocketItem.detail.background = "";
                    pocketItem.detail.floaticon = "";
                    pocketItem.detail.foreground = "news";
                    pocketItem.detail.icon = "";
                    pocketItem.detail.mixicon = "R.drawable.mixicon_news";
                    pockets.pocketitems.add(pocketItem);
                }
                pockets.pocketitemtexts = new ArrayList<PocketItemText>();
                pockets.textanimationtype = 0;
                pockets.titleid = "ktc_00" + i;
                homePageData.pockets.add(pockets);
            }
        }
    }

    private void loadImageUrls() {
        LauncherActivity launcherActivity = wfActivity.get();
        if(launcherActivity.isNetConnected()){
            RequestQueue requestQueue = VolleySingleton.getVolleySingleton(launcherActivity).getRequestQueue();
            StringRequest stringRequest1 = new StringRequest(Method.GET,"http://app.sfgj.org/api/panasonicbanner",new
                    Listener<String>(){
                @Override
                public void onResponse(String s) {
                    //打印请求返回结果
                    LogUtils.i("HomePageDataCategory.java HomePageDataCategory onResponse  s:" + s);
                    if (s != null && !TextUtils.isEmpty(s)) {
                        SfgjEnty sfgjEnty = JSON.parseObject(s, SfgjEnty.class);
                        if (sfgjEnty != null) {
                            List<String> data = sfgjEnty.data;
                            if (data != null) {
                                LogUtils.i("HomePageDataCategory.java HomePageDataCategory onResponse  :"+data.size());
                                for (int i = 0; i < data.size(); i++) {
                                    if (i == 1) {
                                        ShareUtils.put(UIUtils.getContext(), "game_image_url", data.get(i));
                                    } else if (i == 2) {
                                        ShareUtils.put(UIUtils.getContext(), "edu_image_url", data.get(i));
                                    } else if (i == 4) {
                                        ShareUtils.put(UIUtils.getContext(), "app_image_url", data.get(i));
                                    }
                                }
                            }

                        }
                    }

                }
            },new ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtils.i("HomePageDataCategory.java HomePageDataCategory onErrorResponse  "+volleyError.getMessage());
                }
            });
            //将StringRequest对象添加进RequestQueue请求队列中
            VolleySingleton.getVolleySingleton(launcherActivity).addToRequestQueue(stringRequest1);

            StringRequest stringRequest2 = new StringRequest(Method.GET,"http://test60.audiocn.org/tian/oauth/getLauncherImage.json",new
                    Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            //打印请求返回结果
                            LogUtils.i("HomePageDataCategory.java HomePageDataCategory onResponse  s:"+s);
                            if(s!=null&& !TextUtils.isEmpty(s)){
                                TlkgEnty tlkgEnty = JSON.parseObject(s, TlkgEnty.class);
                                if(tlkgEnty!=null&&tlkgEnty.result==1){
                                    ShareUtils.put(UIUtils.getContext(), "music_image_url", tlkgEnty.image);
                                }
                            }
                        }
                    },new ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtils.i("HomePageDataCategory.java HomePageDataCategory onErrorResponse  "+volleyError.getMessage());
                }
            });
            //将StringRequest对象添加进RequestQueue请求队列中
            VolleySingleton.getVolleySingleton(launcherActivity).addToRequestQueue(stringRequest2);
        }
    }

    public HomePageData getHomePageData() {
        return this.homePageData;
    }
}
