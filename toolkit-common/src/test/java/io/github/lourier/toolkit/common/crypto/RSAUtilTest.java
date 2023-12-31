package io.github.lourier.toolkit.common.crypto;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @Date: 2023/11/13 16:26
 * @Author: Lourier
 */
public class RSAUtilTest {

    // 公钥加密，私钥解密
    @Test
    public void testPublicKeyEncrypt() {
        Map<String, Object> map = RSAUtil.initKey();
        byte[] publicKey = RSAUtil.getPublicKey(map);
        byte[] privateKey = RSAUtil.getPrivateKey(map);
        String input = "hellworld";
        byte[] encrypt = RSAUtil.encryptByPublicKey(input.getBytes(), publicKey);
        byte[] decrypt = RSAUtil.decryptByPrivateKey(encrypt, privateKey);
        Assertions.assertEquals(input, new String(decrypt));
    }

    // 私钥加密，公钥解密
    @Test
    public void testPrivateKeyEncrypt() {
        Map<String, Object> map = RSAUtil.initKey();
        byte[] publicKey = RSAUtil.getPublicKey(map);
        byte[] privateKey = RSAUtil.getPrivateKey(map);
        String input = "hellworld";
        byte[] encrypt = RSAUtil.encryptByPrivateKey(input.getBytes(), privateKey);
        byte[] decrypt = RSAUtil.decryptByPublicKey(encrypt, publicKey);
        Assertions.assertEquals(input, new String(decrypt));
    }

    @Test
    public void testDefaultKey() {
        String publicKey = RSAUtil.getDefaultBase64PublicKey();
        String privateKey = RSAUtil.getDefaultBase64PrivateKey();
        String input = "helloworld";
        byte[] encryptPublic = RSAUtil.encryptByPublicKey(input.getBytes(), Base64.decodeBase64(publicKey));
        byte[] decryptPublic = RSAUtil.decryptByPrivateKey(encryptPublic, Base64.decodeBase64(privateKey));
        Assertions.assertEquals(input, new String(decryptPublic));

        byte[] encryptPrivate = RSAUtil.encryptByPrivateKey(input.getBytes(), Base64.decodeBase64(privateKey));
        byte[] decryptPrivate = RSAUtil.decryptByPublicKey(encryptPrivate, Base64.decodeBase64(publicKey));
        Assertions.assertEquals(input, new String(decryptPrivate));
    }

}
