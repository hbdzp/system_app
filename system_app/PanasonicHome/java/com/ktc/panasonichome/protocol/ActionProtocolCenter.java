package com.ktc.panasonichome.protocol;

import com.ktc.panasonichome.LauncherActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActionProtocolCenter {
    private static ActionProtocolCenter _protocolCenter;
    private Map<ACTIONPROTOCOL, IActionProtocol> _protocols = new HashMap();

    public enum ACTIONPROTOCOL {
        FOUL,
        STARTAPP,
        STARTWEBAPP,
        STARTPLAYER,
        SEARCH,
        ACCOUNT,
        MESSAGE,
        LOCALMEDIA,
        HISTORY,
        FAVOURITE,
        NETSETTING,
        LISTSOURCES,
        TVSETTING,
        QRCODE,
        SYSTEMUPGRADE,
        WEBVIEW,
        GETMOVIEHISTORYLIST
    }

    public interface IActionProtocol {
        boolean doIt(String str);

        boolean isLegal(String str);

        ACTIONPROTOCOL protocolName();
    }

    public interface IActionProtocol_ex extends IActionProtocol {
        ArrayList<String> doItWithResult(String str);
    }

    private ActionProtocolCenter(LauncherActivity activity) {
//        add(new Protocol_StartApp(activity));
//        add(new Protocol_StartPlayer(activity));
//        add(new Protocol_GetMovieHistroy(activity));
//        add(new Protocol_StartWebApp(activity));
//        add(new Protocol_StartWebView(activity));
    }

    private void add(IActionProtocol protocol) {
        this._protocols.put(protocol.protocolName(), protocol);
    }

    public static ActionProtocolCenter getInstance(LauncherActivity activity) {
        if (_protocolCenter == null) {
            _protocolCenter = new ActionProtocolCenter(activity);
        }
        return _protocolCenter;
    }

    public boolean doIt(String protocol) {
        ActionParams actionparams = ActionParams.fromJosnString(protocol);
        if (!(actionparams == null || actionparams.cmd == null)) {
            ACTIONPROTOCOL[] values = ACTIONPROTOCOL.values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                ACTIONPROTOCOL action = values[i];
                if (!action.toString().equals(actionparams.cmd.toString())) {
                    i++;
                } else if (this._protocols.containsKey(action)) {
                    IActionProtocol mProtocol = (IActionProtocol) this._protocols.get(action);
                    if (mProtocol.isLegal(actionparams.cmd_param)) {
                        return mProtocol.doIt(actionparams.cmd_param);
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<String> doItWothResult(String protocol) {
        ActionParams actionparams = ActionParams.fromJosnString(protocol);
        if (!(actionparams == null || actionparams.cmd == null)) {
            ACTIONPROTOCOL[] values = ACTIONPROTOCOL.values();
            int i = 0;
            int length = values.length;
            while (i < length) {
                ACTIONPROTOCOL action = values[i];
                if (!action.toString().equals(actionparams.cmd.toString())) {
                    i++;
                } else if (this._protocols.containsKey(action)) {
                    IActionProtocol mProtocol = (IActionProtocol) this._protocols.get(action);
                    if (mProtocol.isLegal(actionparams.cmd_param)) {
                        return ((IActionProtocol_ex) mProtocol).doItWithResult(actionparams.cmd_param);
                    }
                }
            }
        }
        return new ArrayList();
    }
}
