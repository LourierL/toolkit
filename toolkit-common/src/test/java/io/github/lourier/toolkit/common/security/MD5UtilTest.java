package io.github.lourier.toolkit.common.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Date: 2023/11/10 11:37
 * @Author: Lourier
 */
public class MD5UtilTest {

    // 测试加密后的16进制字符串，长度为32
    @Test
    public void testString() {
        String input = "helloworld";
        String encrypt = MD5Util.encryptString(input);
        Assertions.assertNotNull(encrypt);
        Assertions.assertEquals(32, encrypt.length(), "must be 32 hex length");
    }

    // 测试加密后的字节数组，长度为16
    @Test
    public void testByte() {
        String input = "helloworld";
        byte[] bytes = MD5Util.encryptByteArray(input);
        Assertions.assertNotNull(bytes);
        Assertions.assertEquals(16, bytes.length, "must be 16 byte array length");
    }

    // 测试加盐操作，加密后的16进制字符串，长度为32
    @Test
    public void testAddSaltString() {
        String input = "helloworld";
        String encrypt = MD5Util.encryptString(input, a -> a + "salt");
        Assertions.assertNotNull(encrypt);
        Assertions.assertEquals(32, encrypt.length(), "must be 32 hex length");
    }

    // 测试加盐操作，加密后的 Base64 字符串
    @Test
    public void testAddSaltBase64() {
        String input = "helloworld";
        String encrypt = MD5Util.encrypt2Base64(input, a -> a + "salt");
        Assertions.assertNotNull(encrypt);
//        Assertions.assertEquals(32, encrypt.length(), "must be 32 hex length");
    }

}
