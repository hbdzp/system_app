package com.ktc.panasonichome.protocol;


import com.ktc.panasonichome.utils.JSONUtils;

public class ActionParams {
    public String cmd;
    public String cmd_param;

    public static ActionParams fromJosnString(String jsonstr) {
        try {
            return (ActionParams) JSONUtils.getInstance().parseObject(jsonstr, ActionParams.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValidate(ActionParams actionparam) {
        return (actionparam == null || actionparam.cmd == null || actionparam.cmd_param == null
                || actionparam.cmd.length() <= 0 || actionparam.cmd_param.length() <= 0) ? false
                : true;
    }
}
