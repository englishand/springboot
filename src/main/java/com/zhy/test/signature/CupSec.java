package com.zhy.test.signature;

import com.zhy.test.signature.sign.ShaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;

/**
 *  安全算法：加解密、签名验签、敏感信息及密钥加解密
 */
@Slf4j
public class CupSec {

    /**
     * RSA 私钥签名
     * 过程：对报文生成sha256摘要，再对摘要进行RSA签名，最后对生成的签名进行base64编码
     * @param privateKey 私钥
     * @param msg   待签名信息
     * @return
     */
    public static String rsaSignWithSha256(PrivateKey privateKey,byte[] msg) {
        String result;
        byte[] shaMsg = ShaUtil.sha256(msg);
        byte[] sign = RsaUtil.signWithSha256((RSAPrivateKey) privateKey,shaMsg);
        result = Base64.encodeBase64String(sign);
        log.info("base64:"+result);
        return result;
    }

    /**
     *  Rsa公钥验签
     *  过程说明：先对报文<root></root>标签的内容生成SHA256摘要，然后将base64编码后的签名进行解码，最后使用摘要和解码后的签名进行RSA验签
     * @param publicKey 验证公钥
     * @param msg   签名的信息
     * @param signB 待验证的签名
     * @return  验签结果：true(验签成功),false(验签失败)
     * @throws IOException
     */
    public static boolean rsaVerifyWithSha256(PublicKey publicKey,byte[] msg,String signB) throws IOException {
        byte[] shaMsg = ShaUtil.sha256(msg);
        byte[] signature = Base64.decodeBase64(signB);
        boolean result = RsaUtil.verifyWithSha256(publicKey,shaMsg,signature);
        return result;
    }
}
