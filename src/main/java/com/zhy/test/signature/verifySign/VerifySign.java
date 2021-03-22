package com.zhy.test.signature.verifySign;

import com.zhy.test.signature.CupSec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

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

    public static boolean verifySign(String rootStr, String signValue){
        log.info("signstr:"+signValue);
        try {
            publicKey = getPublicKey(pubKey2);
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

        //公钥获取方式2
//        publicKey = CertificateFactory.getInstance("X.509")
//                .generateCertificate(new ByteArrayInputStream(keyBytes))
//                .getPublicKey();
        return publicKey;
    }

    /**
     * 从证书中获取公钥对象和私钥对象
     * @throws Exception
     */
    public static void getPublicOrPrivateKey() throws Exception{
        InputStream resourceAsStream = VerifySign.class.getClassLoader().getResourceAsStream("jks/4002486073.pfx");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //生成证书时设置的密钥库口令
        String storePass = "123456";
        //从文件输入流中获取keystore
        keyStore.load(resourceAsStream,storePass.toCharArray());
        resourceAsStream.close();

        //从keystore中读取证书和私钥、公钥
        //公钥获取方式3
        X509Certificate certificate = (X509Certificate) keyStore.getCertificate("unionpay");//生成证书时设置的alias
        publicKey = certificate.getPublicKey();
        log.info("公钥：{}",Base64.encodeBase64String(publicKey.getEncoded()));
        PrivateKey privateKey = (PrivateKey) keyStore.getKey("unionpay",storePass.toCharArray());
        log.info("私钥：{}",Base64.encodeBase64String(privateKey.getEncoded()));
    }

    public static void main(String[] args){
        try {
            VerifySign.getPublicOrPrivateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //正确的公钥
    //正常应从数据库取，表：core_cert; 或从配置文件中获取
    private static String pubKey = //"-----BEGIN CERTIFICATE-----" +
            "MIIEPTCCAyWgAwIBAgIFQAJIYHMwDQYJKoZIhvcNAQELBQAwXTELMAkGA1UEBhMC\n" +
            "Q04xMDAuBgNVBAoMJ0NoaW5hIEZpbmFuY2lhbCBDZXJ0aWZpY2F0aW9uIEF1dGhv\n" +
            "cml0eTEcMBoGA1UEAwwTQ0ZDQSBBQ1MgVEVTVCBPQ0EzMTAeFw0xNzA5MTUwMjAx\n" +
            "NDVaFw0xNzEyMTUwMjAxNDVaMGQxCzAJBgNVBAYTAkNOMQ4wDAYDVQQKDAVPQ0Ez\n" +
            "MTEPMA0GA1UECwwGVFBDLVMzMRkwFwYDVQQLDBBPcmdhbml6YXRpb25hbC0xMRkw\n" +
            "FwYDVQQDDBB0ZXN0MTBBMTAwMDAyNDE0MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A\n" +
            "MIIBCgKCAQEArAAQnsm8VKAzAw6ts3Sd9R8VBSBZm4wg42Erv2ffTh4gn+NdL+xQ\n" +
            "DlCzUl+NrnO+dKipEZf/v+R0Loztc4grT00dgplGOoCSa7eNwUl7L/+UHjjGY5RB\n" +
            "IderFgHRFlpEhZZvDfiJavlyzdp76QdY/J+/OOgadDJH4KTqCSX+uNRwL0eYGXID\n" +
            "a8IjQP9811Ss4tA9e18iawQK6NbrMqAoIxGwhQN8R9Xe+Up45KF+EtExb5Xq09Zy\n" +
            "XUcFXx3g1AjsqPu9WLyGnN0Ey2GEpi1OplNatnmW/WBdNDLm2HBJpnHWNeJqt4nN\n" +
            "xKu8EU4g7GJa6MvKdtLLR/vva+U68/iIjQIDAQABo4H8MIH5MD8GCCsGAQUFBwEB\n" +
            "BDMwMTAvBggrBgEFBQcwAYYjaHR0cDovL29jc3B0ZXN0LmNmY2EuY29tLmNuOjgw\n" +
            "L29jc3AwHwYDVR0jBBgwFoAUmj20rmVY+85aBXgmoG0rBIa6xuwwDAYDVR0TAQH/\n" +
            "BAIwADA5BgNVHR8EMjAwMC6gLKAqhihodHRwOi8vMjEwLjc0LjQyLjMvT0NBMzEv\n" +
            "UlNBL2NybDQ4MDguY3JsMA4GA1UdDwEB/wQEAwIGwDAdBgNVHQ4EFgQU53gS5hZJ\n" +
            "/2Sv8ukpE1HkTxeBED8wHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMEMA0G\n" +
            "CSqGSIb3DQEBCwUAA4IBAQB0IMz48+7FC73E7l8TgFE8Atq9/CXJKb14v6a0cs5m\n" +
            "a9qeMfaR4YYtlXrAqcvjGbPacl7izxKAGnIa3LzbK/3NuWXqPYhGDMDCeDwMueet\n" +
            "vr/Hk6E5FFBwf6Kmfq7KDYEQuT2UDUixOCZjspIi+Kjm8Kwrkl5FbAemn8wRUwlj\n" +
            "PAGVKDNJXOrSi69g55zr8QMKcfwzsOxsPlyoSAjq+cjXXWsQy/SH5sMCdlrZ8drk\n" +
            "v9TKMeVJm1l5o8YPpGdJjgO8Cqz5zcmohN9mShO/sPn3sjHVFQdjag3YWNUy1wFl\n" +
            "T1BpkiAPqRFFCJVOF/RLBe6YLqZl/cQ4/4Ejk4dsbt+L\n";// +
          //  "-----END CERTIFICATE-----";

    //错误公钥，仅用来测试验签是否能通过
    private static String pubKey2 ="" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArAAQnsm8VKAzAw6ts3Sd9R8VBSBZm4wg42Erv2ffTh4gn+NdL+xQDlCzUl+NrnO+dKipEZf/v+R0Loztc4grT00dgplGOoCSa7eNwUl7L/+UHjjGY5RBIderFgHRFlpEhZZvDfiJavlyzdp76QdY/J+/OOgadDJH4KTqCSX+uNRwL0eYGXIDa8IjQP9811Ss4tA9e18iawQK6NbrMqAoIxGwhQN8R9Xe+Up45KF+EtExb5Xq09ZyXUcFXx3g1AjsqPu9WLyGnN0Ey2GEpi1OplNatnmW/WBdNDLm2HBJpnHWNeJqt4nNxKu8EU4g7GJa6MvKdtLLR/vva+U68/iIjQIDAQAB";
}
