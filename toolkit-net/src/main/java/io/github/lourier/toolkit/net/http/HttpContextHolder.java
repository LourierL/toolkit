package io.github.lourier.toolkit.net.http;

/**
 * @Description: HTTP 上下文状态类
 * @Date: 2023/11/16 15:45
 * @Author: Lourier
 */
public class HttpContextHolder {

    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();

    public static void bind(Object source) {
        THREAD_LOCAL.set(source);
    }

    public static <T> T get(Class<T> clasz) {
        Object object = THREAD_LOCAL.get();
        if (clasz.isInstance(object)) {
            return clasz.cast(object);
        }
        return null;
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
