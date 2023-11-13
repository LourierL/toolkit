package io.github.lourier.toolkit.common.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @Description: DESede 算法实现
 * JDK 支持DESede算法实现，支持112位或168位密钥长度；通过Bouncy Castle可将DESede算法密钥延长至128位或192位
 * @Date: 2023/11/10 11:26
 * @Author: Lourier
 */
public class DESedeUtil {

    public static byte[] encrypt(byte[] data, byte[] key) {
        return doFinal(data, key, Cipher.ENCRYPT_MODE);
    }

    public static String encrypt2HexString(byte[] data, byte[] key) {
        byte[] bytes = encrypt(data, key);
        return Hex.encodeHexString(bytes);
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        return doFinal(data, key, Cipher.DECRYPT_MODE);
    }

    public static String decrypt2HexString(byte[] data, byte[] key) {
        byte[] decrypt = decrypt(data, key);
        return Hex.encodeHexString(decrypt);
    }

    public static byte[] initKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede", "BC"); // 表示 Bouncy Castle 提供
            keyGenerator.init(192); // 指定密钥长度
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hexStringKey() {
        byte[] key = initKey();
        return Hex.encodeHexString(key);
    }

    public static String base64Key() {
        byte[] key = initKey();
        return Base64.encodeBase64String(key);
    }

    private static Key toKey(byte[] key) {
        return new SecretKeySpec(key, "DESede");
    }

    private static byte[] doFinal(byte[] data, byte[] key, int mode) {
        Key k = toKey(key);
        try {
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding", "BC"); // 表示 Bouncy Castle 提供
            cipher.init(mode, k);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}
