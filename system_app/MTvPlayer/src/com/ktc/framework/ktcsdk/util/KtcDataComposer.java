package com.ktc.framework.ktcsdk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class KtcDataComposer {
    final String StringPrefix = "@str:";
    private boolean isMegaData = false;
    private byte[] megaData;
    private int megaDataLength = 0;
    private HashMap<String, String> values = new HashMap();

    private String decode(String str) {
        return new String(str).replace("%3D", "=").replace("%3B", ";").replace("%5D", "]").replace("%2C", ",").replace("%5B", "[").replace("%25", "%");
    }

    private String encode(String str) {
        return new String(str).replace("%", "%25").replace(";", "%3B").replace("[", "%5B").replace("]", "%5D").replace(",", "%2C").replace("=", "%3D");
    }

    public static void main(String[] strArr) {
        KtcDataComposer ktcDataComposer = new KtcDataComposer();
        ktcDataComposer.addValue("url", "http://a?b=%5Bcd%3D");
        ktcDataComposer.addValue("f", 0.123d);
        String ktcDataComposer2 = ktcDataComposer.toString();
        System.out.println(ktcDataComposer2);
        KtcDataDecomposer ktcDataDecomposer = new KtcDataDecomposer(ktcDataComposer2);
        System.out.println(ktcDataDecomposer.getFloatValue("f"));
        System.out.println(ktcDataDecomposer.getStringValue("url"));
    }

    public void addValue(String str, double d) {
        this.values.put(str, String.valueOf(d));
    }

    public void addValue(String str, float f) {
        this.values.put(str, String.valueOf(f));
    }

    public void addValue(String str, int i) {
        this.values.put(str, String.valueOf(i));
    }

    public void addValue(String str, String str2) {
        if (str2 == null) {
            str2 = "";
        }
        this.values.put(str, encode(str2));
    }

    public void addValue(String str, List<String> list) {
        StringBuffer stringBuffer = new StringBuffer(10000);
        stringBuffer.append("[");
        for (String str2 : list) {
            if (str2 != null) {
                stringBuffer.append(encode(str2));
                stringBuffer.append(",");
            }
        }
        stringBuffer.append("]");
        this.values.put(str, encode(stringBuffer.toString()));
    }

    public void addValue(String str, boolean z) {
        this.values.put(str, String.valueOf(z));
    }

    ////public void addValue(String str, byte[] bArr) {
    ////    if (bArr != null) {
    ////        addValue(str, Base64.encodeToString(bArr));
   ////     }
   ///// }

    public boolean getBooleanValue(String str) {
        return getStringValue(str).equals("true");
    }

    public double getDoubleValue(String str) {
        return Double.parseDouble(getStringValue(str));
    }

    public float getFloatValue(String str) {
        return Float.parseFloat(getStringValue(str));
    }

    public int getIntValue(String str) {
        return Integer.parseInt(getStringValue(str));
    }

    public List<String> getStringListValue(String str) {
        int i = 0;
        List<String> arrayList = new ArrayList();
        String stringValue = getStringValue(str);
        if (!(stringValue.startsWith("[") ? stringValue.endsWith("]") : false)) {
            return null;
        }
        String[] split = stringValue.substring(1, stringValue.length() - 2).split(",");
        int length = split.length;
        while (i < length) {
            arrayList.add(decode(split[i]));
            i++;
        }
        return arrayList;
    }

    public String getStringValue(String str) {
        return this.values.get(str) == null ? null : new String((String) this.values.get(str));
    }

    public boolean hasValue(String str) {
        return this.values.containsKey(str);
    }

    public void removeValue(String str) {
        this.values.remove(str);
    }

    public void setMegaData(byte[] bArr) {
        this.megaData = bArr;
        this.megaDataLength = bArr.length;
        this.isMegaData = true;
    }

    public byte[] toByteArray() {
        return this.isMegaData ? this.megaData : toString().getBytes();
    }

    public String toString() {
        if (this.isMegaData) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(10000);
        stringBuffer.append("@str:");
        for (Entry entry : this.values.entrySet()) {
            String str = (String) entry.getValue();
            stringBuffer.append((String) entry.getKey());
            stringBuffer.append("=");
            stringBuffer.append(str);
            stringBuffer.append(";");
        }
        return stringBuffer.toString();
    }
}
