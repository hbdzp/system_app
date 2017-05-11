package com.ktc.framework.ktcsdk.message;

import android.net.Uri;
import android.provider.BaseColumns;

public class MessageInfo {
    public static final int ADDMSGERROROTHER = -2;
    public static final int ADDMSGERRORPUSHID = -3;
    public static final int ADDMSGERRORTIME = -4;
    public static final int ADDMSGSUCCESS = 0;
    public static final String Auth = "com.horion.messag.ProviderAuth";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.provider.msg";
    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.provider.msg";
    public static final String DBNAME = "message.db";
    public static final int MSGCOUNTCODE = 3;
    public static final Uri MSGCOUNTURI = Uri.parse("content://com.horion.messag.ProviderAuth/countmessage");
    public static final int MSGDELETECODE = 2;
    public static final Uri MSGDELETEURI = Uri.parse("content://com.horion.messag.ProviderAuth/deletemessage");
    public static final int MSGINSERTCODE = 1;
    public static final Uri MSGINSERTURI = Uri.parse("content://com.horion.messag.ProviderAuth/insertmessage");
    public static final int MSGQUERYCODE = 4;
    public static final Uri MSGQUERYURI = Uri.parse("content://com.horion.messag.ProviderAuth/querymessage");
    public static final int MSGUPDATECODE = 5;
    public static final Uri MSGUPDATEURI = Uri.parse("content://com.horion.messag.ProviderAuth/updatemessage");

    public static final class MessageInfoData implements BaseColumns {
        public static final String CONTENT = "content";
        public static final String EXTRAPARAM = "extra_param";
        public static final String FROMPKG = "from_pkg";
        public static final String ICONIMG = "icon_img";
        public static final String INTENTACTION = "intent_action";
        public static final String INTENTTYPE = "intent_type";
        public static final String ISDELAFTERCLICK = "is_del_after_click";
        public static final String ISNOTIFY = "is_notify";
        public static final String ISREAD = "is_read";
        public static final String LAYOUTTYPE = "layout_type";
        public static final String MESSAGEID = "msg_id";
        public static final String NOTIFYCONTENT = "notify_content";
        public static final String NOTIFYTIME = "notify_time";
        public static final String PKGNAME = "pkg_name";
        public static final String POSTERIMG = "poster_img";
        public static final String PUSHID = "push_id";
        public static final String SAVETIME = "save_time";
        public static final String SYSTEMTIME = "system_time";
        public static final String TABLE = "messageinfo";
        public static final String TITLE = "title";
        public static final String TOCLASS = "to_cls";
        public static final String TOPKG = "to_pkg";
        public static final String VALIDTIME = "valid_time";
    }
}
