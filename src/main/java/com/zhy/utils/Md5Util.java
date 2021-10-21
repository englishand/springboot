package com.zhy.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    public final static String getMd5(String string){
        MessageDigest md = null;//加密实例
        try {
            md = MessageDigest.getInstance("MD5");//加密方式
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        char[] chars = string.toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i=0;i<bytes.length;i++){
            bytes[i] = (byte) chars[i];
        }
        byte[] md5Bytes = md.digest(bytes);
        StringBuffer hexValue = new StringBuffer();
        for (int i=0;i<md5Bytes.length;i++){
            int val = ((int)md5Bytes[i]) & 0xff;
            if (val<16){
                hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
        }
        return hexValue.toString();
    }

    //可逆的加密算法
    public static String Kl(String string){
        char[] chars = string.toCharArray();
        for (int i=0;i<chars.length;i++){
            chars[i] = (char) (chars[i]^'t');
        }
        String s = new String(chars);
        return s;
    }
    //解密
    public static String Jm(String string){
        char[] chars = string.toCharArray();
        for (int i=0;i<chars.length;i++){
            chars[i] = (char) (chars[i]^'t');
        }
        String k = new String(chars);
        return k;
    }

    public static void main(String[] args) {
        String s = new String("你好");
        System.out.println("MD5后："+getMd5(s));
        System.out.println("MD5后再加密："+Kl(getMd5(s)));
        System.out.println("解密为MD5后的："+Jm(Kl(getMd5(s))));
    }
}
