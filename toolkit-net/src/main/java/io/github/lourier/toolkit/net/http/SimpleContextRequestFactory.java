package io.github.lourier.toolkit.net.http;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @Description: 可设置超时时间的工厂类
 * @Date: 2023/11/16 16:23
 * @Author: Lourier
 */
public class SimpleContextRequestFactory extends SimpleClientHttpRequestFactory {

    private SSLSocketFactory socketFactory;

    public SimpleContextRequestFactory(SSLSocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        SimpleRequestConfig requestConfig;
        if (socketFactory != null && connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
        }
        if ((requestConfig = HttpContextHolder.get()) != null) {
            connection.setConnectTimeout(requestConfig.getConnectTimeout());
            connection.setReadTimeout(requestConfig.getReadTimeout());
            boolean mayWrite =
                    ("POST".equals(httpMethod) || "PUT".equals(httpMethod) ||
                            "PATCH".equals(httpMethod) || "DELETE".equals(httpMethod));

            connection.setDoInput(true);
            connection.setInstanceFollowRedirects("GET".equals(httpMethod));
            connection.setDoOutput(mayWrite);
            connection.setRequestMethod(httpMethod);
        } else {
            super.prepareConnection(connection, httpMethod);
        }

    }


}
