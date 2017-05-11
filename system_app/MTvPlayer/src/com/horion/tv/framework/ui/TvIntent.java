//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>

package com.horion.tv.framework.ui;

public class TvIntent {
    public static final String MAINMENU = "com.mstar.tv.tvplayer.ui.intent.action.MAINMENU";
    public static final String ACTION_SOURCE_CHANGE = "com.mstar.tv.tvplayer.ui.intent.action.SOURCE_CHANGE";
    public static final String ROOT = "com.mstar.tv.tvplayer.ui.intent.action.ROOT";

    public static final String LOGO = "com.mstar.tv.tvplayer.ui.intent.action.LOGO";

    public static final String CHANNEL_LIST = "com.mstar.tv.tvplayer.ui.intent.action.CHANNEL_LIST";

    public static final String CHANNEL_CONTROL = "com.mstar.tv.tvplayer.ui.intent.action.CHANNEL_CONTROL";

    public static final String DELETE_CHANNEL = "com.mstar.tv.tvplayer.ui.intent.action.DELETE_CHANNEL";

    public static final String ACTION_CIPLUS_OP_REFRESH = "com.mstar.tv.tvplayer.ui.intent.action.CI_PLUS_OP_REFRESH";

    public static final String ACTION_CIPLUS_TUNER_UNAVAIABLE = "com.mstar.tv.tvplayer.ui.intent.action.CI_PLUS_TUNER_UNAVAIABLE";

    public static final String ACTION_EXIT_TV_APK = "com.mstar.android.intent.action.EXIT_TV_APK";

    public static final String ACTION_SIGNAL_LOCK = "com.mstar.android.intent.action.SIGNAL_LOCK";

    public static final String ACTION_SIGNAL_UNLOCK = "com.mstar.android.intent.action.SIGNAL_UNLOCK";

    public static final String ACTION_COMMON_VIDEO = "com.mstar.android.intent.action.COMMON_VIDEO";

    public static final String ACTION_REDRAW_NOSIGNAL = "com.mstar.android.intent.action.REDRAW_NOSIGNAL";

    public static final String ACTION_START_ROOTACTIVITY = "com.mstar.tv.tvplayer.ui.intent.action.RootActivity";

    public static final String ACTION_SOURCEINFO = "com.mstar.tv.tvplayer.ui.intent.action.SOURCE_INFO";

    public static final String ACTION_SOURCE_INFO_DISAPPEAR = "source_info_disappear";

    public static final String ACTION_AUTOTUNING_OPTION = "com.mstar.tv.tvplayer.ui.intent.action.AUTOTUNING_OPTION";

    public static final String ACTION_DVBSDTV_AUTOTUNING_OPTION = "com.mstar.tv.tvplayer.ui.intent.action.DVBSDTV_AUTOTUNING_OPTION";

    public static final String ACTION_CHANNEL_TUNING = "com.mstar.tv.tvplayer.ui.intent.action.CHANNEL_TUNING";

    public static final String ACTION_DTV_MANUAL_TUNING = "com.mstar.tv.tvplayer.ui.intent.action.DTV_MANUAL_TUNING";

    public static final String ACTION_NTSC_ATV_MANUAL_TUNING = "com.mstar.tv.tvplayer.ui.intent.action.NTSC_ATV_MANUAL_TUNING";

    public static final String ACTION_PAL_ATV_MANUAL_TUNING = "com.mstar.tv.tvplayer.ui.intent.action.PAL_ATV_MANUAL_TUNING";

    public static final String ACTION_VCHIP_ACTIVITY = "com.mstar.tv.tvplayer.ui.intent.action.VCHIP_ACTIVITY";

    public static final String ACTION_LNBSETTING = "com.mstar.tv.tvplayer.ui.intent.action.LnbSetting";

    public static final String ACTION_LNBOPTION = "com.mstar.tv.tvplayer.ui.intent.action.LnbOption";

    public static final String ACTION_LNBOPTION_EDITOR = "com.mstar.tv.tvplayer.ui.intent.action.LnbOptionEditor";

    public static final String ACTION_LNBMOTOR_EDITOR = "com.mstar.tv.tvplayer.ui.intent.action.LnbMotorEditor";

    public static final String ACTION_SETUP_WIZARD = "com.mstar.tv.tvplayer.ui.intent.action.SETUP_WIZARD";

    public static final String ACTION_EPG_ACTIVITY = "com.mstar.tv.tvplayer.ui.intent.action.EPG_ACTIVITY";

    public static final String ACTION_EPG_REMINDER = "com.mstar.tv.tvplayer.ui.intent.action.EPG_REMINDER";

    public static final String ACTION_PVR_ACTIVITY = "com.mstar.tv.tvplayer.ui.intent.action.PVR_ACTIVITY";

    public static final String ACTION_PVR_BROWSER = "com.mstar.tv.tvplayer.ui.intent.action.PVR_BROWSER";

    public static final String ACTION_PVR_ENTER_FULL_BROWSER = "com.mstar.tv.tvplayer.ui.intent.action.PVR_ENTER_FULL_BROWSER";

    public static final String ACTION_PVR_LEAVE_FULL_BROWSER = "com.mstar.tv.tvplayer.ui.intent.action.PVR_LEAVE_FULL_BROWSER";

    public static final String ACTION_SETUP_LOCATION_WIZARD = "com.mstar.tv.tvplayer.ui.intent.action.SETUP_LOCATION_WIZARD";

    public static final String ACTION_SETUP_LOCATION_WIZARD_DONE = "com.mstar.tv.tvplayer.ui.intent.action.SETUP_LOCATION_WIZARD_DONE";

    public static final String ACTION_TV_TIME_ZONE_CHANGE = "com.mstar.tv.tvplayer.ui.intent.action.TV_TIME_ZONE_CHANGE";

    public static final String ACTION_FORCE_REVEAL_PASSWORD_PROMPT = "com.mstar.tv.tvplayer.ui.intent.action.FORCE_REVEAL_PASSWORD_PROMPT";

    public static final String ACTION_INPUT_SOURCE_BLOCK = "com.mstar.tv.tvplayer.ui.intent.action.INPUT_SOURCE_LOCK";

    public static final String ACTION_EWS_ACTIVITY = "com.mstar.tv.tvplayer.ui.intent.action.EWS_ACTIVITY";

    public static final String ACTION_EWS_FINISH_ACTIVITY = "com.mstar.tv.tvplayer.ui.intent.action.EWS_FINISH_ACTIVITY";
}
