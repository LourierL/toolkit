package io.github.lourier.toolkit.net.http;

import okhttp3.OkHttpClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import java.net.URI;

/**
 * @Description: 可设置超时时间的工厂类
 * @Date: 2023/11/16 19:14
 * @Author: Lourier
 */
public class OkHttp3ContextRequestFactory extends OkHttp3ClientHttpRequestFactory {

    public OkHttp3ContextRequestFactory(OkHttpClient client) {
        super(client);
    }

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) {
        SimpleRequestConfig requestConfig;
        if ((requestConfig = HttpContextHolder.get()) != null) {
            setReadTimeout(requestConfig.getReadTimeout());
            setWriteTimeout(requestConfig.getReadTimeout());
            setConnectTimeout(requestConfig.getConnectTimeout());
        }
        return super.createRequest(uri, httpMethod);
    }

    @Override
    public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) {
        SimpleRequestConfig requestConfig;
        if ((requestConfig = HttpContextHolder.get()) != null) {
            setReadTimeout(requestConfig.getReadTimeout());
            setWriteTimeout(requestConfig.getReadTimeout());
            setConnectTimeout(requestConfig.getConnectTimeout());
        }
        return super.createAsyncRequest(uri, httpMethod);
    }
}
