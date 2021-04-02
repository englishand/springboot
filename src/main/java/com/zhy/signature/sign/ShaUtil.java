package com.zhy.signature.sign;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * 消息摘要
 * sha256 的信息摘要算法
 */
@Slf4j
public class ShaUtil {

    /**
     * 算法常量：sha256
     */
    private static final String ALGORITHM_SHA256="SHA-256";

    /**
     * MessageDigest类为应用程序提供信息摘要算法的功能，如 md5或sha算法。信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
     * MessageDigest对象开始初始化。该对象通过使用update()方法处理数据。任何时候都可以调用reset()方法重置摘要。
     * 一旦所需要的更新的数据都已经更新了，调用digest()方法，完成哈希计算。对于给定数量的更新数据，digest方法只能被调用一次。
     * 在调用digest方法之后，MessageDigest对象被重新重置其初始状态
     * @param message
     * @return
     */
    public static byte[] sha256(byte[] message){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ALGORITHM_SHA256);
            md.reset();
            md.update(message);
            byte[] result = md.digest();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed:sha256 byte[] to byte[]",e);
        }
        return null;
    }

}
