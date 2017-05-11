package com.horion.tv.define.object;

import java.io.Serializable;
import java.util.UUID;

public abstract class KtcTvObject implements Serializable, Cloneable {
    private static final long serialVersionUID = 7962133867380986197L;
    protected transient boolean bAfterDeserialize;
    public String displayName;
    public String id;
    public int index;
    public boolean invalid;
    public String name;
    private transient Object object;

    public KtcTvObject() {
        this.id = null;
        this.name = null;
        this.displayName = null;
        this.index = 0;
        this.invalid = false;
        this.bAfterDeserialize = false;
        this.object = null;
        this.id = UUID.randomUUID().toString();
        this.name = "";
        setDisplayName(this.name);
    }

    public KtcTvObject(String str) {
        this.id = null;
        this.name = null;
        this.displayName = null;
        this.index = 0;
        this.invalid = false;
        this.bAfterDeserialize = false;
        this.object = null;
        this.id = UUID.randomUUID().toString();
        this.name = str;
        setDisplayName(str);
    }

    public KtcTvObject(String str, String str2) {
        this.id = null;
        this.name = null;
        this.displayName = null;
        this.index = 0;
        this.invalid = false;
        this.bAfterDeserialize = false;
        this.object = null;
        this.id = str;
        this.name = str2;
        setDisplayName(str2);
    }

    public final void afterDeserialize() {
        synchronized (this) {
            if (!this.bAfterDeserialize) {
                doAfterDeserialize();
                this.bAfterDeserialize = true;
            }
        }
    }

    public KtcTvObject clone() throws CloneNotSupportedException {
        return (KtcTvObject) super.clone();
    }

    protected abstract void doAfterDeserialize();

    public boolean equals(Object obj) {
        try {
            return ((KtcTvObject) obj).id.equals(this.id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getDisplayName() {
        return this.displayName;
    }

    ///public <T> T getObject(Class<T> cls) {
    ///    return this.object;
   //// }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setObject(Object obj) {
        this.object = obj;
    }

    public String toString() {
        return getClass().getSimpleName() + "/" + getDisplayName();
    }
}
