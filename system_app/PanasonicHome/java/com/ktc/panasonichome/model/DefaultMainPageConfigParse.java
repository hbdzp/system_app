package com.ktc.panasonichome.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DefaultMainPageConfigParse {
    private HashMap<String, String> mData = new HashMap();
    private boolean mInit = false;
    private boolean mParseOk = false;
    private String mXmlpath = null;

    private class ConfigParseHandler extends DefaultHandler {
        private String currentkey;
        private String currentvalue;

        private ConfigParseHandler() {
        }

        public void startDocument() throws SAXException {
            super.startDocument();
        }

        public void endDocument() throws SAXException {
            super.endDocument();
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if ("config".equals(qName)) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if ("name".equals(attributes.getLocalName(i))) {
                        this.currentkey = attributes.getValue(i);
                    }
                    if ("value".equals(attributes.getLocalName(i))) {
                        this.currentvalue = attributes.getValue(i);
                    }
                }
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if ("config".equals(qName) && this.currentkey != null && this.currentvalue != null) {
                DefaultMainPageConfigParse.this.mData.put(this.currentkey, this.currentvalue);
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
        }
    }

    public DefaultMainPageConfigParse(String configXmlPath) {
        this.mXmlpath = configXmlPath;
    }

    public boolean parseNow() {
        if (this.mInit) {
            return this.mParseOk;
        }
        this.mParseOk = false;
        if (this.mXmlpath != null) {
            File xmlfile = new File(this.mXmlpath);
            if (xmlfile.exists()) {
                try {
                    SAXParserFactory.newInstance().newSAXParser().parse(xmlfile, new ConfigParseHandler());
                    this.mParseOk = true;
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

    public HashMap<String, String> getDefaultConfig() {
        if (!this.mParseOk || this.mData.size() <= 0) {
            return null;
        }
        return this.mData;
    }
}
