package io.github.lourier.toolkit.common.crypto;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 算法实现。如果公钥用于加密，那么私钥用于解密，反之亦然
 * @Date: 2023/11/10 11:26
 * @Author: Lourier
 */
public class RSAUtil {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";

    private static final String DEFAULT_BASE64_PUBLIC_KEY;
    private static final String DEFAULT_BASE64_PRIVATE_KEY;

    static {
        RsaKeyPair keyPair = getKeyPair();
        DEFAULT_BASE64_PUBLIC_KEY = keyPair.getBase64PublicKey();
        DEFAULT_BASE64_PRIVATE_KEY = keyPair.getBase64PrivateKey();
    }

    @Data
    public static final class RsaKeyPair {
        private String base64PublicKey;
        private String base64PrivateKey;

        public RsaKeyPair(String base64PublicKey, String base64PrivateKey) {
            this.base64PublicKey = base64PublicKey;
            this.base64PrivateKey = base64PrivateKey;
        }
    }


    public static Map<String, Object> initKey() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            generator.initialize(1024);
            // 生成密钥对
            KeyPair keyPair = generator.genKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> res = new HashMap<>(4);
            res.put(PUBLIC_KEY, publicKey);
            res.put(PRIVATE_KEY, privateKey);
            return res;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getPublicKey(Map<String, Object> map) {
        Key key = (Key) map.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    public static byte[] getPrivateKey(Map<String, Object> map) {
        Key key = (Key) map.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    public static String getDefaultBase64PublicKey() {
        return DEFAULT_BASE64_PUBLIC_KEY;
    }

    public static String getDefaultBase64PrivateKey() {
        return DEFAULT_BASE64_PRIVATE_KEY;
    }

    public static RsaKeyPair getKeyPair() {
        Map<String, Object> map = initKey();
        byte[] publicKey = getPublicKey(map);
        byte[] privateKey = getPrivateKey(map);
        return new RsaKeyPair(Base64.encodeBase64String(publicKey), Base64.encodeBase64String(privateKey));
    }


    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptByPublicKey(byte[] data, byte[] key) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                 | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                 | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptByPublicKey(byte[] data, byte[] key) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                 | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}
