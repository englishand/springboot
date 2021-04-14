package com.zhy.signature.sign;

import com.zhy.signature.CupSec;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Arrays;

public class SignUtil {

    private static PrivateKey privateKey;
    private static String keyFile = "D:/app/ebpay/jks/4002486073.pfx";
    private static String keyAlias = "UNIONPAY";
    private static String keypwd = "123456";

    /**
     * 校验开关
     */
    private static boolean CERT_SWITCH = true;

    /**
     * 业务流程：从证书中获取私钥，通过私钥对待签名数据进行签名
     * @param rootBytes     待签名数据
     * @return  签名数据
     * @throws IOException
     */
    public static String sign(byte[] rootBytes,String keyFile,String alias,String keypass,String storepass,String storetype) throws IOException{
        if (CERT_SWITCH){
            privateKey = RsaCertUtil.getPriKeyPkcs12(keyFile,alias,keypass,storepass,storetype);
            return CupSec.rsaSignWithSha256(privateKey,rootBytes);
        }else {
            return Arrays.toString(rootBytes);
        }
    }
}
