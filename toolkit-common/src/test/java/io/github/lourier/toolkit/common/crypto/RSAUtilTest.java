package io.github.lourier.toolkit.common.crypto;

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

}
