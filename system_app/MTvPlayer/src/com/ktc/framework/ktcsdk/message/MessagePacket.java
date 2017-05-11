package com.ktc.framework.ktcsdk.message;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;

///import com.ktc.framework.skysdk.logger.SkyLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
///import org.apache.http4.HttpStatus;

public class MessagePacket {
    private String content = "";
    private HashMap<String, String> extraParam = null;
    private String fromPkg = "";
    private byte[] iconImg;
    private String intentAction = "";
    private IntentType intentType = IntentType.startactivity;
    private boolean isDeleteAfterClick = false;
    private boolean isNotify = false;
    private String messageID = "";
    private MessageUIMode mode = MessageUIMode.text;
    private String notifyContent = "";
    private int notifyTime = 3;
    private String pkgName = "";
    private byte[] poserImg;
    private String pushID = "";
    private String saveTime = "";
    private String title = "";
    private String toCls = "";
    private String toPkg = "";
    private String validTime = "";

    public enum IntentType {
        startactivity,
        startservice,
        sendbrocast
    }

    public enum MessageUIMode {
        title_image_text,
        image_text,
        image_title,
        title_text,
        text
    }

    @Deprecated
    public MessagePacket(String str, String str2) {
        this.pushID = str;
        this.fromPkg = str2;
        this.toPkg = str2;
    }

    public MessagePacket(String str, String str2, String str3) {
        this.pushID = str;
        this.fromPkg = str2;
        this.toPkg = str3;
    }

    private byte[] bitmapToByte(Bitmap bitmap) {
        int i = 100;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        while (byteArrayOutputStream.toByteArray().length / 1024 > 300 && i > 0) {
            byteArrayOutputStream.reset();
            i -= 10;
            bitmap.compress(CompressFormat.PNG, i, byteArrayOutputStream);
        }
       // SkyLogger.i("msg_tag", "jpeg  size===========" + byteArrayOutputStream.toByteArray().length);
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] bitmapTobyteScaleWay(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 85, byteArrayOutputStream);
        float sqrt = (float) Math.sqrt((double) (((float) 30720) / ((float) byteArrayOutputStream.toByteArray().length)));
        Matrix matrix = new Matrix();
        matrix.setScale(sqrt, sqrt);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //SkyLogger.i("msg_tag", "bitmapTobyteScaleWay jpeg  size===========" + byteArrayOutputStream.toByteArray().length);
        byteArrayOutputStream.reset();
        createBitmap.compress(CompressFormat.PNG, 85, byteArrayOutputStream);
        while (byteArrayOutputStream.toByteArray().length > 30720) {
            System.out.println(byteArrayOutputStream.toByteArray().length);
            matrix.setScale(0.9f, 0.9f);
            createBitmap = Bitmap.createBitmap(createBitmap, 0, 0, createBitmap.getWidth(), createBitmap.getHeight(), matrix, true);
            byteArrayOutputStream.reset();
            createBitmap.compress(CompressFormat.PNG, 85, byteArrayOutputStream);
            //SkyLogger.i("msg_tag", "bitmapTobyteScaleWay jpeg while size===========" + byteArrayOutputStream.toByteArray().length);
        }
        //SkyLogger.i("msg_tag", "bitmapTobyteScaleWay jpeg return size===========" + byteArrayOutputStream.toByteArray().length);
        return byteArrayOutputStream.toByteArray();
    }

    public static void main(String[] strArr) {
    }

    public byte[] ExtraSerialize() {
        IOException e;
        Throwable th;
        Throwable th2;
        ObjectOutputStream objectOutputStream = null;
        if (this.extraParam == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream2;
        try {
            objectOutputStream2 = new ObjectOutputStream(byteArrayOutputStream);
            try {
                objectOutputStream2.writeObject(this.extraParam);
                try {
                    byteArrayOutputStream.close();
                    objectOutputStream2.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (IOException e3) {
                ///e2 = e3;
                try {
                   /// e2.printStackTrace();
                    try {
                        byteArrayOutputStream.close();
                        objectOutputStream2.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                    return byteArrayOutputStream.toByteArray();
                } catch (Throwable th3) {
                    th = th3;
                    objectOutputStream = objectOutputStream2;
                    th2 = th;
                    try {
                        byteArrayOutputStream.close();
                        objectOutputStream.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                    throw th2;
                }
            } catch (Throwable th4) {
                objectOutputStream = objectOutputStream2;
                th2 = th4;
                byteArrayOutputStream.close();
                objectOutputStream.close();
                throw th2;
            }
        } catch (IOException e4) {
           /// e222 = e4;
            objectOutputStream2 = null;
           /// e222.printStackTrace();
            try{
                byteArrayOutputStream.close();
                objectOutputStream2.close();
            }catch (IOException e1){

            }

            return byteArrayOutputStream.toByteArray();
        } catch (Throwable th5) {
           // /th4 = th5;
           /// th2 = th4;
            try{
                byteArrayOutputStream.close();
                objectOutputStream.close();
            }catch (IOException e1){

            }

           /// throw th2;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public String getContent() {
        return this.content;
    }

    public HashMap<String, String> getExtraParam() {
        return this.extraParam;
    }

    public String getFromPkg() {
        return this.fromPkg;
    }

    public byte[] getIconImg() {
        return this.iconImg;
    }

    public String getIntentAction() {
        return this.intentAction;
    }

    public IntentType getIntentType() {
        return this.intentType;
    }

    public String getMessageID() {
        return this.messageID;
    }

    public MessageUIMode getMode() {
        return this.mode;
    }

    public String getNotifyContent() {
        return this.notifyContent;
    }

    public int getNotifyTime() {
        return this.notifyTime;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public byte[] getPoserImg() {
        return this.poserImg;
    }

    public String getPushID() {
        return this.pushID;
    }

    public String getSaveTime() {
        return this.saveTime;
    }

    public String getTitle() {
        return this.title;
    }

    public String getToCls() {
        return this.toCls;
    }

    public String getToPkg() {
        return this.toPkg;
    }

    public String getValidTime() {
        return this.validTime;
    }

    public boolean isDeleteAfterClick() {
        return this.isDeleteAfterClick;
    }

    public boolean isNotify() {
        return this.isNotify;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public void setDeleteAfterClick(boolean z) {
        this.isDeleteAfterClick = z;
    }

    public void setExtraParam(HashMap<String, String> hashMap) {
        this.extraParam = hashMap;
    }

    public void setFromPkg(String str) {
        this.fromPkg = str;
    }

    public void setIconImg(Bitmap bitmap) {
        if (bitmap != null && bitmap.getHeight() != 0 && bitmap.getWidth() != 0) {
            this.iconImg = bitmapToByte(bitmap);
        }
    }

    public void setIconImg(byte[] bArr) {
        this.iconImg = bArr;
    }

    public void setIntentAction(String str) {
        this.intentAction = str;
    }

    public void setIntentType(IntentType intentType) {
        this.intentType = intentType;
    }

    public void setMessageID(String str) {
        this.messageID = str;
    }

    public void setMode(MessageUIMode messageUIMode) {
        this.mode = messageUIMode;
    }

    public void setNotify(boolean z) {
        this.isNotify = z;
    }

    public void setNotifyContent(String str) {
        this.notifyContent = str;
    }

    public void setNotifyTime(int i) {
        this.notifyTime = i;
    }

    public void setPkgName(String str) {
        this.pkgName = str;
    }

    public void setPoserImg(Bitmap bitmap) {
        if (bitmap != null && bitmap.getHeight() != 0 && bitmap.getWidth() != 0) {
            this.poserImg = bitmapToByte(bitmap);
        }
    }

    public void setPoserImg(byte[] bArr) {
        this.poserImg = bArr;
    }

    public void setPushID(String str) {
        this.pushID = str;
    }

    public void setSaveTime(String str) {
        this.saveTime = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setToCls(String str) {
        this.toCls = str;
    }

    public void setToPkg(String str) {
        this.toPkg = str;
    }

    public void setValidTime(String str) {
        this.validTime = str;
    }
}
