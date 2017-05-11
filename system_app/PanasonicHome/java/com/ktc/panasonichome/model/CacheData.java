package com.ktc.panasonichome.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CacheData implements Serializable {
    private static final long serialVersionUID = 4884323518751173649L;
    public List<String> list = new ArrayList();

    public CacheData(List<String> list) {
        this.list = list;
    }

    public CacheData(byte[] byteData) {
        try {
            this.list = ((CacheData) new ObjectInputStream(new ByteArrayInputStream(byteData)).readObject()).list;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] toBytes() {
        try {
            ByteArrayOutputStream byteArrayIn = new ByteArrayOutputStream();
            new ObjectOutputStream(byteArrayIn).writeObject(this);
            return byteArrayIn.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
