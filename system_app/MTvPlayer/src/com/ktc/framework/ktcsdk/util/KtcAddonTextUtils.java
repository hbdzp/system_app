package com.ktc.framework.ktcsdk.util;

import android.text.TextUtils;
import android.util.Log;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
////import com.ktc.framework.ktcsdk.properties.KtcGeneralProperties;
////import com.ktc.framework.ktcsdk.properties.KtcGeneralProperties.GeneralPropKey;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class KtcAddonTextUtils {
    private static KtcAddonTextUtils instance = new KtcAddonTextUtils();
    private String resFilePATH = null;
    private HashMap<String, String> texts = new HashMap();

    private KtcAddonTextUtils() {
        initResFilePath();
        loadTexts();
    }

    public static KtcAddonTextUtils getInstance() {
        return (new KtcAddonTextUtils());
    }

    private void initResFilePath() {
        String str = "chinese";
        Log.d("Maxs50","Locale.ENGLISH.getLanguage() = " + Locale.ENGLISH.getLanguage());
        Log.d("Maxs50","Locale.getDefault().getLanguage() = " + Locale.getDefault().getLanguage());
        if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            str = "english";
        }
        ////this.resFilePATH = KtcGeneralProperties.getProperty(GeneralPropKey.ROPRODUCTCONFIGDIR) + "/addon/" + str + ".xml";
        this.resFilePATH = "/data/data/com.horion.tv/app_menu/" + str + ".xml";
    }

    private void loadTexts() {
        if (this.texts.isEmpty()) {
           /// KtcLogger.e("addon", "AddonText is empty ，start load xml !");
            try {
                loadXML();
                return;
            } catch (Exception e) {
               //// KtcLogger.e("addon", "Load addon file [" + this.resFilePATH + "] fail! " + e.toString());
                e.printStackTrace();
                return;
            }
        }
        ///KtcLogger.e("addon", "AddonText is not empty !");
    }

    private void loadXML() throws Exception {
        DocumentBuilder newDocumentBuilder;
        try {
            newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            newDocumentBuilder = null;
        }
        if (newDocumentBuilder != null) {
            NodeList elementsByTagName = newDocumentBuilder.parse(new File(this.resFilePATH)).getElementsByTagName("string");
            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                Node item = elementsByTagName.item(i);
                this.texts.put(item.getAttributes().getNamedItem("name").getTextContent(), item.getTextContent());
            }
        }
    }

    public String getText(String str) {
        String str2 = "";
        return (TextUtils.isEmpty(str) || this.texts == null || !this.texts.containsKey(str)) ? str2 : (String) this.texts.get(str);
    }
}
