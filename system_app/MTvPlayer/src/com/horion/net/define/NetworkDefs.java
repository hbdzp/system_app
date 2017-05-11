package com.horion.net.define;

public class NetworkDefs {
    public static final String KEY_NET_CURRENT_CONNECT_DEVICE = "device";
    public static final String KEY_NET_STATE_FROM = "from";
    public static final String KEY_NET_STATE_INTENT = "netState";
    public static final String KEY_NET_STATE_TARGET = "target";

    public enum DevConnectStatus {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED,
        UNKNOWN
    }

    public enum DevEnableState {
        DISABLING,
        DISABLED,
        ENABLING,
        ENABLED,
        UNKNOWN
    }

    public enum EthEvent {
        EVENT_ETH_CONNECT_SUCCEEDED,
        EVENT_ETH_CONNECT_FAILD,
        EVENT_ETH_CABLE_CONNECTED,
        EVENT_ETH_CABLE_DISCONNECTED,
        EVENT_ETH_UNKNOW,
        EVENT_ETH_CONNECT_INIT_FAIL,
        EVENT_ETH_CONNECT_FAILD_BY_CABLE_NOT_CONNECT,
        EVENT_ETH_CONNECT_FAILD_BY_TIMEOUT
    }

    public enum HotspotState {
        AP_STATE_DISABLING,
        AP_STATE_DISABLED,
        AP_STATE_ENABLING,
        AP_STATE_ENABLED,
        AP_STATE_FAILED
    }

    public enum NetworkDevices {
        ETHERNET,
        WIFI,
        PPPOE,
        UNKNOW
    }

    public enum NetworkState {
        UNKNOW_STATE,
        DISCONNECT,
        DISCONNECTING,
        CONNECTING,
        CONNECT_LAN,
        CONNECT_WAN
    }

    public enum KtcIpAssignment {
        STATIC,
        DHCP,
        STATIC_1000M,
        STATIC_100M,
        DHCP_1000M,
        DHCP_100M,
        PPPOE,
        UNASSIGNMENT
    }

    public enum KtcSupplicantState {
        DISCONNECTED,
        INTERFACE_DISABLED,
        INACTIVE,
        SCANNING,
        AUTHENTICATING,
        ASSOCIATING,
        ASSOCIATED,
        FOUR_WAY_HANDSHAKE,
        GROUP_HANDSHAKE,
        COMPLETED,
        DORMANT,
        UNINITIALIZED,
        INVALID,
        PWD_ERROR
    }

    public enum WifiCapabilities {
        WPA_PSK,
        WEP,
        NONE,
        WPA_EAP
    }

    public enum WifiEvent {
        EVENT_WIFI_CONNECT_SUCCEEDED,
        EVENT_WIFI_CONNECT_CONNECTING,
        EVENT_WIFI_CONNECT_DISCONNECTED,
        EVENT_WIFI_CONNECT_DISCONNECTING,
        EVENT_WIFI_CONNECT_FAILD,
        EVENT_WIFI_CONNECT_UNKNOW,
        EVENT_WIFI_RSSI_CHANGED,
        EVENT_WIFI_SCAN_FAIL
    }
}
