package io.github.lourier.toolkit.net.http;

/**
 * @Description: HTTP 上下文状态类
 * @Date: 2023/11/16 15:45
 * @Author: Lourier
 */
public class HttpContextHolder {

    private static final ThreadLocal<SimpleRequestConfig> THREAD_LOCAL = new ThreadLocal<>();

    public static void bind(SimpleRequestConfig source) {
        THREAD_LOCAL.set(source);
    }

    public static SimpleRequestConfig get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
