package io.github.lourier.toolkit.net.http;

/**
 * @Description:  HTTP 请求超时时间配置
 * 统一了 HttpUrlConnection、Apache、Okhttp3 三种客户端的超时配置
 * @Date: 2023/11/16 16:29
 * @Author: Lourier
 */
public class SimpleRequestConfig {

    private int readTimeout;
    private int connectTimeout;

    public SimpleRequestConfig(int readTimeout, int connectTimeout) {
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
