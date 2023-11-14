package io.github.lourier.toolkit.common.crypto;

import io.github.lourier.toolkit.common.util.ImageUtil;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @Description: Base64编码封装工具类
 * @Date: 2023/11/13 9:37
 * @Author: Lourier
 */
public class Base64Util {

    // 图片转为 Base64 格式。 网页上可用
    // 仅支持 jpg,png,bmp,gif,webp 格式
    public static String picToBase64String(InputStream inputStream) {

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            byte[] dest = baos.toByteArray();
            return picToBase64String(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // 图片转为 Base64 格式。 网页上可用
    // 仅支持 jpg,png,bmp,gif,webp 格式
    public static String picToBase64String(byte[] bytes) {
        if (bytes == null || bytes.length <= 4) {
            throw new RuntimeException("不支持该文件流格式");
        }
        if (bytes.length > 1024 * 1024) { // 最大支持 1MB 解析长度
            throw new RuntimeException("图片长度过长");
        }
        byte[] header = Arrays.copyOf(bytes, 4);
        ImageUtil.ImageType type = ImageUtil.getImageSuffix(header);
        if (type == null) {
            throw new RuntimeException("不支持该文件流格式");
        }
        String prefix = String.format("data:image/%s;base64,", type.name()); // 图片base64编码前缀
        return prefix + Base64.encodeBase64String(bytes);
    }

}
