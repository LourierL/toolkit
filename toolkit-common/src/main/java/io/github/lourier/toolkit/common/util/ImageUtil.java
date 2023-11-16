package io.github.lourier.toolkit.common.util;

import org.apache.commons.codec.binary.Hex;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

/**
 * @Description: 图片工具类 用 Thumbnails 改造，单独拉一个模块
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

    // 图片处理，并转为其他格式输出
    public static void pic2OtherType(InputStream is, OutputStream os, ImageType type, Function<BufferedImage, BufferedImage> function) {
        try {
            BufferedImage source = ImageIO.read(is);
            BufferedImage dest = function.apply(source);
            ImageIO.write(dest, type.name(), os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 图片处理，并转为其他格式输出
    public static byte[] pic2OtherType(InputStream is, ImageType type, Function<BufferedImage, BufferedImage> function) {
        try {
            BufferedImage source = ImageIO.read(is);
            BufferedImage dest = function.apply(source);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(dest, type.name(), baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
