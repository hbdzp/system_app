package com.ktc.panasonichome.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteToInputStream {
    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    public static final byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        while (true) {
            int rc = inStream.read(buff, 0, 100);
            if (rc <= 0) {
                break;
            }
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = null;
        try {
            in2b = swapStream.toByteArray();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return in2b;
    }
}
