package io.github.lourier.toolkit.common.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static io.github.lourier.toolkit.common.stream.CheckedConsumer.wrapWithRuntimeException;

/**
 * @Description: closure generator 特性实现的 Stream-like API
 * 直观、易懂、远超 Stream 数量的 API，可实现异步、并行流
 * @link https://mp.weixin.qq.com/s/v-HMKBWxtz1iakxFL09PDw
 * @link https://github.com/wolray/seq
 * @Date: 2023/11/9 11:33
 * @Author: Lourier
 */
@FunctionalInterface
public interface Seq<T> {

    void consume(Consumer<T> consumer);

    /**************************************************************************************************/
    // static
    /**************************************************************************************************/

    Seq<Object> EMPTY = c -> {};
    Consumer<Object> NOTHING = t -> {};

    /**************************************************************************************************/
    // source -- 本质上就是一个生成器
    /**************************************************************************************************/

    @SuppressWarnings("unchecked")
    static <T> Seq<T> empty() {
        return (Seq<T>) EMPTY;
    }

    static <T> Seq<T> unit(T t) {
        return consumer -> consumer.accept(t);
    }

    @SafeVarargs
    static <T> Seq<T> array(T... t) {
        return consumer -> Arrays.asList(t).forEach(consumer); // Arrays.asList(t)::forEach
    }

    static <T> Seq<T> list(List<T> list) {
        return list::forEach;
    }

    static <T> Seq<T> of(Iterable<T> iterable) { // 天然可复用
        return iterable.iterator()::forEachRemaining;
    }

    static <T> Seq<T> of(Optional<T> optional) {
        return optional::ifPresent;
    }

    // 生成一个无限流
    static <T> Seq<T> iterate(final T seed, final UnaryOperator<T> f) {
        final Iterator<T> iterator = new Iterator<T>() {
            T t = null;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return t = (t == null) ? seed : f.apply(t);
            }
        };
        return consumer -> {
            while (iterator.hasNext()) { // always true
                consumer.accept(iterator.next());
            }
        };
    }




    /**************************************************************************************************/
    // stateless operation
    /**************************************************************************************************/

    default <R> Seq<R> map(Function<T, R> function) {
        return consumer -> consume(t -> consumer.accept(function.apply(t)));
    }

    default <R> Seq<R> flatMap(Function<T, Seq<R>> function) {
        return consumer -> consume(t -> function.apply(t).consume(consumer));
    }

    default Seq<T> peek(Consumer<T> consumer) {
        return c -> consume(consumer.andThen(c));
    }

    // 等同于 peek
    default Seq<T> onEach(Consumer<T> consumer) {
        return peek(consumer);
    }


    default Seq<T> filter(Predicate<T> predicate) {
        return consumer -> consume(t -> {
            if (predicate.test(t)) {
                consumer.accept(t);
            }
        });
    }

    /**************************************************************************************************/
    // stateful operation
    /**************************************************************************************************/

    default Seq<T> distinct() {
        return consumer -> {
            Set<T> set = new HashSet<>();
            consume(t -> {
                if (!set.contains(t)) {
                    consumer.accept(t);
                    set.add(t);
                }
            });
        };
    }

    default Seq<T> sorted() {
        return sorted(null); // use the elements' natural ordering
    }

    default Seq<T> sorted(Comparator<T> comparator) {
        return consumer -> {
            List<T> list = new ArrayList<>(); // begin
            consume(list::add); // accept
            list.sort(comparator);
            for (T t : list) { // end
                consumer.accept(t);
            }
        };
    }

    // 获取前 n 个
    default Seq<T> limit(long maxSize) {
        return consumer -> {
            long[] i = {maxSize};
            consumeTillStop(t -> {
                if (i[0]-- > 0) {
                    consumer.accept(t);
                } else {
                    stop();
                }
            });
        };
    }

    // 获取前 n 个
    default Seq<T> take(long maxSize) {
        return limit(maxSize);
    }

    default Seq<T> takeWhile(Predicate<T> predicate) {
        return consumer -> {
            consumeTillStop(t -> {
                if (predicate.test(t)) {
                    consumer.accept(t);
                } else {
                    stop();
                }
            });
        };
    }

    // 跳过前 n 个
    default Seq<T> drop(long n) {
        return skip(n);
    }

    // 跳过前 n 个，直到满足条件，则取当前后所有元素
    default Seq<T> dropWhile(Predicate<T> predicate) {
        return consumer -> {
            AtomicBoolean flag = new AtomicBoolean(false);
            consumeTillStop(t -> {
                if (predicate.test(t) || flag.get()) {
                    consumer.accept(t);
                    flag.set(true);
                }
            });
        };
    }

    // 跳过前 n 个
    default Seq<T> skip(long n) {
        return consumer -> {
            long[] i = {n - 1};
            consumeTillStop(t -> {
                if (i[0] < 0) {
                    consumer.accept(t);
                } else {
                    i[0]--;
                }
            });
        };
    }

    // 流和传入的 iterator 元素两两聚合，形成新的流
    default <E, R> Seq<R> zip(Iterable<E> Iterable, BiFunction<T, E, R> function) {
        return consumer -> {
            Iterator<E> iterator = Iterable.iterator();
            consumeTillStop(t -> {
                if (iterator.hasNext()) {
                    consumer.accept(function.apply(t, iterator.next()));
                } else {
                    stop();
                }
            });
        };
    }

    /**************************************************************************************************/
    // terminal Operation (non short-circuiting)
    /**************************************************************************************************/

    default void forEach(Consumer<T> consumer) {
        consume(consumer);
    }

    default void forEachOrdered(Consumer<T> consumer) {
        sorted().consume(consumer);
    }

    @SuppressWarnings("unchecked")
    default T reduce(T identity, BinaryOperator<T> accumulator) {
        Object[] result = {identity};
        consume(t -> result[0] = accumulator.apply((T) result[0], t));
        return (T) result[0];
    }

    default <R> R collect(Supplier<R> supplier,
                  BiConsumer<R, ? super T> accumulator) {
        R r = supplier.get();
        consume(t -> {
            System.out.println(Thread.currentThread().getName() + " handle");
            accumulator.accept(r, t);
        });
        return r;
    }

    default String join(String delimiter) {
        StringJoiner joiner = new StringJoiner(delimiter);
        consume(t -> joiner.add(t.toString()));
        return joiner.toString();
    }

    default List<T> toList() {
        List<T> list = new ArrayList<>();
        consume(list::add);
        return list;
    }

    default Set<T> toSet() {
        Set<T> set = new HashSet<>();
        consume(set::add);
        return set;
    }

    default T[] toArray() {
        return null;
    }

    default T random() {
        return null;
    }

    /**************************************************************************************************/
    // parallelism
    /**************************************************************************************************/

    default Seq<T> parallel() {
        return consumer -> map(t -> ForkJoinPool.commonPool().submit(() -> consumer.accept(t))).cache().consume(ForkJoinTask::join);
    }

    default Seq<T> parallel(ExecutorService executorService) {
        return c -> map(t -> executorService.submit(() -> c.accept(t)))
                .consume(wrapWithRuntimeException(Future::get));
    }

    /**************************************************************************************************/
    // re-use
    /**************************************************************************************************/


    default Seq<T> cache() {
        List<T> arraySeq = new ArrayList<>();
        consume(arraySeq::add);
        return Seq.list(arraySeq);
    }

    /**************************************************************************************************/
    // terminal Operation (short-circuiting)
    /**************************************************************************************************/

    static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(5);
        ArrayList<Integer> collect = Seq.array(1, 2, 3, 4, 5,6,7,8,9,10)
                .parallel()
                .collect(ArrayList::new, ArrayList::add);
        System.out.println(collect);

    }

    /****************/
    // 异常中断
    /****************/

    static void stop() {
        throw StopException.INSTANCE;
    }

    default void consumeTillStop(Consumer<T> consumer) {
        try {
            consume(consumer);
        } catch (Exception ignore){}
    }

    final class StopException extends RuntimeException {

        static final StopException INSTANCE = new StopException();

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
