package io.github.lourier.toolkit.common.stream;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @Description: 封装受检异常
 * @Date: 2023/11/16 15:28
 * @Author: Lourier
 */
public interface CheckedPredicate<T> {

    boolean test(T t) throws Exception;

    static <T> Predicate<T> wrapWithRuntimeException(CheckedPredicate<T> checkedPredicate) {
        return t -> {
            try {
                return checkedPredicate.test(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <T> Predicate<T> wrapWithoutException(CheckedPredicate<T> checkedPredicate, Consumer<Exception> consumer) {
        return t -> {
            try {
                return checkedPredicate.test(t);
            } catch (Exception e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
            }
            return false;
        };
    }

}
