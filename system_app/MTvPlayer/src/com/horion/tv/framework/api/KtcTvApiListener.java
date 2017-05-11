package com.horion.tv.framework.api;

////import com.horion.system.define.KtcUartSerialCmdDefs;

public abstract class KtcTvApiListener {
    ////public static final byte[] NOT_HANDLED = new byte[]{KtcUartSerialCmdDefs.UART_SERIAL_CMD_ATV_CHANNEL_SELECT};
    ////KtcUartSerialCmdDefs.UART_SERIAL_CMD_ATV_CHANNEL_SELECT=48
    public static final byte[] NOT_HANDLED = new byte[]{48};
    public abstract String[] getHandleCmds();

    public abstract byte[] onHandler(String str, String str2, byte[] bArr);

    public boolean onResult(String str, String str2, byte[] bArr) {
        return false;
    }
}
