package com.adcc.util;


import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by zyy on 2016/7/27.
 */
public class MD5 {

    /**
     * 字符串转MD5
     * @param str
     * @return
     */
    public static String getMongoMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes();
            md.update(bytes, 0, bytes.length);
            BigInteger bigInt = new BigInteger(1, md.digest());
            return bigInt.toString(16);
        } catch (Exception ex) {
            return  null;
        }
    }
}
