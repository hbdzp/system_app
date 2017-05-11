//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
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

package com.horion.tv.framework;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.horion.tv.R;
import com.horion.tv.framework.ui.dialog.SkyTVDialog;
import com.ktc.ui.api.SkyDialogView;
import com.ktc.ui.api.SkyDialogView.OnDialogOnKeyListener;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.vo.EnumServiceType;
import com.mstar.android.tvapi.common.vo.ProgramInfo;

public class DeleteChannelActivity extends MstarUIActivity{
	private SkyTVDialog skyTVDialog;
    private FrameLayout rootView;
	private String tip1;
	private String tip2;
	private String btn1;
	private String btn2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        rootView = (FrameLayout)findViewById(R.id.delete_main);
		skyTVDialog = new SkyTVDialog();
        skyTVDialog.init(rootView,this);
		int currInput = TvCommonManager.getInstance().getCurrentTvInputSource();
		ProgramInfo cpi = TvChannelManager.getInstance().getCurrentProgramInfo();
		if (currInput == TvCommonManager.INPUT_SOURCE_ATV){
			tip1 = getString(R.string.deletetitle) + "模拟电视" + (cpi.number + 1) + getString(R.string.delete_brackets);
		}else if (currInput == TvCommonManager.INPUT_SOURCE_DTV){
			tip1 = getString(R.string.deletetitle) + cpi.serviceName + getString(R.string.delete_brackets);
		}

		tip2 = getString(R.string.deletesubtitle);
		btn1 = getString(R.string.ok);
		btn2 = getString(R.string.cancel);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("Maxs34","DeleteChannelActivity:onResume");
		skyTVDialog.showTvDialog(tip1, tip2, btn1, btn2, new OnDialogOnKeyListener() {
			@Override
			public boolean onDialogOnKeyEvent(int keyCode, KeyEvent keyEvent) {
                Log.d("Maxs34","onDialogOnKeyEvent:keyCode = " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    finish();
                }
				return false;
			}

			@Override
			public void onFirstBtnOnClickEvent() {
				Log.d("Maxs34","--->onSecondBtnOnClickEvent = ");
				ProgramInfo curProgInfo = TvChannelManager.getInstance().getCurrentProgramInfo();
				TvChannelManager.getInstance().setProgramAttribute(
						TvChannelManager.PROGRAM_ATTRIBUTE_DELETE,
						curProgInfo.number, curProgInfo.serviceType,
						0x00, true);
				if (EnumServiceType.E_SERVICETYPE_ATV.ordinal() == curProgInfo.serviceType) {
					TvChannelManager.getInstance().changeToFirstService(
							TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
							TvChannelManager.FIRST_SERVICE_DEFAULT);
				} else if (EnumServiceType.E_SERVICETYPE_DTV.ordinal() == curProgInfo.serviceType) {
					TvChannelManager.getInstance().changeToFirstService(
							TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
							TvChannelManager.FIRST_SERVICE_DEFAULT);
				}
                finish();
				Log.d("Maxs34","--->onFirstBtnOnClickEvent = ");
			}

			@Override
			public void onSecondBtnOnClickEvent() {

                finish();
			}
		},null);
	}


	@Override
	protected void onStop() {
        finish();
		super.onStop();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {

		super.onRestart();
	}

	@Override
	public void onDestroy() {
        Log.d("Maxs34","DeleteChannelActivity:onDestory");
		super.onDestroy();
	}



}
