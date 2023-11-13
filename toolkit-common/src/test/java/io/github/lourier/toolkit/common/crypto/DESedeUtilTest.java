package io.github.lourier.toolkit.common.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Date: 2023/11/13 15:36
 * @Author: Lourier
 */
public class DESedeUtilTest {

    @Test
    public void testInitKey() {
        byte[] key = DESedeUtil.initKey();
        Assertions.assertEquals(24, key.length); // 192bit -> 24byte
        String hexKey = DESedeUtil.hexStringKey();
        Assertions.assertEquals(48, hexKey.length());
    }

    @Test
    public void testEncryptAndDecrypt() {
        byte[] key = DESedeUtil.initKey();
        String input = "DESede";
        byte[] encrypt = DESedeUtil.encrypt(input.getBytes(), key);
        byte[] decrypt = DESedeUtil.decrypt(encrypt, key);
        Assertions.assertEquals(input, new String(decrypt));
    }

}
