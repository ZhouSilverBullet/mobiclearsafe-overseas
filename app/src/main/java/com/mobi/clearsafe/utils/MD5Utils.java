package com.mobi.clearsafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author : liangning
 * date : 2019-12-26  15:19
 */
public class MD5Utils {

    public static String getMD5(String text) {
        String md5s = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] b = text.getBytes();
            byte[] digest = md5.digest(b);
            char[] chars = new char[]{
                    '0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
            };
            StringBuffer sb = new StringBuffer();
            for (byte bb : digest) {
                sb.append(chars[(bb >> 4) & 15]);
                sb.append(chars[bb & 15]);
            }
            md5s = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5s;
    }

}
