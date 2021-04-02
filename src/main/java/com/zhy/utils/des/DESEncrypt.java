package com.zhy.utils.des;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

public class DESEncrypt {

    private String encroptMode = "ECB"; // 加密模式

    private String paddingMode = "PKCS5Padding"; // 填充模式

    private String algorithm = "DES"; // 加密算法，目前支持DES、DESede

    private String keyStr ="sjzx9001";  //"abc&*123";

    public String desEncrypt(String oriData){
        try {
            byte[] keyBytes = getKeyStr().getBytes();
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keyBytes);

            KeyGenerator generator = KeyGenerator.getInstance(algorithm);
            generator.init(secureRandom);
            Key key = generator.generateKey();
            Cipher cipher =
                    Cipher.getInstance(
                            algorithm + "/" + encroptMode + "/" + paddingMode);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptBytes =
                    cipher.doFinal(oriData.getBytes());
            return Base64.encodeBase64String(encryptBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public String getEncroptMode() {
        return encroptMode;
    }

    public void setEncroptMode(String encroptMode) {
        this.encroptMode = encroptMode;
    }

    public String getPaddingMode() {
        return paddingMode;
    }

    public void setPaddingMode(String paddingMode) {
        this.paddingMode = paddingMode;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getKeyStr() {
        return keyStr;
    }

    public void setKeyStr(String keyStr) {
        this.keyStr = keyStr;
    }
}
