package com.horion.tv.define.object;

///import com.horion.tv.utils.SkyTvUtils;
///import com.ktc.framework.skysdk.util.Base64;

import java.io.Serializable;
import java.util.UUID;

public class OnlineResource implements Serializable, Cloneable {
    private static final long serialVersionUID = 7687795479714320147L;
    public int code;
    public int duration;
    public String extra;
    public String id;
    public String play_url;
    public String source_from;
    public String title;
    public RES_TYPE type;

    public enum RES_TYPE {
        TIMESHIFT,
        LOOKBACK
    }

    public OnlineResource(String str, String str2) {
        this.title = null;
        this.id = null;
        this.source_from = null;
        this.play_url = null;
        this.duration = 0;
        this.code = 0;
        this.extra = "";
        this.type = null;
        this.id = UUID.randomUUID().toString();
        this.source_from = str2;
        this.title = str;
        this.play_url = parsePlayUrl(str2);
    }

    public OnlineResource(String str, String str2, String str3) {
        this.title = null;
        this.id = null;
        this.source_from = null;
        this.play_url = null;
        this.duration = 0;
        this.code = 0;
        this.extra = "";
        this.type = null;
        this.id = UUID.randomUUID().toString();
        this.source_from = str2;
        this.title = str;
        this.play_url = parsePlayUrl(str3);
    }

    public OnlineResource(String str, String str2, String str3, String str4) {
        this.title = null;
        this.id = null;
        this.source_from = null;
        this.play_url = null;
        this.duration = 0;
        this.code = 0;
        this.extra = "";
        this.type = null;
        this.id = str;
        this.source_from = str3;
        this.title = str2;
        this.play_url = parsePlayUrl(str4);
    }

    public static void main(String[] strArr) {
        String[] split = "".split("#");
        System.out.println("parse pg_url_list.length:" + split.length);
        if (split != null) {
            for (String str : split) {
                System.out.println("parse:" + str);
                OnlineResource onlineResource = new OnlineResource("", str);
                System.out.println(onlineResource.play_url + "/" + onlineResource.duration);
            }
        }
    }

    private String parsePlayUrl(String str) {
        try {
            String[] split = str.split("@");
            if (split != null && split.length > 0) {
                if (split.length == 2) {
                    System.out.println("result[0]:" + split[0] + "  result[1]:" + split[1]);
                    String[] split2 = split[1].split("\\?");
                    if (split2 != null) {
                        for (String split3 : split2) {
                            String split32 = null;
                            String[] split4 = split32.split("=");
                            if (split4 != null && split4.length == 2) {
                                String str2 = split4[0];
                                split32 = split4[1];
                                if (str2.equals("duration")) {
                                    this.duration = Integer.valueOf(split32).intValue();
                                }
                            }
                        }
                    }
                }
                return split[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static OnlineResource toOnlineResource(String str) {
       /// return (OnlineResource) SkyTvUtils.toObject(Base64.decode(str), OnlineResource.class);
        return  null;
    }

    public OnlineResource clone() {
        try {
            return (OnlineResource) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean equals(Object obj) {
        try {
            return ((OnlineResource) obj).id.equals(this.id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String toString() {
        /*return Base64.encodeToString(SkyTvUtils.toBytes(this));*/
        return null;
    }
}
