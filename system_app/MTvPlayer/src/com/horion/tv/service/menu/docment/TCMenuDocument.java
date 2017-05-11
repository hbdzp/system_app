package com.horion.tv.service.menu.docment;

////import com.ktc.framework.ktcsdk.logger.KtcLogger;
import com.horion.net.define.NetworkDefs;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TCMenuDocument extends KtcBaseDocment {
    private static String TAG = "langlang";
    private String scence = null;

    public TCMenuDocument(String str, String str2) {
        super(str);
        this.scence = str2;
    }

    public TCXmlNode getItemsByName(String str) {
        int i = 0;
        try {
            NodeList elementsByTagName = this.mDoc.getElementsByTagName("MenuItem");
            if (elementsByTagName == null) {
                ////KtcLogger.d(TAG, getFilename() + ": not exsit MenuItem.");
                return null;
            }
            for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                Node item = elementsByTagName.item(i2);
                if (str.equals(item.getAttributes().getNamedItem("name").getTextContent())) {
                    ////TCXmlNode parseNode = parseNode(item);
                    TCMenuNode parseNode = parseNode(item);
                    if (parseNode == null) {
                        ////KtcLogger.d(TAG, "parse error");
                        return null;
                    }
                    parseNode.setParent(parseNode(item.getParentNode()));
                    elementsByTagName = item.getChildNodes();
                    while (i < elementsByTagName.getLength()) {
                        item = elementsByTagName.item(i);
                        if (item.getNodeName().equals("MenuItem")) {
                            parseNode.addChild(parseNode(item));
                        }
                        i++;
                    }
                    return parseNode;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getScence() {
        return this.scence;
    }

    public TCMenuNode parseNode(Node node) {
        TCMenuNode tCMenuNode = null;
        if (!(node == null || node.getAttributes() == null)) {
            tCMenuNode = new TCMenuNode();
            if (node.getAttributes().getNamedItem("name") != null) {
                tCMenuNode.setName(node.getAttributes().getNamedItem("name").getTextContent());
            }
            if (node.getAttributes().getNamedItem("command") != null) {
                tCMenuNode.setCommand(node.getAttributes().getNamedItem("command").getTextContent());
            }
            if (node.getAttributes().getNamedItem(NetworkDefs.KEY_NET_STATE_TARGET) != null) {
                tCMenuNode.setTarget(node.getAttributes().getNamedItem(NetworkDefs.KEY_NET_STATE_TARGET).getTextContent());
            }
            if (node.getAttributes().getNamedItem("isFlipOutMenu") != null) {
                tCMenuNode.setIsflipout(node.getAttributes().getNamedItem("isFlipOutMenu").getTextContent().equals("true"));
            }
            if (node.getAttributes().getNamedItem("adpter") != null) {
                tCMenuNode.setAdpter(node.getAttributes().getNamedItem("adpter").getTextContent());
            }
            if (node.getAttributes().getNamedItem("switchtype") != null) {
                tCMenuNode.setSwitchType(Integer.parseInt(node.getAttributes().getNamedItem("switchtype").getTextContent()));
            }
            if (node.getAttributes().getNamedItem("checknet") != null) {
                tCMenuNode.setChecknet(node.getAttributes().getNamedItem("checknet").getTextContent().equals("true"));
            }
            if (node.getAttributes().getNamedItem("isFromPlugin") != null) {
                tCMenuNode.setFromPlugin(node.getAttributes().getNamedItem("isFromPlugin").getTextContent().equals("true"));
            }
            if (node.getAttributes().getNamedItem("refresh") != null) {
                tCMenuNode.setNeedRefresh(node.getAttributes().getNamedItem("refresh").getTextContent().equals("true"));
            }
            if (node.getAttributes().getNamedItem("displaymode") != null) {
            }
            if (node.getAttributes().getNamedItem("showuitype") != null) {
                tCMenuNode.setShowUitype(node.getAttributes().getNamedItem("showuitype").getTextContent());
            }
            if (node.getAttributes().getNamedItem("resetitems") != null) {
                tCMenuNode.setResetItems(node.getAttributes().getNamedItem("resetitems").getTextContent());
            }
            if (node.getAttributes().getNamedItem("clsname") != null) {
                tCMenuNode.setExecClzname(node.getAttributes().getNamedItem("clsname").getTextContent());
            }
            if (node.getAttributes().getNamedItem("pkgname") != null) {
                tCMenuNode.setExecPkgname(node.getAttributes().getNamedItem("pkgname").getTextContent());
            }
            if (node.getAttributes().getNamedItem("getvalcmd") != null) {
                tCMenuNode.setGetvalueCmd(node.getAttributes().getNamedItem("getvalcmd").getTextContent());
            }
            if (node.getAttributes().getNamedItem("setvalcmd") != null) {
                tCMenuNode.setSetvalueCmd(node.getAttributes().getNamedItem("setvalcmd").getTextContent());
            }
            if (node.getAttributes().getNamedItem("valueType") != null) {
                tCMenuNode.setValueType(node.getAttributes().getNamedItem("valueType").getTextContent());
            }
            if (node.getAttributes().getNamedItem("hasDemo") != null) {
                tCMenuNode.setHasDemo(node.getAttributes().getNamedItem("hasDemo").getTextContent().equals("true"));
            }
            if (node.getAttributes().getNamedItem("action") != null) {
                tCMenuNode.setExecActionName(node.getAttributes().getNamedItem("action").getTextContent());
            }
            if (node.getAttributes().getNamedItem("demoid") != null) {
                tCMenuNode.setDemoId(node.getAttributes().getNamedItem("demoid").getTextContent());
            }
            if (node.getAttributes().getNamedItem("showcondition") != null) {
                tCMenuNode.setShowCondition(node.getAttributes().getNamedItem("showcondition").getTextContent());
            }
            if (node.getAttributes().getNamedItem("isSlidExec") != null) {
                tCMenuNode.setIsflipout(node.getAttributes().getNamedItem("isSlidExec").getTextContent().equals("true"));
            }
            tCMenuNode.setScence(this.scence);
        }
        return tCMenuNode;
    }

    public void setScence(String str) {
        this.scence = str;
    }
}
