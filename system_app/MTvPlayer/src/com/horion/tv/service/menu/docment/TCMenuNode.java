package com.horion.tv.service.menu.docment;

import com.ktc.framework.ktcsdk.util.KtcDataComposer;
import com.horion.net.define.NetworkDefs;

public class TCMenuNode extends TCXmlNode {
    private String actionName = null;
    private String adpter = null;
    private boolean checknet = false;
    private String command = null;
    private String demoId = null;
    private String displayName = null;
    private boolean enable = true;
    private String execClzname = null;
    private String execPkgname = null;
    private boolean fromPlugin = true;
    private String getvalueCmd = null;
    private boolean hasDemo = false;
    private boolean isSlidExec = false;
    private boolean isflipout = false;
    private String name = null;
    private boolean needRefresh = false;
    private TCMenuNode parent = null;
    private String refreshItems = null;
    private String scence = null;
    private String setvalueCmd = null;
    private String showCondition = null;
    private String showUitype = "";
    private int switchType = 0;
    private String target = null;
    private String valueType = null;

    static class a {
        a() {
        }

        public void test() {
            System.out.print("a.test");
        }
    }

    static class b extends a {
        b() {
        }

        public void test() {
            System.out.println("b.test");
        }
    }

    public static void main(String[] strArr) {
        print(new b());
    }

    static void print(a aVar) {
        aVar.test();
    }

    public String getAdpter() {
        return this.adpter;
    }

    public String getCommand() {
        return this.command;
    }

    public String getDemoId() {
        return this.demoId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getExecActionName() {
        return this.actionName;
    }

    public String getExecClzname() {
        return this.execClzname;
    }

    public String getExecPkgname() {
        return this.execPkgname;
    }

    public String getGetvalueCmd() {
        return this.getvalueCmd;
    }

    public String getName() {
        return this.name;
    }

    public TCMenuNode getParent() {
        return this.parent;
    }

    public String getResetItems() {
        return this.refreshItems;
    }

    public String getScence() {
        return this.scence;
    }

    public String getSetvalueCmd() {
        return this.setvalueCmd;
    }

    public String getShowCondition() {
        return this.showCondition;
    }

    public String getShowUitype() {
        return this.showUitype;
    }

    public int getSwitchType() {
        return this.switchType;
    }

    public String getTarget() {
        return this.target;
    }

    public String getValueType() {
        return this.valueType;
    }

    public boolean isChecknet() {
        return this.checknet;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public boolean isFlipout() {
        return this.isflipout;
    }

    public boolean isFromPlugin() {
        return this.fromPlugin;
    }

    public boolean isHasDemo() {
        return this.hasDemo;
    }

    public boolean isNeedRefresh() {
        return this.needRefresh;
    }

    public boolean isSlidExec() {
        return this.isSlidExec;
    }

    public void setAdpter(String str) {
        this.adpter = str;
    }

    public void setChecknet(boolean z) {
        this.checknet = z;
    }

    public void setCommand(String str) {
        this.command = str;
    }

    public void setDemoId(String str) {
        this.demoId = str;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public void setEnable(boolean z) {
        this.enable = z;
    }

    public void setExecActionName(String str) {
        this.actionName = str;
    }

    public void setExecClzname(String str) {
        this.execClzname = str;
    }

    public void setExecPkgname(String str) {
        this.execPkgname = str;
    }

    public void setFromPlugin(boolean z) {
        this.fromPlugin = z;
    }

    public void setGetvalueCmd(String str) {
        this.getvalueCmd = str;
    }

    public void setHasDemo(boolean z) {
        this.hasDemo = z;
    }

    public void setIsflipout(boolean z) {
        this.isflipout = z;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNeedRefresh(boolean z) {
        this.needRefresh = z;
    }

    public void setParent(TCMenuNode tCMenuNode) {
        this.parent = tCMenuNode;
    }

    public void setResetItems(String str) {
        this.refreshItems = str;
    }

    public void setScence(String str) {
        this.scence = str;
    }

    public void setSetvalueCmd(String str) {
        this.setvalueCmd = str;
    }

    public void setShowCondition(String str) {
        this.showCondition = str;
    }

    public void setShowUitype(String str) {
        this.showUitype = str;
    }

    public void setSlidExec(boolean z) {
        this.isSlidExec = z;
    }

    public void setSwitchType(int i) {
        this.switchType = i;
    }

    public void setTarget(String str) {
        this.target = str;
    }

    public void setValueType(String str) {
        this.valueType = str;
    }

    public String toString() {
        KtcDataComposer ktcDataComposer = new KtcDataComposer();
        ktcDataComposer.addValue("name", getName());
        ktcDataComposer.addValue(NetworkDefs.KEY_NET_STATE_TARGET, getTarget());
        ktcDataComposer.addValue("command", getCommand());
        ktcDataComposer.addValue("isfromplugin", isFromPlugin());
        return ktcDataComposer.toString();
    }
}
