package io.github.lourier.toolkit.common.crypto;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

/**
 * @Description: DESede 算法实现
 * @Date: 2023/11/10 11:26
 * @Author: Lourier
 */
public class DESedeUtil {

    /**
     * 密钥算法
     * JDK 支持DESede算法实现，支持112位或168位密钥长度；
     * 通过 Bouncy Castle 可将算法密钥延长至128位或192位
     * */
    public static final String KEY_ALGORITHM = "DESede";

    /**
     * 加/解密算法 / 工作模式 / 填充方式；
     * JDK 支持 PKCS5Padding 填充方式。Bouncy Castle 支持 PKCS7Padding 填充方式
     * */
    public static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider()); // 导入 Bouncy Castle 库实现
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        return doFinal(data, key, Cipher.ENCRYPT_MODE);
    }


    public static byte[] decrypt(byte[] data, byte[] key) {
        return doFinal(data, key, Cipher.DECRYPT_MODE);
    }

    public static byte[] initKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM, "BC"); // 表示 Bouncy Castle 提供
            keyGenerator.init(192); // 指定密钥长度，可以是 112、168、192，这里选择最长的密钥长度
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
        try {
            DESedeKeySpec keySpec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generateSecret(keySpec);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] doFinal(byte[] data, byte[] key, int mode) {
        Key k = toKey(key);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC"); // 表示 Bouncy Castle 提供
            cipher.init(mode, k);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}
