package com.horion.tv.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.common.DtvManager;
import com.mstar.android.tvapi.dtv.vo.EpgEventInfo;

public class Tools {
	private static int offsetTimeInMs;
	public static void toastShow(int resId, Context context) {
		Toast toast = new Toast(context);
		TextView MsgShow = new TextView(context);
		toast.setDuration(Toast.LENGTH_LONG);
		MsgShow.setTextColor(Color.RED);
		MsgShow.setTextSize(25);
		MsgShow.setText(resId);
		toast.setView(MsgShow);
		toast.show();
	}

	private static final String MSTAR_PRODUCT_CHARACTERISTICS = "mstar.product.characteristics";
	private static final String MSTAR_PRODUCT_STB = "stb";
	private static String mProduct = null;

	public static boolean isBox() {
		if (mProduct == null) {
			Class<?> systemProperties = null;
			Method method = null;
			try {
				systemProperties = Class.forName("android.os.SystemProperties");
				method = systemProperties.getMethod("get", String.class,
						String.class);
				mProduct = (String) method.invoke(null,
						MSTAR_PRODUCT_CHARACTERISTICS, "");
			} catch (Exception e) {
				return false;
			}
		}
		// Log.d("Tools", "mstar.product.characteristics is " + mProduct);
		if (MSTAR_PRODUCT_STB.equals(mProduct)) {
			return true;
		} else {
			return false;
		}
	}
	public static ArrayList<EpgEventInfo> getEpgEventInfos(ProgramInfo programInfo){
		ArrayList<ProgramInfo> mPrograms = new ArrayList<ProgramInfo>();

		ProgramInfo specificProgInfo = new ProgramInfo();
		int totalProgramCount = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
		for (int i = 0; i < totalProgramCount; i++) {
			specificProgInfo = TvChannelManager.getInstance().getProgramInfoByIndex(i);

			if (specificProgInfo == null) {
				continue;
			}

			if ((specificProgInfo.isDelete == true)
					|| (specificProgInfo.isVisible == false)) {
				continue;
			}

			mPrograms.add(specificProgInfo);
		}
		for (int i = 0; i < mPrograms.size(); i++) {
			ProgramInfo tmpProgInfo = mPrograms.get(i);
			if ((programInfo.serviceType == tmpProgInfo.serviceType)
					&& (programInfo.number == tmpProgInfo.number)) {
				return constructEventInfoList(programInfo);
			}
		}
		return null;
	}

	private static ArrayList<EpgEventInfo> constructEventInfoList(ProgramInfo  specificProgInfo) {
		ArrayList<EpgEventInfo> eventInfoList = new ArrayList<EpgEventInfo>();
		Time nextEventBaseTime = null ;
		if (nextEventBaseTime == null) {
			nextEventBaseTime = new Time();
			nextEventBaseTime.setToNow();
			nextEventBaseTime.set(nextEventBaseTime.toMillis(true));
		}

		try {
			eventInfoList.clear();
			eventInfoList = DtvManager.getEpgManager().getEventInfo(
					(short) specificProgInfo.serviceType, specificProgInfo.number,
					nextEventBaseTime, Short.MAX_VALUE);
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventInfoList;
	}


	public static String getDate(EpgEventInfo mEpgInfo){
		Time curTime = new Time();
		curTime.setToNow();
		curTime.toMillis(true);
		try {
			offsetTimeInMs = DtvManager.getEpgManager().getEpgEventOffsetTime(curTime, true) * 1000;
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis((long) mEpgInfo.startTime * 1000 - offsetTimeInMs);
		CharSequence startTime = DateFormat.format("kk:mm", calendar);

		calendar.setTimeInMillis((long) mEpgInfo.endTime * 1000 - offsetTimeInMs);
		CharSequence endTime = DateFormat.format("kk:mm", calendar);

		String timeInfoStr =startTime.toString();
		return timeInfoStr;
	}

}
