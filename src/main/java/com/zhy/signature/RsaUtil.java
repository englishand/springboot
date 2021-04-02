package com.zhy.signature;

import lombok.extern.slf4j.Slf4j;

import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;

/**
 * RSA签名 非对称、公钥加密，签名验签，基于数学函数
 * 公钥(n,e)加密: m^e=c(mod n),m明文，c密文
 * 私钥(n,d)解密：c^d=m(mod n),m明文，c密文
 */
@Slf4j
public class RsaUtil {

    /**
     * 签名算法名称
     */
    private static final String SIGN_ALGORITHM_SHA256RSA = "SHA256withRSA";

    /**
     * RSA Sha256摘要 ：私钥签名
     * @param privateKey 私钥
     * @param data  消息
     * @return  签名
     */
    public static byte[] signWithSha256(RSAPrivateKey privateKey,byte[] data){
        byte[] result = null;
        Signature st;
        try {
            st = Signature.getInstance(SIGN_ALGORITHM_SHA256RSA);
            st.initSign(privateKey);
            st.update(data);
            result = st.sign();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail:RSA with sha256 sign",e);
        }
        return result;
    }

    /**
     *  Rsa Sha256摘要    公钥验签
     * @param publicKey     公钥
     * @param date      消息
     * @param sign      签名
     * @return  验签结果
     */
    public static boolean verifyWithSha256(PublicKey publicKey,byte[] date,byte[] sign){
        boolean correct  = false;
        try {
            Signature st = Signature.getInstance(SIGN_ALGORITHM_SHA256RSA);
            st.initVerify(publicKey);
            st.update(date);
            correct = st.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail: RSA with sha256 verify",e);
        }
        return correct;
    }
}
