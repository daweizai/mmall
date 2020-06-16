package com.mmall.util;

import java.security.MessageDigest;

/**
 * MD5加密工具
 *
 * @Author daweizai
 * @Date 23:42 2020/5/22
 * @ClassName MD5Util
 * @Version 1.0
 **/
public class MD5Util {

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;

        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 加密时使用的密钥
     */
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 返回大写MD5
     *
     * @param origin      原数据
     * @param charsetname 字符集名称
     */
    private static String MD5Encode(String origin, String charsetname) {
        String resultStr = null;
        try {
            resultStr = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultStr = byteArrayToHexString(md.digest(resultStr.getBytes()));
            } else {
                resultStr = byteArrayToHexString(md.digest(resultStr.getBytes(charsetname)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr.toUpperCase();
    }

    public static String MD5EncodeUtf8(String origin) {
        origin = origin + PropertiesUtil.getProperty("password.salt", "");
        return MD5Encode(origin, "UTF-8");
    }

    public static void main(String[] args) {
        //D8F80B67499E434EA61ADAF6E6219BF2
        String s = MD5EncodeUtf8("123456");
        System.out.println(s);
    }

}
