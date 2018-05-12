package com.xman.service.http.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yx on 2015/10/20.
 */
public class StreamUtil {

    public static byte[] readBytes(BufferedReader reader, String encoding, int contentLen) throws IOException {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            char[] message = new char[contentLen];
            while (readLen != contentLen) {
                readLengthThisTime = reader.read(message, readLen, contentLen - readLen);
                if (readLengthThisTime == -1) { // Should not happen.
                    break;
                }
                readLen += readLengthThisTime;
            }
            return new String(message).getBytes(encoding);
        }
        return new byte[]{};
    }

    public static final byte[] readBytes(InputStream is, int contentLen) throws IOException {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            while (readLen != contentLen) {
                readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                if (readLengthThisTime == -1) {// Should not happen.
                    break;
                }
                readLen += readLengthThisTime;
            }
            return message;
        }
        return new byte[]{};
    }
}
