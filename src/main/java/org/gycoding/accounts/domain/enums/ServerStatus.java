package org.gycoding.accounts.domain.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ServerStatus {
    HOME_NOT_FOUND("API reference not found.", HttpStatus.NOT_FOUND),

    CHAT_NOT_FOUND("Specified chat was not found.", HttpStatus.NOT_FOUND),
    CHAT_EXISTS("Chat already exists.", HttpStatus.CONFLICT),

    MESSAGE_NOT_SENT("Message was not sent.", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_AUTH("Invalid authentication token.", HttpStatus.UNAUTHORIZED),
    USER_NOT_ADMIN("User is not the admin of this chat.", HttpStatus.FORBIDDEN),
    METADATA_NOT_FOUND("Metadata not found.", HttpStatus.NOT_FOUND),

    BAD_REQUEST("The endpoint is malformed.", HttpStatus.BAD_REQUEST),
    DB_ERROR("An error with the database has occurred, sorry for the inconvenience.", HttpStatus.INTERNAL_SERVER_ERROR),
    AUTH_ERROR("An error with the authentication service has occurred, sorry for the inconvenience.", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVER_ERROR("An internal server error has occurred, sorry for the inconvenience.", HttpStatus.INTERNAL_SERVER_ERROR);

    public final String code;
    public final String message;
    public final HttpStatus status;

    private ServerStatus(String message, HttpStatus status) {
        this.code       = this.name();
        this.message    = message;
        this.status     = status;
    }
}