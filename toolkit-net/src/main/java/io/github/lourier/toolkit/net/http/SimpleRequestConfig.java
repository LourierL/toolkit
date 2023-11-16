package io.github.lourier.toolkit.net.http;

/**
 * @Description:  JDK 默认 HTTP 客户端 HttpURLConnection 中需要配置地属性
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
