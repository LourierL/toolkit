package io.github.lourier.toolkit.net.http;

import okhttp3.OkHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @Description: HTTP/HTTPS 客户端工具，支持超时时间设置
 * @see SimpleRequestConfig
 * @see RequestConfig socketTime=readTime
 * @Date: 2023/11/16 16:18
 * @Author: Lourier
 */
public class ClientHttpRequestFactoryUtil {

    public static SSLSocketFactory sslSocketFactory(InputStream keystore, String passwd) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(keystore, passwd.toCharArray());
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
                    .build();
            return sslContext.getSocketFactory();
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException |
                 KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClientHttpRequestFactory simpleHttpRequestFactory(SSLSocketFactory sslSocketFactory) {
        return new SimpleContextRequestFactory(sslSocketFactory);
    }

    public static ClientHttpRequestFactory httpComponentsRequestFactory(SSLSocketFactory socketFactory) {
        HttpComponentsClientHttpRequestFactory requestFactory;
        if (socketFactory == null) {
            requestFactory = new HttpComponentsClientHttpRequestFactory();
        } else {
            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(socketFactory,
                    new String[]{"TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslFactory).build();
            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        }
        requestFactory.setHttpContextFactory(((httpMethod, uri) -> {
            SimpleRequestConfig simpleRequestConfig = HttpContextHolder.get();
            if (simpleRequestConfig != null) {
                HttpClientContext httpClientContext = HttpClientContext.create();
                RequestConfig requestConfig = RequestConfig
                        .custom()
                        .setSocketTimeout(simpleRequestConfig.getReadTimeout())
                        .setConnectTimeout(simpleRequestConfig.getConnectTimeout())
                        .build();
                httpClientContext.setAttribute(HttpClientContext.REQUEST_CONFIG, requestConfig);
                return httpClientContext;
            }
            return null;
        }));
        return requestFactory;
    }

    public static ClientHttpRequestFactory okhttp3RequestFactory(SSLSocketFactory socketFactory) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (socketFactory != null) {
            builder.sslSocketFactory(socketFactory, new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            });
        }
        return new OkHttp3ContextRequestFactory(builder.build());
    }





}
