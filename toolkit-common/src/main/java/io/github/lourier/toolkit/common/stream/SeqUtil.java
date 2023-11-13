package io.github.lourier.toolkit.common.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Description: 封装 Seq 流，处理一些边界异常问题时，挺好用的
 * @Date: 2023/11/9 16:29
 * @Author: Lourier
 */
public class SeqUtil {

    public static Seq<String> files(String path) {
        return consumer -> {
            try(BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
                String line;
                while ((line = br.readLine()) != null) {
                    consumer.accept(line);
                }
            } catch (IOException e) { // closeable
                throw new RuntimeException(e);
            }
        };
    }

}
