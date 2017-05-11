package com.horion.tv.service.menu.docment;

import java.util.ArrayList;

public abstract class TCXmlNode {
    private ArrayList<TCXmlNode> childs;

    public TCXmlNode() {
        this.childs = null;
        this.childs = new ArrayList();
    }

    public boolean addChild(TCXmlNode tCXmlNode) {
        return this.childs.add(tCXmlNode);
    }

    public ArrayList<TCXmlNode> getChilds() {
        return this.childs;
    }

    public void setChilds(ArrayList<TCXmlNode> arrayList) {
        this.childs = arrayList;
    }
}
