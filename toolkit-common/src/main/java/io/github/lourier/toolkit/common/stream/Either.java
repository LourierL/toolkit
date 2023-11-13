package io.github.lourier.toolkit.common.stream;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Description: 封装 exception 以及正常值
 * https://blog.csdn.net/zl1zl2zl3/article/details/90175115
 * @Date: 2023/11/7 16:22
 * @Author: Lourier
 */
public class Either<T extends Exception, R> {

    private final Exception left;
    private final R right;

    public Either(Exception left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <R> Either<Exception, R> left(Exception exception) {
        return new Either<>(exception, null);
    }

    public static <R> Either<Exception, R> right(R value) {
        return new Either<>(null, value);
    }

    public Optional<Exception> getLeft() {
        return Optional.ofNullable(left);
    }

    public Optional<R> getRight() {
        return Optional.ofNullable(right);
    }

    public boolean exception() {
        return left != null;
    }

    public boolean hasValue() {
        return right != null;
    }

    public static <T,R> Function<T, Either<Exception, R>> lift(CheckedFunction<T,R> function) {
        return lift(function, null);
    }

    public static <T,R> Function<T, Either<Exception, R>> lift(CheckedFunction<T,R> function, Supplier<R> supplier) {
        return t -> {
            try {
                return Either.right(function.apply(t));
            } catch (Exception ex) {
                if (supplier == null) {
                    return Either.left(ex);
                } else {
                    return new Either<>(ex, supplier.get());
                }
            }
        };
    }

}
