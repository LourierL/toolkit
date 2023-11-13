package io.github.lourier.toolkit.common.util;

import org.apache.commons.codec.binary.Hex;

/**
 * @Description: 图片工具类
 * @Date: 2023/11/13 14:39
 * @Author: Lourier
 */
public final class ImageUtil {

    public enum ImageType {
        JPEG,
        PNG,
        BMP,
        GIF,
        WEBP
    }

    private static final String HEADER_JPEG = "FFD8FF";
    private static final String HEADER_PNG = "89504E47";
    private static final String HEADER_BMP = "424D";
    private static final String HEADER_GIF = "47494638";
    private static final String HEADER_WEBP = "52494646";

    // 根据文件流的前几个字节获取图片文件的格式
    public static ImageType getImageSuffix(byte[] headerBytes) {
        if (headerBytes == null) {
            return null;
        }
        String hexString = Hex.encodeHexString(headerBytes).toUpperCase();
        if (hexString.contains(HEADER_JPEG)) {
            return ImageType.JPEG;
        }
        if (hexString.contains(HEADER_PNG)) {
            return ImageType.PNG;
        }
        if (hexString.contains(HEADER_BMP)) {
            return ImageType.BMP;
        }
        if (hexString.contains(HEADER_GIF)) {
            return ImageType.GIF;
        }
        if (hexString.contains(HEADER_WEBP)) {
            return ImageType.WEBP;
        }
        return null;
    }

}
