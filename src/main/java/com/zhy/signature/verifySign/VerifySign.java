package com.zhy.signature.verifySign;

import com.zhy.signature.CupSec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;


@Slf4j
public class VerifySign {

    private static PublicKey publicKey;
    //从数据库或配置文件中获取
    private static String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArAAQnsm8VKAzAw6ts3Sd9R8VBSBZm4wg42Erv2ffTh4gn+NdL+xQDlCzUl+NrnO+dKipEZf/v+R0Loztc4grT00dgplGOoCSa7eNwUl7L/+UHjjGY5RBIderFgHRFlpEhZZvDfiJavlyzdp76QdY/J+/OOgadDJH4KTqCSX+uNRwL0eYGXIDa8IjQP9811Ss4tA9e18iawQK6NbrMqAoIxGwhQN8R9Xe+Up45KF+EtExb5Xq09ZyXUcFXx3g1AjsqPu9WLyGnN0Ey2GEpi1OplNatnmW/WBdNDLm2HBJpnHWNeJqt4nNxKu8EU4g7GJa6MvKdtLLR/vva+U68/iIjQIDAQAB ";

    public static boolean verifySign(String rootStr, String signValue,String keyFile,String storepass,String keypass,String alias){
        try {
            if (StringUtils.isNotEmpty(pubKey)){
                publicKey = getPublicKey(pubKey);
            }else if(publicKey==null){
                VerifySign.getPublicOrPrivateKey(keyFile,storepass,keypass,alias);
            }
            return CupSec.rsaVerifyWithSha256(publicKey,rootStr.getBytes(),signValue);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail:getPublickey or verifySign",e);
        }
        return false;
    }

    /**
     * 根据公钥字符串生成公钥对象
     * @desciption PKCS12常用的后缀是： .p12/.pfx;
     * X.509 DER编码（ASCⅡ）的后缀是：.der/.cer/.crt
     * X.509 PAM编码（Base64)的后缀是：.PEM/.cer/.crt
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String pubKey) throws Exception{
        byte[] keyBytes;
        keyBytes =  Base64.decodeBase64(pubKey.replaceAll("\n",""));

        //公钥获取方式1
        X509EncodedKeySpec keySpec =  new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = keyFactory.generatePublic(keySpec);

        //公钥获取方式2,在本例中不适用
//        publicKey = CertificateFactory.getInstance("X.509")
//                .generateCertificate(new ByteArrayInputStream(keyBytes))
//                .getPublicKey();
        return publicKey;
    }

    /**
     * 从证书中获取公钥对象和私钥对象
     * @throws Exception
     */
    public static void getPublicOrPrivateKey(String keyFile,String storepass,String keypass,String alias) throws Exception{
        InputStream resourceAsStream = VerifySign.class.getClassLoader().getResourceAsStream(keyFile);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //从文件输入流中获取keystore
        keyStore.load(resourceAsStream,storepass.toCharArray());
        resourceAsStream.close();

        //从keystore中读取证书和私钥、公钥
        //公钥获取方式3
        X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);//生成证书时设置的alias
        publicKey = certificate.getPublicKey();
        log.info("公钥：{}",Base64.encodeBase64String(publicKey.getEncoded()));
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias,keypass.toCharArray());
        log.info("私钥：{}",Base64.encodeBase64String(privateKey.getEncoded()));
    }

 }
