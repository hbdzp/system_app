package com.mstar.tvsetting.hotkey;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

	/**
	 * 从*.prop中根据key获取对应的值
	 * 
	 * @param filePath *.prop文件路径
	 * @param key
	 * @return
	 */
	public static String readPropValue(String filePath, String key) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
