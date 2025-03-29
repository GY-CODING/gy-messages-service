package org.gycoding.messages.shared.utils.logger;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.gycoding.messages.infrastructure.external.unirest.UnirestFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class Logger {
    @Value("${gy.logs.url}")
    private String initialUrl;

    @Value("${gy.logs.token}")
    private String initialToken;

    private static String url;
    private static String token;

    @PostConstruct
    public void init() {
        url = this.initialUrl;
        token = this.initialToken;
    }

    public static void info(String message, Object data) {
        final var headers = Map.of(
                "Authorization", token
        );
        final var content = String.format("{\"level\": \"%s\", \"message\": \"%s\", \"data\": %s}", LogLevel.INFO, message, data);

        log.info(content);
        UnirestFacade.post(url, headers, content);
    }

    public static void debug(String message, Object data) {
        final var headers = Map.of(
                "Authorization", token
        );
        final var content = String.format("{\"level\": \"%s\", \"message\": \"%s\", \"data\": %s}", LogLevel.DEBUG, message, data);

        log.debug(content);
        UnirestFacade.post(url, headers, content);
    }

    public static void warn(String message, Object data) {
        final var headers = Map.of(
                "Authorization", token
        );
        final var content = String.format("{\"level\": \"%s\", \"message\": \"%s\", \"data\": %s}", LogLevel.WARN, message, data);

        log.warn(content);
        UnirestFacade.post(url, headers, content);
    }

    public static void error(String message, Object data) {
        final var headers = Map.of(
                "Authorization", token
        );
        final var content = String.format("{\"level\": \"%s\", \"message\": \"%s\", \"data\": %s}", LogLevel.ERROR, message, data);

        log.error(content);
        UnirestFacade.post(url, headers, content);
    }
}
