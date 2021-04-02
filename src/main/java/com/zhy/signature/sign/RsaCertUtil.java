package com.zhy.signature.sign;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;

/**
 * 证书工具：从X509证书中获取公钥；从PKCS12、jks、PKCS8中获取私钥
 */
@Slf4j
public class RsaCertUtil {

    /**
     * 从私钥证书中获取私钥
     * @param keyFile 证书文件(.pfx, .keystore)
     * @param keyAlias  证书名称
     * @param keypwd    证书密码
     * @param type  证书类型
     * @return  私钥
     */
    public static PrivateKey getPriKeyPkcs12(String keyFile,String keyAlias,String keypwd,String type){
        KeyStore keyStore = getKeyStore(keyFile,keypwd,type);
        PrivateKey privateKey = null;
        try {
            privateKey = (PrivateKey) keyStore.getKey(keyAlias,keypwd.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail:get priavateKey from keystore",e);
        }
        return privateKey;
    }

    /**
     *  将证书文件读取为证书存储对象，证书文件类型为：JKS(.keystore等)、PKCS12(.pfx)
     * @param keyFile 证书文件
     * @param keypwd    证书密码
     * @param type  证书类型
     * @return  证书对象
     */
    private static KeyStore getKeyStore(String keyFile,String keypwd,String type){
        InputStream fis = null;
        try {
            KeyStore keyStore = null;
            if ("JKS".equals(type)){
                keyStore = KeyStore.getInstance(type);
            }else if ("PKCS12".equals(type)){
                Security.insertProviderAt(new BouncyCastleProvider(),1);
                Security.addProvider(new BouncyCastleProvider());
                keyStore = KeyStore.getInstance(type);
            }

            if (!StringUtils.isEmpty(keyFile)){
                fis = new FileInputStream(keyFile);
            }else {
                fis = RsaCertUtil.class.getClassLoader().getResourceAsStream("jks/4002486073.pfx");
            }

            char[] nPassword = null;
            nPassword = null==keypwd||"".equals(keypwd.trim())?null:keypwd.toCharArray();
            //npassword：用来解锁密码库或者检查密码库数据的完整性
            keyStore.load(fis,nPassword);
            fis.close();
            return keyStore;
        }catch (Exception e){
            if (Security.getProvider("BC")==null){
                log.info("BC Provider not installed.");
            }
            log.error("fail:load keyStore cerficate",e);
        }
        return null;
    }
}
