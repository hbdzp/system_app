package com.horion.tv.service.menu.docment;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public abstract class KtcBaseDocment {
    private DocumentBuilder docBuilder = null;
    protected Document mDoc = null;
    private String mFilename = null;

    public KtcBaseDocment() {
        try {
            this.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public KtcBaseDocment(String str) {
        try {
            this.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.mFilename = str;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilename() {
        return this.mFilename;
    }

    public abstract TCXmlNode getItemsByName(String str);

    public boolean init() {
        try {
            this.mDoc = this.docBuilder.parse(new File(this.mFilename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void setFilename(String str) {
        this.mFilename = str;
    }
}
