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

package com.mstar.tvsetting.hotkey;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideoDisplayFormat;

import com.mstar.tvsetting.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class ProgressActivity extends Activity {
    private static final String TAG = "ProgressActivity";

    private TvCommonManager tvCommonManager = null;

    private TvS3DManager tvS3DManager = null;

    private TvChannelManager tvChannelManager = null;

    private Intent targetIntent = null;

    private final int FINISH = 444;

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FINISH) {
                Log.i(TAG,
                        "+++++++++++++handleMessage msg.what == FINISH+++++++++++++++");
                ProgressActivity.this.finish();
            } else {
                try {
                    if (targetIntent != null)
                        startActivity(targetIntent);
                } catch (Exception e) {
                    Log.i(TAG,
                            "+++++++++++++handleMessage Exception+++++++++++++++");
                    e.printStackTrace();
                    // TODO:tip activity not found..
                }
                // RootActivity is transparent in the very first during when
                // displaying,
                // delay finishing this activity providers a black background
                handler.sendEmptyMessageDelayed(FINISH, 1500);
            }
        };
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.progress_layout);

        tvCommonManager = TvCommonManager.getInstance();
        tvS3DManager = TvS3DManager.getInstance();
        tvChannelManager = TvChannelManager.getInstance();
        final Intent paramIntent = getIntent();

        executePreviousTask(paramIntent);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);

        executePreviousTask(intent);
    }

    private void executePreviousTask(final Intent paramIntent) {
        if (paramIntent != null) {
            String taskTag = paramIntent.getStringExtra("task_tag");

            Log.i(TAG, "on new Intent taskTag:" + taskTag);

            if ("input_source_changed".equals(taskTag)) {

                targetIntent = new Intent(
                        "mstar.tvsetting.ui.intent.action.RootActivity");
                targetIntent.putExtra("task_tag", "input_source_changed");

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        tvS3DManager
                                .setDisplayFormatForUI(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE
                                        .ordinal());

                        int inputSource = paramIntent.getIntExtra("inputSource", 0);

                        Log.i(TAG, "inputSource:" + inputSource);

                        if (inputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                            tvCommonManager.setInputSource(inputSource);
                            int curChannelNumber = tvChannelManager
                                    .getCurrentChannelNumber();
                            if (curChannelNumber > 0xFF) {
                                curChannelNumber = 0;
                            }
                            tvChannelManager.setAtvChannel(curChannelNumber);
                        } else if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                            tvCommonManager.setInputSource(inputSource);
                            tvChannelManager.playDtvCurrentProgram();
                        } else {
                            tvCommonManager.setInputSource(inputSource);
                        }
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
