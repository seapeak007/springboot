package com.lexue.utils;

import java.security.MessageDigest;

/**
 * Created by UI03 on 2017/6/12.
 */
public class SHA1Util {
    public static void main(String[] args) {
        String pwd = getSHA("一天天成长");
        System.out.println(pwd);
    }

    //生成MD5
    public static String getSHA(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");  // 创建一个md5算法对象
            byte[] messageByte = message.getBytes(CommonSet.CHAR_SET);
            byte[] md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static byte[] getSHA(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");  // 创建一个md5算法对象
            byte[] md5Bytes = md.digest(bytes);              // 获得MD5字节数组,16*8=128位
            return md5Bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    // 二进制转十六进制
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if(num < 0) {
                num += 256;
            }
            if(num < 16){
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
}
