package com.adcc.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class MD5Util {

    /**
     * 计算MD5值
     * @param buffer
     * @return
     */
    public static String getMD5(ByteBuffer buffer) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buffer);
        BigInteger bi = new BigInteger(1, md5.digest());
        return bi.toString(16);
    }

    /**
     * 计算MD5值
     * @param buffer
     * @return
     * @throws Exception
     */
    public static String getMD5(String buffer) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buffer.getBytes());
        BigInteger bi = new BigInteger(1, md5.digest());
        return bi.toString(16);
    }

    /**
     * 计算MD5值
     * @param buffer
     * @return
     * @throws Exception
     */
    public static String getMD5(byte[] buffer) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buffer);
        BigInteger bi = new BigInteger(1, md5.digest());
        return bi.toString(16);
    }

}
