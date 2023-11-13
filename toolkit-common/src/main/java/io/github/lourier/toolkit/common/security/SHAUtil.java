package io.github.lourier.toolkit.common.security;

import org.apache.commons.codec.binary.Hex;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.UnaryOperator;

/**
 * @Description: SHA-512 算法实现
 * @Date: 2023/11/10 15:39
 * @Author: Lourier
 */
public class SHAUtil {

    public static byte[] sha1(String input) {
        return sha1(input, UnaryOperator.identity());
    }

    public static byte[] sha1(String input, UnaryOperator<String> operator) {
        return digest(input, operator, "SHA-1");
    }

    public static String sha1HexString(String input) {
        return sha1HexString(input, UnaryOperator.identity());
    }

    public static String sha1HexString(String input, UnaryOperator<String> operator) {
        byte[] bytes = sha1(input, operator);
        return Hex.encodeHexString(bytes);
    }

    public static byte[] sha256(String input) {
        return sha256(input, UnaryOperator.identity());
    }

    public static byte[] sha256(String input, UnaryOperator<String> operator) {
        return digest(input, operator, "SHA-256");
    }

    public static String sha256HexString(String input) {
        return sha256HexString(input, UnaryOperator.identity());
    }

    public static String sha256HexString(String input, UnaryOperator<String> operator) {
        byte[] bytes = sha256(input, operator);
        return Hex.encodeHexString(bytes);
    }

    public static byte[] sha512(String input) {
        return sha512(input, UnaryOperator.identity());
    }

    public static byte[] sha512(String input, UnaryOperator<String> operator) {
        return digest(input, operator, "SHA-512");
    }

    public static String sha512HexString(String input) {
        return sha512HexString(input, UnaryOperator.identity());
    }

    public static String sha512HexString(String input, UnaryOperator<String> operator) {
        byte[] bytes = sha512(input, operator);
        return Hex.encodeHexString(bytes);
    }


    private static byte[] digest(String input, UnaryOperator<String> operator, String algorithm) {
        try {
            String to = operator.apply(input);
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            return instance.digest(to.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
