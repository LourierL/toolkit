package io.github.lourier.toolkit.net.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * @Description: test
 * @Date: 2023/11/16 17:08
 * @Author: Lourier
 */
public class HttpTest {

    @Test
    public void testSimpleHttp() {
        URI uri = URI.create("http://www.baidu.com");
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryUtil.simpleHttpRequestFactory(null);
        try {
            ClientHttpRequest request = requestFactory.createRequest(uri, HttpMethod.GET);
            ClientHttpResponse execute = request.execute();
            Assertions.assertEquals(200, execute.getRawStatusCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSimpleHttps() {
        URI uri = URI.create("https://www.baidu.com");
        String passwd = "123456";
        InputStream keystore = ClassLoader.getSystemResourceAsStream("baidu.keystore");
        SSLSocketFactory socketFactory = ClientHttpRequestFactoryUtil.sslSocketFactory(keystore, passwd);
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryUtil.simpleHttpRequestFactory(socketFactory);
        try {
            ClientHttpRequest request = requestFactory.createRequest(uri, HttpMethod.GET);
            ClientHttpResponse execute = request.execute();
            Assertions.assertEquals(200, execute.getRawStatusCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testApacheHttp() {
        URI uri = URI.create("http://www.baidu.com");
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryUtil.httpComponentsRequestFactory(null);
        try {
            ClientHttpRequest request = requestFactory.createRequest(uri, HttpMethod.GET);
            ClientHttpResponse execute = request.execute();
            Assertions.assertEquals(200, execute.getRawStatusCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testApacheHttps() {
        URI uri = URI.create("https://www.baidu.com");
        String passwd = "123456";
        InputStream keystore = ClassLoader.getSystemResourceAsStream("baidu.keystore");
        SSLSocketFactory socketFactory = ClientHttpRequestFactoryUtil.sslSocketFactory(keystore, passwd);
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryUtil.httpComponentsRequestFactory(socketFactory);
        try {
            ClientHttpRequest request = requestFactory.createRequest(uri, HttpMethod.GET);
            ClientHttpResponse execute = request.execute();
            Assertions.assertEquals(200, execute.getRawStatusCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOkHttp() {
        URI uri = URI.create("http://www.baidu.com");
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryUtil.okhttp3RequestFactory(null);
        try {
            ClientHttpRequest request = requestFactory.createRequest(uri, HttpMethod.GET);
            ClientHttpResponse execute = request.execute();
            Assertions.assertEquals(200, execute.getRawStatusCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOkHttps() {
        URI uri = URI.create("https://www.baidu.com");
        String passwd = "123456";
        InputStream keystore = ClassLoader.getSystemResourceAsStream("baidu.keystore");
        SSLSocketFactory socketFactory = ClientHttpRequestFactoryUtil.sslSocketFactory(keystore, passwd);
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryUtil.okhttp3RequestFactory(socketFactory);
        try {
            ClientHttpRequest request = requestFactory.createRequest(uri, HttpMethod.GET);
            ClientHttpResponse execute = request.execute();
            Assertions.assertEquals(200, execute.getRawStatusCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
