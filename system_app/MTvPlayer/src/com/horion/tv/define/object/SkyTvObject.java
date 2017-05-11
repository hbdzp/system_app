package com.horion.tv.define.object;

import java.io.Serializable;
import java.util.UUID;

public abstract class SkyTvObject implements Serializable, Cloneable {
    private static final long serialVersionUID = 7962133867380986197L;
    protected transient boolean bAfterDeserialize;
    public String displayName;
    public String id;//用于识别的标志
    public int index;
    public boolean invalid;
    public String name;
    private transient Object object;

    public SkyTvObject() {
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

    public SkyTvObject(String str) {
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

    public SkyTvObject(String id, String name) {
        this.id = null;
        this.name = null;
        this.displayName = null;
        this.index = 0;
        this.invalid = false;
        this.bAfterDeserialize = false;
        this.object = null;
        this.id = id;
        this.name = name;
        setDisplayName(name);
    }

    public final void afterDeserialize() {
        synchronized (this) {
            if (!this.bAfterDeserialize) {
                doAfterDeserialize();
                this.bAfterDeserialize = true;
            }
        }
    }

    public SkyTvObject clone() throws CloneNotSupportedException {
        return (SkyTvObject) super.clone();
    }

    protected abstract void doAfterDeserialize();

    public boolean equals(Object obj) {
        try {
            return ((SkyTvObject) obj).id.equals(this.id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public <T> T getObject(Class<T> cls) {
        return (T)this.object;
    }

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
