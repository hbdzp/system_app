package com.ktc.panasonichome.model;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5DigestUtil {
    public static String effective_md5(String string) throws RuntimeException {
        try {
            byte[] hash = MessageDigest.getInstance(MessageDigestAlgorithms.MD5).digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 255) < 16) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 255));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e2);
        }
    }

    public static String getCurrentFileMd5(File cachedFile) {
        if (cachedFile.length() < 10) {
            return null;
        }
        String cachedMd5 = null;
        String newFileString = getFileString(cachedFile);
        if (newFileString == null || newFileString.length() <= 0) {
            return null;
        }
        try {
            cachedMd5 = effective_md5(newFileString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cachedMd5;
    }

    private static String getFileString(File cachedFile) {
        if (cachedFile == null || !cachedFile.exists()) {
            return null;
        }
        int len = 0;
        StringBuffer str = new StringBuffer("");
        try {
            FileInputStream is = new FileInputStream(cachedFile);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                if (len != 0) {
                    str.append("\r\n" + line);
                } else {
                    str.append(line);
                }
                len++;
            }
            in.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
