package io.github.lourier.toolkit.net.http;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 使用 RestTemplate，显示请求信息，响应信息
 * @Date: 2023/11/17 11:18
 * @Author: Lourier
 */
@Slf4j
public class LoggingHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @NotNull
    @Override
    public ClientHttpResponse intercept(@NotNull HttpRequest request, @NotNull byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // before request
        displayRequest(request, body);
        return execution.execute(request, body);
    }

    private void displayRequest(HttpRequest request, byte[] body) {
        log.debug("==== request info ====");
        log.debug("URI         : {}", request.getURI());
        log.debug("Method      : {}", request.getMethod());
        log.debug("Req Headers : {}", this.headersToString(request.getHeaders()));
        log.debug("Request body: {}", body == null ? "" : new String(body, StandardCharsets.UTF_8));
    }

    private String headersToString(HttpHeaders httpHeaders) {
        if (Objects.isNull(httpHeaders)) {
            return "[]";
        }
        return httpHeaders.entrySet().stream()
                .map(entry -> {
                    List<String> values = entry.getValue();
                    return "\t" + entry.getKey() + ":" + (values.size() == 1 ?
                            "\"" + values.get(0) + "\"" :
                            values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
                })
                .collect(Collectors.joining(", \n", "\n[\n", "\n]\n"));
    }
}
