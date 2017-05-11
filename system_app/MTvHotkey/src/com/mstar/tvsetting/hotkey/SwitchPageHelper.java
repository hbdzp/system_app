//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tvsetting.hotkey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.InputDevice;
import android.view.KeyEvent;

import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.tvsetting.R;

public class SwitchPageHelper {
	 

	public static boolean goToSourceInfo(Activity from, int keyCodeToTrans) {
		switch (keyCodeToTrans) {
		case KeyEvent.KEYCODE_INFO:
			if(TvCommonManager.getInstance()
					.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV){
				Intent intent = new Intent(
						"com.mstar.tv.tvplayer.ui.intent.action.SOURCE_INFO");
				from.startActivity(intent);
				from.overridePendingTransition(android.R.anim.fade_in,0);
				return true;
			}else{
				Intent intent = new Intent(
						"com.mstar.tvsetting.hotkey.intent.action.SourceInFoActivity");
				from.startActivity(intent);
				from.overridePendingTransition(R.anim.slide_in_right,0);	
				return true;
			}
		}
		return false;
	}

	public static boolean goToOptionMenuPage(Activity from, int keyCodeToTrans) {
		switch (keyCodeToTrans) {
		case KeyEvent.KEYCODE_MENU:
			Intent intent = new Intent(
					"com.panasonic.ui.intent.action.MainActivity");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			from.startActivity(intent);
			from.overridePendingTransition(R.anim.slide_in_left,0);
			return true;
		}
		return false;
	}

	public static boolean goToProgrameListInfo(Activity from, int keyCodeToTrans) {
		Intent intent;
		if (specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_ATV)
				|| specificSourceIsInUse(from,
						TvCommonManager.INPUT_SOURCE_DTV)) {
			switch (keyCodeToTrans) {
			case MKeyEvent.KEYCODE_LIST:
				intent = new Intent("com.mstar.tvsetting.ui.intent.action.ProgramListViewActivity");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				from.startActivity(intent);
				from.overridePendingTransition(R.anim.slide_in_right,0);
				return true;
			}
		}
		return false;
	}

	public static boolean goToFavorateListInfo(Activity from, int keyCodeToTrans) {
		switch (keyCodeToTrans) {/*
								 * case KeyEvent.KEYCODE_F:// favorite list case
								 * KeyEvent.KEYCODE_MSTAR_SUBCODE:// favorite
								 * info if (specificSourceIsInUse(from,
								 * EnumInputSource.E_INPUT_SOURCE_DTV) ||
								 * specificSourceIsInUse(from,
								 * EnumInputSource.E_INPUT_SOURCE_ATV)) {
								 * //Intent intent = new Intent(from,
								 * ChannelListActivity.class); Intent intent =
								 * new Intent(
								 * "com.philips.ui.intent.action.AdjustMenuActivity"
								 * ); intent.putExtra("ListId", 1);
								 * from.startActivity(intent); return true; //}
								 * //break;
								 */
		}
		return false;
	}

	public static boolean specificSourceIsInUse(Activity from, int source) {
		int currInputSource = TvCommonManager.getInstance()
				.getCurrentTvInputSource();
		if (currInputSource != source) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean goToPhilipsPicFormatDialog(Activity from, int keyCode) {
		int inputSrc = TvCommonManager.getInstance().getCurrentTvInputSource();
		if (TvCommonManager.getInstance().isSignalStable(inputSrc) == false)
			return false;
		switch (keyCode) {
		case MKeyEvent.KEYCODE_PICTURE_MODE:
			Intent intent = new Intent(
					"com.philips.ui.intent.action.AdjustDialogActivity");
			intent.putExtra("actionflag", 2);
			from.startActivity(intent);
			break;

		default:
			break;
		}
		return false;
	}

	public static boolean goToEpgPage(Activity from, int keyCodeToTrans) {
		switch (keyCodeToTrans) {
		case MKeyEvent.KEYCODE_EPG:
			if (specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)) {
				Intent intent = new Intent("cn.ktc.android.intent.action.epg");
				from.startActivity(intent);
				return true;
			}
			break;
		}
		return false;
	}
	public static boolean goToKeyPad_Volumn_adjust(Activity from, int keyCodeToTrans,KeyEvent event) {
		switch (keyCodeToTrans) {		
		case KeyEvent.KEYCODE_DPAD_LEFT:
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
            if (deviceName.equals("MStar Smart TV IR Receiver")
                    || deviceName.equals("MStar Smart TV Keypad")) {
                AudioManager audiomanager = (AudioManager) from.getSystemService(Context.AUDIO_SERVICE);
                /*
                 * Adjust the volume in on key down since it is more
                 * responsive to the user.
                 */
                if (audiomanager != null) {
                    int flags = AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_VIBRATE;
                    audiomanager.adjustVolume(
                    		keyCodeToTrans == KeyEvent.KEYCODE_DPAD_RIGHT ? AudioManager.ADJUST_RAISE
                                    : AudioManager.ADJUST_LOWER, flags);
                }
                return true;
            } 
            break;
            }
		return false;
	}
}
