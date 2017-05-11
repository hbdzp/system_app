package com.ktc.framework.ktcsdk.message;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

///import com.ktc.framework.skysdk.logger.SkyLogger;
import com.ktc.framework.ktcsdk.message.MessageInfo.MessageInfoData;

import java.util.ArrayList;

public class MessageManager {

    public static class MessageStatus {
        public boolean isRead;
        public String msgID;

        public MessageStatus(String str, boolean z) {
            this.msgID = str;
            this.isRead = z;
        }
    }

    public static int addMessage(Context context, MessagePacket messagePacket) {
        return addMsg(context, 0, messagePacket);
    }

    private static int addMsg(Context context, int i, MessagePacket messagePacket) {
        int i2 = 1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MessageInfoData.PUSHID, messagePacket.getPushID());
        contentValues.put(MessageInfoData.MESSAGEID, messagePacket.getMessageID());
        contentValues.put(MessageInfoData.FROMPKG, messagePacket.getFromPkg());
        contentValues.put(MessageInfoData.PKGNAME, messagePacket.getPkgName());
        contentValues.put(MessageInfoData.ISREAD, Integer.valueOf(i));
        contentValues.put(MessageInfoData.LAYOUTTYPE, Integer.valueOf(messagePacket.getMode().ordinal()));
        contentValues.put(MessageInfoData.TITLE, messagePacket.getTitle());
        contentValues.put(MessageInfoData.CONTENT, messagePacket.getContent());
        contentValues.put(MessageInfoData.POSTERIMG, messagePacket.getPoserImg());
        contentValues.put(MessageInfoData.ICONIMG, messagePacket.getIconImg());
        contentValues.put(MessageInfoData.INTENTTYPE, Integer.valueOf(messagePacket.getIntentType().ordinal()));
        contentValues.put(MessageInfoData.INTENTACTION, messagePacket.getIntentAction());
        contentValues.put(MessageInfoData.EXTRAPARAM, messagePacket.ExtraSerialize());
        contentValues.put(MessageInfoData.ISDELAFTERCLICK, Integer.valueOf(messagePacket.isDeleteAfterClick() ? 1 : 0));
        contentValues.put(MessageInfoData.VALIDTIME, messagePacket.getValidTime());
        contentValues.put(MessageInfoData.SAVETIME, messagePacket.getSaveTime());
        if (!messagePacket.isNotify()) {
            i2 = 0;
        }
        contentValues.put(MessageInfoData.ISNOTIFY, Integer.valueOf(i2));
        contentValues.put(MessageInfoData.NOTIFYCONTENT, messagePacket.getNotifyContent());
        contentValues.put(MessageInfoData.NOTIFYTIME, Integer.valueOf(messagePacket.getNotifyTime()));
        contentValues.put(MessageInfoData.TOCLASS, messagePacket.getToCls());
        contentValues.put(MessageInfoData.TOPKG, messagePacket.getToPkg());
        ///SkyLogger.i("msg_tag", "uri===" + MessageInfo.MSGINSERTURI.toString());
        if (context.getContentResolver().getType(MessageInfo.MSGINSERTURI) == null) {
            return -2;
        }
        Uri insert = context.getContentResolver().insert(MessageInfo.MSGINSERTURI, contentValues);
        if (insert == null) {
           /// SkyLogger.i("msg_tag", "insrt msg error");
            return -2;
        }
        long parseId = ContentUris.parseId(insert);
      ///  SkyLogger.i("msg_tag", "insrt id ===" + parseId);
        return parseId < 0 ? (int) parseId : 0;
    }

    public static boolean deleteMessageAll(Context context, String str, String str2) {
        return context.getContentResolver().delete(Uri.withAppendedPath(MessageInfo.MSGDELETEURI, new StringBuilder().append(str).append("/").append(str2).toString()), null, new String[]{str2}) < 0;
    }

    public static boolean deleteMessageByID(Context context, String str, String str2, String str3) {
        return context.getContentResolver().delete(Uri.withAppendedPath(MessageInfo.MSGDELETEURI, new StringBuilder().append(str).append("/").append(str2).toString()), "where", new String[]{str2, str3}) < 0;
    }

    public static int getAllUnreadMessages(Context context) {
        Cursor query = context.getContentResolver().query(MessageInfo.MSGCOUNTURI, null, null, null, null);
        if (query == null) {
            return 0;
        }
        int count = query.getCount();
        query.close();
        return count;
    }

    public static ArrayList<MessageStatus> getMessagesStatus(Context context, String str, String str2) {
        ArrayList<MessageStatus> arrayList = null;
        Cursor query = context.getContentResolver().query(Uri.withAppendedPath(MessageInfo.MSGQUERYURI, str + "/" + str2), null, null, new String[]{str}, null);
        if (query != null) {
            arrayList = new ArrayList();
            while (query.moveToNext()) {
                arrayList.add(new MessageStatus(query.getString(0), query.getInt(1) == 0));
            }
        }
        return arrayList;
    }
}
