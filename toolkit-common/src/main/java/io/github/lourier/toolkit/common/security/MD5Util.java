package io.github.lourier.toolkit.common.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.UnaryOperator;

/**
 * @Description: MD5加密实现
 * 产生一个128bit信息摘要，即16位字节数组. 在此基础上可转为32位十六进制字符串、base64字符串等可视化形式
 * @Date: 2023/11/9 11:36
 * @Author: Lourier
 */
public class MD5Util {

    /**
     * 原始输入作为参数，不建议使用
     * */
    public static String encryptString(String input) {
        return encryptString(input, UnaryOperator.identity());
    }

    /**
     * 实现自己的加盐逻辑，例如：encrypt("helloworld", a -> a + "salt")，推荐使用这种方式
     * */
    public static String encryptString(String input, UnaryOperator<String> operator) {
        String to = operator.apply(input);
        byte[] digest = encryptByteArray(to);
//        return DatatypeConverter.printHexBinary(digest);	// JDK 内置
        return Hex.encodeHexString(digest); 				// commons-codec 提供，效果一样
    }

    public static String encrypt2Base64(String input, UnaryOperator<String> operator) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        String to = operator.apply(input);
        byte[] digest = encryptByteArray(to);
        return Base64.encodeBase64String(digest);
    }

    public static byte[] encryptByteArray(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] data = input.getBytes();
            return messageDigest.digest(data); // 16位字节数组，也即128bit
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("md5 encrypt error", e);
        }
    }
}
