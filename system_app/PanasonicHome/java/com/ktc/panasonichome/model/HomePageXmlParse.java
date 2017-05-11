package com.ktc.panasonichome.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HomePageXmlParse {
    private HomePageData mData = null;
    private boolean mInit = false;
    private boolean mParseOk = false;
    private String mXmlpath = null;

    private class ParseHandler extends DefaultHandler {
        private CItem currentCItem;
        private Category currentCategory;
        private String currentLanKey;
        private Language currentLanguage;
        private String currentLanguageType;
        private PocketItem currentPocketItem;
        private PocketItemText currentPocketItemText;
        private Pockets currentPockets;
        private boolean isCItemEnd;
        private boolean isCategoryEnd;
        private boolean isCurrentLanKeyEnd;
        private boolean isCurrentLanguageTypeEnd;
        private boolean isLanguageEnd;
        private boolean isLegalXml;
        private boolean isPocketsEnd;
        private HashMap<String, String> lanHashMap;

        private ParseHandler() {
            this.currentPockets = null;
            this.currentPocketItem = null;
            this.currentPocketItemText = null;
            this.isPocketsEnd = true;
            this.currentCategory = null;
            this.isCategoryEnd = true;
            this.currentCItem = null;
            this.isCItemEnd = true;
            this.currentLanguage = null;
            this.lanHashMap = null;
            this.isLanguageEnd = true;
            this.currentLanguageType = null;
            this.isCurrentLanguageTypeEnd = true;
            this.currentLanKey = null;
            this.isCurrentLanKeyEnd = true;
            this.isLegalXml = false;
        }

        public boolean isLegalXml() {
            return this.isLegalXml;
        }

        public void startDocument() throws SAXException {
            super.startDocument();
            HomePageXmlParse.this.mData = new HomePageData();
        }

        public void endDocument() throws SAXException {
            super.endDocument();
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            int i;
            super.startElement(uri, localName, qName, attributes);
            if ("pockets".equals(qName)) {
                this.currentPockets = new Pockets();
                this.isPocketsEnd = false;
                this.isLegalXml = true;
            }
            if ("pocket".equals(qName) && !this.isPocketsEnd) {
                for (i = 0; i < attributes.getLength(); i++) {
                    if ("titleid".equals(attributes.getLocalName(i))) {
                        this.currentPockets.titleid = attributes.getValue(i);
                    }
                    if ("animationtype".equals(attributes.getLocalName(i))) {
                        this.currentPockets.animationtype = Integer.valueOf(attributes.getValue(i)).intValue();
                    }
                    if ("textanimationtype".equals(attributes.getLocalName(i))) {
                        this.currentPockets.textanimationtype = Integer.valueOf(attributes.getValue(i)).intValue();
                    }
                    if ("expand".equals(attributes.getLocalName(i))) {
                        this.currentPockets.expand = attributes.getValue(i);
                    }
                }
            }
            if ("pocketitem".equals(qName)) {
                this.currentPocketItem = new PocketItem();
            }
            if ("pocketitemtext".equals(qName)) {
                this.currentPocketItemText = new PocketItemText();
                for (i = 0; i < attributes.getLength(); i++) {
                    if ("titleid".equals(attributes.getLocalName(i))) {
                        this.currentPocketItemText.titleid = attributes.getValue(i);
                    }
                }
            }
            if ("detail".equals(qName)) {
                if (!this.isPocketsEnd) {
                    PocketItemDetail detail = new PocketItemDetail();
                    for (i = 0; i < attributes.getLength(); i++) {
                        if ("icon".equals(attributes.getLocalName(i))) {
                            detail.icon = attributes.getValue(i);
                        }
                        if ("floaticon".equals(attributes.getLocalName(i))) {
                            detail.floaticon = attributes.getValue(i);
                        }
                        if ("background".equals(attributes.getLocalName(i))) {
                            detail.background = attributes.getValue(i);
                        }
                        if ("foreground".equals(attributes.getLocalName(i))) {
                            detail.foreground = attributes.getValue(i);
                        }
                        if ("mixicon".equals(attributes.getLocalName(i))) {
                            detail.mixicon = attributes.getValue(i);
                        }
                    }
                    this.currentPocketItem.detail = detail;
                } else if (!this.isCategoryEnd && this.isCItemEnd) {
                    for (i = 0; i < attributes.getLength(); i++) {
                        if ("titleid".equals(attributes.getLocalName(i))) {
                            this.currentCategory.titleid = attributes.getValue(i);
                        }
                    }
                } else if (!(this.isCategoryEnd || this.isCItemEnd)) {
                    CItemDetail detail2 = new CItemDetail();
                    for (i = 0; i < attributes.getLength(); i++) {
                        if ("icon".equals(attributes.getLocalName(i))) {
                            detail2.icon = attributes.getValue(i);
                        }
                        if ("defaulticonid".equals(attributes.getLocalName(i))) {
                            detail2.defaulticonid = attributes.getValue(i);
                        }
                        if ("floaticon".equals(attributes.getLocalName(i))) {
                            detail2.floaticon = attributes.getValue(i);
                        }
                        if ("icon4k".equals(attributes.getLocalName(i))) {
                            detail2.icon4k = attributes.getValue(i);
                        }
                        if ("floaticon4k".equals(attributes.getLocalName(i))) {
                            detail2.floaticon4k = attributes.getValue(i);
                        }
                        if ("floatanim".equals(attributes.getLocalName(i))) {
                            detail2.floatanim = attributes.getValue(i);
                        }
                        if ("titleid".equals(attributes.getLocalName(i))) {
                            detail2.titleid = attributes.getValue(i);
                        }
                        if ("descid".equals(attributes.getLocalName(i))) {
                            detail2.descid = attributes.getValue(i);
                        }
                        if ("rating".equals(attributes.getLocalName(i))) {
                            detail2.rating = attributes.getValue(i);
                        }
                    }
                    this.currentCItem.detail = detail2;
                }
            }
            if ("cmd_action".equals(qName)) {
                ClickCmdAction action;
                if (!this.isPocketsEnd) {
                    action = new ClickCmdAction();
                    for (i = 0; i < attributes.getLength(); i++) {
                        if ("value".equals(attributes.getLocalName(i))) {
                            action.value = attributes.getValue(i);
                        }
                    }
                    this.currentPocketItem.action = action;
                } else if (!this.isCategoryEnd) {
                    action = new ClickCmdAction();
                    for (i = 0; i < attributes.getLength(); i++) {
                        if ("value".equals(attributes.getLocalName(i))) {
                            action.value = attributes.getValue(i);
                        }
                    }
                    this.currentCItem.action = action;
                }
            }
            if ("category".equals(qName)) {
                this.currentCategory = new Category();
                this.isCategoryEnd = false;
            }
            if ("item".equals(qName)) {
                this.currentCItem = new CItem();
                this.isCItemEnd = false;
            }
            if ("layout".equals(qName) && !this.isCategoryEnd) {
                CLayout layout = new CLayout();
                for (i = 0; i < attributes.getLength(); i++) {
                    if ("shape".equals(attributes.getLocalName(i))) {
                        layout.shape = attributes.getValue(i);
                    }
                    if ("color".equals(attributes.getLocalName(i))) {
                        layout.color = attributes.getValue(i);
                    }
                    if ("type".equals(attributes.getLocalName(i))) {
                        layout.type = attributes.getValue(i);
                    }
                }
                this.currentCItem.layout = layout;
            }
            if ("language".equals(qName)) {
                this.currentLanguage = new Language();
                this.isLanguageEnd = false;
                for (i = 0; i < attributes.getLength(); i++) {
                    if ("default".equals(attributes.getLocalName(i))) {
                        this.currentLanguage.defaultLan = attributes.getValue(i);
                    }
                }
            } else if (!this.isLanguageEnd && this.isCurrentLanguageTypeEnd && this.isCurrentLanKeyEnd) {
                this.currentLanguageType = qName;
                this.lanHashMap = new HashMap();
                this.isCurrentLanguageTypeEnd = false;
            } else if (!(this.isLanguageEnd || this.isCurrentLanguageTypeEnd || !this.isCurrentLanKeyEnd)) {
                this.currentLanKey = qName;
                this.isCurrentLanKeyEnd = false;
            }
            System.out.println("    start element  ------------  ");
            System.out.println("    localName: " + localName);
            System.out.println("    qName: " + qName);
            System.out.println("    attributes.getLength() = " + attributes.getLength());
            for (i = 0; i < attributes.getLength(); i++) {
                System.out.println("attributes.getLocalName(" + i + ") = " + attributes.getLocalName(i) + "  attributes.getValue(" + i + ")= " + attributes.getValue(i));
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if ("pockets".equals(qName)) {
                this.isPocketsEnd = true;
                HomePageXmlParse.this.mData.pockets.add(this.currentPockets);
            }
            if ("pocketitem".equals(qName) && !this.isPocketsEnd) {
                this.currentPockets.pocketitems.add(this.currentPocketItem);
            }
            if ("pocketitemtext".equals(qName) && !this.isPocketsEnd) {
                this.currentPockets.pocketitemtexts.add(this.currentPocketItemText);
            }
            if ("category".equals(qName)) {
                this.isCategoryEnd = true;
                HomePageXmlParse.this.mData.category = this.currentCategory;
            }
            if ("item".equals(qName) && !this.isCategoryEnd) {
                this.isCItemEnd = true;
                this.currentCategory.citems.add(this.currentCItem);
            }
            if ("language".equals(qName)) {
                this.isLanguageEnd = true;
                HomePageXmlParse.this.mData.language = this.currentLanguage;
            }
            if (this.currentLanKey != null && this.currentLanKey.equals(qName)) {
                this.isCurrentLanKeyEnd = true;
            }
            if (this.currentLanguageType != null && this.currentLanguageType.equals(qName)) {
                this.isCurrentLanguageTypeEnd = true;
                this.currentLanguage.languagemap.put(this.currentLanguageType, this.lanHashMap);
            }
            System.out.println("end element-----------");
            System.out.println("    localName: " + localName);
            System.out.println("    qName: " + qName);
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            System.out.println("characters-----------");
            System.out.println("    ch: " + ch);
            System.out.println("    start: " + start);
            System.out.println("    length: " + length);
            System.out.println("    text = " + new String(ch, start, length));
            if (!this.isLanguageEnd && !this.isCurrentLanguageTypeEnd && !this.isCurrentLanKeyEnd) {
                this.lanHashMap.put(this.currentLanKey, new String(ch, start, length));
            }
        }
    }

    public HomePageXmlParse(String xmlpath) {
        this.mXmlpath = xmlpath;
    }

    public boolean parseNow() {
        if (this.mInit) {
            return this.mParseOk;
        }
        this.mParseOk = false;
        if (this.mXmlpath != null) {
            File xmlfile = new File(this.mXmlpath);
            if (xmlfile.exists()) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                try {
                    ParseHandler newParseHandler = new ParseHandler();
                    factory.newSAXParser().parse(xmlfile, newParseHandler);
                    if (newParseHandler.isLegalXml()) {
                        this.mParseOk = true;
                    }
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                } catch (ParserConfigurationException e3) {
                    e3.printStackTrace();
                }
            }
        }
        this.mInit = true;
        return this.mParseOk;
    }

    public HomePageData getHomePageData() {
        if (!this.mParseOk || this.mData == null) {
            return null;
        }
        return this.mData;
    }
}
