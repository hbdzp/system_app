package com.ktc.panasonichome.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.ktc.panasonichome.SimpleHomeApplication;

/*
 * @描述	     和ui相关的一些工具方法
 *
 */
public class UIUtils {
	/**
	 * 得到上下文
	 */
	public static Context getContext() {
		return SimpleHomeApplication.getContext();
	}

	/**
	 * 得到Resource对象
	 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/**
	 * 得到String.xml中定义的字符信息
	 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/**
	 * 得到String.xml中定义的字符信息,带占位符
	 */
	public static String getString(int resId, Object... formatArgs) {
		return getResources().getString(resId, formatArgs);
	}

	/**
	 * 得到String.xml中定义的字符数组信息
	 */
	public static String[] getStrings(int resId) {
		return getResources().getStringArray(resId);
	}

	/**
	 * 得到color.xml中定义的颜色信息
	 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/**
	 * 得到主线程的线程id
	 */
	public static long getMainThreadId() {
		return SimpleHomeApplication.getMainThreadId();
	}

	/**
	 * 得到主线程的一个handler
	 */
	public static Handler getMainThreadHandler() {
		return SimpleHomeApplication.getHandler();
	}

	/**
	 * 安全的执行一个任务
	 */
	public static void postTaskSafely(Runnable task) {
		// 得到当前的线程
		long curThreadId = android.os.Process.myTid();
		// 得到主线程的线程id
		long mainThreadId = getMainThreadId();
		if (curThreadId == mainThreadId) {
			// 如果当前是在主线程-->直接执行
			task.run();
		} else {
			// 如果当前是在子线程-->通过消息机制,把任务发送到主线程执行
			getMainThreadHandler().post(task);
		}
	}

	/**
	 * 得到应用程序的包名
	 */
	public static String getPackageName() {
		return getContext().getPackageName();
	}

	/**
	 * dp-->px
	 */
	public static int dp2Px(int dp) {
		// 1.px/dp = density ==> px和dp倍数关系
		// 2.px/(ppi/160) = dp ==>ppi

		float density = getResources().getDisplayMetrics().density; // 1.5
		// int ppi = getResources().getDisplayMetrics().densityDpi;//160 240 320

		int px = (int) (dp * density + .5f);
		return px;
	}

	/**
	 * px-->dp
	 */
	public static int px2Dp(int px) {
		// 1.px/dp = density ==> px和dp倍数关系
		float density = getResources().getDisplayMetrics().density; // 1.5
		int dp = (int) (px / density + .5f);
		return dp;
	}
}
