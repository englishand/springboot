package com.zhy.utils.des;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

public class DESDecrypt {

    String encroptMode = "ECB"; // 加密模式

    String paddingMode = "PKCS5Padding"; // 填充模式

    String algorithm = "DES"; // 加密算法，目前支持DES、DESede

    String keyStr = "sjzx9001"; //"abc&*123";

    public String desDecrept(String encryStr){
        try {
            byte[] keyBytes = getKeyStr().getBytes();
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keyBytes);
            KeyGenerator generator = KeyGenerator.getInstance(algorithm);

            generator.init(secureRandom);
            Key key = generator.generateKey();
            Cipher cipher = Cipher.getInstance(algorithm + "/" + encroptMode
                    + "/" + paddingMode);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] oriBytes = cipher.doFinal(Base64.decodeBase64(encryStr));
            return new String(oriBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String desDecreptCC(String cardKey,String encryStr){
        try {
            byte[] keyBytes = cardKey.getBytes();
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keyBytes);
            KeyGenerator generator = KeyGenerator.getInstance(algorithm);

            generator.init(secureRandom);
            Key key = generator.generateKey();
            Cipher cipher = Cipher.getInstance(algorithm + "/" + encroptMode
                    + "/" + paddingMode);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] oriBytes = cipher.doFinal(Base64.decodeBase64(encryStr));
            return new String(oriBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    /**
     * @param args
     */
	public static void main(String[] args) {
		String cardKey = "sjzx9001";
		DESEncrypt encrypt = new DESEncrypt();
		String encryStr = encrypt.desEncrypt("123123");//947297 111111
		System.out.println("密文："+encryStr);

		DESDecrypt decrypt = new DESDecrypt();
		String decryptStr = decrypt.desDecreptCC(cardKey,encryStr);
		System.out.println("原文："+decryptStr);
	}
}
