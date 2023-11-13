package io.github.lourier.toolkit.common.stream;

import java.util.function.Consumer;

/**
 * @Date: 2023/11/9 17:34
 * @Author: Lourier
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    void accept(T t) throws Exception;

    static <T> Consumer<T> wrapWithRuntimeException(CheckedConsumer<T> checkedConsumer) {
         return t -> {
             try {
                 checkedConsumer.accept(t);
             } catch (Exception e) {
                 throw new RuntimeException(e);
             }
         };
    }

    static <T> Consumer<T> wrapWithoutException(CheckedConsumer<T> checkedConsumer, Consumer<Exception> consumer) {
        return t -> {
            try {
                checkedConsumer.accept(t);
            } catch (Exception e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
            }
        };
    }


}
