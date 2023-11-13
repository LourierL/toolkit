package io.github.lourier.toolkit.common.stream;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Description: 封装受检异常
 * @Date: 2023/11/7 15:47
 * @Author: Lourier
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T t) throws Exception;

    static <T,R> Function<T,R> wrapWithRuntimeException(CheckedFunction<T,R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <T,R> Function<T,R> wrapWithoutException(CheckedFunction<T,R> checkedFunction, Consumer<Exception> consumer) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
                return null; // 这个数据处理不了，因此返回空即可
            }
        };
    }

    static <T,R> Function<T,R> wrapWithoutExceptionWithSupplier(CheckedFunction<T,R> checkedFunction,
                                                                Consumer<Exception> consumer,
                                                                Supplier<R> supplier) {
        if (supplier == null) return wrapWithoutException(checkedFunction, consumer);
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
                return supplier.get(); // 这个数据处理不了，采用外部赋值的方式传一个
            }
        };
    }

}
