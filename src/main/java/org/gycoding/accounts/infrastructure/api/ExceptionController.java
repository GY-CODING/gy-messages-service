package org.gycoding.accounts.infrastructure.api;

import org.gycoding.accounts.domain.exceptions.ChatAPIException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    private final static String EXCEPTION_ORIGIN = "GYCODING";
    private final static String EXCEPTION_FORMAT =
            "{" +
            "\"code\": \"%s\"," +
            "\"message\": \"%s\"," +
            "\"owner\": \"%s\"," +
            "\"status\": %d," +
            '}';

    @ExceptionHandler(ChatAPIException.class)
    public ResponseEntity<String> handleFOTGAPIException(ChatAPIException e) {
        final var response = String.format(EXCEPTION_FORMAT, e.getStatus().getCode(), e.getMessage(), EXCEPTION_ORIGIN, e.getStatus().getStatus().value());

        return new ResponseEntity<>(response, e.getStatus().getStatus());
    }
}

