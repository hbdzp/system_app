package com.ktc.framework.ktcsdk.util;

/////import com.ktc.framework.ktcsdk.logger.KtcLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KtcDataDecomposer {
    final String StringPrefix;
    private boolean isMegaData;
    private byte[] megaData;
    private int megaDataLength;
    private HashMap<String, String> values;

    public KtcDataDecomposer() {
        this.megaDataLength = 0;
        this.isMegaData = false;
        this.StringPrefix = "@str:";
        this.values = new HashMap();
    }

    public KtcDataDecomposer(String str) {
        this.megaDataLength = 0;
        this.isMegaData = false;
        this.StringPrefix = "@str:";
        this.values = new HashMap();
        parserString(str);
    }

    public KtcDataDecomposer(byte[] bArr, int i) {
        this.megaDataLength = 0;
        this.isMegaData = false;
        this.StringPrefix = "@str:";
        for (int i2 = 0; i2 < "@str:".length(); i2++) {
            if (bArr[i2] != "@str:".charAt(i2)) {
                this.isMegaData = true;
                break;
            }
        }
        if (this.isMegaData) {
            this.megaData = bArr;
            this.megaDataLength = i;
            return;
        }
        parserString(new String(bArr).substring(0, i));
    }

    private String decode(String str) {
        return new String(str).replace("%3D", "=").replace("%3B", ";").replace("%5D", "]").replace("%2C", ",").replace("%5B", "[").replace("%25", "%");
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

    public boolean getBooleanValue(String str) {
        return getStringValue(str).equals("true");
    }

    ////public byte[] getBytesValue(String str) {
    ////    String str2 = (String) this.values.get(str);
    ////    return str2 == null ? null : Base64.decode(str2);
    ////}

    public double getDoubleValue(String str) {
        return Double.parseDouble(getStringValue(str));
    }

    public float getFloatValue(String str) {
        return Float.parseFloat(getStringValue(str));
    }

    public int getIntValue(String str) {
        return Integer.parseInt(getStringValue(str));
    }

    public byte[] getMegaData() {
        return this.isMegaData ? this.megaData : null;
    }

    public int getMegaDataLength() {
        return this.megaDataLength;
    }

    public List<String> getStringListValue(String str) {
        List<String> arrayList = new ArrayList();
        String stringValue = getStringValue(str);
        if (stringValue == null) {
            return null;
        }
        if (stringValue.length() <= 2) {
            return arrayList;
        }
        String[] split = stringValue.substring(1, stringValue.length() - 1).split(",");
        for (String decode : split) {
            arrayList.add(decode(decode));
        }
        return arrayList;
    }

    public String getStringValue(String str) {
        return this.values.get(str) == null ? null : new String((String) this.values.get(str));
    }

    public boolean hasValue(String str) {
        return this.values.containsKey(str);
    }

    public boolean isMegaData() {
        return this.isMegaData;
    }

    public void parserString(String str) {
        if (str != null) {
            if (str.startsWith("@str:")) {
                this.values.clear();
                String[] split = str.substring("@str:".length()).split(";");
                for (int i = 0; i < split.length; i++) {
                    if (split[i].length() > 0) {
                        String[] split2 = split[i].split("=");
                        if (split2.length == 2) {
                            this.values.put(split2[0].trim(), decode(split2[1]).trim());
                        } else {
                            this.values.put(split2[0], "");
                        }
                    }
                }
                return;
            }
            /////KtcLogger.e("KtcDataDecomposer", "INVALID KTC DATA FORMAT");
        }
    }

    public void removeValue(String str) {
        this.values.remove(str);
    }

    public byte[] toByteArray() {
        return this.isMegaData ? this.megaData : toString().getBytes();
    }

    public String toString() {
        if (this.isMegaData) {
            return "";
        }
        String[] strArr = new String[this.values.size()];
        this.values.keySet().toArray(strArr);
        String str = "" + "@str:";
        for (int i = 0; i < strArr.length; i++) {
            str = str + strArr[i] + "=" + new String((String) this.values.get(strArr[i])) + ";";
        }
        return str;
    }
}
