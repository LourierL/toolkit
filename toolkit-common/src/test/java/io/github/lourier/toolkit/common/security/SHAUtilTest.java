package io.github.lourier.toolkit.common.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Date: 2023/11/13 11:32
 * @Author: Lourier
 */
public class SHAUtilTest {

    @Test
    public void testSha1() {
        byte[] bytes = SHAUtil.sha1("hello world");
        Assertions.assertEquals(20, bytes.length); // 160 / 8
        String hexString = SHAUtil.sha1HexString("hahaha");
        Assertions.assertEquals(40, hexString.length()); // 160 / 4
    }

    @Test
    public void testSha256() {
        byte[] bytes = SHAUtil.sha256("hello world");
        Assertions.assertEquals(32, bytes.length); // 160 / 8
        String hexString = SHAUtil.sha256HexString("hahaha");
        Assertions.assertEquals(64, hexString.length()); // 160 / 4
    }

    @Test
    public void testSha512() {
        byte[] bytes = SHAUtil.sha512("hello world");
        Assertions.assertEquals(64, bytes.length); // 160 / 8
        String hexString = SHAUtil.sha512HexString("hahaha");
        Assertions.assertEquals(128, hexString.length()); // 160 / 4
    }

}
