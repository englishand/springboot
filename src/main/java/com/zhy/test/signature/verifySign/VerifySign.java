package com.zhy.test.signature.verifySign;

import com.zhy.test.signature.CupSec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;


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
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String pubKey) throws Exception{
        byte[] keyBytes;
        keyBytes =  Base64.decodeBase64(pubKey.replaceAll("\n",""));
//        X509EncodedKeySpec keySpec =  new X509EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        publicKey = CertificateFactory.getInstance("X.509")
                .generateCertificate(new ByteArrayInputStream(keyBytes))
                .getPublicKey();
        return publicKey;
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

    //错误公钥
    private static String pubKey2 = "MIIB/TCCAWagAwIBAgIETBRPczANBgkqhkiG9w0BAQUFADBDMQswCQYDVQQGEwJD\n" +
            "TjEQMA4GA1UEChMHVGVuY2VudDEPMA0GA1UECxMGVGVucGF5MREwDwYDVQQDEwhv\n" +
            "bmVjbGljazAeFw0xMDA2MTMwMzI0MzVaFw0yMDA2MTAwMzI0MzVaMEMxCzAJBgNV\n" +
            "BAYTAkNOMRAwDgYDVQQKEwdUZW5jZW50MQ8wDQYDVQQLEwZUZW5wYXkxETAPBgNV\n" +
            "BAMTCG9uZWNsaWNrMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpMz1a8sKu\n" +
            "k6gf2hCNvm0xfyfzSd5xOWL9PimhK99tNk0p0A35p/5K5aGcmKVwgHFTLxZzvy9e\n" +
            "DxK9Hf/BabTyqzThG3xNrCyFZHse2EbSZUVApwd4dwXy9U5AHHS/KUJ5QeV9V8d+\n" +
            "RuGZaQc65siVuYl8et41aTMK/kvkRSXkmQIDAQABMA0GCSqGSIb3DQEBBQUAA4GB\n" +
            "AJXZO3WQmhz7UjpbKRPJMSn5rWXY3/gjre62rkpEx4a13lfDAB9eSKUOmE0kTsls\n" +
            "NOKcVgf5GrnVrIQDKJczf8NRQ3nLR9OODE2MwNz7yl/CjJTuEjxBEClP43zTfssx\n" +
            "W//ZWtjwG8xrgMVoD3uJ+VeJxCw+cpirtWpXDfuq42fR";
}
